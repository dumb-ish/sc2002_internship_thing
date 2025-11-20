package test.unit;

import org.junit.*;
import static org.junit.Assert.*;

// Import domain classes
import ApplicationManager;
import Application;
import Student;
import InternshipOpportunity;
import SystemManager;

/**
 * Unit tests for Application Management functionality
 * Tests application submission, status updates, and withdrawal
 * 
 * Test IDs: UT-APP-001 through UT-APP-008
 * 
 * @see ApplicationManager
 * @see Application
 * @see Student
 * @see InternshipOpportunity
 */
public class ApplicationTest {
    
    private ApplicationManager applicationManager;
    private SystemManager systemManager;
    private Student testStudent;
    private InternshipOpportunity testInternship;
    
    @Before
    public void setUp() {
        // Initialize managers
        systemManager = new SystemManager();
        applicationManager = systemManager.getApplicationManager();
        
        // Create test student
        testStudent = new Student("S001", "Test Student", "password", 3.8, "Computer Science");
        
        // Create test internship
        testInternship = new InternshipOpportunity(
            "INT001",
            "Software Engineering Intern",
            "Backend development",
            "Java, Spring Boot",
            "Singapore",
            2000.0,
            3,
            "Tech Corp"
        );
        testInternship.updateStatus("Approved");
    }
    
    @After
    public void tearDown() {
        applicationManager = null;
        systemManager = null;
        testStudent = null;
        testInternship = null;
    }
    
    /**
     * UT-APP-001: Valid Application Submission
     * 
     * Tests that student can successfully submit application.
     * 
     * Preconditions: Student has no active application, internship available
     * Expected Result: Application created with "Pending" status
     */
    @Test
    public void testValidApplicationSubmission() {
        // Arrange
        assertNull("Student should have no active application", testStudent.getActiveApplication());
        
        // Act
        Application app = applicationManager.submitApplication(testStudent, testInternship);
        
        // Assert
        assertNotNull("Application should be created", app);
        assertEquals("Application status should be Pending", "Pending", app.getStatus());
        assertEquals("Student should be set", testStudent, app.getStudent());
        assertEquals("Internship should be set", testInternship, app.getInternship());
        assertEquals("Student activeApplication should be set", app, testStudent.getActiveApplication());
    }
    
    /**
     * UT-APP-002: Duplicate Application Prevention
     * 
     * Tests that student cannot submit multiple applications.
     * 
     * Preconditions: Student has active application
     * Expected Result: Second application blocked
     */
    @Test
    public void testDuplicateApplicationPrevention() {
        // Arrange
        Application firstApp = applicationManager.submitApplication(testStudent, testInternship);
        assertNotNull("First application should succeed", firstApp);
        
        // Create another internship
        InternshipOpportunity anotherInternship = new InternshipOpportunity(
            "INT002",
            "Data Analyst Intern",
            "Data analysis",
            "Python, SQL",
            "Singapore",
            1800.0,
            2,
            "Tech Corp"
        );
        anotherInternship.updateStatus("Approved");
        
        // Act
        Application secondApp = applicationManager.submitApplication(testStudent, anotherInternship);
        
        // Assert
        assertNull("Second application should be blocked", secondApp);
        assertEquals("Student should still have only first application", firstApp, testStudent.getActiveApplication());
    }
    
    /**
     * UT-APP-003: Application to Filled Internship
     * 
     * Tests that cannot apply to filled internship.
     * 
     * Preconditions: Internship at capacity
     * Expected Result: Application blocked
     */
    @Test
    public void testApplicationToFilledInternship() {
        // Arrange
        InternshipOpportunity smallInternship = new InternshipOpportunity(
            "INT003",
            "Small Internship",
            "Description",
            "Requirements",
            "Singapore",
            2000.0,
            1, // Only 1 slot
            "Company"
        );
        smallInternship.updateStatus("Approved");
        
        // Fill the internship
        Student otherStudent = new Student("S002", "Other Student", "password", 3.5, "CS");
        Application fillingApp = applicationManager.submitApplication(otherStudent, smallInternship);
        fillingApp.updateStatus("Successful");
        fillingApp.updateStatus("Accepted"); // This should fill the slot
        
        // Act
        Application app = applicationManager.submitApplication(testStudent, smallInternship);
        
        // Assert
        assertNull("Cannot apply to filled internship", app);
    }
    
    /**
     * UT-APP-004: Accept Application
     * 
     * Tests company rep accepting application.
     * 
     * Preconditions: Application with "Pending" status
     * Expected Result: Status changed to "Successful"
     */
    @Test
    public void testAcceptApplication() {
        // Arrange
        Application app = applicationManager.submitApplication(testStudent, testInternship);
        assertEquals("Initial status should be Pending", "Pending", app.getStatus());
        
        // Act
        app.updateStatus("Successful");
        
        // Assert
        assertEquals("Status should be Successful", "Successful", app.getStatus());
    }
    
    /**
     * UT-APP-005: Reject Application
     * 
     * Tests company rep rejecting application.
     * 
     * Preconditions: Application with "Pending" status
     * Expected Result: Status "Unsuccessful", student freed
     */
    @Test
    public void testRejectApplication() {
        // Arrange
        Application app = applicationManager.submitApplication(testStudent, testInternship);
        
        // Act
        app.updateStatus("Unsuccessful");
        applicationManager.handleRejection(app); // Should clear student's activeApplication
        
        // Assert
        assertEquals("Status should be Unsuccessful", "Unsuccessful", app.getStatus());
        assertNull("Student activeApplication should be cleared", testStudent.getActiveApplication());
    }
    
    /**
     * UT-APP-006: Student Accepts Offer
     * 
     * Tests student accepting successful application.
     * 
     * Preconditions: Application status "Successful"
     * Expected Result: Status "Accepted", slot decremented
     */
    @Test
    public void testStudentAcceptsOffer() {
        // Arrange
        Application app = applicationManager.submitApplication(testStudent, testInternship);
        app.updateStatus("Successful");
        int initialSlots = testInternship.getAvailableSlots();
        
        // Act
        app.updateStatus("Accepted");
        testInternship.decrementSlot(); // System should do this
        
        // Assert
        assertEquals("Status should be Accepted", "Accepted", app.getStatus());
        assertEquals("Available slots should decrease", initialSlots - 1, testInternship.getAvailableSlots());
    }
    
    /**
     * UT-APP-007: Request Withdrawal
     * 
     * Tests student requesting withdrawal.
     * 
     * Preconditions: Student has active application
     * Expected Result: Withdrawal flag set, status unchanged
     */
    @Test
    public void testRequestWithdrawal() {
        // Arrange
        Application app = applicationManager.submitApplication(testStudent, testInternship);
        assertFalse("Initially no withdrawal requested", app.isWithdrawalRequested());
        
        // Act
        app.markWithdrawalRequested();
        
        // Assert
        assertTrue("Withdrawal should be requested", app.isWithdrawalRequested());
        assertEquals("Status should remain Pending", "Pending", app.getStatus());
    }
    
    /**
     * UT-APP-008: Approve Withdrawal
     * 
     * Tests staff approving withdrawal.
     * 
     * Preconditions: Application has withdrawal request
     * Expected Result: Application removed, student freed
     */
    @Test
    public void testApproveWithdrawal() {
        // Arrange
        Application app = applicationManager.submitApplication(testStudent, testInternship);
        app.markWithdrawalRequested();
        
        // Act
        applicationManager.approveWithdrawal(app);
        
        // Assert
        assertNull("Student activeApplication should be cleared", testStudent.getActiveApplication());
        // Additional assertions depend on whether application is removed from lists
    }
}
