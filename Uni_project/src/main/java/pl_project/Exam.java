package pl_project;

import java.util.ArrayList;
import java.util.List;

public class Exam {
    private int id;
    private Course course;
    private int duration;
    private List<Question> questions = new ArrayList<>();

    public Exam(int id, Course course, int duration) {
        this.id = id;
        this.course = course;
        this.duration = duration;
    }

    public int getId() { return id; }
    public Course getCourse() { return course; }
    public int getDuration() { return duration; }
    public List<Question> getQuestions() { return questions; }

    public void setCourse(Course course) { this.course = course; }
    public void setDuration(int duration) { this.duration = duration; }

    public void addQuestion(Question question) { questions.add(question); }
    public void removeQuestion(Question question) { questions.remove(question); }

    @Override
    public String toString() {
        return id + "," + course.getId() + "," + duration;
    }

    public static Exam fromString(String line, List<Course> courses) {
        String[] data = line.split(",");
        int courseId = Integer.parseInt(data[1]);
        Course course = courses.stream().filter(c -> c.getId() == courseId).findFirst().orElse(null);
        return new Exam(Integer.parseInt(data[0]), course, Integer.parseInt(data[2]));
    }
}