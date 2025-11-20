/**
 * Contract interface for all user boundary implementations in the View layer (MVC architecture).
 * <p>
 * This interface defines the core operations that all user interface implementations must support,
 * ensuring consistent behavior across different user types (Student, Company Representative, Staff).
 * It serves as the foundation for the boundary layer, which is responsible for:
 * <ul>
 *   <li>Handling all user input and output interactions</li>
 *   <li>Presenting information to users through the command-line interface</li>
 *   <li>Validating user input before passing to the controller/manager layer</li>
 *   <li>Maintaining separation of concerns between presentation and business logic</li>
 * </ul>
 * 
 * <p><strong>Design Pattern:</strong> This interface is implemented using the Template Method pattern
 * in {@link CLIUserBoundary}, which provides common functionality while allowing subclasses to
 * customize specific behaviors.
 * 
 * <p><strong>MVC Role:</strong> View layer - responsible for presentation logic only, with no
 * business rules or data access logic.
 * 
 * @see CLIUserBoundary
 * @see CLIStudentBoundary
 * @see CLICompanyRepBoundary
 * @see CLIStaffBoundary
 * @see CLIBoundaryFactory
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public interface IUserBoundary {
    
    /**
     * Displays the main menu for the user and handles menu interaction loop.
     * <p>
     * This method presents user-specific menu options, captures user choices,
     * validates input, and delegates to appropriate handler methods. The menu
     * continues to display until the user chooses to logout.
     * 
     * <p><strong>Implementation Note:</strong> Each user type should provide
     * a customized menu reflecting their authorized operations.
     */
    void displayMenu();
    
    /**
     * Prompts the user to change their password with validation.
     * <p>
     * Guides the user through the password change process, including:
     * <ul>
     *   <li>Verifying current password</li>
     *   <li>Collecting new password</li>
     *   <li>Confirming new password matches</li>
     *   <li>Updating the password if validation succeeds</li>
     * </ul>
     * 
     * <p><strong>Security:</strong> Implements password validation to ensure
     * users can only change their own passwords with proper authentication.
     */
    void promptChangePassword();
    
    /**
     * Updates the filter criteria for the current user.
     * <p>
     * Presents a filter configuration interface allowing users to customize
     * which internship opportunities they see based on criteria such as:
     * <ul>
     *   <li>Level (Basic, Intermediate, Advanced)</li>
     *   <li>Major/preferred major</li>
     *   <li>Status (Pending, Approved, etc.)</li>
     *   <li>Closing date</li>
     * </ul>
     * 
     * <p><strong>Note:</strong> Filter options may be restricted based on user
     * type and permissions (e.g., students have limited filter control).
     * 
     * @see FilterBoundary
     * @see FilterCriteria
     */
    void updateFilter();
    
    /**
     * Checks if a specific field can be edited by this user type.
     * <p>
     * This method enforces authorization rules by determining whether the current
     * user has permission to modify certain data fields. Different user types have
     * different edit permissions:
     * <ul>
     *   <li>Students: Can edit filter settings only</li>
     *   <li>Company Representatives: Can edit their internship postings</li>
     *   <li>Staff: Can edit/approve all elements</li>
     * </ul>
     * 
     * @param fieldName the name of the field to check for edit permissions
     * @return true if the user can edit the field, false otherwise
     */
    boolean canEditField(String fieldName);
    
    /**
     * Logs out the current user and ends their session.
     * <p>
     * Performs cleanup operations including:
     * <ul>
     *   <li>Displaying logout confirmation message</li>
     *   <li>Terminating the current user session</li>
     *   <li>Returning control to the login screen</li>
     * </ul>
     * 
     * <p><strong>Session Management:</strong> Ensures proper session cleanup
     * to prevent unauthorized access after logout.
     */
    void logout();
}
