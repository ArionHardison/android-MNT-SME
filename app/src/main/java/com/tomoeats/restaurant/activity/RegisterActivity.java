package com.tomoeats.restaurant.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tomoeats.restaurant.R;
import com.tomoeats.restaurant.countrypicker.Country;
import com.tomoeats.restaurant.countrypicker.CountryPicker;
import com.tomoeats.restaurant.countrypicker.CountryPickerListener;
import com.tomoeats.restaurant.countrypicker.StatusPicker;
import com.tomoeats.restaurant.customviews.PrefixEditText;
import com.tomoeats.restaurant.customviews.SuffixEditText;
import com.tomoeats.restaurant.fragment.CuisineSelectFragment;
import com.tomoeats.restaurant.helper.ConnectionHelper;
import com.tomoeats.restaurant.helper.CustomDialog;
import com.tomoeats.restaurant.helper.GlobalData;
import com.tomoeats.restaurant.helper.SharedHelper;
import com.tomoeats.restaurant.model.AuthToken;
import com.tomoeats.restaurant.model.Cuisine;
import com.tomoeats.restaurant.network.ApiClient;
import com.tomoeats.restaurant.network.ApiInterface;
import com.tomoeats.restaurant.utils.Utils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tomoeats.restaurant.application.MyApplication.ASK_MULTIPLE_PERMISSION_REQUEST_CODE;
import static com.tomoeats.restaurant.utils.TextUtils.isValidEmail;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.country_img)
    ImageView countryImg;
    @BindView(R.id.txt_country_number)
    TextView txtCountryNumber;
    @BindView(R.id.country_picker_lay)
    LinearLayout countryPickerLay;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_eye_img)
    ImageView etPasswordEyeImg;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.et_confirm_password_eye_img)
    ImageView etConfirmPasswordEyeImg;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.address_lay)
    LinearLayout addressLay;
    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.txt_login)
    TextView txtLogin;
    @BindView(R.id.et_landmark)
    EditText etLandmark;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.llStatusPicker)
    LinearLayout llStatusPicker;
    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    boolean isInternet;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    String device_token, device_UDID;
    Utils utils = new Utils();
    String TAG = "RegisterActivity";
    String name, email, password, mobile, confirmPassword, address, landmark,offer_min_amount,offer_percentage,delivery_time,description;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.veg)
    RadioButton veg;
    @BindView(R.id.non_veg)
    RadioButton nonVeg;
    @BindView(R.id.cuisine)
    TextView cuisine;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @BindView(R.id.tvMinAmount)
    PrefixEditText tvMinAmount;

    @BindView(R.id.etOfferInPercentage)
    SuffixEditText etOfferInPercentage;

    @BindView(R.id.tvMaxTimeDelivery)
    SuffixEditText tvMaxTimeDelivery;

    @BindView(R.id.etDescription)
    EditText etDescription;


    private CountryPicker mCountryPicker;
    String country_code;
    LatLng location;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    int CUISINE_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        context = RegisterActivity.this;
        activity = RegisterActivity.this;
        connectionHelper = new ConnectionHelper(context);
        isInternet = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(context);
        getDeviceToken();
        etPasswordEyeImg.setTag(0);
        etConfirmPasswordEyeImg.setTag(0);

        mCountryPicker = CountryPicker.newInstance();
        List<Country> countryList = Country.getAllCountries();
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country s1, Country s2) {
                return s1.getName().compareToIgnoreCase(s2.getName());
            }
        });
        mCountryPicker.setCountriesList(countryList);
        setListener();
        resetData();
    }

    private void resetData() {
        GlobalData.REGISTER_AVATAR=null;
        GlobalData.registerMap.clear();

        GlobalData.email = "";
        GlobalData.password = "";
    }

    public void bindCuisine() {
        StringBuilder cuisneStr = new StringBuilder();
        for (int i=0;i<CuisineSelectFragment.CUISINES.size();i++){
            if(i==0)
                cuisneStr.append(CuisineSelectFragment.CUISINES.get(i).getName());
            else
            cuisneStr.append(",").append(CuisineSelectFragment.CUISINES.get(i).getName());
        }

        cuisine.setText(cuisneStr.toString());
    }

    public void bindStatus(CharSequence s) {
        tvStatus.setText(s);
    }

    private void setListener() {
        mCountryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,
                                        int flagDrawableResID) {
                txtCountryNumber.setText(dialCode);
                countryImg.setImageResource(flagDrawableResID);
                mCountryPicker.dismiss();
            }
        });
        countryPickerLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });
        llStatusPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new StatusPicker().show(getSupportFragmentManager(), "STATUS_PICKER");
            }
        });

        getUserCountryInfo();
    }

    private void getUserCountryInfo() {
        Country country = Country.getCountryFromSIM(context);
        if (country != null) {
            countryImg.setImageResource(country.getFlag());
            txtCountryNumber.setText(country.getDialCode());
            country_code = country.getDialCode();
        } else {
            Country us = new Country("US", "United States", "+1", R.drawable.flag_us);
            countryImg.setImageResource(us.getFlag());
            txtCountryNumber.setText(us.getDialCode());
            country_code = us.getDialCode();
        }
    }

    private void login(HashMap<String, String> map) {
        Call<AuthToken> call = apiInterface.postLogin(map);
        call.enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(@NonNull Call<AuthToken> call, @NonNull Response<AuthToken> response) {
                if (response.body() != null) {
                    SharedHelper.putKey(context, "access_token", response.body().getTokenType() + " " + response.body().getAccessToken());
                    GlobalData.accessToken = SharedHelper.getKey(context, "access_token");

//                    getProfile();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthToken> call, @NonNull Throwable t) {
            }
        });
    }

    public void getDeviceToken() {
        try {
            if (!SharedHelper.getKey(context, "device_token").equals("") && SharedHelper.getKey(context, "device_token") != null) {
                device_token = SharedHelper.getKey(context, "device_token");
                Log.d(TAG, "GCM Registration Token: " + device_token);
            } else {
                device_token = "" + FirebaseInstanceId.getInstance().getToken();
                SharedHelper.putKey(context, "device_token", "" + FirebaseInstanceId.getInstance().getToken());
                Log.d(TAG, "Failed to complete token refresh: " + device_token);
            }
        } catch (Exception e) {
            device_token = "COULD NOT GET FCM TOKEN";
            Log.d(TAG, "Failed to complete token refresh");
        }

        try {
            device_UDID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.d(TAG, "Device UDID:" + device_UDID);
        } catch (Exception e) {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            Log.d(TAG, "Failed to complete device UDID");
        }
    }

    @OnClick({R.id.cuisine, R.id.avatar, R.id.country_picker_lay, R.id.address_lay, R.id.register_btn,
            R.id.txt_login, R.id.et_confirm_password_eye_img, R.id.et_password_eye_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cuisine:
                new CuisineSelectFragment().show(getSupportFragmentManager(), "cuisineSelectFragment");
                break;
            case R.id.avatar:
                galleryIntent();
                break;
            case R.id.country_picker_lay:
                break;
            case R.id.address_lay:
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.getStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.getStackTrace();
                }
                break;
            case R.id.register_btn:
                callRegister();
//                startActivity(new Intent(context, RestaurantTimingActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                finish();
                break;
            case R.id.txt_login:
                startActivity(new Intent(context, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;
            case R.id.et_password_eye_img:
                if (etPasswordEyeImg.getTag().equals(1)) {
                    etPassword.setTransformationMethod(null);
                    etPasswordEyeImg.setImageResource(R.drawable.ic_eye_close);
                    etPasswordEyeImg.setTag(0);
                } else {
                    etPassword.setTransformationMethod(new PasswordTransformationMethod());
                    etPasswordEyeImg.setImageResource(R.drawable.ic_eye_open);
                    etPasswordEyeImg.setTag(1);
                }
                break;
            case R.id.et_confirm_password_eye_img:
                if (etConfirmPasswordEyeImg.getTag().equals(1)) {
                    etConfirmPassword.setTransformationMethod(null);
                    etConfirmPasswordEyeImg.setImageResource(R.drawable.ic_eye_close);
                    etConfirmPasswordEyeImg.setTag(0);
                } else {
                    etConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                    etConfirmPasswordEyeImg.setImageResource(R.drawable.ic_eye_open);
                    etConfirmPasswordEyeImg.setTag(1);
                }
                break;
        }
    }

    private void callRegister() {
        name = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        mobile = etMobile.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPassword = etConfirmPassword.getText().toString().trim();
        address = txtAddress.getText().toString().trim();
        landmark = etLandmark.getText().toString().trim();
        offer_min_amount = tvMinAmount.getText().toString().trim();
        offer_percentage = etOfferInPercentage.getText().toString().trim();
        delivery_time = tvMaxTimeDelivery.getText().toString().trim();
        description = etDescription.getText().toString().trim();

        if (name.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_name));
        else if (email.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_mail_id));
        else if (!isValidEmail(email))
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_valid_mail_id));
        else if (mobile.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_phone_number));
        else if (password.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_password));
        else if (!password.isEmpty() && password.length()<5)
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_minimum_length_password));
        else if (confirmPassword.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_confirm_password));
        else if (!confirmPassword.isEmpty() && confirmPassword.length()<5)
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_minimum_length_password));
        else if (!confirmPassword.equals(password))
            Utils.displayMessage(activity, getResources().getString(R.string.password_and_confirm_password_doesnot_match));
        else if(offer_min_amount.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_amount));
        else if(delivery_time.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_delievery_time));
        else if (address.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_fill_your_address));
        else if (landmark.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_landmark));
        else if (CuisineSelectFragment.CUISINES.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.invalid_cuisine));
        else if (GlobalData.REGISTER_AVATAR == null)
            Utils.displayMessage(activity, getResources().getString(R.string.please_select_avatar));
        else {
            if (isInternet) {
                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("name", RequestBody.create(MediaType.parse("text/plain"), name));
                map.put("email", RequestBody.create(MediaType.parse("text/plain"), email));
                map.put("password", RequestBody.create(MediaType.parse("text/plain"), password));
                map.put("password_confirmation", RequestBody.create(MediaType.parse("text/plain"), confirmPassword));
                map.put("pure_veg", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(veg.isChecked() ? 0 : 1)));
               // map.put("default_banner", RequestBody.create(MediaType.parse("text/plain"), ""));
                map.put("description", RequestBody.create(MediaType.parse("text/plain"), description));
                map.put("offer_min_amount", RequestBody.create(MediaType.parse("text/plain"), offer_min_amount));
                map.put("offer_percent", RequestBody.create(MediaType.parse("text/plain"), offer_percentage));
                map.put("estimated_delivery_time", RequestBody.create(MediaType.parse("text/plain"), delivery_time));
                map.put("phone", RequestBody.create(MediaType.parse("text/plain"), mobile));
                map.put("maps_address", RequestBody.create(MediaType.parse("text/plain"), address));
                map.put("address", RequestBody.create(MediaType.parse("text/plain"), landmark));
                map.put("latitude", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(location.latitude)));
                map.put("longitude", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(location.longitude)));

                //Stored here for login
                GlobalData.email = email;
                GlobalData.password = password;

                for (int i = 0; i < CuisineSelectFragment.CUISINES.size(); i++) {
                    Cuisine obj = CuisineSelectFragment.CUISINES.get(i);
                    map.put("cuisine_id[" + i + "]", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(obj.getId())));
                }

                GlobalData.registerMap.putAll(map);
                startActivity(new Intent(this, RestaurantTimingActivity.class));
            }else{
                Utils.displayMessage(activity, getResources().getString(R.string.oops_no_internet));
            }
        }

    }

    private void galleryIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                EasyImage.openChooserWithDocuments(RegisterActivity.this, "Select", 0);
            else
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        } else EasyImage.openChooserWithDocuments(RegisterActivity.this, "Select", 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            txtAddress.setText(place.getName());
            location = place.getLatLng();
            Log.i(TAG, "Place: " + place.getName());
        }

        if (requestCode == CUISINE_REQUEST_CODE) if (resultCode == 1) {
            // 1 is an arbitrary number, can be any int
            // Now do what you need to do after the dialog dismisses.
        }

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                GlobalData.REGISTER_AVATAR = imageFile;
                Glide
                        .with(context)
                        .load(imageFile)
                        .apply(new RequestOptions()
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher).dontAnimate())
                        .into(avatar);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean permission1 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean permission2 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (permission1 && permission2) galleryIntent();
                    else
                        Toast.makeText(getApplicationContext(), "Please give permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

//    showTimePicker("OPEN");
//    private void showTimePicker(final String type) {
//        Calendar mcurrentTime = Calendar.getInstance();
//        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//        int minute = mcurrentTime.get(Calendar.MINUTE);
//        TimePickerDialog mTimePicker;
//        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                if (type.equalsIgnoreCase("CLOSE"))
//                    txtCloseTime.setText(selectedHour + ":" + selectedMinute);
//                else
//                    txtOpenTime.setText(selectedHour + ":" + selectedMinute);
//
//            }
//        }, hour, minute, true);//Yes 24 hour time
//        mTimePicker.show();
//    }
}
