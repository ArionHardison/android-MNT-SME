package com.pakupaku.outlet.adapter;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pakupaku.outlet.R;
import com.pakupaku.outlet.activity.ChangePasswordActivity;
import com.pakupaku.outlet.activity.DeliveriesActivity;
import com.pakupaku.outlet.activity.EditRestaurantActivity;
import com.pakupaku.outlet.activity.HistoryActivity;
import com.pakupaku.outlet.activity.HomeActivity;
import com.pakupaku.outlet.activity.LoginActivity;
import com.pakupaku.outlet.activity.RestaurantTimingActivity;
import com.pakupaku.outlet.helper.ConnectionHelper;
import com.pakupaku.outlet.helper.CustomDialog;
import com.pakupaku.outlet.helper.GlobalData;
import com.pakupaku.outlet.helper.SharedHelper;
import com.pakupaku.outlet.model.ServerError;
import com.pakupaku.outlet.model.Setting;
import com.pakupaku.outlet.network.ApiClient;
import com.pakupaku.outlet.network.ApiInterface;
import com.pakupaku.outlet.utils.Constants;
import com.pakupaku.outlet.utils.LocaleUtils;
import com.pakupaku.outlet.utils.Utils;

import java.util.Arrays;
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
        } else if (title.equalsIgnoreCase(context.getString(R.string.change_language))) {
            changeLanguage();
        } else if (title.equalsIgnoreCase(context.getString(R.string.change_password))) {
            context.startActivity(new Intent(context, ChangePasswordActivity.class));
        } else if (title.equalsIgnoreCase(context.getString(R.string.logout))) {
            showLogoutAlertDialog();
        } else if (title.equalsIgnoreCase(context.getString(R.string.delete_account))) {
            showDeleteAccountAlertDialog();
        }
    }

    private void changeLanguage() {
        List<String> languages = Arrays.asList(context.getResources().getStringArray(R.array.languages));
        final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.language_dialog, null);
        alertDialog.setView(convertView);
        alertDialog.setCancelable(true);
        alertDialog.setTitle(context.getString(R.string.change_language));
        final android.app.AlertDialog alert = alertDialog.create();
        final RadioGroup chooseLanguage = convertView.findViewById(R.id.choose_language);
        final RadioButton english = convertView.findViewById(R.id.english);
        final RadioButton japnese = convertView.findViewById(R.id.japnese);

        String dd = LocaleUtils.getLanguage(context);
        switch (dd) {
            case "en":
                english.setChecked(true);
                break;
            case "ja":
                japnese.setChecked(true);
                break;
            default:
                english.setChecked(true);
                break;
        }
        chooseLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.english:
                    setLanguage("English");
                    alert.dismiss();
                    break;
                case R.id.japnese:
                    setLanguage("Japanese");
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
            case "Japanese":
                LocaleUtils.setLocale(context, "ja");
                break;
            default:
                LocaleUtils.setLocale(context, "en");
                break;
        }
        context.startActivity(new Intent(context, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra("change_language", true));
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
