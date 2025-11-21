import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDate;

/**
 * Unit tests for Application Management functionality
 * Tests application submission, status updates, and withdrawal
 * 
 * Test Coverage:
 * - Application submission with validation
 * - 3-application limit enforcement
 * - Duplicate application prevention
 * - Year-based eligibility checking
 * - Application status updates
 * - Withdrawal handling
 * 
 * @see ApplicationManager
 * @see Application
 * @see Student
 * @see InternshipOpportunity
 */
public class ApplicationTest {
    
    private ApplicationManager applicationManager;
    private InternshipManager internshipManager;
    private Student testStudent;
    private InternshipOpportunity testInternship;
    
    @Before
    public void setUp() {
        // Initialize managers with proper dependencies
        StudentEligibilityFilter filter = new StudentEligibilityFilter();
        internshipManager = new InternshipManager(filter, filter, filter);
        applicationManager = new ApplicationManager();
        
        // Create test student (Year 3, Computer Science)
        testStudent = new Student("S001", "Test Student", "password", 3, "Computer Science");
        
        // Create test internship with proper constructor
        testInternship = new InternshipOpportunity(
            "Software Engineer Intern",
            "Develop web applications",
            "Intermediate",
            "Computer Science",
            LocalDate.now(),
            LocalDate.now().plusDays(30),
            "Tech Corp",
            "hr@techcorp.com",
            5
        );
        
        // Approve internship so it's open for applications
        internshipManager.addInternship(testInternship);
        internshipManager.approveInternship(testInternship);
    }
    
    @After
    public void tearDown() {
        applicationManager = null;
        internshipManager = null;
        testStudent = null;
        testInternship = null;
    }
    
    /**
     * Test Case: UT-APP-001
     * Verify successful application submission
     */
    @Test
    public void testSubmitApplication() {
        boolean success = applicationManager.submitApplication(testStudent, testInternship);
        
        assertTrue("Application should be submitted successfully", success);
        
        // Verify application was created
        java.util.List<Application> apps = applicationManager.getApplicationsByStudent(testStudent);
        assertEquals("Student should have 1 application", 1, apps.size());
    }
    
    /**
     * Test Case: UT-APP-002
     * Verify 3-application limit is enforced
     */
    @Test
    public void testApplicationLimit() {
        // Create 3 different internships
        InternshipOpportunity internship1 = new InternshipOpportunity(
            "Intern 1", "Desc", "Intermediate", "Computer Science",
            LocalDate.now(), LocalDate.now().plusDays(30), "Company 1", "rep1@company.com", 5
        );
        InternshipOpportunity internship2 = new InternshipOpportunity(
            "Intern 2", "Desc", "Intermediate", "Computer Science",
            LocalDate.now(), LocalDate.now().plusDays(30), "Company 2", "rep2@company.com", 5
        );
        InternshipOpportunity internship3 = new InternshipOpportunity(
            "Intern 3", "Desc", "Intermediate", "Computer Science",
            LocalDate.now(), LocalDate.now().plusDays(30), "Company 3", "rep3@company.com", 5
        );
        InternshipOpportunity internship4 = new InternshipOpportunity(
            "Intern 4", "Desc", "Intermediate", "Computer Science",
            LocalDate.now(), LocalDate.now().plusDays(30), "Company 4", "rep4@company.com", 5
        );
        
        // Approve all internships
        internshipManager.addInternship(internship1);
        internshipManager.approveInternship(internship1);
        internshipManager.addInternship(internship2);
        internshipManager.approveInternship(internship2);
        internshipManager.addInternship(internship3);
        internshipManager.approveInternship(internship3);
        internshipManager.addInternship(internship4);
        internshipManager.approveInternship(internship4);
        
        // Submit 3 applications
        assertTrue("First application should succeed", applicationManager.submitApplication(testStudent, internship1));
        assertTrue("Second application should succeed", applicationManager.submitApplication(testStudent, internship2));
        assertTrue("Third application should succeed", applicationManager.submitApplication(testStudent, internship3));
        
        // Fourth should fail (limit reached)
        assertFalse("Fourth application should fail due to limit", applicationManager.submitApplication(testStudent, internship4));
    }
    
    /**
     * Test Case: UT-APP-003
     * Verify duplicate applications are prevented
     */
    @Test
    public void testDuplicateApplicationPrevention() {
        // First application
        boolean firstSubmit = applicationManager.submitApplication(testStudent, testInternship);
        assertTrue("First application should succeed", firstSubmit);
        
        // Second application to same internship
        boolean secondSubmit = applicationManager.submitApplication(testStudent, testInternship);
        assertFalse("Duplicate application should be prevented", secondSubmit);
    }
    
    /**
     * Test Case: UT-APP-004
     * Verify year-based eligibility restrictions (Year 1-2 students)
     */
    @Test
    public void testYearBasedEligibility() {
        // Create Year 2 student
        Student juniorStudent = new Student("S002", "Junior Student", "password", 2, "Computer Science");
        
        // Create Advanced level internship
        InternshipOpportunity advancedInternship = new InternshipOpportunity(
            "Senior Engineer", "Advanced role", "Advanced", "Computer Science",
            LocalDate.now(), LocalDate.now().plusDays(30), "Tech Corp", "hr@techcorp.com", 5
        );
        internshipManager.addInternship(advancedInternship);
        internshipManager.approveInternship(advancedInternship);
        
        // Year 2 student should not be able to apply for Advanced
        boolean success = applicationManager.submitApplication(juniorStudent, advancedInternship);
        assertFalse("Year 2 student should not be able to apply for Advanced level", success);
        
        // But should be able to apply for Basic
        InternshipOpportunity basicInternship = new InternshipOpportunity(
            "Basic Intern", "Entry level", "Basic", "Computer Science",
            LocalDate.now(), LocalDate.now().plusDays(30), "Tech Corp", "hr@techcorp.com", 5
        );
        internshipManager.addInternship(basicInternship);
        internshipManager.approveInternship(basicInternship);
        
        boolean basicSuccess = applicationManager.submitApplication(juniorStudent, basicInternship);
        assertTrue("Year 2 student should be able to apply for Basic level", basicSuccess);
    }
    
    /**
     * Test Case: UT-APP-005
     * Verify application status updates (Successful/Unsuccessful)
     */
    @Test
    public void testApplicationStatusUpdate() {
        // Submit application
        applicationManager.submitApplication(testStudent, testInternship);
        
        // Get the application
        Application app = applicationManager.getApplicationsByStudent(testStudent).get(0);
        assertEquals("Initial status should be Pending", "Pending", app.getStatus());
        
        // Update status to Successful
        applicationManager.updateApplicationStatus(app, "Successful");
        assertEquals("Status should be Successful", "Successful", app.getStatus());
        
        // Create another application and mark unsuccessful
        InternshipOpportunity internship2 = new InternshipOpportunity(
            "Another Intern", "Desc", "Intermediate", "Computer Science",
            LocalDate.now(), LocalDate.now().plusDays(30), "Company", "rep@company.com", 5
        );
        internshipManager.addInternship(internship2);
        internshipManager.approveInternship(internship2);
        
        applicationManager.submitApplication(testStudent, internship2);
        Application app2 = applicationManager.getApplicationsByInternship(internship2).get(0);
        applicationManager.updateApplicationStatus(app2, "Unsuccessful");
        assertEquals("Status should be Unsuccessful", "Unsuccessful", app2.getStatus());
    }
    
    /**
     * Test Case: UT-APP-006
     * Verify student can accept a successful application
     */
    @Test
    public void testAcceptApplication() {
        // Submit and approve application
        applicationManager.submitApplication(testStudent, testInternship);
        Application app = applicationManager.getApplicationsByStudent(testStudent).get(0);
        applicationManager.updateApplicationStatus(app, "Successful");
        
        // Accept application
        boolean accepted = applicationManager.acceptInternshipPlacement(testStudent, app);
        assertTrue("Application should be accepted", accepted);
        assertEquals("Status should be Accepted", "Accepted", app.getStatus());
    }
    
    /**
     * Test Case: UT-APP-007
     * Verify withdrawal request handling
     */
    @Test
    public void testWithdrawalRequest() {
        // Submit and accept application
        applicationManager.submitApplication(testStudent, testInternship);
        Application app = applicationManager.getApplicationsByStudent(testStudent).get(0);
        applicationManager.updateApplicationStatus(app, "Successful");
        applicationManager.acceptInternshipPlacement(testStudent, app);
        
        // Request withdrawal
        applicationManager.handleWithdrawal(app);
        assertTrue("Withdrawal should be requested", app.isWithdrawalRequested());
    }
    
    /**
     * Test Case: UT-APP-008
     * Verify withdrawal approval by staff
     */
    @Test
    public void testWithdrawalApproval() {
        // Submit, accept, and request withdrawal
        applicationManager.submitApplication(testStudent, testInternship);
        Application app = applicationManager.getApplicationsByStudent(testStudent).get(0);
        applicationManager.updateApplicationStatus(app, "Successful");
        applicationManager.acceptInternshipPlacement(testStudent, app);
        applicationManager.handleWithdrawal(app);
        
        // Staff approves withdrawal
        applicationManager.approveWithdrawal(app, internshipManager);
        
        // Application should be removed from the system
        java.util.List<Application> remainingApps = applicationManager.getApplicationsByStudent(testStudent);
        assertEquals("Student should have no applications after withdrawal", 0, remainingApps.size());
    }
}

