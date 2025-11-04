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
        
        System.out.print("Enter Email (this will be your User ID): ");
        String email = scanner.nextLine();
        if ("0".equals(email)) {
            System.out.println("Registration cancelled.");
            return;
        }
        
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        if ("0".equals(name)) {
            System.out.println("Registration cancelled.");
            return;
        }
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        if ("0".equals(password)) {
            System.out.println("Registration cancelled.");
            return;
        }
        
        System.out.print("Confirm Password: ");
        String confirmPassword = scanner.nextLine();
        if ("0".equals(confirmPassword)) {
            System.out.println("Registration cancelled.");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match! Registration failed.");
            return;
        }
        
        System.out.print("Enter Company Name: ");
        String companyName = scanner.nextLine();
        if ("0".equals(companyName)) {
            System.out.println("Registration cancelled.");
            return;
        }
        
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        if ("0".equals(department)) {
            System.out.println("Registration cancelled.");
            return;
        }
        
        System.out.print("Enter Position: ");
        String position = scanner.nextLine();
        if ("0".equals(position)) {
            System.out.println("Registration cancelled.");
            return;
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
}
