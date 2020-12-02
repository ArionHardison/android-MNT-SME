package com.dietmanager.dietician.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.AssignChefListAdapter;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.model.ForgotPasswordResponse;
import com.dietmanager.dietician.model.MessageResponse;
import com.dietmanager.dietician.model.ServerError;
import com.dietmanager.dietician.model.assignchef.AssignChefItem;
import com.dietmanager.dietician.model.subscribe.SubscribeItem;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignChefListActivity extends AppCompatActivity implements AssignChefListAdapter.IAssignChefListener {

    @BindView(R.id.chef_list_rv)
    RecyclerView chefListRv;
    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;
    CustomDialog customDialog;

    @BindView(R.id.search_et)
    EditText etSearch;
    private List<AssignChefItem> assignChefItems = new ArrayList<>();
    private AssignChefListAdapter chefListAdapter;
    private Context context;
    private Activity activity;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_chef_list);
        customDialog = new CustomDialog(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderId = bundle.getInt("order_id");
        }
        ButterKnife.bind(this);
        ((TextView) findViewById(R.id.toolbar).findViewById(R.id.title)).setText(R.string.assign_chef);
        findViewById(R.id.toolbar).findViewById(R.id.back_img).setVisibility(View.VISIBLE);
        findViewById(R.id.toolbar).findViewById(R.id.back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setupAdapter();
        getAssignChefList();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                chefListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onAssignChefClicked(AssignChefItem assignChefItem) {
        HashMap<String, String> params = new HashMap<>();
        params.put("chef_id", String.valueOf(assignChefItem.getId()));
        params.put("order_id", String.valueOf(orderId));
        assignChefPost(params);
    }

    private void assignChefPost(HashMap<String, String> map) {
        customDialog.show();
        Call<MessageResponse> call = apiInterface.assignChefPost(map);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    Utils.displayMessage(AssignChefListActivity.this, response.body().getMessage());
                    startActivity(new Intent(AssignChefListActivity.this,DietitianMainActivity.class));
                    finishAffinity();
                } else {
                    try {
                        ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(AssignChefListActivity.this, serverError.getError());
                        if (response.code() == 401) {
                            startActivity(new Intent(AssignChefListActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        }
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(AssignChefListActivity.this, getString(R.string.something_went_wrong));
                    }

                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(AssignChefListActivity.this, getString(R.string.something_went_wrong));
            }
        });

    }

    private void setupAdapter() {
        chefListAdapter = new AssignChefListAdapter(assignChefItems, this, this);
        chefListRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        chefListRv.setHasFixedSize(true);
        chefListRv.setAdapter(chefListAdapter);
    }

    private void getAssignChefList() {
        customDialog.show();
        Call<List<AssignChefItem>> call = apiInterface.getAssignChefList(orderId);
        call.enqueue(new Callback<List<AssignChefItem>>() {
            @Override
            public void onResponse(Call<List<AssignChefItem>> call, Response<List<AssignChefItem>> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    assignChefItems.clear();
                    List<AssignChefItem> subscribedModel = response.body();
                    if (subscribedModel != null) {
                        if (subscribedModel.size() > 0) {
                            llNoRecords.setVisibility(View.GONE);
                            chefListRv.setVisibility(View.VISIBLE);
                            assignChefItems.addAll(subscribedModel);
                            chefListAdapter.setList(assignChefItems);
                            chefListAdapter.notifyDataSetChanged();
                        } else {
                            llNoRecords.setVisibility(View.VISIBLE);
                            chefListRv.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AssignChefItem>> call, Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });
    }

}