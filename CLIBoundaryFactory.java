/**
 * Factory class for creating role-specific CLI boundary objects (Factory design pattern).
 * <p>
 * This class implements the Factory Method design pattern to create appropriate boundary
 * instances based on the user's role. It encapsulates the creation logic and ensures that
 * each user type receives the correct boundary implementation with appropriate menu options
 * and operations.
 * 
 * <p><strong>Design Pattern - Factory Method:</strong>
 * <ul>
 *   <li>Centralizes boundary object creation</li>
 *   <li>Eliminates need for client code to know about specific boundary classes</li>
 *   <li>Simplifies adding new user types and boundary implementations</li>
 *   <li>Ensures type safety through compile-time checking</li>
 * </ul>
 * 
 * <p><strong>Boundary Mapping:</strong>
 * <ul>
 *   <li>{@link Student} → {@link CLIStudentBoundary}</li>
 *   <li>{@link CompanyRepresentative} → {@link CLICompanyRepBoundary}</li>
 *   <li>{@link CareerCenterStaff} → {@link CLIStaffBoundary}</li>
 * </ul>
 * 
 * <p><strong>Benefits:</strong>
 * <ul>
 *   <li><strong>Maintainability:</strong> Changes to boundary creation logic are localized</li>
 *   <li><strong>Extensibility:</strong> Easy to add new user types without modifying client code</li>
 *   <li><strong>Separation of Concerns:</strong> Client code doesn't need to know boundary details</li>
 *   <li><strong>Type Safety:</strong> Returns common supertype {@link CLIUserBoundary}</li>
 * </ul>
 * 
 * <p><strong>Usage Pattern:</strong>
 * <pre>{@code
 * CLIBoundaryFactory factory = new CLIBoundaryFactory();
 * CLIUserBoundary boundary = factory.createBoundary(systemManager, user);
 * if (boundary != null) {
 *     boundary.displayMenu();
 * }
 * }</pre>
 * 
 * <p><strong>Error Handling:</strong> Returns null and displays error message if user
 * type is not recognized, preventing runtime errors from invalid user types.
 * 
 * <p><strong>MVC Architecture:</strong> This factory is part of the View layer initialization,
 * ensuring proper separation between user types and their presentation logic.
 * 
 * @see CLIUserBoundary
 * @see CLIStudentBoundary
 * @see CLICompanyRepBoundary
 * @see CLIStaffBoundary
 * @see CLILoginBoundary
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class CLIBoundaryFactory {
    
    /**
     * Creates the appropriate CLI boundary based on the user's type.
     * <p>
     * This method uses instanceof checks to determine the user's role and instantiates
     * the corresponding boundary implementation. Each boundary provides role-specific
     * menu options and operations.
     * 
     * <p><strong>Creation Logic:</strong>
     * <ul>
     *   <li>Checks user type using instanceof operator</li>
     *   <li>Instantiates appropriate boundary subclass</li>
     *   <li>Passes system manager and user to boundary constructor</li>
     *   <li>Returns common supertype {@link CLIUserBoundary}</li>
     * </ul>
     * 
     * <p><strong>Return Values:</strong>
     * <ul>
     *   <li>For Student: {@link CLIStudentBoundary} instance</li>
     *   <li>For CompanyRepresentative: {@link CLICompanyRepBoundary} instance</li>
     *   <li>For CareerCenterStaff: {@link CLIStaffBoundary} instance</li>
     *   <li>For unknown type: null (with error message)</li>
     * </ul>
     * 
     * <p><strong>Error Handling:</strong> If the user type is not recognized (should not
     * occur in normal operation), displays error message and returns null. Calling code
     * should check for null return value.
     * 
     * @param mgr the system manager providing access to business logic and data managers
     * @param user the authenticated user for whom to create a boundary
     * @return the appropriate CLIUserBoundary implementation, or null if user type is unknown
     * 
     * @see Student
     * @see CompanyRepresentative
     * @see CareerCenterStaff
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
