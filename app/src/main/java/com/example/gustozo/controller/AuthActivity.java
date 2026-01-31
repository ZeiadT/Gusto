package com.example.gustozo.controller;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.gustozo.R;
import com.example.gustozo.view.PolygonView;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
    }

    @Override
    protected void onStart() {
        super.onStart();


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frag_container_auth);

        if (!(currentFragment instanceof SplashFragment)) {
            PolygonView overlayView = findViewById(R.id.overlayView);

            if (overlayView.getParent() instanceof android.view.ViewGroup) {
                ((android.view.ViewGroup) overlayView.getParent()).removeView(overlayView);
            }

        }
    }
}