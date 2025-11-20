import java.util.Scanner;

/**
 * Abstract base class for all command-line interface user boundaries in the View layer.
 * <p>
 * This class implements the Template Method design pattern, providing common functionality
 * for user interaction through the command line while allowing subclasses to customize
 * specific behaviors. It serves as the foundation for all user-specific boundary implementations.
 * 
 * <p><strong>Design Pattern - Template Method:</strong> This class defines the skeleton of
 * common operations (password change, filter updates, logout) while deferring the menu display
 * to concrete subclasses. This ensures consistent behavior across user types while allowing
 * customization.
 * 
 * <p><strong>Responsibilities:</strong>
 * <ul>
 *   <li>Managing user input through Scanner</li>
 *   <li>Providing default implementations for common operations</li>
 *   <li>Maintaining reference to system manager for business logic delegation</li>
 *   <li>Storing current user context for session management</li>
 * </ul>
 * 
 * <p><strong>MVC Architecture:</strong> Belongs to the View layer, handling presentation logic
 * and user interaction. Delegates all business logic to the SystemManager and its associated
 * managers.
 * 
 * <p><strong>Subclasses:</strong> Each user type extends this class to provide specialized menus
 * and operations:
 * <ul>
 *   <li>{@link CLIStudentBoundary} - Student-specific operations</li>
 *   <li>{@link CLICompanyRepBoundary} - Company representative operations</li>
 *   <li>{@link CLIStaffBoundary} - Career center staff operations</li>
 * </ul>
 * 
 * @see IUserBoundary
 * @see CLIStudentBoundary
 * @see CLICompanyRepBoundary
 * @see CLIStaffBoundary
 * @see SystemManager
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public abstract class CLIUserBoundary implements IUserBoundary {
    /** System manager for delegating business logic operations */
    protected SystemManager systemManager;
    
    /** The currently logged-in user */
    protected User currentUser;
    
    /** Scanner for reading user input from command line */
    protected Scanner scanner;
    
    /**
     * Constructs a new CLI user boundary with the specified system manager and user.
     * <p>
     * Initializes the boundary with necessary dependencies for user interaction and
     * business logic delegation. The Scanner is created for console input handling.
     * 
     * @param systemManager the system manager for accessing business logic and data managers
     * @param currentUser the user who is currently logged in and interacting with the system
     */
    public CLIUserBoundary(SystemManager systemManager, User currentUser) {
        this.systemManager = systemManager;
        this.currentUser = currentUser;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Displays the main menu - template method to be implemented by subclasses.
     * <p>
     * Each subclass must provide its own menu implementation tailored to the specific
     * user type's authorized operations. This method typically runs in a loop until
     * the user chooses to logout.
     * 
     * <p><strong>Implementation Requirement:</strong> Subclasses must handle:
     * <ul>
     *   <li>Displaying menu options specific to the user role</li>
     *   <li>Capturing and validating user input</li>
     *   <li>Delegating to appropriate handler methods</li>
     *   <li>Providing error handling for invalid input</li>
     * </ul>
     */
    @Override
    public abstract void displayMenu();
    
    /**
     * Prompts the user to change their password with comprehensive validation.
     * <p>
     * Guides users through a secure password change process:
     * <ol>
     *   <li>Verifies current password for authentication</li>
     *   <li>Prompts for new password</li>
     *   <li>Requires password confirmation to prevent typos</li>
     *   <li>Updates password if all validations pass</li>
     * </ol>
     * 
     * <p><strong>Validation Rules:</strong>
     * <ul>
     *   <li>Current password must match existing password</li>
     *   <li>New password and confirmation must match exactly</li>
     *   <li>Provides clear error messages for validation failures</li>
     * </ul>
     * 
     * <p><strong>User Experience:</strong> Displays success or error messages to guide
     * the user through the process.
     */
    @Override
    public void promptChangePassword() {
        System.out.print("Enter current password: ");
        String oldPassword = scanner.nextLine();
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine();
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");
            return;
        }
        
        if (currentUser.changePassword(oldPassword, newPassword)) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Incorrect current password!");
        }
    }
    
    /**
     * Updates the filter criteria for the current user.
     * <p>
     * Delegates to {@link FilterBoundary} to present an interactive filter configuration
     * menu. Users can customize which internship opportunities they see based on various
     * criteria such as level, major, status, and closing date.
     * 
     * <p><strong>Default Implementation:</strong> This method can be overridden by subclasses
     * that need specialized filter behavior (e.g., students have restricted filter options).
     * 
     * @see FilterBoundary#displayFilterMenu(IUserBoundary, FilterCriteria)
     * @see FilterCriteria
     */
    @Override
    public void updateFilter() {
        FilterBoundary filterBoundary = new FilterBoundary();
        FilterCriteria criteria = currentUser.getFilterCriteria();
        filterBoundary.displayFilterMenu(this, criteria);
    }
    
    /**
     * Checks if a field can be edited by this user type - default implementation.
     * <p>
     * This default implementation denies all edit permissions. Subclasses should override
     * this method to specify which fields their user type can edit.
     * 
     * <p><strong>Authorization:</strong> This method enforces role-based access control,
     * ensuring users can only modify data they are authorized to change.
     * 
     * @param fieldName the name of the field to check for edit permissions
     * @return false by default (no fields can be edited unless overridden by subclass)
     */
    @Override
    public boolean canEditField(String fieldName) {
        return false; // Default: no fields can be edited
    }
    
    /**
     * Logs out the current user and terminates their session.
     * <p>
     * Performs logout operations:
     * <ul>
     *   <li>Displays confirmation message to user</li>
     *   <li>Calls system manager to end the current session</li>
     *   <li>Clears session state and returns to login screen</li>
     * </ul>
     * 
     * <p><strong>Session Management:</strong> Ensures proper cleanup of user session
     * to maintain system security.
     */
    @Override
    public void logout() {
        System.out.println("Logging out...");
        systemManager.endSession();
    }
    
    /**
     * Returns the Scanner instance for reading user input.
     * <p>
     * Provides access to the shared Scanner object for subclasses that need to
     * read user input. Using a single Scanner instance avoids issues with multiple
     * Scanner objects reading from System.in.
     * 
     * @return the Scanner instance for console input
     */
    protected Scanner getScanner() {
        return scanner;
    }
}
