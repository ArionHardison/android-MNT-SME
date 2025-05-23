package com.dietmanager.dietician.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.activity.AddProductActivity;
import com.dietmanager.dietician.activity.EditRestaurantActivity;
import com.dietmanager.dietician.activity.LoginActivity;
import com.dietmanager.dietician.activity.RegisterActivity;
import com.dietmanager.dietician.helper.ConnectionHelper;
import com.dietmanager.dietician.model.Cuisine;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CuisineSelectFragment extends DialogFragment {

    public static List<Cuisine> CUISINES = new ArrayList<>();
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    @BindView(R.id.cuisine_rv)
    RecyclerView cuisineRv;
    @BindView(R.id.done)
    Button done;
    Unbinder unbinder;
    RecyclerViewAdapter mAdapter;
    List<Cuisine> list = new ArrayList<>();
    ConnectionHelper connectionHelper;
    boolean singleSelection;
    int selected_pos = -1;

    public CuisineSelectFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public CuisineSelectFragment(boolean singleSelection) {
        // Required empty public constructor
        this.singleSelection = singleSelection;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuisine_select, container, false);

        unbinder = ButterKnife.bind(this, view);

        mAdapter = new RecyclerViewAdapter(list, singleSelection);
        cuisineRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        cuisineRv.setAdapter(mAdapter);

        connectionHelper = new ConnectionHelper(getActivity());


        if (connectionHelper.isConnectingToInternet())
            getCuisines();
        else
            Utils.displayMessage(getActivity(), getString(R.string.oops_no_internet));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.done)
    public void onViewClicked() {
        CUISINES = mAdapter.getSelectedValues();
        if (getActivity() instanceof RegisterActivity)
            ((RegisterActivity) getActivity()).bindCuisine();
        else if (getActivity() instanceof EditRestaurantActivity)
            ((EditRestaurantActivity) getActivity()).bindCuisine();
        else if (getActivity() instanceof AddProductActivity)
            ((AddProductActivity) getActivity()).bindCuisine();
        dismiss();
    }

    private void getCuisines() {
        Call<List<Cuisine>> call = apiInterface.getCuisines();
        call.enqueue(new Callback<List<Cuisine>>() {
            @Override
            public void onResponse(@NonNull Call<List<Cuisine>> call, @NonNull Response<List<Cuisine>> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    list.addAll(response.body());
                    mAdapter.notifyDataSetChanged();
                    if (response.code() == 401) {
                        startActivity(new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        getActivity().finish();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Cuisine>> call, @NonNull Throwable t) {
                if (isAdded()) {
                    Utils.displayMessage(getActivity(), getString(R.string.something_went_wrong));
                }

            }
        });

    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

        boolean isSingleSelection;
        private List<Cuisine> mModelList;

        RecyclerViewAdapter(List<Cuisine> modelList, boolean isSingleSelection) {
            mModelList = modelList;
            this.isSingleSelection = isSingleSelection;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_cuisine, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
            final Cuisine model = mModelList.get(position);
            holder.textView.setText(model.getName());
            if (singleSelection) {
                if (selected_pos == position) {
                    model.setSelected(true);
                } else {
                    model.setSelected(false);
                }
            }

            holder.view.setBackgroundColor(model.isSelected() ? getResources().getColor(R.color.medium_grey) : Color.WHITE);
            holder.textView.setTag(position);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected_pos = (int) view.getTag();
                    model.setSelected(!model.isSelected());
                    holder.view.setBackgroundColor(model.isSelected() ? getResources().getColor(R.color.medium_grey) : Color.WHITE);
                    if (singleSelection) {
                        notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mModelList == null ? 0 : mModelList.size();
        }

        List<Cuisine> getSelectedValues() {
            List<Cuisine> mm = new ArrayList<>();
            for (Cuisine obj : mModelList) if (obj.isSelected()) mm.add(obj);
            return mm;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private View view;
            private TextView textView;

            private MyViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                textView = itemView.findViewById(R.id.name);
            }
        }
    }
}
