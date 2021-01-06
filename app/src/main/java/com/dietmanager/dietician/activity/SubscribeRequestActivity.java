package com.dietmanager.dietician.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.SubscribeRequestAdapter;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.model.MessageResponse;
import com.dietmanager.dietician.model.SubscribeRequestResponse;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.TextUtils;
import com.dietmanager.dietician.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribeRequestActivity extends AppCompatActivity {

    @BindView(R.id.subscribe_request_rv)
    RecyclerView subscribeRequestRv;
    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;

    private List<SubscribeRequestResponse> subscribedRequestList = new ArrayList<>();
    private SubscribeRequestAdapter subscribedRequestAdapter;
    private Context context;
    private Activity activity;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_request);

        ButterKnife.bind(this);

        customDialog = new CustomDialog(this);
        ((TextView) findViewById(R.id.toolbar).findViewById(R.id.title)).setText(R.string.subscribe_request);
        findViewById(R.id.toolbar).findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setupAdapter();
        getSubscribedMembersList();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SubscribeRequestActivity.this, DietitianMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }

    CustomDialog customDialog;
    private int visibleThreshold = 5;
    private int currentPage = 1;
    private int previousTotalItemCount = 0;

    private boolean loading = false;
    @BindView(R.id.mNestedScrollView)
    NestedScrollView mNestedScrollView;
    private void setupAdapter() {
        subscribedRequestAdapter = new SubscribeRequestAdapter(subscribedRequestList, this);
        subscribeRequestRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        subscribeRequestRv.setLayoutManager(llm);

        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY) {
                        int lastVisibleItemPosition = llm.findLastVisibleItemPosition();
                        int totalItemCount = llm.getItemCount();
                        if (loading && totalItemCount > previousTotalItemCount) {
                            loading = false;
                            previousTotalItemCount = totalItemCount;
                        }
                        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount && subscribeRequestRv.getAdapter().getItemCount() > visibleThreshold) { // This condition will useful when recyclerview has less than visibleThreshold items
                            currentPage++;
                            loading = true;
                            mNestedScrollView.fullScroll(View.FOCUS_DOWN);
                            customDialog.show();
                            getSubscribedMembersList();
                        }
                    }
                }
            }
        });
        subscribeRequestRv.setHasFixedSize(true);
        subscribeRequestRv.setAdapter(subscribedRequestAdapter);
    }

    private void inviteUser(HashMap<String, String> map) {
        Call<MessageResponse> call = apiInterface.inviteUser(map);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    Utils.displayMessage(SubscribeRequestActivity.this, response.body().getMessage());
                } else if (response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.has("email"))
                            Toast.makeText(SubscribeRequestActivity.this, jObjError.optString("email"), Toast.LENGTH_LONG).show();
                        else if (jObjError.has("error"))
                            Toast.makeText(SubscribeRequestActivity.this, jObjError.optString("error"), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(SubscribeRequestActivity.this, "Invalid", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Utils.displayMessage(SubscribeRequestActivity.this, getString(R.string.something_went_wrong));
            }
        });
    }

    private void getSubscribedMembersList() {
        Call<List<SubscribeRequestResponse>> call = apiInterface.getSubscribeRequestList(currentPage);
        call.enqueue(new Callback<List<SubscribeRequestResponse>>() {
            @Override
            public void onResponse(Call<List<SubscribeRequestResponse>> call, Response<List<SubscribeRequestResponse>> response) {
                if (response.isSuccessful()) {
                    subscribedRequestList.clear();
                    List<SubscribeRequestResponse> subscribedModel = response.body();
                    if (subscribedModel != null) {
                        if (subscribedModel.size() > 0) {
                            llNoRecords.setVisibility(View.GONE);
                            subscribeRequestRv.setVisibility(View.VISIBLE);
                            subscribedRequestList.addAll(subscribedModel);
                            subscribedRequestAdapter.setList(subscribedRequestList);
                            subscribedRequestAdapter.notifyDataSetChanged();
                        } else {
                            llNoRecords.setVisibility(View.VISIBLE);
                            subscribeRequestRv.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SubscribeRequestResponse>> call, Throwable t) {
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });
    }

}