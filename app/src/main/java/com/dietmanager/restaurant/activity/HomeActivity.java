package com.dietmanager.restaurant.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.dietmanager.restaurant.R;
import com.dietmanager.restaurant.adapter.HomePagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;

    private HomePagerAdapter pagerAdapter;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        pagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        String notification = getIntent().getStringExtra("Notification");
        if (notification != null && notification.isEmpty()) {
            bottomNavigation.setCurrentItem(0);
        }

        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.empty_string, R.drawable.salver, R.color.grey);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.empty_string, R.drawable.ic_bag, R.color.grey);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.empty_string, R.drawable.cash, R.color.grey);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.empty_string, R.drawable.options, R.color.grey);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.empty_string, R.drawable.shop, R.color.grey);
        // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);
        // Set background color
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.background_color));

        // Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);

        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        bottomNavigation.setAccentColor(getResources().getColor(R.color.colorPrimaryDark));
        bottomNavigation.setInactiveColor(getResources().getColor(R.color.bottomSheetInActiveColor));

        bottomNavigation.setCurrentItem(0);

        // Set listeners
        bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            viewPager.setCurrentItem(position);
            return true;
        });

    }
}
