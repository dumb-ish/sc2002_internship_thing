/**
 * Represents an application submitted by a student for an internship opportunity.
 * Tracks the status and withdrawal request state.
 */
public class Application {
    private Student student;
    private InternshipOpportunity internship;
    private String status; // Pending, Successful, Unsuccessful, Accepted
    private boolean withdrawalRequested;
    
    /**
     * Constructor for Application
     */
    public Application(Student student, InternshipOpportunity internship) {
        this.student = student;
        this.internship = internship;
        this.status = "Pending";
        this.withdrawalRequested = false;
    }
    
    /**
     * Update the application status
     */
    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
    
    /**
     * Mark that withdrawal has been requested
     */
    public void markWithdrawalRequested() {
        this.withdrawalRequested = true;
    }
    
    // Getters
    public Student getStudent() {
        return student;
    }
    
    public InternshipOpportunity getInternship() {
        return internship;
    }
    
    public String getStatus() {
        return status;
    }
    
    public boolean isWithdrawalRequested() {
        return withdrawalRequested;
    }
    
    // Setters
    public void setWithdrawalRequested(boolean withdrawalRequested) {
        this.withdrawalRequested = withdrawalRequested;
    }
}
