# Unit Test Suite

This directory contains JUnit 4 unit tests for the Internship Placement System.

## Test Classes

### 1. AuthenticationTest.java
Tests authentication and registration functionality:
- Valid login for all user types (Student, Staff, Company Representative)
- Invalid credentials handling
- Password change functionality
- Registration validation
- Pending/rejected company representative restrictions

### 2. ApplicationTest.java
Tests application management functionality:
- Application submission
- Application limit enforcement (3 applications per student)
- Status updates (Pending → Successful/Unsuccessful → Accepted)
- Withdrawal requests and approval
- Slot management

**Note:** Some methods referenced may need to be implemented:
- `Student.getActiveApplication()` / `setActiveApplication()`
- `ApplicationManager.handleRejection(Application)`
- `InternshipOpportunity.decrementSlot()`

### 3. InternshipTest.java
Tests internship opportunity management:
- Creating internships with validation
- Approval/rejection workflow
- Updating internship details
- Deleting internships (with/without applications)
- Creation limit (5 per company representative)

**Note:** Some methods referenced may need to be implemented:
- `InternshipManager.createInternship(...)`
- `InternshipManager.internshipExists(String id)`
- `InternshipManager.deleteInternship(String id)`
- `InternshipOpportunity.addApplication(Application)`

### 4. FilterTest.java
Tests filtering and sorting functionality:
- Filter by location
- Filter by salary
- Filter by company
- Multiple filter criteria
- Sort by salary (ascending/descending)

**Note:** Classes referenced may need to be implemented or adjusted:
- `InternshipFilter` class
- `FilterCriteria` class with appropriate getters/setters

## Running the Tests

### Prerequisites
1. JUnit 4.13.2 and Hamcrest Core 1.3 in classpath
2. Test CSV files in `test/resources/` directory
3. All source code compiled

### Compile Tests
```powershell
javac -encoding UTF-8 -source 8 -target 8 -cp ".;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" -d . test/unit/*.java
```

### Run All Tests
```powershell
java -cp ".;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore AuthenticationTest ApplicationTest InternshipTest FilterTest
```

### Run Individual Test Class
```powershell
java -cp ".;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore AuthenticationTest
```

## Implementation Notes

### Missing Methods to Implement

Based on the test suite, the following methods may need to be added to the codebase:

**Student.java:**
```java
private Application activeApplication;

public Application getActiveApplication() {
    return activeApplication;
}

public void setActiveApplication(Application app) {
    this.activeApplication = app;
}
```

**ApplicationManager.java:**
```java
public void handleRejection(Application app) {
    Student student = app.getStudent();
    if (student != null) {
        student.setActiveApplication(null);
    }
}
```

**InternshipOpportunity.java:**
```java
public void decrementSlot() {
    if (numSlots > 0) {
        numSlots--;
        if (numSlots == 0) {
            this.status = "Filled";
        }
    }
}

private List<Application> applications = new ArrayList<>();

public void addApplication(Application app) {
    applications.add(app);
}

public List<Application> getApplications() {
    return new ArrayList<>(applications);
}
```

**InternshipManager.java:**
```java
public InternshipOpportunity createInternship(String title, String description, 
        String requirements, String location, double salary, int slots, String companyName) {
    if (title == null || title.isEmpty() || slots <= 0) {
        return null;
    }
    // Create and return new internship
}

public boolean internshipExists(String internshipId) {
    return findInternshipById(internshipId) != null;
}

public boolean deleteInternship(String internshipId) {
    InternshipOpportunity internship = findInternshipById(internshipId);
    if (internship == null) return false;
    
    // Check if internship has applications
    if (internship.getApplications().size() > 0) {
        return false; // Cannot delete with applications
    }
    
    // Remove from list
    internships.remove(internship);
    return true;
}
```

**InternshipFilter.java** (new class):
```java
public class InternshipFilter {
    public List<InternshipOpportunity> filterByCriteria(
            List<InternshipOpportunity> internships, FilterCriteria criteria) {
        // Implement filtering logic
    }
    
    public void sortBySalary(List<InternshipOpportunity> internships, boolean ascending) {
        // Implement sorting logic
    }
}
```

**FilterCriteria.java** (new class):
```java
public class FilterCriteria {
    private String location;
    private Double minSalary;
    private String companyName;
    
    // Getters and setters
}
```

## Test Data

The tests use CSV files from `test/resources/`:
- `test_students.csv` - 8 test students with varying years and majors
- `test_staff.csv` - 3 staff members
- `test_company_representatives.csv` - 5 company reps with different statuses
- `test_internships.csv` - 8 diverse internship opportunities

See `test/resources/README.md` for details on the test data.

## Alignment with AppendixA

These unit tests complement the black-box test cases in AppendixA:
- **AppendixA Test Cases 1-4**: Covered by AuthenticationTest
- **AppendixA Test Cases 5-10**: Covered by ApplicationTest
- **AppendixA Test Cases 13-22**: Covered by InternshipTest
- **AppendixA Test Case 24**: Covered by FilterTest

For complete AppendixA test procedures, see `test/blackbox/APPENDIX_A_TEST_CASES.md`.

## Troubleshooting

### Compilation Errors
If you see "cannot find symbol" errors:
1. Ensure all source files are compiled first
2. Check that missing methods (listed above) are implemented
3. Verify classpath includes JUnit and Hamcrest JARs

### Test Failures
If tests fail unexpectedly:
1. Check that test CSV files are present and properly formatted
2. Verify SystemManager.initializeSystem() loads test data correctly
3. Ensure business logic matches test expectations
4. Check for hardcoded values that differ from test data

### ClassNotFoundException
If you see "ClassNotFoundException":
1. Ensure classpath includes current directory (`.`)
2. Verify all classes are compiled in the same directory structure
3. Check that test classes are compiled with source classes
