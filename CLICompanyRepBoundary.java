import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
     * View internships created by this representative
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
    }
    
    /**
     * Approve or reject an application
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
            
            if (!applications.isEmpty()) {
                oppsWithApps.add(opp);
            }
        }
        
        if (oppsWithApps.isEmpty()) {
            System.out.println("No applications received yet.");
            return;
        }
        
        // Display numbered list of internships with applications
        System.out.println("\nSelect an internship:");
        for (int i = 0; i < oppsWithApps.size(); i++) {
            InternshipOpportunity opp = oppsWithApps.get(i);
            long appCount = systemManager.getApplicationManager()
                .getApplicationsByInternship(opp).size();
            System.out.println((i + 1) + ". " + opp.getTitle() + " (" + appCount + " applications)");
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
        List<Application> applications = systemManager.getApplicationManager()
            .getApplicationsByInternship(selectedOpp);
        
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
        if ("Successful".equals(selectedApp.getStatus()) || "Unsuccessful".equals(selectedApp.getStatus())) {
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
    }
    
    @Override
    public boolean canEditField(String fieldName) {
        // Company reps can edit their internships
        return "internship".equalsIgnoreCase(fieldName);
    }
}
