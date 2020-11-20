package com.dietmanager.dietician.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.helper.ConnectionHelper;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.model.MessageResponse;
import com.dietmanager.dietician.model.food.FoodItem;
import com.dietmanager.dietician.model.subscriptionplan.SubscriptionPlanItem;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddSubscriptionPlanActivity  extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.et_plan_name)
    EditText etPlanName;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.et_no_of_days)
    EditText etNoOfDays;
    @BindView(R.id.add_btn)
    Button addBtn;

    @BindView(R.id.rbAutoAssign)
    RadioButton rbAutoAssign;

    Context context;
    Activity activity;
    private boolean isEdit = false;
    private SubscriptionPlanItem subscriptionPlan = null;

    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    String TAG = "AddSubscriptionPlanActivity";
    String strPlanName, strPlanDescription, strPlanPrice,strPlanNoOfDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subscription_plan);
        ButterKnife.bind(this);
        setUp();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUp() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isEdit = bundle.getBoolean("isEdit", false);
        }
        if(isEdit){
            subscriptionPlan = (SubscriptionPlanItem) bundle.getSerializable("subscriptionPlan");
            title.setText(getString(R.string.edit_subscription_plan));
            addBtn.setText(getString(R.string.edit));
            etPlanName.setText(subscriptionPlan.getTitle());
            etDescription.setText(subscriptionPlan.getDescription());
            etPrice.setText(subscriptionPlan.getPrice());
            etNoOfDays.setText(String.valueOf(subscriptionPlan.getNoOfDays()));
            if(subscriptionPlan.getAutoAssign()==1)
                rbAutoAssign.setChecked(true);
        }else {
            title.setText(getString(R.string.add_subscription_plan));
        }
        backImg.setVisibility(View.VISIBLE);
        context = AddSubscriptionPlanActivity.this;
        activity = AddSubscriptionPlanActivity.this;
        customDialog = new CustomDialog(context);

        etDescription.setOnTouchListener((v, event) -> {
            if (v.getId() == R.id.et_description) {
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


    @OnClick({R.id.back_img, R.id.add_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.add_btn:
                if (validatePlanDetails()) {
                    addSubscription();
                }
                break;
        }
    }

    private void addSubscription() {
        if (customDialog != null)
            customDialog.show();

        HashMap<String, String> map = new HashMap<>();
        map.put("title", String.valueOf(strPlanName));
        map.put("description", String.valueOf(strPlanDescription));
        map.put("no_of_days", String.valueOf(strPlanNoOfDays));
        map.put("price", String.valueOf(strPlanPrice));
        if (rbAutoAssign.isChecked()) {
            map.put("auto_assign", "1");
        }
        if(isEdit){
            map.put("_method","PATCH");
            Call<MessageResponse> call = apiInterface.editSubscriptionPlan(map,subscriptionPlan.getId());
            call.enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(@NonNull Call<MessageResponse> call,
                                       @NonNull Response<MessageResponse> response) {
                    customDialog.cancel();
                    if (response.isSuccessful()) {
                        Toast.makeText(AddSubscriptionPlanActivity.this, response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                    customDialog.cancel();
                    Toast.makeText(AddSubscriptionPlanActivity.this, R.string.something_went_wrong,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Call<MessageResponse> call = apiInterface.addSubscriptionPlan(map);
            call.enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(@NonNull Call<MessageResponse> call,
                                       @NonNull Response<MessageResponse> response) {
                    customDialog.cancel();
                    if (response.isSuccessful()) {
                        Toast.makeText(AddSubscriptionPlanActivity.this, response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                    customDialog.cancel();
                    Toast.makeText(AddSubscriptionPlanActivity.this, R.string.something_went_wrong,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private boolean validatePlanDetails() {
        strPlanName = etPlanName.getText().toString().trim();
        strPlanDescription = etDescription.getText().toString().trim();
        strPlanPrice = etPrice.getText().toString().trim();
        strPlanNoOfDays = etNoOfDays.getText().toString().trim();

        if (strPlanName == null || strPlanName.isEmpty()) {
            Utils.displayMessage(this, getString(R.string.error_msg_plan_name));
            return false;
        } else if (strPlanDescription == null || strPlanDescription.isEmpty()) {
            Utils.displayMessage(this, getString(R.string.error_msg_plan_description));
            return false;
        } else if (strPlanPrice == null || strPlanPrice.isEmpty()) {
            Utils.displayMessage(this, getString(R.string.error_msg_plan_price));
            return false;
        } else if (strPlanNoOfDays == null || strPlanNoOfDays.isEmpty()) {
            Utils.displayMessage(this, getString(R.string.error_msg_plan_no_of_days));
            return false;
        }
        return true;
    }
}