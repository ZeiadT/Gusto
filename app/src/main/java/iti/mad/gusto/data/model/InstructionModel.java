package iti.mad.gusto.data.model;

public class InstructionModel {
    private final String step;

    public InstructionModel(String step) {
        this.step = step;
    }

    public String getStep() {
        return step;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "step='" + step + '\'' +
                '}' + "\n";
    }
}