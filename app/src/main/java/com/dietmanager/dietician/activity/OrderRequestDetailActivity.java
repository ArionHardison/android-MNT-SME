package com.dietmanager.dietician.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.activity.fcm_chat.ChatActivity;
import com.dietmanager.dietician.adapter.IngredientsInvoiceAdapter;
import com.dietmanager.dietician.adapter.SubscribedPlanAdapter;
import com.dietmanager.dietician.config.AppConfigure;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.model.MessageResponse;
import com.dietmanager.dietician.model.ServerError;
import com.dietmanager.dietician.model.subscriptionplan.SubscriptionPlanItem;
import com.dietmanager.dietician.model.userrequest.OrderingredientItem;
import com.dietmanager.dietician.model.userrequest.UserRequestItem;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dietmanager.dietician.utils.Utils.getTimeFromString;

public class OrderRequestDetailActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_address)
    TextView tvUserAddress;
    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.tvOrderTime)
    TextView tvOrderTime;
    @BindView(R.id.food_item_name)
    TextView food_item_name;
    @BindView(R.id.food_item_price)
    TextView food_item_price;
    @BindView(R.id.service_tax)
    TextView service_tax;
    @BindView(R.id.item_total)
    TextView item_total;
    @BindView(R.id.llAssignChef)
    LinearLayout llAssignChef;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.user_img)
    ImageView userImg;
    @BindView(R.id.llContactUser)
    LinearLayout llContactUser;
    @BindView(R.id.ingredients_rv)
    RecyclerView ingredients_rv;
    Context context;
    private IngredientsInvoiceAdapter ingredientsAdapter;
    Activity activity;
    private UserRequestItem userRequestItem = null;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    String TAG = "OrderRequestDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_request_detailed);
        ButterKnife.bind(this);
        setUp();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUp() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userRequestItem = (UserRequestItem) bundle.getSerializable("userRequestItem");
            title.setText(getString(R.string.live_task));
        }
        if (userRequestItem != null) {
            tvOrderId.setText("#" + userRequestItem.getId());

            if (userRequestItem.getScheduleAt() != null)
                tvOrderTime.setText(getTimeFromString(userRequestItem.getScheduleAt()));
            else
                tvOrderTime.setText(getTimeFromString(userRequestItem.getCreatedAt()));

            tvUserName.setText(Utils.toFirstCharUpperAll(userRequestItem.getUser().getName()));

            if(userRequestItem.getStatus().equalsIgnoreCase("COMPLETED")||userRequestItem.getStatus().equalsIgnoreCase("CANCELLED"))
                llContactUser.setVisibility(View.GONE);
            if(userRequestItem.getCustomerAddress()!=null)
                tvUserAddress.setText(userRequestItem.getCustomerAddress().getMapAddress());
            if (userRequestItem.getFood().getAvatar() != null)
                Glide.with(this).load(AppConfigure.BASE_URL + userRequestItem.getFood().getAvatar())
                        .apply(new RequestOptions().centerCrop().placeholder(R.drawable.man).error(R.drawable.man).dontAnimate()).into(userImg);

            food_item_name.setText(userRequestItem.getFood().getName());
            food_item_price.setText(GlobalData.profile.getCurrency() + userRequestItem.getFood().getPrice());
            item_total.setText(GlobalData.profile.getCurrency() + userRequestItem.getPayable());
            service_tax.setText(GlobalData.profile.getCurrency() + userRequestItem.getTax());
            total.setText(GlobalData.profile.getCurrency() + userRequestItem.getTotal());

            if(userRequestItem.getUser()!=null&&userRequestItem.getUser().getSubscribePlans()!=null&&userRequestItem.getUser().getSubscribePlans().getSubscription()!=null&&userRequestItem.getUser().getSubscribePlans().getSubscription().getAutoAssign()==1)
                llAssignChef.setVisibility(View.GONE);

            if(bundle.getBoolean("hideAssignChef",false))
                llAssignChef.setVisibility(View.GONE);

            if(!userRequestItem.getStatus().equalsIgnoreCase("ORDERED"))
                llAssignChef.setVisibility(View.GONE);
        }
        context = OrderRequestDetailActivity.this;
        activity = OrderRequestDetailActivity.this;
        customDialog = new CustomDialog(context);
        setupAdapter();
    }

    private void setupAdapter() {
        ingredientsAdapter = new IngredientsInvoiceAdapter(userRequestItem.getOrderingredient(), context);
        ingredients_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        ingredients_rv.setHasFixedSize(true);
        ingredients_rv.setAdapter(ingredientsAdapter);
        ingredientsAdapter.notifyDataSetChanged();
    }


    private void cancelOrder(HashMap<String, String> map) {
        customDialog.show();
        Call<MessageResponse> call = apiInterface.cancelOrderPost(map);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    Utils.displayMessage(OrderRequestDetailActivity.this, response.body().getMessage());
                    finish();
                } else {
                    try {
                        ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(OrderRequestDetailActivity.this, serverError.getError());
                        if (response.code() == 401) {
                            startActivity(new Intent(OrderRequestDetailActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        }
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(OrderRequestDetailActivity.this, getString(R.string.something_went_wrong));
                    }

                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(OrderRequestDetailActivity.this, getString(R.string.something_went_wrong));
            }
        });

    }

    @OnClick({R.id.back_img, R.id.call_img, R.id.navigation_img, R.id.assign_chef_btn,R.id.cancel_btn,R.id.message_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.assign_chef_btn:
                Intent intentAssignChef = new Intent(OrderRequestDetailActivity.this, AssignChefListActivity.class);
                intentAssignChef.putExtra("order_id",userRequestItem.getId());
                startActivity(intentAssignChef);
                break;
            case R.id.call_img:
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + userRequestItem.getUser().getPhone()));
                if (dialIntent.resolveActivity(getPackageManager()) != null)
                    startActivity(dialIntent);
                else
                    Utils.displayMessage(this, "Call feature not supported");
                break;
            case R.id.cancel_btn:
                HashMap<String, String> params = new HashMap<>();
                params.put("order_id", String.valueOf(userRequestItem.getId()));
                cancelOrder(params);
                break;
            case R.id.message_img:
                Intent intentMessage = new Intent(this, ChatActivity.class);
                intentMessage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intentMessage.putExtra("channel_name", "" + userRequestItem.getId());
                intentMessage.putExtra("channel_sender_id", "" + userRequestItem.getDietitian().getId());
                intentMessage.putExtra("is_push", false);
                startActivity(intentMessage);
                break;
            case R.id.navigation_img:
                if (userRequestItem.getCustomerAddress().getLatitude() != 0.0 && userRequestItem.getCustomerAddress().getLongitude() != 0.0) {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", userRequestItem.getCustomerAddress().getLatitude(), userRequestItem.getCustomerAddress().getLatitude());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                } else
                    Utils.displayMessage(this, "User location not found");
                break;
        }
    }
}