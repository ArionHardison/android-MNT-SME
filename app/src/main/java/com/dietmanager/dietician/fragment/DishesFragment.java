package com.dietmanager.dietician.fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.activity.AddOnsActivity;
import com.dietmanager.dietician.activity.CategoryActivity;
import com.dietmanager.dietician.activity.ProductsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class DishesFragment extends Fragment {


    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.add_ons_lay)
    CardView addOnsLay;
    @BindView(R.id.caregory_lay)
    CardView caregoryLay;
    @BindView(R.id.product_lay)
    CardView productLay;
    Unbinder unbinder;

    public DishesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dishes, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title.setText(getString(R.string.dishes));


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.add_ons_lay, R.id.caregory_lay, R.id.product_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_ons_lay:
                startActivity(new Intent(getContext(), AddOnsActivity.class));
                break;
            case R.id.caregory_lay:
                startActivity(new Intent(getContext(), CategoryActivity.class));
                break;
            case R.id.product_lay:
                startActivity(new Intent(getContext(), ProductsActivity.class));
                break;
        }
    }
}
