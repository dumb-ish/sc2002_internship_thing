package test.unit;

import org.junit.*;
import static org.junit.Assert.*;

// Import domain classes
import InternshipManager;
import InternshipOpportunity;
import CompanyRepresentative;
import Application;
import Student;

/**
 * Unit tests for Internship Management functionality
 * Tests internship creation, approval, updates, and deletion
 * 
 * Test IDs: UT-INT-001 through UT-INT-006
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
        internshipManager = new InternshipManager();
        
        // Create test company representative
        testCompanyRep = new CompanyRepresentative(
            "hr@techcorp.com",
            "HR Manager",
            "password",
            "Tech Corp",
            "HR",
            "Manager"
        );
        testCompanyRep.updateStatus("Approved");
    }
    
    @After
    public void tearDown() {
        internshipManager = null;
        testCompanyRep = null;
    }
    
    /**
     * UT-INT-001: Create Valid Internship
     * 
     * Tests creating internship with valid data.
     * 
     * Preconditions: Company rep approved
     * Expected Result: Internship created with "Pending" status
     */
    @Test
    public void testCreateValidInternship() {
        // Arrange
        String title = "Software Engineering Intern";
        String description = "Backend development role";
        String requirements = "Java, Spring Boot";
        String location = "Singapore";
        double salary = 2000.0;
        int slots = 3;
        
        // Act
        InternshipOpportunity internship = internshipManager.createInternship(
            title, description, requirements, location, salary, slots, testCompanyRep.getCompanyName()
        );
        
        // Assert
        assertNotNull("Internship should be created", internship);
        assertEquals("Title should match", title, internship.getTitle());
        assertEquals("Description should match", description, internship.getDescription());
        assertEquals("Requirements should match", requirements, internship.getRequirements());
        assertEquals("Location should match", location, internship.getLocation());
        assertEquals("Salary should match", salary, internship.getSalary(), 0.01);
        assertEquals("Slots should match", slots, internship.getAvailableSlots());
        assertEquals("Initial status should be Pending", "Pending", internship.getStatus());
    }
    
    /**
     * UT-INT-002: Create Internship with Invalid Data
     * 
     * Tests validation of internship data.
     * 
     * Preconditions: None
     * Expected Result: Creation fails or validation error
     */
    @Test
    public void testCreateInternshipWithInvalidData() {
        // Test 1: Empty title
        InternshipOpportunity internship1 = internshipManager.createInternship(
            "", "Description", "Requirements", "Location", 2000.0, 3, "Company"
        );
        assertNull("Internship with empty title should not be created", internship1);
        
        // Test 2: Invalid slots (0)
        InternshipOpportunity internship2 = internshipManager.createInternship(
            "Title", "Description", "Requirements", "Location", 2000.0, 0, "Company"
        );
        assertNull("Internship with 0 slots should not be created", internship2);
        
        // Test 3: Negative slots
        InternshipOpportunity internship3 = internshipManager.createInternship(
            "Title", "Description", "Requirements", "Location", 2000.0, -1, "Company"
        );
        assertNull("Internship with negative slots should not be created", internship3);
    }
    
    /**
     * UT-INT-003: Approve Internship
     * 
     * Tests staff approving internship.
     * 
     * Preconditions: Internship with "Pending" status
     * Expected Result: Status changed to "Approved"
     */
    @Test
    public void testApproveInternship() {
        // Arrange
        InternshipOpportunity internship = new InternshipOpportunity(
            "INT001",
            "Test Internship",
            "Description",
            "Requirements",
            "Singapore",
            2000.0,
            3,
            "Tech Corp"
        );
        assertEquals("Initial status should be Pending", "Pending", internship.getStatus());
        
        // Act
        internship.updateStatus("Approved");
        
        // Assert
        assertEquals("Status should be Approved", "Approved", internship.getStatus());
    }
    
    /**
     * UT-INT-004: Reject Internship
     * 
     * Tests staff rejecting internship.
     * 
     * Preconditions: Internship with "Pending" status
     * Expected Result: Status changed to "Rejected"
     */
    @Test
    public void testRejectInternship() {
        // Arrange
        InternshipOpportunity internship = new InternshipOpportunity(
            "INT001",
            "Test Internship",
            "Description",
            "Requirements",
            "Singapore",
            2000.0,
            3,
            "Tech Corp"
        );
        
        // Act
        internship.updateStatus("Rejected");
        
        // Assert
        assertEquals("Status should be Rejected", "Rejected", internship.getStatus());
    }
    
    /**
     * UT-INT-005: Update Internship Details
     * 
     * Tests updating internship information.
     * 
     * Preconditions: Internship exists
     * Expected Result: Details updated successfully
     */
    @Test
    public void testUpdateInternshipDetails() {
        // Arrange
        InternshipOpportunity internship = new InternshipOpportunity(
            "Software Intern",
            "Original description",
            "Intermediate",
            "Computer Science",
            null,
            null,
            "Tech Corp",
            "hr@techcorp.com",
            3
        );
        
        // Act
        String newTitle = "Senior Software Intern";
        String newDescription = "Updated description with more details";
        internship.setTitle(newTitle);
        internship.setDescription(newDescription);
        
        // Assert
        assertEquals("Title should be updated", newTitle, internship.getTitle());
        assertEquals("Description should be updated", newDescription, internship.getDescription());
    }
    
    /**
     * UT-INT-006: Delete Internship
     * 
     * Tests deleting internship with no applications.
     * 
     * Preconditions: Internship has no applications
     * Expected Result: Internship deleted successfully
     */
    @Test
    public void testDeleteInternship() {
        // Arrange
        InternshipOpportunity internship = new InternshipOpportunity(
            "INT001",
            "Test Internship",
            "Description",
            "Requirements",
            "Singapore",
            2000.0,
            3,
            "Tech Corp"
        );
        internshipManager.addInternship(internship);
        assertTrue("Internship should exist", internshipManager.internshipExists("INT001"));
        
        // Act
        boolean deleted = internshipManager.deleteInternship("INT001");
        
        // Assert
        assertTrue("Delete should succeed", deleted);
        assertFalse("Internship should no longer exist", internshipManager.internshipExists("INT001"));
    }
    
    /**
     * Additional Test: Cannot Delete Internship with Applications
     * 
     * Tests that internship with applications cannot be deleted.
     */
    @Test
    public void testCannotDeleteInternshipWithApplications() {
        // Arrange
        InternshipOpportunity internship = new InternshipOpportunity(
            "INT001",
            "Test Internship",
            "Description",
            "Requirements",
            "Singapore",
            2000.0,
            3,
            "Tech Corp"
        );
        internshipManager.addInternship(internship);
        
        // Simulate application exists
        internship.addApplication(new Application(
            new Student("S001", "Student", "password", 3.8, "CS"),
            internship
        ));
        
        // Act
        boolean deleted = internshipManager.deleteInternship("INT001");
        
        // Assert
        assertFalse("Delete should fail when applications exist", deleted);
        assertTrue("Internship should still exist", internshipManager.internshipExists("INT001"));
    }
}
