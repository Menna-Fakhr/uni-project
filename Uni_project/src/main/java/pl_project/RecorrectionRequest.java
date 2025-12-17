package pl_project;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RecorrectionRequest {
    private int studentId;
    private String studentName;
    private int examId;
    private int questionId;
    private String reason;
    private String status; // Pending, Reviewed, Approved, Rejected
    private String timestamp;

    public RecorrectionRequest(int studentId, String studentName, int examId, int questionId, String reason) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.examId = examId;
        this.questionId = questionId;
        this.reason = reason;
        this.status = "Pending";
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public int getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public int getExamId() { return examId; }
    public int getQuestionId() { return questionId; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    public String getTimestamp() { return timestamp; }
    
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return studentId + "," + studentName + "," + examId + "," + questionId + "," + 
               reason.replace(",", "|") + "," + status + "," + timestamp;
    }

    public static RecorrectionRequest fromString(String line) {
        String[] data = line.split(",", 7);
        RecorrectionRequest request = new RecorrectionRequest(
            Integer.parseInt(data[0]),
            data[1],
            Integer.parseInt(data[2]),
            Integer.parseInt(data[3]),
            data[4].replace("|", ",")
        );
        if (data.length > 5) {
            request.status = data[5];
        }
        if (data.length > 6) {
            request.timestamp = data[6];
        }
        return request;
    }
}

