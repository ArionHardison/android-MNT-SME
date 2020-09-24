package com.oyola.restaurant.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.oyola.restaurant.R;
import com.oyola.restaurant.helper.CustomDialog;
import com.oyola.restaurant.model.StripeResponse;
import com.oyola.restaurant.network.ApiClient;
import com.oyola.restaurant.network.ApiInterface;
import com.oyola.restaurant.utils.TextUtils;
import com.oyola.restaurant.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankDetailsActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.webview)
    WebView webView;
    private CustomDialog customDialog;
    private boolean isUpdating = false;

    private String tokenUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);
        ButterKnife.bind(this);
        title.setText(getResources().getString(R.string.bank_details));
        String url = "https://dashboard.stripe.com/express/oauth/authorize?response_type=code&client_id=ca_GCqVbp3vX9JZsAk0XeOs8aWraIUCgFOA&scope=read_write";
        customDialog = new CustomDialog(this);
        loadWebView(url);
    }

    private void loadWebView(String url) {
        if (customDialog != null && !customDialog.isShowing()) {
            customDialog.show();
        }
        webView.clearCache(true);
        webView.clearFormData();
        webView.clearHistory();
        webView.clearSslPreferences();

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (customDialog != null && customDialog.isShowing()) {
                    customDialog.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e("Params Url:", "" + request.getUrl());
                if (request.getUrl().toString().contains("https://oyola.co/shop/stripe")) {
                    tokenUrl = request.getUrl().toString();
                }
                if (!TextUtils.isEmpty(tokenUrl) & !isUpdating) {
                    isUpdating = true;
                    String tempUrl = tokenUrl;
                    String code = tempUrl.replace("https://oyola.co/shop/stripe/connect?code=", "");
                    Log.e("Final Url", code);
                    updateBankDetails(code);
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Utils.showToast(BankDetailsActivity.this, "Something wrong with the document url...");
            }
        });
        webView.loadUrl(url);
    }

    private void updateBankDetails(String token) {
        if (customDialog != null && !customDialog.isShowing()) {
            customDialog.show();
        }
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<StripeResponse> call = apiInterface.updateBankDetails(token);
        call.enqueue(new Callback<StripeResponse>() {
            @Override
            public void onResponse(Call<StripeResponse> call, Response<StripeResponse> response) {
                if (customDialog != null && customDialog.isShowing()) {
                    customDialog.dismiss();
                }
                if (response.isSuccessful()) {
                    String message = response.body() != null && !TextUtils.isEmpty(response.body().getMessage()) ?
                            response.body().getMessage() : "Bank Details updated successfully...";
                    Utils.showToast(BankDetailsActivity.this, message);
                } else {
                    Utils.showToast(BankDetailsActivity.this, "Something went wrong...");
                }
            }

            @Override
            public void onFailure(Call<StripeResponse> call, Throwable t) {
                Utils.showToast(BankDetailsActivity.this, "Something went wrong...");
            }
        });
    }

}
