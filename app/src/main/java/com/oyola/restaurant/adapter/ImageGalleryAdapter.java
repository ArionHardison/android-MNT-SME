package com.oyola.restaurant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.oyola.restaurant.R;
import com.oyola.restaurant.model.ImageGallery;

import java.util.List;

/**
 * Created by Prasanth on 13-11-2019.
 */
public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder> {

    List<ImageGallery> mList;
    Context mContext;
    private int selectedIndex = -1;
    ImageSelectedListener mListener;
    boolean mIsFromRegister = false;
    boolean mIsFromFeatured = false;

    public ImageGalleryAdapter(List<ImageGallery> list, Context context, ImageSelectedListener listener,
                               boolean isFrom, boolean isFromFeatured) {
        this.mList = list;
        this.mContext = context;
        this.mListener = listener;
        this.mIsFromRegister = isFrom;
        this.mIsFromFeatured = isFromFeatured;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_image, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        if (i < mList.size()) {
            ImageGallery gallery = mList.get(i);
            if (gallery != null) {
                Glide.with(mContext).load(gallery.getImage())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder_image)
                                .error(R.drawable.ic_place_holder_image).dontAnimate()).into(holder.mImage);
                holder.mImageSelect.setChecked(gallery.isSelected());
            }
        } else {
            holder.mImage.setImageResource(R.drawable.ic_placeholder_image_upload);
            holder.mImageSelect.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        if (mIsFromRegister) {
            return mList.size() + 1;
        } else {
            return mList.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImage;
        CheckBox mImageSelect;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.imgImage);
            mImageSelect = itemView.findViewById(R.id.chkSelect);
            mImageSelect.setOnClickListener(this::onClick);
            mImage.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgImage:
                case R.id.chkSelect:
                    if (mListener != null) {
                        if (selectedIndex >= 0 && selectedIndex < mList.size()) {
                            mList.get(selectedIndex).setSelected(false);
                            notifyItemChanged(selectedIndex);
                        }
                        selectedIndex = getAdapterPosition();
                        if (selectedIndex < mList.size()) {
                            mList.get(selectedIndex).setSelected(true);
                            notifyItemChanged(selectedIndex);
                            mListener.onImageSelected(mList.get(selectedIndex), mIsFromFeatured);
                        } else {
                            mListener.navigateToImageScreen(mIsFromFeatured);
                        }
                    }
                    break;
            }
        }
    }

    public interface ImageSelectedListener {
        void onImageSelected(ImageGallery mGallery, boolean isFeatured);

        void navigateToImageScreen(boolean isFeatured);
    }
}
