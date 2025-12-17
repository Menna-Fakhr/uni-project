package pl_project;

import java.util.List;

public class MCQQuestion extends Question {
    private List<String> options;
    private int correctOptionIndex;

    public MCQQuestion(int id, String text, float mark, List<String> options, int correctOptionIndex) {
        super(id, text, mark);
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public List<String> getOptions() { return options; }

    @Override
    public float grade(StudentAnswer answer) {
        try {
            int chosen = Integer.parseInt(answer.getAnswer());
            return (chosen == correctOptionIndex) ? mark : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public String toStringExtra() {
        return String.join(":", options) + ";" + correctOptionIndex;
    }
}