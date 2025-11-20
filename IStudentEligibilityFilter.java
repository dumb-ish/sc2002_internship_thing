import java.util.List;

/**
 * Defines the contract for student-specific eligibility filtering of internship opportunities.
 * <p>
 * This interface represents a specialized filtering concern focused exclusively on determining
 * which internship opportunities a particular student is eligible to apply for based on
 * business rules and academic requirements.
 * </p>
 * 
 * <h2>Design Principles</h2>
 * <ul>
 *   <li><b>Interface Segregation Principle (ISP)</b>: This interface is intentionally narrow,
 *       focusing only on student eligibility checks. Classes that need general criteria filtering
 *       but not student-specific rules (like {@link CriteriaBasedFilter}) are not forced to
 *       implement this interface.</li>
 *   <li><b>Single Responsibility Principle (SRP)</b>: Handles only the concern of determining
 *       student eligibility, separating this complex business logic from general filtering.</li>
 *   <li><b>Strategy Pattern</b>: Allows different eligibility checking strategies to be
 *       implemented and injected into the system without changing client code.</li>
 * </ul>
 * 
 * <h2>Eligibility Rules</h2>
 * <p>
 * Implementations must enforce multiple eligibility rules including:
 * </p>
 * <ul>
 *   <li><b>Visibility Requirements</b>: Only visible opportunities should be considered</li>
 *   <li><b>Application Status</b>: Opportunities must be open for applications</li>
 *   <li><b>Major Matching</b>: Student's major must match the opportunity's preferred major</li>
 *   <li><b>Year-Based Level Restrictions</b>: 
 *       <ul>
 *         <li>Year 1-2 students: Eligible only for "Basic" level internships</li>
 *         <li>Year 3-4 students: Eligible for all levels (Basic, Intermediate, Advanced)</li>
 *       </ul>
 *   </li>
 * </ul>
 * 
 * <h2>Why This Interface is Separate</h2>
 * <p>
 * Student eligibility filtering involves complex, domain-specific business rules that differ
 * fundamentally from simple criteria-based filtering. By segregating this interface:
 * </p>
 * <ul>
 *   <li>Company Representatives and Staff don't need student-specific filtering logic</li>
 *   <li>Eligibility rules can evolve independently from other filtering concerns</li>
 *   <li>Student-facing features can depend only on relevant interfaces</li>
 *   <li>Testing becomes more focused and manageable</li>
 * </ul>
 * 
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 * @see ICriteriaFilter
 * @see IInternshipSorter
 * @see StudentEligibilityFilter
 * @see Student
 * @see InternshipOpportunity
 */
public interface IStudentEligibilityFilter {
    /**
     * Filters internship opportunities based on student-specific eligibility rules.
     * <p>
     * This method applies complex business logic to determine which opportunities a student
     * can legitimately apply for, considering factors like the student's year of study,
     * major, and the opportunity's visibility and level requirements.
     * </p>
     * <p>
     * <b>Implementation Requirements</b>:
     * </p>
     * <ul>
     *   <li>Must check opportunity visibility and application status</li>
     *   <li>Must enforce major matching (student major == preferred major)</li>
     *   <li>Must apply year-based level restrictions for junior students</li>
     *   <li>Should return an empty list if no eligible opportunities exist</li>
     * </ul>
     * 
     * @param list the list of internship opportunities to filter; must not be null
     * @param student the student for whom to check eligibility; must not be null,
     *                and must have valid major and year of study values
     * @return a filtered list containing only opportunities the student is eligible to apply for;
     *         returns an empty list if no opportunities match all eligibility criteria
     * @see Student#getMajor()
     * @see Student#getYearOfStudy()
     * @see InternshipOpportunity#getVisibility()
     * @see InternshipOpportunity#getLevel()
     */
    List<InternshipOpportunity> filterForStudent(List<InternshipOpportunity> list, Student student);
}
