import java.util.Scanner;

/**
 * Handles login and registration operations for the CLI.
 * Provides the initial interface before users are authenticated.
 */
public class CLILoginBoundary {
    private SystemManager systemManager;
    private Scanner scanner;
    
    /**
     * Constructor
     */
    public CLILoginBoundary(SystemManager systemManager) {
        this.systemManager = systemManager;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Display welcome menu and handle login/registration
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
     * Prompt for login credentials and authenticate
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
     * Display login error message
     */
    public void displayLoginError() {
        System.out.println("Invalid credentials! Please try again.");
    }
    
    /**
     * Prompt for company representative registration
     */
    public void promptRegistration() {
        registerCompanyRepresentative();
    }
    
    /**
     * Register a new company representative
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
     * Validate email format
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
