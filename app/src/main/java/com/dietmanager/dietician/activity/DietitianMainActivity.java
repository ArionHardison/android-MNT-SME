package com.dietmanager.dietician.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.CurrentFoodAdapter;
import com.dietmanager.dietician.adapter.DaysAdapter;
import com.dietmanager.dietician.adapter.FoodAdapter;
import com.dietmanager.dietician.adapter.TimeCategoryAdapter;
import com.dietmanager.dietician.application.MyApplication;
import com.dietmanager.dietician.controller.GetProfile;
import com.dietmanager.dietician.controller.ProfileListener;
import com.dietmanager.dietician.helper.ConnectionHelper;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.helper.SharedHelper;
import com.dietmanager.dietician.model.Profile;
import com.dietmanager.dietician.model.ServerError;
import com.dietmanager.dietician.model.currentfood.CurrentFoodItem;
import com.dietmanager.dietician.model.food.FoodItem;
import com.dietmanager.dietician.model.timecategory.TimeCategoryItem;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Constants;
import com.dietmanager.dietician.utils.TextUtils;
import com.dietmanager.dietician.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DietitianMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProfileListener, DaysAdapter.IDayListener, TimeCategoryAdapter.ITimeCategoryListener/*, ExpandableFoodAdapter.IExpandableClickListener*/ {
    DrawerLayout drawer;
    private ConnectionHelper connectionHelper;
    private DaysAdapter daysAdapter;
    private TimeCategoryAdapter timeCategoryAdapter;
    private CurrentFoodAdapter foodAdapter;
    CircleImageView userAvatar;
    TextView name;
    TextView tvEdit;
    @BindView(R.id.days_rv)
    RecyclerView daysRv;
    @BindView(R.id.time_category_rv)
    RecyclerView timeCategoryRv;
    @BindView(R.id.food_rv)
    RecyclerView foodRv;
    @BindView(R.id.menu_img)
    ImageView menuImg;
    @BindView(R.id.btnAddFood)
    Button btnAddFood;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;/*
    @BindView(R.id.expendableList)
    ExpandableListView expandableListView;*/
    private int selectedDay = 1;
    private int selectedTimeCategory = 1;
    private List<TimeCategoryItem> timeCategoryList = new ArrayList<>();
    private List<CurrentFoodItem> foodItems = new ArrayList<>();
    List<Integer> daysList = new ArrayList<>();
    /*  HashMap<String, List<FoodItem>> expandableFoodList = new HashMap<>();
        private ExpandableFoodAdapter expandableAdapter = null;
        private List<String> titleList = null;*/
    ScheduledExecutorService scheduler;

    private String selectedTimeCategoryName = "Breakfast";

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
        customDialog = new CustomDialog(this);
        connectionHelper = new ConnectionHelper(this);
        drawer = findViewById(R.id.drawer_layout);
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                getProfileAPI();
            }
        }, 0, 5, TimeUnit.SECONDS);
        userAvatar = navigationView.getHeaderView(0).findViewById(R.id.user_avatar);
        LinearLayout nav_header = navigationView.getHeaderView(0).findViewById(R.id.nav_header);
        /*nav_header.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(DietitianMainActivity.this, EditProfileActivity.class));
            }
        });*/
        name = navigationView.getHeaderView(0).findViewById(R.id.name);
        tvEdit = navigationView.getHeaderView(0).findViewById(R.id.tvEdit);
        navigationView.setNavigationItemSelectedListener(this);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(DietitianMainActivity.this, EditProfileActivity.class));
            }
        });
        daysAdapter = new DaysAdapter(this, 0, this);
        daysRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        daysRv.setHasFixedSize(true);
        daysRv.setAdapter(daysAdapter);
        for (int i = 1; i <= 30; i++)
            daysList.add(i);
        daysAdapter.setList(daysList);

        timeCategoryAdapter = new TimeCategoryAdapter(this, 0, this, false);
        timeCategoryRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        timeCategoryRv.setHasFixedSize(true);
        timeCategoryRv.setAdapter(timeCategoryAdapter);

        foodAdapter = new CurrentFoodAdapter(this);
        foodRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        foodRv.setHasFixedSize(true);
        foodRv.setAdapter(foodAdapter);

        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timeCategoryList.isEmpty()) {
                    Intent intent = new Intent(DietitianMainActivity.this, ChooseAddFoodOptionActivity.class);
                    intent.putExtra("timeCategoryList", (Serializable) timeCategoryList);
                    intent.putExtra("selectedTimeCategory", selectedTimeCategory);
                    intent.putExtra("selectedTimeCategoryName", selectedTimeCategoryName);
                    intent.putExtra("selectedDay", selectedDay);
                    startActivity(intent);
                }
            }
        });
        getTimeCategory();
    }


/*    private void initFoodAdapter() {
        titleList = new ArrayList(expandableFoodList.keySet());
        expandableAdapter = new ExpandableFoodAdapter(this, titleList, expandableFoodList, this);
        expandableListView.setAdapter(expandableAdapter);
    }*/

    private void showOrHideView(boolean isVisible) {
        foodRv.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        errorLayout.setVisibility(!isVisible ? View.VISIBLE : View.GONE);
    }

/*    @Override
    public void onCreateNewClicked(String title) {
        if (!timeCategoryList.isEmpty()) {
            for (TimeCategoryItem item : timeCategoryList) {
                if (title.equalsIgnoreCase(item.getName()))
                    selectedTimeCategory = item.getId();
            }
            Intent intent = new Intent(DietitianMainActivity.this, AddFoodActivity.class);
            intent.putExtra("timeCategoryList", (Serializable) timeCategoryList);
            intent.putExtra("selectedTimeCategory", selectedTimeCategory);
            intent.putExtra("isAdminFood", false);
            intent.putExtra("selectedDay", selectedDay);
            startActivity(intent);
        }
    }*/
/*
    @Override
    public void onItemClicked(FoodItem foodItem) {
        if (!timeCategoryList.isEmpty()) {
            for (TimeCategoryItem item : timeCategoryList) {
                if (foodItem.getTimeCategoryId().equalsIgnoreCase(item.getName()))
                    selectedTimeCategory = item.getId();
            }
            Intent intent = new Intent(DietitianMainActivity.this, AddFoodActivity.class);
            intent.putExtra("foodItem", (Serializable) foodItem);
            intent.putExtra("timeCategoryList", (Serializable) timeCategoryList);
            intent.putExtra("selectedTimeCategory", selectedTimeCategory);
            intent.putExtra("selectedDay", selectedDay);
            intent.putExtra("isAdminFood", true);
            startActivity(intent);
        }
    }*/


    public void onDestroy() {
        super.onDestroy();
        scheduler.shutdown();
    }

    @Override
    public void onDayClicked(int day) {
        selectedDay = day;
        getFood();
    }

    private void getProfile() {
        if (connectionHelper.isConnectingToInternet()) {
            new GetProfile(apiInterface, this);
        }
    }

    private void initProfileView() {
        if (GlobalData.profile != null) {
            name.setText(GlobalData.profile.getName());
            //userId.setText(String.valueOf(GlobalData.profile.getId()));
            Glide.with(this)
                    .load(GlobalData.profile.getAvatar())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.man)
                            .error(R.drawable.man))
                    .into(userAvatar);
        }
    }

    public void getProfileAPI() {
        String device_id = Settings.Secure.getString(MyApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        String device_type = "Android";
        String device_token = SharedHelper.getKey(MyApplication.getInstance(), "device_token");
        HashMap<String, String> params = new HashMap<>();
        params.put("device_id", device_id);
        params.put("device_type", device_type);
        params.put("device_token", device_token);
        Call<Profile> call = apiInterface.getProfile(params);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                if (response.isSuccessful()) {
                    if(response.body().getStatus().equalsIgnoreCase("ACTIVE")){
                        if (isWaitingForAdminShowing) {
                            startActivity(new Intent(DietitianMainActivity.this,DietitianMainActivity.class));
                            finishAffinity();
                        }
                    }
                    else {
                        if (!isWaitingForAdminShowing) {
                            waitingForAdminPopup();
                        }
                    }
                } else try {
                    ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                } catch (JsonSyntaxException e) {
                }
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
            }
        });
    }
    private boolean isWaitingForAdminShowing=false;

    public void waitingForAdminPopup() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DietitianMainActivity.this);
            final FrameLayout frameView = new FrameLayout(DietitianMainActivity.this);
            builder.setView(frameView);
            final android.app.AlertDialog purchasedDialog = builder.create();
            purchasedDialog.setCancelable(false);
            LayoutInflater inflater = purchasedDialog.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.waiting_for_admin_approval_popup, frameView);
            purchasedDialog.show();
            isWaitingForAdminShowing=true;
        } catch (Exception e) {
            e.printStackTrace();
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
    public void onCategoryClicked(int category, String categoryName) {
        selectedTimeCategory = category;
        selectedTimeCategoryName = categoryName;
        getFood();
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
            startActivity(new Intent(DietitianMainActivity.this, UserRequestActivity.class));
        } else if (id == R.id.nav_history) {
            startActivity(new Intent(DietitianMainActivity.this, HistoryActivity.class));
        } else if (id == R.id.nav_chef_list) {
        } else if (id == R.id.nav_subscribe_members) {
            startActivity(new Intent(DietitianMainActivity.this, SubscribedMembersActivity.class));
        } else if (id == R.id.nav_subscription_plans) {
            startActivity(new Intent(DietitianMainActivity.this, SubscribePlansActivity.class));
        } else if (id == R.id.nav_invite_link) {
            startActivity(new Intent(DietitianMainActivity.this, InviteLinkActivity.class));
        } else if (id == R.id.nav_invited_user) {
            startActivity(new Intent(DietitianMainActivity.this, InvitedUserActivity.class));
        } else if (id == R.id.nav_guide_lines) {
            startActivity(new Intent(DietitianMainActivity.this, GuideLinesActivity.class));
        } /*else if (id == R.id.nav_portfolio) {
            startActivity(new Intent(DietitianMainActivity.this, PortfolioActivity.class));
        }*/ else if (id == R.id.nav_logout) {
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

    private void getTimeCategory() {

        Call<List<TimeCategoryItem>> call = apiInterface.getTimeCategory();
        call.enqueue(new Callback<List<TimeCategoryItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<TimeCategoryItem>> call, @NonNull Response<List<TimeCategoryItem>> response) {
                if (response.isSuccessful()) {
                    timeCategoryList.clear();
                    List<TimeCategoryItem> timeCategoryItemList = response.body();
                    if (timeCategoryItemList != null && !Utils.isNullOrEmpty(timeCategoryItemList)) {
                        selectedTimeCategory = timeCategoryItemList.get(0).getId();
                        selectedTimeCategoryName = timeCategoryItemList.get(0).getName();
                        timeCategoryList.addAll(timeCategoryItemList);
                        timeCategoryAdapter.setList(timeCategoryList);
                        getFood();
                    }
                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(DietitianMainActivity.this, serverError.getError());
                        if (response.code() == 401) {
                            startActivity(new Intent(DietitianMainActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        }
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(DietitianMainActivity.this, getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TimeCategoryItem>> call, Throwable t) {
                Utils.displayMessage(DietitianMainActivity.this, getString(R.string.something_went_wrong));
            }
        });
    }

    private void getFood() {
        customDialog.show();
        Call<List<CurrentFoodItem>> call = apiInterface.getCurrentFood(selectedTimeCategory, selectedDay);
        call.enqueue(new Callback<List<CurrentFoodItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<CurrentFoodItem>> call, @NonNull Response<List<CurrentFoodItem>> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    foodItems.clear();
                    List<CurrentFoodItem> timeCategoryItemList = response.body();
                    if (timeCategoryItemList.size() > 0) {
                        foodItems.addAll(timeCategoryItemList);
                        showOrHideView(true);
                    } else {
                        showOrHideView(false);
                    }
                    foodAdapter.setList(foodItems);
                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(DietitianMainActivity.this, serverError.getError());
                        if (response.code() == 401) {
                            startActivity(new Intent(DietitianMainActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        }
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(DietitianMainActivity.this, getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CurrentFoodItem>> call, Throwable t) {
                customDialog.cancel();
                Utils.displayMessage(DietitianMainActivity.this, getString(R.string.something_went_wrong));
            }
        });
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
