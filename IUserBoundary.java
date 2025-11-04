/**
 * Interface for all user boundary classes.
 * Defines common operations that all user interfaces must implement.
 */
public interface IUserBoundary {
    
    /**
     * Display the main menu for the user
     */
    void displayMenu();
    
    /**
     * Prompt user to change their password
     */
    void promptChangePassword();
    
    /**
     * Update filter criteria for the user
     */
    void updateFilter();
    
    /**
     * Check if a field can be edited by this user type
     */
    boolean canEditField(String fieldName);
    
    /**
     * Logout the current user
     */
    void logout();
}
