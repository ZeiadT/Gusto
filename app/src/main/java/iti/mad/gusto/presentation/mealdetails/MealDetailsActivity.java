package iti.mad.gusto.presentation.mealdetails;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import iti.mad.gusto.R;
import iti.mad.gusto.domain.entity.MealEntity;
import iti.mad.gusto.presentation.common.util.ImageUtil;

public class MealDetailsActivity extends AppCompatActivity implements MealDetailsContract.View {
    private YouTubePlayerView youTubePlayerView;
    private RecyclerView ingredientRecyclerView;
    private IngredientAdapter ingredientAdapter;

    private ImageView mealImageView;
    private TextView mealTitleTextView;
    private TextView mealCategoryTextView;
    private TextView mealCountryTextView;
    private TextView mealInstructionsTextView;
    private TextView mealIngredientsTextView;

    private CheckBox favoriteCheckBox;
    private ImageButton backButton;



    MealDetailsContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        favoriteCheckBox = findViewById(R.id.btnFavorite);
        backButton = findViewById(R.id.btnBack);

        youTubePlayerView = findViewById(R.id.youtube_player_view);

        ingredientRecyclerView = findViewById(R.id.ingredient_recyclerview);
        ingredientAdapter = new IngredientAdapter();
        mealIngredientsTextView = findViewById(R.id.ingredient_count);

        mealImageView = findViewById(R.id.imageView);
        mealTitleTextView = findViewById(R.id.titleTV);

        mealCategoryTextView = findViewById(R.id.category_tag);
        mealCountryTextView = findViewById(R.id.country_tag);

        mealInstructionsTextView = findViewById(R.id.instructions_desc);

        initIngredientsRecyclerView();

        presenter = new MealDetailsPresenter(this);

        backButton.setOnClickListener(v -> finish());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            presenter.getMealDetails(extras.getString("mealId"));
        }
    }

    void initIngredientsRecyclerView() {
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientRecyclerView.setAdapter(ingredientAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        getLifecycle().addObserver(youTubePlayerView);
    }

    @Override
    public void showMealDetails(MealEntity meal) {
        mealTitleTextView.setText(meal.getName());
        mealCategoryTextView.setText(meal.getCategory());
        mealCountryTextView.setText(meal.getArea());
        mealInstructionsTextView.setText(meal.getInstructions().get(0).getStep());
        ingredientAdapter.setIngredients(meal.getIngredients());
        mealIngredientsTextView.setText(meal.getIngredients().size() + " items");
        ImageUtil.loadFromNetwork(this, mealImageView, meal.getImage());
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener(){
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(meal.getYoutube(), 0);
            }


        });
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}