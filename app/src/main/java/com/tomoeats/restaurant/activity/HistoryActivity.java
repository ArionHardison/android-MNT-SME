package com.tomoeats.restaurant.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomoeats.restaurant.R;
import com.tomoeats.restaurant.adapter.ViewPagerAdapter;
import com.tomoeats.restaurant.fragment.CancelOrderFragment;
import com.tomoeats.restaurant.fragment.PastVisitFragment;
import com.tomoeats.restaurant.fragment.UpcomingVisitFragment;
import com.tomoeats.restaurant.helper.CustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    public static CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        backImg.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.history));

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UpcomingVisitFragment(),getString(R.string.upcoming));
        adapter.addFragment(new PastVisitFragment(), getString(R.string.past));
        adapter.addFragment(new CancelOrderFragment(), getString(R.string.cancelled));
        viewPager.setAdapter(adapter);
        //set ViewPager
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        customDialog = new CustomDialog(this);
    }

    @OnClick(R.id.back_img)
    public void onViewClicked() {
        finish();
    }

    public static void showDialog(){
        if(customDialog!=null && !customDialog.isShowing()){
        customDialog.setCancelable(false);
        customDialog.show();
    }
    }

    public static void dismissDialog(){
        if(customDialog!=null & customDialog.isShowing())
            customDialog.dismiss();
    }
}
