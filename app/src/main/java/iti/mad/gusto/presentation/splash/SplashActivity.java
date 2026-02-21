package iti.mad.gusto.presentation.splash;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import iti.mad.gusto.R;
import iti.mad.gusto.data.repo.AuthRepository;
import iti.mad.gusto.data.repo.SettingsRepository;
import iti.mad.gusto.presentation.auth.activity.AuthActivity;
import iti.mad.gusto.presentation.common.component.PolygonView;
import iti.mad.gusto.presentation.common.constant.NavigationKey;
import iti.mad.gusto.presentation.common.util.AnimationUtil;
import iti.mad.gusto.presentation.main.activity.MainActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    private PolygonView overlayView;
    private ImageView imgLogo;
    private TextView tvAppName;
    private TextView tvSlogan;
    private SplashContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        initUI();
        presenter = new SplashPresenter(this, SettingsRepository.getInstance(this), AuthRepository.getInstance(this));
    }

    private void initUI() {
        imgLogo = findViewById(R.id.imgLogo);
        tvAppName = findViewById(R.id.tvAppName);
        tvSlogan = findViewById(R.id.tvSlogan);
        overlayView = findViewById(R.id.overlayView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startSplashSequence();
    }

    private void startSplashSequence() {
        AnimationUtil.startVectorAnimation(imgLogo,
                () -> AnimationUtil.animateVisibilityWithResource(tvAppName, R.anim.slide_up_fade,
                        () -> AnimationUtil.animateVisibilityWithResource(tvSlogan, R.anim.fade_in, this::prepareAndAnimateOverlay)));
    }

    private void prepareAndAnimateOverlay() {
        overlayView.post(() -> {
            int viewHeight = overlayView.getHeight();
            overlayView.setTranslationY(viewHeight);

            float gapOffset = viewHeight * overlayView.getGapOffsetPercentage();

            AnimationUtil.animateTranslationY(
                    overlayView,
                    gapOffset,
                    () -> overlayView.setVisibility(VISIBLE),
                    presenter::navigate,
                    600,
                    600,
                    new FastOutSlowInInterpolator()
            );
        });
    }

    @Override
    public void navigateMain() {
        activityPushReplacement(MainActivity.class);
    }

    @Override
    public void navigateAuth() {
        activityPushReplacement(AuthActivity.class);
    }

    private void activityPushReplacement(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(NavigationKey.SHOULD_SKIP_OVERLAY_ANIMATION, false);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
    }
}