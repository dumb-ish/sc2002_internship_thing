import java.util.List;

/**
 * Generates comprehensive reports for Career Center Staff.
 * Provides formatted output of internship opportunities based on filter criteria.
 */
public class ReportGenerator {
    
    /**
     * Generate a report of internship opportunities based on filter criteria
     */
    public List<InternshipOpportunity> generateReport(List<InternshipOpportunity> opportunities, 
                                                      FilterCriteria criteria) {
        // The filtering is already done by InternshipManager
        // This method can be used to format or process the results further
        return opportunities;
    }
    
    /**
     * Format and display a report of opportunities
     */
    public void displayReport(List<InternshipOpportunity> opportunities) {
        System.out.println("\n=== Internship Opportunities Report ===");
        System.out.println("Total Opportunities: " + opportunities.size());
        System.out.println("========================================\n");
        
        if (opportunities.isEmpty()) {
            System.out.println("No opportunities found matching the criteria.");
            return;
        }
        
        for (int i = 0; i < opportunities.size(); i++) {
            InternshipOpportunity opp = opportunities.get(i);
            System.out.println((i + 1) + ". " + formatOpportunity(opp));
            System.out.println("   " + "-".repeat(80));
        }
    }
    
    /**
     * Format a single opportunity for display
     */
    private String formatOpportunity(InternshipOpportunity opp) {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(opp.getTitle()).append("\n");
        sb.append("   Company: ").append(opp.getCompanyName()).append("\n");
        sb.append("   Level: ").append(opp.getLevel()).append("\n");
        sb.append("   Preferred Major: ").append(opp.getPreferredMajor()).append("\n");
        sb.append("   Status: ").append(opp.getStatus()).append("\n");
        sb.append("   Slots: ").append(opp.getNumSlots()).append("\n");
        sb.append("   Visible: ").append(opp.getVisibility()).append("\n");
        sb.append("   Opening Date: ").append(opp.getOpeningDate()).append("\n");
        sb.append("   Closing Date: ").append(opp.getClosingDate());
        return sb.toString();
    }
    
    /**
     * Generate a summary statistics report
     */
    public void displaySummaryStats(List<InternshipOpportunity> opportunities) {
        System.out.println("\n=== Summary Statistics ===");
        System.out.println("Total Opportunities: " + opportunities.size());
        
        long pending = opportunities.stream().filter(o -> "Pending".equals(o.getStatus())).count();
        long approved = opportunities.stream().filter(o -> "Approved".equals(o.getStatus())).count();
        long rejected = opportunities.stream().filter(o -> "Rejected".equals(o.getStatus())).count();
        long filled = opportunities.stream().filter(o -> "Filled".equals(o.getStatus())).count();
        
        System.out.println("Pending: " + pending);
        System.out.println("Approved: " + approved);
        System.out.println("Rejected: " + rejected);
        System.out.println("Filled: " + filled);
        
        long basic = opportunities.stream().filter(o -> "Basic".equals(o.getLevel())).count();
        long intermediate = opportunities.stream().filter(o -> "Intermediate".equals(o.getLevel())).count();
        long advanced = opportunities.stream().filter(o -> "Advanced".equals(o.getLevel())).count();
        
        System.out.println("\nBy Level:");
        System.out.println("Basic: " + basic);
        System.out.println("Intermediate: " + intermediate);
        System.out.println("Advanced: " + advanced);
    }
}
