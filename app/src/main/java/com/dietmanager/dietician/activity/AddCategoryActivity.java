package com.dietmanager.dietician.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.ImageGalleryAdapter;
import com.dietmanager.dietician.helper.ConnectionHelper;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.helper.SharedHelper;
//import com.comida.outlet.imagecompressor.Compressor;
import com.dietmanager.dietician.model.Category;
import com.dietmanager.dietician.model.Cuisine;
import com.dietmanager.dietician.model.Image;
import com.dietmanager.dietician.model.ImageGallery;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Constants;
import com.dietmanager.dietician.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import id.zelory.compressor.Compressor;

import static com.dietmanager.dietician.application.MyApplication.ASK_MULTIPLE_PERMISSION_REQUEST_CODE;

public class AddCategoryActivity extends AppCompatActivity implements ImageGalleryAdapter.ImageSelectedListener {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.et_addons_name)
    EditText etAddonsName;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.status_spin)
    MaterialSpinner statusSpin;
    @BindView(R.id.category_img)
    ImageView categoryImg;
    @BindView(R.id.save_btn)
    Button saveBtn;
    @BindView(R.id.category_order_picker)
    EditText categoryOrderPicker;
    @BindView(R.id.imageRecyclerView)
    RecyclerView image_rv;
    @BindView(R.id.lay_existing_image)
    LinearLayout layoutExistingImage;
    public static final int PICK_IMAGE_REQUEST = 100;
    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    File categoryImageFile = null;
    ArrayList<String> lstItems = new ArrayList<>();
    String strCategoryName, strDescription, strCategoryOrder, strStatus;
    private Category categoryDetails;
    String mSelectedImageId, mSelectedShopImageUrl = "";
    ArrayList<ImageGallery> mImageList = new ArrayList<>();
    ImageGalleryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        ButterKnife.bind(this);

        title.setText(getString(R.string.create_category));
        backImg.setVisibility(View.VISIBLE);
        context = AddCategoryActivity.this;
        activity = AddCategoryActivity.this;
        connectionHelper = new ConnectionHelper(context);
        customDialog = new CustomDialog(context);
        setSpinnerAdpater();

        if (getIntent() != null && getIntent().getExtras() != null) {
            Category category = (Category) getIntent().getParcelableExtra("Category");
            if (category != null) {
                setCategoryDetails(category);
            }
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
        customDialog.show();
        getImageGallery();
    }

    private void setSpinnerAdpater() {
        lstItems.add(getString(R.string.enable));
        lstItems.add(getString(R.string.disable));
        statusSpin.setItems(lstItems);
        statusSpin.setOnItemSelectedListener((view, position, id, item) -> {

        });
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
                Utils.displayMessage(AddCategoryActivity.this, getString(R.string.something_went_wrong));
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
        mAdapter = new ImageGalleryAdapter(mGalleryList, context, this, true, false);
        image_rv.setLayoutManager(new GridLayoutManager(context, 4));
        image_rv.setHasFixedSize(true);
        image_rv.setAdapter(mAdapter);
    }

    @OnClick({R.id.back_img, R.id.category_img, R.id.save_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.category_img:
//                galleryIntent();
                break;
            case R.id.save_btn:
                if (validateFields()) {
                    if (connectionHelper.isConnectingToInternet()) {
                        String shop_id = SharedHelper.getKey(this, Constants.PREF.PROFILE_ID);
                        HashMap<String, RequestBody> params = new HashMap<>();
                        params.put("name", RequestBody.create(MediaType.parse("text/plain"), strCategoryName));
                        params.put("description", RequestBody.create(MediaType.parse("text/plain"), strDescription));
                        params.put("status", RequestBody.create(MediaType.parse("text/plain"), strStatus.toLowerCase()));
                        params.put("shop_id", RequestBody.create(MediaType.parse("text/plain"), shop_id));
                        params.put("position", RequestBody.create(MediaType.parse("text/plain"), strCategoryOrder));
//                        params.put("image_gallery_id", RequestBody.create(MediaType.parse("text/plain"), mSelectedImageId));
                        params.put("image_gallery_img", RequestBody.create(MediaType.parse("text/plain"), mSelectedShopImageUrl));
                        /*if (isProductImageChanged) {
                            params.put("image_gallery_id", RequestBody.create(MediaType.parse("text/plain"), mSelectedProductImageId));
                        }*/
                        addCategory(params);
                    } else {
                        Utils.displayMessage(this, getString(R.string.oops_no_internet));
                    }
                }

                break;
        }
    }

    private boolean validateFields() {

        strCategoryName = etAddonsName.getText().toString().trim();
        strDescription = etDescription.getText().toString().trim();
        strCategoryOrder = categoryOrderPicker.getText().toString().trim();
        strStatus = lstItems.get(statusSpin.getSelectedIndex());

        if (strCategoryName.isEmpty()) {
            Utils.displayMessage(this, getString(R.string.please_enter_category_name));
            return false;
        } else if (strDescription.isEmpty()) {
            Utils.displayMessage(this, getString(R.string.please_enter_category_description));
            return false;
        } else if (strStatus.isEmpty()) {
            Utils.displayMessage(this, getString(R.string.please_select_status));
            return false;
        }
        /*else if (categoryImageFile == null) {
            Utils.displayMessage(this, getString(R.string.please_select_category_image));
            return false;
        }*/
        return true;
    }

    private void addCategory(HashMap<String, RequestBody> params) {
        customDialog.show();
        MultipartBody.Part filePart = null;
        if (categoryImageFile != null) {
            try {
                categoryImageFile = new Compressor(context).compressToFile(categoryImageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            filePart = MultipartBody.Part.createFormData("image", categoryImageFile.getName(),
                    RequestBody.create(MediaType.parse("image/*"), categoryImageFile));
        }
        Call<Category> call = null;
        if (categoryDetails != null) {
            params.put("_method", RequestBody.create(MediaType.parse("text/plain"), "PATCH"));
            call = apiInterface.updateCategory(categoryDetails.getId(), params, filePart);
        } else {
            call = apiInterface.addCategory(params, filePart);
        }

        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    Utils.displayMessage(AddCategoryActivity.this, getString(R.string.category_added_successfully));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onBackPressed();
                        }
                    }, 1000);
                } else {
                    Utils.displayMessage(AddCategoryActivity.this, "failed");
                    if (response.code() == 401) {
                        context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        activity.finish();
                    }
                }

            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(AddCategoryActivity.this, t.toString());
            }
        });
    }

    private void setCategoryDetails(Category categoryDetails) {
        this.categoryDetails = categoryDetails;
        title.setText(R.string.edit_category);

        if (categoryDetails.getName() != null)
            etAddonsName.setText(categoryDetails.getName());
        if (categoryDetails.getDescription() != null)
            etDescription.setText(categoryDetails.getDescription());

        String categoryOrder = String.valueOf(categoryDetails.getPosition());
        if (categoryOrder != null && !categoryOrder.equalsIgnoreCase("null"))
            categoryOrderPicker.setText(categoryOrder);
        else
            categoryOrderPicker.setText("");

        if (categoryDetails.getImages() != null && categoryDetails.getImages().size() > 0) {
            List<Image> images = categoryDetails.getImages();
            if (images != null && images.size() > 0) {
                layoutExistingImage.setVisibility(View.VISIBLE);
                String img = images.get(0).getUrl();
//                mSelectedImageId= String.valueOf(images.get(0).getImageGalleryId());
                mSelectedShopImageUrl = img;
               /* Glide.with(context).load(img)
                        .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image)
                                .error(R.drawable.ic_place_holder_image).dontAnimate()).into(categoryImg);
*/
               /* Glide.with(this)
                        .asBitmap()
                        .load(img)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                categoryImg.setImageBitmap(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });*/
                Glide.with(this)
                        .load(img)
                        .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image)
                                .error(R.drawable.ic_place_holder_image).dontAnimate())
                        .into(categoryImg);
            }
        }

        if (categoryDetails.getStatus() != null) {
            if (categoryDetails.getStatus().equalsIgnoreCase(getString(R.string.enable)))
                statusSpin.setSelectedIndex(0);
            else
                statusSpin.setSelectedIndex(1);
        }
    }

    private void galleryIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                EasyImage.openChooserWithDocuments(AddCategoryActivity.this, "Select", 0);
            else
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        } else EasyImage.openChooserWithDocuments(AddCategoryActivity.this, "Select", 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean permission1 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean permission2 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (permission1 && permission2) galleryIntent();
                    else
                        Toast.makeText(getApplicationContext(), "Please give permission", Toast.LENGTH_SHORT).show();
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
                try {
                    categoryImageFile = new Compressor(context).compressToFile(imageFiles.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Glide.with(context)
                        .load(imageFiles.get(0))
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.ic_place_holder_image)
                                .error(R.drawable.ic_place_holder_image).dontAnimate())
                        .into(categoryImg);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
//            mSelectedImageId = data.getExtras().getString("image_id");
            mSelectedShopImageUrl = data.getExtras().getString("image_url");
            layoutExistingImage.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(mSelectedShopImageUrl)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image)
                            .error(R.drawable.ic_place_holder_image).dontAnimate())
                    .into(categoryImg);
        }
    }

    @Override
    public void onImageSelected(ImageGallery mGallery, boolean isFeatured) {
//        mSelectedImageId = String.valueOf(mGallery.getId());
        mSelectedShopImageUrl = mGallery.getImage();
        layoutExistingImage.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(mSelectedShopImageUrl)
                .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image)
                        .error(R.drawable.ic_place_holder_image).dontAnimate())
                .into(categoryImg);
    }

    @Override
    public void navigateToImageScreen(boolean isFeatured) {
        Intent intent = new Intent(context, ImageGalleryActivity.class);
        intent.putExtra("image_list", mImageList);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
}
