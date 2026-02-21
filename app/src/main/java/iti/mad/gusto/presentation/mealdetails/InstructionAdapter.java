package iti.mad.gusto.presentation.mealdetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import iti.mad.gusto.R;
import iti.mad.gusto.domain.entity.InstructionEntity;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder> {

    private List<InstructionEntity> instructions;

    public InstructionAdapter() {
        this.instructions = new ArrayList<>();
    }

    public InstructionAdapter(List<InstructionEntity> instructions) {
        this.instructions = instructions != null ? instructions : new ArrayList<>();
    }

    public void setInstructions(List<InstructionEntity> instructions) {
        this.instructions = instructions != null ? instructions : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_instruction, parent, false);
        return new InstructionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionViewHolder holder, int position) {
        holder.bind(instructions.get(position), position + 1);
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    public static class InstructionViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvStepNumber;
        private final TextView tvStepText;

        public InstructionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStepNumber = itemView.findViewById(R.id.tv_step_number);
            tvStepText = itemView.findViewById(R.id.tv_step_text);
        }

        public void bind(InstructionEntity instruction, int stepNumber) {
            tvStepNumber.setText(String.valueOf(stepNumber));
            tvStepText.setText(instruction.getStep());
        }
    }
}
