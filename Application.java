/**
 * Represents an internship application submitted by a student in the system.
 * <p>
 * An application tracks the entire lifecycle from submission to final status:
 * <ul>
 *   <li><b>Pending</b> - awaiting company representative review</li>
 *   <li><b>Successful</b> - approved by company representative</li>
 *   <li><b>Unsuccessful</b> - rejected by company representative</li>
 *   <li><b>Accepted</b> - student accepted the successful application</li>
 * </ul>
 * <p>
 * Students can request withdrawal from accepted internships, which requires
 * Career Center Staff approval. The withdrawal request state is tracked separately.
 * </p>
 * <p>
 * <b>Business Rules:</b>
 * <ul>
 *   <li>Students can have maximum 3 active applications</li>
 *   <li>Students can only accept 1 internship</li>
 *   <li>Applications cannot be modified once finalized</li>
 * </ul>
 *
 * @see Student
 * @see InternshipOpportunity
 * @see CompanyRepresentative
 * @see CareerCenterStaff
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class Application {
    private Student student;
    private InternshipOpportunity internship;
    private String status; // Pending, Successful, Unsuccessful, Accepted
    private boolean withdrawalRequested;
    
    /**
     * Constructs a new Application for the specified student and internship.
     * <p>
     * The application is created with a default status of "Pending" and
     * no withdrawal request.
     * </p>
     *
     * @param student    the {@link Student} submitting the application
     * @param internship the {@link InternshipOpportunity} being applied for
     */
    public Application(Student student, InternshipOpportunity internship) {
        this.student = student;
        this.internship = internship;
        this.status = "Pending";
        this.withdrawalRequested = false;
    }
    
    /**
     * Updates the status of this application.
     * <p>
     * Status transitions:
     * <ul>
     *   <li>Pending → Successful (company representative approval)</li>
     *   <li>Pending → Unsuccessful (company representative rejection)</li>
     *   <li>Successful → Accepted (student acceptance)</li>
     * </ul>
     *
     * @param newStatus the new status to set
     */
    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
    
    /**
     * Marks that the student has requested withdrawal from this application.
     * <p>
     * This method is called when a student requests to withdraw from an accepted
     * internship. The withdrawal requires Career Center Staff approval.
     * </p>
     */
    public void markWithdrawalRequested() {
        this.withdrawalRequested = true;
    }
    
    /**
     * Retrieves the student who submitted this application.
     *
     * @return the {@link Student} object
     */
    public Student getStudent() {
        return student;
    }
    
    /**
     * Retrieves the internship opportunity for this application.
     *
     * @return the {@link InternshipOpportunity} object
     */
    public InternshipOpportunity getInternship() {
        return internship;
    }
    
    /**
     * Retrieves the current status of this application.
     *
     * @return the status ("Pending", "Successful", "Unsuccessful", or "Accepted")
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Checks if a withdrawal has been requested for this application.
     *
     * @return {@code true} if withdrawal has been requested, {@code false} otherwise
     */
    public boolean isWithdrawalRequested() {
        return withdrawalRequested;
    }
    
    /**
     * Sets the withdrawal request status for this application.
     *
     * @param withdrawalRequested {@code true} to mark as withdrawal requested,
     *                           {@code false} otherwise
     */
    public void setWithdrawalRequested(boolean withdrawalRequested) {
        this.withdrawalRequested = withdrawalRequested;
    }
}
