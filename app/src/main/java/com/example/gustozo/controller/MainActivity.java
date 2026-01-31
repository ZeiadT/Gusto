package com.example.gustozo.controller;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gustozo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}

/*
* Required Dependencies:
*
* AppStorage (sharedPrefs variant)
* Room
* Retrofit
* Glide
* Gson
* GsonConverter
*
* */