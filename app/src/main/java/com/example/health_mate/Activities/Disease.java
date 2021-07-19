package com.example.health_mate.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.health_mate.R;

public class Disease extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        webView = (WebView)findViewById(R.id.webView);

        //load site in our app
       // webView.setWebViewClient(new WebViewClient());
        WebViewClient webViewClient=new WebViewClient();
        webView.setWebViewClient(webViewClient);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://www.amazon.com");
    }

    //this method requires to use back button if want to go to prev page
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }
        else{
            super.onBackPressed();
        }
    }
}