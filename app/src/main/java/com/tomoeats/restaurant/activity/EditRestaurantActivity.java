package com.tomoeats.restaurant.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.tomoeats.restaurant.R;
import com.tomoeats.restaurant.controller.GetProfile;
import com.tomoeats.restaurant.controller.ProfileListener;
import com.tomoeats.restaurant.countrypicker.Country;
import com.tomoeats.restaurant.countrypicker.CountryPicker;
import com.tomoeats.restaurant.countrypicker.CountryPickerListener;
import com.tomoeats.restaurant.countrypicker.StatusPicker;
import com.tomoeats.restaurant.fragment.CuisineSelectFragment;
import com.tomoeats.restaurant.helper.ConnectionHelper;
import com.tomoeats.restaurant.helper.CustomDialog;
import com.tomoeats.restaurant.helper.GlobalData;
import com.tomoeats.restaurant.model.Cuisine;
import com.tomoeats.restaurant.model.Profile;
import com.tomoeats.restaurant.network.ApiClient;
import com.tomoeats.restaurant.network.ApiInterface;
import com.tomoeats.restaurant.utils.Utils;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tomoeats.restaurant.application.MyApplication.ASK_MULTIPLE_PERMISSION_REQUEST_CODE;
import static com.tomoeats.restaurant.utils.TextUtils.isValidEmail;

public class EditRestaurantActivity extends AppCompatActivity implements ProfileListener{

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
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
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_eye_img)
    ImageView etPasswordEyeImg;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.et_confirm_password_eye_img)
    ImageView etConfirmPasswordEyeImg;
    /*@BindView(R.id.status_spin)
    Spinner statusSpin;*/
    @BindView(R.id.shop_img)
    ImageView shopImg;
    @BindView(R.id.radio_yes)
    RadioButton radioYes;
    @BindView(R.id.radio_no)
    RadioButton radioNo;
    @BindView(R.id.radio_grb)
    RadioGroup radioGrb;
    @BindView(R.id.tvMinAmount)
    EditText tvMinAmount;
    @BindView(R.id.etOfferInPercentage)
    EditText etOfferInPercentage;
    @BindView(R.id.tvMaxTimeDelivery)
    EditText etMaximumDeliveryTime;
    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.address_lay)
    LinearLayout addressLay;
    @BindView(R.id.save_btn)
    Button saveBtn;
    @BindView(R.id.et_landmark)
    EditText etLandmark;
    @BindView(R.id.cuisine)
    TextView cuisine;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.llStatusPicker)
    LinearLayout llStatusPicker;
    @BindView(R.id.lnrPassword)
    LinearLayout lnrPassword;



    ConnectionHelper connectionHelper;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    String name, email, password, mobile, confirmPassword, address, landmark,offer_min_amount,offer_percentage,delivery_time,description;
    String TAG = EditRestaurantActivity.this.getClass().getName();

    LatLng location;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    int CUISINE_REQUEST_CODE = 2;

    private CountryPicker mCountryPicker;
    private int id;

    CustomDialog customDialog;
    double latitude;
    double longitude;
    String country_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);
        ButterKnife.bind(this);
        initViews();
        callProfile();
    }

    private void initViews() {
        title.setText(getResources().getString(R.string.edit_restaurant));
        backImg.setVisibility(View.VISIBLE);
        customDialog = new CustomDialog(EditRestaurantActivity.this);

        connectionHelper = new ConnectionHelper(getApplicationContext());

        mCountryPicker = CountryPicker.newInstance();
        List<Country> countryList = Country.getAllCountries();
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country s1, Country s2) {
                return s1.getName().compareToIgnoreCase(s2.getName());
            }
        });

        mCountryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,
                                        int flagDrawableResID) {
                txtCountryNumber.setText(dialCode);
                countryImg.setImageResource(flagDrawableResID);
                mCountryPicker.dismiss();
            }
        });

        lnrPassword.setVisibility(View.GONE);

        getUserCountryInfo();

        etOfferInPercentage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(etOfferInPercentage.getText().toString().trim().equalsIgnoreCase("")){
                        etOfferInPercentage.setText("0");
                    }
                }
            }
        });

    }

    private void getUserCountryInfo() {
        Country country = Country.getCountryFromSIM(EditRestaurantActivity.this);
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


    private void callProfile() {
        if(connectionHelper.isConnectingToInternet()){
            customDialog.show();
            new GetProfile(apiInterface, EditRestaurantActivity.this);
        }else{
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.oops_no_internet));
        }
    }

    @OnClick({R.id.back_img, R.id.country_picker_lay, R.id.shop_img,  R.id.address_lay, R.id.save_btn,R.id.llStatusPicker,R.id.cuisine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.country_picker_lay:
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
                break;
            case R.id.shop_img:
                galleryIntent();
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
            case R.id.save_btn:
                validateProfile();
                break;
            case R.id.llStatusPicker:
                new StatusPicker().show(getSupportFragmentManager(), "STATUS_PICKER");
                break;

            case R.id.cuisine:
                new CuisineSelectFragment().show(getSupportFragmentManager(), "cuisineSelectFragment");
                break;

        }
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


    private void validateProfile() {
        name = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        mobile = etMobile.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPassword = etConfirmPassword.getText().toString().trim();
        address = txtAddress.getText().toString().trim();
        landmark = etLandmark.getText().toString().trim();
        offer_min_amount = tvMinAmount.getText().toString().trim();
        offer_percentage = etOfferInPercentage.getText().toString().trim();
        delivery_time = etMaximumDeliveryTime.getText().toString().trim();
        description = etDescription.getText().toString().trim();


        if (name.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_name));
        else if (email.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_mail_id));
        else if (!isValidEmail(email))
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_valid_mail_id));
        else if (CuisineSelectFragment.CUISINES.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.invalid_cuisine));
        else if (mobile.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_phone_number));
        /*else if (password.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_password));
        else if (!password.isEmpty() && password.length()<5)
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_minimum_length_password));
        else if (confirmPassword.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_confirm_password));
        else if (!confirmPassword.isEmpty() && confirmPassword.length()<5)
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_minimum_length_password));
        else if (!confirmPassword.equals(password))
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.password_and_confirm_password_doesnot_match));*/
        else if(offer_min_amount.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_amount));
        else if(delivery_time.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_delievery_time));
        else if (address.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_fill_your_address));
        else if (landmark.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_landmark));

        /*else if (GlobalData.REGISTER_AVATAR == null)
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_select_avatar));*/
        else {
            if (connectionHelper.isConnectingToInternet()) {
                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("name", RequestBody.create(MediaType.parse("text/plain"), name));
                map.put("email", RequestBody.create(MediaType.parse("text/plain"), email));
                map.put("password", RequestBody.create(MediaType.parse("text/plain"), password));
                map.put("password_confirmation", RequestBody.create(MediaType.parse("text/plain"), confirmPassword));
                map.put("pure_veg", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(radioYes.isChecked() ? 0 : 1)));
                // map.put("default_banner", RequestBody.create(MediaType.parse("text/plain"), ""));
                map.put("description", RequestBody.create(MediaType.parse("text/plain"), description));
                map.put("offer_min_amount", RequestBody.create(MediaType.parse("text/plain"), offer_min_amount));
                map.put("offer_percent", RequestBody.create(MediaType.parse("text/plain"), offer_percentage));
                map.put("estimated_delivery_time", RequestBody.create(MediaType.parse("text/plain"), delivery_time));
                map.put("phone", RequestBody.create(MediaType.parse("text/plain"), mobile));
                map.put("maps_address", RequestBody.create(MediaType.parse("text/plain"), address));
                map.put("address", RequestBody.create(MediaType.parse("text/plain"), landmark));
                if (location != null) {
                    map.put("latitude", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(location.latitude)));
                    map.put("longitude", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(location.longitude)));
                }else{
                    map.put("latitude", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude)));
                    map.put("longitude", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude)));
                }


                for (int i = 0; i < CuisineSelectFragment.CUISINES.size(); i++) {
                    Cuisine obj = CuisineSelectFragment.CUISINES.get(i);
                    map.put("cuisine_id[" + i + "]", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(obj.getId())));
                }

                MultipartBody.Part filePart = null;
                if (GlobalData.REGISTER_AVATAR != null)
                    filePart = MultipartBody.Part.createFormData("avatar", GlobalData.REGISTER_AVATAR.getName(), RequestBody.create(MediaType.parse("image/*"), GlobalData.REGISTER_AVATAR));

                updateProfile(map,filePart);

            }else{
                Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.oops_no_internet));
            }
        }

    }

    private void updateProfile(HashMap<String, RequestBody> map,MultipartBody.Part filePart) {
        customDialog.show();
        Call<Profile> call = null;
        if(filePart==null)
            call = apiInterface.updateProfile(id,map);
        else
            call = apiInterface.updateProfileWithFile(id,map,filePart);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                customDialog.dismiss();
                if(response.body()!=null){
                    Utils.displayMessage(EditRestaurantActivity.this,getString(R.string.restaurant_updated_successfully));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onBackPressed();
                        }
                    },1000);

                }else{
                    Utils.displayMessage(EditRestaurantActivity.this,getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(EditRestaurantActivity.this, getString(R.string.something_went_wrong));
            }
        });
    }


    @Override
    public void onSuccess(Profile profile) {
        customDialog.dismiss();
        id =profile.getId();
        etName.setText(profile.getName());
        etEmail.setText(profile.getEmail());

        latitude = profile.getLatitude();
        longitude = profile.getLongitude();

        CuisineSelectFragment.CUISINES=profile.getCuisines();
        String strCusine = "";
        for (int i = 0; i < profile.getCuisines().size(); i++) {
            if (i == 0)
                strCusine = profile.getCuisines().get(i).getName();
            else
                strCusine = strCusine + "," + profile.getCuisines().get(i).getName();
        }
        cuisine.setText(strCusine);
        etMobile.setText(profile.getPhone());
        String status = profile.getStatus();


        Glide.with(EditRestaurantActivity.this).load(profile.getAvatar())
                .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image).error(R.drawable.ic_place_holder_image).dontAnimate()).into(shopImg);

        tvMinAmount.setText(""+profile.getOfferMinAmount());
        etOfferInPercentage.setText(""+profile.getOfferPercent());
        etMaximumDeliveryTime.setText(""+profile.getEstimatedDeliveryTime());
        etDescription.setText(profile.getDescription());
        txtAddress.setText(profile.getMapsAddress());
        etLandmark.setText(profile.getAddress());

        if(profile.getPureVeg()==0){
            radioNo.setChecked(true);
        }else{
            radioYes.setChecked(true);
        }
    }

    @Override
    public void onFailure(String error) {
        customDialog.dismiss();
        Utils.displayMessage(EditRestaurantActivity.this, getString(R.string.something_went_wrong));
    }

    public void bindStatus(CharSequence s) {
        tvStatus.setText(s);
    }

    private void galleryIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                EasyImage.openChooserWithDocuments(EditRestaurantActivity.this, "Select", 0);
            else
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        } else EasyImage.openChooserWithDocuments(EditRestaurantActivity.this, "Select", 0);
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
                        .with(EditRestaurantActivity.this)
                        .load(imageFile)
                        .apply(new RequestOptions()
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher).dontAnimate())
                        .into(shopImg);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
    }

}
