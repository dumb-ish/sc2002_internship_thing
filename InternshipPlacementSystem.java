/**
 * Main entry point for the Internship Placement Management System.
 * <p>
 * This class contains the {@code main} method that bootstraps the entire application.
 * It is responsible for:
 * <ul>
 *   <li>Initializing the {@link SystemManager} with dependency injection</li>
 *   <li>Loading user data from CSV files (students, staff, company representatives)</li>
 *   <li>Starting the login interface for user authentication</li>
 *   <li>Coordinating the application lifecycle from startup to shutdown</li>
 * </ul>
 * <p>
 * <b>System Architecture:</b><br>
 * The system follows a three-layer architecture:
 * <ol>
 *   <li><b>Entity Layer</b> - Data models (User, Student, CompanyRepresentative, etc.)</li>
 *   <li><b>Control Layer</b> - Business logic managers (SystemManager, InternshipManager, etc.)</li>
 *   <li><b>Boundary Layer</b> - User interface (CLI boundaries)</li>
 * </ol>
 * <p>
 * <b>SOLID Principles Applied:</b>
 * <ul>
 *   <li><b>Single Responsibility Principle</b> - Each class has one clear purpose</li>
 *   <li><b>Open/Closed Principle</b> - System is extensible without modification</li>
 *   <li><b>Liskov Substitution Principle</b> - User subtypes are interchangeable</li>
 *   <li><b>Interface Segregation Principle</b> - Focused interfaces for filters</li>
 *   <li><b>Dependency Inversion Principle</b> - High-level modules depend on abstractions</li>
 * </ul>
 * <p>
 * <b>Usage:</b><br>
 * To run the system, execute this class from the command line:
 * <pre>
 * java InternshipPlacementSystem
 * </pre>
 * The system expects three CSV files in the current directory:
 * <ul>
 *   <li>{@code sample_student_list.csv} - Student user data</li>
 *   <li>{@code sample_staff_list.csv} - Career Center Staff data</li>
 *   <li>{@code sample_company_representative_list.csv} - Company Representative data</li>
 * </ul>
 *
 * @see SystemManager
 * @see CLILoginBoundary
 * @see User
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class InternshipPlacementSystem {
    
    /**
     * Main method - entry point for the Internship Placement Management System.
     * <p>
     * This method performs the following initialization sequence:
     * <ol>
     *   <li>Creates the {@link SystemManager} with injected filter dependencies</li>
     *   <li>Loads user data from CSV files using {@link SystemManager#initializeSystem}</li>
     *   <li>Creates the {@link CLILoginBoundary} for user authentication</li>
     *   <li>Displays the welcome menu and enters the main application loop</li>
     *   <li>Handles application shutdown when user logs out</li>
     * </ol>
     * <p>
     * <b>System Flow:</b>
     * <pre>
     * 1. System Initialization
     * 2. User Login/Registration
     * 3. Role-based Menu Display
     * 4. User Operations (varies by role)
     * 5. Logout and Shutdown
     * </pre>
     *
     * @param args command-line arguments (not currently used)
     */
    public static void main(String[] args) {
        // Create and initialize the system manager
        SystemManager systemManager = new SystemManager();
        
        // Initialize system with user data files
        systemManager.initializeSystem(
            "sample_student_list.csv",
            "sample_staff_list.csv",
            "sample_company_representative_list.csv"
        );
        
        // Create and display login boundary
        CLILoginBoundary loginBoundary = new CLILoginBoundary(systemManager);
        loginBoundary.displayWelcomeMenu();
        
        System.out.println("System shutdown complete.");
    }
}