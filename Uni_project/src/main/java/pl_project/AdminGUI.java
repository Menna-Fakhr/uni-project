package pl_project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminGUI extends JFrame {
    private UserManager userManager;
    private ExamManager examManager;
    private JTextArea outputArea;

    public AdminGUI(UserManager userManager, ExamManager examManager) {
        this.userManager = userManager;
        this.examManager = examManager;
        setTitle("Admin Dashboard - User Management");
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
        JLabel titleLabel = new JLabel("Admin Dashboard");
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
        
        JButton btnAdd = createStyledButton("➕ Add User", new Color(46, 125, 50));
        JButton btnDelete = createStyledButton("🗑️ Delete User", new Color(198, 40, 40));
        JButton btnUpdate = createStyledButton("✏️ Update User", new Color(25, 118, 210));
        JButton btnSearch = createStyledButton("🔍 Search User", new Color(156, 39, 176));
        JButton btnList = createStyledButton("📋 List Users", new Color(255, 152, 0));

        buttons.add(btnAdd);
        buttons.add(btnDelete);
        buttons.add(btnUpdate);
        buttons.add(btnSearch);
        buttons.add(btnList);

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

        btnAdd.addActionListener(e -> addUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnSearch.addActionListener(e -> searchUser());
        btnList.addActionListener(e -> listUsers());

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(140, 40));
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

    private void addUser() {
        try {
            String typeStr = JOptionPane.showInputDialog(this, "Type (Lecturer/Student):", "Add User", JOptionPane.QUESTION_MESSAGE);
            if (typeStr == null) return;
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "ID:", "Add User", JOptionPane.QUESTION_MESSAGE));
            String username = JOptionPane.showInputDialog(this, "Username:", "Add User", JOptionPane.QUESTION_MESSAGE);
            String password = JOptionPane.showInputDialog(this, "Password:", "Add User", JOptionPane.QUESTION_MESSAGE);
            String email = JOptionPane.showInputDialog(this, "Email:", "Add User", JOptionPane.QUESTION_MESSAGE);
            User user;
            switch (typeStr.toLowerCase()) {
                case "lecturer":
                    user = new Lecturer(id, username, password, email);
                    break;
                case "student":
                    user = new Student(id, username, password, email);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Invalid type", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }
            userManager.addUser(user);
            outputArea.append("✓ User added: " + username + " (ID: " + id + ")\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "ID to delete:", "Delete User", JOptionPane.QUESTION_MESSAGE));
            userManager.deleteUser(id);
            outputArea.append("✓ Deleted user ID: " + id + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "ID to update:", "Update User", JOptionPane.QUESTION_MESSAGE));
            String newUsername = JOptionPane.showInputDialog(this, "New Username (empty to skip):", "Update User", JOptionPane.QUESTION_MESSAGE);
            String newEmail = JOptionPane.showInputDialog(this, "New Email (empty to skip):", "Update User", JOptionPane.QUESTION_MESSAGE);
            String newPassword = JOptionPane.showInputDialog(this, "New Password (empty to skip):", "Update User", JOptionPane.QUESTION_MESSAGE);
            userManager.updateUser(id, newUsername, newEmail, newPassword);
            outputArea.append("✓ Updated user ID: " + id + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchUser() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "ID to search:", "Search User", JOptionPane.QUESTION_MESSAGE));
            User user = userManager.findById(id);
            outputArea.append(user != null ? "✓ Found: " + user.toString() + "\n" : "✗ User not found\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listUsers() {
        outputArea.append("═══════════════════════════════════════\n");
        outputArea.append("All Users:\n");
        outputArea.append("═══════════════════════════════════════\n");
        for (User u : userManager.getAllUsers()) {
            outputArea.append("• " + u.toString() + "\n");
        }
        outputArea.append("═══════════════════════════════════════\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
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