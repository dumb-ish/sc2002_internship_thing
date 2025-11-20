import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Criteria-based filter implementation for general internship filtering.
 * Used by Company Representatives and Staff for simple criteria-based filtering.
 * Implements ICriteriaFilter and IInternshipSorter interfaces.
 */
public class CriteriaBasedFilter implements ICriteriaFilter, IInternshipSorter {
    
    /**
     * Apply filter criteria to a list of internship opportunities
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
     * Check if opportunity matches level filter
     */
    private boolean matchesLevel(InternshipOpportunity opp, String level) {
        if (level == null || level.isEmpty()) {
            return true;
        }
        return opp.getLevel().equalsIgnoreCase(level);
    }
    
    /**
     * Check if opportunity matches major filter
     */
    private boolean matchesMajor(InternshipOpportunity opp, String major) {
        if (major == null || major.isEmpty()) {
            return true;
        }
        return opp.getPreferredMajor().equalsIgnoreCase(major);
    }
    
    /**
     * Check if opportunity matches status filter
     */
    private boolean matchesStatus(InternshipOpportunity opp, String status) {
        if (status == null || status.isEmpty()) {
            return true;
        }
        return opp.getStatus().equalsIgnoreCase(status);
    }
    
    /**
     * Check if opportunity matches closing date filter
     */
    private boolean matchesClosingDate(InternshipOpportunity opp, LocalDate closingDate) {
        if (closingDate == null) {
            return true;
        }
        // Include opportunities that close on or before the specified date
        return !opp.getClosingDate().isAfter(closingDate);
    }
    
    /**
     * Sort opportunities alphabetically by title
     */
    @Override
    public List<InternshipOpportunity> sortAlphabetically(List<InternshipOpportunity> list) {
        return list.stream()
            .sorted((o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()))
            .collect(Collectors.toList());
    }
}
