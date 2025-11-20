import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Student-specific filter implementation with eligibility checking.
 * Handles both criteria-based filtering and student-specific eligibility rules.
 * Implements all three filter interfaces separately.
 */
public class StudentEligibilityFilter implements ICriteriaFilter, IStudentEligibilityFilter, IInternshipSorter {
    
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
     * Filter opportunities based on student eligibility rules
     */
    @Override
    public List<InternshipOpportunity> filterForStudent(List<InternshipOpportunity> list, Student student) {
        return list.stream()
            .filter(opp -> isEligibleForStudent(opp, student))
            .collect(Collectors.toList());
    }
    
    /**
     * Check if a student is eligible for an opportunity
     * Handles eligibility rules including:
     * - Visibility and application status
     * - Major matching
     * - Level restrictions for junior students (Year 1-2)
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
