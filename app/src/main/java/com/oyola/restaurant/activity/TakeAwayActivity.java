package com.oyola.restaurant.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.oyola.restaurant.R;
import com.oyola.restaurant.adapter.OrderProductAdapter;
import com.oyola.restaurant.application.MyApplication;
import com.oyola.restaurant.helper.ConnectionHelper;
import com.oyola.restaurant.helper.CustomDialog;
import com.oyola.restaurant.helper.GlobalData;
import com.oyola.restaurant.model.Invoice;
import com.oyola.restaurant.model.Order;
import com.oyola.restaurant.model.ServerError;
import com.oyola.restaurant.network.ApiClient;
import com.oyola.restaurant.network.ApiInterface;
import com.oyola.restaurant.utils.Utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasanth on 30-10-2019.
 */
public class TakeAwayActivity extends AppCompatActivity {


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
    @BindView(R.id.wallet_amount_detection)
    TextView walletAmountDetection;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.ready_btn)
    Button readyBtn;
    @BindView(R.id.deliver_btn)
    Button deliverBtn;
    @BindView(R.id.button_lay)
    LinearLayout buttonLay;
    @BindView(R.id.lay_deliverycharge)
    LinearLayout layDeliveryCharge;
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
    boolean paymentOnce = true;
    AlertDialog alertDialog;
    HashMap<String, String> map = new HashMap<>();
    String numberFormat;
    private double bal = 0;
    private double mRoundedAmt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takeaway);
        ButterKnife.bind(this);
        context = TakeAwayActivity.this;
        activity = TakeAwayActivity.this;
        connectionHelper = new ConnectionHelper(context);
        isInternet = connectionHelper.isConnectingToInternet();
        customDialog = new CustomDialog(context);
        numberFormat = MyApplication.getCurrencyFormat();

        backImg.setVisibility(View.VISIBLE);
        layDeliveryCharge.setVisibility(View.GONE);

        if (GlobalData.selectedOrder != null) {
            order = GlobalData.selectedOrder;
        } else {
            finish();
            return;
        }

        title.setText("#" + order.getId());

        String name = order.getUser().getName();
        String payment_mode;
        if (order.getInvoice().getPaymentMode().equalsIgnoreCase("stripe")) {
            payment_mode = context.getString(R.string.credit_card);
        } else {
            payment_mode = Utils.toFirstCharUpperAll(order.getInvoice().getPaymentMode());
        }
        userName.setText(name);
        if (order.getAddress() != null) {
            if (order.getAddress().getMapAddress() != null) {
                address.setText((order.getAddress().getBuilding() != null ? order.getAddress().getBuilding() + ", " : "") +
                        order.getAddress().getMapAddress());
            }
        } else {
            if (order.getShop().getMapsAddress() != null) {
                address.setText(order.getShop().getMapsAddress());
            }
        }
        paymentMode.setText(payment_mode);

        if (order.getNote() != null)
            notes.setText(order.getNote());
        else
            notes.setText(getResources().getString(R.string.empty));

        if (order.getPickUpRestaurant() != null) {
          if (order.getPickUpRestaurant() == 1) {
              txtOrderType.setText(getString(R.string.order_type_takeaway));
              address.setVisibility(View.GONE);
              try {
                  txtOrderTime.setText(getString(R.string.pick_up_time)+" : " + Utils.getDeliveryTime(order.getDeliveryDate()));
              } catch (ParseException e) {
                  e.printStackTrace();
              }
            }
        }

        if (order.getScheduleStatus() != null) {
            if (order.getScheduleStatus() == 1) {
                txtOrderTime.setVisibility(View.VISIBLE);
            } else {
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
        if (order.getInvoice().getPromocode_amount() > 0) {
            promocodeLayout.setVisibility(View.VISIBLE);
        } else {
            promocodeLayout.setVisibility(View.GONE);
        }
        promocode_amount.setText(GlobalData.profile.getCurrency() + "-" +/*String.format("%.2f"*/(order.getInvoice().getPromocode_amount()));
        tv_cgst.setText(GlobalData.profile.getCurrency() + /*String.format("%.2f"*/cgst);
        tv_sgst.setText(GlobalData.profile.getCurrency() + /*String.format("%.2f"*/sgst);
        deliveryCharges.setText(GlobalData.profile.getCurrency() + /*String.format("%.2f"*/order.getInvoice().getDeliveryCharge());
        total.setText(GlobalData.profile.getCurrency() + /*String.format("%.2f"*/order.getInvoice().getPayable());
        walletAmountDetection.setText(GlobalData.profile.getCurrency() + order.getInvoice().getWalletAmount() + "");

        if (order.getStatus().equals("ORDERED") && order.getDispute().equals("NODISPUTE")) {
            disputeBtn.setVisibility(View.GONE);
            buttonLay.setVisibility(View.VISIBLE);
        } else {
            disputeBtn.setVisibility(View.VISIBLE);
            buttonLay.setVisibility(View.GONE);
        }
        disputeBtn.setVisibility(View.GONE);
        buttonLay.setVisibility(View.VISIBLE);

        if (order.getStatus().equalsIgnoreCase("PICKUP_USER")) {
            readyBtn.setVisibility(View.VISIBLE);
            deliverBtn.setVisibility(View.GONE);
        } else if (order.getStatus().equalsIgnoreCase("READY")) {
            readyBtn.setVisibility(View.GONE);
            deliverBtn.setVisibility(View.VISIBLE);
        } else {
            readyBtn.setVisibility(View.VISIBLE);
            deliverBtn.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.back_img, R.id.call_img, R.id.ready_btn, R.id.deliver_btn})
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
            case R.id.ready_btn:
//                showreasonDialog();
                AlertDialog.Builder cancelAlert = new AlertDialog.Builder(this);
                cancelAlert.setTitle(getResources().getString(R.string.order));
                cancelAlert.setMessage(getResources().getString(R.string.ready_order_content));
                cancelAlert.setPositiveButton(getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("status", "READY");
                        updateOrderStatus(map);
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
            case R.id.deliver_btn:
//                paymentPopupWindow("COMPLETED");
                if (GlobalData.selectedOrder == null || GlobalData.selectedOrder.getInvoice() == null) {
                    return;
                }
                final Invoice invoice = GlobalData.selectedOrder.getInvoice();

                if (invoice.getPaid() != 1) {
                    map = new HashMap<>();
                    map.put("status", "COMPLETED");
                    map.put("total_pay", String.valueOf(invoice.getPayable()));
                    map.put("tender_pay", String.valueOf(bal));
                    map.put("payment_mode", invoice.getPaymentMode());
                    map.put("payment_status", "success");
                    updateOrderStatus(map);
                } else {
                   /* map = new HashMap<>();
                    map.put("status", "COMPLETED");
                    updateOrderStatus(map);*/
                    otpValidation(GlobalData.selectedOrder,"COMPLETED");
                }
                break;
        }
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

    private void paymentPopupWindow(final String status) {

        if (GlobalData.selectedOrder == null || GlobalData.selectedOrder.getInvoice() == null) {
            return;
        }
        final Invoice invoice = GlobalData.selectedOrder.getInvoice();
        final Order order = GlobalData.selectedOrder;

        if (invoice.getPaid() != 1) {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(TakeAwayActivity.this);
                final FrameLayout frameView = new FrameLayout(TakeAwayActivity.this);
                builder.setView(frameView);
                alertDialog = builder.create();
                LayoutInflater inflater = alertDialog.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.payment_popup, frameView);
                alertDialog.setCancelable(false);
                if (paymentOnce) {
                    paymentOnce = false;
                    final Button paid = dialogView.findViewById(R.id.paid);
                    final PinView pinView = dialogView.findViewById(R.id.pinView);
                    TextView amount_paid_currency_symbol =
                            dialogView.findViewById(R.id.amount_paid_currency_symbol);
                    amount_paid_currency_symbol.setText(numberFormat/*.getCurrency().getSymbol()*/);
                    TextView balance_currency_symbol =
                            dialogView.findViewById(R.id.balance_currency_symbol);
                    balance_currency_symbol.setText(numberFormat/*.getCurrency().getSymbol()*/);
                    final TextView amount_to_pay = dialogView.findViewById(R.id.amount_to_pay);
                    final EditText amount_paid = dialogView.findViewById(R.id.amount_paid);
                    final TextView balance = dialogView.findViewById(R.id.balance);
                    mRoundedAmt = GlobalData.roundoff(invoice.getPayable());
                    amount_to_pay.setText(numberFormat + invoice.getPayable());
                    amount_paid.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (!s.toString().equals("")) {
                                double amountPaid = Double.parseDouble(s.toString());
                                bal = amountPaid - mRoundedAmt;
                                String mVal = new DecimalFormat("##.##").format(bal);
                                balance.setText(mVal + "");

                                if (bal >= 0) {
                                    paid.setEnabled(true);
                                    paid.getBackground().setAlpha(255);
                                } else {
                                    // paid.setEnabled(false);
                                    paid.getBackground().setAlpha(128);
                                }
                            } else {
                                balance.setText("0");
                                // paid.setEnabled(false);
                                paid.getBackground().setAlpha(128);
                            }

                        }
                    });
                    paid.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*if (pinView.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), getString(R.string
                                        .enter_otp), Toast.LENGTH_SHORT).show();
                            } else if (!pinView.getText().toString().equals("") || !pinView
                                    .getText().toString().equals("null")) {
                                if (order != null) {
                                    if (!String.valueOf(order.getOrderOtp()).equals(pinView
                                            .getText().toString())) {
                                        Toast.makeText(getApplicationContext(), getString(R
                                                .string.invalid_otp), Toast.LENGTH_SHORT).show();
                                    } else {*/


                            if (!amount_paid.getText().toString().equalsIgnoreCase("")) {

                                Double amount = Double.parseDouble(amount_paid.getText().toString());

                                if (amount >= invoice.getPayable()) {
                                    map = new HashMap<>();
                                    map.put("status", status);
                                    map.put("total_pay", amount_paid.getText().toString());
                                    map.put("tender_pay", String.valueOf(bal));
                                    map.put("payment_mode", invoice.getPaymentMode());
                                    map.put("payment_status", "success");
                                    updateOrderStatus(map);
                                    alertDialog.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            getString(R.string.full_amount), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.enter_the_amount_paid),
                                        Toast.LENGTH_SHORT).show();
                            }
                                   /* }
                                }
                            }*/
                            //service_flow.setText("PAYMENT RECEIVED");
                            //img_5.setColorFilter(ContextCompat.getColor(ServiceFlow.this, R
                            // .color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);
                            //startActivity(new Intent(ServiceFlow.this,Home.class));
                        }
                    });
                    if (alertDialog.getWindow() != null)
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            map = new HashMap<>();
            map.put("status", status);
            updateOrderStatus(map);
        }


    }

    private void otpValidation(final Order order, final String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TakeAwayActivity.this);
        final FrameLayout frameView = new FrameLayout(TakeAwayActivity.this);
        builder.setView(frameView);
        alertDialog = builder.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_otp, frameView);
        alertDialog.setCancelable(false);
        if (paymentOnce) {
            paymentOnce = false;
            final Button paid = dialogView.findViewById(R.id.paid);
            final PinView pinView = dialogView.findViewById(R.id.pinView);
            paid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pinView.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), getString(R.string
                                .enter_otp), Toast.LENGTH_SHORT).show();
                    } else if (!pinView.getText().toString().equals("") || !pinView
                            .getText().toString().equals("null")) {
                        if (order != null) {
                            if (!String.valueOf(order.getOrderOtp()).equals(pinView
                                    .getText().toString())) {
                                Toast.makeText(getApplicationContext(), getString(R
                                        .string.invalid_otp), Toast.LENGTH_SHORT).show();
                            } else {
                                map = new HashMap<>();
                                map.put("status", status);
                                updateOrderStatus(map);
                            }
                        }
                    }
                }
            });
            if (alertDialog.getWindow() != null)
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
        }
    }


}
