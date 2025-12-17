package pl_project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final String FILE = "data/users.txt";
    private List<User> users = new ArrayList<>();

    public UserManager() {
        ensureDataDirectory();
        load();
    }

    private void ensureDataDirectory() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    private void load() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    users.add(User.fromString(line));
                }
            }
        } catch (FileNotFoundException e) {
            // File created on first save
        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private void save() {
        ensureDataDirectory();
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (User user : users) {
                pw.println(user.toString());
            }
        } catch (Exception e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public void addUser(User user) {
        if (users.stream().anyMatch(u -> u.getId() == user.getId())) {
            throw new IllegalArgumentException("ID already exists");
        }
        users.add(user);
        save();
    }

    public void deleteUser(int id) {
        users.removeIf(u -> u.getId() == id);
        save();
    }

    public void updateUser(int id, String newUsername, String newEmail, String newPassword) {
        User user = findById(id);
        if (user != null) {
            if (newUsername != null && !newUsername.isEmpty()) user.setUsername(newUsername);
            if (newEmail != null && !newEmail.isEmpty()) user.setEmail(newEmail);
            if (newPassword != null && !newPassword.isEmpty()) user.setPassword(newPassword);
            save();
        }
    }

    public User findById(int id) {
        return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User login(String username, String password) {
        return users.stream().filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password)).findFirst().orElse(null);
    }
}