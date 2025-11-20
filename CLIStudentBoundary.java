import java.util.List;

/**
 * Command-line interface boundary for Student users in the View layer (MVC architecture).
 * <p>
 * This class provides the student-specific user interface, handling all interactions for
 * student users including viewing internships, applying, managing applications, and accepting
 * placements. It extends {@link CLIUserBoundary} to inherit common functionality while
 * implementing student-specific operations.
 * 
 * <p><strong>Student Operations:</strong>
 * <ul>
 *   <li>View available internship opportunities (filtered by eligibility and preferences)</li>
 *   <li>Apply for internships</li>
 *   <li>View and track application status</li>
 *   <li>Accept internship placements</li>
 *   <li>Request withdrawal from applications</li>
 *   <li>Configure filter settings (with year-based restrictions)</li>
 * </ul>
 * 
 * <p><strong>Business Rules Enforced:</strong>
 * <ul>
 *   <li>Students in Year 1-2 can only see Basic level internships</li>
 *   <li>Students can only see internships matching their major</li>
 *   <li>Only approved internships are visible to students</li>
 *   <li>Students can only apply to open opportunities with available slots</li>
 *   <li>Accepting a placement automatically withdraws other applications</li>
 * </ul>
 * 
 * <p><strong>MVC Role:</strong> View layer - handles presentation and user input validation,
 * delegates business logic to {@link ApplicationManager} and {@link InternshipManager}.
 * 
 * @see CLIUserBoundary
 * @see Student
 * @see ApplicationManager
 * @see InternshipManager
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class CLIStudentBoundary extends CLIUserBoundary {
    /** Reference to the student user for type-specific operations */
    private Student student;
    
    /**
     * Constructs a student boundary for the specified student user.
     * <p>
     * Initializes the boundary with system manager context and casts the user to
     * Student type for accessing student-specific attributes and methods.
     * 
     * @param systemManager the system manager for accessing application and internship managers
     * @param currentUser the currently logged-in user (must be a Student)
     */
    public CLIStudentBoundary(SystemManager systemManager, User currentUser) {
        super(systemManager, currentUser);
        this.student = (Student) currentUser;
    }
    
    /**
     * Displays the student menu and handles user interaction loop.
     * <p>
     * Presents a menu of student-specific operations and processes user choices.
     * The menu continues to display until the user chooses to logout. Each option
     * is validated and delegated to the appropriate handler method.
     * 
     * <p><strong>Menu Options:</strong>
     * <ol>
     *   <li>View Internship Opportunities - Browse available, eligible internships</li>
     *   <li>Apply for Internship - Submit application for selected opportunity</li>
     *   <li>View My Applications - Check status of submitted applications</li>
     *   <li>Accept Internship Placement - Accept a successful placement offer</li>
     *   <li>Request Withdrawal - Request to withdraw from an application</li>
     *   <li>Update Filter Settings - Customize internship visibility criteria</li>
     *   <li>Change Password - Update account password</li>
     *   <li>Logout - End session and return to login screen</li>
     * </ol>
     * 
     * <p><strong>Error Handling:</strong> Catches NumberFormatException for invalid input
     * and prompts user to enter valid numeric choices.
     */
    @Override
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== Student Menu ===");
            System.out.println("Welcome, " + student.getName());
            System.out.println("1. View Internship Opportunities");
            System.out.println("2. Apply for Internship");
            System.out.println("3. View My Applications");
            System.out.println("4. Accept Internship Placement");
            System.out.println("5. Request Withdrawal");
            System.out.println("6. Update Filter Settings");
            System.out.println("7. Change Password");
            System.out.println("8. Logout");
            System.out.print("Choose an option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        viewInternships();
                        break;
                    case 2:
                        applyForInternship();
                        break;
                    case 3:
                        viewApplications();
                        break;
                    case 4:
                        acceptInternship();
                        break;
                    case 5:
                        requestWithdrawal();
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
     * Displays available internship opportunities matching student eligibility and filters.
     * <p>
     * Retrieves and displays internships that the student is eligible to view based on:
     * <ul>
     *   <li>Student's year of study (Year 1-2 see only Basic level)</li>
     *   <li>Student's major (only matching majors shown)</li>
     *   <li>Internship status (only Approved internships)</li>
     *   <li>Student's filter criteria (if set)</li>
     * </ul>
     * 
     * <p><strong>Display Format:</strong> Shows numbered list with details including title,
     * company, level, major, description, dates, and available slots.
     * 
     * <p><strong>User Experience:</strong> If no opportunities match criteria, displays
     * helpful message explaining no matches were found.
     */
    public void viewInternships() {
        List<InternshipOpportunity> opportunities = systemManager.getInternshipManager()
            .getVisibleInternshipsForStudent(student, student.getFilterCriteria());
        
        System.out.println("\n=== Available Internship Opportunities ===");
        if (opportunities.isEmpty()) {
            System.out.println("No opportunities available matching your criteria.");
        } else {
            for (int i = 0; i < opportunities.size(); i++) {
                InternshipOpportunity opp = opportunities.get(i);
                System.out.println("\n" + (i + 1) + ". " + opp.getTitle());
                System.out.println("   Company: " + opp.getCompanyName());
                System.out.println("   Level: " + opp.getLevel());
                System.out.println("   Preferred Major: " + opp.getPreferredMajor());
                System.out.println("   Description: " + opp.getDescription());
                System.out.println("   Opening Date: " + opp.getOpeningDate());
                System.out.println("   Closing Date: " + opp.getClosingDate());
                System.out.println("   Slots: " + opp.getNumSlots());
            }
        }
    }
    
    /**
     * Guides student through the internship application process.
     * <p>
     * This method performs the following workflow:
     * <ol>
     *   <li>Displays available internship opportunities</li>
     *   <li>Prompts student to select an internship by number</li>
     *   <li>Validates the selection</li>
     *   <li>Submits application through {@link ApplicationManager}</li>
     *   <li>Displays confirmation or error message</li>
     * </ol>
     * 
     * <p><strong>Validation:</strong>
     * <ul>
     *   <li>Ensures selection is within valid range</li>
     *   <li>Allows cancellation with 0 input</li>
     *   <li>Handles invalid numeric input gracefully</li>
     * </ul>
     * 
     * <p><strong>Business Logic:</strong> Application submission validates that:
     * <ul>
     *   <li>Student hasn't already applied to this internship</li>
     *   <li>Internship has available slots</li>
     *   <li>Application deadline hasn't passed</li>
     * </ul>
     */
    public void applyForInternship() {
        List<InternshipOpportunity> opportunities = systemManager.getInternshipManager()
            .getVisibleInternshipsForStudent(student, student.getFilterCriteria());
        
        System.out.println("\n=== Available Internship Opportunities ===");
        if (opportunities.isEmpty()) {
            System.out.println("No opportunities available matching your criteria.");
            return;
        }
        
        for (int i = 0; i < opportunities.size(); i++) {
            InternshipOpportunity opp = opportunities.get(i);
            System.out.println("\n" + (i + 1) + ". " + opp.getTitle());
            System.out.println("   Company: " + opp.getCompanyName());
            System.out.println("   Level: " + opp.getLevel());
            System.out.println("   Preferred Major: " + opp.getPreferredMajor());
            System.out.println("   Description: " + opp.getDescription());
            System.out.println("   Opening Date: " + opp.getOpeningDate());
            System.out.println("   Closing Date: " + opp.getClosingDate());
            System.out.println("   Slots: " + opp.getNumSlots());
        }
        
        System.out.print("\nEnter the number of the internship to apply (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;
            
            if (choice < 1 || choice > opportunities.size()) {
                System.out.println("Invalid selection!");
                return;
            }
            
            InternshipOpportunity selected = opportunities.get(choice - 1);
            boolean success = systemManager.getApplicationManager().submitApplication(student, selected);
            
            if (success) {
                System.out.println("Application submitted successfully!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }
    
    /**
     * Displays all applications submitted by the student with their current status.
     * <p>
     * Shows a numbered list of all applications including:
     * <ul>
     *   <li>Internship title and company name</li>
     *   <li>Application status (Pending, Successful, Unsuccessful, Accepted, Withdrawn)</li>
     *   <li>Withdrawal request status (if applicable)</li>
     * </ul>
     * 
     * <p><strong>Status Values:</strong>
     * <ul>
     *   <li><em>Pending</em> - Awaiting company representative review</li>
     *   <li><em>Successful</em> - Approved by company, awaiting student acceptance</li>
     *   <li><em>Unsuccessful</em> - Rejected by company</li>
     *   <li><em>Accepted</em> - Student has accepted the placement</li>
     *   <li><em>Withdrawn</em> - Application withdrawn (after staff approval)</li>
     * </ul>
     * 
     * <p><strong>User Experience:</strong> If student has no applications, displays
     * informative message indicating empty application list.
     */
    public void viewApplications() {
        List<Application> applications = systemManager.getApplicationManager()
            .getApplicationsByStudent(student);
        
        System.out.println("\n=== My Applications ===");
        if (applications.isEmpty()) {
            System.out.println("You have no applications.");
        } else {
            for (int i = 0; i < applications.size(); i++) {
                Application app = applications.get(i);
                InternshipOpportunity opp = app.getInternship();
                System.out.println("\n" + (i + 1) + ". " + opp.getTitle());
                System.out.println("   Company: " + opp.getCompanyName());
                System.out.println("   Status: " + app.getStatus());
                System.out.println("   Withdrawal Requested: " + app.isWithdrawalRequested());
            }
        }
    }
    
    /**
     * Allows student to accept a successful internship placement offer.
     * <p>
     * This critical operation performs the following actions:
     * <ol>
     *   <li>Displays all applications with "Successful" status</li>
     *   <li>Prompts student to select which offer to accept</li>
     *   <li>Confirms acceptance through {@link ApplicationManager}</li>
     *   <li>Automatically withdraws all other pending/successful applications</li>
     *   <li>Updates internship slot availability if all slots are filled</li>
     * </ol>
     * 
     * <p><strong>Business Rules:</strong>
     * <ul>
     *   <li>Only "Successful" applications can be accepted</li>
     *   <li>Accepting one placement withdraws all other applications</li>
     *   <li>If internship reaches maximum slots, status updates to "Filled"</li>
     * </ul>
     * 
     * <p><strong>Validation:</strong> Verifies selection is valid and within range.
     * Allows cancellation with 0 input.
     * 
     * <p><strong>User Feedback:</strong> Provides clear confirmation messages and
     * informs student that other applications were withdrawn.
     */
    public void acceptInternship() {
        List<Application> applications = systemManager.getApplicationManager()
            .getApplicationsByStudent(student);
        
        List<Application> successful = applications.stream()
            .filter(app -> "Successful".equals(app.getStatus()))
            .toList();
        
        if (successful.isEmpty()) {
            System.out.println("You have no successful applications to accept.");
            return;
        }
        
        System.out.println("\n=== Successful Applications ===");
        for (int i = 0; i < successful.size(); i++) {
            Application app = successful.get(i);
            System.out.println((i + 1) + ". " + app.getInternship().getTitle() + 
                             " at " + app.getInternship().getCompanyName());
        }
        
        System.out.print("\nSelect application to accept (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;
            
            if (choice < 1 || choice > successful.size()) {
                System.out.println("Invalid selection!");
                return;
            }
            
            Application selected = successful.get(choice - 1);
            boolean success = systemManager.getApplicationManager()
                .acceptInternshipPlacement(student, selected);
            
            if (success) {
                System.out.println("Internship placement accepted successfully!");
                System.out.println("All other applications have been withdrawn.");
                
                // Update internship status if all slots are filled
                long acceptedCount = systemManager.getApplicationManager()
                    .getAcceptedCount(selected.getInternship());
                systemManager.getInternshipManager()
                    .updateFilledStatus(selected.getInternship(), acceptedCount);
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }
    
    /**
     * Submits a withdrawal request for a student application.
     * <p>
     * This method implements the withdrawal request workflow:
     * <ol>
     *   <li>Displays all student applications</li>
     *   <li>Prompts student to select application for withdrawal</li>
     *   <li>Validates that withdrawal hasn't already been requested</li>
     *   <li>Submits request to {@link ApplicationManager}</li>
     *   <li>Notifies student that request requires staff approval</li>
     * </ol>
     * 
     * <p><strong>Approval Process:</strong> Withdrawal requests must be approved by
     * Career Center Staff before the application is actually removed. This two-step
     * process allows staff oversight of student decisions.
     * 
     * <p><strong>Validation:</strong>
     * <ul>
     *   <li>Checks if withdrawal already requested for selected application</li>
     *   <li>Ensures selection is within valid range</li>
     *   <li>Allows cancellation with 0 input</li>
     * </ul>
     * 
     * <p><strong>Status Update:</strong> Sets withdrawal requested flag, but application
     * remains in system until staff approval.
     */
    public void requestWithdrawal() {
        viewApplications();
        
        List<Application> applications = systemManager.getApplicationManager()
            .getApplicationsByStudent(student);
        
        if (applications.isEmpty()) {
            return;
        }
        
        System.out.print("\nEnter application number to withdraw (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;
            
            if (choice < 1 || choice > applications.size()) {
                System.out.println("Invalid selection!");
                return;
            }
            
            Application selected = applications.get(choice - 1);
            
            if (selected.isWithdrawalRequested()) {
                System.out.println("Withdrawal already requested for this application.");
                return;
            }
            
            systemManager.getApplicationManager().handleWithdrawal(selected);
            System.out.println("Withdrawal request submitted successfully.");
            System.out.println("Awaiting approval from Career Center Staff.");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }
    
    /**
     * Updates filter criteria with student-specific restrictions.
     * <p>
     * Overrides parent method to use {@link FilterBoundary#displayStudentFilterMenu}
     * which enforces student-specific filter restrictions:
     * <ul>
     *   <li><strong>Level Filter:</strong> Locked to "Basic" for Year 1-2 students;
     *       Year 3+ can choose from Basic, Intermediate, Advanced</li>
     *   <li><strong>Major Filter:</strong> Locked to student's major (cannot be changed)</li>
     *   <li><strong>Status Filter:</strong> Locked to "Approved" (students only see approved internships)</li>
     *   <li><strong>Closing Date Filter:</strong> Always editable</li>
     * </ul>
     * 
     * <p><strong>User Experience:</strong> Filter menu clearly indicates which filters
     * are locked and provides explanations for restrictions.
     * 
     * @see FilterBoundary#displayStudentFilterMenu(IUserBoundary, FilterCriteria, Student)
     */
    @Override
    public void updateFilter() {
        FilterBoundary filterBoundary = new FilterBoundary();
        filterBoundary.displayStudentFilterMenu(this, currentUser.getFilterCriteria(), student);
    }
    
    /**
     * Checks if a field can be edited by students.
     * <p>
     * Students have limited edit permissions in the system. They can only modify
     * their own filter settings to customize their view of internship opportunities.
     * 
     * @param fieldName the name of the field to check for edit permissions
     * @return true only if fieldName is "filter" (case-insensitive), false otherwise
     */
    @Override
    public boolean canEditField(String fieldName) {
        // Students can only edit filter settings
        return "filter".equalsIgnoreCase(fieldName);
    }
}
