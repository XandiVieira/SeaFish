package com.xandi.seafish.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.xandi.seafish.R;
import com.xandi.seafish.util.Constants;

public class PrivacyPolicyActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        setLayoutAttributes();

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl(Constants.PRIVACY_POLICY);
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