import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Central controller for the Internship Placement Management System.
 * <p>
 * The SystemManager serves as the primary coordination point for the entire system,
 * managing the lifecycle of all managers and handling critical system-level operations:
 * <ul>
 *   <li>User authentication and session management</li>
 *   <li>System initialization and data loading from CSV files</li>
 *   <li>User registration (particularly for Company Representatives)</li>
 *   <li>Coordination between InternshipManager, ApplicationManager, and ReportGenerator</li>
 *   <li>Approval workflow for new Company Representative registrations</li>
 * </ul>
 * <p>
 * <strong>Design Patterns:</strong>
 * <ul>
 *   <li><strong>Dependency Inversion Principle (DIP):</strong> Injects filter implementations
 *       (ICriteriaFilter, IStudentEligibilityFilter, IInternshipSorter) into InternshipManager,
 *       allowing the high-level SystemManager to depend on abstractions rather than concrete implementations</li>
 *   <li><strong>Facade Pattern:</strong> Provides a unified interface to the subsystem managers,
 *       simplifying interactions for boundary classes</li>
 *   <li><strong>Singleton-like behavior:</strong> Single point of control for system-wide state</li>
 * </ul>
 * <p>
 * <strong>SOLID Principles Demonstrated:</strong>
 * <ul>
 *   <li><strong>Single Responsibility:</strong> Focuses solely on system-level coordination and user management</li>
 *   <li><strong>Dependency Inversion:</strong> Depends on filter interfaces, not concrete implementations</li>
 *   <li><strong>Open/Closed:</strong> New filter implementations can be added without modifying SystemManager</li>
 * </ul>
 *
 * @see User
 * @see InternshipManager
 * @see ApplicationManager
 * @see ReportGenerator
 * @see StudentEligibilityFilter
 * @see ICriteriaFilter
 * @see IStudentEligibilityFilter
 * @see IInternshipSorter
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class SystemManager {
    private List<User> users;
    private InternshipManager internshipManager;
    private ApplicationManager applicationManager;
    private ReportGenerator reportGenerator;
    private User currentUser;
    
    /**
     * Constructs a new SystemManager and initializes all subsystem managers.
     * <p>
     * Demonstrates the Dependency Inversion Principle by creating and injecting
     * filter implementations into the InternshipManager. The StudentEligibilityFilter
     * implements all three required interfaces (ICriteriaFilter, IStudentEligibilityFilter,
     * IInternshipSorter), showing the flexibility of interface-based design.
     * </p>
     * <p>
     * This constructor initializes:
     * <ul>
     *   <li>Empty user list for storing all system users</li>
     *   <li>InternshipManager with injected filter dependencies</li>
     *   <li>ApplicationManager for handling student applications</li>
     *   <li>ReportGenerator for generating system reports</li>
     *   <li>Current user session (initially null)</li>
     * </ul>
     */
    public SystemManager() {
        this.users = new ArrayList<>();
        
        // Create filter implementations
        // StudentEligibilityFilter implements all three interfaces
        StudentEligibilityFilter filter = new StudentEligibilityFilter();
        
        // Inject three separate interfaces (Option 1: Maximum flexibility)
        // Same object can be used for multiple interfaces
        this.internshipManager = new InternshipManager(filter, filter, filter);
        
        this.applicationManager = new ApplicationManager();
        this.reportGenerator = new ReportGenerator();
        this.currentUser = null;
    }
    
    /**
     * Initializes the system by loading all users from CSV data files.
     * <p>
     * This method must be called before the system can be used. It sequentially loads:
     * <ul>
     *   <li>Students from the student CSV file</li>
     *   <li>Career Center Staff from the staff CSV file</li>
     *   <li>Company Representatives from the company representative CSV file</li>
     * </ul>
     * Each CSV file is expected to have a header row which is skipped during loading.
     *
     * @param studentFile    the file path to the student data CSV (e.g., "sample_student_list.csv")
     * @param staffFile      the file path to the staff data CSV (e.g., "sample_staff_list.csv")
     * @param companyRepFile the file path to the company representative CSV (e.g., "sample_company_representative_list.csv")
     */
    public void initializeSystem(String studentFile, String staffFile, String companyRepFile) {
        loadStudents(studentFile);
        loadStaff(staffFile);
        loadCompanyRepresentatives(companyRepFile);
        System.out.println("System initialized successfully.");
    }
    
    /**
     * Loads student data from a CSV file and adds them to the user list.
     * <p>
     * Expected CSV format: UserID, Name, Email, Major, Year
     * <br>Example: U2310001A, John Doe, john@example.com, Computer Science, 2
     * </p>
     * <p>
     * All students are initialized with a default password "password" for security demonstration purposes.
     * In a production system, this would be replaced with a secure password generation mechanism.
     * </p>
     *
     * @param filename the path to the student CSV file
     */
    private void loadStudents(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String major = parts[2].trim();
                    int year = Integer.parseInt(parts[3].trim());
                    Student student = new Student(id, name, "password", year, major);
                    users.add(student);
                }
            }
            System.out.println("Loaded " + users.size() + " students.");
        } catch (IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }
    
    /**
     * Loads Career Center Staff data from a CSV file and adds them to the user list.
     * <p>
     * Expected CSV format: UserID, Name, Email, Role, Department
     * <br>Example: S001, Jane Smith, jane@ntu.edu.sg, Career Counselor, Career Services
     * </p>
     * <p>
     * All staff members are initialized with a default password "password".
     * </p>
     *
     * @param filename the path to the staff CSV file
     */
    private void loadStaff(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            int count = 0;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String department = parts[3].trim();
                    CareerCenterStaff staff = new CareerCenterStaff(id, name, "password", department);
                    users.add(staff);
                    count++;
                }
            }
            System.out.println("Loaded " + count + " staff members.");
        } catch (IOException e) {
            System.out.println("Error loading staff: " + e.getMessage());
        }
    }
    
    /**
     * Loads Company Representative data from a CSV file and adds them to the user list.
     * <p>
     * Expected CSV format: UserID, Name, CompanyName, Department, Position, Email, Status
     * <br>Example: CR001, Bob Johnson, Tech Corp, HR, Recruiter, bob@techcorp.com, Approved
     * </p>
     * <p>
     * Company Representatives can have different statuses (Pending, Approved, Rejected).
     * If a status is not specified in the CSV, it defaults to the value set in the constructor.
     * All representatives are initialized with a default password "password".
     * </p>
     *
     * @param filename the path to the company representative CSV file
     */
    private void loadCompanyRepresentatives(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            int count = 0;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String companyName = parts[2].trim();
                    String department = parts[3].trim();
                    String position = parts[4].trim();
                    CompanyRepresentative rep = new CompanyRepresentative(id, name, "password", 
                                                                         companyName, department, position);
                    if (parts.length >= 7) {
                        rep.setStatus(parts[6].trim());
                    }
                    users.add(rep);
                    count++;
                }
            }
            System.out.println("Loaded " + count + " company representatives.");
        } catch (IOException e) {
            System.out.println("Error loading company representatives: " + e.getMessage());
        }
    }
    
    /**
     * Authenticates a user by verifying their credentials against the system's user list.
     * <p>
     * This method iterates through all registered users and calls their login method
     * to validate credentials. Authentication is case-sensitive for both ID and password.
     * </p>
     *
     * @param id  the user's unique identifier (e.g., "U2310001A", "S001", "CR001")
     * @param pwd the user's password
     * @return the authenticated User object if credentials are valid, null otherwise
     */
    public User authenticateUser(String id, String pwd) {
        for (User user : users) {
            if (user.login(id, pwd)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Starts a new user session by setting the current user.
     * <p>
     * This method should be called after successful authentication to establish
     * the active user context for subsequent operations.
     * </p>
     *
     * @param user the authenticated User object to set as the current user
     */
    public void startUserSession(User user) {
        this.currentUser = user;
    }
    
    /**
     * Registers a new Company Representative in the system.
     * <p>
     * New registrations are created with "Pending" status by default and require
     * approval from Career Center Staff before the representative can access full functionality.
     * This method checks for duplicate user IDs before creating the new representative.
     * </p>
     *
     * @param id          the unique identifier for the new representative (e.g., "CR001")
     * @param name        the representative's full name
     * @param password    the authentication password
     * @param companyName the name of the company the representative works for
     * @param department  the department within the company
     * @param position    the representative's job title/position
     * @return the newly created CompanyRepresentative if successful, null if the ID already exists
     */
    public CompanyRepresentative registerCompanyRepresentative(String id, String name, String password,
                                                               String companyName, String department, 
                                                               String position) {
        // Check if ID already exists
        for (User user : users) {
            if (user.getUserID().equals(id)) {
                return null; // ID already exists
            }
        }
        
        CompanyRepresentative rep = new CompanyRepresentative(id, name, password, 
                                                             companyName, department, position);
        users.add(rep);
        return rep;
    }
    
    /**
     * Retrieves all Company Representatives with "Pending" status awaiting staff approval.
     * <p>
     * This method is used by Career Center Staff to view and process pending registration requests.
     * Only representatives with exactly "Pending" status (case-sensitive) are returned.
     * </p>
     *
     * @return a list of CompanyRepresentative objects with Pending status, empty list if none exist
     */
    public List<CompanyRepresentative> getPendingRepresentatives() {
        List<CompanyRepresentative> pending = new ArrayList<>();
        for (User user : users) {
            if (user instanceof CompanyRepresentative) {
                CompanyRepresentative rep = (CompanyRepresentative) user;
                if ("Pending".equals(rep.getStatus())) {
                    pending.add(rep);
                }
            }
        }
        return pending;
    }
    
    /**
     * Starts the main system execution.
     * <p>
     * This method serves as the entry point for the system's operation.
     * The actual main loop and user interaction logic is handled by CLILoginBoundary
     * and the role-specific boundary classes (CLIStudentBoundary, CLIStaffBoundary, CLICompanyRepBoundary).
     * </p>
     */
    public void run() {
        System.out.println("Internship Placement System started.");
        // Main loop will be handled by CLILoginBoundary and user-specific boundaries
    }
    
    /**
     * Retrieves the InternshipManager instance.
     *
     * @return the system's InternshipManager
     */
    public InternshipManager getInternshipManager() {
        return internshipManager;
    }
    
    /**
     * Retrieves the ApplicationManager instance.
     *
     * @return the system's ApplicationManager
     */
    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }
    
    /**
     * Retrieves the ReportGenerator instance.
     *
     * @return the system's ReportGenerator
     */
    public ReportGenerator getReportGenerator() {
        return reportGenerator;
    }
    
    /**
     * Retrieves the currently authenticated user.
     *
     * @return the current User object, or null if no user is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Retrieves all users in the system.
     * <p>
     * Returns a defensive copy to prevent external modification of the internal user list.
     * </p>
     *
     * @return a new ArrayList containing all registered users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    /**
     * Searches for a user by their unique identifier.
     * <p>
     * This method performs a linear search through the user list to find a matching user ID.
     * The search is case-sensitive.
     * </p>
     *
     * @param userID the unique identifier to search for
     * @return the User object with the matching ID, or null if not found
     */
    public User findUserByID(String userID) {
        for (User user : users) {
            if (user.getUserID().equals(userID)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Ends the current user session and logs out the active user.
     * <p>
     * This method calls the logout method on the current user and clears the session.
     * It is safe to call this method even if no user is currently logged in.
     * </p>
     */
    public void endSession() {
        if (currentUser != null) {
            currentUser.logout();
            currentUser = null;
        }
    }
}
