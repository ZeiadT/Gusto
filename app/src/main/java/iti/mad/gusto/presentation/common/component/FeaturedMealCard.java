package iti.mad.gusto.presentation.common.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import iti.mad.gusto.R;
import iti.mad.gusto.presentation.common.util.ImageUtil;

public class FeaturedMealCard extends MaterialCardView {

    private ImageView ivMealImage;
    private TextView tvTitle, tvSubtitle;
    private MaterialButton btnAdd;
    private CheckBox btnFavorite;

    private String mealId;

    public FeaturedMealCard(Context context) {
        super(context);
        init(context, null);
    }

    public FeaturedMealCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FeaturedMealCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public String getMealId() {
        return mealId;
    }
    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    private void init(Context context, AttributeSet attrs) {
        // 1. Inflate the new layout file
        LayoutInflater.from(context).inflate(R.layout.featured_meal_card, this, true);

        // 2. Default Styling
        setRadius(24);
        setCardElevation(8f);

        // 3. Bind Views
        ivMealImage = findViewById(R.id.ivMealImage);
        tvTitle = findViewById(R.id.tvTitle);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        btnAdd = findViewById(R.id.btnAdd);
        btnFavorite = findViewById(R.id.btnFavorite);

        // 4. Handle Attributes
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FeaturedMealCard);

            String title = a.getString(R.styleable.FeaturedMealCard_mealTitle);
            String subtitle = a.getString(R.styleable.FeaturedMealCard_mealSubtitle);
            int imageResId = a.getResourceId(R.styleable.FeaturedMealCard_mealImage, -1);
            float cardRadius = a.getDimension(R.styleable.FeaturedMealCard_cardRadius, 24);

            if (title != null) tvTitle.setText(title);
            if (subtitle != null) tvSubtitle.setText(subtitle);
            if (imageResId != -1) ivMealImage.setImageResource(imageResId);
            if (cardRadius != -1) setRadius(cardRadius);

            a.recycle();
        }
    }

    public void setOnFavoriteClickListener(OnClickListener listener) {
        btnFavorite.setOnClickListener(listener);
    }

    public void setOnAddClickListener(OnClickListener listener) {
        btnAdd.setOnClickListener(listener);
    }

    public void setOnClickListener(OnClickListener listener) {
        super.setOnClickListener(listener);
    }

    public void setMealData(String title, String subtitle, String imageUrl) {
        tvTitle.setText(title);
        tvSubtitle.setText(subtitle);

        ImageUtil.loadFromNetwork(getContext(), ivMealImage, imageUrl);
    }
}