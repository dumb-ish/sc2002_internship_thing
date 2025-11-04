import java.util.ArrayList;
import java.util.List;

/**
 * Manages all internship opportunities in the system.
 * Handles adding, approving, rejecting, and filtering of internships.
 */
public class InternshipManager {
    private List<InternshipOpportunity> internshipList;
    private InternshipFilter filterHelper;
    
    /**
     * Constructor
     */
    public InternshipManager() {
        this.internshipList = new ArrayList<>();
        this.filterHelper = new InternshipFilter();
    }
    
    /**
     * Add a new internship opportunity
     */
    public void addInternship(InternshipOpportunity opportunity) {
        internshipList.add(opportunity);
    }
    
    /**
     * Approve an internship opportunity
     */
    public void approveInternship(InternshipOpportunity opportunity) {
        opportunity.updateStatus("Approved");
    }
    
    /**
     * Reject an internship opportunity
     */
    public void rejectInternship(InternshipOpportunity opportunity) {
        opportunity.updateStatus("Rejected");
    }
    
    /**
     * Filter internships based on criteria
     */
    public List<InternshipOpportunity> filterInternships(FilterCriteria criteria) {
        List<InternshipOpportunity> filtered = filterHelper.applyFilter(internshipList, criteria);
        return filterHelper.sortAlphabetically(filtered);
    }
    
    /**
     * Get all internship opportunities
     */
    public List<InternshipOpportunity> getAllInternships() {
        return new ArrayList<>(internshipList);
    }
    
    /**
     * Get internships visible to a specific student
     */
    public List<InternshipOpportunity> getVisibleInternshipsForStudent(Student student, FilterCriteria criteria) {
        // First apply student eligibility filter
        List<InternshipOpportunity> eligible = filterHelper.filterForStudent(internshipList, student);
        
        // Then apply user's custom filter criteria
        List<InternshipOpportunity> filtered = filterHelper.applyFilter(eligible, criteria);
        
        // Finally sort alphabetically
        return filterHelper.sortAlphabetically(filtered);
    }
    
    /**
     * Get internships created by a specific company representative
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
     * Get pending internships (waiting for staff approval)
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
     * Check if a representative has reached their internship creation limit
     */
    public boolean hasReachedCreationLimit(String repID) {
        long count = internshipList.stream()
            .filter(opp -> opp.getCompanyRepID().equals(repID))
            .count();
        return count >= 5;
    }
    
    /**
     * Update internship status to Filled if all slots are filled
     */
    public void updateFilledStatus(InternshipOpportunity opportunity, long acceptedCount) {
        if (acceptedCount >= opportunity.getNumSlots()) {
            opportunity.updateStatus("Filled");
        }
    }
    
    /**
     * Toggle visibility of an internship
     */
    public void toggleVisibility(InternshipOpportunity opportunity) {
        opportunity.setVisibility(!opportunity.getVisibility());
    }
    
    /**
     * Find an internship by its properties (for lookup)
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
     * Remove an internship from the list
     */
    public void removeInternship(InternshipOpportunity opportunity) {
        internshipList.remove(opportunity);
    }
}
