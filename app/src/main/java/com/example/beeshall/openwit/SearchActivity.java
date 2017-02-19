package com.example.beeshall.openwit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        final String sid = (String) intent.getExtras().get("sid");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
}
