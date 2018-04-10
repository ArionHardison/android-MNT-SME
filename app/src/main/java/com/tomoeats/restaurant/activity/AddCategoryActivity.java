package com.tomoeats.restaurant.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tomoeats.restaurant.R;
import com.tomoeats.restaurant.helper.ConnectionHelper;
import com.tomoeats.restaurant.helper.CustomDialog;
import com.tomoeats.restaurant.helper.GlobalData;
import com.tomoeats.restaurant.model.Category;
import com.tomoeats.restaurant.model.Image;
import com.tomoeats.restaurant.network.ApiClient;
import com.tomoeats.restaurant.network.ApiInterface;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.tomoeats.restaurant.application.MyApplication.ASK_MULTIPLE_PERMISSION_REQUEST_CODE;

public class AddCategoryActivity extends AppCompatActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.et_addons_name)
    EditText etAddonsName;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.status_spin)
    Spinner statusSpin;
    @BindView(R.id.category_img)
    ImageView categoryImg;
    @BindView(R.id.save_btn)
    Button saveBtn;
    @BindView(R.id.category_order_picker)
    EditText categoryOrderPicker;

    Context context;
    Activity activity;
    ConnectionHelper connectionHelper;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    private Category categoryDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        ButterKnife.bind(this);

        title.setText(getString(R.string.add_category));
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
    }

    private void setSpinnerAdpater() {
        String[] status = {getString(R.string.enable), getString(R.string.disable)};
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, status);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpin.setAdapter(aa);
    }

    @OnClick({R.id.back_img, R.id.category_img, R.id.save_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.category_img:
                galleryIntent();
                break;
            case R.id.save_btn:
                onBackPressed();
                break;
        }
    }

    public void setCategoryDetails(Category categoryDetails) {
        this.categoryDetails = categoryDetails;
        if (categoryDetails.getName() != null)
            etAddonsName.setText(categoryDetails.getName());
        if (categoryDetails.getDescription() != null)
            etDescription.setText(categoryDetails.getDescription());
        categoryOrderPicker.setText(String.valueOf(categoryDetails.getPosition()));

        if (categoryDetails.getImages() != null && categoryDetails.getImages().size() > 0) {
            List<Image> images = categoryDetails.getImages();
            if (images != null && images.size() > 0) {
                String img = images.get(0).getUrl();
                Glide.with(context).load(img)
                        .apply(new RequestOptions().placeholder(R.drawable.delete_shop).error(R.drawable.delete_shop).dontAnimate()).into(categoryImg);
            }
        }

        if (categoryDetails.getStatus() != null) {
            if (categoryDetails.getStatus().equalsIgnoreCase(getString(R.string.enable)))
                statusSpin.setSelection(0);
            else
                statusSpin.setSelection(1);
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
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                GlobalData.REGISTER_AVATAR = imageFile;
                Glide
                        .with(context)
                        .load(imageFile)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.delete_shop)
                                .error(R.drawable.delete_shop).dontAnimate())
                        .into(categoryImg);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
    }

}
