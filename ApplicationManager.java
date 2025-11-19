import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages all application-related operations in the system.
 * Handles submission, status updates, and withdrawal processing.
 */
public class ApplicationManager {
    private List<Application> applicationList;
    
    /**
     * Constructor
     */
    public ApplicationManager() {
        this.applicationList = new ArrayList<>();
    }
    
    /**
     * Submit a new application
     * Returns true if successful, false otherwise
     */
    public boolean submitApplication(Student student, InternshipOpportunity internship) {
        // Check if student has already applied for this opportunity
        if (hasApplied(student, internship)) {
            System.out.println("You have already applied for this internship.");
            return false;
        }
        
        // Check if student has reached maximum applications (3)
        long activeApplications = getActiveApplicationsCount(student);
        if (activeApplications >= 3) {
            System.out.println("You have reached the maximum number of applications (3).");
            return false;
        }
        
        // Check student eligibility based on year of study
        if (student.getYearOfStudy() <= 2 && !"Basic".equals(internship.getLevel())) {
            System.out.println("Year 1 and 2 students can only apply for Basic-level internships.");
            return false;
        }
        
        // Check if internship is open for applications
        if (!internship.isOpenForApplications()) {
            System.out.println("This internship is not currently open for applications.");
            return false;
        }
        
        // Create and add the application
        Application application = new Application(student, internship);
        applicationList.add(application);
        return true;
    }
    
    /**
     * Update the status of an application
     * Prevents changing status of finalized applications (Successful/Unsuccessful)
     */
    public void updateApplicationStatus(Application app, String status) {
        String currentStatus = app.getStatus();
        
        // Prevent changing finalized statuses
        if ("Successful".equals(currentStatus) || "Unsuccessful".equals(currentStatus)) {
            System.out.println("Cannot change status of a finalized application (" + currentStatus + ").");
            return;
        }
        
        app.updateStatus(status);
    }
    
    /**
     * Handle withdrawal request
     */
    public void handleWithdrawal(Application app) {
        app.markWithdrawalRequested();
    }
    
    /**
     * Approve a withdrawal and remove the application
     */
    public void approveWithdrawal(Application app) {
        applicationList.remove(app);
    }
    
    /**
     * Reject a withdrawal and restore previous status
     */
    public void rejectWithdrawal(Application app) {
        app.setWithdrawalRequested(false);
        // Status remains as it was before withdrawal request
    }
    
    /**
     * Get all applications for a specific student
     */
    public List<Application> getApplicationsByStudent(Student student) {
        return applicationList.stream()
            .filter(app -> app.getStudent().equals(student))
            .collect(Collectors.toList());
    }
    
    /**
     * Get all applications for a specific internship
     */
    public List<Application> getApplicationsByInternship(InternshipOpportunity internship) {
        return applicationList.stream()
            .filter(app -> app.getInternship().equals(internship))
            .collect(Collectors.toList());
    }
    
    /**
     * Get all applications with withdrawal requests
     */
    public List<Application> getWithdrawalRequests() {
        return applicationList.stream()
            .filter(Application::isWithdrawalRequested)
            .collect(Collectors.toList());
    }
    
    /**
     * Check if student has already applied for an internship
     */
    private boolean hasApplied(Student student, InternshipOpportunity internship) {
        return applicationList.stream()
            .anyMatch(app -> app.getStudent().equals(student) && 
                           app.getInternship().equals(internship));
    }
    
    /**
     * Get count of active applications for a student
     * (excluding withdrawn applications)
     */
    private long getActiveApplicationsCount(Student student) {
        return applicationList.stream()
            .filter(app -> app.getStudent().equals(student))
            .filter(app -> !"Withdrawn".equals(app.getStatus()))
            .count();
    }
    
    /**
     * Accept an internship placement
     * Withdraws all other applications for the student
     */
    public boolean acceptInternshipPlacement(Student student, Application acceptedApp) {
        // Check if the application is successful
        if (!"Successful".equals(acceptedApp.getStatus())) {
            System.out.println("Can only accept successful applications.");
            return false;
        }
        
        // Update the accepted application status
        acceptedApp.updateStatus("Accepted");
        
        // Withdraw all other applications for this student
        List<Application> otherApps = getApplicationsByStudent(student).stream()
            .filter(app -> !app.equals(acceptedApp))
            .collect(Collectors.toList());
        
        for (Application app : otherApps) {
            applicationList.remove(app);
        }
        
        return true;
    }
    
    /**
     * Get all applications
     */
    public List<Application> getAllApplications() {
        return new ArrayList<>(applicationList);
    }
    
    /**
     * Get count of confirmed/accepted applications for an internship
     */
    public long getAcceptedCount(InternshipOpportunity internship) {
        return applicationList.stream()
            .filter(app -> app.getInternship().equals(internship))
            .filter(app -> "Accepted".equals(app.getStatus()))
            .count();
    }
}
