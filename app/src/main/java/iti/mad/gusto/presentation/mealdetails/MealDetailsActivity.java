package iti.mad.gusto.presentation.mealdetails;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.Objects;

import iti.mad.gusto.R;
import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.presentation.common.component.AddToPlanBottomSheet;
import iti.mad.gusto.presentation.common.component.RippleOverlayView;
import iti.mad.gusto.presentation.common.util.ImageUtil;
import iti.mad.gusto.presentation.common.util.ThemeAwareIconToast;
import iti.mad.gusto.presentation.common.util.ThemeAwareIconToastWithVibration;
import iti.mad.gusto.presentation.common.util.WaveEffectManager;

public class MealDetailsActivity extends AppCompatActivity implements MealDetailsContract.View {

    private YouTubePlayerView youTubePlayerView;

    // Ingredients section
    private View headerIngredients;
    private RecyclerView rvIngredients;
    private IngredientAdapter ingredientAdapter;
    private boolean ingredientsExpanded = true;

    // Instructions section
    private View headerInstructions;
    private RecyclerView rvInstructions;
    private InstructionAdapter instructionAdapter;
    private boolean instructionsExpanded = false;

    private ImageView mealImageView;
    private TextView mealTitleTextView;
    private TextView mealCategoryTextView;
    private TextView mealCountryTextView;
    private FloatingActionButton addToPlanButton;
    private CheckBox favoriteCheckBox;
    private ImageButton backButton;
    private View contentHolder;
    private View connectionLottie;
    private View appBarLayout;
    private RippleOverlayView rippleOverlay;
    private View rootLayout;

    MealDetailsContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        addToPlanButton = findViewById(R.id.addToPlanBtn);
        favoriteCheckBox = findViewById(R.id.btnFavorite);
        backButton = findViewById(R.id.btnBack);

        youTubePlayerView = findViewById(R.id.youtube_player_view);

        // --- Ingredients Section ---
        headerIngredients = findViewById(R.id.header_ingredients);
        rvIngredients = findViewById(R.id.rv_ingredients);
        ingredientAdapter = new IngredientAdapter();
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        rvIngredients.setAdapter(ingredientAdapter);
        setHeaderIcon(headerIngredients, R.drawable.ic_leaf);
        setHeaderTitle(headerIngredients, getString(R.string.ingredients));
        setExpandChevron(headerIngredients, ingredientsExpanded);
        headerIngredients.setOnClickListener(v -> toggleIngredients());

        // --- Instructions Section ---
        headerInstructions = findViewById(R.id.header_instructions);
        rvInstructions = findViewById(R.id.rv_instructions);
        instructionAdapter = new InstructionAdapter();
        rvInstructions.setLayoutManager(new LinearLayoutManager(this));
        rvInstructions.setAdapter(instructionAdapter);
        // Instructions collapsed by default
        rvInstructions.setVisibility(View.GONE);
        setHeaderIcon(headerInstructions, R.drawable.ic_hashtag);
        setHeaderTitle(headerInstructions, getString(R.string.instructions));
        setExpandChevron(headerInstructions, instructionsExpanded);
        headerInstructions.setOnClickListener(v -> toggleInstructions());

        mealImageView = findViewById(R.id.imageView);
        mealTitleTextView = findViewById(R.id.titleTV);
        mealCategoryTextView = findViewById(R.id.category_tag);
        mealCountryTextView = findViewById(R.id.country_tag);

        contentHolder = findViewById(R.id.contentHolder);
        connectionLottie = findViewById(R.id.connectionLottie);
        appBarLayout = findViewById(R.id.appBarLayout);

        rippleOverlay = findViewById(R.id.detailsRippleOverlay);
        rootLayout = findViewById(R.id.main);

        presenter = new MealDetailsPresenter(this, this);

        backButton.setOnClickListener(v -> finish());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            presenter.getMealDetails(extras.getString("mealId"));
        }

        addToPlanButton.setOnClickListener(v -> {
            AddToPlanBottomSheet bottomSheet = AddToPlanBottomSheet.newInstance();
            bottomSheet.show(getSupportFragmentManager(), "AddToPlanBottomSheet");
            bottomSheet.setOnConfirmListener((date, mealType) -> {
                presenter.onFeaturedMealAddToPlan(date, mealType);
            });
        });

        favoriteCheckBox.setOnCheckedChangeListener((btn, isChecked) -> {
            presenter.onFavoriteClicked(isChecked);

            if (isChecked && btn.isPressed()) {
                WaveEffectManager.fireWave(btn, rootLayout, rippleOverlay);
            }
        });

        presenter.addConnectivityListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getLifecycle().addObserver(youTubePlayerView);
    }

    // region Header helpers

    /**
     * Sets the section icon on the given header view.
     */
    private void setHeaderIcon(View header, int iconRes) {
        ImageView icon = header.findViewById(R.id.iv_section_icon);
        if (icon != null)
            icon.setImageResource(iconRes);
    }

    /**
     * Sets the title text on the given header view.
     */
    private void setHeaderTitle(View header, String title) {
        TextView tv = header.findViewById(R.id.tv_group_title);
        if (tv != null)
            tv.setText(title);
    }

    /**
     * Sets the count badge text on the given header view.
     */
    private void setHeaderCount(View header, String countText) {
        TextView tv = header.findViewById(R.id.tv_group_count);
        if (tv != null)
            tv.setText(countText);
    }

    /**
     * Rotates the expand chevron icon based on expanded state.
     */
    private void setExpandChevron(View header, boolean expanded) {
        ImageView chevron = header.findViewById(R.id.iv_expand_icon);
        if (chevron != null)
            chevron.setRotation(expanded ? 180f : 0f);
    }

    // endregion

    // region Toggle logic

    private void toggleIngredients() {
        ingredientsExpanded = !ingredientsExpanded;
        rvIngredients.setVisibility(ingredientsExpanded ? View.VISIBLE : View.GONE);
        setExpandChevron(headerIngredients, ingredientsExpanded);
    }

    private void toggleInstructions() {
        instructionsExpanded = !instructionsExpanded;
        rvInstructions.setVisibility(instructionsExpanded ? View.VISIBLE : View.GONE);
        setExpandChevron(headerInstructions, instructionsExpanded);
    }

    // endregion

    @Override
    public void setFavouriteIcon(boolean isFavourite) {
        if (favoriteCheckBox == null)
            return;
        favoriteCheckBox.setChecked(isFavourite);
        favoriteCheckBox.setButtonDrawable(isFavourite ? R.drawable.bookmark_fill : R.drawable.bookmark);
    }

    @Override
    public void showMealDetails(MealEntity meal) {
        if (meal == null || isDestroyed()) {
            return;
        }
        mealTitleTextView.setText(meal.getName());
        mealCategoryTextView.setText(meal.getCategory());
        mealCountryTextView.setText(meal.getArea());

        // Ingredients
        ingredientAdapter.setIngredients(meal.getIngredients());
        String ingredientCount = (meal.getIngredients() != null ? meal.getIngredients().size() : 0)
                + " " + getString(R.string.items);
        setHeaderCount(headerIngredients, ingredientCount);

        // Instructions
        instructionAdapter.setInstructions(meal.getInstructions());
        String instructionCount = (meal.getInstructions() != null ? meal.getInstructions().size() : 0)
                + " " + getString(R.string.steps);
        setHeaderCount(headerInstructions, instructionCount);

        ImageUtil.loadFromNetwork(this, mealImageView, meal.getImage());
        if (meal.getYoutube() != null) {
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(meal.getYoutube(), 0);
                }
            });
        }
    }

    @Override
    public void showError(String message) {
        ThemeAwareIconToast.error(this, message);
    }

    @Override
    public void showWarning(String message) {
        ThemeAwareIconToastWithVibration.warning(this, message);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void onNetworkDisconnected() {

        Handler mainHandler = new Handler(Looper.getMainLooper());

        mainHandler.post(() -> {
            contentHolder.setVisibility(View.GONE);
            appBarLayout.setVisibility(View.GONE);
            addToPlanButton.setVisibility(View.GONE);
            connectionLottie.setVisibility(View.VISIBLE);

        });
    }

    @Override
    public void onNetworkReconnected() {

        Handler mainHandler = new Handler(Looper.getMainLooper());

        mainHandler.post(() -> {
            contentHolder.setVisibility(View.VISIBLE);
            appBarLayout.setVisibility(View.VISIBLE);
            addToPlanButton.setVisibility(View.VISIBLE);
            connectionLottie.setVisibility(View.GONE);
            presenter.getMealDetails(Objects.requireNonNull(getIntent().getExtras()).getString("mealId"));
        });
    }

}