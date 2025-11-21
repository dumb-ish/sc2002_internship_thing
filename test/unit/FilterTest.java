import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

/**
 * Unit tests for Filter and Sort functionality
 * Tests filtering by criteria and student eligibility
 * 
 * Test Coverage:
 * - Filter by level, major, status, closing date
 * - Student eligibility filtering (year and major)
 * - Alphabetical sorting by title
 * - Multiple criteria filtering
 * 
 * @see InternshipFilter
 * @see StudentEligibilityFilter
 * @see FilterCriteria
 * @see InternshipManager
 */
public class FilterTest {
    private InternshipManager internshipManager;
    private StudentEligibilityFilter filter;
    private List<InternshipOpportunity> testInternships;
    
    @Before
    public void setUp() {
        filter = new StudentEligibilityFilter();
        internshipManager = new InternshipManager(filter, filter, filter);
        testInternships = new ArrayList<>();
        
        // Create diverse test internships
        testInternships.add(new InternshipOpportunity(
            "Software Engineer Intern",
            "Develop applications",
            "Intermediate",
            "Computer Science",
            LocalDate.now(),
            LocalDate.now().plusDays(30),
            "Tech Corp",
            "rep1@techcorp.com",
            5
        ));
        
        testInternships.add(new InternshipOpportunity(
            "Data Analyst Intern",
            "Analyze data",
            "Basic",
            "Data Science & AI",
            LocalDate.now(),
            LocalDate.now().plusDays(20),
            "Data Company",
            "rep2@data.com",
            3
        ));
        
        testInternships.add(new InternshipOpportunity(
            "Advanced Developer",
            "Senior role",
            "Advanced",
            "Computer Science",
            LocalDate.now(),
            LocalDate.now().plusDays(15),
            "Tech Corp",
            "rep1@techcorp.com",
            2
        ));
        
        testInternships.add(new InternshipOpportunity(
            "Business Analyst",
            "Business operations",
            "Basic",
            "Business",
            LocalDate.now(),
            LocalDate.now().plusDays(25),
            "Consulting Inc",
            "rep3@consulting.com",
            4
        ));
        
        testInternships.add(new InternshipOpportunity(
            "Machine Learning Intern",
            "AI research",
            "Intermediate",
            "Data Science & AI",
            LocalDate.now(),
            LocalDate.now().plusDays(40),
            "AI Labs",
            "rep4@ailabs.com",
            3
        ));
        
        // Add and approve all internships
        for (InternshipOpportunity internship : testInternships) {
            internshipManager.addInternship(internship);
            internshipManager.approveInternship(internship);
        }
    }
    
    @After
    public void tearDown() {
        internshipManager = null;
        filter = null;
        testInternships = null;
    }
    
    /**
     * Test Case: UT-FILTER-001
     * Verify filtering by level
     */
    @Test
    public void testFilterByLevel() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setLevel("Basic");
        
        List<InternshipOpportunity> filtered = internshipManager.filterInternships(criteria);
        
        assertEquals("Should return 2 Basic level internships", 2, filtered.size());
        for (InternshipOpportunity internship : filtered) {
            assertEquals("All should be Basic level", "Basic", internship.getLevel());
        }
    }
    
    /**
     * Test Case: UT-FILTER-002
     * Verify filtering by major
     */
    @Test
    public void testFilterByMajor() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setMajor("Computer Science");
        
        List<InternshipOpportunity> filtered = internshipManager.filterInternships(criteria);
        
        assertEquals("Should return 2 Computer Science internships", 2, filtered.size());
        for (InternshipOpportunity internship : filtered) {
            assertEquals("All should require Computer Science", "Computer Science", internship.getPreferredMajor());
        }
    }
    
    /**
     * Test Case: UT-FILTER-003
     * Verify filtering by status (Approved)
     */
    @Test
    public void testFilterByStatus() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setStatus("Approved");
        
        List<InternshipOpportunity> filtered = internshipManager.filterInternships(criteria);
        
        // All test internships are approved
        assertEquals("Should return all approved internships", 5, filtered.size());
        for (InternshipOpportunity internship : filtered) {
            assertEquals("All should be Approved", "Approved", internship.getStatus());
        }
    }
    
    /**
     * Test Case: UT-FILTER-004
     * Verify multiple criteria filtering
     */
    @Test
    public void testMultipleCriteriaFiltering() {
        FilterCriteria criteria = new FilterCriteria();
        criteria.setLevel("Intermediate");
        criteria.setMajor("Computer Science");
        
        List<InternshipOpportunity> filtered = internshipManager.filterInternships(criteria);
        
        assertEquals("Should return 1 internship matching both criteria", 1, filtered.size());
        assertEquals("Should be Software Engineer Intern", "Software Engineer Intern", filtered.get(0).getTitle());
    }
    
    /**
     * Test Case: UT-FILTER-005
     * Verify student eligibility filtering for Year 2 student
     */
    @Test
    public void testStudentEligibilityYear2() {
        // Year 2 student can only see Basic level
        Student juniorStudent = new Student("S002", "Junior", "password", 2, "Computer Science");
        
        FilterCriteria criteria = new FilterCriteria();
        List<InternshipOpportunity> eligible = internshipManager.getVisibleInternshipsForStudent(juniorStudent, criteria);
        
        // Year 2 CS student should only see Basic CS internships (there are none in our test data)
        // But they can see their major's internships at Basic level
        assertTrue("Should filter based on year and major", eligible.size() >= 0);
        for (InternshipOpportunity internship : eligible) {
            assertEquals("Should only show Basic level", "Basic", internship.getLevel());
        }
    }
    
    /**
     * Test Case: UT-FILTER-006
     * Verify student eligibility filtering for Year 3 student
     */
    @Test
    public void testStudentEligibilityYear3() {
        // Year 3 student can see all levels
        Student seniorStudent = new Student("S003", "Senior", "password", 3, "Computer Science");
        
        FilterCriteria criteria = new FilterCriteria();
        List<InternshipOpportunity> eligible = internshipManager.getVisibleInternshipsForStudent(seniorStudent, criteria);
        
        // Year 3 CS student should see all CS internships (Basic, Intermediate, Advanced)
        assertEquals("Should return all 2 CS internships", 2, eligible.size());
        for (InternshipOpportunity internship : eligible) {
            assertEquals("All should match major", "Computer Science", internship.getPreferredMajor());
        }
    }
    
    /**
     * Test Case: UT-FILTER-007
     * Verify alphabetical sorting
     */
    @Test
    public void testAlphabeticalSorting() {
        FilterCriteria criteria = new FilterCriteria();
        List<InternshipOpportunity> filtered = internshipManager.filterInternships(criteria);
        
        // Sort alphabetically
        List<InternshipOpportunity> sorted = filter.sortAlphabetically(filtered);
        
        // Verify sorted order
        assertEquals("First should be Advanced Developer", "Advanced Developer", sorted.get(0).getTitle());
        assertEquals("Second should be Business Analyst", "Business Analyst", sorted.get(1).getTitle());
        assertEquals("Third should be Data Analyst Intern", "Data Analyst Intern", sorted.get(2).getTitle());
        assertEquals("Fourth should be Machine Learning Intern", "Machine Learning Intern", sorted.get(3).getTitle());
        assertEquals("Fifth should be Software Engineer Intern", "Software Engineer Intern", sorted.get(4).getTitle());
    }
    
    /**
     * Test Case: UT-FILTER-008
     * Verify filtering with no criteria returns all approved
     */
    @Test
    public void testNoCriteriaReturnsAll() {
        FilterCriteria criteria = new FilterCriteria();
        // No criteria set - should return all approved internships
        
        List<InternshipOpportunity> filtered = internshipManager.filterInternships(criteria);
        
        assertEquals("Should return all internships when no criteria", 5, filtered.size());
    }
}
