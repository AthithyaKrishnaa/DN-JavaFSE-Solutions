// Exercise 10: Implementing the MVC Pattern
// Scenario: A simple web application for managing student records using
// the MVC (Model-View-Controller) pattern.

// Model
class Student {
    private String name;
    private String id;
    private String grade;

    public Student(String name, String id, String grade) {
        this.name = name;
        this.id = id;
        this.grade = grade;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}

// View
class StudentView {
    public void displayStudentDetails(String name, String id, String grade) {
        System.out.println("Student Details:");
        System.out.println("  ID    : " + id);
        System.out.println("  Name  : " + name);
        System.out.println("  Grade : " + grade);
    }
}

// Controller
class StudentController {
    private Student student;
    private StudentView view;

    public StudentController(Student student, StudentView view) {
        this.student = student;
        this.view = view;
    }

    public void setStudentName(String name) {
        student.setName(name);
    }

    public void setStudentId(String id) {
        student.setId(id);
    }

    public void setStudentGrade(String grade) {
        student.setGrade(grade);
    }

    public void updateView() {
        view.displayStudentDetails(student.getName(), student.getId(), student.getGrade());
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println("=== MVC PATTERN: STUDENT RECORD MANAGEMENT ===");

        Student student = new Student("Aarav Sharma", "S101", "B+");
        StudentView view = new StudentView();
        StudentController controller = new StudentController(student, view);

        System.out.println("\nInitial Student Record:");
        controller.updateView();

        System.out.println("\nUpdating student's grade via controller...");
        controller.setStudentGrade("A");
        controller.setStudentName("Aarav R. Sharma");

        System.out.println("\nUpdated Student Record:");
        controller.updateView();

        System.out.println("\n=== CONCLUSION ===");
        System.out.println("The Controller mediates between the Model (Student) and");
        System.out.println("the View (StudentView), keeping concerns separated.");
    }
}
