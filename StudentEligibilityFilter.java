import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Comprehensive filter implementation with student-specific eligibility checking.
 * <p>
 * This class extends the capabilities of {@link CriteriaBasedFilter} by adding complex
 * student-specific eligibility rules, including major matching and year-based level restrictions.
 * It is the primary filtering strategy used for student-facing internship browsing and
 * application features.
 * </p>
 * 
 * <h2>Implemented Interfaces</h2>
 * <p>
 * This class implements all three filter interfaces, demonstrating <b>Interface Segregation
 * Principle (ISP)</b> and the <b>Strategy Pattern</b>:
 * </p>
 * <ul>
 *   <li>{@link ICriteriaFilter}: General criteria-based filtering (level, major, status, date)</li>
 *   <li>{@link IStudentEligibilityFilter}: Student-specific eligibility rules</li>
 *   <li>{@link IInternshipSorter}: Alphabetical sorting by title</li>
 * </ul>
 * <p>
 * Each interface is implemented separately, allowing this class to serve as a complete
 * filtering solution for student operations while still respecting interface segregation.
 * </p>
 * 
 * <h2>Design Patterns and Principles</h2>
 * <ul>
 *   <li><b>Strategy Pattern</b>: This class represents a complete filtering strategy that combines
 *       multiple filtering behaviors for student use cases</li>
 *   <li><b>Dependency Inversion Principle (DIP)</b>: {@link InternshipManager} depends on
 *       filter interfaces, allowing this implementation to be injected without tight coupling</li>
 *   <li><b>Single Responsibility Principle (SRP)</b>: Despite implementing multiple interfaces,
 *       each method has a single, clear purpose within the filtering domain</li>
 *   <li><b>Open/Closed Principle</b>: New eligibility rules can be added without modifying
 *       existing filtering logic</li>
 * </ul>
 * 
 * <h2>Student Eligibility Rules</h2>
 * <p>
 * The {@link #filterForStudent(List, Student)} method enforces critical business rules:
 * </p>
 * <ol>
 *   <li><b>Visibility Check</b>: Only opportunities with visibility=true are shown</li>
 *   <li><b>Application Status</b>: Only opportunities open for applications are included</li>
 *   <li><b>Major Matching</b>: Student's major must exactly match the preferred major
 *       (case-insensitive comparison)</li>
 *   <li><b>Year-Based Level Restrictions</b>:
 *       <ul>
 *         <li><b>Year 1-2 students (Junior)</b>: Eligible ONLY for "Basic" level internships</li>
 *         <li><b>Year 3-4 students (Senior)</b>: Eligible for ALL levels (Basic, Intermediate, Advanced)</li>
 *       </ul>
 *   </li>
 * </ol>
 * <p>
 * These rules ensure students only see opportunities they're qualified for, improving user
 * experience and reducing inappropriate applications.
 * </p>
 * 
 * <h2>Use Cases</h2>
 * <ul>
 *   <li>Student browsing available internships</li>
 *   <li>Filtering opportunities before application submission</li>
 *   <li>Displaying personalized internship recommendations</li>
 *   <li>Validating student eligibility during application process</li>
 * </ul>
 * 
 * <h2>Comparison with CriteriaBasedFilter</h2>
 * <table border="1">
 *   <caption>Comparison of Filtering Capabilities</caption>
 *   <tr>
 *     <th>Feature</th>
 *     <th>CriteriaBasedFilter</th>
 *     <th>StudentEligibilityFilter</th>
 *   </tr>
 *   <tr>
 *     <td>Criteria Filtering</td>
 *     <td>&#10003;</td>
 *     <td>&#10003;</td>
 *   </tr>
 *   <tr>
 *     <td>Student Eligibility</td>
 *     <td>&#10007;</td>
 *     <td>&#10003;</td>
 *   </tr>
 *   <tr>
 *     <td>Sorting</td>
 *     <td>&#10003;</td>
 *     <td>&#10003;</td>
 *   </tr>
 *   <tr>
 *     <td>Primary Users</td>
 *     <td>Staff, Company Reps</td>
 *     <td>Students</td>
 *   </tr>
 * </table>
 * 
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 * @see ICriteriaFilter
 * @see IStudentEligibilityFilter
 * @see IInternshipSorter
 * @see CriteriaBasedFilter
 * @see Student
 * @see InternshipOpportunity
 */
public class StudentEligibilityFilter implements ICriteriaFilter, IStudentEligibilityFilter, IInternshipSorter {
    
    /**
     * Applies multiple filter criteria to internship opportunities in a chain.
     * <p>
     * This method provides the same criteria-based filtering as {@link CriteriaBasedFilter},
     * processing opportunities through level, major, status, and closing date filters.
     * If criteria is null or empty, returns the original list unchanged.
     * </p>
     * <p>
     * <b>Note</b>: This method does NOT apply student-specific eligibility rules.
     * Use {@link #filterForStudent(List, Student)} for student eligibility checking.
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
     * Filters opportunities based on comprehensive student eligibility rules.
     * <p>
     * This is the core method for student-specific filtering, enforcing multiple business rules:
     * visibility, application status, major matching, and year-based level restrictions.
     * </p>
     * <p>
     * <b>Eligibility Requirements</b>:
     * </p>
     * <ol>
     *   <li>Opportunity must be visible ({@code visibility == true})</li>
     *   <li>Opportunity must be open for applications ({@code isOpenForApplications() == true})</li>
     *   <li>Student's major must match preferred major (case-insensitive)</li>
     *   <li>For Year 1-2 students: Only "Basic" level opportunities are eligible</li>
     *   <li>For Year 3-4 students: All levels (Basic, Intermediate, Advanced) are eligible</li>
     * </ol>
     * <p>
     * This method is called after general criteria filtering to further narrow results
     * based on what the student is actually qualified for.
     * </p>
     * 
     * @param list the list of internship opportunities to filter; must not be null
     * @param student the student for whom to check eligibility; must not be null with valid major and year
     * @return a filtered list of opportunities the student is eligible to apply for;
     *         may be empty if no opportunities meet all eligibility criteria
     */
    @Override
    public List<InternshipOpportunity> filterForStudent(List<InternshipOpportunity> list, Student student) {
        return list.stream()
            .filter(opp -> isEligibleForStudent(opp, student))
            .collect(Collectors.toList());
    }
    
    /**
     * Determines if a specific student is eligible for a specific opportunity.
     * <p>
     * This helper method encapsulates the complete eligibility checking logic, evaluating
     * multiple criteria in sequence. All criteria must pass for the student to be eligible.
     * </p>
     * <p>
     * <b>Evaluation Order</b>:
     * </p>
     * <ol>
     *   <li>Visibility and application status check</li>
     *   <li>Major matching validation</li>
     *   <li>Year-based level restriction enforcement</li>
     * </ol>
     * <p>
     * <b>Level Restriction Details</b>:
     * </p>
     * <ul>
     *   <li>Students in Year 1 or Year 2 (yearOfStudy &lt;= 2) can only apply to "Basic" level</li>
     *   <li>Students in Year 3 or Year 4 (yearOfStudy > 2) can apply to any level</li>
     * </ul>
     * 
     * @param opp the internship opportunity to evaluate
     * @param student the student to check eligibility for
     * @return true if the student meets all eligibility requirements; false otherwise
     */
    private boolean isEligibleForStudent(InternshipOpportunity opp, Student student) {
        // Must be visible and open for applications
        if (!opp.getVisibility() || !opp.isOpenForApplications()) {
            return false;
        }
        
        // Must match student's major (preferred major must be student's major)
        if (!opp.getPreferredMajor().equalsIgnoreCase(student.getMajor())) {
            return false;
        }
        
        // Year 1-2 students (Junior) can only apply for Basic level
        // Year 3+ students (Senior) can apply for any level
        if (student.getYearOfStudy() <= 2 && !"Basic".equals(opp.getLevel())) {
            return false;
        }
        
        return true;
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
