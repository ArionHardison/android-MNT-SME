package com.dietmanager.dietician.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dietmanager.dietician.R;
import com.dietmanager.dietician.model.MessageResponse;
import com.dietmanager.dietician.network.ApiClient;
import com.dietmanager.dietician.network.ApiInterface;
import com.dietmanager.dietician.utils.TextUtils;
import com.dietmanager.dietician.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteLinkActivity extends AppCompatActivity {
    @BindView(R.id.et_username)
    EditText name;
    @BindView(R.id.et_mail)
    EditText email;
    @BindView(R.id.submit_btn)
    Button submit_btn;
    private ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_link);

        ButterKnife.bind(this);
        ((TextView) findViewById(R.id.toolbar).findViewById(R.id.title)).setText(R.string.invite_link);
        findViewById(R.id.toolbar).findViewById(R.id.back_img).setVisibility(View.VISIBLE);
        findViewById(R.id.toolbar).findViewById(R.id.back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInviteLink();
            }
        });
    }

    private void addInviteLink(){
        if (email.getText().toString().isEmpty()) {
            Toast.makeText(InviteLinkActivity.this, getString(R.string.please_enter_user_email), Toast.LENGTH_LONG).show();
        } else if (name.getText().toString().isEmpty()) {
            Toast.makeText(InviteLinkActivity.this, getString(R.string.please_enter_user_name), Toast.LENGTH_LONG).show();
        } else if (!TextUtils.isValidEmail(email.getText().toString())) {
            Toast.makeText(InviteLinkActivity.this, getString(R.string.please_enter_valid_email), Toast.LENGTH_LONG).show();
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("email", email.getText().toString());
            map.put("name", name.getText().toString());
            inviteUser(map);
        }
    }

    private void inviteUser(HashMap<String, String> map) {
        Call<MessageResponse> call = apiInterface.inviteUser(map);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(InviteLinkActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if (jObjError.has("email"))
                            Toast.makeText(InviteLinkActivity.this, jObjError.optString("email"), Toast.LENGTH_LONG).show();
                        else if (jObjError.has("error"))
                            Toast.makeText(InviteLinkActivity.this, jObjError.optString("error"), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(InviteLinkActivity.this, "Invalid", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(InviteLinkActivity.this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Utils.displayMessage(InviteLinkActivity.this, getString(R.string.something_went_wrong));
            }
        });
    }
}
