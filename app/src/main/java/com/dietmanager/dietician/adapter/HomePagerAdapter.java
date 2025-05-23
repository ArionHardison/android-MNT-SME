package com.dietmanager.dietician.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dietmanager.dietician.fragment.DishesFragment;
import com.dietmanager.dietician.fragment.HomeFragment;
import com.dietmanager.dietician.fragment.RevenueFragment;
import com.dietmanager.dietician.fragment.SettingFragment;
import com.dietmanager.dietician.fragment.TakeAwayFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new TakeAwayFragment();
            case 2:
                return new RevenueFragment();
            case 3:
                return new DishesFragment();
            case 4:
                return new SettingFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
