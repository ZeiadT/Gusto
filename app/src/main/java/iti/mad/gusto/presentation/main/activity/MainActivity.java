package iti.mad.gusto.presentation.main.activity;

import static com.google.android.material.navigation.NavigationBarView.LABEL_VISIBILITY_UNLABELED;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import iti.mad.gusto.R;
import iti.mad.gusto.domain.entity.SearchTagEntity;
import iti.mad.gusto.presentation.common.component.PolygonView;
import iti.mad.gusto.presentation.common.constant.NavigationKey;
import iti.mad.gusto.presentation.common.util.AnimationUtil;
import iti.mad.gusto.presentation.main.discover.DiscoverFragment;
import iti.mad.gusto.presentation.main.favourite.FavouriteFragment;
import iti.mad.gusto.presentation.main.plan.PlanFragment;
import iti.mad.gusto.presentation.main.search.SearchFragment;
import iti.mad.gusto.presentation.main.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity implements MainContract.View, BottomNavBarCommunicator, PendingSearchTagProvider {

    private PolygonView overlayView;
    private BottomNavigationView bottomNavigationView;
    private MainContract.Presenter presenter;
    private FragmentManager fragmentManager;
    private boolean shouldSkipOverlayAnimation = true;
    private SearchTagEntity pendingSearchTag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        overlayView = findViewById(R.id.main_overlay_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        Intent intent = getIntent();
        if (intent != null) {
            shouldSkipOverlayAnimation = intent.getBooleanExtra(NavigationKey.SHOULD_SKIP_OVERLAY_ANIMATION, true);
        }

        presenter = new MainPresenter(this, this);
        presenter.animateIntro(savedInstanceState != null, shouldSkipOverlayAnimation);

        fragmentManager = getSupportFragmentManager();

        setupNavigation();
        if (presenter.isGuestUser()) {
            hideRestrictedMenuItems();
        }

        if (savedInstanceState == null) {
            showInitialFragment(R.id.discoverFragment, new DiscoverFragment());
        }
    }

    private void showInitialFragment(int fragmentID, Fragment fragment) {
        String tag = String.valueOf(fragmentID);
        fragmentManager
                .beginTransaction().add(R.id.frag_container_main, fragment, tag)
                .commit();
    }

    private void hideRestrictedMenuItems() {
        Menu menu = bottomNavigationView.getMenu();

        MenuItem favoritesItem = menu.findItem(R.id.favouriteFragment);
        MenuItem planItem = menu.findItem(R.id.planFragment);

        if (favoritesItem != null) {
            favoritesItem.setVisible(false);
        }
        if (planItem != null) {
            planItem.setVisible(false);
        }
    }

    private void setupNavigation() {
        bottomNavigationView.setLabelVisibilityMode(LABEL_VISIBILITY_UNLABELED);

        bottomNavigationView.setOnItemSelectedListener(item ->
                presenter.onBottomNavItemSelected(
                        item.getItemId(),
                        getNavPositionFromId(item.getItemId()),
                        bottomNavigationView.getSelectedItemId(),
                        getNavPositionFromId(bottomNavigationView.getSelectedItemId()
                        )
                )
        );

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                int currentId = bottomNavigationView.getSelectedItemId();

                if (currentId != R.id.discoverFragment) {
                    bottomNavigationView.setSelectedItemId(R.id.discoverFragment);
                    return;
                }

                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    private int getNavPositionFromId(int id) {
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == id) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void navigateToSection(int destinationId, boolean isForward) {
        int enterAnim = isForward ? R.anim.slide_right_to_center : R.anim.slide_left_to_center;
        int exitAnim = isForward ? R.anim.slide_center_to_left : R.anim.slide_center_to_right;

        String tag = String.valueOf(destinationId);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enterAnim, exitAnim, R.anim.slide_left_to_center, R.anim.slide_center_to_right);

        Fragment targetFragment = fragmentManager.findFragmentByTag(tag);
        if (targetFragment == null) {
            targetFragment = createFragmentFromId(destinationId);
            if (targetFragment == null) return;

            transaction.add(R.id.frag_container_main, targetFragment, tag);
        }

        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment == null)
                continue;

            if (fragment == targetFragment) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }

        }

        transaction.commit();
    }

    private Fragment createFragmentFromId(int id) {
        if (id == R.id.discoverFragment) {
            return new DiscoverFragment();
        } else if (id == R.id.searchFragment) {
            return new SearchFragment();
        } else if (id == R.id.favouriteFragment) {
            return new FavouriteFragment();
        } else if (id == R.id.planFragment) {
            return new PlanFragment();
        } else if (id == R.id.settingsFragment) {
            return new SettingsFragment();
        } else {
            return null;
        }
    }

    @Override
    public void animateIntro() {
        overlayView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        overlayView.getViewTreeObserver().removeOnPreDrawListener(this);
                        float gapOffset = overlayView.getHeight() * overlayView.getGapOffsetPercentage();
                        overlayView.setTranslationY(gapOffset);
                        animateOverlayRemoval();
                        return true;
                    }
                });
    }

    @Override
    public void skipIntro() {
        removeOverlay();
    }

    private void animateOverlayRemoval() {
        float currentTranslation = overlayView.getTranslationY();
        float targetTranslation = -(overlayView.getHeight() + currentTranslation);

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

    public void removeOverlay() {
        if (overlayView != null && overlayView.getParent() instanceof ViewGroup) {
            ((ViewGroup) overlayView.getParent()).removeView(overlayView);
        }
    }

    @Override
    public void navigateToSearchWithTag(SearchTagEntity tag) {
        pendingSearchTag = tag;
        bottomNavigationView.setSelectedItemId(R.id.searchFragment);

        SearchFragment searchFragment = (SearchFragment) fragmentManager.findFragmentByTag(String.valueOf(R.id.searchFragment));

        if (searchFragment != null && tag != null) {
            searchFragment.onTagReceived(tag);
        }

    }

    @Override
    public SearchTagEntity getAndClearPendingSearchTag() {
        SearchTagEntity tag = pendingSearchTag;
        pendingSearchTag = null;
        return tag;
    }
}