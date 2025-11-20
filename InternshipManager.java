import java.util.ArrayList;
import java.util.List;

/**
 * Manages all internship opportunities in the Internship Placement Management System.
 * <p>
 * The InternshipManager is responsible for the complete lifecycle of internship opportunities:
 * <ul>
 *   <li>CRUD operations (Create, Read, Update, Delete) for internship opportunities</li>
 *   <li>Status management (Pending, Approved, Rejected, Filled)</li>
 *   <li>Visibility control for internship postings</li>
 *   <li>Student eligibility filtering based on year of study and major</li>
 *   <li>Criteria-based filtering (company, level, preferred major)</li>
 *   <li>Alphabetical sorting of filtered results</li>
 *   <li>Representative-specific internship limits (maximum 5 per representative)</li>
 *   <li>Automatic status updates when slots are filled or freed</li>
 * </ul>
 * <p>
 * <strong>Design Patterns:</strong>
 * <ul>
 *   <li><strong>Dependency Inversion Principle (DIP):</strong> Depends on three separate interfaces
 *       (ICriteriaFilter, IStudentEligibilityFilter, IInternshipSorter) rather than concrete implementations,
 *       allowing flexible composition of filtering and sorting strategies</li>
 *   <li><strong>Strategy Pattern:</strong> Filter and sorter implementations can be swapped at runtime
 *       without modifying InternshipManager</li>
 *   <li><strong>Single Responsibility:</strong> Focuses solely on internship opportunity management,
 *       delegating filtering logic to injected dependencies</li>
 * </ul>
 * <p>
 * <strong>SOLID Principles Demonstrated:</strong>
 * <ul>
 *   <li><strong>Single Responsibility:</strong> Manages only internship opportunities and their lifecycle</li>
 *   <li><strong>Open/Closed:</strong> Open for extension (new filters) without modification</li>
 *   <li><strong>Dependency Inversion:</strong> High-level module depends on abstractions (interfaces)</li>
 *   <li><strong>Interface Segregation:</strong> Uses three focused interfaces instead of one large interface</li>
 * </ul>
 *
 * @see InternshipOpportunity
 * @see ICriteriaFilter
 * @see IStudentEligibilityFilter
 * @see IInternshipSorter
 * @see StudentEligibilityFilter
 * @see FilterCriteria
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class InternshipManager {
    private List<InternshipOpportunity> internshipList;
    private ICriteriaFilter criteriaFilter;
    private IStudentEligibilityFilter studentEligibilityFilter;
    private IInternshipSorter sorter;
    
    /**
     * Constructs a new InternshipManager with dependency injection for filters and sorter.
     * <p>
     * This constructor demonstrates the Dependency Inversion Principle by accepting
     * three separate interface implementations. This design provides:
     * <ul>
     *   <li><strong>Flexibility:</strong> Different implementations can be mixed and matched</li>
 *   <li><strong>Testability:</strong> Mock implementations can be injected for unit testing</li>
 *   <li><strong>Clear Contract:</strong> Explicitly requires three distinct capabilities</li>
     *   <li><strong>Reusability:</strong> The same object can implement multiple interfaces</li>
     * </ul>
     * <p>
     * In the current implementation, SystemManager injects StudentEligibilityFilter
     * for all three interfaces, demonstrating that a single class can satisfy multiple
     * interface contracts.
     * </p>
     *
     * @param criteriaFilter           the filter implementation for criteria-based filtering
     *                                  (company name, level, preferred major)
     * @param studentEligibilityFilter the filter implementation for student eligibility
     *                                  (year of study restrictions, major matching)
     * @param sorter                   the sorter implementation for alphabetical sorting by title
     */
    public InternshipManager(ICriteriaFilter criteriaFilter,
                            IStudentEligibilityFilter studentEligibilityFilter,
                            IInternshipSorter sorter) {
        this.internshipList = new ArrayList<>();
        this.criteriaFilter = criteriaFilter;
        this.studentEligibilityFilter = studentEligibilityFilter;
        this.sorter = sorter;
    }
    
    /**
     * Adds a new internship opportunity to the system.
     * <p>
     * New internships are created with "Pending" status by default and require
     * approval from Career Center Staff before becoming visible to students.
     * </p>
     *
     * @param opportunity the InternshipOpportunity object to add
     */
    public void addInternship(InternshipOpportunity opportunity) {
        internshipList.add(opportunity);
    }
    
    /**
     * Approves an internship opportunity, making it visible to eligible students.
     * <p>
     * This method is called by Career Center Staff after reviewing a pending internship.
     * Approved internships become searchable and viewable by students who meet eligibility criteria.
     * </p>
     *
     * @param opportunity the internship opportunity to approve
     */
    public void approveInternship(InternshipOpportunity opportunity) {
        opportunity.updateStatus("Approved");
    }
    
    /**
     * Rejects an internship opportunity, preventing it from being visible to students.
     * <p>
     * Rejected internships remain in the system for record-keeping but are not displayed
     * to students or available for applications.
     * </p>
     *
     * @param opportunity the internship opportunity to reject
     */
    public void rejectInternship(InternshipOpportunity opportunity) {
        opportunity.updateStatus("Rejected");
    }
    
    /**
     * Filters internship opportunities based on specified criteria and sorts the results alphabetically.
     * <p>
     * This method demonstrates the Strategy Pattern by delegating to injected filter and sorter implementations.
     * The filtering is performed by the criteriaFilter, and results are sorted by the sorter.
     * </p>
     *
     * @param criteria the FilterCriteria object specifying company name, level, and/or preferred major
     * @return a filtered and sorted list of InternshipOpportunity objects matching the criteria
     */
    public List<InternshipOpportunity> filterInternships(FilterCriteria criteria) {
        List<InternshipOpportunity> filtered = criteriaFilter.applyFilter(internshipList, criteria);
        return sorter.sortAlphabetically(filtered);
    }
    
    /**
     * Retrieves all internship opportunities in the system.
     * <p>
     * Returns a defensive copy to prevent external modification of the internal list.
     * This method returns all internships regardless of status or visibility.
     * </p>
     *
     * @return a new ArrayList containing all internship opportunities
     */
    public List<InternshipOpportunity> getAllInternships() {
        return new ArrayList<>(internshipList);
    }
    
    /**
     * Retrieves internship opportunities visible to a specific student based on eligibility and filter criteria.
     * <p>
     * This method applies a three-stage filtering pipeline:
     * <ol>
     *   <li><strong>Eligibility Filtering:</strong> Applies student-specific rules
     *       <ul>
     *         <li>Year 1-2 students: only Basic level internships</li>
     *         <li>Major matching: only internships for student's major</li>
     *         <li>Visibility: only visible and approved internships</li>
     *       </ul>
     *   </li>
     *   <li><strong>Criteria Filtering:</strong> Applies user-specified filters (company, level, major)</li>
     *   <li><strong>Alphabetical Sorting:</strong> Sorts results by title for easy browsing</li>
     * </ol>
     *
     * @param student  the Student object for which to filter internships
     * @param criteria the FilterCriteria object with optional company, level, and major filters
     * @return a filtered, eligibility-checked, and sorted list of InternshipOpportunity objects
     */
    public List<InternshipOpportunity> getVisibleInternshipsForStudent(Student student, FilterCriteria criteria) {
        // First apply student eligibility filter
        List<InternshipOpportunity> eligible = studentEligibilityFilter.filterForStudent(internshipList, student);
        
        // Then apply user's custom filter criteria
        List<InternshipOpportunity> filtered = criteriaFilter.applyFilter(eligible, criteria);
        
        // Finally sort alphabetically
        return sorter.sortAlphabetically(filtered);
    }
    
    /**
     * Retrieves all internship opportunities created by a specific company representative.
     * <p>
     * This method is used by representatives to view and manage their own internship postings.
     * </p>
     *
     * @param repID the unique identifier of the company representative
     * @return a list of InternshipOpportunity objects created by the specified representative
     */
    public List<InternshipOpportunity> getInternshipsByRepresentative(String repID) {
        List<InternshipOpportunity> result = new ArrayList<>();
        for (InternshipOpportunity opp : internshipList) {
            if (opp.getCompanyRepID().equals(repID)) {
                result.add(opp);
            }
        }
        return result;
    }
    
    /**
     * Retrieves all internship opportunities with "Pending" status awaiting staff approval.
     * <p>
     * This method is used by Career Center Staff to review and approve/reject new internship postings.
     * </p>
     *
     * @return a list of InternshipOpportunity objects with Pending status
     */
    public List<InternshipOpportunity> getPendingInternships() {
        List<InternshipOpportunity> result = new ArrayList<>();
        for (InternshipOpportunity opp : internshipList) {
            if ("Pending".equals(opp.getStatus())) {
                result.add(opp);
            }
        }
        return result;
    }
    
    /**
     * Checks if a company representative has reached their internship creation limit.
     * <p>
     * Company representatives are limited to creating a maximum of 5 internship opportunities
     * to prevent system abuse and ensure quality postings.
     * </p>
     *
     * @param repID the unique identifier of the company representative
     * @return true if the representative has created 5 or more internships, false otherwise
     */
    public boolean hasReachedCreationLimit(String repID) {
        long count = internshipList.stream()
            .filter(opp -> opp.getCompanyRepID().equals(repID))
            .count();
        return count >= 5;
    }
    
    /**
     * Updates an internship status to "Filled" if all available slots are filled.
     * <p>
     * This method is called by ApplicationManager after a student accepts an internship offer.
     * When an internship is marked as Filled, it is no longer visible to students for new applications.
     * </p>
     *
     * @param opportunity   the internship opportunity to check
     * @param acceptedCount the number of students who have accepted offers for this internship
     */
    public void updateFilledStatus(InternshipOpportunity opportunity, long acceptedCount) {
        if (acceptedCount >= opportunity.getNumSlots()) {
            opportunity.updateStatus("Filled");
        }
    }
    
    /**
     * Reverts an internship status from "Filled" to "Approved" when slots become available.
     * <p>
     * This method is called when a student withdraws from an accepted internship,
     * making the internship visible and available for applications again.
     * </p>
     *
     * @param opportunity the internship opportunity to revert
     */
    public void revertFilledStatus(InternshipOpportunity opportunity) {
        if ("Filled".equals(opportunity.getStatus())) {
            opportunity.updateStatus("Approved");
        }
    }
    
    /**
     * Toggles the visibility of an internship opportunity.
     * <p>
     * Company representatives can use this to temporarily hide or show their internship postings
     * without deleting them. Hidden internships are not visible to students.
     * </p>
     *
     * @param opportunity the internship opportunity to toggle
     */
    public void toggleVisibility(InternshipOpportunity opportunity) {
        opportunity.setVisibility(!opportunity.getVisibility());
    }
    
    /**
     * Finds an internship opportunity by its title and company name.
     * <p>
     * This method performs an exact match search (case-sensitive) for both title and company name.
     * Used for looking up specific internships during application or management operations.
     * </p>
     *
     * @param title       the exact title of the internship
     * @param companyName the exact company name
     * @return the matching InternshipOpportunity, or null if not found
     */
    public InternshipOpportunity findInternship(String title, String companyName) {
        for (InternshipOpportunity opp : internshipList) {
            if (opp.getTitle().equals(title) && opp.getCompanyName().equals(companyName)) {
                return opp;
            }
        }
        return null;
    }
    
    /**
     * Removes an internship opportunity from the system.
     * <p>
     * This method permanently deletes an internship. Should be used with caution,
     * typically only when no applications exist for the internship.
     * </p>
     *
     * @param opportunity the internship opportunity to remove
     */
    public void removeInternship(InternshipOpportunity opportunity) {
        internshipList.remove(opportunity);
    }
}
