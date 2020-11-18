package com.dietmanager.dietician.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.InvitedUserAdapter;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.model.initeduser.InvitedUserItem;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitedUserActivity extends AppCompatActivity {

    @BindView(R.id.invited_list_rv)
    RecyclerView invitedUserRv;
    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;
    CustomDialog customDialog;

    private List<InvitedUserItem> assignChefItems = new ArrayList<>();
    private InvitedUserAdapter invitedUserAdapter;
    private Context context;
    private Activity activity;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invited_user);
        customDialog = new CustomDialog(this);
        ButterKnife.bind(this);
        ((TextView) findViewById(R.id.toolbar).findViewById(R.id.title)).setText(R.string.invite_user);
        findViewById(R.id.toolbar).findViewById(R.id.back_img).setVisibility(View.VISIBLE);
        findViewById(R.id.toolbar).findViewById(R.id.back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setupAdapter();
        getInvitedUserList();
    }


    private void setupAdapter() {
        invitedUserAdapter = new InvitedUserAdapter(assignChefItems, this);
        invitedUserRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        invitedUserRv.setHasFixedSize(true);
        invitedUserRv.setAdapter(invitedUserAdapter);
    }

    private void getInvitedUserList() {
        Call<List<InvitedUserItem>> call = apiInterface.getInvitedUser();
        call.enqueue(new Callback<List<InvitedUserItem>>() {
            @Override
            public void onResponse(Call<List<InvitedUserItem>> call, Response<List<InvitedUserItem>> response) {
                if (response.isSuccessful()) {
                    assignChefItems.clear();
                    List<InvitedUserItem> subscribedModel = response.body();
                    if (subscribedModel != null) {
                        if (subscribedModel.size() > 0) {
                            llNoRecords.setVisibility(View.GONE);
                            invitedUserRv.setVisibility(View.VISIBLE);
                            assignChefItems.addAll(subscribedModel);
                            invitedUserAdapter.setList(assignChefItems);
                            invitedUserAdapter.notifyDataSetChanged();
                        } else {
                            llNoRecords.setVisibility(View.VISIBLE);
                            invitedUserRv.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<InvitedUserItem>> call, Throwable t) {
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });
    }
}