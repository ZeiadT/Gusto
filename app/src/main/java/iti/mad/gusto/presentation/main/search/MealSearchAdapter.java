package iti.mad.gusto.presentation.main.search;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

import iti.mad.gusto.R;
import iti.mad.gusto.domain.entity.MealEntity;

public class MealSearchAdapter extends RecyclerView.Adapter<MealSearchAdapter.MealSearchViewHolder> {

    private List<MealEntity> mealList;
    private final Context context;
    private final OnMealClickListener listener;

    public MealSearchAdapter(Context context, OnMealClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.mealList = new ArrayList<>();
    }

    public void setList(List<MealEntity> newMeals) {
        this.mealList = newMeals;
        notifyDataSetChanged();
    }
    public List<MealEntity> getList() {
        return mealList;
    }


    @NonNull
    @Override
    public MealSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meal_grid_card, parent, false);
        return new MealSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealSearchViewHolder holder, int position) {
        holder.bind(mealList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return mealList != null ? mealList.size() : 0;
    }

    public interface OnMealClickListener {
        void onMealClick(MealEntity meal);
        void onFavoriteClick(MealEntity meal, boolean isFavorite);
    }

    public static class MealSearchViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMealThumb;
        TextView tvMealName;
        CheckBox btnFavorite;

        public MealSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMealThumb = itemView.findViewById(R.id.iv_meal_thumb);
            tvMealName = itemView.findViewById(R.id.tv_meal_name);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
        }


        public void bind(MealEntity meal, OnMealClickListener listener){

            tvMealName.setText(meal.getName());

            Glide.with(itemView.getContext())
                    .load(meal.getImage())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.logo)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivMealThumb);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMealClick(meal);
                }
            });

            btnFavorite.setOnCheckedChangeListener(null);

            btnFavorite.setChecked(false);

            btnFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (listener != null) listener.onFavoriteClick(meal, isChecked);
            });
        }
    }
}