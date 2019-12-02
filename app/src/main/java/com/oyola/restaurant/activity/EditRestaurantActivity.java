package com.oyola.restaurant.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.oyola.restaurant.R;
import com.oyola.restaurant.adapter.ImageGalleryAdapter;
import com.oyola.restaurant.controller.GetProfile;
import com.oyola.restaurant.controller.ProfileListener;
import com.oyola.restaurant.countrypicker.Country;
import com.oyola.restaurant.countrypicker.CountryPicker;
import com.oyola.restaurant.countrypicker.CountryPickerListener;
import com.oyola.restaurant.countrypicker.StatusPicker;
import com.oyola.restaurant.fragment.CuisineSelectFragment;
import com.oyola.restaurant.helper.ConnectionHelper;
import com.oyola.restaurant.helper.CustomDialog;
import com.oyola.restaurant.helper.GlobalData;
import com.oyola.restaurant.model.Cuisine;
import com.oyola.restaurant.model.ImageGallery;
import com.oyola.restaurant.model.Profile;
import com.oyola.restaurant.network.ApiClient;
import com.oyola.restaurant.network.ApiInterface;
import com.oyola.restaurant.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

import static com.oyola.restaurant.application.MyApplication.ASK_MULTIPLE_PERMISSION_REQUEST_CODE;
import static com.oyola.restaurant.utils.TextUtils.isValidEmail;

public class EditRestaurantActivity extends AppCompatActivity implements ProfileListener, ImageGalleryAdapter.ImageSelectedListener {

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
    @BindView(R.id.shop_banner)
    ImageView shop_banner;
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
    @BindView(R.id.takeaway)
    CheckBox chkTakeaway;
    @BindView(R.id.delivery)
    CheckBox chkDelivery;
    @BindView(R.id.imageRecyclerView)
    RecyclerView image_rv;
    public static final int PICK_IMAGE_REQUEST = 100;
    ConnectionHelper connectionHelper;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    String name, email, password, mobile, confirmPassword, address, landmark, offer_min_amount, offer_percentage, delivery_time, description;
    String TAG = EditRestaurantActivity.this.getClass().getName();

    LatLng location;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    int CUISINE_REQUEST_CODE = 2;
    CustomDialog customDialog;
    double latitude;
    double longitude;
    String country_code;
    int SHOP_IMAGE = 0;
    int SHOP_BANNER = 1;
    int CT_TYPE = SHOP_IMAGE;
    private CountryPicker mCountryPicker;
    private int id;
    String status;
    boolean mIsDelivery = false;
    boolean mIsTakeaway = false;
    List<String> mRestraurantOffer = new ArrayList<>();
    String mSelectedImageId = "";
    ArrayList<ImageGallery> mImageList = new ArrayList<>();
    ImageGalleryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);
        ButterKnife.bind(this);
        Places.initialize(EditRestaurantActivity.this, getResources().getString(R.string.google_maps_key));
        initViews();
        callProfile();
        customDialog.show();
        getImageGallery();
    }

    private void initViews() {
        title.setText(getResources().getString(R.string.edit_restaurant));
        backImg.setVisibility(View.VISIBLE);
        customDialog = new CustomDialog(this);

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
                if (!hasFocus) {
                    if (etOfferInPercentage.getText().toString().trim().equalsIgnoreCase("")) {
                        etOfferInPercentage.setText("0");
                    }
                }
            }
        });


        etDescription.setOnTouchListener((v, event) -> {
            if (v.getId() == R.id.etDescription) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
            }
            return false;
        });

    }

    private void getUserCountryInfo() {
        Country country = Country.getCountryFromSIM(EditRestaurantActivity.this);
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


    private void callProfile() {
        if (connectionHelper.isConnectingToInternet()) {
            customDialog.show();
            new GetProfile(apiInterface, EditRestaurantActivity.this);
        } else {
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.oops_no_internet));
        }
    }

    @OnClick({R.id.back_img, R.id.country_picker_lay, R.id.shop_img, R.id.address_lay, R.id.save_btn, R.id.llStatusPicker, R.id.cuisine, R.id.shop_banner})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.country_picker_lay:
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
                break;
            case R.id.shop_img:
//                CT_TYPE = SHOP_IMAGE;
//                galleryIntent(SHOP_IMAGE);
                break;
            case R.id.shop_banner:
                CT_TYPE = SHOP_BANNER;
                galleryIntent(SHOP_BANNER);
                break;
            case R.id.address_lay:

                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME);
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
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
        for (int i = 0; i < CuisineSelectFragment.CUISINES.size(); i++)
            if (i == 0)
                cuisneStr.append(CuisineSelectFragment.CUISINES.get(i).getName());
            else
                cuisneStr.append(",").append(CuisineSelectFragment.CUISINES.get(i).getName());

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
        country_code = txtCountryNumber.getText().toString().trim();

        //Setting default values
        if (offer_percentage == null || offer_percentage.isEmpty() || offer_percentage.equalsIgnoreCase("null")) {
            offer_percentage = "0";
        }
        mRestraurantOffer = new ArrayList<>();
        mRestraurantOffer.clear();
        if (chkTakeaway.isChecked()) {
            mRestraurantOffer.add("Takeaway");
        }
        if (chkDelivery.isChecked()) {
            mRestraurantOffer.add("Delivery");
        }

        if (name.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_name));
        else if (email.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_mail_id));
        else if (!isValidEmail(email))
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_valid_mail_id));
        else if (CuisineSelectFragment.CUISINES.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.invalid_cuisine));
        else if (mobile.isEmpty() || mobile.length() != 10)
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_phone_number));
        else if (mRestraurantOffer.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_select_offer));
        else if (offer_min_amount.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_amount));
        else if (delivery_time.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.please_enter_delievery_time));
        else if (description.isEmpty())
            Utils.displayMessage(EditRestaurantActivity.this, getString(R.string.please_enter_description));
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
                map.put("pure_veg", RequestBody.create(MediaType.parse("text/plain"), radioYes.isChecked() ? "1" : "0"));
                map.put("description", RequestBody.create(MediaType.parse("text/plain"), description));
                map.put("offer_min_amount", RequestBody.create(MediaType.parse("text/plain"), offer_min_amount));
                map.put("offer_percent", RequestBody.create(MediaType.parse("text/plain"), offer_percentage));
                map.put("estimated_delivery_time", RequestBody.create(MediaType.parse("text/plain"), delivery_time));
                map.put("phone", RequestBody.create(MediaType.parse("text/plain"), mobile));
                map.put("maps_address", RequestBody.create(MediaType.parse("text/plain"), address));
                map.put("address", RequestBody.create(MediaType.parse("text/plain"), landmark));
                map.put("country_code", RequestBody.create(MediaType.parse("text/plain"), country_code));
                map.put("image_gallery_id", RequestBody.create(MediaType.parse("text/plain"),mSelectedImageId));
                if (tvStatus.getText().toString().equalsIgnoreCase("onboarding")) {
                    status = "onboarding";
                } else if (tvStatus.getText().toString().equalsIgnoreCase("banned")) {
                    status = "banned";
                } else if (tvStatus.getText().toString().equalsIgnoreCase("active")) {
                    status = "active";
                }
                map.put("status", RequestBody.create(MediaType.parse("text/plain"), status));

                if (location != null) {
                    map.put("latitude", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(location.latitude)));
                    map.put("longitude", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(location.longitude)));
                } else {
                    map.put("latitude", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude)));
                    map.put("longitude", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude)));
                }


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

                MultipartBody.Part filePart1 = null;
                if (GlobalData.REGISTER_AVATAR != null) {
                   /* try {
                        GlobalData.REGISTER_AVATAR = new Compressor(this).compressToFile(GlobalData.REGISTER_AVATAR);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    filePart1 = MultipartBody.Part.createFormData("avatar", GlobalData.REGISTER_AVATAR.getName(), RequestBody.create(MediaType.parse("image/*"), GlobalData.REGISTER_AVATAR));
                }

                MultipartBody.Part filePart2 = null;
                if (GlobalData.REGISTER_SHOP_BANNER != null) {
                   /* try {
                        GlobalData.REGISTER_SHOP_BANNER = new Compressor(this).compressToFile(GlobalData.REGISTER_SHOP_BANNER);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    filePart2 = MultipartBody.Part.createFormData("default_banner", GlobalData.REGISTER_SHOP_BANNER.getName(), RequestBody.create(MediaType.parse("image/*"), GlobalData.REGISTER_SHOP_BANNER));
                }

                updateProfile(map, filePart1, filePart2);

            } else {
                Utils.displayMessage(EditRestaurantActivity.this, getResources().getString(R.string.oops_no_internet));
            }
        }

    }

    private void updateProfile(HashMap<String, RequestBody> map, MultipartBody.Part filePart1, MultipartBody.Part filePart2) {
        if (customDialog != null && !isDestroyed())
            customDialog.show();
        Call<Profile> call = null;
        if (filePart1 == null && filePart2 == null)
            call = apiInterface.updateProfile(id, map);
        else
            call = apiInterface.updateProfileWithFile(id, map, filePart1, filePart2);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                customDialog.dismiss();
                if (response.body() != null) {
                    Utils.displayMessage(EditRestaurantActivity.this, getString(R.string.restaurant_updated_successfully));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            onBackPressed();
                            startActivity(new Intent(EditRestaurantActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        }
                    }, 1000);

                } else {
                    if (response.code() == 401) {
                        startActivity(new Intent(EditRestaurantActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    }
                    Utils.displayMessage(EditRestaurantActivity.this, getString(R.string.something_went_wrong));
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
        id = profile.getId();
        etName.setText(profile.getName());
        etEmail.setText(profile.getEmail());

        latitude = profile.getLatitude();
        longitude = profile.getLongitude();

        CuisineSelectFragment.CUISINES = profile.getCuisines();
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

        if (profile.getAvatar() != null)
            Glide.with(EditRestaurantActivity.this).load(profile.getAvatar())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image).error(R.drawable.ic_place_holder_image).dontAnimate()).into(shopImg);

        if (profile.getDefaultBanner() != null)
            Glide.with(EditRestaurantActivity.this).load(profile.getDefaultBanner())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image).error(R.drawable.ic_place_holder_image).dontAnimate()).into(shop_banner);

        tvMinAmount.setText("" + profile.getOfferMinAmount());
        etOfferInPercentage.setText("" + profile.getOfferPercent());
        etMaximumDeliveryTime.setText("" + profile.getEstimatedDeliveryTime());
        etDescription.setText(profile.getDescription());
        txtAddress.setText(profile.getMapsAddress());
        etLandmark.setText(profile.getAddress());
        if (profile.getImageGalleyId() != null) {
            mSelectedImageId = String.valueOf(profile.getImageGalleyId());
        }
        if (profile.getPureVeg() == 1) {
            radioYes.setChecked(true);
            radioNo.setChecked(false);
        } else {
            radioYes.setChecked(false);
            radioNo.setChecked(true);
        }
        if (profile.getDeliveryOptionList() != null) {
            mRestraurantOffer = new ArrayList<>();
            mRestraurantOffer.clear();
            for (int i = 0; i < profile.getDeliveryOptionList().size(); i++) {
                mRestraurantOffer.add(profile.getDeliveryOptionList().get(i).getName());
                if (profile.getDeliveryOptionList().get(i).getName().equalsIgnoreCase("Delivery")) {
                    mIsDelivery = true;
                }
                if (profile.getDeliveryOptionList().get(i).getName().equalsIgnoreCase("Takeaway")) {
                    mIsTakeaway = true;
                }
            }
            chkDelivery.setChecked(mIsDelivery);
            chkTakeaway.setChecked(mIsTakeaway);
        }
        if (profile.getCountry_code() != null && !profile.getCountry_code().equals("null")) {
            Country country = new Country();
            country = country.getCountryByDialCode(profile.getCountry_code());
            txtCountryNumber.setText(country.getDialCode());
            countryImg.setImageResource(country.getFlag());
            country_code = country.getDialCode();
        }
        if (profile.getStatus() != null && !profile.getStatus().isEmpty()) {

            tvStatus.setText(profile.getStatus());
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

    private void galleryIntent(Integer type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                EasyImage.openChooserWithDocuments(EditRestaurantActivity.this, "Select", type);
            else
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        } else EasyImage.openChooserWithDocuments(EditRestaurantActivity.this, "Select", type);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) if (resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            txtAddress.setText(place.getName());
            location = place.getLatLng();
            Log.i(TAG, "Place: " + place.getName());
        }

        if (requestCode == CUISINE_REQUEST_CODE) if (resultCode == 1) {
            // 1 is an arbitrary number, can be any int
            // Now do what you need to do after the dialog dismisses.
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            mSelectedImageId = data.getExtras().getString("image_id");
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
                        GlobalData.REGISTER_AVATAR = new id.zelory.compressor.Compressor(EditRestaurantActivity.this).compressToFile(imageFiles.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Glide.with(EditRestaurantActivity.this)
                            .load(imageFiles.get(0))
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.ic_place_holder_image)
                                    .error(R.drawable.ic_place_holder_image).dontAnimate())
                            .into(shopImg);
                } else if (type == SHOP_BANNER) {
//                    GlobalData.REGISTER_SHOP_BANNER = imageFiles.get(0);
                    try {
                        GlobalData.REGISTER_SHOP_BANNER = new id.zelory.compressor.Compressor(EditRestaurantActivity.this).compressToFile(imageFiles.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Glide.with(EditRestaurantActivity.this)
                            .load(imageFiles.get(0))
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.ic_place_holder_image)
                                    .error(R.drawable.ic_place_holder_image).dontAnimate())
                            .into(shop_banner);
                }
            }


            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
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
                Utils.displayMessage(EditRestaurantActivity.this, getString(R.string.something_went_wrong));
            }
        });

    }

    private void setupAdapter() {
        List<ImageGallery> mGalleryList;
        if (mImageList.size() > 7) {
            mGalleryList = mImageList.subList(0, 7);
        } else {
            mGalleryList = mImageList;
        }
        mAdapter = new ImageGalleryAdapter(mGalleryList, this, this, true);
        image_rv.setLayoutManager(new GridLayoutManager(this, 4));
        image_rv.setHasFixedSize(true);
        image_rv.setAdapter(mAdapter);
    }


    @Override
    public void onImageSelected(ImageGallery mGallery) {
        mSelectedImageId = String.valueOf(mGallery.getId());
    }

    @Override
    public void navigateToImageScreen() {
        Intent intent = new Intent(EditRestaurantActivity.this, ImageGalleryActivity.class);
        intent.putExtra("image_list", mImageList);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
}
