import java.util.*;
import java.time.LocalDate;

class InternshipOpportunity {
    private String id;
    private String title;
    private String description;
    private String level;
    private String preferredMajor;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private String status;
    private String companyName;
    private String companyRepId;
    private int slots;
    private int appliedCount;
    private boolean visible;
    private List<InternshipApplication> applications;
    
    public InternshipOpportunity(String id, String title, String description, String level,
                               String preferredMajor, LocalDate openingDate, LocalDate closingDate,
                               String status, String companyName, String companyRepId, int slots, boolean initialVisibility) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.status = status;
        this.companyName = companyName;
        this.companyRepId = companyRepId;
        this.slots = slots;
        this.appliedCount = 0;
        this.visible = initialVisibility;
        this.applications = new ArrayList<>();
    }
    
    public InternshipOpportunity(String id, String title, String description, String level,
                               String preferredMajor, LocalDate openingDate, LocalDate closingDate,
                               String status, String companyName, String companyRepId, int slots) {
        this(id, title, description, level, preferredMajor, openingDate, closingDate,
             status, companyName, companyRepId, slots, false);
    }
    
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLevel() { return level; }
    public String getPreferredMajor() { return preferredMajor; }
    public LocalDate getOpeningDate() { return openingDate; }
    public LocalDate getClosingDate() { return closingDate; }
    public String getStatus() { return status; }
    public String getCompanyName() { return companyName; }
    public String getCompanyRepId() { return companyRepId; }
    public int getSlots() { return slots; }
    public boolean isVisible() { return visible; }
    public List<InternshipApplication> getApplications() { return applications; }
    
    public void setStatus(String status) { 
        this.status = status; 
        if (status.equals("Approved")) {
            this.visible = true;
        }
    }
    
    public void addApplication(InternshipApplication application) {
        if (!applications.contains(application)) {
            applications.add(application);
            appliedCount++;
        }
    }
    
    public InternshipApplication getApplicationByStudentId(String studentId) {
        return applications.stream()
            .filter(app -> app.getStudentId().equals(studentId))
            .findFirst()
            .orElse(null);
    }
    
    public boolean hasReachedApplicationLimit() {
        return appliedCount >= slots;
    }
    
    public void updateStatusAfterAcceptance() {
        long acceptedCount = applications.stream()
            .filter(app -> app.getStatus().equals("Accepted"))
            .count();
            
        if (acceptedCount >= slots) {
            this.status = "Filled";
        }
    }
    
    public void toggleVisibility() {
        this.visible = !this.visible;
    }
    
    @Override
    public String toString() {
        return "ID: " + id + ", Title: " + title + ", Company: " + companyName + 
               ", Level: " + level + ", Status: " + status + ", Slots: " + slots + 
               ", Applied: " + appliedCount + ", Visible: " + visible;
    }
}