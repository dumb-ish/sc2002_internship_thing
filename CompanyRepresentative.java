/**
 * Represents a Company Representative user in the system.
 * Company representatives can create internship opportunities and manage applications.
 */
public class CompanyRepresentative extends User {
    private String companyName;
    private String department;
    private String position;
    private String status; // Pending, Approved, Rejected
    
    /**
     * Constructor for CompanyRepresentative
     */
    public CompanyRepresentative(String userID, String name, String password, 
                                String companyName, String department, String position) {
        super(userID, name, password);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.status = "Pending"; // Default status
    }
    
    // Getters
    public String getCompanyName() { 
        return companyName; 
    }
    
    public String getDepartment() { 
        return department; 
    }
    
    public String getPosition() { 
        return position; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    // Setters
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    /**
     * Check if the representative is approved
     */
    public boolean isApproved() {
        return "Approved".equals(status);
    }
}
