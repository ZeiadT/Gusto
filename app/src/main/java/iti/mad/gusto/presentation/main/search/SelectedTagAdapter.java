package iti.mad.gusto.presentation.main.search;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import iti.mad.gusto.R;
import iti.mad.gusto.domain.entity.SearchTagEntity;
import iti.mad.gusto.presentation.common.util.ColorUtil;

public class SelectedTagAdapter extends RecyclerView.Adapter<SelectedTagAdapter.TagViewHolder> {
    List<SearchTagEntity> tags;

    public SelectedTagAdapter() {
        tags = new ArrayList<>();
    }

    public SelectedTagAdapter(List<SearchTagEntity> tags) {
        this.tags = tags;
    }

    public void setTags(List<SearchTagEntity> tags){
        this.tags = tags;
        notifyDataSetChanged();
    }
    public void addTag(SearchTagEntity tag){
        if (isDuplicateTag(tag)) return;
        tags.add(tag);
        notifyDataSetChanged();
    }
    public void removeTag(SearchTagEntity tag){
        tags.remove(tag);
        notifyDataSetChanged();
    }

    public List<SearchTagEntity> getTags() {
        return tags;
    }



    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip_search_tag, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        holder.bind(tags.get(position));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public class TagViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = (Chip) itemView;
        }
        public void bind(SearchTagEntity tag) {
            chip.setText(tag.getTagName());
            Context context = chip.getContext();

            int colorResId;
            switch (tag.getTagType()) {
                case COUNTRY:
                    colorResId = R.color.boarding_discover;
                    break;
                case INGREDIENT:
                    colorResId = R.color.boarding_plan;
                    break;
                default:
                    colorResId = R.color.boarding_never_lose;
            }

            int resolvedColor = ContextCompat.getColor(context, colorResId);
            int alphaBackgroundColor = ColorUtil.adjustAlpha(resolvedColor, 0.2f);

            chip.setChipStrokeColor(ColorStateList.valueOf(resolvedColor));
            chip.setChipBackgroundColor(ColorStateList.valueOf(alphaBackgroundColor));
            chip.setTextColor(resolvedColor);
            chip.setCloseIconTint(ColorStateList.valueOf(resolvedColor));

            chip.setOnClickListener(v -> SelectedTagAdapter.this.removeTag(tag));
        }
    }

    private boolean isDuplicateTag(SearchTagEntity tag){
        for (SearchTagEntity existingTag : tags) {

            if (existingTag.getTagType() == tag.getTagType() &&
                    existingTag.getTagName().equals(tag.getTagName()))
            {
                return true;
            }
        }
        return false;
    }
}
