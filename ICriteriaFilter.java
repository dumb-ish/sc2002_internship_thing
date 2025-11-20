import java.util.List;

/**
 * Defines the contract for criteria-based filtering of internship opportunities.
 * <p>
 * This interface is a key demonstration of the <b>Interface Segregation Principle (ISP)</b>,
 * one of the SOLID principles. Instead of creating one large filter interface with multiple
 * responsibilities, the system uses three focused interfaces ({@code ICriteriaFilter},
 * {@link IStudentEligibilityFilter}, {@link IInternshipSorter}), each with a single,
 * well-defined purpose.
 * </p>
 * 
 * <h2>Design Principles</h2>
 * <ul>
 *   <li><b>Interface Segregation Principle (ISP)</b>: Clients should not be forced to depend
 *       on interfaces they don't use. This interface focuses solely on criteria-based filtering,
 *       allowing implementations to choose only the filtering capabilities they need.</li>
 *   <li><b>Dependency Inversion Principle (DIP)</b>: High-level modules like {@link InternshipManager}
 *       depend on this abstraction rather than concrete implementations, enabling flexible
 *       filter strategy selection at runtime.</li>
 *   <li><b>Strategy Pattern</b>: This interface defines a strategy for filtering internships
 *       based on criteria, allowing different filtering algorithms to be swapped interchangeably.</li>
 * </ul>
 * 
 * <h2>Implementation Guidelines</h2>
 * <p>
 * Implementations should handle filtering based on various criteria including:
 * </p>
 * <ul>
 *   <li>Internship level (Basic, Intermediate, Advanced)</li>
 *   <li>Preferred major</li>
 *   <li>Status (Open, Closed, etc.)</li>
 *   <li>Closing date ranges</li>
 * </ul>
 * 
 * <h2>Why Focused Interfaces Matter</h2>
 * <p>
 * By separating criteria-based filtering from student eligibility checks and sorting operations,
 * the system achieves:
 * </p>
 * <ul>
 *   <li><b>Flexibility</b>: Classes can implement only the filtering behaviors they need</li>
 *   <li><b>Clarity</b>: Each interface has a clear, single responsibility</li>
 *   <li><b>Maintainability</b>: Changes to one type of filtering don't affect others</li>
 *   <li><b>Testability</b>: Each filtering concern can be tested independently</li>
 * </ul>
 * 
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 * @see IStudentEligibilityFilter
 * @see IInternshipSorter
 * @see CriteriaBasedFilter
 * @see StudentEligibilityFilter
 * @see FilterCriteria
 * @see InternshipManager
 */
public interface ICriteriaFilter {
    /**
     * Applies filter criteria to a list of internship opportunities and returns matching results.
     * <p>
     * This method enables flexible filtering based on multiple criteria such as level, major,
     * status, and closing date. If no criteria are provided or criteria has no active filters,
     * the original list should be returned unchanged.
     * </p>
     * <p>
     * <b>Implementation Note</b>: Implementations should use a chain of filters approach,
     * applying each criterion sequentially to narrow down the results. Empty or null criteria
     * values should be treated as "no filter" for that specific criterion.
     * </p>
     * 
     * @param list the list of internship opportunities to filter; must not be null
     * @param criteria the filtering criteria containing level, major, status, and/or closing date filters;
     *                 may be null or empty to indicate no filtering
     * @return a filtered list containing only opportunities matching all specified criteria;
     *         returns the original list if no criteria are active
     * @see FilterCriteria
     * @see InternshipOpportunity
     */
    List<InternshipOpportunity> applyFilter(List<InternshipOpportunity> list, FilterCriteria criteria);
}
