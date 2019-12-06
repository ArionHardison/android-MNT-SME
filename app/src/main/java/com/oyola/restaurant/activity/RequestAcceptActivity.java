package com.oyola.restaurant.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.oyola.restaurant.R;
import com.oyola.restaurant.adapter.OrderProductAdapter;
import com.oyola.restaurant.helper.ConnectionHelper;
import com.oyola.restaurant.helper.CustomDialog;
import com.oyola.restaurant.helper.GlobalData;
import com.oyola.restaurant.model.CancelReasons;
import com.oyola.restaurant.model.FeedBack;
import com.oyola.restaurant.model.Order;
import com.oyola.restaurant.model.ServerError;
import com.oyola.restaurant.network.ApiClient;
import com.oyola.restaurant.network.ApiInterface;
import com.oyola.restaurant.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestAcceptActivity extends AppCompatActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.user_img)
    CircleImageView userImg;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.payment_mode)
    TextView paymentMode;
    @BindView(R.id.call_img)
    ImageView callImg;
    @BindView(R.id.order_product_rv)
    RecyclerView orderProductRv;
    @BindView(R.id.sub_total)
    TextView subTotal;
    @BindView(R.id.service_tax)
    TextView service_tax;
    @BindView(R.id.tv_cgst)
    TextView tv_cgst;
    @BindView(R.id.tv_sgst)
    TextView tv_sgst;
    @BindView(R.id.delivery_charges)
    TextView deliveryCharges;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.cancel_btn)
    Button cancelBtn;
    @BindView(R.id.accept_btn)
    Button acceptBtn;
    @BindView(R.id.button_lay)
    LinearLayout buttonLay;
    @BindView(R.id.txt_order_type)
    TextView txtOrderType;
    @BindView(R.id.txt_order_time)
    TextView txtOrderTime;

    @BindView(R.id.promocodeLayout)
    LinearLayout promocodeLayout;
    @BindView(R.id.dispute_btn)
    Button disputeBtn;

    Order order;
    OrderProductAdapter orderProductAdapter;

    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    boolean isInternet;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    String TAG = "RequestAcceptActivity";
    @BindView(R.id.notes)
    TextView notes;

    @BindView(R.id.discount)
    TextView discount;
    @BindView(R.id.promocode_amount)
    TextView promocode_amount;
    RadioGroup rg;
    FeedBack feedBack;
    ArrayList<FeedBack> feedback_array;
    AlertDialog reasonDialog;
    String cancalReason = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_accept);
        ButterKnife.bind(this);
        context = RequestAcceptActivity.this;
        activity = RequestAcceptActivity.this;
        connectionHelper = new ConnectionHelper(context);
        isInternet = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(context);


        backImg.setVisibility(View.VISIBLE);

        if (GlobalData.selectedOrder != null) {
            order = GlobalData.selectedOrder;
        } else {
            finish();
            return;
        }

        title.setText("#" + order.getId());

        String name = order.getUser().getName();
        String payment_mode = order.getInvoice().getPaymentMode();

        //No minimum character limit in register screen.
        if (name.length() > 1)
            name = name.substring(0, 1).toUpperCase() + name.substring(1);

        payment_mode = payment_mode.substring(0, 1).toUpperCase() + payment_mode.substring(1);

        userName.setText(name);
        if (order.getAddress()!=null) {
            if (order.getAddress().getMapAddress() != null) {
                address.setText(order.getAddress().getMapAddress());
            }
        }
        paymentMode.setText(payment_mode);

        if (order.getNote() != null)
            notes.setText(order.getNote());
        else
            notes.setText(getResources().getString(R.string.empty));

        if (order.getPickUpRestaurant()!=null){
            if (order.getPickUpRestaurant()==0){
                txtOrderType.setText("Order Type : DELIVERY");
                txtOrderTime.setVisibility(View.GONE);
            }else   if (order.getPickUpRestaurant()==1){
                txtOrderType.setText("Order Type : PICKUP");
                address.setVisibility(View.GONE);
                try {
                    txtOrderTime.setText("Pickup Time : "+Utils.getDeliveryTime(order.getDeliveryDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else {
                txtOrderType.setText("Order Type : DELIVERY");
                txtOrderTime.setVisibility(View.GONE);
            }
        }

        if (order.getScheduleStatus()!=null){
            if (order.getScheduleStatus()==1){
                txtOrderTime.setVisibility(View.VISIBLE);
            }else {
                txtOrderTime.setVisibility(View.GONE);
            }
        }

        orderProductAdapter = new OrderProductAdapter(context, order.getItems());
        orderProductRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        orderProductRv.setHasFixedSize(true);
        orderProductRv.setAdapter(orderProductAdapter);

        double cgst_percentage_multiplayer = order.getInvoice().getCGST() / 100;
        double sgst_percentage_multiplayer = order.getInvoice().getSGST() / 100;

        double gross_amount = order.getInvoice().getGross() - order.getInvoice().getDiscount();

        double cgst = (gross_amount * (cgst_percentage_multiplayer));
        double sgst = (gross_amount * (sgst_percentage_multiplayer));


        subTotal.setText(GlobalData.profile.getCurrency() + /*String.format("%.2f"*/order.getInvoice().getGross());
        service_tax.setText(GlobalData.profile.getCurrency() + /*String.format("%.2f"*/order.getInvoice().getTax());

        discount.setText(GlobalData.profile.getCurrency() + "-" + /*String.format("%.2f"*/(order.getInvoice().getDiscount()));
        if (order.getInvoice().getPromocode_amount() > 0){
            promocodeLayout.setVisibility(View.VISIBLE);
        }else{
            promocodeLayout.setVisibility(View.GONE);
        }
        promocode_amount.setText(GlobalData.profile.getCurrency() + "-" +/*String.format("%.2f"*/(order.getInvoice().getPromocode_amount()));
        tv_cgst.setText(GlobalData.profile.getCurrency() + /*String.format("%.2f"*/cgst);
        tv_sgst.setText(GlobalData.profile.getCurrency() + /*String.format("%.2f"*/sgst);
        deliveryCharges.setText(GlobalData.profile.getCurrency() + /*String.format("%.2f"*/order.getInvoice().getDeliveryCharge());
        total.setText(GlobalData.profile.getCurrency() + /*String.format("%.2f"*/order.getInvoice().getNet());

        if (order.getStatus().equals("ORDERED") && order.getDispute().equals("NODISPUTE")) {
            disputeBtn.setVisibility(View.GONE);
            buttonLay.setVisibility(View.VISIBLE);
        } else {
            disputeBtn.setVisibility(View.VISIBLE);
            buttonLay.setVisibility(View.GONE);
        }


    }

    @OnClick({R.id.back_img, R.id.call_img, R.id.cancel_btn, R.id.accept_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.call_img:
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + order.getUser().getPhone()));
                if (dialIntent.resolveActivity(getPackageManager()) != null)
                    startActivity(dialIntent);
                else
                    Utils.displayMessage(this, "Call feature not supported");
                break;
            case R.id.cancel_btn:
//                showreasonDialog();
                AlertDialog.Builder cancelAlert = new AlertDialog.Builder(this);
                cancelAlert.setTitle(getResources().getString(R.string.order));
                cancelAlert.setMessage(getResources().getString(R.string.are_you_sure_want_to_cancel_the_order));
                cancelAlert.setPositiveButton(getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        /*HashMap<String, String> map = new HashMap<>();
                        map.put("status", "CANCELLED");
                        updateOrderStatus(map);*/
                        getFeedback_new();
                    }
                });
                cancelAlert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                        dialog.dismiss();
                    }
                });
                cancelAlert.show();
                break;
            case R.id.accept_btn:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.order_delivery_time, null);
                final EditText edittext = alertLayout.findViewById(R.id.edit_text);
                alert.setTitle(getResources().getString(R.string.order_delivery_time));
                alert.setView(alertLayout);
                alert.setPositiveButton(getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String deliveryTime = edittext.getText().toString();
                        int deliverytime = 0;
                        if (!deliveryTime.isEmpty()){
                            deliverytime = Integer.parseInt(deliveryTime);
                        }
                        if (deliveryTime.isEmpty()) {
                            Toast.makeText(context, getResources().getString(R.string.please_enter_delivery_time), Toast.LENGTH_SHORT).show();
                        } else if (deliverytime < 3){
                            Toast.makeText(context, getResources().getString(R.string.order_ready_time), Toast.LENGTH_SHORT).show();
                        } else {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("status", "RECEIVED");
                            map.put("order_ready_time", deliveryTime);
                            updateOrderStatus(map);
                        }
                    }
                });

                alert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                        dialog.dismiss();
                    }
                });
                alert.show();
                break;
        }
    }

    private void showreasonDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.cancel_dialog, null);
        rg = view.findViewById(R.id.reasons);
        final RadioButton[] radioButton = new RadioButton[1];
        for (int i = 0; i < feedback_array.size(); i++) {
            radioButton[0] = new RadioButton(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            radioButton[0].setLayoutParams(params);
            radioButton[0].setText(feedback_array.get(i).getReason());
            radioButton[0].setId(feedback_array.get(i).getId());
            rg.addView(radioButton[0], i);
            rg.setGravity(View.FOCUS_RIGHT);

        }

        final String[] reason = {""};
        final String[] reason_id = {""};
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                          @Override
                                          public void onCheckedChanged(RadioGroup group, int checkedId) {
                                              radioButton[0] = view.findViewById(checkedId);
                                              //  Toast.makeText(context, radioButton[0].getText(), Toast.LENGTH_SHORT).show();
                                              reason[0] = radioButton[0].getText().toString();
                                              reason_id[0] = String.valueOf(radioButton[0].getId());
                                          }
                                      }
        );

        Button submitBtn = view.findViewById(R.id.submit_reason);
        builder.setView(view)
                .setCancelable(true);
        reasonDialog = builder.create();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancalReason = reason[0];
                if (cancalReason.equalsIgnoreCase("")) {
                    Utils.displayMessage(activity, getString(R.string.give_your_feedback));
                } else {
                    cancelRequest(reason_id[0]);
                }

                reasonDialog.dismiss();
            }
        });

        Window window = reasonDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        reasonDialog.show();


    }

    private void cancelRequest(String cancelId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("status", "CANCELLED");
        map.put("cancel_reason_id", cancelId);
        updateOrderStatus(map);
    }


    private void updateOrderStatus(HashMap<String, String> map) {
        customDialog.show();
        Call<Order> call = apiInterface.updateOrderStatus(GlobalData.selectedOrder.getId(), map);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, Response<Order> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    finish();
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
            public void onFailure(@NonNull Call<Order> call, Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });


    }

    private void getFeedback_new() {
        customDialog.show();
        Call<CancelReasons> call = apiInterface.getCancelReasonList();
        call.enqueue(new Callback<CancelReasons>() {
            @Override
            public void onResponse(@NonNull Call<CancelReasons> call, Response<CancelReasons> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {

                    if (response.body()!=null) {

                        feedback_array = new ArrayList<>();
                        if (response.body().getReasonList().size() > 0) {

                            for (int i = 0; i < response.body().getReasonList().size(); i++) {
                                feedBack = new FeedBack();
                                feedBack.setReason(response.body().getReasonList().get(i).getReason());
                                feedBack.setId(response.body().getReasonList().get(i).getId());
                                feedback_array.add(feedBack);
                            }
                        }
                        showreasonDialog();

                        Log.e(TAG, "feedback_array: " + feedback_array.toString());
                    }

                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(activity, serverError.getError());
                        /*if (response.code() == 401) {
                            context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            activity.finish();
                        }*/
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(activity, getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CancelReasons> call, Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });


    }

}
