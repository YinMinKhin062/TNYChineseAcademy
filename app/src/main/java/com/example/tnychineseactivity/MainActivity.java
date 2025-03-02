package com.example.tnychineseactivity;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.os.Bundle;

import android.view.View;


import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    WebView webView;
    private ProgressBar progressBar;
    private FrameLayout fullscreenContainer;
    private View customView;
    RelativeLayout no_internet_layout;
    boolean hasConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.progressBar);
        no_internet_layout = findViewById(R.id.no_internet_layout);

        fullscreenContainer = new FrameLayout(this);

        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new MyWebChromeClient());

        ConnectivityManager manager = (ConnectivityManager) MainActivity.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = manager.getActiveNetworkInfo();
        hasConnect = (i != null && i.isConnected() && i.isAvailable());

        if (hasConnect) {
            webView.loadUrl("http://tnychineseacademy.infinityfreeapp.com");
            no_internet_layout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        } else {
            no_internet_layout.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION! TRY AGAIN",
                    Toast.LENGTH_SHORT).show();
        }


        WebSettings webSettings = webView.getSettings();
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);


        webSettings.setJavaScriptEnabled(true);

        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
    }

    public void ReconnectWebsite(View view) {
        finish();
        startActivity(getIntent());


    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(newProgress); // Update progress as the page loads
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            // When fullscreen mode is triggered, show the custom view in fullscreenContainer
            if (fullscreenContainer != null) {
                fullscreenContainer.addView(view);
                customView = view;  // Store the fullscreen view

                // Get the root layout to add the fullscreen container as an overlay
                FrameLayout rootLayout = findViewById(android.R.id.content);  // Access the root layout of the activity
                rootLayout.addView(fullscreenContainer);  // Add fullscreen container on top of the activity

                webView.setVisibility(View.INVISIBLE);  // Hide the WebView while in fullscreen mode

                // Allow the container to resize and rotate as needed
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }

        @Override
        public void onHideCustomView() {
            // When exiting fullscreen, remove the custom view
            if (customView != null) {
                fullscreenContainer.removeView(customView);
                customView = null;

                // Restore WebView and remove the fullscreen container
                webView.setVisibility(View.VISIBLE);
                FrameLayout rootLayout = findViewById(android.R.id.content);
                rootLayout.removeView(fullscreenContainer);  // Remove fullscreen container

                // Restore the original orientation (portrait) after exiting fullscreen
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }


    private class MyWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(ProgressBar.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }


        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);
            progressBar.setVisibility(ProgressBar.GONE);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            return super.shouldOverrideUrlLoading(view, url);
            if (url.startsWith("tel:")) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(i);
                return true;
            }
            return false;
        }


    }


    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();  // Navigate back in WebView history
        } else {
            super.onBackPressed();  // Call the default back press behavior (close app or activity)
        }
    }


}