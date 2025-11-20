import java.util.List;

/**
 * Interface for criteria-based filtering of internship opportunities.
 * Part of Interface Segregation Principle (ISP) - focused on criteria filtering only.
 */
public interface ICriteriaFilter {
    /**
     * Apply filter criteria to a list of internship opportunities
     * @param list The list of internships to filter
     * @param criteria The filter criteria to apply
     * @return Filtered list of internships
     */
    List<InternshipOpportunity> applyFilter(List<InternshipOpportunity> list, FilterCriteria criteria);
}
