package com.dietmanager.dietician.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.SubscribedMemberAdapter;
import com.dietmanager.dietician.model.SubscribedMembers;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscribePlansActivity extends AppCompatActivity {

    @BindView(R.id.subscribed_members_rv)
    RecyclerView subscribedMembersRv;
    @BindView(R.id.llNoRecords)
    LinearLayout llNoRecords;

    private List<SubscribedMembers> SubscribedMembersList = new ArrayList<>();
    private SubscribedMemberAdapter subscribedMemberAdapter;
    private Context context;
    private Activity activity;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_plans);

        ButterKnife.bind(this);
        setupAdapter();
        getSubscribedMembersList();
    }

    private void setupAdapter() {
        subscribedMemberAdapter = new SubscribedMemberAdapter(SubscribedMembersList, context);
        subscribedMembersRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        subscribedMembersRv.setHasFixedSize(true);
        subscribedMembersRv.setAdapter(subscribedMemberAdapter);
    }

    private void getSubscribedMembersList() {
       /* HistoryActivity.showDialog();
        Call<HistoryModel> call = apiInterface.getHistory();
        call.enqueue(new Callback<HistoryModel>() {
            @Override
            public void onResponse(Call<HistoryModel> call, Response<HistoryModel> response) {
                HistoryActivity.dismissDialog();
                if (response.isSuccessful()) {
                    SubscribedMembersList.clear();
                    HistoryModel historyModel = response.body();
                    if (historyModel != null) {
                        if (historyModel.getCOMPLETED() != null && historyModel.getCOMPLETED().size() > 0) {
                            llNoRecords.setVisibility(View.GONE);
                            pastRv.setVisibility(View.VISIBLE);
                            orderList = historyModel.getCOMPLETED();
                            sortOrdersToDescending(orderList);
                            historyAdapter.setList(orderList);
                            historyAdapter.notifyDataSetChanged();
                        } else {
                            llNoRecords.setVisibility(View.VISIBLE);
                            pastRv.setVisibility(View.GONE);
                        }
                        if (cancelledListListener != null)
                            if (historyModel.getCANCELLED() != null && historyModel.getCANCELLED().size() > 0) {
                                cancelledListListener.setCancelledListener(historyModel.getCANCELLED());
                            } else {
                                cancelledListListener.setCancelledListener(new ArrayList<Order>());
                            }
                    }
                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(activity, serverError.getError());
                        if (response.code() == 401) {
                            context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            activity.finish();
                        }
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(activity, getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<HistoryModel> call, Throwable t) {
                HistoryActivity.dismissDialog();
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });*/
    }

}