package pl_project;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Feedback {
    private int studentId;
    private String studentName;
    private String feedbackText;
    private String timestamp;
    private String category; // General, Technical, Suggestion, Complaint

    public Feedback(int studentId, String studentName, String feedbackText, String category) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.feedbackText = feedbackText;
        this.category = category;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public int getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getFeedbackText() { return feedbackText; }
    public String getCategory() { return category; }
    public String getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return studentId + "," + studentName + "," + feedbackText.replace(",", "|") + "," + category + "," + timestamp;
    }

    public static Feedback fromString(String line) {
        String[] data = line.split(",", 5);
        Feedback feedback = new Feedback(
            Integer.parseInt(data[0]),
            data[1],
            data[2].replace("|", ","),
            data[3]
        );
        if (data.length > 4) {
            feedback.timestamp = data[4];
        }
        return feedback;
    }
}

