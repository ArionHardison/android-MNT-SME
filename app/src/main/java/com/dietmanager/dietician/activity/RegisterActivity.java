package com.dietmanager.dietician.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.iid.FirebaseInstanceId;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.ImageGalleryAdapter;
import com.dietmanager.dietician.countrypicker.Country;
import com.dietmanager.dietician.countrypicker.CountryPicker;
import com.dietmanager.dietician.countrypicker.StatusPicker;
import com.dietmanager.dietician.customviews.SuffixEditText;
import com.dietmanager.dietician.fragment.CuisineSelectFragment;
import com.dietmanager.dietician.helper.ConnectionHelper;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.helper.SharedHelper;
import com.dietmanager.dietician.model.Cuisine;
import com.dietmanager.dietician.model.ImageGallery;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dietmanager.dietician.application.MyApplication.ASK_MULTIPLE_PERMISSION_REQUEST_CODE;
import static com.dietmanager.dietician.utils.TextUtils.isValidEmail;
import static com.dietmanager.dietician.utils.TextUtils.isValidPassword;

public class RegisterActivity extends AppCompatActivity implements ImageGalleryAdapter.ImageSelectedListener {

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
    /*@BindView(R.id.et_landmark)
    EditText etLandmark;*/
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.llStatusPicker)
    LinearLayout llStatusPicker;
    @BindView(R.id.takeaway)
    CheckBox takeaway;
    @BindView(R.id.delivery)
    CheckBox delivery;
    @BindView(R.id.halal)
    CheckBox halal;
    @BindView(R.id.free_delivery)
    CheckBox freeDelivery;
    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    boolean isInternet;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    String device_token, device_UDID;
    Utils utils = new Utils();
    String TAG = "RegisterActivity";
    String name, email, password, mobile, confirmPassword, address, /*landmark,*/ offer_min_amount, offer_percentage, delivery_time, description;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.shop_image)
    ImageView shop_image;
    @BindView(R.id.veg)
    RadioButton veg;
    @BindView(R.id.non_veg)
    RadioButton nonVeg;
    @BindView(R.id.cuisine)
    TextView cuisine;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @BindView(R.id.tvMinAmount)
    EditText tvMinAmount;

    @BindView(R.id.shop_img)
    ImageView shopImage;

    @BindView(R.id.shop_banner)
    ImageView shopBanner;

    @BindView(R.id.etOfferInPercentage)
    SuffixEditText etOfferInPercentage;

    @BindView(R.id.tvMaxTimeDelivery)
    SuffixEditText tvMaxTimeDelivery;

    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.imageShopRecyclerView)
    RecyclerView shopImageRV;
    @BindView(R.id.imageBannerRecyclerView)
    RecyclerView bannerImageRV;
    String country_code;
    LatLng location;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    int PICK_IMAGE_REQUEST = 100;
    int CUISINE_REQUEST_CODE = 2;
    int SHOP_IMAGE = 0;
    int SHOP_BANNER = 1;
    int CT_TYPE = SHOP_IMAGE;
    private CountryPicker mCountryPicker;
    String status;
    String mSelectedShopImageId,mSelectedShopImageUrl = "";
    String mSelectedBannerImageId,mSelectedBannerImageUrl = "";
    List<String> mRestraurantOffer = new ArrayList<>();
    ArrayList<ImageGallery> mImageList = new ArrayList<>();
    ImageGalleryAdapter mShopAdapter,mBannerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        Places.initialize(RegisterActivity.this, getResources().getString(R.string.google_maps_key));

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
        Collections.sort(countryList, (s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
        mCountryPicker.setCountriesList(countryList);
        setListener();
        resetData();

        etOfferInPercentage.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && etOfferInPercentage.getText().toString().trim().isEmpty())
                etOfferInPercentage.setText("0");
        });
        tvStatus.setText("Active");
        customDialog.show();
        getImageGallery();

    }

    private void resetData() {
        GlobalData.REGISTER_AVATAR = null;
        GlobalData.REGISTER_SHOP_BANNER = null;
        GlobalData.registerMap.clear();

        GlobalData.email = "";
        GlobalData.password = "";
    }

    public void bindCuisine() {
        StringBuilder cuisneStr = new StringBuilder();
        for (int i = 0; i < CuisineSelectFragment.CUISINES.size(); i++) {
            if (i == 0)
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
        mCountryPicker.setListener((name, code, dialCode, flagDrawableResID) -> {
            txtCountryNumber.setText(dialCode);
            countryImg.setImageResource(flagDrawableResID);
            mCountryPicker.dismiss();
        });
        countryPickerLay.setOnClickListener(v -> mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER"));
        llStatusPicker.setOnClickListener(v -> new StatusPicker().show(getSupportFragmentManager(), "STATUS_PICKER"));
        getUserCountryInfo();
    }

    private void getUserCountryInfo() {
        Country country = Country.getCountryFromSIM(context);
        if (country != null) {
            countryImg.setImageResource(country.getFlag());
            txtCountryNumber.setText(country.getDialCode());
            country_code = country.getDialCode();
        } else {
            Country mCountry = new Country("AU", "Australia", "+61", R.drawable.flag_au);
            countryImg.setImageResource(mCountry.getFlag());
            txtCountryNumber.setText(mCountry.getDialCode());
            country_code = mCountry.getDialCode();
        }
    }

    public void getDeviceToken() {
        try {
            if (!SharedHelper.getKey(context, "device_token").equals("") && SharedHelper.getKey(context, "device_token") != null) {
                device_token = SharedHelper.getKey(context, "device_token");
                Log.d(TAG, "GCM Registration Token: " + device_token);
            } else {
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        return;
                    }
                    device_token = ""+ Objects.requireNonNull(task.getResult()).getToken();
                    SharedHelper.putKey(context, "device_token", device_token);
                });
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

    private void setupAdapter() {
        List<ImageGallery> mGalleryList;
        if (mImageList.size() > 7) {
            mGalleryList = mImageList.subList(0, 7);
        } else {
            mGalleryList = mImageList;
        }
        mShopAdapter = new ImageGalleryAdapter(mGalleryList, context, this, true,false);
        shopImageRV.setLayoutManager(new GridLayoutManager(context, 4));
        shopImageRV.setHasFixedSize(true);
        shopImageRV.setAdapter(mShopAdapter);
        mBannerAdapter = new ImageGalleryAdapter(mGalleryList, context, this, true,true);
        bannerImageRV.setLayoutManager(new GridLayoutManager(context, 4));
        bannerImageRV.setHasFixedSize(true);
        bannerImageRV.setAdapter(mBannerAdapter);
    }

    @OnClick({R.id.cuisine, R.id.avatar, R.id.country_picker_lay, R.id.address_lay, R.id.register_btn,
            R.id.txt_login, R.id.et_confirm_password_eye_img, R.id.et_password_eye_img, R.id.shop_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cuisine:
                new CuisineSelectFragment().show(getSupportFragmentManager(), "cuisineSelectFragment");
                break;
            case R.id.avatar:
                CT_TYPE = SHOP_IMAGE;
                galleryIntent(SHOP_IMAGE);
                break;

            case R.id.shop_image:
                CT_TYPE = SHOP_BANNER;
                galleryIntent(SHOP_BANNER);
                break;
            case R.id.country_picker_lay:
                break;
            case R.id.address_lay:
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS);
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.
                        IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                //
                break;
            case R.id.register_btn:
                signupCall();
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

    private void signupCall() {
        name = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        mobile = etMobile.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPassword = etConfirmPassword.getText().toString().trim();
        address = txtAddress.getText().toString().trim();
//        landmark = etLandmark.getText().toString().trim();
        offer_min_amount = tvMinAmount.getText().toString().trim();
        offer_percentage = etOfferInPercentage.getText().toString().trim();
        delivery_time = tvMaxTimeDelivery.getText().toString().trim();
        description = etDescription.getText().toString().trim();
        country_code = txtCountryNumber.getText().toString().trim();

        mRestraurantOffer = new ArrayList<>();
        mRestraurantOffer.clear();
        if (takeaway.isChecked()) {
            mRestraurantOffer.add("Takeaway");
        }
        if (delivery.isChecked()) {
            mRestraurantOffer.add("Delivery");
        }
        if (offer_percentage == null || offer_percentage.isEmpty() || offer_percentage.equalsIgnoreCase("null"))
            offer_percentage = "0";

        if (name.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_name));
        else if (email.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_mail_id));
        else if (!isValidEmail(email))
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_valid_mail_id));
        else if (CuisineSelectFragment.CUISINES.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.invalid_cuisine));
        else if (mobile.isEmpty() || mobile.length() != 10)
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_phone_number));
        else if (password.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_password));
//        else if (!password.isEmpty() && password.length() < 6)
//            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_minimum_length_password));
        else if (!isValidPassword(password))
            Utils.displayMessage(activity, getResources().getString(R.string.password_validation_text));
        else if (confirmPassword.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_confirm_password));
        else if (!isValidPassword(confirmPassword))
            Utils.displayMessage(activity, getResources().getString(R.string.password_validation_text));
//        else if (!confirmPassword.isEmpty() && confirmPassword.length() < 6)
//            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_minimum_length_password));
        else if (!confirmPassword.equals(password))
            Utils.displayMessage(activity, getResources().getString(R.string.password_and_confirm_password_doesnot_match));
//        else if (GlobalData.REGISTER_AVATAR == null)
//            Utils.displayMessage(activity, getResources().getString(R.string.please_select_avatar));
        else if (offer_min_amount.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_amount));
        else if (delivery_time.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_delievery_time));
        else if (description.isEmpty())
            Utils.displayMessage(activity, getString(R.string.please_enter_description));
        else if (address.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_fill_your_address));
        /*else if (landmark.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_landmark));*/
        else if (tvStatus.getText().toString().isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_select_status));
        else if (mRestraurantOffer.isEmpty())
            Utils.displayMessage(activity, getResources().getString(R.string.please_select_offer));
        else {
            if (isInternet) {
                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("name", RequestBody.create(MediaType.parse("text/plain"), name));
                map.put("email", RequestBody.create(MediaType.parse("text/plain"), email));
                map.put("password", RequestBody.create(MediaType.parse("text/plain"), password));
                map.put("password_confirmation", RequestBody.create(MediaType.parse("text/plain"), confirmPassword));
                map.put("pure_veg", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(veg.isChecked() ? 1 : 0)));
                // map.put("default_banner", RequestBody.create(MediaType.parse("text/plain"), ""));
                map.put("description", RequestBody.create(MediaType.parse("text/plain"), description));
                map.put("offer_min_amount", RequestBody.create(MediaType.parse("text/plain"), offer_min_amount));
                map.put("offer_percent", RequestBody.create(MediaType.parse("text/plain"), offer_percentage));
                map.put("estimated_delivery_time", RequestBody.create(MediaType.parse("text/plain"), delivery_time));
                map.put("phone", RequestBody.create(MediaType.parse("text/plain"), mobile));
                map.put("maps_address", RequestBody.create(MediaType.parse("text/plain"), address));
//                map.put("address", RequestBody.create(MediaType.parse("text/plain"), landmark));
                map.put("latitude", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(location.latitude)));
                map.put("longitude", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(location.longitude)));
                map.put("country_code", RequestBody.create(MediaType.parse("text/plain"), country_code));
                /*if (tvStatus.getText().toString().equalsIgnoreCase("onboarding")) {
                    status = "onboarding";
                } else if (tvStatus.getText().toString().equalsIgnoreCase("banned")) {
                    status = "banned";
                } else if (tvStatus.getText().toString().equalsIgnoreCase("active")) {
                    status = "active";
                }
                map.put("status", RequestBody.create(MediaType.parse("text/plain"), status));*/
             /*   map.put("image_gallery_id", RequestBody.create(MediaType.parse("text/plain"), mSelectedShopImageId));
                map.put("image_banner_id", RequestBody.create(MediaType.parse("text/plain"), mSelectedBannerImageId));*/
                map.put("image_gallery_img", RequestBody.create(MediaType.parse("text/plain"), mSelectedShopImageUrl));
                map.put("image_banner_img", RequestBody.create(MediaType.parse("text/plain"), mSelectedBannerImageUrl));
                map.put("halal", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(halal.isChecked() ? 1 : 0)));
                map.put("free_delivery", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(freeDelivery.isChecked() ? 1 : 0)));

//                if (halal.isChecked()) {
//                    map.put("halal", RequestBody.create(MediaType.parse("text/plain"), "1"));
//                } else {
//                    map.put("halal", RequestBody.create(MediaType.parse("text/plain"), "0"));
//                }
                //Stored here for login
                GlobalData.email = email;
                GlobalData.password = password;

                for (int i = 0; i < CuisineSelectFragment.CUISINES.size(); i++) {
                    Cuisine obj = CuisineSelectFragment.CUISINES.get(i);
                    map.put("cuisine_id[" + i + "]", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(obj.getId())));
                }
                for (int i = 0; i < mRestraurantOffer.size(); i++) {
                    if (mRestraurantOffer.get(i).equalsIgnoreCase("Takeaway")) {
                        map.put("i_offer[" + i + "]", RequestBody.create(MediaType.parse("text/plain"), "1"));
                    } else if (mRestraurantOffer.get(i).equalsIgnoreCase("Delivery")) {
                        map.put("i_offer[" + i + "]", RequestBody.create(MediaType.parse("text/plain"), "2"));
                    }
                }

                GlobalData.registerMap.putAll(map);
                startActivity(new Intent(this, RestaurantTimingActivity.class));
            } else {
                Utils.displayMessage(activity, getResources().getString(R.string.oops_no_internet));
            }
        }

    }

    private void galleryIntent(Integer type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                EasyImage.openChooserWithDocuments(RegisterActivity.this, "Select", type);
            else
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        } else EasyImage.openChooserWithDocuments(RegisterActivity.this, "Select", type);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) if (resultCode == RESULT_OK) {
//            Place place = PlaceAutocomplete.getPlace(this, data);
            Place place = Autocomplete.getPlaceFromIntent(data);
            if (place.getAddress().toString().contains(place.getName().toString())) {
                txtAddress.setText(place.getAddress());
            } else {
                txtAddress.setText(place.getName() + ", " + place.getAddress());
            }
            location = place.getLatLng();
            Log.i(TAG, "Place: " + place.getName());
        }

        if (requestCode == CUISINE_REQUEST_CODE) if (resultCode == 1) {
            // 1 is an arbitrary number, can be any int
            // Now do what you need to do after the dialog dismisses.
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getExtras().getBoolean("is_featured")) {
//                mSelectedBannerImageId = data.getExtras().getString("image_id");
                mSelectedBannerImageUrl = data.getExtras().getString("image_url");
                Glide.with(this).load(mSelectedBannerImageUrl)
                        .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image)
                                .error(R.drawable.ic_place_holder_image).dontAnimate()).into(shopBanner);
            } else {
//                mSelectedShopImageId = data.getExtras().getString("image_id");
                mSelectedShopImageUrl = data.getExtras().getString("image_url");
                Glide.with(this).load(mSelectedShopImageUrl)
                        .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image)
                                .error(R.drawable.ic_place_holder_image).dontAnimate()).into(shopImage);
            }
        }

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {

                if (type == SHOP_IMAGE) {
//                    GlobalData.REGISTER_AVATAR = imageFiles.get(0);
                    try {
                        GlobalData.REGISTER_AVATAR = new Compressor(context).compressToFile(imageFiles.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Glide.with(context)
                            .load(imageFiles.get(0))
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.ic_place_holder_image)
                                    .error(R.drawable.ic_place_holder_image).dontAnimate())
                            .into(avatar);
                } else if (type == SHOP_BANNER) {
//                    GlobalData.REGISTER_SHOP_BANNER = imageFiles.get(0);
                    try {
                        GlobalData.REGISTER_SHOP_BANNER = new Compressor(context).compressToFile(imageFiles.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Glide
                            .with(context)
                            .load(imageFiles.get(0))
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.ic_place_holder_image)
                                    .error(R.drawable.ic_place_holder_image).dontAnimate())
                            .into(shop_image);
                }
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
                    if (permission1 && permission2) galleryIntent(CT_TYPE);
                    else
                        Toast.makeText(getApplicationContext(), "Please give permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onImageSelected(ImageGallery mGallery,boolean isFeatured) {
        if (isFeatured) {
//            mSelectedBannerImageId = String.valueOf(mGallery.getId());
            mSelectedBannerImageUrl = mGallery.getImage();
            Glide.with(this).load(mSelectedBannerImageUrl)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image)
                            .error(R.drawable.ic_place_holder_image).dontAnimate()).into(shopImage);
        } else {
//            mSelectedShopImageId = String.valueOf(mGallery.getId());
            mSelectedShopImageUrl = mGallery.getImage();
            Glide.with(this).load(mSelectedShopImageUrl)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image)
                            .error(R.drawable.ic_place_holder_image).dontAnimate()).into(shopImage);
        }
    }

    @Override
    public void navigateToImageScreen(boolean isFeatured) {
        Intent intent = new Intent(context, ImageGalleryActivity.class);
        intent.putExtra("image_list", mImageList);
        intent.putExtra("is_featured", isFeatured);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void getImageGallery() {
        Call<List<Cuisine>> call = apiInterface.getImages();
        call.enqueue(new Callback<List<Cuisine>>() {
            @Override
            public void onResponse(@NonNull Call<List<Cuisine>> call, @NonNull Response<List<Cuisine>> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        mImageList = new ArrayList<>();
                        for (int i = 0; i < response.body().size(); i++) {
                            if (response.body().get(i).getImageGallery() != null) {
                                mImageList.addAll(response.body().get(i).getImageGallery());
                            }
                        }
                        setupAdapter();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Cuisine>> call, @NonNull Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(RegisterActivity.this, getString(R.string.something_went_wrong));
            }
        });

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
