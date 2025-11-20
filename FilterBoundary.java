import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Handles filter menu operations shared across all user types.
 * Allows users to set and modify their filter criteria.
 */
public class FilterBoundary {
    private Scanner scanner;
    
    /**
     * Constructor
     */
    public FilterBoundary() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Display filter menu and allow user to update criteria
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
     * Display student-specific filter menu with conditional editability
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
     * Set level filter
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
     * Set major filter
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
     * Set status filter
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
     * Set closing date filter
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
     * Prompt for criteria - alternative method for quick filter setup
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
