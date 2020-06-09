package com.oyola.restaurant.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.oyola.restaurant.R;
import com.oyola.restaurant.adapter.ImageGalleryAdapter;
import com.oyola.restaurant.fragment.CuisineSelectFragment;
import com.oyola.restaurant.helper.ConnectionHelper;
import com.oyola.restaurant.helper.CustomDialog;
import com.oyola.restaurant.messages.ProductMessage;
import com.oyola.restaurant.model.Category;
import com.oyola.restaurant.model.Cuisine;
import com.oyola.restaurant.model.Image;
import com.oyola.restaurant.model.ImageGallery;
import com.oyola.restaurant.model.Product;
import com.oyola.restaurant.model.ServerError;
import com.oyola.restaurant.model.product.ProductResponse;
import com.oyola.restaurant.network.ApiClient;
import com.oyola.restaurant.network.ApiInterface;
import com.oyola.restaurant.utils.Constants;
import com.oyola.restaurant.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.oyola.restaurant.application.MyApplication.ASK_MULTIPLE_PERMISSION_REQUEST_CODE;

public class AddProductActivity extends AppCompatActivity implements ImageGalleryAdapter.ImageSelectedListener {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.et_product_name)
    EditText etProductName;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.et_product_order)
    EditText etProductOrder;
    @BindView(R.id.status_spin)
    MaterialSpinner statusSpin;
    @BindView(R.id.category_spin)
    MaterialSpinner categorySpin;
    @BindView(R.id.product_img)
    ImageView productImg;
    @BindView(R.id.fetured_img)
    ImageView featuredImg;
    @BindView(R.id.fetured_img_lay)
    LinearLayout feturedImgLay;
    @BindView(R.id.next_btn)
    Button nextBtn;
    @BindView(R.id.cuisine)
    TextView cuisine;
    @BindView(R.id.rlProductImage)
    RelativeLayout rlProductImage;
    @BindView(R.id.rlFeaturedImage)
    RelativeLayout rlFeaturedImage;
    @BindView(R.id.rbYes)
    RadioButton rbYes;
    @BindView(R.id.rbNo)
    RadioButton rbNo;
    @BindView(R.id.rbVeg)
    RadioButton rbVeg;
    @BindView(R.id.rbNonVeg)
    RadioButton rbNonVeg;
    @BindView(R.id.imageRecyclerView)
    RecyclerView image_rv;
    @BindView(R.id.imageFeatureRecyclerView)
    RecyclerView rvFeature;
    @BindView(R.id.lay_feature_rv)
    LinearLayout layoutFeatureRv;
    @BindView(R.id.lay_existing_image)
    LinearLayout layoutExistingImage;
    @BindView(R.id.layfeature_existing_image)
    LinearLayout layoutFeatureExistingImage;
    @BindView(R.id.et_ingredients)
    EditText edtIngredients;
    @BindView(R.id.et_calories)
    EditText edtCalories;
    @BindView(R.id.exist_feature_img)
    ImageView existFeatureImage;

    public static final int PICK_IMAGE_REQUEST = 100;

    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    String TAG = "AddProductActivity";
    int PRODUCT_IMAGE_TYPE = 0;
    int FEATURE_IMAGE_TYPE = 1;
    String strProductName, strProductDescription, strStatus = "Enabled", strProductOrder = "0",
            strCategory, strIngredients, strCalories;
    List<Category> listCategory;
    ArrayList<String> lstCategoryNames = new ArrayList<String>();
    HashMap<String, Integer> hshCategory = new HashMap<>();
    ArrayList<String> lstStatus = new ArrayList<String>();
    File productImageFile = null, featuredImageFile;
    ProductResponse productResponse;
    int selected_pos = 0;
    private String foodType;
    String mSelectedProductImageId, mSelectedProductImageUrl = "";
    String mSelectedFeaturedImageId, mSelectedFeaturedImageUrl = "";
    ArrayList<ImageGallery> mImageList = new ArrayList<>();
    ImageGalleryAdapter mProductAdapter;
    ImageGalleryAdapter mFeatureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);
        setUp();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUp() {
        CuisineSelectFragment.CUISINES.clear();
        title.setText(getString(R.string.add_product));
        backImg.setVisibility(View.VISIBLE);
        context = AddProductActivity.this;
        activity = AddProductActivity.this;
        connectionHelper = new ConnectionHelper(context);
        customDialog = new CustomDialog(context);
        customDialog.show();
        getImageGallery();
        setStatusSpinner();

        if (connectionHelper.isConnectingToInternet())
            getCategory();
        else
            Utils.displayMessage(this, getString(R.string.oops_no_internet));


        etDescription.setOnTouchListener((v, event) -> {
            if (v.getId() == R.id.et_description) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
            }
            return false;
        });

        //Default
        rlFeaturedImage.setClickable(false);
        rlFeaturedImage.setAlpha(0.5f);
        layoutFeatureRv.setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title.setText(R.string.edit_product);
            productResponse = bundle.getParcelable("product_data");
            etProductName.setText(productResponse.getName());
            etDescription.setText(productResponse.getDescription());
            if (productResponse.getStatus() != null && productResponse.getStatus().equalsIgnoreCase("enabled")) {
                statusSpin.setSelectedIndex(0);
            } else {
                statusSpin.setSelectedIndex(1);
            }

            etProductOrder.setText(productResponse.getPosition() + "");

            if (productResponse.getIngredients() != null)
                edtIngredients.setText(productResponse.getIngredients());
            if (productResponse.getCalories() != null) {
                edtCalories.setText("" + productResponse.getCalories());
            } else {
                edtCalories.setText("0");
            }
            if (productResponse.getImages() != null &&
                    productResponse.getImages().size() > 0) {
                List<Image> imageList = productResponse.getImages();
                String url = imageList.get(0).getUrl();
//                mSelectedProductImageId = String.valueOf(imageList.get(0).getImageGalleryId());
                mSelectedProductImageUrl = url;
                layoutExistingImage.setVisibility(View.VISIBLE);
             /*   Glide.with(this)
                        .asBitmap()
                        .load(url)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                productImg.setImageBitmap(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });*/

                Glide.with(this)
                        .load(url)
                        .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image)
                                .error(R.drawable.ic_place_holder_image).dontAnimate())
                        .into(productImg);
            }

            if (productResponse.getFeaturedImages() != null &&
                    productResponse.getFeaturedImages().size() > 0) {
                List<Image> imageList = productResponse.getFeaturedImages();
                String url = imageList.get(0).getUrl();
                mSelectedFeaturedImageUrl = url;
//                mSelectedFeaturedImageId = String.valueOf(imageList.get(0).getFeaturedImageGalleryId());
                layoutFeatureExistingImage.setVisibility(View.VISIBLE);
                /*Glide.with(this)
                        .asBitmap()
                        .load(url)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                existFeatureImage.setImageBitmap(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });*/
                Glide.with(this)
                        .load(url)
                        .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image)
                                .error(R.drawable.ic_place_holder_image).dontAnimate())
                        .into(existFeatureImage);
            }

            if (productResponse.getFoodType() != null &&
                    productResponse.getFoodType().equals(Constants.VEG)) {
                rbVeg.setChecked(true);
                rbNonVeg.setChecked(false);
            } else {
                rbVeg.setChecked(false);
                rbNonVeg.setChecked(true);
            }

            if (productResponse.getFeatured() == 1) {
                //debug
                //rbYes.setChecked(false);
                rbYes.setChecked(true);
                rbNo.setChecked(false);
                rlFeaturedImage.setClickable(true);
                rlFeaturedImage.setAlpha(1.0f);
                layoutFeatureRv.setVisibility(View.VISIBLE);
                layoutFeatureExistingImage.setVisibility(View.VISIBLE);
            } else {
                rbYes.setChecked(false);
                rbNo.setChecked(true);
                rlFeaturedImage.setClickable(false);
                rlFeaturedImage.setAlpha(0.5f);
                layoutFeatureRv.setVisibility(View.GONE);
                layoutFeatureExistingImage.setVisibility(View.GONE);
            }

            if (productResponse.getProductcuisines() != null) {
                Cuisine data = new Cuisine();
                data.setId(productResponse.getProductcuisines().getId());
                data.setName(productResponse.getProductcuisines().getName());
                CuisineSelectFragment.CUISINES.add(data);
                cuisine.setText(productResponse.getProductcuisines().getName());
            }
        }

        rbYes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rlFeaturedImage.setClickable(true);
                rlFeaturedImage.setAlpha(1.0f);
                layoutFeatureRv.setVisibility(View.VISIBLE);
            } else {
                rlFeaturedImage.setClickable(false);
                rlFeaturedImage.setAlpha(0.5f);
                layoutFeatureRv.setVisibility(View.GONE);
            }
        });
        rbNo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                layoutFeatureExistingImage.setVisibility(View.GONE);
            } else {
                layoutFeatureExistingImage.setVisibility(View.VISIBLE);
            }
        });
        rbVeg.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rbVeg.setChecked(true);
                rbNonVeg.setChecked(false);
                foodType = Constants.VEG;
            }
        });
        rbNonVeg.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rbNonVeg.setChecked(true);
                rbVeg.setChecked(false);
                foodType = Constants.NON_VEG;
            }
        });
    }


    private void setStatusSpinner() {
        lstStatus.add("Enabled");
        lstStatus.add("Disabled");
        statusSpin.setItems(lstStatus);
        statusSpin.setOnItemSelectedListener(new CommonOnItemSelectListener());
    }

    private void setCategorySpinner() {
        customDialog.dismiss();
        if (productResponse != null && productResponse.getCategories().size() > 0) {
            selected_pos = lstCategoryNames.indexOf(productResponse.getCategories().get(0).getName());

        }
        categorySpin.setItems(lstCategoryNames);
        categorySpin.setOnItemSelectedListener(new CommonOnItemSelectListener());
        if (selected_pos != 0 && selected_pos != -1) {
            categorySpin.setSelectedIndex(selected_pos);
            strCategory = hshCategory.get(lstCategoryNames.get(selected_pos)) + "";
        }
    }

    private void getImageGallery() {
        Call<List<Cuisine>> call = apiInterface.getImages();
        call.enqueue(new Callback<List<Cuisine>>() {
            @Override
            public void onResponse(@NonNull Call<List<Cuisine>> call, @NonNull Response<List<Cuisine>> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        mImageList = new ArrayList<>();
                        for (int i = 0; i < response.body().size(); i++) {
                            if (response.body().get(i).getImageGallery() != null) {
                                mImageList.addAll(response.body().get(i).getImageGallery());
                            }
                        }
                        setupAdapter();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Cuisine>> call, @NonNull Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(AddProductActivity.this, getString(R.string.something_went_wrong));
            }
        });

    }

    private void setupAdapter() {
        List<ImageGallery> mGalleryList;
        if (mImageList.size() > 7) {
            mGalleryList = mImageList.subList(0, 7);
        } else {
            mGalleryList = mImageList;
        }
        mProductAdapter = new ImageGalleryAdapter(mGalleryList, context, this, true, false);
        image_rv.setLayoutManager(new GridLayoutManager(context, 4));
        image_rv.setHasFixedSize(true);
        image_rv.setAdapter(mProductAdapter);
        mFeatureAdapter = new ImageGalleryAdapter(mGalleryList, context, this, true, true);
        rvFeature.setLayoutManager(new GridLayoutManager(context, 4));
        rvFeature.setHasFixedSize(true);
        rvFeature.setAdapter(mFeatureAdapter);
    }

    @OnClick({R.id.back_img, R.id.rlProductImage, R.id.rlFeaturedImage, R.id.next_btn, R.id.cuisine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;

            case R.id.rlProductImage:
                galleryIntent(PRODUCT_IMAGE_TYPE);
                break;

            case R.id.rlFeaturedImage:
                galleryIntent(FEATURE_IMAGE_TYPE);
                break;


            case R.id.next_btn:
                if (validateProductDetails()) {
                    goToNextPage();
                }
                break;

            case R.id.cuisine:
                new CuisineSelectFragment(true).show(getSupportFragmentManager(), "cuisineSelectFragment");
                break;
        }
    }

    private void getCategory() {
        customDialog.show();
        Call<List<Category>> call = apiInterface.getCategory();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listCategory = response.body();
                        //default option
//                        lstCategoryNames.add(getString(R.string.product_select_catefory));
//                        hshCategory.put(getString(R.string.product_select_catefory), 0);
                        if (listCategory.size() > 0) {
                            for (int i = 0; i < listCategory.size(); i++) {
                                Category category = listCategory.get(i);
                                List<Product> product = category.getProducts();
                                for (int j = 0; j < product.size(); j++) {
                                    foodType = product.get(j).getFoodType();
                                }
                                lstCategoryNames.add(listCategory.get(i).getName());
                                hshCategory.put(listCategory.get(i).getName(), listCategory.get(i).getId());
                                strCategory = hshCategory.get(lstCategoryNames.get(0)) + "";

                                if (i == (listCategory.size() - 1)) {
                                    setCategorySpinner();
                                }
                            }
                        } else {
                            setCategorySpinner();
                        }

                    }
                } else {
                    customDialog.dismiss();
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
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });
    }

    private void goToNextPage() {
        ProductMessage message = new ProductMessage();
        message.setStrProductName(strProductName);
        message.setStrProductDescription(strProductDescription);
        message.setStrProductStatus(strStatus.equals("Enabled") ? "1" : "0");
        message.setStrProductCategory(strCategory);
        message.setStrProductOrder(strProductOrder);
        message.setProductIngredients(strIngredients);
        message.setStrCalorieValue(strCalories);

//        message.setImageGalleryId(mSelectedProductImageId);
        message.setImageGalleryUrl(mSelectedProductImageUrl);
        if (rbYes.isChecked()) {
            message.setIsFeatured("1");
//            message.setFeaturedGalleryId(mSelectedFeaturedImageId);
            message.setFeaturedGalleryUrl(mSelectedFeaturedImageUrl);
        } else {
            message.setIsFeatured("0");
        }

        if (productImageFile != null) {
            message.setProductImageFile(productImageFile);
        } else {
            message.setProductImageFile(null);
        }

        if (featuredImageFile != null) {
            message.setFeaturedImageFile(featuredImageFile);
        } else {
            message.setFeaturedImageFile(null);
        }

        if (foodType.equals(Constants.VEG)) {
            message.setStrSelectedFoodType(Constants.VEG);
        } else {
            message.setStrSelectedFoodType(Constants.NON_VEG);
        }
//        message.setStrCuisineId(CuisineSelectFragment.CUISINES.get(0).getId() + "");
        ProductAddOnActivity.setMessage(message);
        if (productResponse != null) {
            Intent intent = new Intent(context, ProductAddOnActivity.class);
            intent.putExtra("product_data", productResponse);
            startActivity(intent);
        } else {
            startActivity(new Intent(this, ProductAddOnActivity.class));
        }

    }

    private void galleryIntent(int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                EasyImage.openChooserWithDocuments(AddProductActivity.this, "Select", type);
            else
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        } else EasyImage.openChooserWithDocuments(AddProductActivity.this, "Select", type);
    }

    private boolean validateProductDetails() {
        strProductName = etProductName.getText().toString().trim();
        strProductDescription = etDescription.getText().toString().trim();
        strProductOrder = etProductOrder.getText().toString().trim();
        strIngredients = edtIngredients.getText().toString().trim();
        strCalories = edtCalories.getText().toString().trim();

        if (strProductName == null || strProductName.isEmpty()) {
            Utils.displayMessage(this, getString(R.string.error_msg_product_name));
            return false;
        } else if (strProductDescription == null || strProductDescription.isEmpty()) {
            Utils.displayMessage(this, getString(R.string.error_msg_product_description));
            return false;
        } else if (strProductOrder == null || strProductOrder.isEmpty() || strProductOrder.equals("0")) {
            Utils.displayMessage(this, getString(R.string.error_msg_product_order));
            return false;
        } else if (strCategory == null || strCategory.isEmpty()) {
            Utils.displayMessage(activity, getResources().getString(R.string.error_msg_product_category));
            return false;
        } else if (!rbVeg.isChecked() && !rbNonVeg.isChecked()) {
            Utils.displayMessage(activity, getResources().getString(R.string.error_msg_selected_food_type));
            return false;
        } else if (strIngredients == null || strIngredients.isEmpty()) {
            Utils.displayMessage(activity, getResources().getString(R.string.error_msg_ingredients));
            return false;
        } else if (strCalories == null || strCalories.isEmpty()) {
            Utils.displayMessage(activity, getResources().getString(R.string.please_enter_calories));
            return false;
        }
        /* else if (productImageFile == null) {
            Utils.displayMessage(activity, getString(R.string.please_select_product_image));
            return false;
        }*/
       /* else if (rbYes.isChecked() && featuredImageFile == null) {
            Utils.displayMessage(activity, getResources().getString(R.string.error_msg_product_select_featured_image));
            return false;
        }*/

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                if (type == PRODUCT_IMAGE_TYPE) {
//                    productImageFile = imageFiles.get(0);

                    try {
                        productImageFile = new Compressor(context).compressToFile(imageFiles.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Glide.with(AddProductActivity.this)
                            .load(imageFiles.get(0))
                            .apply(new RequestOptions()
                                    .placeholder(R.mipmap.ic_launcher)
                                    .error(R.mipmap.ic_launcher).dontAnimate())
                            .into(productImg);
                } else if (type == FEATURE_IMAGE_TYPE) {
//                    featuredImageFile = imageFiles.get(0);

                    try {
                        featuredImageFile = new Compressor(context).compressToFile(imageFiles.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Glide.with(AddProductActivity.this)
                            .load(imageFiles.get(0))
                            .apply(new RequestOptions()
                                    .placeholder(R.mipmap.ic_launcher)
                                    .error(R.mipmap.ic_launcher).dontAnimate())
                            .into(featuredImg);
                }

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getExtras().getBoolean("is_featured")) {
//                mSelectedFeaturedImageId = data.getExtras().getString("image_id");
                mSelectedFeaturedImageUrl = data.getExtras().getString("image_url");
                layoutFeatureExistingImage.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(mSelectedFeaturedImageUrl)
                        .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image)
                                .error(R.drawable.ic_place_holder_image).dontAnimate())
                        .into(existFeatureImage);
            } else {
//                mSelectedProductImageId = data.getExtras().getString("image_id");
                mSelectedProductImageUrl = data.getExtras().getString("image_url");
                layoutExistingImage.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(mSelectedProductImageUrl)
                        .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image)
                                .error(R.drawable.ic_place_holder_image).dontAnimate())
                        .into(productImg);
            }

        }
    }

    public void bindCuisine() {
        StringBuilder cuisneStr = new StringBuilder();
        for (int i = 0; i < CuisineSelectFragment.CUISINES.size(); i++) {
            if (i == 0)
                cuisneStr.append(CuisineSelectFragment.CUISINES.get(i).getName());
            else
                cuisneStr.append(",").append(CuisineSelectFragment.CUISINES.get(i).getName());
        }

        cuisine.setText(cuisneStr.toString());
    }


    @Override
    public void onImageSelected(ImageGallery mGallery, boolean isFeatured) {
        if (isFeatured) {
//            mSelectedFeaturedImageId = String.valueOf(mGallery.getId());
            mSelectedFeaturedImageUrl = mGallery.getImage();
            layoutFeatureExistingImage.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(mSelectedFeaturedImageUrl)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image)
                            .error(R.drawable.ic_place_holder_image).dontAnimate())
                    .into(existFeatureImage);
        } else {
//            mSelectedProductImageId = String.valueOf(mGallery.getId());
            mSelectedProductImageUrl = mGallery.getImage();
            layoutExistingImage.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(mSelectedProductImageUrl)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image)
                            .error(R.drawable.ic_place_holder_image).dontAnimate())
                    .into(productImg);
        }
    }

    @Override
    public void navigateToImageScreen(boolean isFeatured) {
        Intent intent = new Intent(context, ImageGalleryActivity.class);
        intent.putExtra("image_list", mImageList);
        intent.putExtra("is_featured", isFeatured);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    class CommonOnItemSelectListener implements MaterialSpinner.OnItemSelectedListener {
        @Override
        public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
            switch (view.getId()) {
                case R.id.status_spin:
                    strStatus = lstStatus.get(position);
                    break;

                case R.id.category_spin:
                    strCategory = "" + hshCategory.get(lstCategoryNames.get(position));
                    if (strCategory.equalsIgnoreCase("0")) {
                        strCategory = "";
                    }
                    break;
            }
        }
    }
}
