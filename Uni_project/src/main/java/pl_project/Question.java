package pl_project;

import java.util.Arrays;
import java.util.List;

public abstract class Question {
    protected int id;
    protected String text;
    protected float mark;

    public Question(int id, String text, float mark) {
        this.id = id;
        this.text = text;
        this.mark = mark;
    }

    public int getId() { return id; }
    public String getText() { return text; }
    public float getMark() { return mark; }

    public void setText(String text) { this.text = text; }
    public void setMark(float mark) { this.mark = mark; }

    public abstract float grade(StudentAnswer answer);

    public abstract String toStringExtra();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "," + id + "," + text + "," + mark + "," + toStringExtra();
    }

    public static Question fromString(String line) {
        String[] data = line.split(",", 5);  // Limit to 5 parts
        if (data.length < 5) {
            throw new IllegalArgumentException("Invalid question line format: " + line);
        }

        String type = data[0];
        int id = Integer.parseInt(data[1]);
        String text = data[2];
        float mark = Float.parseFloat(data[3]);
        String extra = data[4];

        switch (type) {
            case "MCQQuestion":
                String[] parts = extra.split(";", 2);
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid MCQ extra format: " + extra);
                }
                String[] optionArray = parts[0].split(":");
                if (optionArray.length < 2) {
                    throw new IllegalArgumentException("MCQ must have at least 2 options");
                }
                List<String> options = Arrays.asList(optionArray);
                int correctIndex = Integer.parseInt(parts[1]);
                if (correctIndex < 0 || correctIndex >= options.size()) {
                    throw new IllegalArgumentException("Correct index out of bounds: " + correctIndex);
                }
                return new MCQQuestion(id, text, mark, options, correctIndex);

            case "TrueFalseQuestion":
                boolean correct = Boolean.parseBoolean(extra);
                return new TrueFalseQuestion(id, text, mark, correct);

            case "ShortAnswerQuestion":
                return new ShortAnswerQuestion(id, text, mark, extra);

            default:
                throw new IllegalArgumentException("Unknown question type: " + type);
        }
    }
}  // <--- This closing brace MUST be here!