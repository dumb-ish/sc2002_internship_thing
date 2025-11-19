import java.util.Scanner;

/**
 * Abstract base class for all CLI user boundary classes.
 * Provides common functionality for user interaction through command line.
 */
public abstract class CLIUserBoundary implements IUserBoundary {
    protected SystemManager systemManager;
    protected User currentUser;
    protected Scanner scanner;
    
    /**
     * Constructor
     */
    public CLIUserBoundary(SystemManager systemManager, User currentUser) {
        this.systemManager = systemManager;
        this.currentUser = currentUser;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Display menu - to be implemented by subclasses
     */
    @Override
    public abstract void displayMenu();
    
    /**
     * Prompt user to change password
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
     * Update filter criteria
     */
    @Override
    public void updateFilter() {
        FilterBoundary filterBoundary = new FilterBoundary();
        FilterCriteria criteria = currentUser.getFilterCriteria();
        filterBoundary.displayFilterMenu(this, criteria);
    }
    
    /**
     * Check if field can be edited - default implementation
     */
    @Override
    public boolean canEditField(String fieldName) {
        return false; // Default: no fields can be edited
    }
    
    /**
     * Logout the current user
     */
    @Override
    public void logout() {
        System.out.println("Logging out...");
        systemManager.endSession();
    }
    
    /**
     * Get scanner for input
     */
    protected Scanner getScanner() {
        return scanner;
    }
}
