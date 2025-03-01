package com.example.tnychineseactivity;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;

import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    WebView webView;
    RelativeLayout no_internet_layout;
    boolean hasConnect;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView=findViewById(R.id.web_view);
        no_internet_layout=findViewById(R.id.no_internet_layout);

        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new WebChromeClient());

        ConnectivityManager manager = (ConnectivityManager)MainActivity.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = manager.getActiveNetworkInfo();
        hasConnect = (i!= null && i.isConnected() && i.isAvailable());

        if(hasConnect)
        {
            webView.loadUrl("http://tnychineseacademy.infinityfreeapp.com");
            no_internet_layout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }
        else
        {
            no_internet_layout.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"NO INTERNET CONNECTION! TRY AGAIN",
                    Toast.LENGTH_SHORT).show();
        }



        WebSettings webSettings=webView.getSettings();

        webSettings.setJavaScriptEnabled(true);

        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
    }

    public void ReconnectWebsite(View view) {
        finish();
        startActivity(getIntent());



    }




    private class MyWebClient extends WebViewClient{

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }



        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            return super.shouldOverrideUrlLoading(view, url);
            if(url.startsWith("tel:")){
                Intent i=new Intent(Intent.ACTION_DIAL,Uri.parse(url));
                startActivity(i);
                return  true;
            }
            return  false;
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