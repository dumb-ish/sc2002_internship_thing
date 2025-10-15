import java.util.*;
import java.time.LocalDate;


class CompanyRepresentative extends User {
    private String companyName;
    private String department;
    private String position;
    private String status;
    private String email;
    
    public CompanyRepresentative(String userId, String name, String password, String companyName, 
                               String department, String position, String email) {
        super(userId, name, password);
        this.role = "Company Representative";
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.email = email;
        this.status = "Pending";
    }
    
    public String getCompanyName() { return companyName; }
    public String getDepartment() { return department; }
    public String getPosition() { return position; }
    public String getEmail() { return email; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public void displayMenu() {
        if (!status.equals("Approved")) {
            System.out.println("\n=== Company Representative Menu ===");
            System.out.println("Your account is pending approval. Please wait for Career Center Staff approval.");
            System.out.println("1. Change Password");
            System.out.println("2. Logout");
            System.out.print("Choose an option: ");
        } else {
            System.out.println("\n=== Company Representative Menu ===");
            System.out.println("1. Create Internship Opportunity");
            System.out.println("2. View My Internship Opportunities");
            System.out.println("3. View Applications for My Opportunities");
            System.out.println("4. Approve/Reject Application");
            System.out.println("5. Toggle Visibility");
            System.out.println("6. Change Password");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");
        }
    }
    
    @Override
    public void performAction(int choice, InternshipSystem system) {
        if (!status.equals("Approved")) {
            switch (choice) {
                case 1:
                    changePassword(system);
                    break;
                case 2:
                    system.logout();
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        } else {
            switch (choice) {
                case 1:
                    createInternshipOpportunity(system);
                    break;
                case 2:
                    viewMyOpportunities(system);
                    break;
                case 3:
                    viewApplicationsForMyOpportunities(system);
                    break;
                case 4:
                    approveRejectApplication(system);
                    break;
                case 5:
                    toggleVisibility(system);
                    break;
                case 6:
                    changePassword(system);
                    break;
                case 7:
                    system.logout();
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
    
    private void createInternshipOpportunity(InternshipSystem system) {
        system.reloadInternshipsFromFile();
        List<InternshipOpportunity> currentOpps = system.getOpportunitiesByCompanyRepId(userId);
        if (currentOpps.size() >= 5) {
            System.out.println("You have reached the maximum number of internship opportunities (5).");
            return;
        }
        
        System.out.print("Enter Internship Title: ");
        String title = system.getScanner().nextLine();
        System.out.print("Enter Description: ");
        String description = system.getScanner().nextLine();
        System.out.println("Select Level:");
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
                System.out.println("Invalid choice! Defaulting to Basic level.");
                level = "Basic";
        }
        
        System.out.print("Enter Preferred Major: ");
        String preferredMajor = system.getScanner().nextLine();
        System.out.print("Enter Application Opening Date (YYYY-MM-DD): ");
        String openDateStr = system.getScanner().nextLine();
        System.out.print("Enter Application Closing Date (YYYY-MM-DD): ");
        String closeDateStr = system.getScanner().nextLine();
        System.out.print("Enter Number of Slots (max 10): ");
        int slots = system.getScanner().nextInt();
        system.getScanner().nextLine();
        
        if (slots > 10) {
            System.out.println("Maximum slots allowed is 10.");
            return;
        }
        
        LocalDate openDate = LocalDate.parse(openDateStr);
        LocalDate closeDate = LocalDate.parse(closeDateStr);
        
        String opportunityId = "IO" + (system.getNextOpportunityId());
        InternshipOpportunity opportunity = new InternshipOpportunity(
            opportunityId, title, description, level, preferredMajor, 
            openDate, closeDate, "Pending", companyName, userId, slots
        );
        
        system.addInternshipOpportunity(opportunity);
        system.saveInternshipOpportunityToFile(opportunity);
        System.out.println("Internship opportunity created successfully! ID: " + opportunityId);
    }
    
    private void viewMyOpportunities(InternshipSystem system) {
        system.reloadInternshipsFromFile();
        List<InternshipOpportunity> myOpps = system.getOpportunitiesByCompanyRepId(userId);
        System.out.println("\n=== My Internship Opportunities ===");
        if (myOpps.isEmpty()) {
            System.out.println("No opportunities found for your company.");
        } else {
            for (InternshipOpportunity opp : myOpps) {
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
    
    private void viewApplicationsForMyOpportunities(InternshipSystem system) {
        system.reloadInternshipApplicationsFromFile();
        List<InternshipOpportunity> myOpps = system.getOpportunitiesByCompanyRepId(userId);
        System.out.println("\n=== Applications for My Opportunities ===");
        boolean hasApplications = false;
        for (InternshipOpportunity opp : myOpps) {
            List<InternshipApplication> apps = opp.getApplications();
            if (!apps.isEmpty()) {
                System.out.println("\nOpportunity: " + opp.getTitle() + " (ID: " + opp.getId() + ")");
                for (InternshipApplication app : apps) {
                    Student student = (Student) system.getUserById(app.getStudentId());
                    System.out.println("  Student: " + (student != null ? student.getName() : "Unknown") + 
                                     ", Status: " + app.getStatus() + 
                                     ", Company Approval: " + app.getCompanyApprovalStatus());
                }
                hasApplications = true;
            }
        }
        if (!hasApplications) {
            System.out.println("No applications found for your opportunities.");
        }
    }
    
    private void approveRejectApplication(InternshipSystem system) {
        system.reloadInternshipApplicationsFromFile();
        List<InternshipOpportunity> myOpps = system.getOpportunitiesByCompanyRepId(userId);
        System.out.println("\n=== Applications for My Opportunities ===");
        boolean hasApplications = false;
        for (InternshipOpportunity opp : myOpps) {
            List<InternshipApplication> apps = opp.getApplications();
            if (!apps.isEmpty()) {
                System.out.println("\nOpportunity: " + opp.getTitle() + " (ID: " + opp.getId() + ")");
                for (InternshipApplication app : apps) {
                    Student student = (Student) system.getUserById(app.getStudentId());
                    System.out.println("  Student: " + (student != null ? student.getName() : "Unknown") + 
                                     ", Status: " + app.getStatus() + 
                                     ", Company Approval: " + app.getCompanyApprovalStatus() +
                                     ", Student ID: " + app.getStudentId());
                }
                hasApplications = true;
            }
        }
        
        if (!hasApplications) {
            System.out.println("No applications found for your opportunities.");
            return;
        }
        
        System.out.print("Enter Internship ID: ");
        String internshipId = system.getScanner().nextLine();
        System.out.print("Enter Student ID: ");
        String studentId = system.getScanner().nextLine();
        System.out.print("Enter Status (Successful/Unsuccessful): ");
        String newStatus = system.getScanner().nextLine();
        
        if (!newStatus.equals("Successful") && !newStatus.equals("Unsuccessful")) {
            System.out.println("Invalid status! Use 'Successful' or 'Unsuccessful'.");
            return;
        }
        
        InternshipOpportunity opportunity = system.getInternshipOpportunityById(internshipId);
        if (opportunity == null) {
            System.out.println("Internship opportunity not found!");
            return;
        }
        
        if (!opportunity.getCompanyRepId().equals(userId)) {
            System.out.println("You don't have permission to modify this opportunity.");
            return;
        }
        
        InternshipApplication application = opportunity.getApplicationByStudentId(studentId);
        if (application == null) {
            System.out.println("Application not found!");
            return;
        }
        
        application.setStatus(newStatus);
        application.setCompanyApprovalStatus("Accepted");
        
        if (newStatus.equals("Successful")) {
            opportunity.updateStatusAfterAcceptance();
        }
        
        system.updateInternshipOpportunityInFile(opportunity);
        system.updateInternshipApplicationInFile(application);
        System.out.println("Application status updated to: " + newStatus);
    }
    
    private void toggleVisibility(InternshipSystem system) {
        system.reloadInternshipsFromFile();
        List<InternshipOpportunity> myOpps = system.getOpportunitiesByCompanyRepId(userId);
        System.out.println("\n=== My Internship Opportunities ===");
        if (myOpps.isEmpty()) {
            System.out.println("No opportunities found for your company.");
            return;
        }
        
        for (int i = 0; i < myOpps.size(); i++) {
            System.out.println((i + 1) + ". " + myOpps.get(i));
        }
        
        System.out.print("Select opportunity to toggle visibility (number): ");
        int choice = system.getScanner().nextInt();
        system.getScanner().nextLine();
        
        if (choice < 1 || choice > myOpps.size()) {
            System.out.println("Invalid selection!");
            return;
        }
        
        InternshipOpportunity opportunity = myOpps.get(choice - 1);
        opportunity.toggleVisibility();
        system.updateInternshipOpportunityInFile(opportunity);
        System.out.println("Visibility toggled. Current visibility: " + opportunity.isVisible());
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