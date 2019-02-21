package com.comida.outlet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.comida.outlet.R;
import com.comida.outlet.activity.ChangePasswordActivity;
import com.comida.outlet.activity.DeliveriesActivity;
import com.comida.outlet.activity.EditRestaurantActivity;
import com.comida.outlet.activity.HistoryActivity;
import com.comida.outlet.activity.LoginActivity;
import com.comida.outlet.activity.RestaurantTimingActivity;
import com.comida.outlet.helper.ConnectionHelper;
import com.comida.outlet.helper.CustomDialog;
import com.comida.outlet.helper.GlobalData;
import com.comida.outlet.helper.SharedHelper;
import com.comida.outlet.model.ServerError;
import com.comida.outlet.model.Setting;
import com.comida.outlet.network.ApiClient;
import com.comida.outlet.network.ApiInterface;
import com.comida.outlet.utils.Constants;
import com.comida.outlet.utils.Utils;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.MyViewHolder> {
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    CustomDialog customDialog;
    ConnectionHelper connectionHelper;
    private List<Setting> list;
    private Context context;
    private Activity activity;


    public SettingAdapter(List<Setting> list, Context con, Activity activity) {
        this.list = list;
        this.context = con;
        this.activity = activity;

        customDialog = new CustomDialog(context);
        connectionHelper = new ConnectionHelper(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Setting setting = list.get(position);
        holder.title.setText(setting.getTitle());
        holder.icon.setImageResource(setting.getIcon());
        holder.llMain.setTag(position);
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                Setting data = list.get(pos);
                String title = data.getTitle();
                redirectPage(title);
            }
        });
    }

    private void redirectPage(String title) {
        if (title.equalsIgnoreCase(context.getString(R.string.history))) {
            context.startActivity(new Intent(context, HistoryActivity.class));
        } else if (title.equalsIgnoreCase(context.getString(R.string.edit_restaurant))) {
            context.startActivity(new Intent(context, EditRestaurantActivity.class));
        } else if (title.equalsIgnoreCase(context.getString(R.string.edit_timing))) {
            Intent intent = new Intent(context, RestaurantTimingActivity.class);
            intent.putExtra("from", "Settings");
            context.startActivity(intent);
        } else if (title.equalsIgnoreCase(context.getString(R.string.deliveries))) {
            context.startActivity(new Intent(context, DeliveriesActivity.class));
        } else if (title.equalsIgnoreCase(context.getString(R.string.change_password))) {
            context.startActivity(new Intent(context, ChangePasswordActivity.class));
        } else if (title.equalsIgnoreCase(context.getString(R.string.logout))) {
            showLogoutAlertDialog();
        } else if (title.equalsIgnoreCase(context.getString(R.string.delete_account))) {
            showDeleteAccountAlertDialog();
        }
    }

    private void showLogoutAlertDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(context.getString(R.string.app_name));
        builder.setMessage("Would you like to logout ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                logOut();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void logOut() {
        customDialog.show();
        // String shop_id = SharedHelper.getKey(context, Constants.PREF.PROFILE_ID);
        Call<ResponseBody> call = apiInterface.logOut();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    clearAndExit();
                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(activity, serverError.getError());
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(activity, activity.getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(activity, activity.getString(R.string.something_went_wrong));
            }
        });
    }

    private void showDeleteAccountAlertDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(context.getString(R.string.app_name));
        builder.setMessage("Are you sure you want to delete this restaurant ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (connectionHelper.isConnectingToInternet()) {
                    deleteAccount();
                } else {
                    Utils.displayMessage(activity, context.getString(R.string.oops_no_internet));
                }


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void deleteAccount() {
        customDialog.show();
        String shop_id = SharedHelper.getKey(context, Constants.PREF.PROFILE_ID);
        Call<ResponseBody> call = apiInterface.deleteAccount(shop_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    clearAndExit();
                } else {
                    Gson gson = new Gson();
                    try {
                        ServerError serverError = gson.fromJson(response.errorBody().charStream(), ServerError.class);
                        Utils.displayMessage(activity, serverError.getError());
                    } catch (JsonSyntaxException e) {
                        Utils.displayMessage(activity, activity.getString(R.string.something_went_wrong));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                customDialog.dismiss();
                Utils.displayMessage(activity, activity.getString(R.string.something_went_wrong));
            }
        });
    }


    private void clearAndExit() {
        SharedHelper.clearSharedPreferences(context);
        GlobalData.accessToken = "";
        context.startActivity(new Intent(context, LoginActivity.class));
        activity.finish();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(Setting item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Setting item) {
        int position = list.indexOf(item);
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView icon;
        LinearLayout llMain;

        MyViewHolder(View view) {
            super(view);
            icon = view.findViewById(R.id.setting_icon);
            title = view.findViewById(R.id.setting_title);
            llMain = view.findViewById(R.id.llMain);
        }
    }
}
