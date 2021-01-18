package com.xandi.seafish.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.xandi.seafish.R;
import com.xandi.seafish.util.Constants;


public class TermsActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        setLayoutAttributes();

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl(Constants.TERMS_AND_CONDITIONS);
    }

    private void setLayoutAttributes() {
        webView = findViewById(R.id.webView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}