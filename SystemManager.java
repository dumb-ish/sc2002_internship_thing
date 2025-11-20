import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Central controller for the Internship Placement System.
 * Manages users, coordinates between managers, and handles system initialization.
 * Demonstrates Dependency Inversion Principle by injecting filter implementations.
 */
public class SystemManager {
    private List<User> users;
    private InternshipManager internshipManager;
    private ApplicationManager applicationManager;
    private ReportGenerator reportGenerator;
    private User currentUser;
    
    /**
     * Constructor with Dependency Injection for filters
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
     * Initialize the system by loading users from files
     */
    public void initializeSystem(String studentFile, String staffFile, String companyRepFile) {
        loadStudents(studentFile);
        loadStaff(staffFile);
        loadCompanyRepresentatives(companyRepFile);
        System.out.println("System initialized successfully.");
    }
    
    /**
     * Load students from CSV file
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
     * Load staff from CSV file
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
     * Load company representatives from CSV file
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
     * Authenticate a user and return the User object if successful
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
     * Start a user session
     */
    public void startUserSession(User user) {
        this.currentUser = user;
    }
    
    /**
     * Register a new company representative
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
     * Get all pending company representatives
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
     * Run the main system loop
     */
    public void run() {
        System.out.println("Internship Placement System started.");
        // Main loop will be handled by CLILoginBoundary and user-specific boundaries
    }
    
    // Getters for managers
    public InternshipManager getInternshipManager() {
        return internshipManager;
    }
    
    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }
    
    public ReportGenerator getReportGenerator() {
        return reportGenerator;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    /**
     * Find a user by ID
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
     * End current user session
     */
    public void endSession() {
        if (currentUser != null) {
            currentUser.logout();
            currentUser = null;
        }
    }
}
