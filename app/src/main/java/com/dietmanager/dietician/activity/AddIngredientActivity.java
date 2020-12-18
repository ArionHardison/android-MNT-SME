package com.dietmanager.dietician.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.config.AppConfigure;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.model.Addon;
import com.dietmanager.dietician.model.MessageResponse;
import com.dietmanager.dietician.model.ServerError;
import com.dietmanager.dietician.model.SmallMessageResponse;
import com.dietmanager.dietician.model.ingredients.IngredientsItem;
import com.dietmanager.dietician.model.ingredients.UnitType;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Utils;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.gson.Gson;
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


public class AddIngredientActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.et_ingrdient_name)
    EditText etIngredientName;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.add_btn)
    Button addBtn;
    public static final int PICK_IMAGE_REQUEST = 100;

    String mSelectedFeaturedImageId, mSelectedFeaturedImageUrl = "";
    File featuredImageFile;
    @BindView(R.id.ingredient_img)
    ImageView ingredientImg;
    @BindView(R.id.unit_type_spin)
    MaterialSpinner unitTypeSpin;
    Context context;
    Activity activity;
    private boolean isEdit = false;
    private IngredientsItem ingredientsItem = null;

    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    String TAG = "AddSubscriptionPlanActivity";
    String strIngredientName, strPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);
        ButterKnife.bind(this);
        setUp();
    }


    private List<UnitType> unitTypeList = new ArrayList<>();

    private void getUnitTypeList() {
        customDialog.show();
        Call<List<UnitType>> call = apiInterface.getUnitTypeList();
        call.enqueue(new Callback<List<UnitType>>() {
            @Override
            public void onResponse(Call<List<UnitType>> call, Response<List<UnitType>> response) {
                customDialog.cancel();
                if (response.isSuccessful()) {
                    unitTypeList.clear();
                    List<UnitType> subscribedModel = response.body();
                    if (subscribedModel != null) {
                        if (subscribedModel.size() > 0) {
                            unitTypeList.addAll(subscribedModel);
                        }
                        setUnitTypeSpinner();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UnitType>> call, Throwable t) {
                customDialog.cancel();
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });
    }

    int selected_pos = 0;
    ArrayList<String> lstUnitTypeNames = new ArrayList<String>();

    private void setUnitTypeSpinner() {
        for (int i = 0; i < unitTypeList.size(); i++) {
            lstUnitTypeNames.add(unitTypeList.get(i).getName());
            if(ingredientsItem!=null&&unitTypeList.get(i).getId()==ingredientsItem.getUnitTypeId()){
                selected_pos=i;
            }
        }
        unitTypeSpin.setItems(lstUnitTypeNames);
        unitTypeSpin.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selected_pos = position;
            }
        });
        unitTypeSpin.setSelectedIndex(selected_pos);
    }



    @SuppressLint("ClickableViewAccessibility")
    private void setUp() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isEdit = bundle.getBoolean("isEdit", false);
        }
        if(isEdit){
            ingredientsItem = (IngredientsItem) bundle.getSerializable("ingredientItem");
            title.setText(getString(R.string.edit_ingredient));
            addBtn.setText(getString(R.string.save));
            etIngredientName.setText(ingredientsItem.getName());
            etPrice.setText(ingredientsItem.getPrice());
            Glide.with(this)
                    .load(AppConfigure.BASE_URL + ingredientsItem.getAvatar())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.shimmer_bg)
                            .error(R.drawable.shimmer_bg))
                    .into(ingredientImg);

        }else {
            title.setText(getString(R.string.add_ingredient));
        }
        backImg.setVisibility(View.VISIBLE);
        context = AddIngredientActivity.this;
        activity = AddIngredientActivity.this;
        customDialog = new CustomDialog(context);

        getUnitTypeList();
    }


    @OnClick({R.id.back_img, R.id.ingredient_img, R.id.add_btn,R.id.imgDelete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.imgDelete:
                AlertDialog.Builder cancelAlert = new AlertDialog.Builder(context);
                cancelAlert.setTitle(context.getResources().getString(R.string.ingredient));
                cancelAlert.setMessage(context.getResources().getString(R.string.are_you_sure_want_to_delete_ingredient));
                cancelAlert.setPositiveButton(context.getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteIngredient();
                        dialog.dismiss();
                    }
                });
                cancelAlert.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                cancelAlert.show();
                break;
            case R.id.ingredient_img:
                galleryIntent(1);
                break;
            case R.id.add_btn:
                if (validatePlanDetails()) {
                    addIngredient();
                }
                break;
        }
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

                    Glide.with(AddIngredientActivity.this)
                            .load(imageFiles.get(0))
                            .apply(new RequestOptions().centerCrop()
                                    .placeholder(R.drawable.ic_document_placeholder)
                                    .error(R.drawable.ic_document_placeholder).dontAnimate())
                            .into(ingredientImg);
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
                    .apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_place_holder_image)
                            .error(R.drawable.ic_place_holder_image).dontAnimate())
                    .into(ingredientImg);
        }
    }

    private void galleryIntent(int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                EasyImage.openChooserWithDocuments(AddIngredientActivity.this, "Select", type);
            else
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        } else EasyImage.openChooserWithDocuments(AddIngredientActivity.this, "Select", type);
    }

    public void deleteIngredient() {
        customDialog.show();
        Call<SmallMessageResponse> call = apiInterface.deleteIngredient(ingredientsItem.getId());
        call.enqueue(new Callback<SmallMessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<SmallMessageResponse> call, @NonNull Response<SmallMessageResponse> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(AddIngredientActivity.this, response.body().getMessage(),
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(activity, serverError.getError());
                        if (response.code() == 401)
                        {context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            activity.finish();}
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(activity, getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SmallMessageResponse> call, @NonNull Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(activity, getString(R.string.something_went_wrong));
            }
        });
    }

    private void addIngredient() {
        if (customDialog != null)
            customDialog.show();

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("name", RequestBody.create(MediaType.parse("text/plain"),String.valueOf(strIngredientName)));
        map.put("price", RequestBody.create(MediaType.parse("text/plain"),String.valueOf(strPrice)));
        map.put("unit_type", RequestBody.create(MediaType.parse("text/plain"),String.valueOf(unitTypeList.get(selected_pos).getId())));

        MultipartBody.Part filePart = null;

        if (featuredImageFile != null)
            filePart = MultipartBody.Part.createFormData("avatar", featuredImageFile.getName(),
                    RequestBody.create(MediaType.parse("image/*"), featuredImageFile));

        if(isEdit){
            map.put("_method",RequestBody.create(MediaType.parse("text/plain"),"PATCH"));
            Call<SmallMessageResponse> call = apiInterface.editIngredient(map,filePart, ingredientsItem.getId());
            call.enqueue(new Callback<SmallMessageResponse>() {
                @Override
                public void onResponse(@NonNull Call<SmallMessageResponse> call,
                                       @NonNull Response<SmallMessageResponse> response) {
                    customDialog.cancel();
                    if (response.isSuccessful()) {
                        Toast.makeText(AddIngredientActivity.this, response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SmallMessageResponse> call, @NonNull Throwable t) {
                    customDialog.cancel();
                    Toast.makeText(AddIngredientActivity.this, R.string.something_went_wrong,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Call<SmallMessageResponse> call = apiInterface.addIngredient(map,filePart);
            call.enqueue(new Callback<SmallMessageResponse>() {
                @Override
                public void onResponse(@NonNull Call<SmallMessageResponse> call,
                                       @NonNull Response<SmallMessageResponse> response) {
                    customDialog.cancel();
                    if (response.isSuccessful()) {
                        Toast.makeText(AddIngredientActivity.this, response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SmallMessageResponse> call, @NonNull Throwable t) {
                    customDialog.cancel();
                    Toast.makeText(AddIngredientActivity.this, R.string.something_went_wrong,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private boolean validatePlanDetails() {
        strIngredientName = etIngredientName.getText().toString().trim();
        strPrice = etPrice.getText().toString().trim();
        if (strIngredientName == null || strIngredientName.isEmpty()) {
            Utils.displayMessage(this, getString(R.string.error_msg_plan_name));
            return false;
        } else if (strPrice == null || strPrice.isEmpty()) {
            Utils.displayMessage(this, getString(R.string.error_msg_plan_price));
            return false;
        } else if (CollectionUtils.isEmpty(unitTypeList)) {
            Utils.displayMessage(activity, getResources().getString(R.string.please_add_unit_type));
            return false;
        } else if (!isEdit&&featuredImageFile == null) {
        Utils.displayMessage(activity, getResources().getString(R.string.please_upload_image));
        return false;
    }
        return true;
    }
}