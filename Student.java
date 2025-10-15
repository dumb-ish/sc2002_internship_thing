import java.util.*;

class Student extends User {
    private int yearOfStudy;
    private String major;
    private List<InternshipApplication> applications;
    private String acceptedInternshipId;
    
    public Student(String userId, String name, String password, int yearOfStudy, String major) {
        super(userId, name, password);
        this.role = "Student";
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.applications = new ArrayList<>();
        this.acceptedInternshipId = null;
    }
    
    public int getYearOfStudy() { return yearOfStudy; }
    public String getMajor() { return major; }
    public List<InternshipApplication> getApplications() { 
        return applications; 
    }
    public String getAcceptedInternshipId() { return acceptedInternshipId; }
    public void setAcceptedInternshipId(String acceptedInternshipId) { this.acceptedInternshipId = acceptedInternshipId; }
    
    @Override
    public void displayMenu() {
        System.out.println("\n=== Student Menu ===");
        System.out.println("1. View Internship Opportunities");
        System.out.println("2. Apply for Internship");
        System.out.println("3. View My Applications");
        System.out.println("4. Accept Internship Placement");
        System.out.println("5. Request Withdrawal");
        System.out.println("6. Change Password");
        System.out.println("7. Logout");
        System.out.print("Choose an option: ");
    }
    
    @Override
    public void performAction(int choice, InternshipSystem system) {
        switch (choice) {
            case 1:
                viewInternshipOpportunities(system);
                break;
            case 2:
                applyForInternship(system);
                break;
            case 3:
                viewMyApplications(system);
                break;
            case 4:
                acceptInternshipPlacement(system);
                break;
            case 5:
                requestWithdrawal(system);
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
    
    private void viewInternshipOpportunities(InternshipSystem system) {
        system.reloadInternshipsFromFile();
        List<InternshipOpportunity> opportunities = system.getVisibleOpportunities(this);
        System.out.println("\n=== Available Internship Opportunities ===");
        if (opportunities.isEmpty()) {
            System.out.println("No opportunities available.");
        } else {
            for (InternshipOpportunity opp : opportunities) {
                System.out.println(opp);
            }
        }
    }
    
    private void applyForInternship(InternshipSystem system) {
        system.reloadInternshipsFromFile();
        boolean hasAcceptedInternship = system.hasStudentAcceptedInternship(this.userId);
        if (hasAcceptedInternship) {
            System.out.println("You have already accepted an internship and cannot apply for more.");
            return;
        }
        
        long pendingApps = applications.stream()
            .filter(app -> !app.getStatus().equals("Withdrawn"))
            .count();
            
        if (pendingApps >= 3) {
            System.out.println("You have reached the maximum number of applications (3).");
            return;
        }
        
        viewInternshipOpportunities(system);
        System.out.print("Enter Internship ID to apply: ");
        String internshipId = system.getScanner().nextLine();
        
        InternshipOpportunity opportunity = system.getInternshipOpportunityById(internshipId);
        if (opportunity == null) {
            System.out.println("Internship opportunity not found!");
            return;
        }
        
        if (!opportunity.isVisible() || opportunity.getStatus().equals("Filled")) {
            System.out.println("This opportunity is not available for application.");
            return;
        }
        
        if (yearOfStudy <= 2 && !opportunity.getLevel().equals("Basic")) {
            System.out.println("You can only apply for Basic-level internships.");
            return;
        }
        
        if (opportunity.hasReachedApplicationLimit()) {
            System.out.println("Application limit reached for this opportunity.");
            return;
        }
        
        if (applications.stream().anyMatch(app -> app.getInternshipId().equals(internshipId))) {
            System.out.println("You have already applied for this internship.");
            return;
        }
        
        InternshipApplication application = new InternshipApplication(internshipId, this.userId, "Pending", "Pending");
        applications.add(application);
        opportunity.addApplication(application);
        system.updateInternshipOpportunityInFile(opportunity);
        system.saveInternshipApplicationToFile(application);
        System.out.println("Successfully applied for internship: " + opportunity.getTitle());
    }
    
    private void viewMyApplications(InternshipSystem system) {
        system.reloadInternshipApplicationsForUser(this.userId);
        System.out.println("\n=== My Applications ===");
        if (applications.isEmpty()) {
            System.out.println("No applications found.");
        } else {
            for (InternshipApplication app : applications) {
                InternshipOpportunity opp = system.getInternshipOpportunityById(app.getInternshipId());
                System.out.println("ID: " + app.getInternshipId() + 
                                 ", Status: " + app.getStatus() + 
                                 ", Company Approval: " + app.getCompanyApprovalStatus() +
                                 ", Title: " + (opp != null ? opp.getTitle() : "Unknown"));
            }
        }
    }
    
    private void acceptInternshipPlacement(InternshipSystem system) {
        system.reloadInternshipApplicationsForUser(this.userId);
        system.reloadInternshipsFromFile();
        List<InternshipApplication> successfulApps = applications.stream()
            .filter(app -> app.getStatus().equals("Successful"))
            .filter(app -> !app.getInternshipId().equals(acceptedInternshipId))
            .toList();
            
        if (successfulApps.isEmpty()) {
            System.out.println("No successful applications to accept.");
            return;
        }
        
        System.out.println("\n=== Successful Applications ===");
        for (int i = 0; i < successfulApps.size(); i++) {
            InternshipApplication app = successfulApps.get(i);
            InternshipOpportunity opp = system.getInternshipOpportunityById(app.getInternshipId());
            System.out.println((i + 1) + ". " + (opp != null ? opp.getTitle() : "Unknown") + 
                             " (ID: " + app.getInternshipId() + ")");
        }
        
        System.out.print("Select internship to accept (number): ");
        int choice = system.getScanner().nextInt();
        system.getScanner().nextLine();
        
        if (choice < 1 || choice > successfulApps.size()) {
            System.out.println("Invalid selection!");
            return;
        }
        
        InternshipApplication selectedApp = successfulApps.get(choice - 1);
        InternshipOpportunity opportunity = system.getInternshipOpportunityById(selectedApp.getInternshipId());
        
        selectedApp.setStatus("Accepted");
        selectedApp.setCompanyApprovalStatus("Accepted");
        acceptedInternshipId = selectedApp.getInternshipId();
        opportunity.updateStatusAfterAcceptance();
        
        List<InternshipApplication> appsToRemove = applications.stream()
            .filter(app -> !app.getInternshipId().equals(selectedApp.getInternshipId()) && 
                           !app.getStatus().equals("Withdrawn"))
            .toList();
        
        for (InternshipApplication app : appsToRemove) {
            system.removeInternshipApplicationFromFile(app);
        }
        
        system.updateInternshipOpportunityInFile(opportunity);
        system.updateInternshipApplicationInFile(selectedApp);
        
        System.out.println("Successfully accepted internship: " + opportunity.getTitle());
        System.out.println("All other applications have been removed from the system.");
    }
    
    private void requestWithdrawal(InternshipSystem system) {
        system.reloadInternshipApplicationsForUser(this.userId);
        viewMyApplications(system);
        System.out.print("Enter Internship ID to request withdrawal: ");
        String internshipId = system.getScanner().nextLine();
        
        InternshipApplication app = applications.stream()
            .filter(a -> a.getInternshipId().equals(internshipId))
            .findFirst()
            .orElse(null);
            
        if (app == null) {
            System.out.println("No application found for this internship.");
            return;
        }
        
        if (app.getStatus().equals("Withdrawn")) {
            System.out.println("Application already withdrawn.");
            return;
        }
        
        app.setStatus("Withdrawal Requested");
        system.updateInternshipApplicationInFile(app);
        System.out.println("Withdrawal request submitted. Awaiting approval from Career Center Staff.");
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