package com.oyola.restaurant.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oyola.restaurant.R;
import com.oyola.restaurant.adapter.ImageGalleryAdapter;
import com.oyola.restaurant.model.ImageGallery;

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
    @BindView(R.id.imageRecyclerView)
    RecyclerView image_rv;
    Context context;
    List<ImageGallery> mGalleryList = new ArrayList<>();
    ImageGalleryAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        ButterKnife.bind(this);
        context = ImageGalleryActivity.this;
        title.setText(getResources().getString(R.string.library));
        backImg.setVisibility(View.VISIBLE);
        mGalleryList = (List<ImageGallery>) getIntent().getSerializableExtra("image_list");
        setupAdapter();
    }

    @OnClick({R.id.btnNoImage, R.id.back_img})
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                onBackPressed();
            case R.id.btnNoImage:
                showAlertDialog();
                break;
        }
    }

    private void setupAdapter() {
        mAdapter = new ImageGalleryAdapter(mGalleryList, context, this,false);
        image_rv.setLayoutManager(new GridLayoutManager(context, 4));
        image_rv.setHasFixedSize(true);
        image_rv.setAdapter(mAdapter);
    }

    @Override
    public void onImageSelected(ImageGallery mGallery) {
        String mSelectedId= String.valueOf(mGallery.getId());
        Intent intent = new Intent();
        intent.putExtra("image_id", mSelectedId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void navigateToImageScreen() {
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

}
