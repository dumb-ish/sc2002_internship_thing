import java.util.List;

/**
 * Interface for student eligibility filtering.
 * Part of Interface Segregation Principle (ISP) - focused on student-specific filtering only.
 */
public interface IStudentEligibilityFilter {
    /**
     * Filter opportunities based on student eligibility rules
     * @param list The list of internships to filter
     * @param student The student to check eligibility for
     * @return List of internships the student is eligible for
     */
    List<InternshipOpportunity> filterForStudent(List<InternshipOpportunity> list, Student student);
}
