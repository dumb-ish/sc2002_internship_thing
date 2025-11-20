/**
 * Represents a company representative user in the Internship Placement Management System.
 * <p>
 * Company representatives are responsible for:
 * <ul>
 *   <li>Creating up to 5 internship opportunities for their company</li>
 *   <li>Reviewing and managing student applications</li>
 *   <li>Approving or rejecting applications</li>
 *   <li>Toggling internship visibility</li>
 * </ul>
 * <p>
 * Representatives must be approved by Career Center Staff before they can create
 * internship opportunities. The approval status is tracked via the {@code status} field.
 * </p>
 *
 * @see User
 * @see InternshipOpportunity
 * @see Application
 * @see CareerCenterStaff
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class CompanyRepresentative extends User {
    private String companyName;
    private String department;
    private String position;
    private String status; // Pending, Approved, Rejected
    
    /**
     * Constructs a new Company Representative with the specified details.
     * <p>
     * The representative is created with a default status of "Pending" and must be
     * approved by Career Center Staff before creating internship opportunities.
     * </p>
     *
     * @param userID      the unique identifier for this representative (e.g., "CR001")
     * @param name        the representative's full name
     * @param password    the authentication password
     * @param companyName the name of the company this representative works for
     * @param department  the department within the company
     * @param position    the job title or position of the representative
     */
    public CompanyRepresentative(String userID, String name, String password, 
                                String companyName, String department, String position) {
        super(userID, name, password);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.status = "Pending"; // Default status
    }
    
    /**
     * Retrieves the name of the company this representative works for.
     *
     * @return the company name
     */
    public String getCompanyName() { 
        return companyName; 
    }
    
    /**
     * Retrieves the department within the company.
     *
     * @return the department name
     */
    public String getDepartment() { 
        return department; 
    }
    
    /**
     * Retrieves the job position of this representative.
     *
     * @return the position title
     */
    public String getPosition() { 
        return position; 
    }
    
    /**
     * Retrieves the approval status of this representative.
     * <p>
     * Possible values:
     * <ul>
     *   <li>"Pending" - awaiting approval from Career Center Staff</li>
     *   <li>"Approved" - approved and can create internships</li>
     *   <li>"Rejected" - registration rejected</li>
     * </ul>
     *
     * @return the current status
     */
    public String getStatus() { 
        return status; 
    }
    
    /**
     * Updates the approval status of this representative.
     * <p>
     * This method is typically called by Career Center Staff during the approval process.
     * </p>
     *
     * @param status the new status ("Pending", "Approved", or "Rejected")
     */
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    /**
     * Updates the company name for this representative.
     *
     * @param companyName the new company name
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    /**
     * Updates the department for this representative.
     *
     * @param department the new department name
     */
    public void setDepartment(String department) {
        this.department = department;
    }
    
    /**
     * Updates the position for this representative.
     *
     * @param position the new position title
     */
    public void setPosition(String position) {
        this.position = position;
    }
    
    /**
     * Checks if this representative has been approved by Career Center Staff.
     * <p>
     * Only approved representatives can create internship opportunities and
     * manage applications.
     * </p>
     *
     * @return {@code true} if the status is "Approved", {@code false} otherwise
     */
    public boolean isApproved() {
        return "Approved".equals(status);
    }
}
