package com.example.twitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;
public class DisplayMessageActivity extends AppCompatActivity {
    private WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        Intent intent = getIntent();

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
        layout.addView(web);
    }
    public void setWeb(WebView inweb){
        web = inweb;
    }
}
