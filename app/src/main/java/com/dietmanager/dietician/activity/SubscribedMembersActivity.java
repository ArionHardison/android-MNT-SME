package com.dietmanager.dietician.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.SubscribedMemberAdapter;
import com.dietmanager.dietician.model.MessageResponse;
import com.dietmanager.dietician.model.subscribe.SubscribeItem;
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

public class SubscribedMembersActivity extends AppCompatActivity {

    @BindView(R.id.subscribed_members_rv)
    RecyclerView subscribedMembersRv;
    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;

    private List<SubscribeItem> subscribedMembersList = new ArrayList<>();
    private SubscribedMemberAdapter subscribedMemberAdapter;
    private Context context;
    private Activity activity;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_members);

        ButterKnife.bind(this);
        ((TextView) findViewById(R.id.toolbar).findViewById(R.id.title)).setText(R.string.subscribed_members);
        findViewById(R.id.toolbar).findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.btnInvite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        setupAdapter();
        getSubscribedMembersList();
    }

    private void setupAdapter() {
        subscribedMemberAdapter = new SubscribedMemberAdapter(subscribedMembersList, context);
        subscribedMembersRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        subscribedMembersRv.setHasFixedSize(true);
        subscribedMembersRv.setAdapter(subscribedMemberAdapter);
    }

    private void inviteUser(HashMap<String, String> map) {
        Call<MessageResponse> call = apiInterface.inviteUser(map);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    Utils.displayMessage(SubscribedMembersActivity.this, response.body().getMessage());
                } else if (response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.has("email"))
                            Toast.makeText(SubscribedMembersActivity.this, jObjError.optString("email"), Toast.LENGTH_LONG).show();
                        else if (jObjError.has("error"))
                            Toast.makeText(SubscribedMembersActivity.this, jObjError.optString("error"), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(SubscribedMembersActivity.this, "Invalid", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Utils.displayMessage(SubscribedMembersActivity.this, getString(R.string.something_went_wrong));
            }
        });
    }

    private void showDialog() {
        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_edit, null);
        EditText input = dialogView.findViewById(R.id.etEmail);
        EditText name = dialogView.findViewById(R.id.etName);
        TextView yes = dialogView.findViewById(R.id.tvYes);
        TextView no = dialogView.findViewById(R.id.tvNo);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.cancel();
            }
        });


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input.getText().toString().isEmpty()) {
                    Toast.makeText(SubscribedMembersActivity.this, getString(R.string.please_enter_user_email), Toast.LENGTH_LONG).show();
                } else if (name.getText().toString().isEmpty()) {
                    Toast.makeText(SubscribedMembersActivity.this, getString(R.string.please_enter_user_name), Toast.LENGTH_LONG).show();
                } else if (!TextUtils.isValidEmail(input.getText().toString())) {
                    Toast.makeText(SubscribedMembersActivity.this, getString(R.string.please_enter_valid_email), Toast.LENGTH_LONG).show();
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("email", input.getText().toString());
                    map.put("name", name.getText().toString());
                    inviteUser(map);
                    dialogBuilder.cancel();
                }
            }
        });
        dialogBuilder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    private void getSubscribedMembersList() {
        Call<List<SubscribeItem>> call = apiInterface.getSubscribedList();
        call.enqueue(new Callback<List<SubscribeItem>>() {
            @Override
            public void onResponse(Call<List<SubscribeItem>> call, Response<List<SubscribeItem>> response) {
                if (response.isSuccessful()) {
                    subscribedMembersList.clear();
                    List<SubscribeItem> subscribedModel = response.body();
                    if (subscribedModel != null) {
                        if (subscribedModel.size() > 0) {
                            llNoRecords.setVisibility(View.GONE);
                            subscribedMembersRv.setVisibility(View.VISIBLE);
                            subscribedMembersList.addAll(subscribedModel);
                            subscribedMemberAdapter.setList(subscribedMembersList);
                            subscribedMemberAdapter.notifyDataSetChanged();
                        } else {
                            llNoRecords.setVisibility(View.VISIBLE);
                            subscribedMembersRv.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SubscribeItem>> call, Throwable t) {
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });
    }

}