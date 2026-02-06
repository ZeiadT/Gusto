package iti.mad.gusto.presentation.main.activity;

import static com.google.android.material.navigation.NavigationBarView.LABEL_VISIBILITY_UNLABELED;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import iti.mad.gusto.R;
import iti.mad.gusto.presentation.common.component.PolygonView;
import iti.mad.gusto.presentation.common.util.AnimationUtil;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.Presenter presenter;
    private PolygonView overlayView;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Main", "onCreate Main: " + savedInstanceState);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        overlayView = findViewById(R.id.main_overlay_view);

        Log.d("Main", "onCreate: " + overlayView);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setLabelVisibilityMode(LABEL_VISIBILITY_UNLABELED);

        setupNavigation();

        presenter = new MainPresenter(this);
        presenter.onViewCreated(savedInstanceState != null);

        bottomNavigationView.setOnItemSelectedListener(item ->
                presenter.onBottomNavItemSelected(item.getItemId(), bottomNavigationView.getSelectedItemId())
        );
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_container_main);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                Menu menu = bottomNavigationView.getMenu();
                if (menu.findItem(destination.getId()) != null) {
                    menu.findItem(destination.getId()).setChecked(true);
                }
            });
        }
    }

    @Override
    public void removeOverlay() {
        if (overlayView != null && overlayView.getParent() instanceof ViewGroup) {
            ((ViewGroup) overlayView.getParent()).removeView(overlayView);
        }
    }

    @Override
    public void navigateToSection(int destinationId) {
        // The View constructs the complex Android NavOptions
        NavOptions navOptions = new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .setPopEnterAnim(R.anim.slide_in_right)
                .setPopExitAnim(R.anim.slide_out_left)
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(R.id.discoverFragment, false, true)
                .build();

        navController.navigate(destinationId, null, navOptions);
    }

    @Override
    public void showIntroAnimation() {
        overlayView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        overlayView.getViewTreeObserver().removeOnPreDrawListener(this);
                        float gapOffset = overlayView.getHeight() * overlayView.getGapOffsetPercentage();
                        overlayView.setTranslationY(gapOffset);
                        runExitAnimation();
                        return true;
                    }
                });
    }

    private void runExitAnimation() {
        float currentTranslation = overlayView.getTranslationY();
        float targetTranslation = -(overlayView.getHeight() + currentTranslation);

        Log.d("Main", "runExitAnimation: currentTranslation" + currentTranslation);
        Log.d("Main", "runExitAnimation: targetTranslation" + targetTranslation);

        AnimationUtil.animateTranslationY(
                overlayView,
                targetTranslation,
                null,
                this::removeOverlay,
                0,
                600,
                new AccelerateDecelerateInterpolator()
        );
    }
}