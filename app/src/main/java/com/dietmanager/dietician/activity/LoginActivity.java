package com.dietmanager.dietician.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dietmanager.dietician.countrypicker.Country;
import com.dietmanager.dietician.countrypicker.CountryPicker;
import com.dietmanager.dietician.countrypicker.CountryPickerListener;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.AppConstants;
import com.dietmanager.dietician.helper.ConnectionHelper;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.helper.SharedHelper;
import com.dietmanager.dietician.model.AuthToken;
import com.dietmanager.dietician.model.Profile;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.TextUtils;
import com.dietmanager.dietician.utils.Utils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dietmanager.dietician.utils.TextUtils.isValidEmail;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.app_logo)
    ImageView appLogo;
    @BindView(R.id.ed_mobile_number)
    EditText edMobileNumber;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.donnot_have_account)
    TextView donnotHaveAccount;
    @BindView(R.id.connect_with)
    TextView connectWith;
    @BindView(R.id.facebook_login)
    ImageButton facebookLogin;
    @BindView(R.id.google_login)
    ImageButton googleLogin;
    String mobile, password,email;
    String GRANT_TYPE = "password";
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    Context context;
    CustomDialog customDialog;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    @BindView(R.id.countryImage)
    ImageView countryImage;
    @BindView(R.id.countryNumber)
    TextView countryNumber;
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.eye_img)
    ImageView eyeImg;
    private CountryPicker mCountryPicker;
    String country_code = "+961";
    Activity activity;
    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 0;

    String device_token, device_UDID;
    Utils utils = new Utils();
    String TAG = "Login";
    /*----------Facebook Login---------------*/
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;

    Button fb_login;
    JSONObject json;
    String fb_first_name = "", fb_id = "", profile_img = "", fb_email = "", fb_last_name = "";
    ConnectionHelper helper;
    Boolean isInternet;


    /*----------Google Login---------------*/
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 100;
    private static final int REQ_SIGN_IN_REQUIRED = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = LoginActivity.this;
        activity = LoginActivity.this;
        customDialog = new CustomDialog(context);
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();
        getDeviceToken();
        signOut();

        callbackManager = CallbackManager.Factory.create();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        /*----------Google Login---------------*/

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //taken from google api console (Web api client id)
//                .requestIdToken("795253286119-p5b084skjnl7sll3s24ha310iotin5k4.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
            }
        }

        mCountryPicker = CountryPicker.newInstance();
        // You can limit the displayed countries
        List<Country> countryList = Country.getAllCountries();
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country s1, Country s2) {
                return s1.getName().compareToIgnoreCase(s2.getName());
            }
        });
        mCountryPicker.setCountriesList(countryList);
        setListener();
        eyeImg.setTag(1);


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
            device_UDID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            Log.d(TAG, "Device UDID:" + device_UDID);
        } catch (Exception e) {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            Log.d(TAG, "Failed to complete device UDID");
        }
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //taken from google api console (Web api client id)
//                .requestIdToken("795253286119-p5b084skjnl7sll3s24ha310iotin5k4.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {

//                FirebaseAuth.getInstance().signOut();
                if (mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.d("MainAct", "Google User Logged out");
                               /* Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();*/
                            }
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d("MAin", "Google API Client Connection Suspended");
            }
        });
    }

    @OnClick({R.id.login_btn, R.id.forgot_password, R.id.donnot_have_account, R.id.back_img, R.id.eye_img, R.id.facebook_login, R.id.google_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                initValues();
                break;
            case R.id.forgot_password:
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
                break;
            case R.id.donnot_have_account:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.facebook_login:
//                Snackbar.make(this.findViewById(android.R.id.content), getResources().getString(R.string.coming_soon), Snackbar.LENGTH_SHORT).show();
                fbLogin();
                break;
            case R.id.google_login:
//                Snackbar.make(this.findViewById(android.R.id.content), getResources().getString(R.string.coming_soon), Snackbar.LENGTH_SHORT).show();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.eye_img:
                if (eyeImg.getTag().equals(1)) {
                    edPassword.setTransformationMethod(null);
                    eyeImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_eye_close));
                    eyeImg.setTag(0);
                } else {
                    eyeImg.setTag(1);
                    edPassword.setTransformationMethod(new PasswordTransformationMethod());
                    eyeImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_eye_open));
                }
                break;

        }
    }

    private void setListener() {
        mCountryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,
                                        int flagDrawableResID) {
                countryNumber.setText(dialCode);
                country_code = dialCode;
                countryImage.setImageResource(flagDrawableResID);
                mCountryPicker.dismiss();
            }
        });

        countryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });

        countryNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });

        getUserCountryInfo();
    }

    private void getUserCountryInfo() {
        Country country = Country.getCountryByName("United States");
        if (country != null) {
            countryImage.setImageResource(country.getFlag());
            countryNumber.setText(country.getDialCode());
            country_code = country.getDialCode();
        } else {
            countryImage.setImageResource(R.drawable.flag_in);
            countryNumber.setText("IN");
            country_code = "+91";
        }
    }


    /*@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }


    public void initValues() {
        GlobalData.loginBy = "manual";
        mobile = edMobileNumber.getText().toString();
        email = etEmail.getText().toString();
        password = edPassword.getText().toString();
        /*if (!isValidMobile(country_code + mobile)) {
            Toast.makeText(this, getResources().getString(R.string.please_enter_mobile_number), Toast.LENGTH_SHORT).show();
        } */
         if (email.isEmpty())
            Utils.displayMessage(LoginActivity.this, getResources().getString(R.string.please_enter_mail_id));
        else if (!isValidEmail(email))
            Utils.displayMessage(LoginActivity.this, getResources().getString(R.string.please_enter_valid_mail_id));
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getResources().getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, String> map = new HashMap<>();
            //map.put("mobile",mobile);
             // map.put("dial_code",country_code);
            map.put("username",email);
            map.put("password", password);
            //map.put("grant_type", "password");
            //map.put("client_id", AppConfigure.CLIENT_ID);
            //map.put("client_secret", AppConfigure.CLIENT_SECRET);
            //map.put("guard", "shops");
            map.put("device_id", device_UDID);
            map.put("device_token", device_token);
            map.put("device_type", AppConstants.DEVICE_TYPE);
            if (helper.isConnectingToInternet()) {
                login(map);
            } else {
                Utils.displayMessage(activity, getString(R.string.oops_connect_your_internet));
            }

        }
    }

    private boolean isValidMobile(String phone) {
        return !(phone == null || phone.length() < 6 || phone.length() > 13) && android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public void login(HashMap<String, String> map) {
        if (!customDialog.isShowing()) {
            customDialog = new CustomDialog(context);
            customDialog.setCancelable(false);
            customDialog.show();
        }
        Call<AuthToken> call;
        if (GlobalData.loginBy.equals("manual"))
            call = apiInterface.login(map);
        else
            call = apiInterface.login(map);
        call.enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(@NonNull Call<AuthToken> call, @NonNull Response<AuthToken> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    SharedHelper.putKey(context, "logged", "true");
                    SharedHelper.putKey(context, "access_token",  response.body().getAccessToken());
                    GlobalData.accessToken = response.body().getTokenType() + " " + response.body().getAccessToken();
                    getProfile();
                } else if (response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.has("email"))
                            Toast.makeText(LoginActivity.this, jObjError.optString("email"), Toast.LENGTH_LONG).show();
                        else if (jObjError.has("error"))
                            Toast.makeText(LoginActivity.this, jObjError.optString("error"), Toast.LENGTH_LONG).show();
                        else if (jObjError.has("password"))
                            Toast.makeText(LoginActivity.this, jObjError.optString("password"), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(LoginActivity.this, "Invalid", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthToken> call, @NonNull Throwable t) {
                customDialog.dismiss();
            }
        });
    }

    private void getProfile() {
        HashMap<String, String> map = new HashMap<>();
        map.put("device_type", "android");
        map.put("device_id", device_UDID);
        map.put("device_token", device_token);
        Call<Profile> getprofile = apiInterface.getProfile(map);
        getprofile.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                customDialog.dismiss();
                SharedHelper.putKey(context, "logged", "true");
                String stripeUrl = response.body() != null && !android.text.TextUtils.isEmpty(response.body().getStripeConnectUrl()) ? response.body().getStripeConnectUrl() : "";

                GlobalData.profile = response.body();
                /*GlobalData.addCart = new AddCart();
                GlobalData.addCart.setProductList(response.body().getCart());
                GlobalData.addressList = new AddressList();
                GlobalData.addressList.setAddresses(response.body().getAddresses());*/
                startActivity(new Intent(LoginActivity.this, DietitianMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                customDialog.dismiss();
            }
        });
    }

    public void fbLogin() {

        if (isInternet) {
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email"));

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {

                        public void onSuccess(LoginResult loginResult) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                Log.e("loginresult", "" + loginResult.getAccessToken().getToken());
                                SharedHelper.putKey(LoginActivity.this, "access_token", loginResult.getAccessToken().getToken());
                                GlobalData.access_token = loginResult.getAccessToken().getToken();
//                                RequestData();
                                GlobalData.loginBy = "facebook";
                                HashMap<String, String> map = new HashMap<>();
                                map.put("login_by", GlobalData.loginBy);
                                map.put("accessToken", GlobalData.access_token);
//                                map.put("client_id", BuildConfigure.CLIENT_ID);
//                                map.put("client_secret", BuildConfigure.CLIENT_SECRET);
                                login(map);
                            }

                        }

                        @Override
                        public void onCancel() {
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                        }
                    });
        } else {
            //mProgressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(getResources().getString(R.string.check_your_internet)).setCancelable(false);
            builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(getResources().getString(R.string.settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent NetworkAction = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(NetworkAction);

                }
            });
            builder.show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        try {
            Log.d("Beginscreen", "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                GlobalData.name = acct.getDisplayName();
                GlobalData.email = acct.getEmail();
                GlobalData.imageUrl = "" + acct.getPhotoUrl();
                Log.d("Google", "display_name:" + acct.getDisplayName());
                Log.d("Google", "mail:" + acct.getEmail());
                Log.d("Google", "photo:" + acct.getPhotoUrl());
                new RetrieveTokenTask().execute(acct.getEmail());
            }
//            } else {
//
//                Snackbar.make(this.findViewById(android.R.id.content), getResources().getString(R.string.google_login_failed), Snackbar.LENGTH_SHORT).show();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class RetrieveTokenTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String accountName = params[0];
            String scopes = "oauth2:profile email";
            String token = null;
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), accountName, scopes);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (UserRecoverableAuthException e) {
                startActivityForResult(e.getIntent(), REQ_SIGN_IN_REQUIRED);
            } catch (GoogleAuthException e) {
                Log.e(TAG, e.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String GoogleaccessToken) {
            super.onPostExecute(GoogleaccessToken);
            Log.e("Token", GoogleaccessToken);
            GlobalData.access_token = GoogleaccessToken;
            GlobalData.loginBy = "google";
            HashMap<String, String> map = new HashMap<>();
            map.put("login_by", GlobalData.loginBy);
            map.put("accessToken", GlobalData.access_token);
//            map.put("client_id", BuildConfigure.CLIENT_ID);
//            map.put("client_secret", BuildConfigure.CLIENT_SECRET);
            login(map);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean FINE_LOCATIONPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean COARSE_LOCATIONPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (FINE_LOCATIONPermission && COARSE_LOCATIONPermission) {


                    } else {
                        Snackbar.make(this.findViewById(android.R.id.content),
                                "Please Grant Permissions to start service",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
                                    }
                                }).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
