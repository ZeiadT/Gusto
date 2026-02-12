package iti.mad.gusto.presentation.mealdetails;

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
import iti.mad.gusto.domain.entity.IngredientEntity;
import iti.mad.gusto.presentation.common.util.ImageUtil;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    List<IngredientEntity> ingredients;

    public IngredientAdapter() {
        this.ingredients = new ArrayList<>();
    }

    public IngredientAdapter(List<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }

    public void setIngredients(List<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        IngredientEntity ingredient = ingredients.get(position);
        holder.bind(ingredient);

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView nameTextView;
        private final TextView measureTextView;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_product_icon);
            nameTextView = itemView.findViewById(R.id.tv_product_name);
            measureTextView = itemView.findViewById(R.id.tv_product_measure);
        }

        public void bind(IngredientEntity ingredient) {
            nameTextView.setText(ingredient.getName());
            measureTextView.setText(ingredient.getMeasure());

            ImageUtil.loadFromNetwork(itemView.getContext(), imageView, ingredient.getImage());
        }
    }
}
