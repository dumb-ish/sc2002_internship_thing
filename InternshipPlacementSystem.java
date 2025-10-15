import java.io.*;
import java.util.*;
import java.time.LocalDate;

abstract class User {
    protected String userId;
    protected String name;
    protected String password;
    protected String role;
    
    public User(String userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }
    
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }

    public abstract void displayMenu();
    
    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }
    
    public abstract void performAction(int choice, InternshipSystem system);
}

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

class InternshipOpportunity {
    private String id;
    private String title;
    private String description;
    private String level;
    private String preferredMajor;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private String status;
    private String companyName;
    private String companyRepId;
    private int slots;
    private int appliedCount;
    private boolean visible;
    private List<InternshipApplication> applications;
    
    public InternshipOpportunity(String id, String title, String description, String level,
                               String preferredMajor, LocalDate openingDate, LocalDate closingDate,
                               String status, String companyName, String companyRepId, int slots, boolean initialVisibility) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.status = status;
        this.companyName = companyName;
        this.companyRepId = companyRepId;
        this.slots = slots;
        this.appliedCount = 0;
        this.visible = initialVisibility;
        this.applications = new ArrayList<>();
    }
    
    public InternshipOpportunity(String id, String title, String description, String level,
                               String preferredMajor, LocalDate openingDate, LocalDate closingDate,
                               String status, String companyName, String companyRepId, int slots) {
        this(id, title, description, level, preferredMajor, openingDate, closingDate,
             status, companyName, companyRepId, slots, false);
    }
    
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLevel() { return level; }
    public String getPreferredMajor() { return preferredMajor; }
    public LocalDate getOpeningDate() { return openingDate; }
    public LocalDate getClosingDate() { return closingDate; }
    public String getStatus() { return status; }
    public String getCompanyName() { return companyName; }
    public String getCompanyRepId() { return companyRepId; }
    public int getSlots() { return slots; }
    public boolean isVisible() { return visible; }
    public List<InternshipApplication> getApplications() { return applications; }
    
    public void setStatus(String status) { 
        this.status = status; 
        if (status.equals("Approved")) {
            this.visible = true;
        }
    }
    
    public void addApplication(InternshipApplication application) {
        if (!applications.contains(application)) {
            applications.add(application);
            appliedCount++;
        }
    }
    
    public InternshipApplication getApplicationByStudentId(String studentId) {
        return applications.stream()
            .filter(app -> app.getStudentId().equals(studentId))
            .findFirst()
            .orElse(null);
    }
    
    public boolean hasReachedApplicationLimit() {
        return appliedCount >= slots;
    }
    
    public void updateStatusAfterAcceptance() {
        long acceptedCount = applications.stream()
            .filter(app -> app.getStatus().equals("Accepted"))
            .count();
            
        if (acceptedCount >= slots) {
            this.status = "Filled";
        }
    }
    
    public void toggleVisibility() {
        this.visible = !this.visible;
    }
    
    @Override
    public String toString() {
        return "ID: " + id + ", Title: " + title + ", Company: " + companyName + 
               ", Level: " + level + ", Status: " + status + ", Slots: " + slots + 
               ", Applied: " + appliedCount + ", Visible: " + visible;
    }
}

class InternshipApplication {
    private String internshipId;
    private String studentId;
    private String status;
    private String companyApprovalStatus;
    
    public InternshipApplication(String internshipId, String studentId, String status, String companyApprovalStatus) {
        this.internshipId = internshipId;
        this.studentId = studentId;
        this.status = status;
        this.companyApprovalStatus = companyApprovalStatus;
    }
    
    public InternshipApplication(String internshipId, String studentId, String status) {
        this(internshipId, studentId, status, "Pending");
    }
    
    public String getInternshipId() { return internshipId; }
    public String getStudentId() { return studentId; }
    public String getStatus() { return status; }
    public String getCompanyApprovalStatus() { return companyApprovalStatus; }
    public void setStatus(String status) { this.status = status; }
    public void setCompanyApprovalStatus(String companyApprovalStatus) { this.companyApprovalStatus = companyApprovalStatus; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        InternshipApplication that = (InternshipApplication) obj;
        return Objects.equals(internshipId, that.internshipId) && 
               Objects.equals(studentId, that.studentId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(internshipId, studentId);
    }
}

class InternshipSystem {
    private Map<String, User> users;
    private Map<String, InternshipOpportunity> opportunities;
    private Map<String, InternshipApplication> applications;
    private User currentUser;
    private Scanner scanner;
    private int nextOpportunityId;
    
    public InternshipSystem() {
        users = new HashMap<>();
        opportunities = new HashMap<>();
        applications = new HashMap<>();
        currentUser = null;
        scanner = new Scanner(System.in);
        nextOpportunityId = 1;
    }
    
    public Scanner getScanner() {
        return scanner;
    }
    
    public void initializeSystem() {
        loadUsersFromFile("sample_staff_list.csv", "staff");
        loadUsersFromFile("sample_student_list.csv", "student");
        loadUsersFromFile("sample_company_representative_list.csv", "company_rep");
        loadInternshipsFromFile("internships.csv");
        loadInternshipApplicationsFromFile("internship_status.csv");
    }
    
    private void loadUsersFromFile(String filename, String type) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (type.equals("staff")) {
                    if (parts.length >= 5) {
                        String userId = parts[0].trim();
                        String name = parts[1].trim();
                        String role = parts[2].trim();
                        String department = parts[3].trim();
                        String email = parts[4].trim();
                        
                        User staff = new CareerCenterStaff(userId, name, "password", department);
                        users.put(userId, staff);
                    }
                } else if (type.equals("student")) {
                    if (parts.length >= 5) {
                        String userId = parts[0].trim();
                        String name = parts[1].trim();
                        String major = parts[2].trim();
                        int year = Integer.parseInt(parts[3].trim());
                        String email = parts[4].trim();
                        
                        User student = new Student(userId, name, "password", year, major);
                        users.put(userId, student);
                    }
                } else if (type.equals("company_rep")) {
                    if (parts.length >= 7) {
                        String userId = parts[0].trim();
                        String name = parts[1].trim();
                        String companyName = parts[2].trim();
                        String department = parts[3].trim();
                        String position = parts[4].trim();
                        String email = parts[5].trim();
                        String status = parts[6].trim();
                        
                        User rep = new CompanyRepresentative(userId, name, "password", companyName, department, position, email);
                        ((CompanyRepresentative)rep).setStatus(status);
                        users.put(userId, rep);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + filename);
        } catch (Exception e) {
            System.out.println("Error processing file: " + e.getMessage());
        }
    }
    
    private void loadInternshipsFromFile(String filename) {
        opportunities.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 12) {
                    String id = parts[0].trim();
                    String title = parts[1].trim();
                    String description = parts[2].trim();
                    String level = parts[3].trim();
                    String preferredMajor = parts[4].trim();
                    LocalDate openingDate = LocalDate.parse(parts[5].trim());
                    LocalDate closingDate = LocalDate.parse(parts[6].trim());
                    String status = parts[7].trim();
                    String companyName = parts[8].trim();
                    String companyRepId = parts[9].trim();
                    int slots = Integer.parseInt(parts[10].trim());
                    boolean visible = Boolean.parseBoolean(parts[11].trim());
                    
                    InternshipOpportunity opportunity = new InternshipOpportunity(
                        id, title, description, level, preferredMajor, openingDate, closingDate,
                        status, companyName, companyRepId, slots, visible
                    );
                    
                    opportunities.put(id, opportunity);
                    
                    String numericPart = id.substring(2);
                    try {
                        int idNum = Integer.parseInt(numericPart);
                        if (idNum >= nextOpportunityId) {
                            nextOpportunityId = idNum + 1;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Internships file not found or error loading: " + filename + ". Creating new file.");
            try (PrintWriter pw = new PrintWriter(new FileWriter(filename, false))) {
                pw.println("ID,Title,Description,Level,PreferredMajor,OpeningDate,ClosingDate,Status,CompanyName,CompanyRepId,Slots,Visible");
            } catch (IOException ex) {
                System.out.println("Error creating internships file: " + ex.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error processing internships file: " + e.getMessage());
        }
    }
    
    private void loadInternshipApplicationsFromFile(String filename) {
        applications.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String internshipId = parts[0].trim();
                    String studentId = parts[1].trim();
                    String status = parts[2].trim();
                    String companyApprovalStatus = parts[3].trim();
                    
                    InternshipApplication application = new InternshipApplication(
                        internshipId, studentId, status, companyApprovalStatus
                    );
                    
                    applications.put(internshipId + "_" + studentId, application);
                    
                    InternshipOpportunity opportunity = opportunities.get(internshipId);
                    if (opportunity != null) {
                        opportunity.addApplication(application);
                    }
                    
                    User user = users.get(studentId);
                    if (user instanceof Student) {
                        Student student = (Student) user;
                        if (!student.getApplications().contains(application)) {
                            student.getApplications().add(application);
                        }
                        if (status.equals("Accepted")) {
                            student.setAcceptedInternshipId(internshipId);
                        }
                    }
                } else if (parts.length >= 3) {
                    String internshipId = parts[0].trim();
                    String studentId = parts[1].trim();
                    String status = parts[2].trim();
                    
                    InternshipApplication application = new InternshipApplication(
                        internshipId, studentId, status, "Pending"
                    );
                    
                    applications.put(internshipId + "_" + studentId, application);
                    
                    InternshipOpportunity opportunity = opportunities.get(internshipId);
                    if (opportunity != null) {
                        opportunity.addApplication(application);
                    }
                    
                    User user = users.get(studentId);
                    if (user instanceof Student) {
                        Student student = (Student) user;
                        if (!student.getApplications().contains(application)) {
                            student.getApplications().add(application);
                        }
                        if (status.equals("Accepted")) {
                            student.setAcceptedInternshipId(internshipId);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Internship status file not found or error loading: " + filename + ". Creating new file.");
            try (PrintWriter pw = new PrintWriter(new FileWriter(filename, false))) {
                pw.println("InternshipID,StudentID,Status,CompanyApproval");
            } catch (IOException ex) {
                System.out.println("Error creating internship status file: " + ex.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error processing internship status file: " + e.getMessage());
        }
    }
    
    public void reloadInternshipsFromFile() {
        loadInternshipsFromFile("internships.csv");
    }
    
    public void reloadInternshipApplicationsFromFile() {
        loadInternshipApplicationsFromFile("internship_status.csv");
    }
    
    public void reloadInternshipApplicationsForUser(String userId) {
        if (users.get(userId) instanceof Student) {
            Student student = (Student) users.get(userId);
            student.getApplications().clear();
            
            for (InternshipApplication app : applications.values()) {
                if (app.getStudentId().equals(userId)) {
                    student.getApplications().add(app);
                }
            }
        }
    }
    
    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }
    
    public User getUserById(String userId) {
        return users.get(userId);
    }
    
    public boolean authenticateUser(String userId, String password) {
        User user = users.get(userId);
        return user != null && user.getPassword().equals(password);
    }
    
    public void setCurrentUser(String userId) {
        this.currentUser = users.get(userId);
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public void addInternshipOpportunity(InternshipOpportunity opportunity) {
        opportunities.put(opportunity.getId(), opportunity);
    }
    
    public InternshipOpportunity getInternshipOpportunityById(String id) {
        return opportunities.get(id);
    }
    
    public List<InternshipOpportunity> getVisibleOpportunities(Student student) {
        LocalDate today = LocalDate.now();
        return opportunities.values().stream()
            .filter(opp -> opp.isVisible() && 
                         opp.getStatus().equals("Approved") &&
                         (opp.getOpeningDate().isBefore(today) || opp.getOpeningDate().isEqual(today)) &&
                         (opp.getClosingDate().isAfter(today) || opp.getClosingDate().isEqual(today)) &&
                         (opp.getPreferredMajor().equals("All") || 
                          opp.getPreferredMajor().equals(student.getMajor())) &&
                         (student.getYearOfStudy() > 2 || opp.getLevel().equals("Basic")))
            .sorted((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()))
            .toList();
    }
    
    public List<User> getPendingCompanyRepresentatives() {
        return users.values().stream()
            .filter(user -> user instanceof CompanyRepresentative)
            .filter(user -> ((CompanyRepresentative) user).getStatus().equals("Pending"))
            .toList();
    }
    
    public List<InternshipOpportunity> getPendingInternshipOpportunities() {
        return opportunities.values().stream()
            .filter(opp -> opp.getStatus().equals("Pending"))
            .toList();
    }
    
    public List<InternshipApplication> getWithdrawalRequests() {
        return applications.values().stream()
            .filter(app -> app.getStatus().equals("Withdrawal Requested"))
            .toList();
    }
    
    public List<InternshipOpportunity> getAllInternshipOpportunities() {
        return new ArrayList<>(opportunities.values());
    }
    
    public List<InternshipOpportunity> getOpportunitiesByCompanyRepId(String companyRepId) {
        return opportunities.values().stream()
            .filter(opp -> opp.getCompanyRepId().equals(companyRepId))
            .sorted((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()))
            .toList();
    }
    
    public List<InternshipOpportunity> getInternshipOpportunitiesByStatus(String status) {
        return opportunities.values().stream()
            .filter(opp -> opp.getStatus().equalsIgnoreCase(status))
            .sorted((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()))
            .toList();
    }
    
    public List<InternshipOpportunity> getInternshipOpportunitiesByMajor(String major) {
        return opportunities.values().stream()
            .filter(opp -> opp.getPreferredMajor().equalsIgnoreCase(major))
            .sorted((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()))
            .toList();
    }
    
    public List<InternshipOpportunity> getInternshipOpportunitiesByLevel(String level) {
        return opportunities.values().stream()
            .filter(opp -> opp.getLevel().equalsIgnoreCase(level))
            .sorted((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()))
            .toList();
    }
    
    public int getNextOpportunityId() {
        return nextOpportunityId++;
    }
    
    public void run() {
        initializeSystem();
        
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                currentUser.displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();
                currentUser.performAction(choice, this);
            }
        }
    }
    
    private void showLoginMenu() {
        System.out.println("\n=== Internship Placement Management System ===");
        System.out.println("1. Login");
        System.out.println("2. Register as Company Representative");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                performLogin();
                break;
            case 2:
                registerCompanyRepresentative();
                break;
            case 3:
                System.out.println("Thank you for using the system!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option!");
        }
    }
    
    private void performLogin() {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        if (authenticateUser(userId, password)) {
            setCurrentUser(userId);
            System.out.println("Login successful! Welcome, " + currentUser.getName());
        } else {
            System.out.println("Invalid credentials!");
        }
    }
    
    private void registerCompanyRepresentative() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Company Email (as User ID): ");
        String email = scanner.nextLine();
        
        if (users.containsKey(email)) {
            System.out.println("A user with this email already exists. Registration failed.");
            return;
        }
        
        System.out.print("Enter Company Name: ");
        String companyName = scanner.nextLine();
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        System.out.print("Enter Position: ");
        String position = scanner.nextLine();
        
        User rep = new CompanyRepresentative(email, name, "password", companyName, department, position, email);
        addUser(rep);
        
        saveCompanyRepresentativeToFile((CompanyRepresentative) rep);
        
        System.out.println("Registration successful! Awaiting approval from Career Center Staff.");
    }
    
    private void saveCompanyRepresentativeToFile(CompanyRepresentative rep) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("sample_company_representative_list.csv", true))) {
            File file = new File("sample_company_representative_list.csv");
            if (file.length() == 0) {
                pw.println("CompanyRepID,Name,CompanyName,Department,Position,Email,Status");
            }
            
            pw.println(rep.getUserId() + "," + 
                      rep.getName() + "," + 
                      rep.getCompanyName() + "," + 
                      rep.getDepartment() + "," + 
                      rep.getPosition() + "," + 
                      rep.getEmail() + "," + 
                      rep.getStatus());
        } catch (IOException e) {
            System.out.println("Error saving company representative to file: " + e.getMessage());
        }
    }
    
    public void updateCompanyRepresentativeInFile(CompanyRepresentative rep) {
        List<CompanyRepresentative> allReps = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("sample_company_representative_list.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String userId = parts[0].trim();
                    String name = parts[1].trim();
                    String companyName = parts[2].trim();
                    String department = parts[3].trim();
                    String position = parts[4].trim();
                    String email = parts[5].trim();
                    String status = parts[6].trim();
                    
                    CompanyRepresentative existingRep = new CompanyRepresentative(userId, name, "password", 
                        companyName, department, position, email);
                    existingRep.setStatus(status);
                    allReps.add(existingRep);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }
        
        for (int i = 0; i < allReps.size(); i++) {
            if (allReps.get(i).getUserId().equals(rep.getUserId())) {
                allReps.set(i, rep);
                break;
            }
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter("sample_company_representative_list.csv", false))) {
            pw.println("CompanyRepID,Name,CompanyName,Department,Position,Email,Status");
            for (CompanyRepresentative r : allReps) {
                pw.println(r.getUserId() + "," + 
                          r.getName() + "," + 
                          r.getCompanyName() + "," + 
                          r.getDepartment() + "," + 
                          r.getPosition() + "," + 
                          r.getEmail() + "," + 
                          r.getStatus());
            }
        } catch (IOException e) {
            System.out.println("Error updating file: " + e.getMessage());
        }
    }
    
    public void saveInternshipOpportunityToFile(InternshipOpportunity opportunity) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("internships.csv", true))) {
            File file = new File("internships.csv");
            if (file.length() == 0) {
                pw.println("ID,Title,Description,Level,PreferredMajor,OpeningDate,ClosingDate,Status,CompanyName,CompanyRepId,Slots,Visible");
            }
            
            pw.println(opportunity.getId() + "," + 
                      opportunity.getTitle() + "," + 
                      opportunity.getDescription() + "," + 
                      opportunity.getLevel() + "," + 
                      opportunity.getPreferredMajor() + "," + 
                      opportunity.getOpeningDate() + "," + 
                      opportunity.getClosingDate() + "," + 
                      opportunity.getStatus() + "," + 
                      opportunity.getCompanyName() + "," + 
                      opportunity.getCompanyRepId() + "," + 
                      opportunity.getSlots() + "," + 
                      opportunity.isVisible());
        } catch (IOException e) {
            System.out.println("Error saving internship opportunity to file: " + e.getMessage());
        }
    }
    
    public void updateInternshipOpportunityInFile(InternshipOpportunity opportunity) {
        List<InternshipOpportunity> allOpps = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("internships.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 12) {
                    String id = parts[0].trim();
                    String title = parts[1].trim();
                    String description = parts[2].trim();
                    String level = parts[3].trim();
                    String preferredMajor = parts[4].trim();
                    LocalDate openingDate = LocalDate.parse(parts[5].trim());
                    LocalDate closingDate = LocalDate.parse(parts[6].trim());
                    String status = parts[7].trim();
                    String companyName = parts[8].trim();
                    String companyRepId = parts[9].trim();
                    int slots = Integer.parseInt(parts[10].trim());
                    boolean visible = Boolean.parseBoolean(parts[11].trim());
                    
                    InternshipOpportunity existingOpp = new InternshipOpportunity(
                        id, title, description, level, preferredMajor, openingDate, closingDate,
                        status, companyName, companyRepId, slots, visible
                    );
                    
                    allOpps.add(existingOpp);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading internships file: " + e.getMessage());
            return;
        }
        
        for (int i = 0; i < allOpps.size(); i++) {
            if (allOpps.get(i).getId().equals(opportunity.getId())) {
                allOpps.set(i, opportunity);
                break;
            }
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter("internships.csv", false))) {
            pw.println("ID,Title,Description,Level,PreferredMajor,OpeningDate,ClosingDate,Status,CompanyName,CompanyRepId,Slots,Visible");
            for (InternshipOpportunity opp : allOpps) {
                pw.println(opp.getId() + "," + 
                          opp.getTitle() + "," + 
                          opp.getDescription() + "," + 
                          opp.getLevel() + "," + 
                          opp.getPreferredMajor() + "," + 
                          opp.getOpeningDate() + "," + 
                          opp.getClosingDate() + "," + 
                          opp.getStatus() + "," + 
                          opp.getCompanyName() + "," + 
                          opp.getCompanyRepId() + "," + 
                          opp.getSlots() + "," + 
                          opp.isVisible());
            }
        } catch (IOException e) {
            System.out.println("Error updating internships file: " + e.getMessage());
        }
    }
    
    public void saveInternshipApplicationToFile(InternshipApplication application) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("internship_status.csv", true))) {
            File file = new File("internship_status.csv");
            if (file.length() == 0) {
                pw.println("InternshipID,StudentID,Status,CompanyApproval");
            }
            
            pw.println(application.getInternshipId() + "," + 
                      application.getStudentId() + "," + 
                      application.getStatus() + "," + 
                      application.getCompanyApprovalStatus());
        } catch (IOException e) {
            System.out.println("Error saving internship application to file: " + e.getMessage());
        }
    }
    
    public void updateInternshipApplicationInFile(InternshipApplication application) {
        List<InternshipApplication> allApps = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("internship_status.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String internshipId = parts[0].trim();
                    String studentId = parts[1].trim();
                    String status = parts[2].trim();
                    String companyApprovalStatus = parts[3].trim();
                    
                    InternshipApplication existingApp = new InternshipApplication(
                        internshipId, studentId, status, companyApprovalStatus
                    );
                    
                    allApps.add(existingApp);
                } else if (parts.length >= 3) {
                    String internshipId = parts[0].trim();
                    String studentId = parts[1].trim();
                    String status = parts[2].trim();
                    
                    InternshipApplication existingApp = new InternshipApplication(
                        internshipId, studentId, status, "Pending"
                    );
                    
                    allApps.add(existingApp);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading internship status file: " + e.getMessage());
            return;
        }
        
        for (int i = 0; i < allApps.size(); i++) {
            if (allApps.get(i).getInternshipId().equals(application.getInternshipId()) &&
                allApps.get(i).getStudentId().equals(application.getStudentId())) {
                allApps.set(i, application);
                break;
            }
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter("internship_status.csv", false))) {
            pw.println("InternshipID,StudentID,Status,CompanyApproval");
            for (InternshipApplication app : allApps) {
                pw.println(app.getInternshipId() + "," + 
                          app.getStudentId() + "," + 
                          app.getStatus() + "," + 
                          app.getCompanyApprovalStatus());
            }
        } catch (IOException e) {
            System.out.println("Error updating internship status file: " + e.getMessage());
        }
    }
    
    public void removeInternshipApplicationFromFile(InternshipApplication application) {
        List<InternshipApplication> allApps = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("internship_status.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String internshipId = parts[0].trim();
                    String studentId = parts[1].trim();
                    String status = parts[2].trim();
                    String companyApprovalStatus = parts[3].trim();
                    
                    if (!(internshipId.equals(application.getInternshipId()) && 
                          studentId.equals(application.getStudentId()))) {
                        InternshipApplication existingApp = new InternshipApplication(
                            internshipId, studentId, status, companyApprovalStatus
                        );
                        allApps.add(existingApp);
                    }
                } else if (parts.length >= 3) {
                    String internshipId = parts[0].trim();
                    String studentId = parts[1].trim();
                    String status = parts[2].trim();
              
                    if (!(internshipId.equals(application.getInternshipId()) && 
                          studentId.equals(application.getStudentId()))) {
                        InternshipApplication existingApp = new InternshipApplication(
                            internshipId, studentId, status, "Pending"
                        );
                        allApps.add(existingApp);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading internship status file: " + e.getMessage());
            return;
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter("internship_status.csv", false))) {
            pw.println("InternshipID,StudentID,Status,CompanyApproval");
            for (InternshipApplication app : allApps) {
                pw.println(app.getInternshipId() + "," + 
                          app.getStudentId() + "," + 
                          app.getStatus() + "," + 
                          app.getCompanyApprovalStatus());
            }
        } catch (IOException e) {
            System.out.println("Error updating internship status file: " + e.getMessage());
        }
        
        String key = application.getInternshipId() + "_" + application.getStudentId();
        applications.remove(key);
        
        User user = users.get(application.getStudentId());
        if (user instanceof Student) {
            Student student = (Student) user;
            student.getApplications().removeIf(app -> 
                app.getInternshipId().equals(application.getInternshipId()) &&
                app.getStudentId().equals(application.getStudentId()));
        }
        
        InternshipOpportunity opportunity = opportunities.get(application.getInternshipId());
        if (opportunity != null) {
            opportunity.getApplications().removeIf(app -> 
                app.getInternshipId().equals(application.getInternshipId()) &&
                app.getStudentId().equals(application.getStudentId()));
        }
    }
    
    public boolean hasStudentAcceptedInternship(String studentId) {
        try (BufferedReader br = new BufferedReader(new FileReader("internship_status.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String csvStudentId = parts[1].trim();
                    String status = parts[2].trim();
                    
                    if (csvStudentId.equals(studentId) && status.equals("Accepted")) {
                        return true;
                    }
                } else if (parts.length >= 3) {
                    String csvStudentId = parts[1].trim();
                    String status = parts[2].trim();
                    
                    if (csvStudentId.equals(studentId) && status.equals("Accepted")) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading internship status file: " + e.getMessage());
        }
        return false;
    }
    
    public int getAcceptedStudentsCount(String internshipId) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("internship_status.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String csvInternshipId = parts[0].trim();
                    String status = parts[2].trim();
                    
                    if (csvInternshipId.equals(internshipId) && status.equals("Accepted")) {
                        count++;
                    }
                } else if (parts.length >= 3) {
                    String csvInternshipId = parts[0].trim();
                    String status = parts[2].trim();
                    
                    if (csvInternshipId.equals(internshipId) && status.equals("Accepted")) {
                        count++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading internship status file: " + e.getMessage());
        }
        return count;
    }
}

public class InternshipPlacementSystem {
    public static void main(String[] args) {
        InternshipSystem system = new InternshipSystem();
        system.run();
    }
}