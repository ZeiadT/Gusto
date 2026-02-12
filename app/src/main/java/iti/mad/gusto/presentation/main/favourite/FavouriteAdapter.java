package iti.mad.gusto.presentation.main.favourite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import iti.mad.gusto.R;
import iti.mad.gusto.domain.entity.FavouriteMealEntity;
import iti.mad.gusto.presentation.common.util.ImageUtil;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {
    List<FavouriteMealEntity> meals;
    OnItemClickListener listener;

    public FavouriteAdapter(OnItemClickListener listener, List<FavouriteMealEntity> meals) {
        this.meals = meals;
        this.listener = listener;
    }

    public FavouriteAdapter(OnItemClickListener listener) {
        meals = new ArrayList<>();
        this.listener = listener;
    }

    public void setMeals(List<FavouriteMealEntity> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_card, parent, false);

        return new FavouriteViewHolder(view, listener);

    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        holder.bind(meals.get(position));
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public interface OnItemClickListener {
        void onItemClick(FavouriteMealEntity meal);
        void onFavClick(FavouriteMealEntity meal);
    }

    public static class FavouriteViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoodImage, ivFav;
        TextView tvTitle;
        OnItemClickListener listener;

        public FavouriteViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            ivFoodImage = itemView.findViewById(R.id.iv_food_image);
            ivFav = itemView.findViewById(R.id.iv_fav);
            tvTitle = itemView.findViewById(R.id.tv_title);
            this.listener = listener;
        }

        void bind(FavouriteMealEntity meal) {
            tvTitle.setText(meal.getName());
            itemView.setOnClickListener(v -> listener.onItemClick(meal));
            ivFav.setOnClickListener(v -> listener.onFavClick(meal));
            ImageUtil.loadFromNetwork(itemView.getContext(), ivFoodImage, meal.getImage());
        }
    }
}
