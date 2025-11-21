import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDate;

/**
 * Unit tests for Internship Management functionality
 * Tests internship creation, approval workflow, and visibility control
 * 
 * Test Coverage:
 * - Internship creation with validation
 * - Approval and rejection workflows
 * - Visibility toggle
 * - Status management
 * - Representative internship limits
 * 
 * @see InternshipManager
 * @see InternshipOpportunity
 * @see CompanyRepresentative
 */
public class InternshipTest {
    private InternshipManager internshipManager;
    private CompanyRepresentative testCompanyRep;
    
    @Before
    public void setUp() {
        // Initialize manager with filter dependencies
        StudentEligibilityFilter filter = new StudentEligibilityFilter();
        internshipManager = new InternshipManager(filter, filter, filter);
        
        // Create approved company representative
        testCompanyRep = new CompanyRepresentative(
            "hr@testcorp.com",
            "HR Manager",
            "password",
            "Test Corp",
            "Human Resources",
            "Manager"
        );
        testCompanyRep.setStatus("Approved");
    }
    
    @After
    public void tearDown() {
        internshipManager = null;
        testCompanyRep = null;
    }
    
    /**
     * Test Case: UT-INT-001
     * Verify internship creation with valid data
     */
    @Test
    public void testCreateInternship() {
        InternshipOpportunity internship = new InternshipOpportunity(
            "Software Engineer Intern",
            "Develop web applications using Java and React",
            "Intermediate",
            "Computer Science",
            LocalDate.now(),
            LocalDate.now().plusDays(30),
            "Test Corp",
            testCompanyRep.getUserID(),
            5
        );
        
        internshipManager.addInternship(internship);
        
        assertNotNull("Internship should be created", internship);
        assertEquals("Title should match", "Software Engineer Intern", internship.getTitle());
        assertEquals("Description should match", "Develop web applications using Java and React", internship.getDescription());
        assertEquals("Level should match", "Intermediate", internship.getLevel());
        assertEquals("Initial status should be Pending", "Pending", internship.getStatus());
        assertFalse("Initial visibility should be false", internship.getVisibility());
    }
    
    /**
     * Test Case: UT-INT-002
     * Verify internship approval workflow
     */
    @Test
    public void testApproveInternship() {
        InternshipOpportunity internship = new InternshipOpportunity(
            "Data Analyst Intern",
            "Analyze business data",
            "Basic",
            "Data Science & AI",
            LocalDate.now(),
            LocalDate.now().plusDays(30),
            "Test Corp",
            testCompanyRep.getUserID(),
            3
        );
        
        internshipManager.addInternship(internship);
        assertEquals("Initial status should be Pending", "Pending", internship.getStatus());
        
        // Staff approves internship
        internshipManager.approveInternship(internship);
        
        assertEquals("Status should be Approved", "Approved", internship.getStatus());
        assertTrue("Visibility should be automatically set to true", internship.getVisibility());
    }
    
    /**
     * Test Case: UT-INT-003
     * Verify internship rejection workflow
     */
    @Test
    public void testRejectInternship() {
        InternshipOpportunity internship = new InternshipOpportunity(
            "Marketing Intern",
            "Social media management",
            "Basic",
            "Business",
            LocalDate.now(),
            LocalDate.now().plusDays(30),
            "Test Corp",
            testCompanyRep.getUserID(),
            2
        );
        
        internshipManager.addInternship(internship);
        
        // Staff rejects internship
        internshipManager.rejectInternship(internship);
        
        assertEquals("Status should be Rejected", "Rejected", internship.getStatus());
        assertFalse("Visibility should remain false", internship.getVisibility());
    }
    
    /**
     * Test Case: UT-INT-004
     * Verify visibility toggle functionality
     */
    @Test
    public void testVisibilityToggle() {
        InternshipOpportunity internship = new InternshipOpportunity(
            "UI/UX Designer Intern",
            "Design user interfaces",
            "Intermediate",
            "Computer Science",
            LocalDate.now(),
            LocalDate.now().plusDays(30),
            "Test Corp",
            testCompanyRep.getUserID(),
            4
        );
        
        internshipManager.addInternship(internship);
        internshipManager.approveInternship(internship);
        assertTrue("Should be visible after approval", internship.getVisibility());
        
        // Company rep toggles visibility off
        internshipManager.toggleVisibility(internship);
        assertFalse("Visibility should be toggled off", internship.getVisibility());
        
        // Toggle back on
        internshipManager.toggleVisibility(internship);
        assertTrue("Visibility should be toggled back on", internship.getVisibility());
    }
    
    /**
     * Test Case: UT-INT-005
     * Verify filtering returns only approved and visible internships
     */
    @Test
    public void testGetApprovedInternships() {
        // Create and approve first internship
        InternshipOpportunity approved1 = new InternshipOpportunity(
            "Intern 1", "Desc", "Intermediate", "Computer Science",
            LocalDate.now(), LocalDate.now().plusDays(30), "Company 1", "rep1@company.com", 5
        );
        internshipManager.addInternship(approved1);
        internshipManager.approveInternship(approved1);
        
        // Create pending internship (not approved)
        InternshipOpportunity pending = new InternshipOpportunity(
            "Intern 2", "Desc", "Basic", "Computer Science",
            LocalDate.now(), LocalDate.now().plusDays(30), "Company 2", "rep2@company.com", 3
        );
        internshipManager.addInternship(pending);
        
        // Create rejected internship
        InternshipOpportunity rejected = new InternshipOpportunity(
            "Intern 3", "Desc", "Advanced", "Computer Science",
            LocalDate.now(), LocalDate.now().plusDays(30), "Company 3", "rep3@company.com", 2
        );
        internshipManager.addInternship(rejected);
        internshipManager.rejectInternship(rejected);
        
        // Filter with no criteria (should return only approved and visible)
        FilterCriteria criteria = new FilterCriteria();
        criteria.setStatus("Approved");
        var filteredList = internshipManager.filterInternships(criteria);
        
        // Only approved1 should be in the list
        assertEquals("Should return 1 approved internship", 1, filteredList.size());
        assertEquals("Should be the approved internship", "Intern 1", filteredList.get(0).getTitle());
    }
    
    /**
     * Test Case: UT-INT-006
     * Verify opening and closing dates are validated
     */
    @Test
    public void testDateValidation() {
        // Create internship with proper dates
        InternshipOpportunity validInternship = new InternshipOpportunity(
            "Valid Intern",
            "Description",
            "Basic",
            "Computer Science",
            LocalDate.now(),
            LocalDate.now().plusDays(30),
            "Test Corp",
            testCompanyRep.getUserID(),
            5
        );
        
        internshipManager.addInternship(validInternship);
        internshipManager.approveInternship(validInternship);
        
        // Check if internship is open for applications
        assertTrue("Internship should be open for applications", validInternship.isOpenForApplications());
        
        // Create internship with past closing date
        InternshipOpportunity closedInternship = new InternshipOpportunity(
            "Closed Intern",
            "Description",
            "Basic",
            "Computer Science",
            LocalDate.now().minusDays(30),
            LocalDate.now().minusDays(1),
            "Test Corp",
            testCompanyRep.getUserID(),
            5
        );
        
        internshipManager.addInternship(closedInternship);
        internshipManager.approveInternship(closedInternship);
        
        // Check if internship is closed
        assertFalse("Internship with past closing date should not be open", closedInternship.isOpenForApplications());
    }
}
