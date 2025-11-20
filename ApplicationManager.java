import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages all application-related operations in the Internship Placement Management System.
 * <p>
 * The ApplicationManager handles the complete lifecycle of student applications for internships:
 * <ul>
 *   <li>Application submission with validation (3-application limit, eligibility checks)</li>
 *   <li>Application status management (Pending, Shortlisted, Successful, Unsuccessful, Accepted)</li>
 *   <li>Withdrawal request handling and approval workflow</li>
 *   <li>Automatic withdrawal of competing applications when a placement is accepted</li>
 *   <li>Integration with InternshipManager for slot availability updates</li>
 *   <li>Business rule enforcement (year-based eligibility, maximum applications)</li>
 * </ul>
 * <p>
 * <strong>Key Business Rules Enforced:</strong>
 * <ul>
 *   <li><strong>3-Application Limit:</strong> Students can maintain at most 3 active applications simultaneously</li>
 *   <li><strong>Year-Based Restrictions:</strong> Year 1-2 students can only apply for Basic level internships</li>
 *   <li><strong>Duplicate Prevention:</strong> Students cannot apply twice for the same internship</li>
 *   <li><strong>Status Finality:</strong> Finalized applications (Successful/Unsuccessful) cannot be modified</li>
 *   <li><strong>Automatic Cleanup:</strong> When a student accepts an offer, all other applications are withdrawn</li>
 * </ul>
 * <p>
 * <strong>SOLID Principles Demonstrated:</strong>
 * <ul>
 *   <li><strong>Single Responsibility:</strong> Focuses solely on application lifecycle management</li>
 *   <li><strong>Open/Closed:</strong> Business rules are encapsulated in methods, easy to extend</li>
 *   <li><strong>Dependency:</strong> Coordinates with InternshipManager for status updates</li>
 * </ul>
 *
 * @see Application
 * @see Student
 * @see InternshipOpportunity
 * @see InternshipManager
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class ApplicationManager {
    private List<Application> applicationList;
    
    /**
     * Constructs a new ApplicationManager with an empty application list.
     * <p>
     * Initializes the internal data structure for storing all applications in the system.
     * </p>
     */
    public ApplicationManager() {
        this.applicationList = new ArrayList<>();
    }
    
    /**
     * Submits a new internship application for a student.
     * <p>
     * This method performs comprehensive validation before creating an application:
     * <ul>
     *   <li>Checks for duplicate applications (same student + internship)</li>
     *   <li>Enforces 3-application limit per student (only counts active applications)</li>
     *   <li>Validates year-based eligibility (Year 1-2 students restricted to Basic level)</li>
     *   <li>Verifies the internship is open for applications (status and date checks)</li>
     * </ul>
     * If all validations pass, a new Application object is created with "Pending" status.
     *
     * @param student    the Student submitting the application
     * @param internship the InternshipOpportunity to apply for
     * @return true if the application was successfully created, false if validation failed
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
     * Updates the status of an application with validation to prevent modification of finalized applications.
     * <p>
     * Valid status transitions:
     * <ul>
     *   <li>Pending → Shortlisted (company representative reviews)</li>
     *   <li>Shortlisted → Successful/Unsuccessful (company representative decides)</li>
     *   <li>Successful → Accepted (student accepts offer)</li>
     * </ul>
     * <p>
     * <strong>Important:</strong> Applications with "Successful" or "Unsuccessful" status are considered
     * finalized and cannot be changed. This prevents accidental modification of completed decisions.
     * </p>
     *
     * @param app    the Application to update
     * @param status the new status to set ("Pending", "Shortlisted", "Successful", "Unsuccessful", "Accepted")
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
     * Initiates a withdrawal request for an application.
     * <p>
     * This method marks the application as having a pending withdrawal request.
     * The actual withdrawal must be approved by Career Center Staff using
     * {@link #approveWithdrawal(Application, InternshipManager)}.
     * </p>
     *
     * @param app the Application for which withdrawal is requested
     */
    public void handleWithdrawal(Application app) {
        app.markWithdrawalRequested();
    }
    
    /**
     * Approves a withdrawal request and removes the application from the system.
     * <p>
     * This method has important side effects:
     * <ul>
     *   <li>Permanently removes the application from the system</li>
     *   <li>If the application was "Accepted" and the internship is "Filled", checks if the
     *       internship should be reverted to "Approved" status (making it available again)</li>
     *   <li>Coordinates with InternshipManager to update slot availability</li>
     * </ul>
     *
     * @param app                the Application to approve withdrawal for
     * @param internshipManager  the InternshipManager to coordinate status updates with
     */
    public void approveWithdrawal(Application app, InternshipManager internshipManager) {
        InternshipOpportunity internship = app.getInternship();
        String appStatus = app.getStatus();
        
        // Remove the application
        applicationList.remove(app);
        
        // If the application was Accepted and internship is Filled, revert to Approved
        if ("Accepted".equals(appStatus) && "Filled".equals(internship.getStatus())) {
            long remainingAccepted = getAcceptedCount(internship);
            if (remainingAccepted < internship.getNumSlots()) {
                internshipManager.revertFilledStatus(internship);
            }
        }
    }
    
    /**
     * Rejects a withdrawal request, keeping the application in the system.
     * <p>
     * The application status remains unchanged, and the withdrawal request flag is cleared.
     * </p>
     *
     * @param app the Application to reject withdrawal for
     */
    public void rejectWithdrawal(Application app) {
        app.setWithdrawalRequested(false);
        // Status remains as it was before withdrawal request
    }
    
    /**
     * Retrieves all applications submitted by a specific student.
     * <p>
     * This method uses streams for efficient filtering and returns all applications
     * regardless of status (including withdrawn applications).
     * </p>
     *
     * @param student the Student whose applications to retrieve
     * @return a list of Application objects submitted by the student
     */
    public List<Application> getApplicationsByStudent(Student student) {
        return applicationList.stream()
            .filter(app -> app.getStudent().equals(student))
            .collect(Collectors.toList());
    }
    
    /**
     * Retrieves all applications for a specific internship opportunity.
     * <p>
     * Used by company representatives to view applicants for their internship postings.
     * </p>
     *
     * @param internship the InternshipOpportunity to get applications for
     * @return a list of Application objects for the specified internship
     */
    public List<Application> getApplicationsByInternship(InternshipOpportunity internship) {
        return applicationList.stream()
            .filter(app -> app.getInternship().equals(internship))
            .collect(Collectors.toList());
    }
    
    /**
     * Retrieves all applications with pending withdrawal requests.
     * <p>
     * Used by Career Center Staff to review and process withdrawal requests from students.
     * </p>
     *
     * @return a list of Application objects with pending withdrawal requests
     */
    public List<Application> getWithdrawalRequests() {
        return applicationList.stream()
            .filter(Application::isWithdrawalRequested)
            .collect(Collectors.toList());
    }
    
    /**
     * Checks if a student has already applied for a specific internship.
     * <p>
     * This is a helper method used during application submission to prevent duplicate applications.
     * </p>
     *
     * @param student    the Student to check
     * @param internship the InternshipOpportunity to check
     * @return true if the student has already applied, false otherwise
     */
    private boolean hasApplied(Student student, InternshipOpportunity internship) {
        return applicationList.stream()
            .anyMatch(app -> app.getStudent().equals(student) && 
                           app.getInternship().equals(internship));
    }
    
    /**
     * Counts the number of active applications for a student.
     * <p>
     * Active applications are those with any status except "Withdrawn".
     * This count is used to enforce the 3-application limit per student.
     * </p>
     *
     * @param student the Student to count applications for
     * @return the number of active applications (non-withdrawn)
     */
    private long getActiveApplicationsCount(Student student) {
        return applicationList.stream()
            .filter(app -> app.getStudent().equals(student))
            .filter(app -> !"Withdrawn".equals(app.getStatus()))
            .count();
    }
    
    /**
     * Accepts an internship placement offer and withdraws all other applications for the student.
     * <p>
     * This method implements the business rule that a student can only accept one internship placement.
     * When a student accepts an offer:
     * <ul>
     *   <li>The accepted application status is changed to "Accepted"</li>
     *   <li>All other applications for the same student are automatically withdrawn and removed</li>
     *   <li>This frees up slots for other students in those internships</li>
     * </ul>
     * <p>
     * <strong>Important:</strong> Only applications with "Successful" status can be accepted.
     * This ensures the company representative has already approved the student before acceptance.
     * </p>
     *
     * @param student      the Student accepting the offer
     * @param acceptedApp  the Application being accepted (must have "Successful" status)
     * @return true if acceptance was successful, false if the application is not in "Successful" status
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
     * Retrieves all applications in the system.
     * <p>
     * Returns a defensive copy to prevent external modification of the internal list.
     * </p>
     *
     * @return a new ArrayList containing all applications
     */
    public List<Application> getAllApplications() {
        return new ArrayList<>(applicationList);
    }
    
    /**
     * Counts the number of accepted applications for a specific internship.
     * <p>
     * This count is used to determine if an internship has reached its slot limit
     * and should be marked as "Filled". Only applications with "Accepted" status are counted.
     * </p>
     *
     * @param internship the InternshipOpportunity to count accepted applications for
     * @return the number of accepted applications
     */
    public long getAcceptedCount(InternshipOpportunity internship) {
        return applicationList.stream()
            .filter(app -> app.getInternship().equals(internship))
            .filter(app -> "Accepted".equals(app.getStatus()))
            .count();
    }
}
