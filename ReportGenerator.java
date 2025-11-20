import java.util.List;

/**
 * Generates comprehensive reports for Career Center Staff in the Internship Placement Management System.
 * <p>
 * The ReportGenerator provides formatted reporting capabilities for system data:
 * <ul>
 *   <li>Detailed internship opportunity reports with all attributes</li>
 *   <li>Summary statistics by status (Pending, Approved, Rejected, Filled)</li>
 *   <li>Statistical breakdowns by level (Basic, Intermediate, Advanced)</li>
 *   <li>Formatted console output for easy readability</li>
 * </ul>
 * <p>
 * <strong>Design Philosophy:</strong>
 * <ul>
 *   <li><strong>Separation of Concerns:</strong> Focuses solely on report generation and formatting,
 *       relying on InternshipManager for filtering logic</li>
 *   <li><strong>Single Responsibility:</strong> Handles only presentation and formatting of data</li>
 *   <li><strong>Extensibility:</strong> Can be extended to support additional report types (PDF, CSV, etc.)</li>
 * </ul>
 * <p>
 * <strong>Usage Pattern:</strong>
 * The typical workflow involves:
 * <ol>
 *   <li>InternshipManager filters data based on criteria</li>
 *   <li>ReportGenerator formats and displays the filtered results</li>
 *   <li>This separation allows the same filtering logic to be used for different output formats</li>
 * </ol>
 *
 * @see InternshipOpportunity
 * @see InternshipManager
 * @see FilterCriteria
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class ReportGenerator {
    
    /**
     * Generates a report from a list of internship opportunities.
     * <p>
     * This method currently acts as a pass-through, returning the opportunities as-is.
     * The filtering is already done by InternshipManager before calling this method.
     * This design allows for future enhancements such as additional processing,
     * data transformation, or export to different formats.
     * </p>
     *
     * @param opportunities the list of InternshipOpportunity objects to include in the report
     * @param criteria      the FilterCriteria that was used to generate this list (for reference)
     * @return the same list of opportunities (can be extended for additional processing)
     */
    public List<InternshipOpportunity> generateReport(List<InternshipOpportunity> opportunities, 
                                                      FilterCriteria criteria) {
        // The filtering is already done by InternshipManager
        // This method can be used to format or process the results further
        return opportunities;
    }
    
    /**
     * Formats and displays a detailed report of internship opportunities to the console.
     * <p>
     * The report includes:
     * <ul>
     *   <li>Header with total count of opportunities</li>
     *   <li>Numbered list of all opportunities with full details</li>
     *   <li>Formatted separators for readability</li>
     *   <li>Special message if no opportunities match the criteria</li>
     * </ul>
     * <p>
     * Each opportunity displays:
     * Title, Company, Level, Preferred Major, Status, Slots, Visibility, Opening Date, Closing Date
     * </p>
     *
     * @param opportunities the list of InternshipOpportunity objects to display
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
     * Formats a single internship opportunity as a multi-line string for display.
     * <p>
     * This is a helper method used by {@link #displayReport(List)} to format individual
     * opportunities. The format includes all key attributes with labels for clarity.
     * </p>
     *
     * @param opp the InternshipOpportunity to format
     * @return a formatted multi-line string representation of the opportunity
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
     * Displays summary statistics for a list of internship opportunities.
     * <p>
     * This method provides a high-level overview of the data, showing:
     * <ul>
     *   <li><strong>Status Distribution:</strong> Count of opportunities by status
     *       (Pending, Approved, Rejected, Filled)</li>
     *   <li><strong>Level Distribution:</strong> Count of opportunities by difficulty level
     *       (Basic, Intermediate, Advanced)</li>
     * </ul>
     * This is useful for Career Center Staff to get a quick snapshot of the system state.
     *
     * @param opportunities the list of InternshipOpportunity objects to analyze
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
