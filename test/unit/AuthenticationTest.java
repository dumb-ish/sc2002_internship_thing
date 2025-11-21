import org.junit.*;
import static org.junit.Assert.*;
import java.io.File;

/**
 * Unit tests for Authentication functionality
 * Tests user login and company representative registration
 * 
 * Test Coverage:
 * - User authentication for Students, Company Representatives, and Staff
 * - Company representative registration
 * - Password validation
 * 
 * @see SystemManager
 * @see User
 * @see Student
 * @see CompanyRepresentative
 * @see CareerCenterStaff
 */
public class AuthenticationTest {
    private SystemManager systemManager;
    
    @Before
    public void setUp() {
        systemManager = new SystemManager();
        
        // Load test data from CSV files
        systemManager.initializeSystem(
            "sample_student_list.csv",
            "sample_staff_list.csv",
            "sample_company_representative_list.csv"
        );
    }
    
    @After
    public void tearDown() {
        systemManager = null;
    }
    
    /**
     * Test Case: UT-AUTH-001
     * Verify successful student login with valid credentials
     */
    @Test
    public void testValidStudentLogin() {
        // Test with first student from CSV
        User user = systemManager.authenticateUser("U2310001A", "password");
        
        assertNotNull("User should not be null for valid credentials", user);
        assertTrue("User should be Student", user instanceof Student);
        assertEquals("User ID should match", "U2310001A", user.getUserID());
    }
    
    /**
     * Test Case: UT-AUTH-002
     * Verify successful company representative login
     */
    @Test
    public void testValidCompanyRepLogin() {
        // Register a new company rep first since CSV has none
        CompanyRepresentative newRep = systemManager.registerCompanyRepresentative(
            "test@company.com", "Test Rep", "password", "Test Company", "HR", "Manager"
        );
        assertNotNull("Registration should succeed", newRep);
        
        // Now try to login
        User user = systemManager.authenticateUser("test@company.com", "password");
        
        assertNotNull("User should not be null", user);
        assertTrue("User should be CompanyRepresentative", user instanceof CompanyRepresentative);
        CompanyRepresentative rep = (CompanyRepresentative) user;
        assertEquals("Email should match", "test@company.com", rep.getUserID());
    }
    
    /**
     * Test Case: UT-AUTH-003
     * Verify successful staff login
     */
    @Test
    public void testValidStaffLogin() {
        // Test with staff from CSV (sng001)
        User user = systemManager.authenticateUser("sng001", "password");
        
        assertNotNull("User should not be null", user);
        assertTrue("User should be CareerCenterStaff", user instanceof CareerCenterStaff);
        assertEquals("User ID should match", "sng001", user.getUserID());
    }
    
    /**
     * Test Case: UT-AUTH-004
     * Verify login fails with wrong password
     */
    @Test
    public void testInvalidPasswordLogin() {
        User user = systemManager.authenticateUser("U2310001A", "wrongpassword");
        
        assertNull("Authentication should fail with wrong password", user);
    }
    
    /**
     * Test Case: UT-AUTH-005
     * Verify login fails with non-existent user ID
     */
    @Test
    public void testNonExistentUserLogin() {
        User user = systemManager.authenticateUser("NONEXISTENT", "password");
        
        assertNull("Authentication should fail for non-existent user", user);
    }
    
    /**
     * Test Case: UT-AUTH-006
     * Verify company representative registration succeeds with valid data
     */
    @Test
    public void testCompanyRepRegistration() {
        String email = "newrep@company.com";
        String name = "New Representative";
        String password = "password123";
        String company = "New Company";
        String department = "HR";
        String position = "Recruiter";
        
        CompanyRepresentative rep = systemManager.registerCompanyRepresentative(
            email, name, password, company, department, position
        );
        
        assertNotNull("Registration should succeed", rep);
        assertEquals("Email should match", email, rep.getUserID());
        assertEquals("Name should match", name, rep.getName());
        assertEquals("Company should match", company, rep.getCompanyName());
        assertEquals("Initial status should be Pending", "Pending", rep.getStatus());
    }
    
    /**
     * Test Case: UT-AUTH-007
     * Verify duplicate email registration is prevented
     */
    @Test
    public void testDuplicateEmailRegistration() {
        // First registration
        String email = "duplicate@company.com";
        CompanyRepresentative rep1 = systemManager.registerCompanyRepresentative(
            email, "Rep 1", "password", "Company", "HR", "Manager"
        );
        
        assertNotNull("First registration should succeed", rep1);
        
        // Attempt duplicate registration
        CompanyRepresentative rep2 = systemManager.registerCompanyRepresentative(
            email, "Rep 2", "password", "Company", "HR", "Manager"
        );
        
        assertNull("Registration should fail for duplicate email", rep2);
    }
    
    /**
     * Test Case: UT-AUTH-008
     * Verify password change functionality
     */
    @Test
    public void testPasswordChange() {
        // Authenticate first
        User user = systemManager.authenticateUser("U2310001A", "password");
        assertNotNull("Initial login should succeed", user);
        
        // Change password
        String newPassword = "newpassword123";
        user.setPassword(newPassword);
        
        // Try logging in with new password
        User userWithNewPassword = systemManager.authenticateUser("U2310001A", newPassword);
        assertNotNull("Login with new password should succeed", userWithNewPassword);
        
        // Verify old password doesn't work
        User userWithOldPassword = systemManager.authenticateUser("U2310001A", "password");
        assertNull("Login with old password should fail", userWithOldPassword);
    }
}
