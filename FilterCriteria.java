import java.time.LocalDate;

/**
 * Represents filter criteria for searching and filtering internship opportunities.
 * <p>
 * This class provides a flexible filtering mechanism that allows all users (Students,
 * Company Representatives, Career Center Staff) to narrow down internship listings
 * based on multiple criteria. Filters can be applied individually or in combination.
 * </p>
 * <p>
 * Supported filter criteria:
 * <ul>
 *   <li><b>Level</b> - filters by internship difficulty (Basic, Intermediate, Advanced)</li>
 *   <li><b>Major</b> - filters by preferred academic major</li>
 *   <li><b>Status</b> - filters by approval/availability status (Pending, Approved, Rejected, Filled)</li>
 *   <li><b>Closing Date</b> - filters by application deadline</li>
 * </ul>
 * <p>
 * <b>Usage Pattern:</b>
 * <ul>
 *   <li>Create a FilterCriteria object with default (null) values</li>
 *   <li>Set desired filter criteria using setters</li>
 *   <li>Pass to filtering methods in InternshipFilter or InternshipManager</li>
 *   <li>Reset criteria when needed to clear all filters</li>
 * </ul>
 * <p>
 * <b>Note:</b> A null value for any criterion means "no filter applied" for that field.
 * Only non-null criteria are used during filtering operations.
 * </p>
 *
 * @see InternshipFilter
 * @see InternshipManager
 * @see User
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class FilterCriteria {
    private String level;
    private String major;
    private String status;
    private LocalDate closingDate;

    /**
     * Constructs a new FilterCriteria with all fields set to null.
     * <p>
     * This creates an empty filter where no criteria are applied, meaning
     * all internships will pass through this filter initially.
     * </p>
     */
    public FilterCriteria() {
        this.level = null;
        this.major = null;
        this.status = null;
        this.closingDate = null;
    }

    /**
     * Constructs a new FilterCriteria with the specified filter values.
     * <p>
     * Any parameter can be null to indicate that criterion should not be applied.
     * Only non-null criteria will be used during filtering operations.
     * </p>
     *
     * @param level       the internship level to filter by (Basic, Intermediate, Advanced), or null
     * @param major       the preferred major to filter by, or null
     * @param status      the status to filter by (Pending, Approved, Rejected, Filled), or null
     * @param closingDate the closing date to filter by, or null
     */
    public FilterCriteria(String level, String major, String status, LocalDate closingDate) {
        this.level = level;
        this.major = major;
        this.status = status;
        this.closingDate = closingDate;
    }

    // Getters
    /**
     * Retrieves the level filter criterion.
     *
     * @return the level to filter by (Basic, Intermediate, Advanced), or null if not filtering by level
     */
    public String getLevel() {
        return level;
    }

    /**
     * Retrieves the major filter criterion.
     *
     * @return the preferred major to filter by, or null if not filtering by major
     */
    public String getMajor() {
        return major;
    }

    /**
     * Retrieves the status filter criterion.
     *
     * @return the status to filter by (Pending, Approved, Rejected, Filled), or null if not filtering by status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Retrieves the closing date filter criterion.
     *
     * @return the closing date to filter by, or null if not filtering by closing date
     */
    public LocalDate getClosingDate() {
        return closingDate;
    }

    // Setters
    /**
     * Sets the level filter criterion.
     *
     * @param level the level to filter by (Basic, Intermediate, Advanced), or null to disable level filtering
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * Sets the major filter criterion.
     *
     * @param major the preferred major to filter by, or null to disable major filtering
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * Sets the status filter criterion.
     *
     * @param status the status to filter by (Pending, Approved, Rejected, Filled), or null to disable status filtering
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets the closing date filter criterion.
     *
     * @param closingDate the closing date to filter by, or null to disable closing date filtering
     */
    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    /**
     * Resets all filter criteria to null, effectively removing all filters.
     * <p>
     * After calling this method, the filter will not restrict any internships,
     * and all opportunities will pass through when this criteria is applied.
     * </p>
     */
    public void reset() {
        this.level = null;
        this.major = null;
        this.status = null;
        this.closingDate = null;
    }

    /**
     * Checks if any filter criterion has been set.
     * <p>
     * This is useful for determining whether filtering is active or if
     * all internships should be displayed without restriction.
     * </p>
     *
     * @return {@code true} if at least one filter criterion is set (non-null), {@code false} if all are null
     */
    public boolean hasFilters() {
        return level != null || major != null || status != null || closingDate != null;
    }
}
