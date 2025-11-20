import java.util.Scanner;

/**
 * Command-line interface boundary for authentication and registration operations.
 * <p>
 * This class serves as the entry point to the Internship Placement Management System,
 * handling all pre-authentication user interactions. It provides the welcome screen,
 * login functionality, and company representative self-registration capabilities.
 * 
 * <p><strong>Primary Responsibilities:</strong>
 * <ul>
 *   <li>Display welcome screen and initial menu</li>
 *   <li>Authenticate users (students, company representatives, staff)</li>
 *   <li>Process company representative self-registration</li>
 *   <li>Validate credentials and account status</li>
 *   <li>Create appropriate boundary for authenticated users using factory pattern</li>
 *   <li>Handle login errors and provide user feedback</li>
 * </ul>
 * 
 * <p><strong>Authentication Flow:</strong>
 * <ol>
 *   <li>User enters credentials (user ID and password)</li>
 *   <li>System authenticates through {@link SystemManager}</li>
 *   <li>For company representatives, checks approval status</li>
 *   <li>Creates user session and appropriate boundary using {@link CLIBoundaryFactory}</li>
 *   <li>Transfers control to role-specific boundary for main operations</li>
 * </ol>
 * 
 * <p><strong>Registration Flow:</strong>
 * <ol>
 *   <li>Collects company representative information with validation</li>
 *   <li>Validates email format and all required fields</li>
 *   <li>Checks for duplicate registrations</li>
 *   <li>Creates account with \"Pending\" status</li>
 *   <li>Notifies user that approval is required before login</li>
 * </ol>
 * 
 * <p><strong>Security Features:</strong>
 * <ul>
 *   <li>Password confirmation during registration</li>
 *   <li>Email format validation</li>
 *   <li>Prevents login for rejected company representatives</li>
 *   <li>Clear error messages without revealing sensitive information</li>
 * </ul>
 * 
 * <p><strong>User Experience:</strong>
 * <ul>
 *   <li>Attractive ASCII art welcome banner</li>
 *   <li>Clear menu options and prompts</li>
 *   <li>Ability to cancel registration at any step</li>
 *   <li>Comprehensive input validation with re-prompting</li>
 *   <li>Helpful error messages and status notifications</li>
 * </ul>
 * 
 * <p><strong>MVC Role:</strong> View layer - handles initial presentation and authentication
 * UI, delegates authentication logic to {@link SystemManager}.
 * 
 * @see SystemManager
 * @see CLIBoundaryFactory
 * @see CLIUserBoundary
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class CLILoginBoundary {
    /** System manager for authentication and registration operations */
    private SystemManager systemManager;
    
    /** Scanner for reading user input */
    private Scanner scanner;
    
    /**
     * Constructs a login boundary with the specified system manager.
     * <p>
     * Initializes the boundary with system manager for authentication operations
     * and creates a Scanner for console input handling.
     * 
     * @param systemManager the system manager for user authentication and registration
     */
    public CLILoginBoundary(SystemManager systemManager) {
        this.systemManager = systemManager;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Displays the welcome menu and processes initial user choices.
     * <p>
     * This is the main entry point for all users accessing the system. It presents
     * an attractive welcome banner and menu loop that continues until the user exits.
     * 
     * <p><strong>Menu Options:</strong>
     * <ol>
     *   <li>Login - For existing users (students, company reps, staff)</li>
     *   <li>Register as Company Representative - Self-service registration</li>
     *   <li>Exit - Close the application</li>
     * </ol>
     * 
     * <p><strong>Design:</strong> Uses ASCII art border for visual appeal and clear
     * presentation of the system name.
     * 
     * <p><strong>Error Handling:</strong> Validates numeric input and handles
     * NumberFormatException for invalid entries.
     */
    public void displayWelcomeMenu() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║  Internship Placement Management System  ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Login");
            System.out.println("2. Register as Company Representative");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        promptLogin();
                        break;
                    case 2:
                        promptRegistration();
                        break;
                    case 3:
                        System.out.println("Thank you for using the Internship Placement System. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
    
    /**
     * Prompts user for login credentials and performs authentication.
     * <p>
     * This method implements the complete login workflow:
     * <ol>
     *   <li>Prompts for user ID (email for company reps, ID for students/staff)</li>
     *   <li>Prompts for password</li>
     *   <li>Authenticates through {@link SystemManager#authenticateUser(String, String)}</li>
     *   <li>For company representatives, checks account status (rejected accounts denied)</li>
     *   <li>Creates user session</li>
     *   <li>Uses {@link CLIBoundaryFactory} to create appropriate boundary</li>
     *   <li>Transfers control to role-specific menu</li>
     * </ol>
     * 
     * <p><strong>Status Validation:</strong>
     * <ul>
     *   <li>Company representatives with "Rejected" status cannot login</li>
     *   <li>Company representatives with "Pending" status can login but have limited access</li>
     *   <li>"Approved" representatives have full access</li>
     * </ul>
     * 
     * <p><strong>Error Handling:</strong> Displays appropriate error message for invalid
     * credentials or rejected accounts.
     * 
     * @see SystemManager#authenticateUser(String, String)
     * @see SystemManager#startUserSession(User)
     * @see CLIBoundaryFactory#createBoundary(SystemManager, User)
     */
    public void promptLogin() {
        System.out.println("\n=== Login ===");
        System.out.print("Enter User ID: ");
        String userID = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        User user = systemManager.authenticateUser(userID, password);
        
        if (user == null) {
            displayLoginError();
            return;
        }
        
        // Check if company representative is approved
        if (user instanceof CompanyRepresentative) {
            CompanyRepresentative rep = (CompanyRepresentative) user;
            if ("Rejected".equals(rep.getStatus())) {
                System.out.println("Your account has been rejected. Please contact Career Center Staff.");
                return;
            }
        }
        
        System.out.println("Login successful! Welcome, " + user.getName());
        systemManager.startUserSession(user);
        
        // Create appropriate boundary using factory
        CLIBoundaryFactory factory = new CLIBoundaryFactory();
        CLIUserBoundary boundary = factory.createBoundary(systemManager, user);
        
        if (boundary != null) {
            boundary.displayMenu();
        }
    }
    
    /**
     * Displays an error message for failed login attempts.
     * <p>
     * Provides user-friendly error message without revealing whether the user ID
     * exists or if the password was incorrect (security best practice to prevent
     * account enumeration).
     * 
     * <p><strong>Security:</strong> Generic error message prevents attackers from
     * determining valid user IDs.
     */
    public void displayLoginError() {
        System.out.println("Invalid credentials! Please try again.");
    }
    
    /**
     * Initiates the company representative registration process.
     * <p>
     * This method delegates to {@link #registerCompanyRepresentative()} to handle
     * the complete registration workflow.
     * 
     * @see #registerCompanyRepresentative()
     */
    public void promptRegistration() {
        registerCompanyRepresentative();
    }
    
    /**
     * Handles the complete company representative self-registration process.
     * <p>
     * This comprehensive method guides users through registration with extensive
     * validation and error handling:
     * 
     * <p><strong>Registration Fields (all validated in loops):</strong>
     * <ol>
     *   <li><strong>Email:</strong> Must be valid format (contains @, proper domain). Used as user ID.</li>
     *   <li><strong>Name:</strong> Required, cannot be empty</li>
     *   <li><strong>Password:</strong> Required, cannot be empty</li>
     *   <li><strong>Password Confirmation:</strong> Must match password exactly</li>
     *   <li><strong>Company Name:</strong> Required, cannot be empty</li>
     *   <li><strong>Department:</strong> Required, cannot be empty</li>
     *   <li><strong>Position:</strong> Required, cannot be empty</li>
     * </ol>
     * 
     * <p><strong>Validation Loop Pattern:</strong> Each field uses validation loop that:
     * <ul>
     *   <li>Prompts for input</li>
     *   <li>Allows cancellation with '0' input at any step</li>
     *   <li>Validates input (non-empty, format check for email, match check for password)</li>
     *   <li>Re-prompts with clear error message if validation fails</li>
     *   <li>Continues until valid input received or user cancels</li>
     * </ul>
     * 
     * <p><strong>Account Creation:</strong>
     * <ul>
     *   <li>New accounts start with "Pending" status</li>
     *   <li>Representatives cannot login until approved by Career Center Staff</li>
     *   <li>Email is used as the unique user ID</li>
     *   <li>Duplicate emails are rejected</li>
     * </ul>
     * 
     * <p><strong>User Experience:</strong>
     * <ul>
     *   <li>Clear instructions including cancellation option</li>
     *   <li>Attractive success banner upon completion</li>
     *   <li>Explicit notice that approval is required</li>
     *   <li>Displays user ID for future login</li>
     * </ul>
     * 
     * <p><strong>Error Scenarios:</strong>
     * <ul>
     *   <li>Duplicate email: Registration fails with error message</li>
     *   <li>Invalid format: Re-prompts with specific validation error</li>
     *   <li>Empty fields: Re-prompts indicating field cannot be empty</li>
     * </ul>
     * 
     * @see SystemManager#registerCompanyRepresentative(String, String, String, String, String, String)
     * @see #isValidEmail(String)
     */
    public void registerCompanyRepresentative() {
        System.out.println("\n=== Company Representative Registration ===");
        System.out.println("(Enter '0' at any prompt to cancel registration)");
        
        // Email validation with loop
        String email = null;
        while (email == null) {
            System.out.print("Enter Email (this will be your User ID): ");
            String input = scanner.nextLine().trim();
            
            if ("0".equals(input)) {
                System.out.println("Registration cancelled.");
                return;
            }
            
            if (input.isEmpty()) {
                System.out.println("Email cannot be empty. Please re-enter.");
            } else if (!isValidEmail(input)) {
                System.out.println("Invalid email format. Please enter a valid email address.");
            } else {
                email = input;
            }
        }
        
        // Name validation with loop
        String name = null;
        while (name == null) {
            System.out.print("Enter Name: ");
            String input = scanner.nextLine().trim();
            
            if ("0".equals(input)) {
                System.out.println("Registration cancelled.");
                return;
            }
            
            if (input.isEmpty()) {
                System.out.println("Name cannot be empty. Please re-enter.");
            } else {
                name = input;
            }
        }
        
        // Password validation with loop
        String password = null;
        while (password == null) {
            System.out.print("Enter Password: ");
            String input = scanner.nextLine();
            
            if ("0".equals(input)) {
                System.out.println("Registration cancelled.");
                return;
            }
            
            if (input.isEmpty()) {
                System.out.println("Password cannot be empty. Please re-enter.");
            } else {
                password = input;
            }
        }
        
        // Password confirmation with loop
        String confirmPassword = null;
        while (confirmPassword == null) {
            System.out.print("Confirm Password: ");
            String input = scanner.nextLine();
            
            if ("0".equals(input)) {
                System.out.println("Registration cancelled.");
                return;
            }
            
            if (input.isEmpty()) {
                System.out.println("Password confirmation cannot be empty. Please re-enter.");
            } else if (!password.equals(input)) {
                System.out.println("Passwords do not match! Please re-enter.");
            } else {
                confirmPassword = input;
            }
        }
        
        // Company name validation with loop
        String companyName = null;
        while (companyName == null) {
            System.out.print("Enter Company Name: ");
            String input = scanner.nextLine().trim();
            
            if ("0".equals(input)) {
                System.out.println("Registration cancelled.");
                return;
            }
            
            if (input.isEmpty()) {
                System.out.println("Company name cannot be empty. Please re-enter.");
            } else {
                companyName = input;
            }
        }
        
        // Department validation with loop
        String department = null;
        while (department == null) {
            System.out.print("Enter Department: ");
            String input = scanner.nextLine().trim();
            
            if ("0".equals(input)) {
                System.out.println("Registration cancelled.");
                return;
            }
            
            if (input.isEmpty()) {
                System.out.println("Department cannot be empty. Please re-enter.");
            } else {
                department = input;
            }
        }
        
        // Position validation with loop
        String position = null;
        while (position == null) {
            System.out.print("Enter Position: ");
            String input = scanner.nextLine().trim();
            
            if ("0".equals(input)) {
                System.out.println("Registration cancelled.");
                return;
            }
            
            if (input.isEmpty()) {
                System.out.println("Position cannot be empty. Please re-enter.");
            } else {
                position = input;
            }
        }
        
        CompanyRepresentative rep = systemManager.registerCompanyRepresentative(
            email, name, password, companyName, department, position
        );
        
        if (rep == null) {
            System.out.println("Registration failed! Email already exists.");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║    Registration Successful!            ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("Your account is pending approval by Career Center Staff.");
        System.out.println("You will be able to login once approved.");
        System.out.println("User ID: " + email);
    }
    
    /**
     * Validates email address format for company representative registration.
     * <p>
     * Performs basic email validation to ensure the email is in a valid format
     * before allowing registration. This helps prevent typos and ensures users
     * can be contacted.
     * 
     * <p><strong>Validation Rules:</strong>
     * <ul>
     *   <li>Must not be null or empty</li>
     *   <li>Must contain exactly one '@' symbol</li>
     *   <li>Must have text before the '@' symbol (local part)</li>
     *   <li>Must have text after '@' including a domain with period</li>
     *   <li>Domain must have text before and after the last period</li>
     * </ul>
     * 
     * <p><strong>Valid Examples:</strong>
     * <ul>
     *   <li>user@company.com</li>
     *   <li>john.doe@example.org</li>
     *   <li>hr.manager@company.co.uk</li>
     * </ul>
     * 
     * <p><strong>Invalid Examples:</strong>
     * <ul>
     *   <li>@company.com (no local part)</li>
     *   <li>user@ (no domain)</li>
     *   <li>user@domain (no period in domain)</li>
     *   <li>user@@company.com (multiple @ symbols)</li>
     * </ul>
     * 
     * <p><strong>Note:</strong> This is a basic validation suitable for user input.
     * It does not guarantee deliverability or compliance with all RFC 5322 rules.
     * 
     * @param email the email address string to validate
     * @return true if the email format is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        // Basic email validation: contains @ and has text before and after it
        if (email == null || email.isEmpty()) {
            return false;
        }
        
        int atIndex = email.indexOf('@');
        int lastAtIndex = email.lastIndexOf('@');
        
        // Must have exactly one @ symbol
        if (atIndex == -1 || atIndex != lastAtIndex) {
            return false;
        }
        
        // Must have text before @
        if (atIndex == 0) {
            return false;
        }
        
        // Must have text after @ including a dot
        String domain = email.substring(atIndex + 1);
        if (domain.isEmpty() || !domain.contains(".")) {
            return false;
        }
        
        // Domain must have text after the dot
        int dotIndex = domain.lastIndexOf('.');
        if (dotIndex == domain.length() - 1 || dotIndex == 0) {
            return false;
        }
        
        return true;
    }
}
