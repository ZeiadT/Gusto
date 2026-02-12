package iti.mad.gusto.presentation.auth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import iti.mad.gusto.presentation.auth.splash.SplashFragment;
import iti.mad.gusto.presentation.common.component.PolygonView;

import iti.mad.gusto.R;

public class AuthActivity extends AppCompatActivity implements AuthActivityCommunicator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Fragment currentFragment = getCurrentFragment();

        Log.d("TAG", "onStart currentFragment: " + currentFragment);

        PolygonView overlayView = findViewById(R.id.overlayView);

        if ((currentFragment instanceof SplashFragment) || overlayView == null) return;

        if (overlayView.getParent() instanceof ViewGroup) {
            ((ViewGroup) overlayView.getParent()).removeView(overlayView);
        }



    }

    @Nullable
    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment navHostFragment =
                fragmentManager.findFragmentById(R.id.frag_container_auth);

        Fragment currentFragment = null;

        if (navHostFragment instanceof NavHostFragment) {
            FragmentManager childFragmentManager =
                    navHostFragment.getChildFragmentManager();

            currentFragment = childFragmentManager
                    .getPrimaryNavigationFragment();
        }
        return currentFragment;
    }

    @Override
    public void navigateReplacementToAnotherActivity(Class<?> clazz){
        Intent intent = new Intent(this, clazz);

        startActivity(intent);
        overridePendingTransition(
                0,
                0
        );

        finish();
        overridePendingTransition(
                0,
                0
        );
        Log.d("Main", "navigateReplacementToAnotherActivity: ");
    }

    @Override
    public void navigateReplacementToAnotherActivityWithAnimation(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }
}