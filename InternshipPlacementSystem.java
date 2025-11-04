/**
 * Main entry point for the Internship Placement Management System.
 * Initializes the system and starts the login interface.
 */
public class InternshipPlacementSystem {
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