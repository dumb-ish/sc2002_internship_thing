import java.util.*;

class CareerCenterStaff extends User {
    private String department;
    
    public CareerCenterStaff(String userId, String name, String password, String department) {
        super(userId, name, password);
        this.role = "Career Center Staff";
        this.department = department;
    }
    
    public String getDepartment() { return department; }
    
    @Override
    public void displayMenu() {
        System.out.println("\n=== Career Center Staff Menu ===");
        System.out.println("1. Authorize Company Representatives");
        System.out.println("2. Approve/Reject Internship Opportunities");
        System.out.println("3. Approve/Reject Withdrawal Requests");
        System.out.println("4. Generate Reports");
        System.out.println("5. Change Password");
        System.out.println("6. Logout");
        System.out.print("Choose an option: ");
    }
    
    @Override
    public void performAction(int choice, InternshipSystem system) {
        switch (choice) {
            case 1:
                authorizeCompanyRepresentatives(system);
                break;
            case 2:
                approveRejectInternshipOpportunities(system);
                break;
            case 3:
                approveRejectWithdrawalRequests(system);
                break;
            case 4:
                generateReports(system);
                break;
            case 5:
                changePassword(system);
                break;
            case 6:
                system.logout();
                break;
            default:
                System.out.println("Invalid option!");
        }
    }
    
    private void authorizeCompanyRepresentatives(InternshipSystem system) {
        List<User> pendingReps = system.getPendingCompanyRepresentatives();
        if (pendingReps.isEmpty()) {
            System.out.println("No pending company representatives.");
            return;
        }
        
        System.out.println("\n=== Pending Company Representatives ===");
        for (int i = 0; i < pendingReps.size(); i++) {
            CompanyRepresentative rep = (CompanyRepresentative) pendingReps.get(i);
            System.out.println((i + 1) + ". " + rep.getName() + " - " + rep.getCompanyName());
        }
        
        System.out.print("Select representative to process (number) or 0 to cancel: ");
        int choice = system.getScanner().nextInt();
        system.getScanner().nextLine();
        
        if (choice == 0) return;
        if (choice < 1 || choice > pendingReps.size()) {
            System.out.println("Invalid selection!");
            return;
        }
        
        CompanyRepresentative rep = (CompanyRepresentative) pendingReps.get(choice - 1);
        System.out.print("Approve (Y/N)? ");
        String decision = system.getScanner().nextLine().toUpperCase();
        
        if (decision.equals("Y")) {
            rep.setStatus("Approved");
            system.updateCompanyRepresentativeInFile(rep);
            System.out.println("Company representative approved!");
        } else {
            rep.setStatus("Rejected");
            system.updateCompanyRepresentativeInFile(rep);
            System.out.println("Company representative rejected!");
        }
    }
    
    private void approveRejectInternshipOpportunities(InternshipSystem system) {
        system.reloadInternshipsFromFile();
        List<InternshipOpportunity> pendingOpps = system.getPendingInternshipOpportunities();
        if (pendingOpps.isEmpty()) {
            System.out.println("No pending internship opportunities.");
            return;
        }
        
        System.out.println("\n=== Pending Internship Opportunities ===");
        for (int i = 0; i < pendingOpps.size(); i++) {
            InternshipOpportunity opp = pendingOpps.get(i);
            System.out.println((i + 1) + ". " + opp.getTitle() + " - " + opp.getCompanyName());
        }
        
        System.out.print("Select opportunity to process (number) or 0 to cancel: ");
        int choice = system.getScanner().nextInt();
        system.getScanner().nextLine();
        
        if (choice == 0) return;
        if (choice < 1 || choice > pendingOpps.size()) {
            System.out.println("Invalid selection!");
            return;
        }
        
        InternshipOpportunity opp = pendingOpps.get(choice - 1);
        System.out.print("Approve (Y/N)? ");
        String decision = system.getScanner().nextLine().toUpperCase();
        
        if (decision.equals("Y")) {
            opp.setStatus("Approved");
            system.updateInternshipOpportunityInFile(opp);
            System.out.println("Internship opportunity approved!");
        } else {
            opp.setStatus("Rejected");
            system.updateInternshipOpportunityInFile(opp);
            System.out.println("Internship opportunity rejected!");
        }
    }
    
    private void approveRejectWithdrawalRequests(InternshipSystem system) {
        system.reloadInternshipApplicationsFromFile();
        List<InternshipApplication> withdrawalRequests = system.getWithdrawalRequests();
        if (withdrawalRequests.isEmpty()) {
            System.out.println("No withdrawal requests.");
            return;
        }
        
        System.out.println("\n=== Withdrawal Requests ===");
        for (int i = 0; i < withdrawalRequests.size(); i++) {
            InternshipApplication app = withdrawalRequests.get(i);
            Student student = (Student) system.getUserById(app.getStudentId());
            InternshipOpportunity opp = system.getInternshipOpportunityById(app.getInternshipId());
            System.out.println((i + 1) + ". Student: " + (student != null ? student.getName() : "Unknown") + 
                             ", Opportunity: " + (opp != null ? opp.getTitle() : "Unknown") + 
                             ", ID: " + app.getInternshipId() + 
                             ", Student ID: " + app.getStudentId() +
                             ", Company Approval: " + app.getCompanyApprovalStatus());
        }
        
        System.out.print("Select request to process (number) or 0 to cancel: ");
        int choice = system.getScanner().nextInt();
        system.getScanner().nextLine();
        
        if (choice == 0) return;
        if (choice < 1 || choice > withdrawalRequests.size()) {
            System.out.println("Invalid selection!");
            return;
        }
        
        InternshipApplication app = withdrawalRequests.get(choice - 1);
        System.out.print("Approve (Y/N)? ");
        String decision = system.getScanner().nextLine().toUpperCase();
        
        if (decision.equals("Y")) {
            system.removeInternshipApplicationFromFile(app);
            System.out.println("Withdrawal request approved! Application removed from system.");
        } else {
            if (app.getCompanyApprovalStatus().equals("Accepted")) {
                app.setStatus("Successful");
            } else {
                app.setStatus("Pending");
            }
            system.updateInternshipApplicationInFile(app);
            System.out.println("Withdrawal request rejected!");
        }
    }
    
    private void generateReports(InternshipSystem system) {
        system.reloadInternshipsFromFile();
        System.out.println("\n=== Generate Reports ===");
        System.out.println("1. All Internship Opportunities");
        System.out.println("2. Filter by Status");
        System.out.println("3. Filter by Major");
        System.out.println("4. Filter by Level");
        System.out.print("Choose filter option: ");
        int filterChoice = system.getScanner().nextInt();
        system.getScanner().nextLine();
        
        List<InternshipOpportunity> opportunities = new ArrayList<>();
        
        switch (filterChoice) {
            case 1:
                opportunities = system.getAllInternshipOpportunities();
                break;
            case 2:
                System.out.print("Enter status to filter by: ");
                String status = system.getScanner().nextLine();
                opportunities = system.getInternshipOpportunitiesByStatus(status);
                break;
            case 3:
                System.out.print("Enter major to filter by: ");
                String major = system.getScanner().nextLine();
                opportunities = system.getInternshipOpportunitiesByMajor(major);
                break;
            case 4:
                System.out.println("Select level to filter by:");
                System.out.println("1. Basic");
                System.out.println("2. Intermediate");
                System.out.println("3. Advanced");
                System.out.print("Enter choice (1-3): ");
                int levelChoice = system.getScanner().nextInt();
                system.getScanner().nextLine();
                
                String level;
                switch (levelChoice) {
                    case 1:
                        level = "Basic";
                        break;
                    case 2:
                        level = "Intermediate";
                        break;
                    case 3:
                        level = "Advanced";
                        break;
                    default:
                        System.out.println("Invalid choice! Defaulting to all levels.");
                        opportunities = system.getAllInternshipOpportunities();
                        return;
                }
                opportunities = system.getInternshipOpportunitiesByLevel(level);
                break;
            default:
                System.out.println("Invalid option!");
                return;
        }
        
        System.out.println("\n=== Report Results ===");
        if (opportunities.isEmpty()) {
            System.out.println("No opportunities found for the selected criteria.");
        } else {
            for (InternshipOpportunity opp : opportunities) {
                int acceptedCount = system.getAcceptedStudentsCount(opp.getId());
                System.out.println("ID: " + opp.getId() + 
                                 ", Title: " + opp.getTitle() + 
                                 ", Company: " + opp.getCompanyName() + 
                                 ", Level: " + opp.getLevel() + 
                                 ", Status: " + opp.getStatus() + 
                                 ", Slots: " + opp.getSlots() + 
                                 ", Applied: " + opp.getApplications().size() + 
                                 ", Accepted: " + acceptedCount + 
                                 ", Visible: " + opp.isVisible());
            }
        }
    }
    
    private void changePassword(InternshipSystem system) {
        System.out.print("Enter current password: ");
        String oldPassword = system.getScanner().nextLine();
        System.out.print("Enter new password: ");
        String newPassword = system.getScanner().nextLine();
        
        if (changePassword(oldPassword, newPassword)) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Incorrect current password!");
        }
    }
}