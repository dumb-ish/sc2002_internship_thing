import java.util.List;

/**
 * CLI Boundary for Career Center Staff users.
 * Handles all staff-specific menu operations including approvals and reports.
 */
public class CLIStaffBoundary extends CLIUserBoundary {
    private CareerCenterStaff staff;
    
    /**
     * Constructor
     */
    public CLIStaffBoundary(SystemManager systemManager, User currentUser) {
        super(systemManager, currentUser);
        this.staff = (CareerCenterStaff) currentUser;
    }
    
    /**
     * Display staff menu
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
     * Approve or reject company representatives
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
     * Approve or reject internship opportunities
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
     * Approve or reject withdrawal requests
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
                systemManager.getApplicationManager().approveWithdrawal(selected);
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
     * Generate comprehensive reports
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
                    System.out.print("Enter status (Pending/Approved/Rejected/Filled): ");
                    criteria.setStatus(scanner.nextLine());
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
    
    @Override
    public boolean canEditField(String fieldName) {
        // Staff can approve/reject all elements
        return true;
    }
}
