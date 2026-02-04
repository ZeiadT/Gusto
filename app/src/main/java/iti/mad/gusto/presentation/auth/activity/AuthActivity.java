package iti.mad.gusto.presentation.auth.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import iti.mad.gusto.presentation.auth.splash.SplashFragment;
import iti.mad.gusto.presentation.common.component.PolygonView;

import iti.mad.gusto.R;

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

        PolygonView overlayView = findViewById(R.id.overlayView);

        if ((currentFragment instanceof SplashFragment) || overlayView == null) return;

        if (overlayView.getParent() instanceof android.view.ViewGroup) {
            ((android.view.ViewGroup) overlayView.getParent()).removeView(overlayView);
        }


    }
}