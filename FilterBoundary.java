import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Shared filter UI component for managing user filter criteria across all user types.
 * <p>
 * This boundary class provides a reusable filter configuration interface that allows
 * users to customize which internship opportunities they see. It implements role-based
 * filtering with different capabilities for different user types.
 * 
 * <p><strong>Responsibilities:</strong>
 * <ul>
 *   <li>Present interactive filter configuration menus</li>
 *   <li>Handle filter criteria input and validation</li>
 *   <li>Enforce role-specific filter restrictions</li>
 *   <li>Provide consistent filter UX across user types</li>
 *   <li>Support filter reset functionality</li>
 * </ul>
 * 
 * <p><strong>Filter Criteria Supported:</strong>
 * <ul>
 *   <li><strong>Level:</strong> Basic, Intermediate, Advanced</li>
 *   <li><strong>Major:</strong> Preferred major for internship</li>
 *   <li><strong>Status:</strong> Pending, Approved, Rejected, Filled</li>
 *   <li><strong>Closing Date:</strong> Filter by application deadline</li>
 * </ul>
 * 
 * <p><strong>User-Specific Filter Behavior:</strong>
 * <ul>
 *   <li><strong>Students:</strong> Restricted filters based on year of study and major
 *       <ul>
 *         <li>Year 1-2: Level locked to \"Basic\"</li>
 *         <li>All years: Major locked to student's major</li>
 *         <li>All years: Status locked to \"Approved\"</li>
 *         <li>All years: Can modify closing date filter</li>
 *       </ul>
 *   </li>
 *   <li><strong>Company Representatives:</strong> Full filter control for viewing their internships</li>
 *   <li><strong>Staff:</strong> Full filter control for generating reports</li>
 * </ul>
 * 
 * <p><strong>Menu Types:</strong>
 * <ol>
 *   <li>{@link #displayFilterMenu} - Generic filter menu for company reps and staff</li>
 *   <li>{@link #displayStudentFilterMenu} - Student-specific menu with locked fields</li>
 *   <li>{@link #promptForCriteria} - Quick filter setup for reports</li>
 * </ol>
 * 
 * <p><strong>User Experience Features:</strong>
 * <ul>
 *   <li>Shows current filter values</li>
 *   <li>Clear indication of locked fields for students</li>
 *   <li>Ability to clear individual filters or reset all</li>
 *   <li>Input validation with error messages</li>
 *   <li>Consistent menu navigation</li>
 * </ul>
 * 
 * <p><strong>MVC Role:</strong> View layer component - handles filter UI presentation
 * and input collection. Works with {@link FilterCriteria} model objects.
 * 
 * @see FilterCriteria
 * @see IStudentEligibilityFilter
 * @see InternshipFilter
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public class FilterBoundary {
    /** Scanner for reading user input */
    private Scanner scanner;
    
    /**
     * Constructs a new filter boundary.
     * <p>
     * Initializes the Scanner for console input handling. The same Scanner instance
     * is used throughout the filter configuration process.
     */
    public FilterBoundary() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Displays an interactive filter menu allowing users to configure filter criteria.
     * <p>
     * This is the generic filter menu used by company representatives and staff who have
     * full control over all filter options. The menu displays current filter values and
     * allows modification of individual criteria or resetting all filters.
     * 
     * <p><strong>Menu Options:</strong>
     * <ol>
     *   <li>Level - Choose Basic, Intermediate, or Advanced</li>
     *   <li>Major - Enter preferred major or clear filter</li>
     *   <li>Status - Choose Pending, Approved, Rejected, Filled, or clear</li>
     *   <li>Closing Date - Enter date (YYYY-MM-DD format) or clear</li>
     *   <li>Reset All Filters - Clear all filter criteria</li>
     *   <li>Back to Main Menu - Exit filter configuration</li>
     * </ol>
     * 
     * <p><strong>Display Format:</strong> Shows "None" for unset filters, making it
     * clear which filters are active.
     * 
     * <p><strong>Persistence:</strong> Changes are made directly to the provided
     * {@link FilterCriteria} object, which is typically stored in the User model.
     * 
     * <p><strong>Navigation:</strong> Menu loops until user chooses to return to main menu.
     * 
     * @param boundary the user boundary that invoked this filter menu (for context)
     * @param criteria the filter criteria object to modify
     * 
     * @see FilterCriteria
     */
    public void displayFilterMenu(IUserBoundary boundary, FilterCriteria criteria) {
        while (true) {
            System.out.println("\n=== Filter Settings ===");
            System.out.println("Current Filters:");
            System.out.println("1. Level: " + (criteria.getLevel() != null ? criteria.getLevel() : "None"));
            System.out.println("2. Major: " + (criteria.getMajor() != null ? criteria.getMajor() : "None"));
            System.out.println("3. Status: " + (criteria.getStatus() != null ? criteria.getStatus() : "None"));
            System.out.println("4. Closing Date: " + (criteria.getClosingDate() != null ? criteria.getClosingDate() : "None"));
            System.out.println("5. Reset All Filters");
            System.out.println("6. Back to Main Menu");
            System.out.print("Select filter to modify: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        setLevelFilter(criteria);
                        break;
                    case 2:
                        setMajorFilter(criteria);
                        break;
                    case 3:
                        setStatusFilter(criteria);
                        break;
                    case 4:
                        setClosingDateFilter(criteria);
                        break;
                    case 5:
                        criteria.reset();
                        System.out.println("All filters reset.");
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
    
    /**
     * Displays student-specific filter menu with conditional field editability.
     * <p>
     * This specialized menu enforces business rules for student filtering:
     * <ul>
     *   <li>Level filter is locked to "Basic" for Year 1-2 students</li>
     *   <li>Year 3+ students can choose any level</li>
     *   <li>Major filter is always locked to the student's major</li>
     *   <li>Status filter is always locked to "Approved"</li>
     *   <li>Closing date filter is always editable</li>
     * </ul>
     * 
     * <p><strong>Business Rationale:</strong>
     * <ul>
     *   <li>Year 1-2 students should focus on basic internships appropriate to their level</li>
     *   <li>Students should only see internships matching their academic program</li>
     *   <li>Only approved internships should be visible to students (quality control)</li>
     * </ul>
     * 
     * <p><strong>User Experience:</strong>
     * <ul>
     *   <li>Locked fields clearly labeled with "(Locked)" and explanation</li>
     *   <li>Attempting to edit locked field displays informative message</li>
     *   <li>Shows current values even for locked fields</li>
     *   <li>Provides clear feedback on restrictions</li>
     * </ul>
     * 
     * <p><strong>Menu Options:</strong>
     * <ol>
     *   <li>Level - Editable for Year 3+, locked for Year 1-2</li>
     *   <li>Major - Always locked (displays student's major)</li>
     *   <li>Status - Always locked (displays "Approved")</li>
     *   <li>Closing Date - Always editable</li>
     *   <li>Reset Filters - Clear editable filters (locked filters remain)</li>
     *   <li>Back to Main Menu - Exit filter configuration</li>
     * </ol>
     * 
     * @param boundary the student boundary that invoked this filter menu
     * @param criteria the filter criteria object to modify
     * @param student the student user for checking year of study and major
     * 
     * @see Student#getYearOfStudy()
     * @see Student#getMajor()
     * @see IStudentEligibilityFilter
     */
    public void displayStudentFilterMenu(IUserBoundary boundary, FilterCriteria criteria, Student student) {
        while (true) {
            System.out.println("\n=== Filter Settings ===");
            System.out.println("Current Filters:");
            
            // Determine what can be edited based on student year
            boolean canEditLevel = student.getYearOfStudy() >= 3;
            
            // Show level filter (locked for year 1-2)
            if (canEditLevel) {
                System.out.println("1. Level: " + (criteria.getLevel() != null ? criteria.getLevel() : "None"));
            } else {
                System.out.println("1. Level: Basic (Locked for Year 1-2 students)");
            }
            
            // Major is always locked to student's major
            System.out.println("2. Major: " + student.getMajor() + " (Locked - matches your major)");
            
            // Status is always locked to Approved
            System.out.println("3. Status: Approved (Locked - only approved internships shown)");
            
            // Closing date is always editable
            System.out.println("4. Closing Date: " + (criteria.getClosingDate() != null ? criteria.getClosingDate() : "None"));
            
            System.out.println("5. Reset Filters");
            System.out.println("6. Back to Main Menu");
            System.out.print("Select option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        if (canEditLevel) {
                            setLevelFilter(criteria);
                        } else {
                            System.out.println("Level filter is locked for Year 1-2 students (Basic only).");
                        }
                        break;
                    case 2:
                        System.out.println("Major filter is locked to your major: " + student.getMajor());
                        break;
                    case 3:
                        System.out.println("Status filter is locked to: Approved");
                        break;
                    case 4:
                        setClosingDateFilter(criteria);
                        break;
                    case 5:
                        criteria.reset();
                        System.out.println("Filters reset.");
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
    
    /**
     * Prompts user to set or clear the level filter.
     * <p>
     * Presents a menu of level options and updates the criteria based on user choice.
     * 
     * <p><strong>Options:</strong>
     * <ol>
     *   <li>Basic - Entry-level internships</li>
     *   <li>Intermediate - Mid-level internships</li>
     *   <li>Advanced - Advanced internships</li>
     *   <li>Clear Filter - Remove level restriction</li>
     * </ol>
     * 
     * <p><strong>Validation:</strong> Only accepts choices 1-4. Invalid input displays
     * error message.
     * 
     * @param criteria the filter criteria object to update
     */
    private void setLevelFilter(FilterCriteria criteria) {
        System.out.println("\nSelect Level:");
        System.out.println("1. Basic");
        System.out.println("2. Intermediate");
        System.out.println("3. Advanced");
        System.out.println("4. Clear Filter");
        System.out.print("Choice: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    criteria.setLevel("Basic");
                    System.out.println("Level filter set to: Basic");
                    break;
                case 2:
                    criteria.setLevel("Intermediate");
                    System.out.println("Level filter set to: Intermediate");
                    break;
                case 3:
                    criteria.setLevel("Advanced");
                    System.out.println("Level filter set to: Advanced");
                    break;
                case 4:
                    criteria.setLevel(null);
                    System.out.println("Level filter cleared.");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }
    
    /**
     * Prompts user to set or clear the major filter.
     * <p>
     * Allows free-text entry of preferred major or clearing the filter.
     * 
     * <p><strong>Input:</strong>
     * <ul>
     *   <li>Enter major name (e.g., "Computer Science", "Engineering")</li>
     *   <li>Enter "clear" to remove major filter</li>
     *   <li>Empty input is ignored</li>
     * </ul>
     * 
     * @param criteria the filter criteria object to update
     */
    private void setMajorFilter(FilterCriteria criteria) {
        System.out.print("\nEnter preferred major (or 'clear' to remove filter): ");
        String major = scanner.nextLine().trim();
        
        if (major.equalsIgnoreCase("clear")) {
            criteria.setMajor(null);
            System.out.println("Major filter cleared.");
        } else if (!major.isEmpty()) {
            criteria.setMajor(major);
            System.out.println("Major filter set to: " + major);
        }
    }
    
    /**
     * Prompts user to set or clear the status filter.
     * <p>
     * Presents a menu of status options and updates the criteria based on user choice.
     * 
     * <p><strong>Status Options:</strong>
     * <ol>
     *   <li>Pending - Awaiting staff approval</li>
     *   <li>Approved - Approved and visible to students</li>
     *   <li>Rejected - Rejected by staff</li>
     *   <li>Filled - All slots taken</li>
     *   <li>Clear Filter - Remove status restriction</li>
     * </ol>
     * 
     * <p><strong>Validation:</strong> Only accepts choices 1-5. Invalid input displays
     * error message.
     * 
     * @param criteria the filter criteria object to update
     */
    private void setStatusFilter(FilterCriteria criteria) {
        System.out.println("\nSelect Status:");
        System.out.println("1. Pending");
        System.out.println("2. Approved");
        System.out.println("3. Rejected");
        System.out.println("4. Filled");
        System.out.println("5. Clear Filter");
        System.out.print("Choice: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    criteria.setStatus("Pending");
                    System.out.println("Status filter set to: Pending");
                    break;
                case 2:
                    criteria.setStatus("Approved");
                    System.out.println("Status filter set to: Approved");
                    break;
                case 3:
                    criteria.setStatus("Rejected");
                    System.out.println("Status filter set to: Rejected");
                    break;
                case 4:
                    criteria.setStatus("Filled");
                    System.out.println("Status filter set to: Filled");
                    break;
                case 5:
                    criteria.setStatus(null);
                    System.out.println("Status filter cleared.");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }
    
    /**
     * Prompts user to set or clear the closing date filter.
     * <p>
     * Allows users to filter internships by their application deadline. Useful for
     * finding opportunities that are still accepting applications.
     * 
     * <p><strong>Input Format:</strong> YYYY-MM-DD (e.g., 2025-12-31)
     * 
     * <p><strong>Options:</strong>
     * <ul>
     *   <li>Enter date in YYYY-MM-DD format</li>
     *   <li>Enter "clear" to remove date filter</li>
     * </ul>
     * 
     * <p><strong>Validation:</strong> Parses date and handles DateTimeParseException
     * for invalid formats.
     * 
     * @param criteria the filter criteria object to update
     */
    private void setClosingDateFilter(FilterCriteria criteria) {
        System.out.print("\nEnter closing date (YYYY-MM-DD) or 'clear' to remove filter: ");
        String input = scanner.nextLine().trim();
        
        if (input.equalsIgnoreCase("clear")) {
            criteria.setClosingDate(null);
            System.out.println("Closing date filter cleared.");
        } else {
            try {
                LocalDate date = LocalDate.parse(input);
                criteria.setClosingDate(date);
                System.out.println("Closing date filter set to: " + date);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Please use YYYY-MM-DD.");
            }
        }
    }
    
    /**
     * Prompts for quick filter criteria setup without a menu loop.
     * <p>
     * This alternative method provides a streamlined filter setup process, primarily
     * used by staff when generating reports. Instead of a menu loop, it sequentially
     * prompts for each filter criterion, allowing users to skip any by pressing Enter.
     * 
     * <p><strong>Use Case:</strong> Quick filter configuration for one-time report
     * generation, where a menu loop would be unnecessary overhead.
     * 
     * <p><strong>Prompt Sequence:</strong>
     * <ol>
     *   <li>Level (Basic/Intermediate/Advanced or skip)</li>
     *   <li>Major (enter major name or skip)</li>
     *   <li>Status (Pending/Approved/Rejected/Filled or skip)</li>
     * </ol>
     * 
     * <p><strong>User Experience:</strong> Press Enter to skip any criterion. Only
     * non-empty inputs are applied to the filter.
     * 
     * @param existingCriteria existing criteria to modify, or null to create new criteria
     * @return the updated or new FilterCriteria object with user-specified filters
     * 
     * @see ReportGenerator
     * @see CLIStaffBoundary#generateReport()
     */
    public FilterCriteria promptForCriteria(FilterCriteria existingCriteria) {
        FilterCriteria criteria = existingCriteria != null ? existingCriteria : new FilterCriteria();
        
        System.out.println("\n=== Set Filter Criteria ===");
        System.out.print("Filter by level? (Basic/Intermediate/Advanced or press Enter to skip): ");
        String level = scanner.nextLine().trim();
        if (!level.isEmpty()) {
            criteria.setLevel(level);
        }
        
        System.out.print("Filter by major? (Enter major or press Enter to skip): ");
        String major = scanner.nextLine().trim();
        if (!major.isEmpty()) {
            criteria.setMajor(major);
        }
        
        System.out.print("Filter by status? (Pending/Approved/Rejected/Filled or press Enter to skip): ");
        String status = scanner.nextLine().trim();
        if (!status.isEmpty()) {
            criteria.setStatus(status);
        }
        
        return criteria;
    }
}
