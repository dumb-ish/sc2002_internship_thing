package test.unit;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

// Import domain classes
import InternshipOpportunity;
import InternshipFilter;
import FilterCriteria;

/**
 * Unit tests for Filter and Sort functionality
 * Tests filtering by various criteria and sorting internships
 * 
 * Test IDs: UT-FILTER-001 through UT-FILTER-004, UT-SORT-001 through UT-SORT-002
 * 
 * @see InternshipFilter
 * @see FilterCriteria
 * @see CriteriaBasedFilter
 */
public class FilterTest {
    
    private List<InternshipOpportunity> testInternships;
    private InternshipFilter filter;
    
    @Before
    public void setUp() {
        // Create diverse test internships
        testInternships = new ArrayList<>();
        
        // Internship 1: Singapore, Tech Corp, 2000
        testInternships.add(new InternshipOpportunity(
            "INT001",
            "Software Engineer Intern",
            "Backend development",
            "Java, Spring",
            "Singapore",
            2000.0,
            3,
            "Tech Corp"
        ));
        
        // Internship 2: Singapore, Innovate, 1800
        testInternships.add(new InternshipOpportunity(
            "INT002",
            "Data Analyst Intern",
            "Data analysis",
            "Python, SQL",
            "Singapore",
            1800.0,
            2,
            "Innovate Pte Ltd"
        ));
        
        // Internship 3: Remote, StartUp, 1500
        testInternships.add(new InternshipOpportunity(
            "INT003",
            "Web Developer Intern",
            "Frontend development",
            "React, JavaScript",
            "Remote",
            1500.0,
            1,
            "StartUp Inc"
        ));
        
        // Internship 4: Singapore, Tech Corp, 2200
        testInternships.add(new InternshipOpportunity(
            "INT004",
            "Mobile App Developer",
            "iOS and Android",
            "Swift, Kotlin",
            "Singapore",
            2200.0,
            2,
            "Tech Corp"
        ));
        
        // Internship 5: Kuala Lumpur, Global Tech, 1000
        testInternships.add(new InternshipOpportunity(
            "INT005",
            "QA Tester Intern",
            "Testing applications",
            "Selenium, JUnit",
            "Kuala Lumpur",
            1000.0,
            1,
            "Global Tech"
        ));
        
        // Mark all as approved
        for (InternshipOpportunity internship : testInternships) {
            internship.updateStatus("Approved");
        }
        
        filter = new InternshipFilter();
    }
    
    @After
    public void tearDown() {
        testInternships = null;
        filter = null;
    }
    
    /**
     * UT-FILTER-001: Filter by Location
     * 
     * Tests filtering internships by location.
     * 
     * Preconditions: Multiple internships with different locations
     * Expected Result: Only Singapore internships returned
     */
    @Test
    public void testFilterByLocation() {
        // Arrange
        FilterCriteria criteria = new FilterCriteria();
        criteria.setLocation("Singapore");
        
        // Act
        List<InternshipOpportunity> filtered = filter.filterByCriteria(testInternships, criteria);
        
        // Assert
        assertEquals("Should return 3 Singapore internships", 3, filtered.size());
        for (InternshipOpportunity internship : filtered) {
            assertEquals("All should be in Singapore", "Singapore", internship.getLocation());
        }
    }
    
    /**
     * UT-FILTER-002: Filter by Salary Range
     * 
     * Tests filtering by minimum salary.
     * 
     * Preconditions: Internships with various salaries
     * Expected Result: Only internships with salary >= 1500 returned
     */
    @Test
    public void testFilterBySalary() {
        // Arrange
        FilterCriteria criteria = new FilterCriteria();
        criteria.setMinSalary(1500.0);
        
        // Act
        List<InternshipOpportunity> filtered = filter.filterByCriteria(testInternships, criteria);
        
        // Assert
        assertEquals("Should return 4 internships with salary >= 1500", 4, filtered.size());
        for (InternshipOpportunity internship : filtered) {
            assertTrue("Salary should be >= 1500", internship.getSalary() >= 1500.0);
        }
    }
    
    /**
     * UT-FILTER-003: Filter by Company
     * 
     * Tests filtering by company name.
     * 
     * Preconditions: Multiple companies with internships
     * Expected Result: Only Tech Corp internships returned
     */
    @Test
    public void testFilterByCompany() {
        // Arrange
        FilterCriteria criteria = new FilterCriteria();
        criteria.setCompanyName("Tech Corp");
        
        // Act
        List<InternshipOpportunity> filtered = filter.filterByCriteria(testInternships, criteria);
        
        // Assert
        assertEquals("Should return 2 Tech Corp internships", 2, filtered.size());
        for (InternshipOpportunity internship : filtered) {
            assertEquals("All should be from Tech Corp", "Tech Corp", internship.getCompanyName());
        }
    }
    
    /**
     * UT-FILTER-004: Multiple Filter Criteria
     * 
     * Tests applying multiple filters simultaneously.
     * 
     * Preconditions: Diverse internship data
     * Expected Result: Only internships matching ALL criteria returned
     */
    @Test
    public void testMultipleFilterCriteria() {
        // Arrange
        FilterCriteria criteria = new FilterCriteria();
        criteria.setLocation("Singapore");
        criteria.setMinSalary(1500.0);
        criteria.setCompanyName("Tech Corp");
        
        // Act
        List<InternshipOpportunity> filtered = filter.filterByCriteria(testInternships, criteria);
        
        // Assert
        assertEquals("Should return 2 internships matching all criteria", 2, filtered.size());
        for (InternshipOpportunity internship : filtered) {
            assertEquals("Location should be Singapore", "Singapore", internship.getLocation());
            assertTrue("Salary should be >= 1500", internship.getSalary() >= 1500.0);
            assertEquals("Company should be Tech Corp", "Tech Corp", internship.getCompanyName());
        }
    }
    
    /**
     * UT-SORT-001: Sort by Salary Ascending
     * 
     * Tests sorting internships by salary (low to high).
     * 
     * Preconditions: Internships with different salaries
     * Expected Result: Internships sorted in ascending salary order
     */
    @Test
    public void testSortBySalaryAscending() {
        // Arrange
        List<InternshipOpportunity> internships = new ArrayList<>(testInternships);
        
        // Act
        filter.sortBySalary(internships, true); // true = ascending
        
        // Assert
        assertEquals("First should have lowest salary", 1000.0, internships.get(0).getSalary(), 0.01);
        assertEquals("Second should be 1500", 1500.0, internships.get(1).getSalary(), 0.01);
        assertEquals("Third should be 1800", 1800.0, internships.get(2).getSalary(), 0.01);
        assertEquals("Fourth should be 2000", 2000.0, internships.get(3).getSalary(), 0.01);
        assertEquals("Last should have highest salary", 2200.0, internships.get(4).getSalary(), 0.01);
        
        // Verify order is correct
        for (int i = 0; i < internships.size() - 1; i++) {
            assertTrue("Each salary should be <= next",
                internships.get(i).getSalary() <= internships.get(i + 1).getSalary());
        }
    }
    
    /**
     * UT-SORT-002: Sort by Salary Descending
     * 
     * Tests sorting internships by salary (high to low).
     * 
     * Preconditions: Internships with different salaries
     * Expected Result: Internships sorted in descending salary order
     */
    @Test
    public void testSortBySalaryDescending() {
        // Arrange
        List<InternshipOpportunity> internships = new ArrayList<>(testInternships);
        
        // Act
        filter.sortBySalary(internships, false); // false = descending
        
        // Assert
        assertEquals("First should have highest salary", 2200.0, internships.get(0).getSalary(), 0.01);
        assertEquals("Second should be 2000", 2000.0, internships.get(1).getSalary(), 0.01);
        assertEquals("Third should be 1800", 1800.0, internships.get(2).getSalary(), 0.01);
        assertEquals("Fourth should be 1500", 1500.0, internships.get(3).getSalary(), 0.01);
        assertEquals("Last should have lowest salary", 1000.0, internships.get(4).getSalary(), 0.01);
        
        // Verify order is correct
        for (int i = 0; i < internships.size() - 1; i++) {
            assertTrue("Each salary should be >= next",
                internships.get(i).getSalary() >= internships.get(i + 1).getSalary());
        }
    }
    
    /**
     * Additional Test: Filter with No Matches
     * 
     * Tests handling when no internships match criteria.
     */
    @Test
    public void testFilterWithNoMatches() {
        // Arrange
        FilterCriteria criteria = new FilterCriteria();
        criteria.setLocation("Antarctica"); // No internships in Antarctica
        
        // Act
        List<InternshipOpportunity> filtered = filter.filterByCriteria(testInternships, criteria);
        
        // Assert
        assertNotNull("Result should not be null", filtered);
        assertEquals("Should return empty list", 0, filtered.size());
    }
    
    /**
     * Additional Test: Filter with Empty Criteria
     * 
     * Tests that empty criteria returns all internships.
     */
    @Test
    public void testFilterWithEmptyCriteria() {
        // Arrange
        FilterCriteria criteria = new FilterCriteria(); // No filters set
        
        // Act
        List<InternshipOpportunity> filtered = filter.filterByCriteria(testInternships, criteria);
        
        // Assert
        assertEquals("Should return all internships", testInternships.size(), filtered.size());
    }
}
