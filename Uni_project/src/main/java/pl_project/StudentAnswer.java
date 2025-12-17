package pl_project;

public class StudentAnswer {
    private int studentId;
    private int examId;
    private int questionId;
    private String answer;

    public StudentAnswer(int studentId, int examId, int questionId, String answer) {
        this.studentId = studentId;
        this.examId = examId;
        this.questionId = questionId;
        this.answer = answer;
    }

    public int getStudentId() { return studentId; }
    public int getExamId() { return examId; }
    public int getQuestionId() { return questionId; }
    public String getAnswer() { return answer; }

    @Override
    public String toString() {
        return studentId + "," + examId + "," + questionId + "," + answer;
    }

    public static StudentAnswer fromString(String line) {
        String[] data = line.split(",");
        return new StudentAnswer(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), data[3]);
    }
}