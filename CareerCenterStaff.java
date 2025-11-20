/**
 * Represents a Career Center staff member in the Internship Placement Management System.
 * <p>
 * Career Center staff members have administrative privileges and are responsible for:
 * <ul>
 *   <li>Approving or rejecting company representative registrations</li>
 *   <li>Reviewing and approving/rejecting internship opportunities</li>
 *   <li>Managing withdrawal requests from students</li>
 *   <li>Generating reports on internships and applications</li>
 * </ul>
 * <p>
 * Staff members have oversight over the entire internship placement process and
 * ensure quality control by vetting both representatives and internship postings.
 * </p>
 *
 * @see User
 * @see CompanyRepresentative
 * @see InternshipOpportunity
 * @see Application
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class CareerCenterStaff extends User {
    private String staffDepartment;
    
    /**
     * Constructs a new Career Center Staff member with the specified details.
     *
     * @param userID         the unique identifier for this staff member (e.g., "S001")
     * @param name           the staff member's full name
     * @param password       the authentication password
     * @param staffDepartment the department this staff member belongs to
     */
    public CareerCenterStaff(String userID, String name, String password, String staffDepartment) {
        super(userID, name, password);
        this.staffDepartment = staffDepartment;
    }
    
    /**
     * Retrieves the department this staff member belongs to.
     *
     * @return the staff department name
     */
    public String getStaffDepartment() { 
        return staffDepartment; 
    }
    
    /**
     * Updates the department for this staff member.
     *
     * @param staffDepartment the new department name
     */
    public void setStaffDepartment(String staffDepartment) {
        this.staffDepartment = staffDepartment;
    }
}
