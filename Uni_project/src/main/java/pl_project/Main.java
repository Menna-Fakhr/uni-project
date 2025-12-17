package pl_project;

public class Main {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        ExamManager examManager = new ExamManager();
        
        // Uncomment the line below to generate mock data on startup
        // MockDataGenerator.generateMockData(userManager, examManager);
        
        new LoginGUI(userManager, examManager);
    }
}