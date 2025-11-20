import java.util.List;

/**
 * Defines the contract for sorting internship opportunities.
 * <p>
 * This interface demonstrates the <b>Interface Segregation Principle (ISP)</b> by focusing
 * exclusively on sorting operations. Classes that need filtering capabilities but not sorting
 * are not forced to implement unnecessary methods.
 * </p>
 * 
 * <h2>Design Principles</h2>
 * <ul>
 *   <li><b>Interface Segregation Principle (ISP)</b>: By separating sorting from filtering
 *       and eligibility checking, this interface allows classes to implement only the
 *       operations they need. A class that only filters doesn't need to implement sorting.</li>
 *   <li><b>Single Responsibility Principle (SRP)</b>: This interface has one reason to change:
 *       if the sorting algorithm or requirements change.</li>
 *   <li><b>Strategy Pattern</b>: Different sorting strategies (alphabetical, by date, by company)
 *       can be implemented and swapped without affecting clients.</li>
 * </ul>
 * 
 * <h2>Sorting Strategy</h2>
 * <p>
 * Current implementations provide alphabetical sorting by internship title, which improves
 * user experience by presenting opportunities in a predictable, searchable order. This is
 * particularly useful when displaying filtered results to users.
 * </p>
 * 
 * <h2>Benefits of Separation</h2>
 * <p>
 * Separating sorting into its own interface provides:
 * </p>
 * <ul>
 *   <li><b>Flexibility</b>: Sorting can be implemented independently of filtering logic</li>
 *   <li><b>Composability</b>: Classes can combine sorting with other capabilities as needed</li>
 *   <li><b>Extensibility</b>: New sorting algorithms can be added without modifying existing code</li>
 *   <li><b>Optional Behavior</b>: Systems can choose whether to provide sorting functionality</li>
 * </ul>
 * 
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 * @see ICriteriaFilter
 * @see IStudentEligibilityFilter
 * @see CriteriaBasedFilter
 * @see StudentEligibilityFilter
 * @see InternshipOpportunity
 */
public interface IInternshipSorter {
    /**
     * Sorts internship opportunities alphabetically by title in ascending order.
     * <p>
     * This method provides a consistent, user-friendly ordering of internship opportunities
     * that makes it easier for users to browse and locate specific opportunities. The sorting
     * is case-insensitive to ensure natural alphabetical ordering.
     * </p>
     * <p>
     * <b>Implementation Note</b>: Implementations should perform case-insensitive comparison
     * of titles to provide intuitive alphabetical ordering (e.g., "Apple" and "apple" are
     * treated equivalently).
     * </p>
     * 
     * @param list the list of internship opportunities to sort; must not be null
     * @return a new list containing the same opportunities sorted alphabetically by title;
     *         the original list is not modified
     * @see InternshipOpportunity#getTitle()
     */
    List<InternshipOpportunity> sortAlphabetically(List<InternshipOpportunity> list);
}
