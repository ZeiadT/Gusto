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
import iti.mad.gusto.domain.entity.InstructionEntity;
import iti.mad.gusto.presentation.common.util.ImageUtil;

/**
 * Expandable adapter for Ingredients and Instructions sections.
 * Each section is a group row that can expand/collapse to show child rows.
 */
public class MealDetailsExpandableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_GROUP = 0;
    private static final int TYPE_INGREDIENT = 1;
    private static final int TYPE_INSTRUCTION = 2;

    private final List<Object> items = new ArrayList<>();
    private boolean ingredientsExpanded = true;
    private boolean instructionsExpanded = true;
    private List<IngredientEntity> ingredients = new ArrayList<>();
    private List<InstructionEntity> instructions = new ArrayList<>();
    private String itemsLabel = "items";

    public void setItemsLabel(String itemsLabel) {
        this.itemsLabel = itemsLabel != null ? itemsLabel : "items";
    }

    public void setData(List<IngredientEntity> ingredients, List<InstructionEntity> instructions) {
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
        this.instructions = instructions != null ? instructions : new ArrayList<>();
        rebuildList();
    }

    private void rebuildList() {
        items.clear();
        String ingredientCount = ingredients.size() + " " + itemsLabel;
        items.add(new GroupItem(ingredientCount, ingredientsExpanded, true));
        if (ingredientsExpanded) {
            items.addAll(ingredients);
        }
        String instructionCount = instructions.size() + " steps";
        items.add(new GroupItem(instructionCount, instructionsExpanded, false));
        if (instructionsExpanded) {
            items.addAll(instructions);
        }
        notifyDataSetChanged();
    }

    public void setIngredientsExpanded(boolean expanded) {
        if (ingredientsExpanded == expanded) return;
        ingredientsExpanded = expanded;
        rebuildList();
    }

    public void setInstructionsExpanded(boolean expanded) {
        if (instructionsExpanded == expanded) return;
        instructionsExpanded = expanded;
        rebuildList();
    }

    public void toggleIngredients() {
        setIngredientsExpanded(!ingredientsExpanded);
    }

    public void toggleInstructions() {
        setInstructionsExpanded(!instructionsExpanded);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof GroupItem) return TYPE_GROUP;
        if (item instanceof IngredientEntity) return TYPE_INGREDIENT;
        if (item instanceof InstructionEntity) return TYPE_INSTRUCTION;
        return TYPE_GROUP;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_GROUP) {
            View v = inflater.inflate(R.layout.item_expandable_group, parent, false);
            return new GroupViewHolder(v);
        }
        if (viewType == TYPE_INGREDIENT) {
            View v = inflater.inflate(R.layout.item_ingredient, parent, false);
            return new IngredientViewHolder(v);
        }
        View v = inflater.inflate(R.layout.item_instruction, parent, false);
        return new InstructionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);
        if (holder instanceof GroupViewHolder) {
            GroupItem group = (GroupItem) item;
            ((GroupViewHolder) holder).bind(group, position);
        } else if (holder instanceof IngredientViewHolder) {
            ((IngredientViewHolder) holder).bind((IngredientEntity) item);
        } else if (holder instanceof InstructionViewHolder) {
            int stepIndex = findInstructionIndex(position);
            ((InstructionViewHolder) holder).bind((InstructionEntity) item, stepIndex + 1);
        }
    }

    private int findInstructionIndex(int position) {
        int index = 0;
        for (int i = 0; i < position; i++) {
            if (items.get(i) instanceof InstructionEntity) index++;
        }
        return index;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private static class GroupItem {
        final String countText;
        final boolean expanded;
        final boolean isIngredients;

        GroupItem(String countText, boolean expanded, boolean isIngredients) {
            this.countText = countText;
            this.expanded = expanded;
            this.isIngredients = isIngredients;
        }
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivExpand;
        private final TextView tvTitle;
        private final TextView tvCount;

        GroupViewHolder(View itemView) {
            super(itemView);
            ivExpand = itemView.findViewById(R.id.iv_expand_icon);
            tvTitle = itemView.findViewById(R.id.tv_group_title);
            tvCount = itemView.findViewById(R.id.tv_group_count);
        }

        void bind(GroupItem group, int position) {
            tvTitle.setText(group.isIngredients ? R.string.ingredients : R.string.instructions);
            tvCount.setText(group.countText);
            ivExpand.setRotation(group.expanded ? 180f : 0f);
            itemView.setOnClickListener(v -> {
                if (group.isIngredients) toggleIngredients();
                else toggleInstructions();
            });
        }
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private final android.widget.ImageView imageView;
        private final TextView nameTextView;
        private final TextView measureTextView;

        IngredientViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_product_icon);
            nameTextView = itemView.findViewById(R.id.tv_product_name);
            measureTextView = itemView.findViewById(R.id.tv_product_measure);
        }

        void bind(IngredientEntity ingredient) {
            nameTextView.setText(ingredient.getName());
            measureTextView.setText(ingredient.getMeasure());
            ImageUtil.loadFromNetwork(itemView.getContext(), imageView, ingredient.getImage());
        }
    }

    static class InstructionViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvStepNumber;
        private final TextView tvStepText;

        InstructionViewHolder(View itemView) {
            super(itemView);
            tvStepNumber = itemView.findViewById(R.id.tv_step_number);
            tvStepText = itemView.findViewById(R.id.tv_step_text);
        }

        void bind(InstructionEntity instruction, int stepNumber) {
            tvStepNumber.setText(String.valueOf(stepNumber));
            tvStepText.setText(instruction.getStep());
        }
    }
}
