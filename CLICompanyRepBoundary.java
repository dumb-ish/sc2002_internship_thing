import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * CLI Boundary for Company Representative users.
 * Handles all company representative-specific menu operations.
 */
public class CLICompanyRepBoundary extends CLIUserBoundary {
    private CompanyRepresentative representative;
    
    /**
     * Constructor
     */
    public CLICompanyRepBoundary(SystemManager systemManager, User currentUser) {
        super(systemManager, currentUser);
        this.representative = (CompanyRepresentative) currentUser;
    }
    
    /**
     * Display company representative menu
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
     * Display menu when account is pending approval
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
     * Create a new internship opportunity
     */
    public void createInternshipOpportunity() {
        // Check if limit reached
        if (systemManager.getInternshipManager().hasReachedCreationLimit(representative.getUserID())) {
            System.out.println("You have reached the maximum limit of 5 internship opportunities.");
            waitForEnter();
            return;
        }
        
        System.out.println("\n=== Create Internship Opportunity ===");
        
        System.out.print("Enter Internship Title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();
        
        System.out.println("Select Level:");
        System.out.println("1. Basic");
        System.out.println("2. Intermediate");
        System.out.println("3. Advanced");
        System.out.print("Choice: ");
        String level = "";
        try {
            int levelChoice = Integer.parseInt(scanner.nextLine());
            switch (levelChoice) {
                case 1: level = "Basic"; break;
                case 2: level = "Intermediate"; break;
                case 3: level = "Advanced"; break;
                default:
                    System.out.println("Invalid choice! Defaulting to Basic.");
                    level = "Basic";
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Defaulting to Basic.");
            level = "Basic";
        }
        
        System.out.print("Enter Preferred Major: ");
        String preferredMajor = scanner.nextLine();
        
        System.out.print("Enter Opening Date (YYYY-MM-DD): ");
        LocalDate openingDate;
        try {
            openingDate = LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format! Using today's date.");
            openingDate = LocalDate.now();
        }
        
        System.out.print("Enter Closing Date (YYYY-MM-DD): ");
        LocalDate closingDate;
        try {
            closingDate = LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format! Using one month from today.");
            closingDate = LocalDate.now().plusMonths(1);
        }
        
        System.out.print("Enter Number of Slots (max 10): ");
        int slots;
        try {
            slots = Integer.parseInt(scanner.nextLine());
            if (slots > 10) {
                System.out.println("Maximum 10 slots allowed. Setting to 10.");
                slots = 10;
            }
            if (slots < 1) {
                System.out.println("Minimum 1 slot required. Setting to 1.");
                slots = 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number! Setting to 1 slot.");
            slots = 1;
        }
        
        InternshipOpportunity opportunity = new InternshipOpportunity(
            title, description, level, preferredMajor, openingDate, closingDate,
            representative.getCompanyName(), representative.getUserID(), slots
        );
        
        systemManager.getInternshipManager().addInternship(opportunity);
        System.out.println("\nInternship opportunity created successfully!");
        System.out.println("Status: Pending (awaiting Career Center Staff approval)");
        waitForEnter();
    }
    
    /**
     * View internships created by this representative
     */
    public void viewMyOpportunities() {
        List<InternshipOpportunity> opportunities = systemManager.getInternshipManager()
            .getInternshipsByRepresentative(representative.getUserID());
        
        System.out.println("\n=== My Internship Opportunities ===");
        if (opportunities.isEmpty()) {
            System.out.println("You have not created any internship opportunities.");
        } else {
            for (int i = 0; i < opportunities.size(); i++) {
                InternshipOpportunity opp = opportunities.get(i);
                System.out.println("\n" + (i + 1) + ". " + opp.getTitle());
                System.out.println("   Level: " + opp.getLevel());
                System.out.println("   Status: " + opp.getStatus());
                System.out.println("   Visible: " + opp.getVisibility());
                System.out.println("   Slots: " + opp.getNumSlots());
                System.out.println("   Opening: " + opp.getOpeningDate());
                System.out.println("   Closing: " + opp.getClosingDate());
                
                // Show application count
                long appCount = systemManager.getApplicationManager()
                    .getApplicationsByInternship(opp).size();
                System.out.println("   Applications: " + appCount);
            }
        }
        waitForEnter();
    }
    
    /**
     * View applications for representative's opportunities
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
        waitForEnter();
    }
    
    /**
     * Approve or reject an application
     */
    public void approveRejectApplication() {
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
            waitForEnter();
            return;
        }
        
        System.out.print("\nEnter internship title: ");
        String title = scanner.nextLine();
        
        InternshipOpportunity selectedOpp = null;
        for (InternshipOpportunity opp : opportunities) {
            if (opp.getTitle().equalsIgnoreCase(title)) {
                selectedOpp = opp;
                break;
            }
        }
        
        if (selectedOpp == null) {
            System.out.println("Internship not found or you don't have permission.");
            waitForEnter();
            return;
        }
        
        List<Application> applications = systemManager.getApplicationManager()
            .getApplicationsByInternship(selectedOpp);
        
        if (applications.isEmpty()) {
            System.out.println("No applications for this internship.");
            waitForEnter();
            return;
        }
        
        System.out.print("Enter student ID: ");
        String studentID = scanner.nextLine();
        
        Application selectedApp = null;
        for (Application app : applications) {
            if (app.getStudent().getUserID().equals(studentID)) {
                selectedApp = app;
                break;
            }
        }
        
        if (selectedApp == null) {
            System.out.println("Application not found.");
            waitForEnter();
            return;
        }
        
        System.out.println("\n1. Approve (Successful)");
        System.out.println("2. Reject (Unsuccessful)");
        System.out.print("Choice: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) {
                systemManager.getApplicationManager().updateApplicationStatus(selectedApp, "Successful");
                System.out.println("Application approved successfully!");
            } else if (choice == 2) {
                systemManager.getApplicationManager().updateApplicationStatus(selectedApp, "Unsuccessful");
                System.out.println("Application rejected.");
            } else {
                System.out.println("Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
        waitForEnter();
    }
    
    /**
     * Toggle visibility of an internship
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
        waitForEnter();
    }
    
    @Override
    public boolean canEditField(String fieldName) {
        // Company reps can edit their internships
        return "internship".equalsIgnoreCase(fieldName);
    }
}
