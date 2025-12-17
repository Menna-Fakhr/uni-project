package pl_project;

public class Course {
    private int id;
    private String name;

    public Course(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return id + "," + name;
    }

    public static Course fromString(String line) {
        String[] data = line.split(",");
        return new Course(Integer.parseInt(data[0]), data[1]);
    }
}