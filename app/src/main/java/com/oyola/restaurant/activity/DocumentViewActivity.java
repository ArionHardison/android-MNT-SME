package com.oyola.restaurant.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.oyola.restaurant.R;
import com.oyola.restaurant.helper.CustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Prasanth on 21-01-2020
 */
public class DocumentViewActivity extends AppCompatActivity {


    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.webview_document)
    WebView mWebViewDocument;

    private String mPdfUrl = "";
    private String mDocName = "";
    CustomDialog customDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentview);
        ButterKnife.bind(this);
        customDialog = new CustomDialog(this);
        if (getIntent() != null) {
            mPdfUrl = getIntent().getStringExtra("pdf_url");
            mDocName = getIntent().getStringExtra("document_name");
        }
        backImg.setVisibility(View.VISIBLE);
        if (mDocName != null && mDocName.length() > 2) {
            mDocName = mDocName.substring(0, 1).toUpperCase() + mDocName.substring(1);
        }
        title.setText(mDocName);
        mPdfUrl = "http://docs.google.com/gview?embedded=true&url=" + mPdfUrl;
        mWebViewDocument.getSettings().setJavaScriptEnabled(true);
        customDialog.show();
        mWebViewDocument.loadUrl(mPdfUrl);
        mWebViewDocument.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                customDialog.dismiss();
            }
        });
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
