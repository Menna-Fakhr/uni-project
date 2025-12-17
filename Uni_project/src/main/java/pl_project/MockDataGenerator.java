package pl_project;

import java.util.Arrays;

public class MockDataGenerator {
    
    public static void generateMockData(UserManager userManager, ExamManager examManager) {
        System.out.println("Generating mock data...");
        
        // Generate Users
        generateUsers(userManager);
        
        // Generate Courses
        generateCourses(examManager);
        
        // Generate Exams
        generateExams(examManager);
        
        // Generate Questions
        generateQuestions(examManager);
        
        // Generate Sample Student Answers
        generateStudentAnswers(examManager);
        
        System.out.println("Mock data generation completed!");
    }
    
    private static void generateUsers(UserManager userManager) {
        try {
            // Admin
            userManager.addUser(new Admin(1, "admin", "admin123", "admin@university.edu"));
            
            // Lecturers
            userManager.addUser(new Lecturer(2, "dr_smith", "lecturer123", "smith@university.edu"));
            userManager.addUser(new Lecturer(3, "prof_jones", "lecturer123", "jones@university.edu"));
            userManager.addUser(new Lecturer(4, "dr_williams", "lecturer123", "williams@university.edu"));
            
            // Students
            userManager.addUser(new Student(101, "john_doe", "student123", "john.doe@student.university.edu"));
            userManager.addUser(new Student(102, "jane_smith", "student123", "jane.smith@student.university.edu"));
            userManager.addUser(new Student(103, "bob_wilson", "student123", "bob.wilson@student.university.edu"));
            userManager.addUser(new Student(104, "alice_brown", "student123", "alice.brown@student.university.edu"));
            userManager.addUser(new Student(105, "charlie_davis", "student123", "charlie.davis@student.university.edu"));
            
            System.out.println("✓ Users created");
        } catch (Exception e) {
            System.out.println("Error creating users: " + e.getMessage());
        }
    }
    
    private static void generateCourses(ExamManager examManager) {
        try {
            examManager.createCourse(1, "Introduction to Computer Science");
            examManager.createCourse(2, "Data Structures and Algorithms");
            examManager.createCourse(3, "Database Systems");
            examManager.createCourse(4, "Software Engineering");
            examManager.createCourse(5, "Operating Systems");
            
            System.out.println("✓ Courses created");
        } catch (Exception e) {
            System.out.println("Error creating courses: " + e.getMessage());
        }
    }
    
    private static void generateExams(ExamManager examManager) {
        try {
            Course cs101 = examManager.findCourseById(1);
            Course ds201 = examManager.findCourseById(2);
            Course db301 = examManager.findCourseById(3);
            
            examManager.createExam(1, cs101, 60); // 60 minutes
            examManager.createExam(2, ds201, 90); // 90 minutes
            examManager.createExam(3, db301, 75); // 75 minutes
            
            System.out.println("✓ Exams created");
        } catch (Exception e) {
            System.out.println("Error creating exams: " + e.getMessage());
        }
    }
    
    private static void generateQuestions(ExamManager examManager) {
        try {
            // Exam 1: Introduction to Computer Science
            examManager.addQuestion(1, new MCQQuestion(1, 
                "What is the time complexity of binary search?", 
                10.0f, 
                Arrays.asList("O(n)", "O(log n)", "O(n log n)", "O(1)"), 
                1)); // Correct: O(log n)
            
            examManager.addQuestion(1, new TrueFalseQuestion(2, 
                "Java is a compiled language.", 
                5.0f, 
                true));
            
            examManager.addQuestion(1, new ShortAnswerQuestion(3, 
                "What does CPU stand for?", 
                10.0f, 
                "Central Processing Unit"));
            
            examManager.addQuestion(1, new MCQQuestion(4, 
                "Which data structure follows LIFO principle?", 
                10.0f, 
                Arrays.asList("Queue", "Stack", "Array", "LinkedList"), 
                1)); // Correct: Stack
            
            // Exam 2: Data Structures and Algorithms
            examManager.addQuestion(2, new MCQQuestion(5, 
                "What is the worst-case time complexity of Quick Sort?", 
                15.0f, 
                Arrays.asList("O(n)", "O(n log n)", "O(n²)", "O(log n)"), 
                2)); // Correct: O(n²)
            
            examManager.addQuestion(2, new TrueFalseQuestion(6, 
                "A binary tree can have more than two children per node.", 
                10.0f, 
                false));
            
            examManager.addQuestion(2, new ShortAnswerQuestion(7, 
                "What is the main advantage of using a hash table?", 
                15.0f, 
                "Fast average-case lookup time O(1)"));
            
            examManager.addQuestion(2, new MCQQuestion(8, 
                "Which algorithm is used to find the shortest path in a graph?", 
                15.0f, 
                Arrays.asList("BFS", "DFS", "Dijkstra", "All of the above"), 
                3)); // Correct: Dijkstra
            
            examManager.addQuestion(2, new MCQQuestion(9, 
                "What is the space complexity of merge sort?", 
                10.0f, 
                Arrays.asList("O(1)", "O(n)", "O(log n)", "O(n log n)"), 
                1)); // Correct: O(n)
            
            // Exam 3: Database Systems
            examManager.addQuestion(3, new MCQQuestion(10, 
                "What does ACID stand for in database transactions?", 
                20.0f, 
                Arrays.asList("Atomicity, Consistency, Isolation, Durability", 
                             "Access, Control, Integrity, Data", 
                             "Analysis, Creation, Implementation, Design", 
                             "Application, Code, Interface, Database"), 
                0)); // Correct: Atomicity, Consistency, Isolation, Durability
            
            examManager.addQuestion(3, new TrueFalseQuestion(11, 
                "SQL is a NoSQL database language.", 
                10.0f, 
                false));
            
            examManager.addQuestion(3, new ShortAnswerQuestion(12, 
                "What is the primary key in a database table?", 
                15.0f, 
                "A unique identifier for each row in a table"));
            
            examManager.addQuestion(3, new MCQQuestion(13, 
                "Which normal form eliminates transitive dependencies?", 
                15.0f, 
                Arrays.asList("1NF", "2NF", "3NF", "BCNF"), 
                2)); // Correct: 3NF
            
            System.out.println("✓ Questions created");
        } catch (Exception e) {
            System.out.println("Error creating questions: " + e.getMessage());
        }
    }
    
    private static void generateStudentAnswers(ExamManager examManager) {
        try {
            // Student 101 (john_doe) answers for Exam 1
            examManager.addStudentAnswer(new StudentAnswer(101, 1, 1, "1")); // Correct: O(log n)
            examManager.addStudentAnswer(new StudentAnswer(101, 1, 2, "true")); // Correct
            examManager.addStudentAnswer(new StudentAnswer(101, 1, 3, "Central Processing Unit")); // Correct
            examManager.addStudentAnswer(new StudentAnswer(101, 1, 4, "1")); // Correct: Stack
            
            // Student 102 (jane_smith) answers for Exam 1
            examManager.addStudentAnswer(new StudentAnswer(102, 1, 1, "0")); // Wrong: O(n)
            examManager.addStudentAnswer(new StudentAnswer(102, 1, 2, "true")); // Correct
            examManager.addStudentAnswer(new StudentAnswer(102, 1, 3, "CPU")); // Wrong (case sensitive)
            examManager.addStudentAnswer(new StudentAnswer(102, 1, 4, "1")); // Correct: Stack
            
            // Student 101 answers for Exam 2
            examManager.addStudentAnswer(new StudentAnswer(101, 2, 5, "2")); // Correct: O(n²)
            examManager.addStudentAnswer(new StudentAnswer(101, 2, 6, "false")); // Correct
            examManager.addStudentAnswer(new StudentAnswer(101, 2, 7, "Fast average-case lookup time O(1)")); // Correct
            examManager.addStudentAnswer(new StudentAnswer(101, 2, 8, "2")); // Wrong: DFS
            examManager.addStudentAnswer(new StudentAnswer(101, 2, 9, "1")); // Correct: O(n)
            
            // Student 103 (bob_wilson) answers for Exam 2
            examManager.addStudentAnswer(new StudentAnswer(103, 2, 5, "1")); // Wrong: O(n log n)
            examManager.addStudentAnswer(new StudentAnswer(103, 2, 6, "false")); // Correct
            examManager.addStudentAnswer(new StudentAnswer(103, 2, 7, "Fast lookup")); // Partial (might not match exactly)
            examManager.addStudentAnswer(new StudentAnswer(103, 2, 8, "3")); // Correct: Dijkstra
            examManager.addStudentAnswer(new StudentAnswer(103, 2, 9, "1")); // Correct: O(n)
            
            // Student 101 answers for Exam 3
            examManager.addStudentAnswer(new StudentAnswer(101, 3, 10, "0")); // Correct: ACID
            examManager.addStudentAnswer(new StudentAnswer(101, 3, 11, "false")); // Correct
            examManager.addStudentAnswer(new StudentAnswer(101, 3, 12, "A unique identifier for each row in a table")); // Correct
            examManager.addStudentAnswer(new StudentAnswer(101, 3, 13, "2")); // Correct: 3NF
            
            System.out.println("✓ Student answers created");
        } catch (Exception e) {
            System.out.println("Error creating student answers: " + e.getMessage());
        }
    }
}

