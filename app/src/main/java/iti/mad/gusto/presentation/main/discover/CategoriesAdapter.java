package iti.mad.gusto.presentation.main.discover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import iti.mad.gusto.R;
import iti.mad.gusto.domain.entity.CategoryEntity;
import iti.mad.gusto.presentation.common.util.ImageUtil;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {
    List<CategoryEntity> categories;
    static OnCategoryClickListener onCategoryClickListener;

    public CategoriesAdapter(OnCategoryClickListener onClick) {
        categories = new ArrayList<>();
        onCategoryClickListener = onClick;
    }

    public void setCategories(List<CategoryEntity> categories, OnCategoryClickListener onClick) {
        this.categories = categories;
        onCategoryClickListener = onClick;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_category_discover, parent, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.category_name_view);
        }

        public void bind(CategoryEntity category) {
            Glide.with(itemView.getContext()).load(category.getImage()).into(imageView);
            ImageUtil.loadFromNetworkWithMatchingBackground(itemView.getContext(), imageView, category.getImage());
            textView.setText(category.getName());

            itemView.setOnClickListener(v -> {
                onCategoryClickListener.onClicked(category);
            });

        }
    }

}
