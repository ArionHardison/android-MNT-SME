package com.tomoeats.restaurant.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tomoeats.restaurant.R;
import com.tomoeats.restaurant.helper.ConnectionHelper;
import com.tomoeats.restaurant.helper.CustomDialog;
import com.tomoeats.restaurant.network.ApiClient;
import com.tomoeats.restaurant.network.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddProductActivity extends AppCompatActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.et_product_name)
    EditText etProductName;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.status_spin)
    Spinner statusSpin;
    @BindView(R.id.category_spin)
    Spinner categorySpin;
    @BindView(R.id.category_order_picker)
    EditText categoryOrderPicker;
    @BindView(R.id.product_img)
    ImageView productImg;
    @BindView(R.id.fetured_img)
    ImageView feturedImg;
    @BindView(R.id.fetured_img_lay)
    LinearLayout feturedImgLay;
    @BindView(R.id.save_btn)
    Button saveBtn;


    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    String TAG = "AddProductActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);

        title.setText(getString(R.string.add_product));
        backImg.setVisibility(View.VISIBLE);
        context = AddProductActivity.this;
        activity = AddProductActivity.this;
        connectionHelper = new ConnectionHelper(context);
        customDialog = new CustomDialog(context);



    }

    @OnClick({R.id.back_img, R.id.product_img, R.id.fetured_img, R.id.save_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.product_img:
                break;
            case R.id.fetured_img:
                break;
            case R.id.save_btn:
                onBackPressed();
                break;
        }
    }
}
