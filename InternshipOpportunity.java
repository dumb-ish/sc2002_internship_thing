import java.time.LocalDate;

/**
 * Represents an internship opportunity in the system.
 * Contains all details about the internship including dates, requirements, and status.
 */
public class InternshipOpportunity {
    private String title;
    private String description;
    private String level; // Basic, Intermediate, Advanced
    private String preferredMajor;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private String status; // Pending, Approved, Rejected, Filled
    private boolean visibility;
    private String companyName;
    private String companyRepID; // ID of the representative in charge
    private int numSlots;

    /**
     * Constructor for InternshipOpportunity
     */
    public InternshipOpportunity(String title, String description, String level,
                                String preferredMajor, LocalDate openingDate, LocalDate closingDate,
                                String companyName, String companyRepID, int numSlots) {
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.status = "Pending"; // Default status
        this.visibility = false; // Default visibility
        this.companyName = companyName;
        this.companyRepID = companyRepID;
        this.numSlots = numSlots;
    }
    
    /**
     * Check if the internship is currently open for applications
     */
    public boolean isOpenForApplications() {
        LocalDate today = LocalDate.now();
        return visibility && 
               "Approved".equals(status) && 
               !today.isBefore(openingDate) && 
               !today.isAfter(closingDate) &&
               !"Filled".equals(status);
    }
    
    /**
     * Update the status of the internship
     */
    public void updateStatus(String newStatus) {
        this.status = newStatus;
        // Automatically set visibility to true when approved
        if ("Approved".equals(newStatus)) {
            this.visibility = true;
        }
    }
    
    // Getters
    public String getTitle() { 
        return title; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public String getLevel() { 
        return level; 
    }
    
    public String getPreferredMajor() { 
        return preferredMajor; 
    }
    
    public LocalDate getOpeningDate() { 
        return openingDate; 
    }
    
    public LocalDate getClosingDate() { 
        return closingDate; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public boolean getVisibility() { 
        return visibility; 
    }
    
    public String getCompanyName() { 
        return companyName; 
    }
    
    public String getCompanyRepID() { 
        return companyRepID; 
    }
    
    public int getNumSlots() { 
        return numSlots; 
    }
    
    // Setters
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setLevel(String level) {
        this.level = level;
    }
    
    public void setPreferredMajor(String preferredMajor) {
        this.preferredMajor = preferredMajor;
    }
    
    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }
    
    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public void setNumSlots(int numSlots) {
        this.numSlots = numSlots;
    }
}