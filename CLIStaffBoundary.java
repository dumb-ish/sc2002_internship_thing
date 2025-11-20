import java.util.List;

/**
 * Command-line interface boundary for Career Center Staff users in the View layer (MVC).
 * <p>
 * This class provides the staff-specific user interface for Career Center administrators,
 * managing all oversight and approval operations in the system. Staff members have the highest
 * level of access and are responsible for maintaining system integrity through approval processes
 * and report generation.
 * 
 * <p><strong>Staff Operations:</strong>
 * <ul>
 *   <li>Approve or reject company representative registrations</li>
 *   <li>Approve or reject internship opportunity postings</li>
 *   <li>Approve or reject student withdrawal requests</li>
 *   <li>Generate comprehensive reports with filtering options</li>
 *   <li>Monitor system activity and application statuses</li>
 * </ul>
 * 
 * <p><strong>Approval Responsibilities:</strong>
 * <ol>
 *   <li><strong>Company Representative Approval:</strong> Review and approve/reject new
 *       company representative accounts before they can post internships</li>
 *   <li><strong>Internship Approval:</strong> Vet internship opportunities for quality
 *       and appropriateness before making them visible to students</li>
 *   <li><strong>Withdrawal Approval:</strong> Review student withdrawal requests to ensure
 *       appropriate handling of application withdrawals</li>
 * </ol>
 * 
 * <p><strong>Reporting Capabilities:</strong>
 * Staff can generate filtered reports showing:
 * <ul>
 *   <li>All internship opportunities</li>
 *   <li>Internships filtered by status (Pending, Approved, Rejected, Filled)</li>
 *   <li>Internships filtered by major</li>
 *   <li>Internships filtered by level</li>
 *   <li>Custom filtered reports with multiple criteria</li>
 *   <li>Summary statistics for system overview</li>
 * </ul>
 * 
 * <p><strong>Business Rules Enforced:</strong>
 * <ul>
 *   <li>Company representatives must be approved before posting internships</li>
 *   <li>Internships must be approved before students can see them</li>
 *   <li>Withdrawal requests require explicit approval/rejection</li>
 *   <li>Approving withdrawal removes application and updates slot availability</li>
 * </ul>
 * 
 * <p><strong>MVC Role:</strong> View layer - handles presentation and user input,
 * delegates all business logic to {@link SystemManager}, {@link InternshipManager},
 * {@link ApplicationManager}, and {@link ReportGenerator}.
 * 
 * @see CLIUserBoundary
 * @see CareerCenterStaff
 * @see SystemManager
 * @see InternshipManager
 * @see ApplicationManager
 * @see ReportGenerator
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class CLIStaffBoundary extends CLIUserBoundary {
    /** Reference to the staff user for type-specific operations */
    private CareerCenterStaff staff;
    
    /**
     * Constructs a staff boundary for the specified staff user.
     * <p>
     * Initializes the boundary with system manager context and casts the user to
     * CareerCenterStaff type for accessing staff-specific attributes and operations.
     * 
     * @param systemManager the system manager for accessing all system managers and data
     * @param currentUser the currently logged-in user (must be a CareerCenterStaff)
     */
    public CLIStaffBoundary(SystemManager systemManager, User currentUser) {
        super(systemManager, currentUser);
        this.staff = (CareerCenterStaff) currentUser;
    }
    
    /**
     * Displays the Career Center Staff menu and handles user interaction loop.
     * <p>
     * Presents the staff-specific menu with oversight and administrative operations.
     * The menu continues to display until the staff member chooses to logout.
     * 
     * <p><strong>Menu Options:</strong>
     * <ol>
     *   <li>Approve/Reject Company Representatives - Review pending registrations</li>
     *   <li>Approve/Reject Internship Opportunities - Vet new postings</li>
     *   <li>Approve/Reject Withdrawal Requests - Handle student withdrawal requests</li>
     *   <li>Generate Reports - Create filtered reports with statistics</li>
     *   <li>Change Password - Update account security</li>
     *   <li>Logout - End session</li>
     * </ol>
     * 
     * <p><strong>User Experience:</strong> Displays staff name in welcome message and
     * provides clear menu structure. Validates numeric input and handles errors gracefully.
     */
    @Override
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== Career Center Staff Menu ===");
            System.out.println("Welcome, " + staff.getName());
            System.out.println("1. Approve/Reject Company Representatives");
            System.out.println("2. Approve/Reject Internship Opportunities");
            System.out.println("3. Approve/Reject Withdrawal Requests");
            System.out.println("4. Generate Reports");
            System.out.println("5. Change Password");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        approveRepresentative();
                        break;
                    case 2:
                        approveInternship();
                        break;
                    case 3:
                        approveWithdrawal();
                        break;
                    case 4:
                        generateReport();
                        break;
                    case 5:
                        promptChangePassword();
                        break;
                    case 6:
                        logout();
                        return;
                    default:
                        System.out.println("Invalid option!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
    
    /**
     * Manages the approval or rejection of pending company representative registrations.
     * <p>
     * This method implements the representative approval workflow:
     * <ol>
     *   <li>Retrieves all pending company representatives from system</li>
     *   <li>Displays detailed information about each pending representative</li>
     *   <li>Prompts staff to select a representative for review</li>
     *   <li>Presents approve/reject decision options</li>
     *   <li>Updates representative status based on decision</li>
     * </ol>
     * 
     * <p><strong>Representative Information Displayed:</strong>
     * <ul>
     *   <li>Name and email (user ID)</li>
     *   <li>Company name</li>
     *   <li>Department and position</li>
     * </ul>
     * 
     * <p><strong>Status Transitions:</strong>
     * <ul>
     *   <li>Approve: Status changes from "Pending" to "Approved" (enables full access)</li>
     *   <li>Reject: Status changes to "Rejected" (prevents login)</li>
     * </ul>
     * 
     * <p><strong>Business Impact:</strong> Approved representatives can immediately begin
     * creating internship opportunities. Rejected representatives cannot login.
     * 
     * <p><strong>Validation:</strong> Ensures selection is within valid range and allows
     * cancellation with 0 input.
     * 
     * @see SystemManager#getPendingRepresentatives()
     */
    public void approveRepresentative() {
        List<CompanyRepresentative> pending = systemManager.getPendingRepresentatives();
        
        System.out.println("\n=== Pending Company Representatives ===");
        if (pending.isEmpty()) {
            System.out.println("No pending company representatives.");
            return;
        }
        
        for (int i = 0; i < pending.size(); i++) {
            CompanyRepresentative rep = pending.get(i);
            System.out.println((i + 1) + ". " + rep.getName());
            System.out.println("   Email: " + rep.getUserID());
            System.out.println("   Company: " + rep.getCompanyName());
            System.out.println("   Department: " + rep.getDepartment());
            System.out.println("   Position: " + rep.getPosition());
        }
        
        System.out.print("\nSelect representative to review (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;
            
            if (choice < 1 || choice > pending.size()) {
                System.out.println("Invalid selection!");
                return;
            }
            
            CompanyRepresentative selected = pending.get(choice - 1);
            
            System.out.println("\n1. Approve");
            System.out.println("2. Reject");
            System.out.print("Decision: ");
            
            int decision = Integer.parseInt(scanner.nextLine());
            if (decision == 1) {
                selected.setStatus("Approved");
                System.out.println("Representative approved!");
            } else if (decision == 2) {
                selected.setStatus("Rejected");
                System.out.println("Representative rejected.");
            } else {
                System.out.println("Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }
    
    /**
     * Manages the approval or rejection of pending internship opportunities.
     * <p>
     * This method implements the internship approval workflow:
     * <ol>
     *   <li>Retrieves all pending internship opportunities</li>
     *   <li>Displays comprehensive details about each internship</li>
     *   <li>Prompts staff to select an internship for review</li>
     *   <li>Presents approve/reject decision options</li>
     *   <li>Updates internship status through InternshipManager</li>
     * </ol>
     * 
     * <p><strong>Internship Information Displayed:</strong>
     * <ul>
     *   <li>Title and company name</li>
     *   <li>Level and preferred major</li>
     *   <li>Full description</li>
     *   <li>Number of slots available</li>
     *   <li>Opening and closing dates</li>
     * </ul>
     * 
     * <p><strong>Status Transitions:</strong>
     * <ul>
     *   <li>Approve: Status changes to "Approved" (becomes visible to eligible students)</li>
     *   <li>Reject: Status changes to "Rejected" (hidden from students)</li>
     * </ul>
     * 
     * <p><strong>Quality Control:</strong> This approval step ensures all internships
     * meet quality standards and are appropriate for students before becoming visible.
     * 
     * <p><strong>Validation:</strong> Ensures selection is within valid range and allows
     * cancellation with 0 input.
     * 
     * @see InternshipManager#getPendingInternships()
     * @see InternshipManager#approveInternship(InternshipOpportunity)
     * @see InternshipManager#rejectInternship(InternshipOpportunity)
     */
    public void approveInternship() {
        List<InternshipOpportunity> pending = systemManager.getInternshipManager()
            .getPendingInternships();
        
        System.out.println("\n=== Pending Internship Opportunities ===");
        if (pending.isEmpty()) {
            System.out.println("No pending internship opportunities.");
            return;
        }
        
        for (int i = 0; i < pending.size(); i++) {
            InternshipOpportunity opp = pending.get(i);
            System.out.println("\n" + (i + 1) + ". " + opp.getTitle());
            System.out.println("   Company: " + opp.getCompanyName());
            System.out.println("   Level: " + opp.getLevel());
            System.out.println("   Preferred Major: " + opp.getPreferredMajor());
            System.out.println("   Description: " + opp.getDescription());
            System.out.println("   Slots: " + opp.getNumSlots());
            System.out.println("   Opening: " + opp.getOpeningDate());
            System.out.println("   Closing: " + opp.getClosingDate());
        }
        
        System.out.print("\nSelect opportunity to review (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;
            
            if (choice < 1 || choice > pending.size()) {
                System.out.println("Invalid selection!");
                return;
            }
            
            InternshipOpportunity selected = pending.get(choice - 1);
            
            System.out.println("\n1. Approve");
            System.out.println("2. Reject");
            System.out.print("Decision: ");
            
            int decision = Integer.parseInt(scanner.nextLine());
            if (decision == 1) {
                systemManager.getInternshipManager().approveInternship(selected);
                System.out.println("Internship opportunity approved!");
            } else if (decision == 2) {
                systemManager.getInternshipManager().rejectInternship(selected);
                System.out.println("Internship opportunity rejected.");
            } else {
                System.out.println("Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }
    
    /**
     * Manages the approval or rejection of student application withdrawal requests.
     * <p>
     * This method implements the withdrawal request approval workflow:
     * <ol>
     *   <li>Retrieves all pending withdrawal requests</li>
     *   <li>Displays student and internship details for each request</li>
     *   <li>Prompts staff to select a request for review</li>
     *   <li>Presents approve/reject decision options</li>
     *   <li>Updates application status based on decision</li>
     * </ol>
     * 
     * <p><strong>Request Information Displayed:</strong>
     * <ul>
     *   <li>Student name and user ID</li>
     *   <li>Internship title and company</li>
     *   <li>Current application status</li>
     * </ul>
     * 
     * <p><strong>Decision Outcomes:</strong>
     * <ul>
     *   <li><strong>Approve Withdrawal:</strong> Application is permanently removed from
     *       system and internship slot is freed for other students</li>
     *   <li><strong>Reject Withdrawal:</strong> Withdrawal flag is cleared and application
     *       is restored to its previous status</li>
     * </ul>
     * 
     * <p><strong>Business Impact:</strong>
     * <ul>
     *   <li>Approving withdrawal frees up an internship slot</li>
     *   <li>Application is completely removed from the system</li>
     *   <li>Student can apply again if they change their mind</li>
     * </ul>
     * 
     * <p><strong>Validation:</strong> Ensures selection is within valid range and allows
     * cancellation with 0 input.
     * 
     * @see ApplicationManager#getWithdrawalRequests()
     * @see ApplicationManager#approveWithdrawal(Application, InternshipManager)
     * @see ApplicationManager#rejectWithdrawal(Application)
     */
    public void approveWithdrawal() {
        List<Application> withdrawalRequests = systemManager.getApplicationManager()
            .getWithdrawalRequests();
        
        System.out.println("\n=== Withdrawal Requests ===");
        if (withdrawalRequests.isEmpty()) {
            System.out.println("No withdrawal requests.");
            return;
        }
        
        for (int i = 0; i < withdrawalRequests.size(); i++) {
            Application app = withdrawalRequests.get(i);
            Student student = app.getStudent();
            InternshipOpportunity opp = app.getInternship();
            
            System.out.println("\n" + (i + 1) + ".");
            System.out.println("   Student: " + student.getName() + " (ID: " + student.getUserID() + ")");
            System.out.println("   Internship: " + opp.getTitle());
            System.out.println("   Company: " + opp.getCompanyName());
            System.out.println("   Current Status: " + app.getStatus());
        }
        
        System.out.print("\nSelect withdrawal request to review (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;
            
            if (choice < 1 || choice > withdrawalRequests.size()) {
                System.out.println("Invalid selection!");
                return;
            }
            
            Application selected = withdrawalRequests.get(choice - 1);
            
            System.out.println("\n1. Approve Withdrawal (Remove application)");
            System.out.println("2. Reject Withdrawal");
            System.out.print("Decision: ");
            
            int decision = Integer.parseInt(scanner.nextLine());
            if (decision == 1) {
                systemManager.getApplicationManager().approveWithdrawal(selected, systemManager.getInternshipManager());
                System.out.println("Withdrawal approved. Application removed from system.");
            } else if (decision == 2) {
                systemManager.getApplicationManager().rejectWithdrawal(selected);
                System.out.println("Withdrawal rejected. Application status restored.");
            } else {
                System.out.println("Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }
    
    /**
     * Generates comprehensive reports on internship opportunities with flexible filtering.
     * <p>
     * This method provides powerful reporting capabilities for staff oversight:
     * 
     * <p><strong>Report Options:</strong>
     * <ol>
     *   <li><strong>All Internship Opportunities:</strong> Unfiltered view of entire system</li>
     *   <li><strong>Filter by Status:</strong> Pending, Approved, Rejected, or Filled</li>
     *   <li><strong>Filter by Major:</strong> Internships for specific academic programs</li>
     *   <li><strong>Filter by Level:</strong> Basic, Intermediate, or Advanced</li>
     *   <li><strong>Custom Filter:</strong> Multiple criteria combined for detailed analysis</li>
     * </ol>
     * 
     * <p><strong>Report Output:</strong>
     * <ul>
     *   <li>Detailed listing of matching internships</li>
     *   <li>Summary statistics (total count, status breakdown)</li>
     *   <li>Application metrics where applicable</li>
     * </ul>
     * 
     * <p><strong>Custom Filter Workflow:</strong>
     * When custom filter is selected, delegates to {@link FilterBoundary} to prompt
     * for detailed criteria including level, major, status, and dates.
     * 
     * <p><strong>Use Cases:</strong>
     * <ul>
     *   <li>Monitor pending approvals requiring attention</li>
     *   <li>Analyze distribution of internships by major/level</li>
     *   <li>Track filled vs. available opportunities</li>
     *   <li>Generate reports for administrative decision-making</li>
     * </ul>
     * 
     * <p><strong>Error Handling:</strong> Validates numeric input and handles invalid
     * choices gracefully with error messages.
     * 
     * @see ReportGenerator#displayReport(List)
     * @see ReportGenerator#displaySummaryStats(List)
     * @see FilterBoundary#promptForCriteria(FilterCriteria)
     */
    public void generateReport() {
        System.out.println("\n=== Generate Report ===");
        System.out.println("1. All Internship Opportunities");
        System.out.println("2. Filter by Status");
        System.out.println("3. Filter by Major");
        System.out.println("4. Filter by Level");
        System.out.println("5. Custom Filter");
        System.out.print("Choice: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            FilterCriteria criteria = new FilterCriteria();
            
            switch (choice) {
                case 1:
                    // No filter - show all
                    break;
                case 2:
                    System.out.println("Select status:");
                    System.out.println("1. Pending");
                    System.out.println("2. Approved");
                    System.out.println("3. Rejected");
                    System.out.println("4. Filled");
                    System.out.print("Choice: ");
                    int statusChoice = Integer.parseInt(scanner.nextLine());
                    switch (statusChoice) {
                        case 1: criteria.setStatus("Pending"); break;
                        case 2: criteria.setStatus("Approved"); break;
                        case 3: criteria.setStatus("Rejected"); break;
                        case 4: criteria.setStatus("Filled"); break;
                        default:
                            System.out.println("Invalid choice!");
                            return;
                    }
                    break;
                case 3:
                    System.out.print("Enter major: ");
                    criteria.setMajor(scanner.nextLine());
                    break;
                case 4:
                    System.out.println("Select level:");
                    System.out.println("1. Basic");
                    System.out.println("2. Intermediate");
                    System.out.println("3. Advanced");
                    System.out.print("Choice: ");
                    int levelChoice = Integer.parseInt(scanner.nextLine());
                    switch (levelChoice) {
                        case 1: criteria.setLevel("Basic"); break;
                        case 2: criteria.setLevel("Intermediate"); break;
                        case 3: criteria.setLevel("Advanced"); break;
                    }
                    break;
                case 5:
                    FilterBoundary filterBoundary = new FilterBoundary();
                    criteria = filterBoundary.promptForCriteria(criteria);
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }
            
            List<InternshipOpportunity> filtered = systemManager.getInternshipManager()
                .filterInternships(criteria);
            
            systemManager.getReportGenerator().displayReport(filtered);
            systemManager.getReportGenerator().displaySummaryStats(filtered);
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }
    
    /**
     * Checks if a field can be edited by staff members.
     * <p>
     * Career Center Staff have the highest level of permissions in the system and can
     * edit or approve/reject all system elements including:
     * <ul>
     *   <li>Company representative accounts</li>
     *   <li>Internship opportunities</li>
     *   <li>Application withdrawal requests</li>
     *   <li>System configuration and filters</li>
     * </ul>
     * 
     * <p><strong>Authorization:</strong> This method enforces that staff have administrative
     * access to all system data and operations.
     * 
     * @param fieldName the name of the field to check for edit permissions (not used)
     * @return true always - staff can edit all fields
     */
    @Override
    public boolean canEditField(String fieldName) {
        // Staff can approve/reject all elements
        return true;
    }
}
