package pl_project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginGUI extends JFrame {
    private UserManager userManager;
    private ExamManager examManager;

    public LoginGUI(UserManager userManager, ExamManager examManager) {
        this.userManager = userManager;
        this.examManager = examManager;
        setTitle("University Exam System - Login");
        setSize(500, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main container with gradient-like background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("University Exam System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(52, 73, 94));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Form panel with card-like appearance
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(30, 40, 30, 40)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Username field
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsername.setForeground(new Color(52, 73, 94));
        formPanel.add(lblUsername, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        JTextField txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 12, 10, 12)
        ));
        formPanel.add(txtUsername, gbc);

        // Password field
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setForeground(new Color(52, 73, 94));
        formPanel.add(lblPassword, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        JPasswordField txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 12, 10, 12)
        ));
        formPanel.add(txtPassword, gbc);

        // Buttons panel
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(25, 10, 10, 10);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        JButton btnLogin = createStyledButton("Login", new Color(46, 125, 50), new Color(56, 142, 60));
        JButton btnRegister = createStyledButton("Register", new Color(25, 118, 210), new Color(30, 136, 229));
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                showStyledMessage("Fields cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            User user = userManager.login(username, password);
            if (user != null) {
                openDashboard(user);
                dispose();
            } else {
                showStyledMessage("Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRegister.addActionListener(e -> registerUser());

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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

    private void showStyledMessage(String message, String title, int type) {
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.PLAIN, 12));
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    private void registerUser() {
        try {
            String typeStr = JOptionPane.showInputDialog(this, "Type (Admin/Lecturer/Student):", "Registration", JOptionPane.QUESTION_MESSAGE);
            if (typeStr == null) return;
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "ID:", "Registration", JOptionPane.QUESTION_MESSAGE));
            String username = JOptionPane.showInputDialog(this, "Username:", "Registration", JOptionPane.QUESTION_MESSAGE);
            String password = JOptionPane.showInputDialog(this, "Password:", "Registration", JOptionPane.QUESTION_MESSAGE);
            String email = JOptionPane.showInputDialog(this, "Email:", "Registration", JOptionPane.QUESTION_MESSAGE);
            if (username == null || password == null || email == null || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                showStyledMessage("Fields cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            User user;
            switch (typeStr.toLowerCase()) {
                case "admin":
                    user = new Admin(id, username, password, email);
                    break;
                case "lecturer":
                    user = new Lecturer(id, username, password, email);
                    break;
                case "student":
                    user = new Student(id, username, password, email);
                    break;
                default:
                    showStyledMessage("Invalid type", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }
            userManager.addUser(user);
            showStyledMessage("Registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            showStyledMessage("Invalid ID", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            showStyledMessage(e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openDashboard(User user) {
        if (user instanceof Admin) {
            new AdminGUI(userManager, examManager);
        } else if (user instanceof Lecturer) {
            new LecturerGUI(examManager, userManager);
        } else if (user instanceof Student) {
            new StudentGUI((Student) user, examManager, userManager);
        }
    }
}