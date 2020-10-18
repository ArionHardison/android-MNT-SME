package com.dietmanager.dietician.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.dietmanager.dietician.R;
import com.dietmanager.dietician.helper.CustomDialog;
import com.dietmanager.dietician.utils.TextUtils;
import com.dietmanager.dietician.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Prasanth on 21-01-2020
 */
public class DocumentViewActivity extends AppCompatActivity {

    private static final String JPG = ".JPG";
    private static final String JPEG = ".JPEG";
    private static final String PNG = ".PNG";
    private static final String GIF = ".GIF";
    private static final String PDF = ".PDF";

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.img_document)
    ImageView imgDoc;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.webview_document)
    WebView webView;

    private String docUrl = "";
    private String mDocName = "";
    CustomDialog customDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentview);
        ButterKnife.bind(this);
        customDialog = new CustomDialog(this);
        if (getIntent() != null) {
            docUrl = getIntent().getStringExtra("pdf_url");
            mDocName = getIntent().getStringExtra("document_name");
        }
        backImg.setVisibility(View.VISIBLE);
        if (mDocName != null && mDocName.length() > 2) {
            mDocName = mDocName.substring(0, 1).toUpperCase() + mDocName.substring(1);
        }
        title.setText(mDocName);
        String mimeType = Utils.getExtensionFromUrl(docUrl).toUpperCase();

        if (!TextUtils.isEmpty(mimeType)) {
            switch (mimeType) {
                case JPG:
                case JPEG:
                case PNG:
                case GIF:
                    imgDoc.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);
                    loadImage(docUrl);
                    break;
                case PDF:
                    imgDoc.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    loadWebView(docUrl, true);
                    break;
                default:
                    imgDoc.setVisibility(View.GONE);
                    webView.setVisibility(View.GONE);
                    tvError.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            webView.setVisibility(View.VISIBLE);
            imgDoc.setVisibility(View.GONE);
            loadWebView(docUrl, false);
        }
    }

    private void loadImage(String url) {
        Glide.with(this).load(url).placeholder(R.drawable.ic_place_holder_image).into(imgDoc);
    }

    private void loadWebView(String url, boolean isPdf) {
        String finalUrl = isPdf ? "http://docs.google.com/viewer?url=" + url + "&embedded=true" : url;
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
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setUserAgentString("Notice Board");
        webSettings.setLoadWithOverviewMode(true);
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
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Utils.showToast(DocumentViewActivity.this, "Something wrong with the document url...");
            }
        });
        webView.loadUrl(finalUrl);
    }

    @OnClick({R.id.back_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                onBackPressed();
            default:
                break;
        }
    }
}
