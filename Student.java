
/**
 * Represents a student user in the Internship Placement Management System.
 * <p>
 * Students can perform the following operations:
 * <ul>
 *   <li>View available internship opportunities filtered by eligibility rules</li>
 *   <li>Apply for up to 3 internships simultaneously</li>
 *   <li>View and manage their internship applications</li>
 *   <li>Accept successful internship offers</li>
 *   <li>Request withdrawal from accepted internships</li>
 * </ul>
 * <p>
 * Students have restrictions based on their year of study:
 * <ul>
 *   <li>Year 1-2 students can only apply for "Basic" level internships</li>
 *   <li>Year 3-4 students can apply for all levels (Basic, Intermediate, Advanced)</li>
 * </ul>
 * Additionally, students can only view internships matching their major.
 *
 * @see User
 * @see Application
 * @see InternshipOpportunity
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class Student extends User {
    private int yearOfStudy;
    private String major;
    
    /**
     * Constructs a new Student with the specified details.
     *
     * @param userID      the unique identifier for this student (e.g., "U2310001A")
     * @param name        the student's full name
     * @param password    the authentication password
     * @param yearOfStudy the current year of study (1-4)
     * @param major       the student's major field of study (e.g., "Computer Science")
     */
    public Student(String userID, String name, String password, int yearOfStudy, String major) {
        super(userID, name, password);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
    }
    
    /**
     * Retrieves the student's current year of study.
     *
     * @return the year of study (1-4)
     */
    public int getYearOfStudy() { 
        return yearOfStudy; 
    }
    
    /**
     * Retrieves the student's major field of study.
     *
     * @return the major (e.g., "Computer Science", "Data Science &amp; AI")
     */
    public String getMajor() { 
        return major; 
    }
    
    /**
     * Updates the student's year of study.
     *
     * @param yearOfStudy the new year of study (1-4)
     */
    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }
    
    /**
     * Updates the student's major field of study.
     *
     * @param major the new major
     */
    public void setMajor(String major) {
        this.major = major;
    }
}
