package com.dietmanager.dietician.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.countrypicker.Country;
import com.dietmanager.dietician.countrypicker.CountryPicker;
import com.dietmanager.dietician.fragment.IngredientSelectFragment;
import com.dietmanager.dietician.helper.ConnectionHelper;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.helper.MultiSelectionSpinner;
import com.dietmanager.dietician.helper.SharedHelper;
import com.dietmanager.dietician.model.MessageResponse;
import com.dietmanager.dietician.model.ServerError;
import com.dietmanager.dietician.model.SpinnerItem;
import com.dietmanager.dietician.model.food.FoodItem;
import com.dietmanager.dietician.model.ingredients.IngredientsItem;
import com.dietmanager.dietician.model.timecategory.TimeCategoryItem;
import com.dietmanager.dietician.model.userrequest.Foodingredient;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.TextUtils;
import com.dietmanager.dietician.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dietmanager.dietician.application.MyApplication.ASK_MULTIPLE_PERMISSION_REQUEST_CODE;

public class AddFoodActivity extends AppCompatActivity implements IngredientSelectFragment.IngredientSelectFragmentListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.et_product_name)
    EditText etProductName;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.ingredients_tv)
    TextView ingredientsTv;
    @BindView(R.id.et_price)
    EditText etPrice;/*
    @BindView(R.id.ingredients_spin)
    MultiSelectionSpinner ingredientsSpin;*/
    @BindView(R.id.featured_img)
    ImageView featuredImg;
    @BindView(R.id.add_btn)
    Button addBtn;
    @BindView(R.id.tvTimeCategory)
    TextView tvTimeCategory;
    @BindView(R.id.rlFeaturedImage)
    RelativeLayout rlFeaturedImage;
    public static final int PICK_IMAGE_REQUEST = 100;
    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    String TAG = "AddFoodActivity";
    String strProductName, strProductDescription, strProductPrice;
    private int selectedDay = 1;
    File featuredImageFile;
    private String selectedTimeCategoryName = "Breakfast";

    String mSelectedFeaturedImageId, mSelectedFeaturedImageUrl = "";
    private int selectedTimeCategory = 0;
    private boolean isAdminFood = false;
   /* ArrayList<SpinnerItem> itemSpinnerList = new ArrayList<>();*/
    private FoodItem foodItem = null;

    private Handler mDelayHandler = null;
    private long SPLASH_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        ButterKnife.bind(this);
        setUp();
        mDelayHandler = new Handler();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUp() {
        title.setText(getString(R.string.add_food));
        backImg.setVisibility(View.VISIBLE);
        context = AddFoodActivity.this;
        activity = AddFoodActivity.this;
        connectionHelper = new ConnectionHelper(context);
        customDialog = new CustomDialog(context);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isAdminFood = bundle.getBoolean("isAdminFood", false);
            if (isAdminFood) {
                foodItem = (FoodItem) bundle.getSerializable("foodItem");
                etProductName.setText(foodItem.getName());
                etDescription.setText(foodItem.getDescription());
                etPrice.setText(String.valueOf(foodItem.getPrice()));

                if (foodItem.getAvatar() != null)
                    Glide.with(context).load(foodItem.getAvatar())
                            .apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_placeholder_image_upload).error(R.drawable.ic_placeholder_image_upload).dontAnimate()).into(featuredImg);
                etProductName.setClickable(false);
                etDescription.setClickable(false);
                etPrice.setClickable(false);
                //ingredientsSpin.setClickable(false);

                etProductName.setEnabled(false);
                etDescription.setEnabled(false);
                etPrice.setEnabled(false);
                //ingredientsSpin.setEnabled(false);

                if (foodItem.getFood_ingredients().size() > 0) {
                    GlobalData.ingredientsItemList.clear();
                    //itemSpinnerList.clear();
                    for (Foodingredient foodingredient : foodItem.getFood_ingredients()) {
                        GlobalData.ingredientsItemList.add(foodingredient.getIngredient());
                        //itemSpinnerList.add(new SpinnerItem(foodingredient.getIngredient().getName(), true, foodingredient.getIngredient().getId(), foodingredient.getQuantity(), (foodingredient.getIngredient().getUnitType()) != null ? foodingredient.getIngredient().getUnitType().getName() : ""));
                    }
                    //ingredientsSpin.setSelectedItems(itemSpinnerList);
                    StringBuilder sb = new StringBuilder();
                    boolean foundOne = false;
                    for (int i = 0; i < GlobalData.ingredientsItemList.size(); ++i) {
                        if (foundOne) {
                            sb.append(", ");
                        }
                        foundOne = true;
                        sb.append(GlobalData.ingredientsItemList.get(i).getName());
                    }
                    ingredientsTv.setText(sb.toString());
                } else
                    ingredientsTv.setText("No Ingredients found");
                addBtn.setText(getString(R.string.confirm));
            }
            selectedTimeCategoryName = bundle.getString("selectedTimeCategoryName");
            selectedTimeCategory = bundle.getInt("selectedTimeCategory");
            selectedDay = bundle.getInt("selectedDay");
            tvTimeCategory.setText(selectedTimeCategoryName);
        }

        if (!isAdminFood) {
            if (connectionHelper.isConnectingToInternet())
                getIngredients();
            else
                Utils.displayMessage(this, getString(R.string.oops_no_internet));
        }

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
    }

    @Override
    public void onIngredientSubmit() {
        if (GlobalData.selectedIngredientsList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            boolean foundOne = false;
            for (int i = 0; i < GlobalData.selectedIngredientsList.size(); ++i) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;
                sb.append(GlobalData.selectedIngredientsList.get(i).getName());
            }
            ingredientsTv.setText(sb.toString());
        } else {
            ingredientsTv.setText(getString(R.string.select_ingredients));
        }
    }


    private IngredientSelectFragment ingredientSelectFragment;



    @OnClick({R.id.back_img, R.id.rlFeaturedImage, R.id.add_btn, R.id.ingredients_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;

            case R.id.rlFeaturedImage:
                if (!isAdminFood)
                    galleryIntent(1);
                break;

            case R.id.ingredients_tv:
                if (!isAdminFood) {
                    ingredientSelectFragment = IngredientSelectFragment.newInstance(this);
                    ingredientSelectFragment.show(getSupportFragmentManager(), "SELECT_INGREDIENT");
                }
                break;

            case R.id.add_btn:
                if (!isAdminFood) {
                    if (validateProductDetails()) {
                        addFood();
                    }
                } else {
                    addAdminFood();
                }
                break;
        }
    }

    private void getIngredients() {
        customDialog.show();
        Call<List<IngredientsItem>> call = apiInterface.getIngredients();
        call.enqueue(new Callback<List<IngredientsItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<IngredientsItem>> call, @NonNull Response<List<IngredientsItem>> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        GlobalData.ingredientsItemList = response.body();
                        //itemSpinnerList.clear();
                       /* if (GlobalData.ingredientsItemList.size() > 0) {
                            for (int i = 0; i < GlobalData.ingredientsItemList.size(); i++) {
                                IngredientsItem item = GlobalData.ingredientsItemList.get(i);
                                itemSpinnerList.add(new SpinnerItem(item.getName(), false, item.getId(), "1", item.getUnitType().getName()));
                            }
                        }*/
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
            public void onFailure(@NonNull Call<List<IngredientsItem>> call, @NonNull Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });
    }

    private void addFood() {
        if (customDialog != null)
            customDialog.show();

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("name[0]", RequestBody.create(MediaType.parse("text/plain"),
                strProductName));
        map.put("description[0]", RequestBody.create(MediaType.parse("text/plain"),
                strProductDescription));
        map.put("price[0]", RequestBody.create(MediaType.parse("text/plain"),
                strProductPrice));
        map.put("category_id[0]", RequestBody.create(MediaType.parse("text/plain"),
                String.valueOf(selectedTimeCategory)));
        map.put("day", RequestBody.create(MediaType.parse("text/plain"),
                String.valueOf(selectedDay)));
        for (int i = 0; i < GlobalData.selectedIngredientsList.size(); i++) {
            IngredientsItem item = GlobalData.selectedIngredientsList.get(i);
            map.put("ingredient[0][" + i + "]", RequestBody.create(MediaType.parse("text/plain"),
                    String.valueOf(item.getId())));
            map.put("quantity[0][" + i + "]", RequestBody.create(MediaType.parse("text/plain"),
                    String.valueOf(item.getQuantity())));
        }
        MultipartBody.Part filePart = null;

        if (featuredImageFile != null)
            filePart = MultipartBody.Part.createFormData("avatar[0]", featuredImageFile.getName(),
                    RequestBody.create(MediaType.parse("image/*"), featuredImageFile));

        String header = SharedHelper.getKey(this, "token_type") + " " + SharedHelper.getKey(this,
                "access_token");
        Call<MessageResponse> call = apiInterface.addFood(map, filePart);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call,
                                   @NonNull Response<MessageResponse> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    showDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                customDialog.cancel();
                Toast.makeText(AddFoodActivity.this, R.string.something_went_wrong,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showDialog() {
        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_success, null);
        dialogBuilder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

        //Navigate with delay
        mDelayHandler.postDelayed(mRunnable, SPLASH_DELAY);
    }

    Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            startActivity(new Intent(AddFoodActivity.this, DietitianMainActivity.class));
            finishAffinity();
        }
    };

        private void addAdminFood() {
            if (customDialog != null)
                customDialog.show();

            HashMap<String, String> map = new HashMap<>();
            map.put("day", String.valueOf(selectedDay));
            map.put("food_id", String.valueOf(foodItem.getId()));

            Call<MessageResponse> call = apiInterface.addAdminFood(map);
            call.enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(@NonNull Call<MessageResponse> call,
                                       @NonNull Response<MessageResponse> response) {
                    customDialog.cancel();
                    if (response.isSuccessful()) {
                        showDialog();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                    customDialog.cancel();
                    Toast.makeText(AddFoodActivity.this, R.string.something_went_wrong,
                            Toast.LENGTH_SHORT).show();
                }
            });

        }

        private void galleryIntent(int type) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    EasyImage.openChooserWithDocuments(AddFoodActivity.this, "Select", type);
                else
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
            } else EasyImage.openChooserWithDocuments(AddFoodActivity.this, "Select", type);
        }

        private boolean validateProductDetails() {
            strProductName = etProductName.getText().toString().trim();
            strProductDescription = etDescription.getText().toString().trim();
            strProductPrice = etPrice.getText().toString().trim();

            if (strProductName == null || strProductName.isEmpty()) {
                Utils.displayMessage(this, getString(R.string.error_msg_product_name));
                return false;
            } else if (strProductDescription == null || strProductDescription.isEmpty()) {
                Utils.displayMessage(this, getString(R.string.error_msg_product_description));
                return false;
            } else if (strProductPrice == null || strProductPrice.isEmpty()) {
                Utils.displayMessage(this, getString(R.string.error_msg_product_price));
                return false;
            } else if (GlobalData.selectedIngredientsList.isEmpty()) {
                Utils.displayMessage(activity, getResources().getString(R.string.error_msg_select__ingredients));
                return false;
            } else if (featuredImageFile == null) {
                Utils.displayMessage(activity, getResources().getString(R.string.please_upload_image));
                return false;
            }
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
                    if (type == 1) {
//                    featuredImageFile = imageFiles.get(0);
                        try {
                            featuredImageFile = new Compressor(context).compressToFile(imageFiles.get(0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Glide.with(AddFoodActivity.this)
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
                mSelectedFeaturedImageId = data.getExtras().getString("image_id");
                mSelectedFeaturedImageUrl = data.getExtras().getString("image_url");
                Glide.with(this)
                        .load(mSelectedFeaturedImageUrl)
                        .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image)
                                .error(R.drawable.ic_place_holder_image).dontAnimate())
                        .into(featuredImg);
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            GlobalData.selectedIngredientsList = new ArrayList<>();
            GlobalData.ingredientsItemList = new ArrayList<>();
        }
    }
