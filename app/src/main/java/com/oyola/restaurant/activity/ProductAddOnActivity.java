package com.oyola.restaurant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.oyola.restaurant.R;
import com.oyola.restaurant.helper.ConnectionHelper;
import com.oyola.restaurant.helper.CustomDialog;
import com.oyola.restaurant.helper.SharedHelper;
import com.oyola.restaurant.messages.ProductMessage;
import com.oyola.restaurant.model.Addon;
import com.oyola.restaurant.model.product.ProductResponse;
import com.oyola.restaurant.network.ApiClient;
import com.oyola.restaurant.network.ApiInterface;
import com.oyola.restaurant.utils.Constants;
import com.oyola.restaurant.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAddOnActivity extends AppCompatActivity {

    private static final String TAG = "ProductAddOnActivity";

    private static ProductMessage message;
    private final int ADD_ON_REQ_CODE = 1000;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back_img)
    ImageView imgBack;
    @BindView(R.id.tvAddons)
    TextView tvAddons;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.etDiscount)
    EditText etDiscount;
    @BindView(R.id.spinnerDiscountType)
    MaterialSpinner spinnerDiscountType;
    ArrayList<String> lstDiscountType = new ArrayList<String>();
    String strDiscountType = "";
    String mImageGalleryId = "";
    String strProductPrice = "", strProductDiscount = "";
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    ArrayList<Addon> addOnList = new ArrayList<>();
    ArrayList<Addon> addOnReceivedList = new ArrayList<>();

    ProductResponse productResponse;
    private File compressedImage;

    public static void setMessage(ProductMessage message) {
        ProductAddOnActivity.message = message;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add_on);
        ButterKnife.bind(this);

        setUp();
    }

    private void setUp() {
        connectionHelper = new ConnectionHelper(this);
        customDialog = new CustomDialog(this);

        title.setText(getString(R.string.add_product));
        imgBack.setVisibility(View.VISIBLE);
        setDiscountTypeSpinner();

        Bundle bundle = getIntent().getExtras();
        String currency = SharedHelper.getKey(this, Constants.PREF.CURRENCY);

        tvPrice.setText("Price (" + currency + ")");

        if (bundle != null) {
            title.setText(R.string.edit_product);
            productResponse = bundle.getParcelable("product_data");
            if (productResponse.getPrices() != null) {
                etPrice.setText(productResponse.getPrices().getPrice() + "");
            }
            if (productResponse.getPrices().getDiscountType().equalsIgnoreCase("percentage")) {
                spinnerDiscountType.setSelectedIndex(1);
                strDiscountType = "Percentage";
            } else if (productResponse.getPrices().getDiscountType().equalsIgnoreCase("amount")) {
                spinnerDiscountType.setSelectedIndex(2);
                strDiscountType = "Amount";
            }

            if (productResponse.getPrices() != null) {
                etDiscount.setText(productResponse.getPrices().getDiscount() + "");
            }

            if (productResponse.getAddons().size() > 0) {
                String addOnNames = "";

                if (!addOnReceivedList.isEmpty()) {
                    addOnReceivedList.clear();
                }

                if (productResponse.getAddons().size() > 0) {
                    addOnReceivedList.addAll(productResponse.getAddons());
                }

                for (int i = 0; i < productResponse.getAddons().size(); i++) {
                    if (productResponse.getAddons().get(i).getAddon() != null) {
                        if (i == 0) {
                            addOnNames = productResponse.getAddons().get(i).getAddon().getName();
                        } else {
                            addOnNames = addOnNames + "," + productResponse.getAddons().get(i).getAddon().getName();
                        }
                    }
                }
                tvAddons.setText(addOnNames);
            }
        }

    }

    private void setDiscountTypeSpinner() {
        lstDiscountType.add("Select Discount Type");
        lstDiscountType.add("Percentage");
        lstDiscountType.add("Amount");
        spinnerDiscountType.setItems(lstDiscountType);
        spinnerDiscountType.setOnItemSelectedListener((view, position, id, item) -> strDiscountType = lstDiscountType.get(position));
    }

    @OnClick({R.id.btnSave, R.id.ivAddOn, R.id.back_img})
    public void SubmitNext(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                if (validateInput()) {
                    addProduct();
                }
                break;

            case R.id.back_img:
                onBackPressed();
                break;

            case R.id.ivAddOn:
                Intent intent = new Intent(this, ProductAddOnListActivity.class);
                if (addOnReceivedList.size() > 0) {
                    intent.putParcelableArrayListExtra("addon", addOnReceivedList);
                }
                startActivityForResult(intent, ADD_ON_REQ_CODE);
                break;
        }
    }

    private void addProduct() {
        customDialog.show();
        String shop_id = SharedHelper.getKey(this, Constants.PREF.PROFILE_ID);
        HashMap<String, RequestBody> params = new HashMap<>();

        if (message.getStrProductOrder().equals("")) {
            message.setStrProductOrder("0");
        }

/*        {
            "addons[1]" = 11;
            "addons_price[1]" = "";
            category = 4;
            description = Dhb;
            discount = 1;
            "discount_type" = amount;
            featured = 0;
            "featured_position" = 1;
            name = pizza;
            price = 10;
            "product_position" = fhcgh;
            shop = 2;
            status = enabled;
        }*/

        params.put("name", RequestBody.create(MediaType.parse("text/plain"), message.getStrProductName()));
        params.put("description", RequestBody.create(MediaType.parse("text/plain"), message.getStrProductDescription()));
        params.put("category", RequestBody.create(MediaType.parse("text/plain"), message.getStrProductCategory()));
        params.put("price", RequestBody.create(MediaType.parse("text/plain"), strProductPrice));
        params.put("product_position", RequestBody.create(MediaType.parse("text/plain"), message.getStrProductOrder()));
        params.put("shop", RequestBody.create(MediaType.parse("text/plain"), shop_id));
        params.put("discount", RequestBody.create(MediaType.parse("text/plain"), strProductDiscount));
        params.put("discount_type", RequestBody.create(MediaType.parse("text/plain"), strDiscountType));
        params.put("image_gallery_id", RequestBody.create(MediaType.parse("text/plain"), message.getImageGalleryId()));
        /*if (message.isImageChanged()) {
            params.put("image_gallery_id", RequestBody.create(MediaType.parse("text/plain"), message.getImageGalleryId()));
        }*/
        for (int i = 0; i < addOnList.size(); i++) {
            params.put("addons[" + i + "]", RequestBody.create(MediaType.parse("text/plain"), addOnList.get(i).getId() + ""));
            params.put("addons_price[" + i + "]", RequestBody.create(MediaType.parse("text/plain"),
                    addOnList.get(i).getPrice().replace(getString(R.string.currency_value), "")));
            /*params.put("addons_price[" +addOnList.get(i).getId() + "]", RequestBody.create(MediaType.parse("text/plain"),
                     addOnList.get(i).getPrice().replace(getString(R.string.currency_value), "")));*/
        }
        params.put("status", RequestBody.create(MediaType.parse("text/plain"), message.getStrProductStatus()));
        params.put("cuisine_id", RequestBody.create(MediaType.parse("text/plain"), message.getStrCuisineId()));
        params.put("food_type", RequestBody.create(MediaType.parse("text/plain"), message.getStrSelectedFoodType()));


        MultipartBody.Part filePart1 = null;
        if (message.getProductImageFile() != null) {
            File compressToFile = message.getProductImageFile();
            filePart1 = MultipartBody.Part.createFormData("avatar[]", compressToFile.getName(), RequestBody.create(MediaType.parse("image/*"), compressToFile));
        }

        String featured = (message.getFeaturedImageFile() != null) ? "1" : "0";
        params.put("featured", RequestBody.create(MediaType.parse("text/plain"), featured));
        params.put("featured_position", RequestBody.create(MediaType.parse("text/plain"), featured));

        MultipartBody.Part filePart2 = null;
        if (message.getFeaturedImageFile() != null) {
            File compressToFile = message.getFeaturedImageFile();
            filePart2 = MultipartBody.Part.createFormData("featured_image", compressToFile.getName(), RequestBody.create(MediaType.parse("image/*"), compressToFile));
        }


        Log.d(TAG, params.toString());

        Call<ProductResponse> call;
        if (productResponse != null) {
            int product_id = productResponse.getId();
            params.put("_method", RequestBody.create(MediaType.parse("text/plain"), "PATCH"));
            call = apiInterface.updateProduct(product_id, params, filePart1, filePart2);
        } else {
            params.put("_method", RequestBody.create(MediaType.parse("text/plain"), "POST"));
            call = apiInterface.addProduct(params, filePart1, filePart2);
        }

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    redirectToProductList();
                } else {
                    Utils.displayMessage(ProductAddOnActivity.this, "failed");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                customDialog.dismiss();
                Log.d("onFailure", "" + t.getMessage());
                Utils.displayMessage(ProductAddOnActivity.this, t.toString());
            }
        });
    }

   /* public File Compress(File compressFile) {
        try {
            compressedImage = new Compressor(this)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFile(compressFile);
            return compressedImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressedImage;
    }*/


    private void redirectToProductList() {
        Utils.displayMessage(ProductAddOnActivity.this, getString(R.string.product_added_successfully));
        new Handler().postDelayed(this::finishAffinity, 1000);
    }

    private boolean validateInput() {
        strProductPrice = etPrice.getText().toString().trim();
        strProductDiscount = etDiscount.getText().toString().trim();

        if (strProductPrice.isEmpty()) {
            Utils.displayMessage(this, getString(R.string.please_enter_price));
            return false;
        } else if (strDiscountType.isEmpty()) {
            Utils.displayMessage(this, getString(R.string.please_select_discount_type));
            return false;
        } else if (strProductDiscount.isEmpty()) {
            Utils.displayMessage(this, getString(R.string.please_enter_discount_amount));
            return false;
        }


        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ON_REQ_CODE && resultCode == RESULT_OK) {
            if (!addOnList.isEmpty()) {
                addOnList.clear();
            }
            addOnList = data.getParcelableArrayListExtra("addon");
            String addOns = "";
            if (addOnList.size() > 0) {
                for (int i = 0; i < addOnList.size(); i++) {
                    if (i == 0) {
                        addOns = addOnList.get(i).getName();
                    } else {
                        addOns = addOns + "," + addOnList.get(i).getName();
                    }
                }
            }
            tvAddons.setText(addOns);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        message = null;
    }
}
