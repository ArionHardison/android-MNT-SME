package com.tomoeats.restaurant.adapter;

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

import com.tomoeats.restaurant.R;
import com.tomoeats.restaurant.activity.ChangePasswordActivity;
import com.tomoeats.restaurant.activity.DeliveriesActivity;
import com.tomoeats.restaurant.activity.EditRestaurantActivity;
import com.tomoeats.restaurant.activity.HistoryActivity;
import com.tomoeats.restaurant.activity.LoginActivity;
import com.tomoeats.restaurant.activity.RestaurantTimingActivity;
import com.tomoeats.restaurant.helper.GlobalData;
import com.tomoeats.restaurant.helper.SharedHelper;
import com.tomoeats.restaurant.model.Setting;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.MyViewHolder> {
    private List<Setting> list;
    private Context context;
    private Activity activity;

    public SettingAdapter(List<Setting> list, Context con, Activity activity) {
        this.list = list;
        this.context = con;
        this.activity = activity;
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
        }else if (title.equalsIgnoreCase(context.getString(R.string.delete_account))){
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
                SharedHelper.clearSharedPreferences(context);
                GlobalData.accessToken = "";
                context.startActivity(new Intent(context, LoginActivity.class));
                activity.finish();
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

    private void showDeleteAccountAlertDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(context.getString(R.string.app_name));
        builder.setMessage("Are you sure you want to delete this restaurant ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SharedHelper.clearSharedPreferences(context);
                GlobalData.accessToken = "";
                context.startActivity(new Intent(context, LoginActivity.class));
                activity.finish();
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
