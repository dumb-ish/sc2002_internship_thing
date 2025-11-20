import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Command-line interface boundary for Company Representative users in the View layer (MVC).
 * <p>
 * This class provides the company representative-specific user interface, managing all
 * interactions for company representatives including creating internship opportunities,
 * managing applications, and controlling internship visibility. It extends {@link CLIUserBoundary}
 * to inherit common functionality while implementing company-specific operations.
 * 
 * <p><strong>Company Representative Operations:</strong>
 * <ul>
 *   <li>Create new internship opportunities (up to 5 per representative)</li>
 *   <li>View and manage their posted internships</li>
 *   <li>Review applications from students</li>
 *   <li>Approve or reject student applications</li>
 *   <li>Toggle visibility of approved internships</li>
 *   <li>Configure filter settings for viewing internships</li>
 * </ul>
 * 
 * <p><strong>Account Status Handling:</strong>
 * <ul>
 *   <li><em>Pending</em> - Restricted menu: can only change password and logout</li>
 *   <li><em>Approved</em> - Full menu access with all operations</li>
 *   <li><em>Rejected</em> - Cannot login (handled in login boundary)</li>
 * </ul>
 * 
 * <p><strong>Business Rules Enforced:</strong>
 * <ul>
 *   <li>Maximum 5 internship opportunities per representative</li>
 *   <li>All new internships require Career Center Staff approval</li>
 *   <li>Cannot approve applications beyond available slots</li>
 *   <li>Can only toggle visibility of approved internships</li>
 *   <li>Cannot modify finalized applications (Successful/Unsuccessful/Accepted)</li>
 * </ul>
 * 
 * <p><strong>Input Validation:</strong> All user inputs are validated with error messages
 * and re-prompting for correct input. Users can cancel operations at any prompt by entering 'q' or '0'.
 * 
 * <p><strong>MVC Role:</strong> View layer - handles presentation and input validation,
 * delegates business logic to {@link InternshipManager} and {@link ApplicationManager}.
 * 
 * @see CLIUserBoundary
 * @see CompanyRepresentative
 * @see InternshipManager
 * @see ApplicationManager
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class CLICompanyRepBoundary extends CLIUserBoundary {
    /** Reference to the company representative user for type-specific operations */
    private CompanyRepresentative representative;
    
    /**
     * Constructs a company representative boundary for the specified user.
     * <p>
     * Initializes the boundary with system manager context and casts the user to
     * CompanyRepresentative type for accessing representative-specific attributes.
     * 
     * @param systemManager the system manager for accessing internship and application managers
     * @param currentUser the currently logged-in user (must be a CompanyRepresentative)
     */
    public CLICompanyRepBoundary(SystemManager systemManager, User currentUser) {
        super(systemManager, currentUser);
        this.representative = (CompanyRepresentative) currentUser;
    }
    
    /**
     * Displays the company representative menu based on account approval status.
     * <p>
     * This method implements conditional menu display:
     * <ul>
     *   <li>If status is "Approved": Shows full menu with all operations</li>
     *   <li>If status is not "Approved" (Pending): Shows restricted menu (password change and logout only)</li>
     * </ul>
     * 
     * <p><strong>Full Menu Options (Approved accounts):</strong>
     * <ol>
     *   <li>Create Internship Opportunity - Post new internship (max 5)</li>
     *   <li>View My Internship Opportunities - See all posted internships</li>
     *   <li>View Applications - Review student applications</li>
     *   <li>Approve/Reject Application - Make hiring decisions</li>
     *   <li>Toggle Internship Visibility - Show/hide approved internships</li>
     *   <li>Update Filter Settings - Customize internship view</li>
     *   <li>Change Password - Update account security</li>
     *   <li>Logout - End session</li>
     * </ol>
     * 
     * <p><strong>User Experience:</strong> Displays company name in welcome message and
     * provides clear menu structure with numeric choices.
     * 
     * @see #displayPendingMenu()
     */
    @Override
    public void displayMenu() {
        // Check if approved
        if (!"Approved".equals(representative.getStatus())) {
            displayPendingMenu();
            return;
        }
        
        while (true) {
            System.out.println("\n=== Company Representative Menu ===");
            System.out.println("Welcome, " + representative.getName() + " (" + representative.getCompanyName() + ")");
            System.out.println("1. Create Internship Opportunity");
            System.out.println("2. View My Internship Opportunities");
            System.out.println("3. View Applications");
            System.out.println("4. Approve/Reject Application");
            System.out.println("5. Toggle Internship Visibility");
            System.out.println("6. Update Filter Settings");
            System.out.println("7. Change Password");
            System.out.println("8. Logout");
            System.out.print("Choose an option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        createInternshipOpportunity();
                        break;
                    case 2:
                        viewMyOpportunities();
                        break;
                    case 3:
                        viewApplications();
                        break;
                    case 4:
                        approveRejectApplication();
                        break;
                    case 5:
                        toggleVisibility();
                        break;
                    case 6:
                        updateFilter();
                        break;
                    case 7:
                        promptChangePassword();
                        break;
                    case 8:
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
     * Displays restricted menu for company representatives with pending approval status.
     * <p>
     * When a company representative's account is awaiting approval from Career Center Staff,
     * they have limited access to the system. This menu only allows:
     * <ul>
     *   <li>Change Password - Update account credentials</li>
     *   <li>Logout - Exit the system</li>
     * </ul>
     * 
     * <p><strong>User Feedback:</strong> Displays clear message indicating the account is
     * pending approval and shows current status. This helps users understand why they have
     * limited access.
     * 
     * <p><strong>Business Rule:</strong> Representatives must be approved by staff before
     * they can create internship opportunities or perform other operations.
     */
    private void displayPendingMenu() {
        while (true) {
            System.out.println("\n=== Company Representative Menu ===");
            System.out.println("Your account is pending approval by Career Center Staff.");
            System.out.println("Status: " + representative.getStatus());
            System.out.println("1. Change Password");
            System.out.println("2. Logout");
            System.out.print("Choose an option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        promptChangePassword();
                        break;
                    case 2:
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
     * Guides company representative through creating a new internship opportunity.
     * <p>
     * This comprehensive method implements a multi-step creation process with extensive
     * validation and error handling:
     * 
     * <p><strong>Input Fields with Validation:</strong>
     * <ol>
     *   <li><strong>Title:</strong> Required, cannot be empty</li>
     *   <li><strong>Description:</strong> Required, cannot be empty</li>
     *   <li><strong>Level:</strong> Must be Basic, Intermediate, or Advanced</li>
     *   <li><strong>Preferred Major:</strong> Optional field</li>
     *   <li><strong>Opening Date:</strong> Must be today or future date (YYYY-MM-DD format)</li>
     *   <li><strong>Closing Date:</strong> Must be after opening date and not in the past</li>
     *   <li><strong>Number of Slots:</strong> Integer between 1-10</li>
     * </ol>
     * 
     * <p><strong>Business Rules:</strong>
     * <ul>
     *   <li>Representatives can create maximum 5 internship opportunities</li>
     *   <li>All new internships start with "Pending" status awaiting staff approval</li>
     *   <li>Company name and representative ID are automatically assigned</li>
     * </ul>
     * 
     * <p><strong>Validation Loop Pattern:</strong> Each field uses a validation loop that:
     * <ul>
     *   <li>Prompts for input</li>
     *   <li>Allows cancellation with 'q' input</li>
     *   <li>Validates input against business rules</li>
     *   <li>Re-prompts with error message if validation fails</li>
     *   <li>Continues until valid input received</li>
     * </ul>
     * 
     * <p><strong>User Experience:</strong> Clear instructions, error messages, and ability
     * to cancel at any step. Upon successful creation, displays confirmation with pending
     * approval notice.
     * 
     * @see InternshipManager#addInternship(InternshipOpportunity)
     */
    public void createInternshipOpportunity() {
        // Check if limit reached
        if (systemManager.getInternshipManager().hasReachedCreationLimit(representative.getUserID())) {
            System.out.println("You have reached the maximum limit of 5 internship opportunities.");
            return;
        }
        
        System.out.println("\n=== Create Internship Opportunity ===");
        System.out.println("(Enter 'q' at any prompt to cancel)");
        
        // Title validation with loop
        String title = null;
        while (title == null) {
            System.out.print("Enter Internship Title: ");
            String input = scanner.nextLine().trim();
            
            if ("q".equalsIgnoreCase(input)) {
                System.out.println("Internship creation cancelled.");
                return;
            }
            
            if (input.isEmpty()) {
                System.out.println("Title cannot be empty. Please re-enter.");
            } else {
                title = input;
            }
        }
        
        // Description validation with loop
        String description = null;
        while (description == null) {
            System.out.print("Enter Description: ");
            String input = scanner.nextLine().trim();
            
            if ("q".equalsIgnoreCase(input)) {
                System.out.println("Internship creation cancelled.");
                return;
            }
            
            if (input.isEmpty()) {
                System.out.println("Description cannot be empty. Please re-enter.");
            } else {
                description = input;
            }
        }
        
        // Level selection with validation loop
        String level = "";
        while (level.isEmpty()) {
            System.out.println("Select Level:");
            System.out.println("1. Basic");
            System.out.println("2. Intermediate");
            System.out.println("3. Advanced");
            System.out.print("Choice: ");
            String input = scanner.nextLine().trim();
            
            if ("q".equalsIgnoreCase(input)) {
                System.out.println("Internship creation cancelled.");
                return;
            }
            
            try {
                int levelChoice = Integer.parseInt(input);
                switch (levelChoice) {
                    case 1: level = "Basic"; break;
                    case 2: level = "Intermediate"; break;
                    case 3: level = "Advanced"; break;
                    default:
                        System.out.println("Invalid choice! Please enter 1, 2, or 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number (1, 2, or 3).");
            }
        }
        
        // Preferred major with quit option
        System.out.print("Enter Preferred Major: ");
        String preferredMajor = scanner.nextLine().trim();
        
        if ("q".equalsIgnoreCase(preferredMajor)) {
            System.out.println("Internship creation cancelled.");
            return;
        }
        
        // Opening date validation with loop
        LocalDate openingDate = null;
        LocalDate today = LocalDate.now();
        while (openingDate == null) {
            System.out.print("Enter Opening Date (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();
            
            if ("q".equalsIgnoreCase(input)) {
                System.out.println("Internship creation cancelled.");
                return;
            }
            
            try {
                LocalDate inputDate = LocalDate.parse(input);
                if (inputDate.isBefore(today)) {
                    System.out.println("Opening date cannot be before today (" + today + "). Please re-enter.");
                } else {
                    openingDate = inputDate;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Please use YYYY-MM-DD format.");
            }
        }
        
        // Closing date validation with loop
        LocalDate closingDate = null;
        while (closingDate == null) {
            System.out.print("Enter Closing Date (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();
            
            if ("q".equalsIgnoreCase(input)) {
                System.out.println("Internship creation cancelled.");
                return;
            }
            
            try {
                LocalDate inputDate = LocalDate.parse(input);
                if (inputDate.isBefore(today)) {
                    System.out.println("Closing date cannot be before today (" + today + "). Please re-enter.");
                } else if (inputDate.isBefore(openingDate) || inputDate.isEqual(openingDate)) {
                    System.out.println("Closing date must be after opening date (" + openingDate + "). Please re-enter.");
                } else {
                    closingDate = inputDate;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Please use YYYY-MM-DD format.");
            }
        }
        
        // Slots validation with loop
        int slots = 0;
        while (slots == 0) {
            System.out.print("Enter Number of Slots (1-10): ");
            String input = scanner.nextLine().trim();
            
            if ("q".equalsIgnoreCase(input)) {
                System.out.println("Internship creation cancelled.");
                return;
            }
            
            try {
                int inputSlots = Integer.parseInt(input);
                if (inputSlots < 1) {
                    System.out.println("Number of slots must be at least 1. Please re-enter.");
                } else if (inputSlots > 10) {
                    System.out.println("Maximum 10 slots allowed. Please re-enter.");
                } else {
                    slots = inputSlots;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number! Please enter a number between 1 and 10.");
            }
        }
        
        InternshipOpportunity opportunity = new InternshipOpportunity(
            title, description, level, preferredMajor, openingDate, closingDate,
            representative.getCompanyName(), representative.getUserID(), slots
        );
        
        systemManager.getInternshipManager().addInternship(opportunity);
        System.out.println("\nInternship opportunity created successfully!");
        System.out.println("Status: Pending (awaiting Career Center Staff approval)");
    }
    
    /**
     * Displays all internship opportunities created by this company representative.
     * <p>
     * Shows comprehensive information about each posted internship including:
     * <ul>
     *   <li>Title, level, and preferred major</li>
     *   <li>Status (Pending, Approved, Rejected, Filled)</li>
     *   <li>Visibility setting (Visible/Hidden to students)</li>
     *   <li>Maximum slots and opening/closing dates</li>
     *   <li>Application statistics (total applications and filled slots)</li>
     * </ul>
     * 
     * <p><strong>Filter Support:</strong> If the representative has configured filter
     * criteria, only internships matching those criteria are displayed. Otherwise,
     * all internships created by this representative are shown.
     * 
     * <p><strong>Slot Tracking:</strong> For each internship, displays:
     * <ul>
     *   <li>Total number of applications received</li>
     *   <li>Number of slots filled (Successful + Accepted applications)</li>
     *   <li>Slots available (calculated from max slots)</li>
     * </ul>
     * 
     * <p><strong>User Feedback:</strong> If no internships exist or match filters,
     * displays appropriate informative message.
     * 
     * @see InternshipManager#getInternshipsByRepresentative(String)
     * @see ApplicationManager#getApplicationsByInternship(InternshipOpportunity)
     */
    public void viewMyOpportunities() {
        List<InternshipOpportunity> allOpportunities = systemManager.getInternshipManager()
            .getInternshipsByRepresentative(representative.getUserID());
        
        // Apply filter if criteria is set
        List<InternshipOpportunity> opportunities;
        if (representative.getFilterCriteria() != null) {
            opportunities = systemManager.getInternshipManager()
                .filterInternships(representative.getFilterCriteria()).stream()
                .filter(opp -> opp.getCompanyRepID().equals(representative.getUserID()))
                .collect(java.util.stream.Collectors.toList());
        } else {
            opportunities = allOpportunities;
        }
        
        System.out.println("\n=== My Internship Opportunities ===");
        if (opportunities.isEmpty()) {
            if (allOpportunities.isEmpty()) {
                System.out.println("You have not created any internship opportunities.");
            } else {
                System.out.println("No internship opportunities match your current filter.");
            }
        } else {
            for (int i = 0; i < opportunities.size(); i++) {
                InternshipOpportunity opp = opportunities.get(i);
                System.out.println("\n" + (i + 1) + ". " + opp.getTitle());
                System.out.println("   Level: " + opp.getLevel());
                System.out.println("   Preferred Major: " + opp.getPreferredMajor());
                System.out.println("   Status: " + opp.getStatus());
                System.out.println("   Visible: " + opp.getVisibility());
                System.out.println("   Max Slots: " + opp.getNumSlots());
                System.out.println("   Opening: " + opp.getOpeningDate());
                System.out.println("   Closing: " + opp.getClosingDate());
                
                // Show application count and slots taken
                List<Application> applications = systemManager.getApplicationManager()
                    .getApplicationsByInternship(opp);
                long appCount = applications.size();
                long slotsTaken = applications.stream()
                    .filter(app -> "Successful".equals(app.getStatus()) || "Accepted".equals(app.getStatus()))
                    .count();
                System.out.println("   Applications: " + appCount);
                System.out.println("   Slots: " + slotsTaken + " / " + opp.getNumSlots() + " filled");
            }
        }
    }
    
    /**
     * Displays all student applications for this representative's internship opportunities.
     * <p>
     * Groups applications by internship opportunity and shows detailed student information:
     * <ul>
     *   <li>Student name and user ID</li>
     *   <li>Year of study and major</li>
     *   <li>Application status</li>
     * </ul>
     * 
     * <p><strong>Organization:</strong> Applications are grouped under their respective
     * internship titles for easy review and management.
     * 
     * <p><strong>Display Logic:</strong>
     * <ul>
     *   <li>Only shows internships that have received applications</li>
     *   <li>Displays numbered list for easy reference</li>
     *   <li>Shows clear message if no applications received</li>
     * </ul>
     * 
     * <p><strong>Use Case:</strong> Allows representatives to quickly review all pending
     * and processed applications across their internship postings before making decisions
     * in the approve/reject flow.
     * 
     * @see ApplicationManager#getApplicationsByInternship(InternshipOpportunity)
     */
    public void viewApplications() {
        List<InternshipOpportunity> opportunities = systemManager.getInternshipManager()
            .getInternshipsByRepresentative(representative.getUserID());
        
        System.out.println("\n=== Applications for My Opportunities ===");
        boolean hasApplications = false;
        
        for (InternshipOpportunity opp : opportunities) {
            List<Application> applications = systemManager.getApplicationManager()
                .getApplicationsByInternship(opp);
            
            if (!applications.isEmpty()) {
                System.out.println("\n" + opp.getTitle() + ":");
                for (int i = 0; i < applications.size(); i++) {
                    Application app = applications.get(i);
                    Student student = app.getStudent();
                    System.out.println("  " + (i + 1) + ". " + student.getName() + 
                                     " (ID: " + student.getUserID() + ")");
                    System.out.println("     Year: " + student.getYearOfStudy() + 
                                     ", Major: " + student.getMajor());
                    System.out.println("     Status: " + app.getStatus());
                }
                hasApplications = true;
            }
        }
        
        if (!hasApplications) {
            System.out.println("No applications received yet.");
        }
    }
    
    /**
     * Guides representative through approving or rejecting student applications.
     * <p>
     * This method implements a multi-step selection process:
     * <ol>
     *   <li>Display internships with pending applications</li>
     *   <li>Representative selects an internship</li>
     *   <li>Display pending applications for selected internship</li>
     *   <li>Representative selects an application</li>
     *   <li>Representative chooses to approve (Successful) or reject (Unsuccessful)</li>
     *   <li>System updates application status accordingly</li>
     * </ol>
     * 
     * <p><strong>Application Filtering:</strong> Only shows applications with non-finalized
     * status. Finalized applications (Successful, Unsuccessful, Accepted) cannot be modified
     * to maintain data integrity.
     * 
     * <p><strong>Slot Management:</strong> When approving applications:
     * <ul>
     *   <li>Verifies available slots before approval</li>
     *   <li>Prevents approval if all slots are filled</li>
     *   <li>Displays current slot usage (e.g., "3 / 5 filled")</li>
     *   <li>Counts both Successful and Accepted applications as filled slots</li>
     * </ul>
     * 
     * <p><strong>Validation:</strong>
     * <ul>
     *   <li>Ensures selection is within valid range</li>
     *   <li>Allows cancellation with 0 input at any step</li>
     *   <li>Prevents modification of finalized applications</li>
     *   <li>Handles invalid numeric input gracefully</li>
     * </ul>
     * 
     * <p><strong>User Feedback:</strong> Displays clear confirmation messages and slot
     * usage information after each decision.
     * 
     * @see ApplicationManager#updateApplicationStatus(Application, String)
     */
    public void approveRejectApplication() {
        List<InternshipOpportunity> opportunities = systemManager.getInternshipManager()
            .getInternshipsByRepresentative(representative.getUserID());
        
        // Build list of opportunities with applications
        List<InternshipOpportunity> oppsWithApps = new ArrayList<>();
        System.out.println("\n=== Approve/Reject Applications ===");
        
        for (InternshipOpportunity opp : opportunities) {
            List<Application> applications = systemManager.getApplicationManager()
                .getApplicationsByInternship(opp);
            
            // Filter out finalized applications (Successful, Unsuccessful, Accepted)
            List<Application> pendingApplications = applications.stream()
                .filter(app -> !"Successful".equals(app.getStatus()) 
                            && !"Unsuccessful".equals(app.getStatus())
                            && !"Accepted".equals(app.getStatus()))
                .collect(java.util.stream.Collectors.toList());
            
            if (!pendingApplications.isEmpty()) {
                oppsWithApps.add(opp);
            }
        }
        
        if (oppsWithApps.isEmpty()) {
            System.out.println("No applications to approve/reject.");
            return;
        }
        
        // Display numbered list of internships with applications
        System.out.println("\nSelect an internship:");
        for (int i = 0; i < oppsWithApps.size(); i++) {
            InternshipOpportunity opp = oppsWithApps.get(i);
            // Count only pending applications that can be approved/rejected
            long pendingCount = systemManager.getApplicationManager()
                .getApplicationsByInternship(opp).stream()
                .filter(app -> !"Successful".equals(app.getStatus()) 
                            && !"Unsuccessful".equals(app.getStatus())
                            && !"Accepted".equals(app.getStatus()))
                .count();
            System.out.println((i + 1) + ". " + opp.getTitle() + " (" + pendingCount + " pending application" + (pendingCount != 1 ? "s" : "") + ")");
        }
        
        System.out.print("\nEnter number (or 0 to cancel): ");
        int oppChoice;
        try {
            oppChoice = Integer.parseInt(scanner.nextLine());
            if (oppChoice == 0) return;
            
            if (oppChoice < 1 || oppChoice > oppsWithApps.size()) {
                System.out.println("Invalid selection!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
            return;
        }
        
        InternshipOpportunity selectedOpp = oppsWithApps.get(oppChoice - 1);
        List<Application> allApplications = systemManager.getApplicationManager()
            .getApplicationsByInternship(selectedOpp);
        
        // Filter out finalized applications
        List<Application> applications = allApplications.stream()
            .filter(app -> !"Successful".equals(app.getStatus()) 
                        && !"Unsuccessful".equals(app.getStatus())
                        && !"Accepted".equals(app.getStatus()))
            .collect(java.util.stream.Collectors.toList());
        
        if (applications.isEmpty()) {
            System.out.println("No pending applications for this internship.");
            return;
        }
        
        // Display numbered list of applications
        System.out.println("\nApplications for: " + selectedOpp.getTitle());
        for (int i = 0; i < applications.size(); i++) {
            Application app = applications.get(i);
            Student student = app.getStudent();
            System.out.println((i + 1) + ". " + student.getName() + " (ID: " + student.getUserID() + ")");
            System.out.println("   Year: " + student.getYearOfStudy() + ", Major: " + student.getMajor());
            System.out.println("   Status: " + app.getStatus());
        }
        
        System.out.print("\nEnter application number (or 0 to cancel): ");
        int appChoice;
        try {
            appChoice = Integer.parseInt(scanner.nextLine());
            if (appChoice == 0) return;
            
            if (appChoice < 1 || appChoice > applications.size()) {
                System.out.println("Invalid selection!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
            return;
        }
        
        Application selectedApp = applications.get(appChoice - 1);
        
        // Check if application is already finalized
        if ("Successful".equals(selectedApp.getStatus()) || "Unsuccessful".equals(selectedApp.getStatus()) || "Accepted".equals(selectedApp.getStatus())) {
            System.out.println("\nThis application has already been finalized as: " + selectedApp.getStatus());
            System.out.println("Cannot change the status of a finalized application.");
            return;
        }
        
        System.out.println("\n1. Approve (Successful)");
        System.out.println("2. Reject (Unsuccessful)");
        System.out.print("Choice: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) {
                // Check if there are available slots before approving
                long successfulCount = allApplications.stream()
                    .filter(app -> "Successful".equals(app.getStatus()) || "Accepted".equals(app.getStatus()))
                    .count();
                
                if (successfulCount >= selectedOpp.getNumSlots()) {
                    System.out.println("Cannot approve: All slots (" + selectedOpp.getNumSlots() + ") for this internship are already filled.");
                    System.out.println("Current approved/accepted applications: " + successfulCount);
                    return;
                }
                
                systemManager.getApplicationManager().updateApplicationStatus(selectedApp, "Successful");
                System.out.println("Application approved successfully!");
                System.out.println("Slots used: " + (successfulCount + 1) + " / " + selectedOpp.getNumSlots());
            } else if (choice == 2) {
                systemManager.getApplicationManager().updateApplicationStatus(selectedApp, "Unsuccessful");
                System.out.println("Application rejected.");
            } else {
                System.out.println("Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }
    
    /**
     * Toggles the visibility status of an internship opportunity.
     * <p>
     * This method allows representatives to control whether approved internships are
     * visible to students. The workflow:
     * <ol>
     *   <li>Displays all internships created by representative</li>
     *   <li>Prompts for selection by number</li>
     *   <li>Validates that internship is approved (only approved can toggle)</li>
     *   <li>Toggles visibility status (Visible ↔ Hidden)</li>
     *   <li>Displays new visibility status</li>
     * </ol>
     * 
     * <p><strong>Business Rules:</strong>
     * <ul>
     *   <li>Only approved internships can have visibility toggled</li>
     *   <li>Pending or rejected internships remain hidden regardless of setting</li>
     *   <li>Visibility affects whether students can see and apply to the internship</li>
     * </ul>
     * 
     * <p><strong>Validation:</strong> Ensures selection is valid and within range,
     * allows cancellation with 0 input.
     * 
     * <p><strong>Use Case:</strong> Representatives can temporarily hide internships
     * (e.g., while reviewing applications) without deleting them.
     * 
     * @see InternshipManager#toggleVisibility(InternshipOpportunity)
     */
    public void toggleVisibility() {
        viewMyOpportunities();
        
        List<InternshipOpportunity> opportunities = systemManager.getInternshipManager()
            .getInternshipsByRepresentative(representative.getUserID());
        
        if (opportunities.isEmpty()) {
            return;
        }
        
        System.out.print("\nEnter opportunity number to toggle visibility (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;
            
            if (choice < 1 || choice > opportunities.size()) {
                System.out.println("Invalid selection!");
                return;
            }
            
            InternshipOpportunity selected = opportunities.get(choice - 1);
            
            if (!"Approved".equals(selected.getStatus())) {
                System.out.println("Can only toggle visibility of approved internships.");
                return;
            }
            
            systemManager.getInternshipManager().toggleVisibility(selected);
            System.out.println("Visibility toggled. Current visibility: " + selected.getVisibility());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }
    
    /**
     * Checks if a field can be edited by company representatives.
     * <p>
     * Company representatives have permission to edit their own internship postings,
     * including modifying details and managing visibility settings.
     * 
     * <p><strong>Authorization:</strong> This method enforces that representatives can
     * only modify their own internship opportunities, not system data or other users'
     * content.
     * 
     * @param fieldName the name of the field to check for edit permissions
     * @return true if fieldName is "internship" (case-insensitive), false otherwise
     */
    @Override
    public boolean canEditField(String fieldName) {
        // Company reps can edit their internships
        return "internship".equalsIgnoreCase(fieldName);
    }
}
