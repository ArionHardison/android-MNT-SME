package com.dietmanager.dietician.activity;

import android.graphics.Typeface;
import android.os.Bundle;

import com.dietmanager.dietician.fragment.OngoingFragment;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.adapter.ViewPagerAdapter;
import com.dietmanager.dietician.fragment.CancelOrderFragment;
import com.dietmanager.dietician.fragment.PastVisitFragment;
import com.dietmanager.dietician.fragment.UpcomingVisitFragment;
import com.dietmanager.dietician.helper.CustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class HistoryActivity extends AppCompatActivity {

    public static CustomDialog customDialog;
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    public static void showDialog() {
        if (customDialog != null && !customDialog.isShowing()) {
            customDialog.setCancelable(false);
            customDialog.show();
        }
    }

    public static void dismissDialog() {
        if (customDialog != null & customDialog.isShowing())
            customDialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        backImg.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.history));

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new OngoingFragment(), getString(R.string.ongoing_order));
        adapter.addFragment(new UpcomingVisitFragment(), getString(R.string.upcoming_order));
        adapter.addFragment(new PastVisitFragment(), getString(R.string.past_order));
        adapter.addFragment(new CancelOrderFragment(), getString(R.string.cancelled_order));
        viewPager.setAdapter(adapter);
        //set ViewPager
        tabLayout.setupWithViewPager(viewPager);
        changeTabsFont();
        viewPager.setOffscreenPageLimit(3);
        customDialog = new CustomDialog(this);
    }

    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    Typeface custom_font = ResourcesCompat.getFont(getApplicationContext(), R.font.nunito_semibold);
                    ((TextView) tabViewChild).setTypeface(custom_font);
                }
            }
        }
    }

    @OnClick(R.id.back_img)
    public void onViewClicked() {
        finish();
    }
}
