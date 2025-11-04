/**
 * Represents a Career Center Staff user in the system.
 * Staff members can approve representatives, internships, and manage withdrawals.
 */
public class CareerCenterStaff extends User {
    private String staffDepartment;
    
    /**
     * Constructor for CareerCenterStaff
     */
    public CareerCenterStaff(String userID, String name, String password, String staffDepartment) {
        super(userID, name, password);
        this.staffDepartment = staffDepartment;
    }
    
    // Getter
    public String getStaffDepartment() { 
        return staffDepartment; 
    }
    
    // Setter
    public void setStaffDepartment(String staffDepartment) {
        this.staffDepartment = staffDepartment;
    }
}
