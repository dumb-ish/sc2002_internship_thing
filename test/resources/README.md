# Test Data Files

This directory contains CSV test data files for comprehensive testing of the Internship Placement Management System.

## Files

### test_students.csv
Contains sample student accounts for testing various scenarios:
- Students from different years (Year 1-4)
- Multiple majors (CS, IS, Business Analytics, Data Science, etc.)
- Used for testing eligibility filters and application workflows

### test_staff.csv
Contains Career Center Staff accounts for testing:
- Approval workflows (internships and company representatives)
- Withdrawal request management
- Report generation

### test_company_representatives.csv
Contains company representative accounts with various statuses:
- **Approved** - Can create and manage internships
- **Pending** - Awaiting staff approval
- **Rejected** - Denied access to create internships

### test_internships.csv
Contains diverse internship opportunities for testing:
- Different levels (Basic, Intermediate, Advanced)
- Various locations (Singapore, Remote, Kuala Lumpur)
- Multiple statuses (Approved, Pending, Rejected)
- Different visibility settings
- Range of majors and requirements

## Usage

### Option 1: Backup and Replace Production Data

```powershell
# Backup current data
New-Item -ItemType Directory -Force -Path backup
Copy-Item *.csv backup\

# Copy test data
Copy-Item test\resources\test_*.csv .

# Rename to production format
Rename-Item test_students.csv sample_student_list.csv -Force
Rename-Item test_staff.csv sample_staff_list.csv -Force
Rename-Item test_company_representatives.csv sample_company_representative_list.csv -Force
Rename-Item test_internships.csv internships.csv -Force

# Run tests

# Restore production data
Copy-Item backup\*.csv .
```

### Option 2: Modify System to Load Test Data

Update `InternshipPlacementSystem.java` to load from test resources:

```java
systemManager.initializeSystem(
    "test/resources/test_students.csv",
    "test/resources/test_staff.csv",
    "test/resources/test_company_representatives.csv"
);
```

## Test Accounts

### Students
| User ID | Name | Major | Year | Use Case |
|---------|------|-------|------|----------|
| U2310001A | Alice Tan | Computer Science | 3 | General testing, can apply to all levels |
| U2310002B | Bob Lee | Information Systems | 2 | Basic level only testing |
| U2310004D | David Wong | Business Analytics | 1 | First year restrictions |

### Staff
| User ID | Name | Password |
|---------|------|----------|
| STAFF001 | Dr. Sarah Chen | password |
| STAFF002 | Mr. Michael Lim | password |

### Company Representatives
| Email | Company | Status | Password |
|-------|---------|--------|----------|
| hr@techcorp.com | Tech Corp | Approved | password |
| pending@startup.com | StartUp Inc | Pending | password |
| rejected@badcompany.com | Bad Company | Rejected | password |

**All test accounts use password: `password`**
