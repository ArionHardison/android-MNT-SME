package com.tomoeats.restaurant.fragment;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tomoeats.restaurant.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterDialogFragment extends DialogFragment {


    @BindView(R.id.reset_img)
    ImageView resetImg;
    @BindView(R.id.status_spin)
    Spinner statusSpin;
    @BindView(R.id.txt_from_date)
    TextView txtFromDate;
    @BindView(R.id.from_date_lay)
    LinearLayout fromDateLay;
    @BindView(R.id.to_date_txt)
    TextView toDateTxt;
    @BindView(R.id.to_date_lay)
    LinearLayout toDateLay;
    @BindView(R.id.filter_btn)
    Button filterBtn;
    Unbinder unbinder;
    @BindView(R.id.root_layout)
    LinearLayout rootLayout;

    public FilterDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.reset_img, R.id.from_date_lay, R.id.to_date_lay, R.id.filter_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reset_img:
                break;
            case R.id.from_date_lay:
                break;
            case R.id.to_date_lay:
                break;
            case R.id.filter_btn:
                dismiss();
                break;
        }
    }

}
