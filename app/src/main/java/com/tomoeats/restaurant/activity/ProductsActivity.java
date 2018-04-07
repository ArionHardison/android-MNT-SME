package com.tomoeats.restaurant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomoeats.restaurant.R;
import com.tomoeats.restaurant.adapter.ProductsAdapter;
import com.tomoeats.restaurant.helper.ConnectionHelper;
import com.tomoeats.restaurant.helper.CustomDialog;
import com.tomoeats.restaurant.model.CategoryList;
import com.tomoeats.restaurant.model.Product;
import com.tomoeats.restaurant.network.ApiClient;
import com.tomoeats.restaurant.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductsActivity extends AppCompatActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.products_rv)
    RecyclerView productsRv;
    @BindView(R.id.add_products_btn)
    Button addProductsBtn;

    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    String TAG="ProductsActivity";

    List<Product> productList;
    List<CategoryList> categoryLists;
    CategoryList categoryList;
    ProductsAdapter productsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);

        title.setText(getString(R.string.products));
        backImg.setVisibility(View.VISIBLE);
        context=ProductsActivity.this;
        activity=ProductsActivity.this;
        connectionHelper = new ConnectionHelper(context);
        customDialog = new CustomDialog(context);
        categoryLists= new ArrayList<>();

//        productList= new ArrayList<>();
//        productList.add(new Product( "Veg Pizza","Cheese,Nuts,Spicy..."));
//        productList.add(new Product( "Chicken Pizza",""));
//        categoryList=new CategoryList();
//        categoryList.setHeader("Pizza");
//        categoryList.setProductList(productList);
//        categoryLists.add(categoryList);
//        categoryList=new CategoryList();
//        categoryList.setHeader("Burger");
//        categoryList.setProductList(productList);
//        categoryLists.add(categoryList);
//        productsAdapter = new ProductsAdapter(context, categoryLists);
//        productsRv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
//        productsRv.setHasFixedSize(true);
//        productsRv.setAdapter(productsAdapter);

    }

    @OnClick({R.id.back_img, R.id.add_products_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.add_products_btn:
                startActivity(new Intent(context,AddProductActivity.class));
                break;
        }
    }
}
