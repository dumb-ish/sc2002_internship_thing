import java.time.LocalDate;

/**
 * Represents an internship opportunity posted by a company in the system.
 * <p>
 * An internship opportunity contains all the necessary details about an internship position,
 * including the job requirements, application timeline, company information, and approval status.
 * The lifecycle of an internship goes through several stages from creation to completion.
 * </p>
 * <p>
 * Key features and responsibilities:
 * <ul>
 *   <li>Stores comprehensive internship details (title, description, level, major requirements)</li>
 *   <li>Manages application timeline with opening and closing dates</li>
 *   <li>Tracks approval status and visibility to students</li>
 *   <li>Controls number of available positions (slots)</li>
 *   <li>Links to the company and responsible representative</li>
 * </ul>
 * <p>
 * <b>Status Flow:</b>
 * <ul>
 *   <li><b>Pending</b> - newly created, awaiting Career Center Staff approval</li>
 *   <li><b>Approved</b> - approved by staff, visible to students</li>
 *   <li><b>Rejected</b> - rejected by staff, not visible</li>
 *   <li><b>Filled</b> - all slots filled, no longer accepting applications</li>
 * </ul>
 * <p>
 * <b>Business Rules:</b>
 * <ul>
 *   <li>Internships must be approved before becoming visible to students</li>
 *   <li>Applications are only accepted during the opening-closing date window</li>
 *   <li>Visibility is automatically enabled when status changes to "Approved"</li>
 *   <li>Level must be one of: Basic, Intermediate, Advanced</li>
 * </ul>
 *
 * @see CompanyRepresentative
 * @see CareerCenterStaff
 * @see Application
 * @see InternshipManager
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class InternshipOpportunity {
    private String title;
    private String description;
    /** Internship difficulty level: Basic, Intermediate, or Advanced */
    private String level;
    private String preferredMajor;
    private LocalDate openingDate;
    private LocalDate closingDate;
    /** Current status: Pending, Approved, Rejected, or Filled */
    private String status;
    /** Whether the internship is visible to students (automatically set when approved) */
    private boolean visibility;
    private String companyName;
    /** ID of the company representative managing this internship */
    private String companyRepID;
    /** Number of available positions for this internship */
    private int numSlots;

    /**
     * Constructs a new InternshipOpportunity with the specified details.
     * <p>
     * The internship is created with default status "Pending" and visibility set to false.
     * It will require Career Center Staff approval before becoming visible to students.
     * </p>
     *
     * @param title          the job title of the internship position
     * @param description    detailed description of the internship role and responsibilities
     * @param level          the difficulty level (Basic, Intermediate, or Advanced)
     * @param preferredMajor the preferred academic major for applicants
     * @param openingDate    the date when applications open
     * @param closingDate    the date when applications close
     * @param companyName    the name of the company offering the internship
     * @param companyRepID   the ID of the company representative managing this opportunity
     * @param numSlots       the number of available positions
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
     * Checks if the internship is currently open and accepting student applications.
     * <p>
     * An internship is open for applications only when all of the following conditions are met:
     * <ul>
     *   <li>The internship is visible to students</li>
     *   <li>The status is "Approved"</li>
     *   <li>The current date is within the opening and closing date range</li>
     *   <li>The status is not "Filled"</li>
     * </ul>
     *
     * @return {@code true} if students can currently apply, {@code false} otherwise
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
     * Updates the status of this internship opportunity.
     * <p>
     * When the status is changed to "Approved", the visibility is automatically
     * set to true, making the internship visible to students.
     * </p>
     *
     * @param newStatus the new status to set (Pending, Approved, Rejected, or Filled)
     */
    public void updateStatus(String newStatus) {
        this.status = newStatus;
        // Automatically set visibility to true when approved
        if ("Approved".equals(newStatus)) {
            this.visibility = true;
        }
    }
    
    // Getters
    /**
     * Retrieves the title of this internship position.
     *
     * @return the job title
     */
    public String getTitle() { 
        return title; 
    }
    
    /**
     * Retrieves the detailed description of this internship.
     *
     * @return the internship description
     */
    public String getDescription() { 
        return description; 
    }
    
    /**
     * Retrieves the difficulty level of this internship.
     *
     * @return the level (Basic, Intermediate, or Advanced)
     */
    public String getLevel() { 
        return level; 
    }
    
    /**
     * Retrieves the preferred academic major for applicants.
     *
     * @return the preferred major
     */
    public String getPreferredMajor() { 
        return preferredMajor; 
    }
    
    /**
     * Retrieves the date when applications open for this internship.
     *
     * @return the opening date
     */
    public LocalDate getOpeningDate() { 
        return openingDate; 
    }
    
    /**
     * Retrieves the date when applications close for this internship.
     *
     * @return the closing date
     */
    public LocalDate getClosingDate() { 
        return closingDate; 
    }
    
    /**
     * Retrieves the current status of this internship.
     *
     * @return the status (Pending, Approved, Rejected, or Filled)
     */
    public String getStatus() { 
        return status; 
    }
    
    /**
     * Retrieves the visibility status of this internship.
     *
     * @return {@code true} if visible to students, {@code false} otherwise
     */
    public boolean getVisibility() { 
        return visibility; 
    }
    
    /**
     * Retrieves the name of the company offering this internship.
     *
     * @return the company name
     */
    public String getCompanyName() { 
        return companyName; 
    }
    
    /**
     * Retrieves the ID of the company representative managing this internship.
     *
     * @return the company representative's ID
     */
    public String getCompanyRepID() { 
        return companyRepID; 
    }
    
    /**
     * Retrieves the number of available positions for this internship.
     *
     * @return the number of slots
     */
    public int getNumSlots() { 
        return numSlots; 
    }
    
    // Setters
    /**
     * Sets the title of this internship position.
     *
     * @param title the new job title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Sets the description of this internship.
     *
     * @param description the new internship description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Sets the difficulty level of this internship.
     *
     * @param level the new level (Basic, Intermediate, or Advanced)
     */
    public void setLevel(String level) {
        this.level = level;
    }
    
    /**
     * Sets the preferred academic major for applicants.
     *
     * @param preferredMajor the new preferred major
     */
    public void setPreferredMajor(String preferredMajor) {
        this.preferredMajor = preferredMajor;
    }
    
    /**
     * Sets the date when applications open for this internship.
     *
     * @param openingDate the new opening date
     */
    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }
    
    /**
     * Sets the date when applications close for this internship.
     *
     * @param closingDate the new closing date
     */
    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }
    
    /**
     * Sets the status of this internship.
     * <p>
     * <b>Note:</b> Use {@link #updateStatus(String)} instead to ensure
     * visibility is automatically managed when status changes to "Approved".
     * </p>
     *
     * @param status the new status (Pending, Approved, Rejected, or Filled)
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Sets the visibility status of this internship.
     *
     * @param visibility {@code true} to make visible to students, {@code false} to hide
     */
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
    
    /**
     * Sets the name of the company offering this internship.
     *
     * @param companyName the new company name
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    /**
     * Sets the number of available positions for this internship.
     *
     * @param numSlots the new number of slots
     */
    public void setNumSlots(int numSlots) {
        this.numSlots = numSlots;
    }
}