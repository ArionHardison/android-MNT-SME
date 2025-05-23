package com.dietmanager.dietician.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.helper.ConnectionHelper;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.helper.SharedHelper;
import com.dietmanager.dietician.model.RevenueCountModel;
import com.dietmanager.dietician.model.RevenueGraphModel;
import com.dietmanager.dietician.model.RevenueResponse;
import com.dietmanager.dietician.model.ServerError;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Constants;
import com.dietmanager.dietician.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RevenueFragment extends Fragment {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvTotalRevenue)
    TextView tvTotalRevenue;
    @BindView(R.id.tvOrderReceived)
    TextView tvOrderReceived;
    @BindView(R.id.tvOrderDelievered)
    TextView tvOrderDelievered;
    @BindView(R.id.tvTodayEarnings)
    TextView tvTodayEarnings;
    @BindView(R.id.tvMonthlyEarnings)
    TextView tvMonthlyEarnings;
    @BindView(R.id.chart)
    BarChart mChart;
    Unbinder unbinder;

    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    public RevenueFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_revenue, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUp();
        getRevenueDetails();
    }

    private void setUp() {
        title.setText(getString(R.string.revenue));
        context = getContext();
        activity = getActivity();
        connectionHelper = new ConnectionHelper(context);
        customDialog = new CustomDialog(context);
    }

    private void prepareBarChart(List<String> monthList, List<RevenueGraphModel> graphModelList) {

        ArrayList<BarEntry> entriesGroup1 = new ArrayList<>();
        ArrayList<BarEntry> entriesGroup2 = new ArrayList<>();

        // fill the lists
        for (int i = 0; i < graphModelList.size(); i++) {
            RevenueGraphModel graphModel = graphModelList.get(i);
            entriesGroup1.add(new BarEntry(i, (float) graphModel.getDeliveredCount()));
            entriesGroup2.add(new BarEntry(i, (float) graphModel.getCancelledCount()));
            //entriesGroup1.add(new BarEntry(i, 10));
            //entriesGroup2.add(new BarEntry(i, 5));
        }

        float barWidth = 0.3f;
        float barSpace = 0f;
        float groupSpace = 0.4f;

        mChart.setDescription(null);
        mChart.setPinchZoom(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);

        BarDataSet set1, set2;
        set1 = new BarDataSet(entriesGroup1, getResources().getString(R.string.revenue_order_completed));
        set1.setColor(context.getResources().getColor(R.color.color_green));
        set1.setDrawValues(false);

        set2 = new BarDataSet(entriesGroup2, getResources().getString(R.string.order_cancelled));
        set2.setColor(context.getResources().getColor(R.color.colorRed));
        set2.setDrawValues(false);

        BarData data = new BarData(set1, set2);
//        data.setValueFormatter(new LargeValueFormatter());
        mChart.setData(data);
        mChart.getBarData().setBarWidth(barWidth);
        mChart.getXAxis().setAxisMinimum(0);
        mChart.getXAxis().setAxisMaximum(0 + mChart.getBarData().getGroupWidth(groupSpace, barSpace) * monthList.size());
        mChart.groupBars(0, groupSpace, barSpace);
        mChart.getData().setHighlightEnabled(false);
        mChart.invalidate();

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setYOffset(0f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        //l.setTextSize(8f);

        //X-axis
        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(monthList.size());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthList));
        xAxis.setLabelCount(monthList.size());
//Y-axis
        mChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

        mChart.setScaleMinima((float) 12 / 17f, 1f);
        mChart.zoom(-10f, 0f, 0, 0);
    }

    private void getRevenueDetails() {
        customDialog.show();
        Call<RevenueResponse> call = apiInterface.getRevenueDetails();
        call.enqueue(new Callback<RevenueResponse>() {
            @Override
            public void onResponse(Call<RevenueResponse> call, Response<RevenueResponse> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    updateUI(response.body());
                } else {
                    try {
                        ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(activity, serverError.getError());
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(activity, getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<RevenueResponse> call, Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });
    }

    private void updateUI(RevenueResponse response) {
        String currency = SharedHelper.getKey(context, Constants.PREF.CURRENCY);
        String total_revenue = currency + String.format("%.2f", response.getTotalRevenue());
        String order_received = response.getOrderReceivedToday() + "";
        String order_delivered = response.getOrderDeliveredToday() + "";
        String today_earnings = currency + String.format("%.2f", response.getOrderIncomeToday());
        String monthly_earnings = currency + String.format("%.2f", response.getOrderIncomeMonthly());
        if (tvTotalRevenue != null) {
            tvTotalRevenue.setText(total_revenue);
            tvOrderReceived.setText(order_received);
            tvOrderDelievered.setText(order_delivered);
            tvTodayEarnings.setText(today_earnings);
            tvMonthlyEarnings.setText(monthly_earnings);
            if (!Utils.isNullOrEmpty(response.getDeliveredCountList()) &&
                    !Utils.isNullOrEmpty(response.getCancelledCountList())) {
                List<String> monthList = getMonthList(response.getDeliveredCountList());
                List<RevenueGraphModel> graphModelList = getCountList(response.getDeliveredCountList(), response.getCancelledCountList());
                prepareBarChart(monthList, graphModelList);
            }
        }
    }

    private List<String> getMonthList(List<RevenueCountModel> countModelList) {
        List<String> monthList = new ArrayList<>(countModelList.size());
        for (int i = 0, size = countModelList.size(); i < size; i++) {
            String month = countModelList.get(i).getMonthName();
            monthList.add(month);
        }
        return monthList;
    }

    private List<RevenueGraphModel> getCountList(List<RevenueCountModel> deliveredCounts, List<RevenueCountModel> cancelledCounts) {
        List<RevenueGraphModel> graphModelList = new ArrayList<>(deliveredCounts.size());

        for (int i = 0, size = deliveredCounts.size(); i < size; i++) {

            RevenueCountModel deliveredCount = deliveredCounts.get(i);
            RevenueCountModel cancelledCount = cancelledCounts.get(i);

            RevenueGraphModel graphModel = new RevenueGraphModel();
            graphModel.setDeliveredCount(deliveredCount.getCount());
            graphModel.setCancelledCount(cancelledCount.getCount());
            graphModelList.add(graphModel);
        }
        return graphModelList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}