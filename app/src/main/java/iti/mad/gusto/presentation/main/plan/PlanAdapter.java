package iti.mad.gusto.presentation.main.plan;

import android.content.Context;
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
import iti.mad.gusto.domain.entity.PlanMealEntity;
import iti.mad.gusto.presentation.common.util.ImageUtil;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private List<PlanMealEntity> plans;
    private final OnPlanClickListener listener;

    public interface OnPlanClickListener {
        void onMealClick(PlanMealEntity meal);
    }

    public PlanAdapter(OnPlanClickListener listener) {
        this.plans = new ArrayList<>();
        this.listener = listener;
    }

    public PlanAdapter(List<PlanMealEntity> plans, OnPlanClickListener listener) {
        this.plans = plans;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlanAdapter.PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_card_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {

        holder.bind(plans.get(position));
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public void setPlans(List<PlanMealEntity> newPlans) {
        this.plans = newPlans;
        notifyDataSetChanged();
    }
    public PlanMealEntity getByPosition(int position) {
        return this.plans.get(position);
    }
    public void removeByPosition(int position) {
        this.plans.remove(position);
        notifyDataSetChanged();
    }

    public class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeader, tvTitle, tvTag, tvCountry;
        ImageView ivImage;

        PlanViewHolder(View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tvSectionHeader);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTag = itemView.findViewById(R.id.tvTag);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            ivImage = itemView.findViewById(R.id.ivMealImage);

        }

        void bind(PlanMealEntity plan) {
            if (tvHeader != null) tvHeader.setText(plan.getType().name());

            tvTitle.setText(plan.getName());
            tvTag.setText(plan.getCategory());
            tvCountry.setText(plan.getArea());
            ImageUtil.loadFromNetwork(itemView.getContext(), ivImage, plan.getImage());
            itemView.setOnClickListener(v -> listener.onMealClick(plan));
        }
    }

}