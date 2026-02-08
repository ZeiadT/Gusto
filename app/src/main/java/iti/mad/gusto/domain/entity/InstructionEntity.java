package iti.mad.gusto.domain.entity;

public class InstructionEntity {
    private String step;

    public InstructionEntity(String step) {
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