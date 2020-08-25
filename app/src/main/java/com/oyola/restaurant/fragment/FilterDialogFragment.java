package com.oyola.restaurant.fragment;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.oyola.restaurant.R;
import com.oyola.restaurant.messages.FilterDialogFragmentMessage;
import com.oyola.restaurant.messages.communicator.DataMessage;
import com.oyola.restaurant.model.Transporter;
import com.oyola.restaurant.utils.TextUtils;
import com.oyola.restaurant.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterDialogFragment extends DialogFragment implements CalendarDatePickerDialogFragment.OnDateSetListener, DataMessage<FilterDialogFragmentMessage>, CompoundButton.OnCheckedChangeListener {

    private static final String FROM_DATE = "from_date";
    private static final String TO_DATE = "to_date";
    private static List<Transporter> listTransporter;
    String TAG = FilterDialogFragment.this.getClass().getName();
    @BindView(R.id.reset_img)
    ImageView resetImg;
    @BindView(R.id.status_spin)
    MaterialSpinner statusSpin;
    @BindView(R.id.order_type_spinner)
    MaterialSpinner orderTypeSpinner;
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
    @BindView(R.id.rb_all)
    RadioButton rbAll;
    @BindView(R.id.rb_completed)
    RadioButton rbCompleted;
    @BindView(R.id.rb_cancelled)
    RadioButton rbCancelled;
    @BindView(R.id.root_layout)
    LinearLayout rootLayout;
    @BindView(R.id.main_content)
    CoordinatorLayout main_content;
    int selected_id = 0;
    int selected_pos = 0;
    int selectedOrderPosition = 0;
    String strFromDate = "";
    String strToDate = "";
    DataMessage<FilterDialogFragmentMessage> dataMessage;
    private FilterDialogFragmentMessage message;
    private int current_year;
    private int current_month;
    private int current_day;
    private Calendar ctDate;
    private MonthAdapter.CalendarDay minDay;

    private String orderType = "";
    private String orderStatus;
    private String[] orderTypeArray = new String[]{"Select Order Type", "Delivery", "Takeaway"};

    public FilterDialogFragment() {
        // Required empty public constructor
    }

    public static FilterDialogFragment newInstance(List<Transporter> list) {

        Bundle args = new Bundle();
        listTransporter = list;
        FilterDialogFragment fragment = new FilterDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataMessage = (DataMessage) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initData();
    }

    private void initData() {

        final HashMap<String, Integer> orderMap = new HashMap<>();
        orderMap.put(getString(R.string.select_order_type), 0);
        orderTypeSpinner.setItems(orderTypeArray);

        rbAll.setOnCheckedChangeListener(this);
        rbCancelled.setOnCheckedChangeListener(this);
        rbCompleted.setOnCheckedChangeListener(this);

        orderTypeSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
            if (position > 0)
                orderType = orderTypeArray[position];
            selectedOrderPosition = position;
        });

        if (listTransporter != null) {
            List<String> lstNames = new ArrayList<>();
            final HashMap<String, Integer> hshDPIds = new HashMap<>();
            hshDPIds.put(getString(R.string.select_delievery_person), 0);

            for (int i = 0; i < listTransporter.size(); i++) {
                String name = listTransporter.get(i).getName().trim();
                name = Utils.toFirstCharUpperAll(name);
                lstNames.add(name);
                hshDPIds.put(name, listTransporter.get(i).getId());
            }

            Collections.sort(lstNames);
            lstNames.add(0, getString(R.string.select_delievery_person));
            statusSpin.setItems(lstNames);

            statusSpin.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                    selected_id = hshDPIds.get(item);
                    selected_pos = position;
                }
            });
        }

        prepareData();
    }

    private void prepareData() {
        if (message != null) {
            statusSpin.setSelectedIndex(message.getSelectePos());
            txtFromDate.setText(message.getFormattedFromDate());
            toDateTxt.setText(message.getFormattedToDate());

            strFromDate = message.getFromDate();
            strToDate = message.getToDate();
            selected_id = message.getDelieveryPersonId();
            selected_pos = message.getSelectePos();

            if (!TextUtils.isEmpty(message.getOrderType())) {
                int selectedOrderType = 0;
                for (int i = 0, size = orderTypeArray.length; i < size; i++) {
                    String item = orderTypeArray[i];
                    if (message.getOrderType().equalsIgnoreCase(item)) {
                        selectedOrderType = i;
                        orderType = item;
                        break;
                    }
                }
                orderTypeSpinner.setSelectedIndex(selectedOrderType);
            }

            if (!TextUtils.isEmpty(message.getOrderStatus())) {
                if (message.getOrderStatus().equalsIgnoreCase("ALL")) {
                    rbAll.setChecked(true);
                    orderStatus = rbAll.getText().toString();
                } else if (message.getOrderStatus().equalsIgnoreCase("COMPLETED")) {
                    rbCompleted.setChecked(true);
                    orderStatus = rbCompleted.getText().toString();
                } else if (message.getOrderStatus().equalsIgnoreCase("CANCELLED")) {
                    rbCancelled.setChecked(true);
                    orderStatus = rbCancelled.getText().toString();
                }
            }
        }
    }


    private void resetData() {
        txtFromDate.setText("");
        toDateTxt.setText("");
        statusSpin.setSelectedIndex(0);
        orderTypeSpinner.setSelectedIndex(0);
        selected_pos = 0;
        selected_id = 0;
        strFromDate = "";
        strToDate = "";
        orderStatus = "";
        orderType = "";

        rbAll.setChecked(false);
        rbCancelled.setChecked(false);
        rbCompleted.setChecked(false);
        message.clear();
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

    private void getCurrentDate() {
        ctDate = Calendar.getInstance();
        current_year = ctDate.get(Calendar.YEAR);
        current_month = ctDate.get(Calendar.MONTH)/* + 1*/; // Note: zero based!
        current_day = ctDate.get(Calendar.DAY_OF_MONTH);

        minDay = new MonthAdapter.CalendarDay();
        minDay.setDay(current_year, current_month, current_day);


    }

    @OnClick({R.id.reset_img, R.id.from_date_lay, R.id.to_date_lay, R.id.filter_btn, R.id.main_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reset_img:
                resetData();
                break;
            case R.id.from_date_lay:
                getCurrentDate();
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment();
                cdp.setOnDateSetListener(this);
                cdp.setFirstDayOfWeek(Calendar.SUNDAY);
                cdp.setPreselectedDate(current_year, current_month, current_day);
                //cdp.setDateRange(minDay, null);
                cdp.setDoneText("Yes");
                cdp.setCancelText("No");
                cdp.show(getActivity().getSupportFragmentManager(), FROM_DATE);
                break;
            case R.id.to_date_lay:
                if (!validateDate()) {
                    Utils.showAlertDialog(getActivity(), "Please select from date");
                    return;
                }
                getCurrentDate();
                CalendarDatePickerDialogFragment cdp2 = new CalendarDatePickerDialogFragment();
                cdp2.setOnDateSetListener(this);
                cdp2.setFirstDayOfWeek(Calendar.SUNDAY);
                cdp2.setPreselectedDate(current_year, current_month, current_day);
                //cdp2.setDateRange(minDay, null);
                cdp2.setDoneText("Yes");
                cdp2.setCancelText("No");
                cdp2.show(getActivity().getSupportFragmentManager(), TO_DATE);
                break;
            case R.id.filter_btn:
                //validateFilter();
                sendMessageToScreen();
                break;

            case R.id.main_content:
                dismiss();
                break;
        }
    }

    public boolean validateDate() {
        String strFromDate = txtFromDate.getText().toString();
        String strToDate = toDateTxt.getText().toString();
        return !strFromDate.isEmpty() || strToDate.isEmpty();
    }

    private void validateFilter() {
        String strFromDate = txtFromDate.getText().toString();
        String strToDate = toDateTxt.getText().toString();
        if (selected_id == 0) {
            Utils.showAlertDialog(getContext(), "Please select a delivery person.");
        } else if (strFromDate.isEmpty()) {
            Utils.showAlertDialog(getContext(), "Please select a start date");
        } else if (strToDate.isEmpty()) {
            Utils.showAlertDialog(getContext(), "Please select a end date");
        } else {
            sendMessageToScreen();
        }
    }

    private void sendMessageToScreen() {
        if (message == null)
            message = new FilterDialogFragmentMessage();
        message.setDelieveryPersonId(selected_id);
        message.setFromDate(strFromDate);
        message.setToDate(strToDate);
        message.setSelectePos(selected_pos);
        message.setFormattedFromDate(txtFromDate.getText().toString());
        message.setFormattedToDate(toDateTxt.getText().toString());
        message.setOrderStatus(orderStatus);
        message.setOrderType(orderType);
        dataMessage.onReceiveData(message);
        dismiss();
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "-" + String.format("%02d", monthOfYear) + "-" + String.format("%02d", dayOfMonth);
        String formattedDate = String.format("%02d", dayOfMonth) + ":" + String.format("%02d", monthOfYear + 1) + ":" + year;
        if (dialog.getTag().equals(FROM_DATE)) {
            strFromDate = date;
            txtFromDate.setText(formattedDate);
        } else {
            strToDate = date;
            toDateTxt.setText(formattedDate);
        }
    }

    @Override
    public void onReceiveData(FilterDialogFragmentMessage message) {
        this.message = message;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked)
            orderStatus = buttonView.getText().toString();
    }
}
