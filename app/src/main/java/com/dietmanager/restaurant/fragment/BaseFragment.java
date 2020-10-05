package com.dietmanager.restaurant.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dietmanager.restaurant.model.Order;

import java.util.Collections;
import java.util.List;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void sortOrdersToDescending(List<Order> orderList) {
        Collections.sort(orderList, (lhs, rhs) -> {
            if (rhs.getCreatedAtDate() == null || lhs.getCreatedAtDate() == null) {
                return 0;
            }
            return rhs.getCreatedAtDate().compareTo(lhs.getCreatedAtDate());
        });
    }
}
