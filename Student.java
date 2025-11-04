
/**
 * Represents a Student user in the system.
 * Students can view internships, apply for them, and manage their applications.
 */
public class Student extends User {
    private int yearOfStudy;
    private String major;
    
    /**
     * Constructor for Student
     */
    public Student(String userID, String name, String password, int yearOfStudy, String major) {
        super(userID, name, password);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
    }
    
    // Getters
    public int getYearOfStudy() { 
        return yearOfStudy; 
    }
    
    public String getMajor() { 
        return major; 
    }
    
    // Setters
    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }
    
    public void setMajor(String major) {
        this.major = major;
    }
}
