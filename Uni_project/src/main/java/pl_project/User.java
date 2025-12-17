package pl_project;

import java.io.Serializable;

public abstract class User implements Serializable {
    protected int id;
    protected String username;
    protected String password;
    protected String email;

    public User(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "," + id + "," + username + "," + password + "," + email;
    }

    public static User fromString(String line) {
        String[] data = line.split(",");
        int id = Integer.parseInt(data[1]);
        String username = data[2];
        String password = data[3];
        String email = data[4];
        switch (data[0]) {
            case "Admin":
                return new Admin(id, username, password, email);
            case "Lecturer":
                return new Lecturer(id, username, password, email);
            case "Student":
                return new Student(id, username, password, email);
            default:
                throw new IllegalArgumentException("Unknown user type");
        }
    }
}