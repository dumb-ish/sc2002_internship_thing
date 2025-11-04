/**
 * Factory class for creating appropriate CLI boundary objects.
 * Implements the Factory design pattern for boundary creation.
 */
public class CLIBoundaryFactory {
    
    /**
     * Create the appropriate boundary based on user type
     */
    public CLIUserBoundary createBoundary(SystemManager mgr, User user) {
        if (user instanceof Student) {
            return new CLIStudentBoundary(mgr, user);
        } else if (user instanceof CompanyRepresentative) {
            return new CLICompanyRepBoundary(mgr, user);
        } else if (user instanceof CareerCenterStaff) {
            return new CLIStaffBoundary(mgr, user);
        }
        
        System.out.println("Error: Unknown user type!");
        return null;
    }
}
