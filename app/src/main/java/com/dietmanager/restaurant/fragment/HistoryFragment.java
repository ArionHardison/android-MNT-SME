package com.dietmanager.restaurant.fragment;


import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dietmanager.restaurant.R;
import com.dietmanager.restaurant.adapter.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {


    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    Unbinder unbinder;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);
        //ViewPager Adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new UpcomingVisitFragment(), getString(R.string.upcoming));
        adapter.addFragment(new PastVisitFragment(), getString(R.string.past));
        adapter.addFragment(new CancelOrderFragment(), getString(R.string.cancelled));
        viewPager.setAdapter(adapter);
        //set ViewPager
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
