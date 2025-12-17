package pl_project;

public class TrueFalseQuestion extends Question {
    private boolean correctAnswer;

    public TrueFalseQuestion(int id, String text, float mark, boolean correctAnswer) {
        super(id, text, mark);
        this.correctAnswer = correctAnswer;
    }

    @Override
    public float grade(StudentAnswer answer) {
        try {
            boolean studentValue = Boolean.parseBoolean(answer.getAnswer());
            return (studentValue == correctAnswer) ? mark : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String toStringExtra() {
        return String.valueOf(correctAnswer);
    }
}