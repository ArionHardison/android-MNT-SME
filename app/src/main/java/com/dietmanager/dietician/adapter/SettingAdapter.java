package com.dietmanager.dietician.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.dietmanager.dietician.activity.DietitianMainActivity;
import com.dietmanager.dietician.activity.SubscribePlansActivity;
import com.dietmanager.dietician.activity.SubscribedMembersActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.activity.BankDetailsActivity;
import com.dietmanager.dietician.activity.ChangePasswordActivity;
import com.dietmanager.dietician.activity.DeliveriesActivity;
import com.dietmanager.dietician.activity.EditRestaurantActivity;
import com.dietmanager.dietician.activity.FoodSafetyActivity;
import com.dietmanager.dietician.activity.HistoryActivity;
import com.dietmanager.dietician.activity.HomeActivity;
import com.dietmanager.dietician.activity.LoginActivity;
import com.dietmanager.dietician.activity.RestaurantTimingActivity;
import com.dietmanager.dietician.helper.ConnectionHelper;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.helper.GlobalData;
import com.dietmanager.dietician.helper.SharedHelper;
import com.dietmanager.dietician.model.ServerError;
import com.dietmanager.dietician.model.Setting;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.Constants;
import com.dietmanager.dietician.utils.LocaleUtils;
import com.dietmanager.dietician.utils.TextUtils;
import com.dietmanager.dietician.utils.Utils;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.MyViewHolder> {
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private CustomDialog customDialog;
    private ConnectionHelper connectionHelper;
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
        holder.llMain.setOnClickListener(view -> {
            int pos = (int) view.getTag();
            Setting data = list.get(pos);
            String title = data.getTitle();
            redirectPage(title);
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
        } else if (title.equalsIgnoreCase(context.getString(R.string.bank_account_details))) {
            Intent intent = new Intent(context, BankDetailsActivity.class);
            intent.putExtra("from", "Settings");
            context.startActivity(intent);
        } else if (title.equalsIgnoreCase(context.getString(R.string.deliveries))) {
            context.startActivity(new Intent(context, DeliveriesActivity.class));
        } else if (title.equalsIgnoreCase(context.getString(R.string.food_safety))) {
            context.startActivity(new Intent(context, FoodSafetyActivity.class));
        } else if (title.equalsIgnoreCase(context.getString(R.string.change_language))) {
            changeLanguage();
        } else if (title.equalsIgnoreCase(context.getString(R.string.change_password))) {
            context.startActivity(new Intent(context, ChangePasswordActivity.class));
        } else if (title.equalsIgnoreCase(context.getString(R.string.logout))) {
            showLogoutAlertDialog();
        } else if (title.equalsIgnoreCase(context.getString(R.string.delete_account))) {
            showDeleteAccountAlertDialog();
        } else if (title.equalsIgnoreCase(context.getString(R.string.subscribed_members))) {
            context.startActivity(new Intent(context, SubscribedMembersActivity.class));
        } else if (title.equalsIgnoreCase(context.getString(R.string.subscription_plans))) {
            context.startActivity(new Intent(context, SubscribePlansActivity.class));
        } else if (title.equalsIgnoreCase(context.getString(R.string.portfolio))) {
            showDeleteAccountAlertDialog();
        }
    }

    private void changeLanguage() {
        final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.language_dialog, null);
        alertDialog.setView(convertView);
        alertDialog.setCancelable(true);
        alertDialog.setIcon(R.mipmap.ic_launcher_round);
        alertDialog.setTitle(context.getString(R.string.change_language));
        final android.app.AlertDialog alert = alertDialog.create();
        final RadioGroup chooseLanguage = convertView.findViewById(R.id.choose_language);
        final RadioButton english = convertView.findViewById(R.id.rbEnglish);
        final RadioButton rbArabic = convertView.findViewById(R.id.rbArabic);

        String dd = LocaleUtils.getLanguage(context);
        switch (dd) {
            case "en":
                english.setChecked(true);
                break;
            case "ar":
                rbArabic.setChecked(true);
                break;
            default:
                english.setChecked(true);
                break;
        }
        chooseLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbEnglish:
                    setLanguage("English");
                    alert.dismiss();
                    break;
                case R.id.rbArabic:
                    setLanguage("Arabic");
                    alert.dismiss();
                    break;

            }
        });
        alert.show();

    }

    private void setLanguage(String value) {
        SharedHelper.putKey(context, "language", value);
        switch (value) {
            case "English":
                LocaleUtils.setLocale(context, "en");
                break;
            case "Arabic":
                LocaleUtils.setLocale(context, "ar");
                break;
            default:
                LocaleUtils.setLocale(context, "en");
                break;
        }
        context.startActivity(new Intent(context, DietitianMainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra("change_language", true));
    }

    private void showLogoutAlertDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(context.getString(R.string.app_name));
        builder.setMessage(context.getResources().getString(R.string.alert_log_out));
        builder.setPositiveButton(context.getResources().getString(R.string.okay), (dialog, which) -> {
            dialog.dismiss();
            logOut();
        });
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
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
                    ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                    String message = serverError != null ? serverError.getError() : null;
                    clearAndExit();
                    Log.e("Error", !TextUtils.isEmpty(message) ? message : "");
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
        builder.setMessage(context.getResources().getString(R.string.delete_restaurents));
        builder.setPositiveButton(context.getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
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
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
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
        context.startActivity(new Intent(context, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        activity.finishAffinity();
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
