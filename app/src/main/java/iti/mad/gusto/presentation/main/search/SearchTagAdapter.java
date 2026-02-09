package iti.mad.gusto.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import iti.mad.gusto.R;
import iti.mad.gusto.domain.entity.SearchTagEntity;

public class SearchTagAdapter extends ArrayAdapter<SearchTagEntity> {

    private final List<SearchTagEntity> originalList;
    private final List<SearchTagEntity> filteredList;
    private final Context context;

    public SearchTagAdapter(@NonNull Context context, @NonNull List<SearchTagEntity> tagList) {
        super(context, 0, tagList);
        this.context = context;
        this.originalList = new ArrayList<>(tagList);
        this.filteredList = new ArrayList<>(tagList);
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Nullable
    @Override
    public SearchTagEntity getItem(int position) {
        return filteredList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_tag, parent, false);
        }

        SearchTagEntity tag = getItem(position);

        if (tag != null) {
            TextView tvTagName = convertView.findViewById(R.id.tvTagName);
            TextView tvTagType = convertView.findViewById(R.id.tvTagType);

            tvTagName.setText(tag.getTagName());

            if (tag.getTagType() != null) {
                tvTagType.setText(tag.getTagType().toString());
            }
        }

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<SearchTagEntity> suggestions = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    suggestions.addAll(originalList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim();

                    for (SearchTagEntity item : originalList) {
                        if (item.getTagName() != null &&
                                item.getTagName().toLowerCase(Locale.ROOT).contains(filterPattern)) {
                            suggestions.add(item);
                        }
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList.clear();
                if (results != null && results.values != null) {
                    filteredList.addAll((List<SearchTagEntity>) results.values);
                }
                notifyDataSetChanged();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                // IMPORTANT: This controls what text appears in the input field when clicked
                return ((SearchTagEntity) resultValue).getTagName();
            }
        };
    }

    public void updateData(List<SearchTagEntity> newTags) {
        this.originalList.clear();
        this.filteredList.clear();

        if (newTags != null) {
            this.originalList.addAll(newTags);
            this.filteredList.addAll(newTags);
        }

        notifyDataSetChanged();
    }
}