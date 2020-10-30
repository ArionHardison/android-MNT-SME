package com.dietmanager.dietician.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.controller.GetProfile;
import com.dietmanager.dietician.controller.ProfileListener;
import com.dietmanager.dietician.helper.ConnectionHelper;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.helper.SharedHelper;
import com.dietmanager.dietician.model.Profile;
import com.dietmanager.dietician.model.ServerError;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.TextUtils;
import com.dietmanager.dietician.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DietitianMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProfileListener {
    DrawerLayout drawer;
    private ConnectionHelper connectionHelper;

    CircleImageView userAvatar;
    TextView name;
    TextView userId;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietitian_main);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        initProfileView();
        getProfile();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        customDialog = new CustomDialog(this);
        connectionHelper = new ConnectionHelper(this);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu_24px, DietitianMainActivity.this.getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        toggle.syncState();
        userAvatar = navigationView.getHeaderView(0).findViewById(R.id.user_avatar);
        LinearLayout nav_header = navigationView.getHeaderView(0).findViewById(R.id.nav_header);
        nav_header.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(DietitianMainActivity.this, EditProfileActivity.class));
            }
        });
        name = navigationView.getHeaderView(0).findViewById(R.id.name);
        userId = navigationView.getHeaderView(0).findViewById(R.id.user_id);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void getProfile() {
        if (connectionHelper.isConnectingToInternet()) {
            new GetProfile(apiInterface, this);
        }
    }

    private void initProfileView() {
        if (GlobalData.profile != null) {
            name.setText(GlobalData.profile.getName());
            userId.setText(String.valueOf(GlobalData.profile.getId()));
            Glide.with(this)
                    .load(GlobalData.profile.getAvatar())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(userAvatar);
        }
    }

    @Override
    public void onSuccess(Profile profile) {
        try {
            GlobalData.profile = profile;
            initProfileView();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(String error) {
        Log.e("Main", error);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_user_request) {

        } else if (id == R.id.nav_history) {
            startActivity(new Intent(DietitianMainActivity.this, HistoryActivity.class));
        } else if (id == R.id.nav_chef_list) {
        } else if (id == R.id.nav_subscribe_members) {
            startActivity(new Intent(DietitianMainActivity.this, SubscribedMembersActivity.class));
        } else if (id == R.id.nav_subscription_plans) {
            startActivity(new Intent(DietitianMainActivity.this, SubscribePlansActivity.class));
        }else if (id == R.id.nav_guide_lines) {
            startActivity(new Intent(DietitianMainActivity.this, GuideLinesActivity.class));
        }else if (id == R.id.nav_portfolio) {
            startActivity(new Intent(DietitianMainActivity.this, PortfolioActivity.class));
        } else if (id == R.id.nav_logout) {
            showLogoutAlertDialog();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private void showLogoutAlertDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getResources().getString(R.string.alert_log_out));
        builder.setPositiveButton(getResources().getString(R.string.okay), (dialog, which) -> {
            dialog.dismiss();
            logOut();
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        builder.show();
    }
    private CustomDialog customDialog;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    private void logOut() {
        customDialog.show();
        // String shop_id = SharedHelper.getKey(context, Constants.PREF.PROFILE_ID);
        Call<ResponseBody> call = apiInterface.logOut();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    clearAndExit();
                } else {
                    ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                    String message = serverError != null ? serverError.getError() : null;
                    clearAndExit();
                    Log.e("Error", !TextUtils.isEmpty(message) ? message : "");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(DietitianMainActivity.this, getString(R.string.something_went_wrong));
            }
        });
    }
    private void clearAndExit() {
        SharedHelper.clearSharedPreferences(this);
        GlobalData.accessToken = "";
        startActivity(new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
