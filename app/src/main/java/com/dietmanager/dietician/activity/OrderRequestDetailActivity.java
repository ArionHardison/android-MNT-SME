package com.dietmanager.dietician.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.IngredientsInvoiceAdapter;
import com.dietmanager.dietician.adapter.SubscribedPlanAdapter;
import com.dietmanager.dietician.config.AppConfigure;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.model.subscriptionplan.SubscriptionPlanItem;
import com.dietmanager.dietician.model.userrequest.OrderingredientItem;
import com.dietmanager.dietician.model.userrequest.UserRequestItem;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.item_total)
    TextView item_total;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.user_img)
    ImageView userImg;
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
        if(userRequestItem!=null){
            tvOrderId.setText("#"+userRequestItem.getId());
            try {
                tvOrderTime.setText(Utils.getTime(userRequestItem.getCreatedAt()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvUserName.setText(userRequestItem.getUser().getName());
            if (userRequestItem.getFood().getAvatar() != null)
                Glide.with(context).load(AppConfigure.BASE_URL+userRequestItem.getFood().getAvatar())
                        .apply(new RequestOptions().centerCrop().placeholder(R.drawable.man).error(R.drawable.man).dontAnimate()).into(userImg);

            food_item_name.setText(userRequestItem.getFood().getName());
            food_item_price.setText(userRequestItem.getFood().getPrice());
            item_total.setText(userRequestItem.getPayable());
            total.setText(userRequestItem.getPayable());
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

    @OnClick({R.id.back_img,R.id.call_img,R.id.navigation_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.call_img:
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + userRequestItem.getUser().getPhone()));
                if (dialIntent.resolveActivity(getPackageManager()) != null)
                    startActivity(dialIntent);
                else
                    Utils.displayMessage(this, "Call feature not supported");
                break;
            case R.id.navigation_img:
                if(userRequestItem.getUser().getLatitude()!=null&&userRequestItem.getUser().getLongitude()!=null) {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", userRequestItem.getUser().getLatitude(), userRequestItem.getUser().getLatitude());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }
                else
                    Utils.displayMessage(this, "User location not found");
                break;
        }
    }
}