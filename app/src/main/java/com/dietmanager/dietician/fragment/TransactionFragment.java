package com.dietmanager.dietician.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.TransactionAdapter;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.helper.JavaUtils;
import com.dietmanager.dietician.helper.SharedHelper;
import com.dietmanager.dietician.model.transaction.TransactionItem;
import com.dietmanager.dietician.model.transaction.TransactionResponse;
import com.dietmanager.dietician.network.APIError;
import com.dietmanager.dietician.network.APISingleError;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.network.ErrorUtils;
import com.dietmanager.dietician.network.SingleErrorUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionFragment extends BaseFragment {

    @BindView(R.id.rv_transactions)
    RecyclerView rvTransactions;
    @BindView(R.id.group)
    Group group;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;

    private CustomDialog customDialog;
    private TransactionAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TransactionAdapter();

        customDialog = new CustomDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallet_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTransactions.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getTransactions();
    }
    private void showLoading() {
        if (customDialog != null && !customDialog.isShowing()) {
            customDialog.show();
        }
    }

    private void hideLoading() {
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
    }
    private void getTransactions() {
        showLoading();
        String header = SharedHelper.getKey(getContext(), "token_type") + " " + SharedHelper.getKey(getContext(), "access_token");
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<TransactionResponse> call = apiInterface.fetchTransactions();
        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(@NonNull Call<TransactionResponse> call,
                                   @NonNull Response<TransactionResponse> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<TransactionItem> transactionItemList = response.body().getTransactionItemList();
                        loadTransactionAdapter(transactionItemList);
                    }
                } else {
                    APISingleError error = SingleErrorUtils.parseError(response);
                    Toast.makeText(getContext(), error.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionResponse> call, @NonNull Throwable t) {
                hideLoading();
                Toast.makeText(getContext(), R.string.something_went_wrong,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadTransactionAdapter(List<TransactionItem> transactionItemList) {
        if (!JavaUtils.isNullOrEmpty(transactionItemList)) {
            tvNoData.setVisibility(View.GONE);
            group.setVisibility(View.VISIBLE);
            rvTransactions.setVisibility(View.VISIBLE);
            adapter.setTransactionItemList(transactionItemList);
        } else {
            tvNoData.setVisibility(View.VISIBLE);
            rvTransactions.setVisibility(View.GONE);
            group.setVisibility(View.GONE);
        }
    }

}
