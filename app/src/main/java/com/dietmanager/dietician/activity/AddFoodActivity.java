package com.dietmanager.dietician.activity;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.ImageGalleryAdapter;
import com.dietmanager.dietician.helper.ConnectionHelper;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.helper.MultiSelectionSpinner;
import com.dietmanager.dietician.helper.SharedHelper;
import com.dietmanager.dietician.messages.ProductMessage;
import com.dietmanager.dietician.model.MessageResponse;
import com.dietmanager.dietician.model.Product;
import com.dietmanager.dietician.model.Profile;
import com.dietmanager.dietician.model.ProfileError;
import com.dietmanager.dietician.model.ServerError;
import com.dietmanager.dietician.model.SpinnerItem;
import com.dietmanager.dietician.model.ingredients.IngredientsItem;
import com.dietmanager.dietician.model.product.ProductResponse;
import com.dietmanager.dietician.model.timecategory.TimeCategoryItem;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

public class AddFoodActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.et_product_name)
    EditText etProductName;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.choose_food_time_spin)
    MaterialSpinner foodTimeSpin;
    @BindView(R.id.ingredients_spin)
    MultiSelectionSpinner ingredientsSpin;
    @BindView(R.id.featured_img)
    ImageView featuredImg;
    @BindView(R.id.next_btn)
    Button nextBtn;
    @BindView(R.id.rlFeaturedImage)
    RelativeLayout rlFeaturedImage;
    public static final int PICK_IMAGE_REQUEST = 100;
    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    String TAG = "AddFoodActivity";
    String strProductName, strProductDescription,strProductPrice;
    List<IngredientsItem> ingredientsItemList=new ArrayList<>();
    ArrayList<String> lstTimeCategory = new ArrayList<String>();
    private List<TimeCategoryItem> timeCategoryList = new ArrayList<>();
    private int selectedDay = 1;
    File featuredImageFile;
    String mSelectedFeaturedImageId, mSelectedFeaturedImageUrl = "";
    private int selectedTimeCategory = 0;
    ArrayList<SpinnerItem>  itemSpinnerList =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        ButterKnife.bind(this);
        setUp();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUp() {
        title.setText(getString(R.string.add_food));
        backImg.setVisibility(View.VISIBLE);
        context = AddFoodActivity.this;
        activity = AddFoodActivity.this;
        connectionHelper = new ConnectionHelper(context);
        customDialog = new CustomDialog(context);
        customDialog.show();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedTimeCategory = bundle.getInt("selectedTimeCategory");
            selectedDay = bundle.getInt("selectedDay");
            timeCategoryList = (List<TimeCategoryItem>) bundle.getSerializable("timeCategoryList");
        }
        setTimeCategorySpinner();

        if (connectionHelper.isConnectingToInternet())
            getIngredients();
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
    }


    private void setTimeCategorySpinner() {
        int selectedIndex = 0;
        for (int i = 0; i < timeCategoryList.size(); i++) {
            TimeCategoryItem timeCategoryItem = timeCategoryList.get(i);
            lstTimeCategory.add(timeCategoryItem.getName());
            if (selectedTimeCategory == timeCategoryItem.getId())
                selectedIndex = i;
        }
        foodTimeSpin.setItems(lstTimeCategory);
        foodTimeSpin.setSelectedIndex(selectedIndex);
        foodTimeSpin.setOnItemSelectedListener(new AddFoodActivity.CommonOnItemSelectListener());
    }

    private void setIngredientSpinner() {
        customDialog.dismiss();
        ingredientsSpin.setItems(itemSpinnerList);
        ingredientsSpin.setLabel(getString(R.string.select_ingredients));
    }


    @OnClick({R.id.back_img, R.id.rlFeaturedImage, R.id.next_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;

            case R.id.rlFeaturedImage:
                galleryIntent(1);
                break;

            case R.id.next_btn:
                if (validateProductDetails()) {
                    addFood();
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
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ingredientsItemList = response.body();
                        itemSpinnerList.clear();
                        if (ingredientsItemList.size() > 0) {
                            for (int i = 0; i < ingredientsItemList.size(); i++) {
                                IngredientsItem item = ingredientsItemList.get(i);
                                itemSpinnerList.add(new SpinnerItem(item.getName(),false,item.getId(),item.getPrice()));
                            }
                        }
                        setIngredientSpinner();
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
        for (int i=0;i<ingredientsSpin.getSelectedItems().size();i++)
        {
            SpinnerItem item=ingredientsSpin.getSelectedItems().get(i);
            map.put("ingredient[0]["+i+"]", RequestBody.create(MediaType.parse("text/plain"),
                    String.valueOf(item.getId())));
            map.put("i_price[0]["+i+"]", RequestBody.create(MediaType.parse("text/plain"),
                    String.valueOf(item.getPrice())));
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
                    Toast.makeText(AddFoodActivity.this,response.body().getMessage(),
                            Toast.LENGTH_SHORT).show();
                    finish();
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
        }else if (strProductPrice == null || strProductPrice.isEmpty()) {
            Utils.displayMessage(this, getString(R.string.error_msg_product_price));
            return false;
        } else if (ingredientsSpin.getSelectedItems().isEmpty()) {
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

    class CommonOnItemSelectListener implements MaterialSpinner.OnItemSelectedListener {
        @Override
        public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
            switch (view.getId()) {
                case R.id.choose_food_time_spin:
                    selectedTimeCategory = timeCategoryList.get(position).getId();
                    break;
            }
        }
    }
}
