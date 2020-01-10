package com.oyola.restaurant.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oyola.restaurant.R;
import com.oyola.restaurant.adapter.ImageGalleryAdapter;
import com.oyola.restaurant.model.ImageGallery;
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto;
import com.unsplash.pickerandroid.photopicker.presentation.UnsplashPickerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Prasanth on 14-11-2019.
 */
public class ImageGalleryActivity extends AppCompatActivity implements ImageGalleryAdapter.ImageSelectedListener {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.txt_load_more)
    TextView mTxtLoadMore;
    @BindView(R.id.imageRecyclerView)
    RecyclerView image_rv;
    Context context;
    List<ImageGallery> mGalleryList = new ArrayList<>();
    ImageGalleryAdapter mAdapter;
    boolean mIsFeatured = false;
    private final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        ButterKnife.bind(this);
        context = ImageGalleryActivity.this;
        title.setText(getResources().getString(R.string.library));
        backImg.setVisibility(View.VISIBLE);
        mGalleryList = (List<ImageGallery>) getIntent().getSerializableExtra("image_list");
        mIsFeatured = getIntent().getExtras().getBoolean("is_featured");
        setupAdapter();
        mTxtLoadMore.setPaintFlags(mTxtLoadMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @OnClick({R.id.btnNoImage, R.id.back_img, R.id.txt_load_more})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                onBackPressed();
            case R.id.btnNoImage:
                showAlertDialog();
                break;
            case R.id.txt_load_more:
                Intent intent = new Intent(this, UnsplashPickerActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    private void setupAdapter() {
        mAdapter = new ImageGalleryAdapter(mGalleryList, context, this, false, false);
        image_rv.setLayoutManager(new GridLayoutManager(context, 4));
        image_rv.setHasFixedSize(true);
        image_rv.setAdapter(mAdapter);
    }

    @Override
    public void onImageSelected(ImageGallery mGallery, boolean isFeatured) {
        String mSelectedId = String.valueOf(mGallery.getId());
        Intent intent = new Intent();
        intent.putExtra("image_id", mSelectedId);
        intent.putExtra("image_url", mGallery.getImage());
        intent.putExtra("is_featured", mIsFeatured);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void navigateToImageScreen(boolean isFeatured) {
        Toast.makeText(context, "selected", Toast.LENGTH_SHORT).show();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ImageGalleryActivity.this);
        final FrameLayout frameView = new FrameLayout(ImageGalleryActivity.this);
        builder.setView(frameView);
        final AlertDialog alertDialog = builder.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_mail, frameView);
        alertDialog.setCancelable(true);
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null) {

            ArrayList<UnsplashPhoto> mList = new ArrayList<>();
            mList.clear();
            mList = data.getParcelableArrayListExtra(UnsplashPickerActivity.EXTRA_PHOTOS);
            if (mList != null && mList.size() > 0) {
                String mImageUrl = "";
                Intent intent = new Intent();
                intent.putExtra("image_id", "");
                if (mList.get(0).getUrls().getSmall() != null) {
                    mImageUrl = mList.get(0).getUrls().getSmall();
                } else if (mList.get(0).getUrls().getRegular() != null) {
                    mImageUrl = mList.get(0).getUrls().getRegular();
                }else if (mList.get(0).getUrls().getFull() != null) {
                    mImageUrl = mList.get(0).getUrls().getFull();
                }
                intent.putExtra("image_url", mImageUrl);
                intent.putExtra("is_featured", mIsFeatured);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Intent intent = new Intent();
                intent.putExtra("image_id", "");
                intent.putExtra("image_url", "");
                intent.putExtra("is_featured", mIsFeatured);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
