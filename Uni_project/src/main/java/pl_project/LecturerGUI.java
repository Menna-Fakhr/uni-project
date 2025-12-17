package pl_project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class LecturerGUI extends JFrame {
    private ExamManager examManager;
    private UserManager userManager;
    private JTextArea outputArea;

    public LecturerGUI(ExamManager examManager, UserManager userManager) {
        this.examManager = examManager;
        this.userManager = userManager;
        setTitle("Lecturer Dashboard - Exam Management");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Lecturer Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Logout button in header
        JButton btnLogout = createStyledButton("🚪 Logout", new Color(198, 40, 40));
        btnLogout.addActionListener(e -> logout());
        headerPanel.add(btnLogout, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Buttons panel with modern styling
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JButton btnCreateCourse = createStyledButton("📚 Create Course", new Color(46, 125, 50));
        JButton btnCreateExam = createStyledButton("📝 Create Exam", new Color(25, 118, 210));
        JButton btnAddQuestion = createStyledButton("❓ Add Question", new Color(156, 39, 176));
        JButton btnGradeReport = createStyledButton("📊 Grade Report", new Color(255, 152, 0));
        JButton btnStudentReport = createStyledButton("👤 Student Report", new Color(0, 150, 136));

        buttons.add(btnCreateCourse);
        buttons.add(btnCreateExam);
        buttons.add(btnAddQuestion);
        buttons.add(btnGradeReport);
        buttons.add(btnStudentReport);

        // Output area with modern styling
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setBackground(new Color(255, 255, 255));
        outputArea.setForeground(new Color(33, 33, 33));
        outputArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(5, 5, 5, 5)
        ));
        scroll.setBackground(Color.WHITE);
        scroll.getViewport().setBackground(Color.WHITE);

        // Create a container for buttons and scroll
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.add(buttons, BorderLayout.NORTH);
        contentPanel.add(scroll, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);

        btnCreateCourse.addActionListener(e -> createCourse());
        btnCreateExam.addActionListener(e -> createExam());
        btnAddQuestion.addActionListener(e -> addQuestion());
        btnGradeReport.addActionListener(e -> gradeReport());
        btnStudentReport.addActionListener(e -> studentReport());

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        Color hoverColor = bgColor.darker();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void createCourse() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Course ID:", "Create Course", JOptionPane.QUESTION_MESSAGE));
            String name = JOptionPane.showInputDialog(this, "Course Name:", "Create Course", JOptionPane.QUESTION_MESSAGE);
            examManager.createCourse(id, name);
            outputArea.append("✓ Created course: " + name + " (ID: " + id + ")\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createExam() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Exam ID:", "Create Exam", JOptionPane.QUESTION_MESSAGE));
            int courseId = Integer.parseInt(JOptionPane.showInputDialog(this, "Course ID:", "Create Exam", JOptionPane.QUESTION_MESSAGE));
            int duration = Integer.parseInt(JOptionPane.showInputDialog(this, "Duration (min):", "Create Exam", JOptionPane.QUESTION_MESSAGE));
            Course course = examManager.findCourseById(courseId);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            examManager.createExam(id, course, duration);
            outputArea.append("✓ Created exam " + id + " for course: " + course.getName() + " (Duration: " + duration + " min)\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addQuestion() {
        try {
            int examId = Integer.parseInt(JOptionPane.showInputDialog(this, "Exam ID:", "Add Question", JOptionPane.QUESTION_MESSAGE));
            if (examManager.findExamById(examId) == null) {
                JOptionPane.showMessageDialog(this, "Exam not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int qId = Integer.parseInt(JOptionPane.showInputDialog(this, "Question ID:", "Add Question", JOptionPane.QUESTION_MESSAGE));
            String text = JOptionPane.showInputDialog(this, "Question Text:", "Add Question", JOptionPane.QUESTION_MESSAGE);
            float mark = Float.parseFloat(JOptionPane.showInputDialog(this, "Mark:", "Add Question", JOptionPane.QUESTION_MESSAGE));
            String type = JOptionPane.showInputDialog(this, "Type (MCQ/TF/SA):", "Add Question", JOptionPane.QUESTION_MESSAGE);
            Question q;
            switch (type.toUpperCase()) {
                case "MCQ":
                    String optsStr = JOptionPane.showInputDialog(this, "Options (A:B:C:D):", "Add Question", JOptionPane.QUESTION_MESSAGE);
                    int correct = Integer.parseInt(JOptionPane.showInputDialog(this, "Correct index (0-based):", "Add Question", JOptionPane.QUESTION_MESSAGE));
                    q = new MCQQuestion(qId, text, mark, Arrays.asList(optsStr.split(":")), correct);
                    break;
                case "TF":
                    boolean correctTF = JOptionPane.showConfirmDialog(this, "Is True?", "True/False Question", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                    q = new TrueFalseQuestion(qId, text, mark, correctTF);
                    break;
                case "SA":
                    String sample = JOptionPane.showInputDialog(this, "Sample Answer:", "Add Question", JOptionPane.QUESTION_MESSAGE);
                    q = new ShortAnswerQuestion(qId, text, mark, sample);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Invalid type", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }
            examManager.addQuestion(examId, q);
            outputArea.append("✓ Added question (ID: " + qId + ") to exam " + examId + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gradeReport() {
        try {
            int examId = Integer.parseInt(JOptionPane.showInputDialog(this, "Exam ID:", "Grade Report", JOptionPane.QUESTION_MESSAGE));
            String report = examManager.generateGradeReport(examId);
            outputArea.append("═══════════════════════════════════════\n");
            outputArea.append(report);
            outputArea.append("═══════════════════════════════════════\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void studentReport() {
        try {
            int examId = Integer.parseInt(JOptionPane.showInputDialog(this, "Exam ID:", "Student Report", JOptionPane.QUESTION_MESSAGE));
            int studentId = Integer.parseInt(JOptionPane.showInputDialog(this, "Student ID:", "Student Report", JOptionPane.QUESTION_MESSAGE));
            String report = examManager.generateStudentReport(examId, studentId);
            outputArea.append("═══════════════════════════════════════\n");
            outputArea.append(report);
            outputArea.append("═══════════════════════════════════════\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Logout", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            // Create new managers for login screen
            UserManager um = new UserManager();
            ExamManager em = new ExamManager();
            new LoginGUI(um, em);
        }
    }
}