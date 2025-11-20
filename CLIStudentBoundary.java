import java.util.List;

/**
 * CLI Boundary for Student users.
 * Handles all student-specific menu operations and interactions.
 */
public class CLIStudentBoundary extends CLIUserBoundary {
    private Student student;
    
    /**
     * Constructor
     */
    public CLIStudentBoundary(SystemManager systemManager, User currentUser) {
        super(systemManager, currentUser);
        this.student = (Student) currentUser;
    }
    
    /**
     * Display student menu and handle user choices
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
     * View available internship opportunities
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
     * Apply for an internship
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
     * View student's applications
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
     * Accept an internship placement
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
     * Request withdrawal from an application
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
    
    @Override
    public void updateFilter() {
        FilterBoundary filterBoundary = new FilterBoundary();
        filterBoundary.displayStudentFilterMenu(this, currentUser.getFilterCriteria(), student);
    }
    
    @Override
    public boolean canEditField(String fieldName) {
        // Students can only edit filter settings
        return "filter".equalsIgnoreCase(fieldName);
    }
}
