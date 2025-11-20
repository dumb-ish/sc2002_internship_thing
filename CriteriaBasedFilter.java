import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple implementation of criteria-based filtering for internship opportunities.
 * <p>
 * This class provides straightforward filtering and sorting capabilities used primarily by
 * Company Representatives and Career Center Staff. Unlike {@link StudentEligibilityFilter},
 * this implementation does NOT include student-specific eligibility checks, making it suitable
 * for administrative and company-facing operations.
 * </p>
 * 
 * <h2>Implemented Interfaces</h2>
 * <p>
 * This class implements two interfaces, demonstrating <b>Interface Segregation Principle (ISP)</b>:
 * </p>
 * <ul>
 *   <li>{@link ICriteriaFilter}: Provides criteria-based filtering by level, major, status,
 *       and closing date</li>
 *   <li>{@link IInternshipSorter}: Provides alphabetical sorting by title</li>
 * </ul>
 * <p>
 * <b>Note</b>: This class intentionally does NOT implement {@link IStudentEligibilityFilter}
 * because Company Representatives and Staff don't need student-specific eligibility logic.
 * This is a key benefit of ISP - classes implement only the interfaces they actually need.
 * </p>
 * 
 * <h2>Design Patterns and Principles</h2>
 * <ul>
 *   <li><b>Strategy Pattern</b>: This class implements filtering and sorting strategies that
 *       can be injected into {@link InternshipManager} through interface references</li>
 *   <li><b>Dependency Inversion Principle (DIP)</b>: High-level components depend on the
 *       {@link ICriteriaFilter} and {@link IInternshipSorter} abstractions, not this concrete class</li>
 *   <li><b>Open/Closed Principle</b>: New filtering criteria can be added by extending this
 *       class without modifying existing code</li>
 * </ul>
 * 
 * <h2>Use Cases</h2>
 * <ul>
 *   <li>Company Representatives filtering their own posted internships</li>
 *   <li>Career Center Staff viewing and managing all internships by criteria</li>
 *   <li>Administrative reports and bulk operations on internship data</li>
 *   <li>Any scenario where user-agnostic filtering is needed</li>
 * </ul>
 * 
 * <h2>Filtering Behavior</h2>
 * <p>
 * Filtering uses a chain-of-responsibility approach where each criterion acts as a filter gate:
 * </p>
 * <ul>
 *   <li><b>Level</b>: Exact match (case-insensitive) on Basic/Intermediate/Advanced</li>
 *   <li><b>Major</b>: Exact match (case-insensitive) on preferred major</li>
 *   <li><b>Status</b>: Exact match (case-insensitive) on Open/Closed/etc.</li>
 *   <li><b>Closing Date</b>: Includes opportunities closing on or before the specified date</li>
 * </ul>
 * <p>
 * Empty or null criteria values are treated as "no filter" - they don't exclude any opportunities.
 * </p>
 * 
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 * @see ICriteriaFilter
 * @see IInternshipSorter
 * @see StudentEligibilityFilter
 * @see InternshipManager
 * @see FilterCriteria
 */
public class CriteriaBasedFilter implements ICriteriaFilter, IInternshipSorter {
    
    /**
     * Applies multiple filter criteria to internship opportunities in a chain.
     * <p>
     * This method processes the input list through a series of filter predicates, where each
     * criterion (level, major, status, closing date) acts as a gate. An opportunity must pass
     * all active filters to appear in the result.
     * </p>
     * <p>
     * If the criteria object is null or contains no active filters ({@link FilterCriteria#hasFilters()}
     * returns false), the original list is returned unchanged.
     * </p>
     * 
     * @param list the list of internship opportunities to filter; must not be null
     * @param criteria the filtering criteria; if null or empty, no filtering is performed
     * @return a filtered list containing opportunities matching all specified criteria
     */
    @Override
    public List<InternshipOpportunity> applyFilter(List<InternshipOpportunity> list, FilterCriteria criteria) {
        if (criteria == null || !criteria.hasFilters()) {
            return list;
        }
        
        return list.stream()
            .filter(opp -> matchesLevel(opp, criteria.getLevel()))
            .filter(opp -> matchesMajor(opp, criteria.getMajor()))
            .filter(opp -> matchesStatus(opp, criteria.getStatus()))
            .filter(opp -> matchesClosingDate(opp, criteria.getClosingDate()))
            .collect(Collectors.toList());
    }
    
    /**
     * Checks if an opportunity matches the specified level filter.
     * <p>
     * Performs case-insensitive comparison. If level is null or empty, returns true
     * (no filtering on this criterion).
     * </p>
     * 
     * @param opp the opportunity to check
     * @param level the level to match (Basic, Intermediate, Advanced); null or empty to skip this filter
     * @return true if the opportunity matches the level or if no level filter is specified
     */
    private boolean matchesLevel(InternshipOpportunity opp, String level) {
        if (level == null || level.isEmpty()) {
            return true;
        }
        return opp.getLevel().equalsIgnoreCase(level);
    }
    
    /**
     * Checks if an opportunity matches the specified major filter.
     * <p>
     * Performs case-insensitive comparison on the preferred major. If major is null or empty,
     * returns true (no filtering on this criterion).
     * </p>
     * 
     * @param opp the opportunity to check
     * @param major the major to match; null or empty to skip this filter
     * @return true if the opportunity's preferred major matches or if no major filter is specified
     */
    private boolean matchesMajor(InternshipOpportunity opp, String major) {
        if (major == null || major.isEmpty()) {
            return true;
        }
        return opp.getPreferredMajor().equalsIgnoreCase(major);
    }
    
    /**
     * Checks if an opportunity matches the specified status filter.
     * <p>
     * Performs case-insensitive comparison on the status. If status is null or empty,
     * returns true (no filtering on this criterion).
     * </p>
     * 
     * @param opp the opportunity to check
     * @param status the status to match (Open, Closed, etc.); null or empty to skip this filter
     * @return true if the opportunity's status matches or if no status filter is specified
     */
    private boolean matchesStatus(InternshipOpportunity opp, String status) {
        if (status == null || status.isEmpty()) {
            return true;
        }
        return opp.getStatus().equalsIgnoreCase(status);
    }
    
    /**
     * Checks if an opportunity matches the specified closing date filter.
     * <p>
     * Includes opportunities that close on or before the specified date. If closingDate is null,
     * returns true (no filtering on this criterion).
     * </p>
     * 
     * @param opp the opportunity to check
     * @param closingDate the maximum closing date; null to skip this filter
     * @return true if the opportunity closes on or before the specified date, or if no date filter is specified
     */
    private boolean matchesClosingDate(InternshipOpportunity opp, LocalDate closingDate) {
        if (closingDate == null) {
            return true;
        }
        // Include opportunities that close on or before the specified date
        return !opp.getClosingDate().isAfter(closingDate);
    }
    
    /**
     * Sorts opportunities alphabetically by title in ascending order (case-insensitive).
     * <p>
     * Creates a new sorted list; the original list is not modified. Uses case-insensitive
     * comparison to provide natural alphabetical ordering.
     * </p>
     * 
     * @param list the list of opportunities to sort; must not be null
     * @return a new list containing the same opportunities sorted by title
     */
    @Override
    public List<InternshipOpportunity> sortAlphabetically(List<InternshipOpportunity> list) {
        return list.stream()
            .sorted((o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()))
            .collect(Collectors.toList());
    }
}
