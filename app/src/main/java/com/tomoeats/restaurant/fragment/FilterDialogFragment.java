package com.tomoeats.restaurant.fragment;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.tomoeats.restaurant.R;
import com.tomoeats.restaurant.model.Transporter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterDialogFragment extends DialogFragment implements CalendarDatePickerDialogFragment.OnDateSetListener {

    String TAG = FilterDialogFragment.this.getClass().getSimpleName();

    @BindView(R.id.reset_img)
    ImageView resetImg;
    @BindView(R.id.status_spin)
    MaterialSpinner statusSpin;
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

    private int current_year;
    private int current_month;
    private int current_day;
    private Calendar ctDate;

    private MonthAdapter.CalendarDay minDay;
    private static final String FROM_DATE="from_date";
    private static final String TO_DATE="to_date";

    public FilterDialogFragment() {
        // Required empty public constructor
    }

    private static List<Transporter> listTransporter;

    public static FilterDialogFragment newInstance(List<Transporter> list) {

        Bundle args = new Bundle();
        listTransporter = list;
        FilterDialogFragment fragment = new FilterDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        List<String> lstNames = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (listTransporter != null) {
            for (int i = 0; i < listTransporter.size(); i++) {
                lstNames.add(listTransporter.get(i).getName());
            }

            Collections.sort(lstNames);
            statusSpin.setItems(lstNames);

            statusSpin.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                    Log.e(TAG, "Selected Items==>>> "+item);
                }
            });
        }
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

    private void getCurrentDate(){
        ctDate = Calendar.getInstance();
        current_year = ctDate.get(Calendar.YEAR);
        current_month = ctDate.get(Calendar.MONTH) + 1; // Note: zero based!
        current_day = ctDate.get(Calendar.DAY_OF_MONTH);

        minDay = new MonthAdapter.CalendarDay();
        minDay.setDay(current_year, current_month,current_day);
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
                getCurrentDate();
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment();
                cdp.setOnDateSetListener(this);
                cdp.setFirstDayOfWeek(Calendar.SUNDAY);
                cdp.setPreselectedDate(current_year, current_month, current_day);
                cdp.setDateRange(minDay, null);
                cdp.setDoneText("Yes");
                cdp.setCancelText("No");
                cdp.show(getActivity().getSupportFragmentManager(), FROM_DATE);
                break;
            case R.id.to_date_lay:
                getCurrentDate();
                CalendarDatePickerDialogFragment cdp2 = new CalendarDatePickerDialogFragment();
                cdp2.setOnDateSetListener(this);
                cdp2.setFirstDayOfWeek(Calendar.SUNDAY);
                cdp2.setPreselectedDate(current_year, current_month, current_day);
                cdp2.setDateRange(minDay, null);
                cdp2.setDoneText("Yes");
                cdp2.setCancelText("No");
                cdp2.show(getActivity().getSupportFragmentManager(), TO_DATE);
                break;
            case R.id.filter_btn:
                break;
        }
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        String date =year+"-"+String.format("%02d", monthOfYear) +"-"+String.format("%02d", dayOfMonth);
        if(dialog.getTag().equals(FROM_DATE)){
            txtFromDate.setText(date);
        }else{
            toDateTxt.setText(date);
        }
    }
}
