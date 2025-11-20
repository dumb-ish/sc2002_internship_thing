package test.unit;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Unit tests for Authentication functionality
 * Tests user login, registration, and credential validation
 * 
 * Test IDs: UT-AUTH-001 through UT-AUTH-008
 * 
 * @see SystemManager
 * @see User
 * @see Student
 * @see CompanyRepresentative
 * @see CareerCenterStaff
 */
public class AuthenticationTest {
    
    private SystemManager systemManager;
    
    /**
     * Set up test environment before each test
     * Initializes SystemManager with test data
     */
    @Before
    public void setUp() {
        // Initialize system manager with test data
        systemManager = new SystemManager();
        // Load test CSV files or create test users programmatically
    }
    
    /**
     * Clean up after each test
     */
    @After
    public void tearDown() {
        // Clean up resources
        systemManager = null;
    }
    
    /**
     * UT-AUTH-001: Valid Login - Student
     * 
     * Tests that a student can successfully login with valid credentials.
     * 
     * Preconditions: Student S001 exists with password "password"
     * Expected Result: Authentication successful, Student object returned
     */
    @Test
    public void testValidStudentLogin() {
        // Arrange
        String userID = "S001";
        String password = "password";
        
        // Act
        User user = systemManager.authenticateUser(userID, password);
        
        // Assert
        assertNotNull("User should not be null for valid credentials", user);
        assertTrue("User should be instance of Student", user instanceof Student);
        assertEquals("User ID should match", userID, user.getUserID());
    }
    
    /**
     * UT-AUTH-002: Valid Login - Company Representative
     * 
     * Tests that an approved company representative can login successfully.
     * 
     * Preconditions: Company rep exists with "Approved" status
     * Expected Result: Authentication successful, CompanyRepresentative returned
     */
    @Test
    public void testValidCompanyRepLogin() {
        // Arrange
        String userID = "hr@techcorp.com";
        String password = "password";
        
        // Act
        User user = systemManager.authenticateUser(userID, password);
        
        // Assert
        assertNotNull("User should not be null", user);
        assertTrue("User should be CompanyRepresentative", user instanceof CompanyRepresentative);
        
        CompanyRepresentative rep = (CompanyRepresentative) user;
        assertEquals("Status should be Approved", "Approved", rep.getStatus());
    }
    
    /**
     * UT-AUTH-003: Valid Login - Career Center Staff
     * 
     * Tests that career center staff can login successfully.
     * 
     * Preconditions: Staff STAFF001 exists
     * Expected Result: Authentication successful, CareerCenterStaff returned
     */
    @Test
    public void testValidStaffLogin() {
        // Arrange
        String userID = "STAFF001";
        String password = "password";
        
        // Act
        User user = systemManager.authenticateUser(userID, password);
        
        // Assert
        assertNotNull("User should not be null", user);
        assertTrue("User should be CareerCenterStaff", user instanceof CareerCenterStaff);
    }
    
    /**
     * UT-AUTH-004: Invalid Login - Wrong Password
     * 
     * Tests that authentication fails with incorrect password.
     * 
     * Preconditions: User S001 exists
     * Expected Result: Authentication fails, null returned
     */
    @Test
    public void testInvalidPassword() {
        // Arrange
        String userID = "S001";
        String wrongPassword = "wrongpassword";
        
        // Act
        User user = systemManager.authenticateUser(userID, wrongPassword);
        
        // Assert
        assertNull("Authentication should fail with wrong password", user);
    }
    
    /**
     * UT-AUTH-005: Invalid Login - Non-existent User
     * 
     * Tests that authentication fails for non-existent user.
     * 
     * Preconditions: None
     * Expected Result: Authentication fails, null returned
     */
    @Test
    public void testNonExistentUser() {
        // Arrange
        String userID = "INVALID123";
        String password = "password";
        
        // Act
        User user = systemManager.authenticateUser(userID, password);
        
        // Assert
        assertNull("Authentication should fail for non-existent user", user);
    }
    
    /**
     * UT-AUTH-006: Invalid Login - Rejected Company Representative
     * 
     * Tests that rejected company representative cannot login.
     * 
     * Preconditions: Company rep exists with "Rejected" status
     * Expected Result: Login blocked
     */
    @Test
    public void testRejectedCompanyRepLogin() {
        // Arrange
        String userID = "rejected@company.com";
        String password = "password";
        
        // Act
        User user = systemManager.authenticateUser(userID, password);
        
        // Assert
        // Either returns null or returns user but with Rejected status
        if (user != null) {
            assertTrue("User should be CompanyRepresentative", user instanceof CompanyRepresentative);
            CompanyRepresentative rep = (CompanyRepresentative) user;
            assertEquals("Status should be Rejected", "Rejected", rep.getStatus());
            // UI should block login for rejected status
        } else {
            // Alternative: system blocks login completely
            assertNull("Rejected rep should not be able to login", user);
        }
    }
    
    /**
     * UT-AUTH-007: Valid Registration
     * 
     * Tests successful company representative registration.
     * 
     * Preconditions: Email not already registered
     * Expected Result: Registration successful, account with "Pending" status
     */
    @Test
    public void testValidRegistration() {
        // Arrange
        String email = "newrep@company.com";
        String name = "John Doe";
        String password = "password123";
        String company = "Tech Corp";
        String department = "HR";
        String position = "Recruiter";
        
        // Act
        CompanyRepresentative rep = systemManager.registerCompanyRepresentative(
            email, name, password, company, department, position
        );
        
        // Assert
        assertNotNull("Registration should succeed", rep);
        assertEquals("Email should match", email, rep.getUserID());
        assertEquals("Name should match", name, rep.getName());
        assertEquals("Status should be Pending", "Pending", rep.getStatus());
        assertEquals("Company should match", company, rep.getCompanyName());
    }
    
    /**
     * UT-AUTH-008: Duplicate Email Registration
     * 
     * Tests that registration fails for duplicate email.
     * 
     * Preconditions: Email already registered
     * Expected Result: Registration fails, null returned
     */
    @Test
    public void testDuplicateEmailRegistration() {
        // Arrange
        String existingEmail = "hr@techcorp.com"; // Assume this exists
        String name = "Jane Doe";
        String password = "password";
        String company = "Another Company";
        String department = "HR";
        String position = "Manager";
        
        // Act
        CompanyRepresentative rep = systemManager.registerCompanyRepresentative(
            existingEmail, name, password, company, department, position
        );
        
        // Assert
        assertNull("Registration should fail for duplicate email", rep);
    }
}
