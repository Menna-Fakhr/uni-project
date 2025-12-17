package pl_project;

public class ShortAnswerQuestion extends Question {
    private String sampleAnswer;

    public ShortAnswerQuestion(int id, String text, float mark, String sampleAnswer) {
        super(id, text, mark);
        this.sampleAnswer = sampleAnswer;
    }

    @Override
    public float grade(StudentAnswer answer) {
        return sampleAnswer.trim().equalsIgnoreCase(answer.getAnswer().trim()) ? mark : 0;
    }

    @Override
    public String toStringExtra() {
        return sampleAnswer;
    }
}