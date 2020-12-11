package com.dietmanager.dietician.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.config.AppConfigure;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.helper.SharedHelper;
import com.dietmanager.dietician.model.Profile;
import com.dietmanager.dietician.model.ProfileError;
import com.dietmanager.dietician.network.APIError;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.network.ErrorUtils;
import com.dietmanager.dietician.utils.LocaleUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 0;
    @BindView(R.id.user_avatar)
    CircleImageView userAvatar;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.user_id)
    EditText userId;
    @BindView(R.id.mobile_no)
    EditText mobileNo;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.experience)
    EditText experience;
    @BindView(R.id.title)
    TextView title;
/*    @BindView(R.id.fb_link)
    EditText fb_link;
    @BindView(R.id.twitter_link)
    EditText twitter_link;
    @BindView(R.id.flickr_link)
    EditText flickr_link;*/
    @BindView(R.id.update)
    Button update;
    @BindView(R.id.dob)
    TextView dob;
    CustomDialog customDialog;
    @BindView(R.id.back_img)
    ImageView back;
    @BindView(R.id.etTitle)
    TextView etTitle;
    @BindView(R.id.etDescription)
    TextView etDescription;
    @BindView(R.id.upload_profile)
    ImageView uploadProfile;
    File imgFile;
    String device_token, device_UDID;
    private int PICK_IMAGE_REQUEST = 1;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    String selectedDob="";
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        back.setVisibility(View.VISIBLE);
        getDeviceToken();
        title.setText(getResources().getString(R.string.profile));
        customDialog = new CustomDialog(EditProfileActivity.this);
        String dd = SharedHelper.getKey(this, "language");
        switch (dd) {
            case "English":
                LocaleUtils.setLocale(this, "en");
                break;
            case "Japanese":
                LocaleUtils.setLocale(this, "ja");
                break;
            default:
                LocaleUtils.setLocale(this, "en");
                break;
        }
        dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                selectedDob=year + "-" +(monthOfYear + 1) +"-" +dayOfMonth;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                dob.setText(sdf.format(myCalendar.getTime()));
            }

        };
        initView();
        getProfile();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    private void openDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        //dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

    }
    private void initView() {
        if (GlobalData.profile != null) {
            name.setText(GlobalData.profile.getName());
            email.setText(GlobalData.profile.getEmail());
            mobileNo.setText(GlobalData.profile.getPhone());
            userId.setText(String.valueOf(GlobalData.profile.getId()));
            if(GlobalData.profile.getDob()!=null&& !GlobalData.profile.getDob().equals("")) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                DateFormat dateStringFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dateFormat.parse(GlobalData.profile.getDob());
                    selectedDob = dateStringFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                };
            }
            dob.setText(selectedDob);
            experience.setText(GlobalData.profile.getExperience());
            etTitle.setText(GlobalData.profile.getTitle());
            etDescription.setText(GlobalData.profile.getDescription());
/*            fb_link.setText(GlobalData.profile.getFb_link());
            twitter_link.setText(GlobalData.profile.getTwitter_link());
            flickr_link.setText(GlobalData.profile.getFlickr_link());*/
            Glide.with(this)
                    .load(AppConfigure.BASE_URL+GlobalData.profile.getAvatar())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(userAvatar);
            SharedHelper.putKey(this, "currency_code", GlobalData.profile.getCurrency());
        }
    }

    private void getProfile() {
        HashMap<String, String> map = new HashMap<>();
        map.put("device_type", "android");
        map.put("device_id", device_UDID);
        map.put("device_token", device_token);
        String header = SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this,
                "access_token");
        System.out.println("getProfile Header " + header);
        Call<Profile> call = apiInterface.getProfile(map);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call,
                                   @NonNull Response<Profile> response) {
                if (response.isSuccessful()) {
                    GlobalData.profile = response.body();
                    initView();
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    if (response.code() == 401) {
                        SharedHelper.putKey(EditProfileActivity.this, "logged_in", "0");
                        startActivity(new Intent(EditProfileActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                Toast.makeText(EditProfileActivity.this, R.string.something_went_wrong,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateProfile() {

        if (name.getText().toString().isEmpty()) {
            Toast.makeText(this, "The name field is required.", Toast.LENGTH_SHORT).show();
            return;
        } else if (email.getText().toString().isEmpty()) {
            Toast.makeText(this, "The email field is required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (customDialog != null)
            customDialog.show();

        /*HashMap<String, String> map = new HashMap<>();
        map.put("name", name.getText().toString());
        map.put("email", email.getText().toString());*/

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("name", RequestBody.create(MediaType.parse("text/plain"),
                name.getText().toString()));
        map.put("email", RequestBody.create(MediaType.parse("text/plain"),
                email.getText().toString()));
        map.put("mobile", RequestBody.create(MediaType.parse("text/plain"),
                mobileNo.getText().toString()));
        map.put("dob", RequestBody.create(MediaType.parse("text/plain"),
                selectedDob));
        map.put("experience", RequestBody.create(MediaType.parse("text/plain"),
                experience.getText().toString()));
        map.put("title", RequestBody.create(MediaType.parse("text/plain"),
                etTitle.getText().toString()));
        map.put("description", RequestBody.create(MediaType.parse("text/plain"),
                etDescription.getText().toString()));
/*        map.put("fb_link", RequestBody.create(MediaType.parse("text/plain"),
                fb_link.getText().toString()));
        map.put("twitter_link", RequestBody.create(MediaType.parse("text/plain"),
                twitter_link.getText().toString()));
        map.put("flickr_link", RequestBody.create(MediaType.parse("text/plain"),
                flickr_link.getText().toString()));*/
        MultipartBody.Part filePart = null;

        if (imgFile != null)
            filePart = MultipartBody.Part.createFormData("avatar", imgFile.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgFile));

        String header = SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this,
                "access_token");
        Call<Profile> call = apiInterface.updateProfileWithImage(map, filePart);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call,
                                   @NonNull Response<Profile> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    GlobalData.profile = response.body();
                    initView();
                    Toast.makeText(EditProfileActivity.this,
                            getResources().getString(R.string.success_profile),
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Gson gson = new GsonBuilder().create();
                    try {
                        ProfileError error = gson.fromJson(response.errorBody().string(),
                                ProfileError.class);
                        System.out.println("error " + error.toString());
                        if (error.getName() != null) {
                            Toast.makeText(EditProfileActivity.this, error.getName().get(0),
                                    Toast.LENGTH_SHORT).show();
                        } else if (error.getEmail() != null) {
                            Toast.makeText(EditProfileActivity.this, error.getEmail().get(0),
                                    Toast.LENGTH_SHORT).show();
                        } else if (error.getAvatar() != null) {
                            Toast.makeText(EditProfileActivity.this, error.getAvatar().get(0),
                                    Toast.LENGTH_SHORT).show();
                            initView();
                        }
                    } catch (IOException e) {
                        // handle failure to read error
                        Toast.makeText(EditProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(EditProfileActivity.this, R.string.something_went_wrong,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();

            Glide.with(this)
                    .load(imgDecodableString)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(userAvatar);
//            imgFile = new File(imgDecodableString);

            try {
                imgFile =
                        new id.zelory.compressor.Compressor(EditProfileActivity.this).compressToFile(new File(imgDecodableString));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == Activity.RESULT_CANCELED)
            Toast.makeText(this, getResources().getString(R.string.dont_pick_image),
                    Toast.LENGTH_SHORT).show();
    }

    public void goToImageIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.back_img, R.id.upload_profile, R.id.update, R.id.dob})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.upload_profile:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        goToImageIntent();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
                    }
                } else {
                    goToImageIntent();
                }
                break;
            case R.id.update:
                updateProfile();
                break;
            case R.id.dob:
                openDatePicker();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean permission1 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean permission2 = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (permission1 && permission2) {
                        goToImageIntent();
                    } else {
                        Snackbar.make(this.findViewById(android.R.id.content),
                                "Please Grant Permissions to upload Profile",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(EditProfileActivity.this,
                                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
                                    }
                                }).show();
                    }
                }
                break;
        }
    }

    public void getDeviceToken() {
        String TAG = "FCM";
        try {
            if (!SharedHelper.getKey(this, "device_token").equals("") && SharedHelper.getKey(this
                    , "device_token") != null) {
                device_token = SharedHelper.getKey(this, "device_token");
                Log.d(TAG, "GCM Registration Token: " + device_token);
            } else {
                device_token = FirebaseInstanceId.getInstance().getToken();
                SharedHelper.putKey(this, "device_token",
                        "" + FirebaseInstanceId.getInstance().getToken());
                Log.d(TAG, "Failed to complete token refresh: " + device_token);
            }
        } catch (Exception e) {
            device_token = "COULD NOT GET FCM TOKEN";
            Log.d(TAG, "Failed to complete token refresh");
        }

        try {
            device_UDID = android.provider.Settings.Secure.getString(getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            Log.d(TAG, "Device UDID:" + device_UDID);
            SharedHelper.putKey(this, "device_id", "" + device_UDID);
        } catch (Exception e) {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            Log.d(TAG, "Failed to complete device UDID");
        }
    }
}