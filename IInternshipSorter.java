import java.util.List;

/**
 * Interface for sorting internship opportunities.
 * Part of Interface Segregation Principle (ISP) - focused on sorting only.
 */
public interface IInternshipSorter {
    /**
     * Sort opportunities alphabetically by title
     * @param list The list of internships to sort
     * @return Sorted list of internships
     */
    List<InternshipOpportunity> sortAlphabetically(List<InternshipOpportunity> list);
}
