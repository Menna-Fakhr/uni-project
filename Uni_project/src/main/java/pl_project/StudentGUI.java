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
        setSize(900, 650);
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

        // Buttons panel with modern styling - using GridLayout for 2 rows
        JPanel buttons = new JPanel(new GridLayout(2, 3, 10, 10));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JButton btnViewExams = createStyledButton("📋 View Exams", new Color(25, 118, 210));
        JButton btnTakeExam = createStyledButton("✍️ Take Exam", new Color(46, 125, 50));
        JButton btnViewResults = createStyledButton("📊 View Results", new Color(255, 152, 0));
        JButton btnSendFeedback = createStyledButton("💬 Send Feedback", new Color(0, 150, 136));
        JButton btnRequestRecorrection = createStyledButton("🔄 Request Recorrection", new Color(244, 67, 54));
        JButton btnUpdateInfo = createStyledButton("⚙️ Update Info", new Color(156, 39, 176));

        buttons.add(btnViewExams);
        buttons.add(btnTakeExam);
        buttons.add(btnViewResults);
        buttons.add(btnSendFeedback);
        buttons.add(btnRequestRecorrection);
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
        btnSendFeedback.addActionListener(e -> sendFeedback());
        btnRequestRecorrection.addActionListener(e -> requestRecorrection());
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

    private void sendFeedback() {
        // Create feedback dialog
        JDialog feedbackDialog = new JDialog(this, "Send Feedback", true);
        feedbackDialog.setSize(500, 400);
        feedbackDialog.setLocationRelativeTo(this);
        feedbackDialog.setLayout(new BorderLayout(10, 10));
        feedbackDialog.getContentPane().setBackground(new Color(245, 247, 250));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Category selection
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.setOpaque(false);
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        String[] categories = {"General", "Technical", "Suggestion", "Complaint"};
        JComboBox<String> categoryCombo = new JComboBox<>(categories);
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryCombo);

        // Feedback text area
        JLabel feedbackLabel = new JLabel("Your Feedback:");
        feedbackLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextArea feedbackText = new JTextArea(10, 30);
        feedbackText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        feedbackText.setLineWrap(true);
        feedbackText.setWrapStyleWord(true);
        feedbackText.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane scrollPane = new JScrollPane(feedbackText);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        JButton btnSubmit = createStyledButton("Submit", new Color(46, 125, 50));
        JButton btnCancel = createStyledButton("Cancel", new Color(158, 158, 158));

        btnSubmit.addActionListener(e -> {
            String feedback = feedbackText.getText().trim();
            if (feedback.isEmpty()) {
                JOptionPane.showMessageDialog(feedbackDialog, "Please enter your feedback", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String category = (String) categoryCombo.getSelectedItem();
            Feedback feedbackObj = new Feedback(student.getId(), student.getUsername(), feedback, category);
            examManager.addFeedback(feedbackObj);
            JOptionPane.showMessageDialog(feedbackDialog, "Feedback submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            outputArea.append("✓ Feedback submitted: " + category + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
            feedbackDialog.dispose();
        });

        btnCancel.addActionListener(e -> feedbackDialog.dispose());

        buttonPanel.add(btnSubmit);
        buttonPanel.add(btnCancel);

        mainPanel.add(categoryPanel, BorderLayout.NORTH);
        mainPanel.add(feedbackLabel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        feedbackDialog.add(mainPanel);
        feedbackDialog.setVisible(true);
    }

    private void requestRecorrection() {
        try {
            // Get exam ID
            String examIdStr = JOptionPane.showInputDialog(this, "Enter Exam ID:", "Request Recorrection", JOptionPane.QUESTION_MESSAGE);
            if (examIdStr == null || examIdStr.trim().isEmpty()) return;
            int examId = Integer.parseInt(examIdStr);
            
            Exam exam = examManager.findExamById(examId);
            if (exam == null) {
                JOptionPane.showMessageDialog(this, "Exam not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Ask if they want to request for entire exam or specific question
            int option = JOptionPane.showOptionDialog(this,
                "Do you want to request recorrection for the entire exam or a specific question?",
                "Recorrection Request",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Entire Exam", "Specific Question", "Cancel"},
                "Entire Exam");

            final int questionId; // 0 means entire exam
            String questionInfo;
            
            if (option == 1) { // Specific Question
                String questionIdStr = JOptionPane.showInputDialog(this, "Enter Question ID (or 0 for entire exam):", "Request Recorrection", JOptionPane.QUESTION_MESSAGE);
                if (questionIdStr == null || questionIdStr.trim().isEmpty()) return;
                int tempQuestionId = Integer.parseInt(questionIdStr);
                
                if (tempQuestionId != 0) {
                    Question question = exam.getQuestions().stream()
                        .filter(q -> q.getId() == tempQuestionId)
                        .findFirst()
                        .orElse(null);
                    
                    if (question == null) {
                        JOptionPane.showMessageDialog(this, "Question not found in this exam", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    questionId = tempQuestionId;
                    questionInfo = "Question " + questionId + ": " + question.getText();
                } else {
                    questionId = 0;
                    questionInfo = "Entire Exam";
                }
            } else if (option == JOptionPane.CLOSED_OPTION || option == 2) {
                return; // User cancelled
            } else {
                questionId = 0;
                questionInfo = "Entire Exam";
            }

            // Create recorrection dialog
            JDialog recorrectionDialog = new JDialog(this, "Request Exam Recorrection", true);
            recorrectionDialog.setSize(550, 400);
            recorrectionDialog.setLocationRelativeTo(this);
            recorrectionDialog.setLayout(new BorderLayout(10, 10));
            recorrectionDialog.getContentPane().setBackground(new Color(245, 247, 250));

            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            mainPanel.setBackground(Color.WHITE);

            // Exam and question info
            JLabel infoLabel = new JLabel("<html><b>Student ID:</b> " + student.getId() + " (" + student.getUsername() + ")<br>" +
                                         "<b>Exam ID:</b> " + examId + " - " + exam.getCourse().getName() + "<br>" +
                                         "<b>Request For:</b> " + questionInfo + "</html>");
            infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            infoLabel.setBorder(new EmptyBorder(0, 0, 15, 0));

            // Reason text area
            JLabel reasonLabel = new JLabel("Reason for Recorrection Request:");
            reasonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            JTextArea reasonText = new JTextArea(8, 35);
            reasonText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            reasonText.setLineWrap(true);
            reasonText.setWrapStyleWord(true);
            reasonText.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(10, 10, 10, 10)
            ));
            JScrollPane scrollPane = new JScrollPane(reasonText);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            buttonPanel.setOpaque(false);
            JButton btnSubmit = createStyledButton("Submit Request", new Color(244, 67, 54));
            JButton btnCancel = createStyledButton("Cancel", new Color(158, 158, 158));

            btnSubmit.addActionListener(e -> {
                String reason = reasonText.getText().trim();
                if (reason.isEmpty()) {
                    JOptionPane.showMessageDialog(recorrectionDialog, "Please provide a reason for recorrection", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                RecorrectionRequest request = new RecorrectionRequest(
                    student.getId(), 
                    student.getUsername(), 
                    examId, 
                    questionId, 
                    reason
                );
                examManager.addRecorrectionRequest(request);
                JOptionPane.showMessageDialog(recorrectionDialog, "Recorrection request submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                outputArea.append("✓ Recorrection request submitted for Exam " + examId + " (Student: " + student.getId() + ")\n");
                outputArea.setCaretPosition(outputArea.getDocument().getLength());
                recorrectionDialog.dispose();
            });

            btnCancel.addActionListener(e -> recorrectionDialog.dispose());

            buttonPanel.add(btnSubmit);
            buttonPanel.add(btnCancel);

            mainPanel.add(infoLabel, BorderLayout.NORTH);
            mainPanel.add(reasonLabel, BorderLayout.CENTER);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            recorrectionDialog.add(mainPanel);
            recorrectionDialog.setVisible(true);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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