package pl_project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExamManager {
    private static final String COURSES_FILE = "data/courses.txt";
    private static final String EXAMS_FILE = "data/exams.txt";
    private static final String QUESTIONS_FILE = "data/questions.txt";
    private static final String ANSWERS_FILE = "data/answers.txt";
    private static final String FEEDBACK_FILE = "data/feedback.txt";
    private static final String RECORRECTION_FILE = "data/recorrections.txt";

    private List<Course> courses = new ArrayList<>();
    private List<Exam> exams = new ArrayList<>();
    private List<Question> questions = new ArrayList<>(); // All questions across exams
    private List<StudentAnswer> answers = new ArrayList<>();
    private List<Feedback> feedbacks = new ArrayList<>();
    private List<RecorrectionRequest> recorrectionRequests = new ArrayList<>();

    public ExamManager() {
        ensureDataDirectory();
        loadAll();
    }

    private void ensureDataDirectory() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    private void loadAll() {
        loadCourses();
        loadExams();
        loadQuestions();
        loadAnswers();
        loadFeedbacks();
        loadRecorrections();
    }

    private void loadCourses() {
        try (BufferedReader br = new BufferedReader(new FileReader(COURSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    courses.add(Course.fromString(line));
                }
            }
        } catch (FileNotFoundException e) {
            // OK
        } catch (Exception e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
    }

    private void loadExams() {
        try (BufferedReader br = new BufferedReader(new FileReader(EXAMS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    exams.add(Exam.fromString(line, courses));
                }
            }
        } catch (FileNotFoundException e) {
            // OK
        } catch (Exception e) {
            System.out.println("Error loading exams: " + e.getMessage());
        }
    }

    private void loadQuestions() {
        try (BufferedReader br = new BufferedReader(new FileReader(QUESTIONS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",", 2);
                    if (parts.length == 2) {
                        int examId = Integer.parseInt(parts[0]);
                        Question q = Question.fromString(parts[1]);
                        questions.add(q);
                        Exam exam = findExamById(examId);
                        if (exam != null) {
                            exam.addQuestion(q);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // OK
        } catch (Exception e) {
            System.out.println("Error loading questions: " + e.getMessage());
        }
    }

    private void loadAnswers() {
        try (BufferedReader br = new BufferedReader(new FileReader(ANSWERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    answers.add(StudentAnswer.fromString(line));
                }
            }
        } catch (FileNotFoundException e) {
            // OK
        } catch (Exception e) {
            System.out.println("Error loading answers: " + e.getMessage());
        }
    }

    private void loadFeedbacks() {
        try (BufferedReader br = new BufferedReader(new FileReader(FEEDBACK_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    feedbacks.add(Feedback.fromString(line));
                }
            }
        } catch (FileNotFoundException e) {
            // OK
        } catch (Exception e) {
            System.out.println("Error loading feedbacks: " + e.getMessage());
        }
    }

    private void loadRecorrections() {
        try (BufferedReader br = new BufferedReader(new FileReader(RECORRECTION_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    recorrectionRequests.add(RecorrectionRequest.fromString(line));
                }
            }
        } catch (FileNotFoundException e) {
            // OK
        } catch (Exception e) {
            System.out.println("Error loading recorrections: " + e.getMessage());
        }
    }

    private void saveAll() {
        saveCourses();
        saveExams();
        saveQuestions();
        saveAnswers();
    }

    private void saveCourses() {
        ensureDataDirectory();
        try (PrintWriter pw = new PrintWriter(new FileWriter(COURSES_FILE))) {
            for (Course c : courses) {
                pw.println(c.toString());
            }
        } catch (Exception e) {
            System.out.println("Error saving courses: " + e.getMessage());
        }
    }

    private void saveExams() {
        ensureDataDirectory();
        try (PrintWriter pw = new PrintWriter(new FileWriter(EXAMS_FILE))) {
            for (Exam e : exams) {
                pw.println(e.toString());
            }
        } catch (Exception e) {
            System.out.println("Error saving exams: " + e.getMessage());
        }
    }

    private void saveQuestions() {
        ensureDataDirectory();
        try (PrintWriter pw = new PrintWriter(new FileWriter(QUESTIONS_FILE))) {
            for (Exam e : exams) {
                for (Question q : e.getQuestions()) {
                    pw.println(e.getId() + "," + q.toString());
                }
            }
        } catch (Exception e) {
            System.out.println("Error saving questions: " + e.getMessage());
        }
    }

    private void saveAnswers() {
        ensureDataDirectory();
        try (PrintWriter pw = new PrintWriter(new FileWriter(ANSWERS_FILE))) {
            for (StudentAnswer a : answers) {
                pw.println(a.toString());
            }
        } catch (Exception e) {
            System.out.println("Error saving answers: " + e.getMessage());
        }
    }

    private void saveFeedbacks() {
        ensureDataDirectory();
        try (PrintWriter pw = new PrintWriter(new FileWriter(FEEDBACK_FILE))) {
            for (Feedback f : feedbacks) {
                pw.println(f.toString());
            }
        } catch (Exception e) {
            System.out.println("Error saving feedbacks: " + e.getMessage());
        }
    }

    private void saveRecorrections() {
        ensureDataDirectory();
        try (PrintWriter pw = new PrintWriter(new FileWriter(RECORRECTION_FILE))) {
            for (RecorrectionRequest r : recorrectionRequests) {
                pw.println(r.toString());
            }
        } catch (Exception e) {
            System.out.println("Error saving recorrections: " + e.getMessage());
        }
    }

    public void updateRecorrectionStatus(RecorrectionRequest request) {
        saveRecorrections();
    }

    public Course createCourse(int id, String name) {
        if (courses.stream().anyMatch(c -> c.getId() == id)) {
            throw new IllegalArgumentException("Course ID exists");
        }
        Course course = new Course(id, name);
        courses.add(course);
        saveCourses();
        return course;
    }

    public Exam createExam(int id, Course course, int duration) {
        if (exams.stream().anyMatch(e -> e.getId() == id)) {
            throw new IllegalArgumentException("Exam ID exists");
        }
        Exam exam = new Exam(id, course, duration);
        exams.add(exam);
        saveExams();
        return exam;
    }

    public void updateExam(int id, Course newCourse, Integer newDuration) {
        Exam exam = findExamById(id);
        if (exam != null) {
            if (newCourse != null) exam.setCourse(newCourse);
            if (newDuration != null) exam.setDuration(newDuration);
            saveExams();
        }
    }

    public void addQuestion(int examId, Question question) {
        Exam exam = findExamById(examId);
        if (exam != null) {
            exam.addQuestion(question);
            questions.add(question);
            saveQuestions();
        }
    }

    public void addStudentAnswer(StudentAnswer answer) {
        answers.add(answer);
        saveAnswers();
    }

    public List<Exam> getAvailableExamsForStudent(int studentId) {
        // For simplicity, all exams are available to all students
        return new ArrayList<>(exams);
    }

    public float gradeExam(int examId, int studentId) {
        Exam exam = findExamById(examId);
        if (exam == null) return 0;
        List<StudentAnswer> studentAnswers = answers.stream()
                .filter(a -> a.getStudentId() == studentId && a.getExamId() == examId)
                .collect(Collectors.toList());
        float total = 0;
        for (StudentAnswer sa : studentAnswers) {
            Question q = exam.getQuestions().stream()
                    .filter(qq -> qq.getId() == sa.getQuestionId())
                    .findFirst()
                    .orElse(null);
            if (q != null) {
                total += q.grade(sa);
            }
        }
        return total;
    }

    public String generateGradeReport(int examId) {
        Exam exam = findExamById(examId);
        if (exam == null) return "Exam not found";
        StringBuilder sb = new StringBuilder("Grade Report for Exam " + examId + "\n");
        List<Integer> studentIds = answers.stream()
                .filter(a -> a.getExamId() == examId)
                .map(StudentAnswer::getStudentId)
                .distinct()
                .collect(Collectors.toList());
        for (int sid : studentIds) {
            float grade = gradeExam(examId, sid);
            sb.append("Student ").append(sid).append(": ").append(grade).append("\n");
        }
        return sb.toString();
    }

    public String generateStudentReport(int examId, int studentId) {
        Exam exam = findExamById(examId);
        if (exam == null) return "Exam not found";
        float grade = gradeExam(examId, studentId);
        StringBuilder sb = new StringBuilder("Report for Student " + studentId + " in Exam " + examId + "\n");
        sb.append("Total Grade: ").append(grade).append("\n");
        return sb.toString();
    }

    public Exam findExamById(int id) {
        return exams.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    public Course findCourseById(int id) {
        return courses.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public void addFeedback(Feedback feedback) {
        feedbacks.add(feedback);
        saveFeedbacks();
    }

    public void addRecorrectionRequest(RecorrectionRequest request) {
        recorrectionRequests.add(request);
        saveRecorrections();
    }

    public List<RecorrectionRequest> getRecorrectionRequestsByStudent(int studentId) {
        return recorrectionRequests.stream()
            .filter(r -> r.getStudentId() == studentId)
            .collect(Collectors.toList());
    }

    public List<Feedback> getAllFeedbacks() {
        return new ArrayList<>(feedbacks);
    }

    public List<RecorrectionRequest> getAllRecorrectionRequests() {
        return new ArrayList<>(recorrectionRequests);
    }
}