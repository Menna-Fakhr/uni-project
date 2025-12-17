package pl_project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class StudentGUI extends JFrame {
    private Student student;
    private ExamManager examManager;
    private UserManager userManager;
    private JTextArea outputArea;

    public StudentGUI(Student student, ExamManager examManager, UserManager userManager) {
        this.student = student;
        this.examManager = examManager;
        this.userManager = userManager;
        setTitle("Student Dashboard - " + student.getUsername());
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
        JLabel titleLabel = new JLabel("Student Dashboard - " + student.getUsername());
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
        
        JButton btnViewExams = createStyledButton("📋 View Exams", new Color(25, 118, 210));
        JButton btnTakeExam = createStyledButton("✍️ Take Exam", new Color(46, 125, 50));
        JButton btnViewResults = createStyledButton("📊 View Results", new Color(255, 152, 0));
        JButton btnUpdateInfo = createStyledButton("⚙️ Update Info", new Color(156, 39, 176));

        buttons.add(btnViewExams);
        buttons.add(btnTakeExam);
        buttons.add(btnViewResults);
        buttons.add(btnUpdateInfo);

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

        btnViewExams.addActionListener(e -> viewExams());
        btnTakeExam.addActionListener(e -> takeExam());
        btnViewResults.addActionListener(e -> viewResults());
        btnUpdateInfo.addActionListener(e -> updateInfo());

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

    private void viewExams() {
        outputArea.append("═══════════════════════════════════════\n");
        outputArea.append("Available Exams:\n");
        outputArea.append("═══════════════════════════════════════\n");
        List<Exam> exams = examManager.getAvailableExamsForStudent(student.getId());
        if (exams.isEmpty()) {
            outputArea.append("No exams available.\n");
        } else {
            for (Exam exam : exams) {
                outputArea.append("• Exam ID: " + exam.getId() + " | Course: " + exam.getCourse().getName() + " | Duration: " + exam.getDuration() + " min\n");
            }
        }
        outputArea.append("═══════════════════════════════════════\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    private void takeExam() {
        try {
            int examId = Integer.parseInt(JOptionPane.showInputDialog(this, "Exam ID:", "Take Exam", JOptionPane.QUESTION_MESSAGE));
            Exam exam = examManager.findExamById(examId);
            if (exam == null) {
                JOptionPane.showMessageDialog(this, "Exam not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (Question q : exam.getQuestions()) {
                String prompt = q.getText() + "\n";
                if (q instanceof MCQQuestion) {
                    prompt += "Options: " + String.join(", ", ((MCQQuestion) q).getOptions()) + "\nEnter index (0-based):";
                } else if (q instanceof TrueFalseQuestion) {
                    prompt += "Enter true/false:";
                } else {
                    prompt += "Enter answer:";
                }
                String ans = JOptionPane.showInputDialog(this, prompt, "Question " + q.getId(), JOptionPane.QUESTION_MESSAGE);
                if (ans == null) return;
                examManager.addStudentAnswer(new StudentAnswer(student.getId(), examId, q.getId(), ans));
            }
            JOptionPane.showMessageDialog(this, "Exam submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            outputArea.append("✓ Exam " + examId + " submitted successfully\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewResults() {
        try {
            int examId = Integer.parseInt(JOptionPane.showInputDialog(this, "Exam ID:", "View Results", JOptionPane.QUESTION_MESSAGE));
            float grade = examManager.gradeExam(examId, student.getId());
            outputArea.append("═══════════════════════════════════════\n");
            outputArea.append("Grade for Exam " + examId + ": " + grade + " points\n");
            outputArea.append("═══════════════════════════════════════\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateInfo() {
        try {
            String newUsername = JOptionPane.showInputDialog(this, "New Username (empty to skip):", "Update Info", JOptionPane.QUESTION_MESSAGE);
            String newEmail = JOptionPane.showInputDialog(this, "New Email (empty to skip):", "Update Info", JOptionPane.QUESTION_MESSAGE);
            String newPassword = JOptionPane.showInputDialog(this, "New Password (empty to skip):", "Update Info", JOptionPane.QUESTION_MESSAGE);
            userManager.updateUser(student.getId(), newUsername, newEmail, newPassword);
            outputArea.append("✓ Information updated successfully\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
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
            new LoginGUI(userManager, examManager);
        }
    }
}