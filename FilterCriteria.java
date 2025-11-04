import java.time.LocalDate;

/**
 * Represents filter criteria for internship opportunities.
 * Used by all users to filter and search internship opportunities.
 */
public class FilterCriteria {
    private String level;
    private String major;
    private String status;
    private LocalDate closingDate;

    /**
     * Default constructor
     */
    public FilterCriteria() {
        this.level = null;
        this.major = null;
        this.status = null;
        this.closingDate = null;
    }

    /**
     * Constructor with all fields
     */
    public FilterCriteria(String level, String major, String status, LocalDate closingDate) {
        this.level = level;
        this.major = major;
        this.status = status;
        this.closingDate = closingDate;
    }

    // Getters
    public String getLevel() {
        return level;
    }

    public String getMajor() {
        return major;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    // Setters
    public void setLevel(String level) {
        this.level = level;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    /**
     * Reset all filter criteria
     */
    public void reset() {
        this.level = null;
        this.major = null;
        this.status = null;
        this.closingDate = null;
    }

    /**
     * Check if any filter is set
     */
    public boolean hasFilters() {
        return level != null || major != null || status != null || closingDate != null;
    }
}
