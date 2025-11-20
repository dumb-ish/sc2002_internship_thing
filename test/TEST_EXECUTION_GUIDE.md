# Test Execution Guide
## Internship Placement Management System

**Version:** 1.0  
**Date:** November 20, 2025

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Prerequisites](#2-prerequisites)
3. [Test Environment Setup](#3-test-environment-setup)
4. [Unit Testing Execution](#4-unit-testing-execution)
5. [Black-Box Testing Execution](#5-black-box-testing-execution)
6. [Test Data Preparation](#6-test-data-preparation)
7. [Recording Test Results](#7-recording-test-results)
8. [Troubleshooting](#8-troubleshooting)

---

## 1. Introduction

This guide provides step-by-step instructions for executing all test cases defined in the Test Plan Document. Follow these procedures to ensure consistent and thorough testing of the Internship Placement Management System.

---

## 2. Prerequisites

### 2.1 Software Requirements

Ensure the following are installed:

- **Java Development Kit (JDK)** 8 or higher
  ```powershell
  java -version
  javac -version
  ```

- **JUnit 4.13.2** and **Hamcrest Core 1.3** (for unit testing)
  - Download from: https://github.com/junit-team/junit4/releases
  - Download Hamcrest from: http://hamcrest.org/JavaHamcrest/

### 2.2 Project Setup

1. Clone or navigate to project directory:
   ```powershell
   cd c:\Users\songj\OneDrive\Documents\GitHub\sc2002_internship_thing
   ```

2. Verify all source files are present:
   ```powershell
   Get-ChildItem *.java
   ```

---

## 3. Test Environment Setup

### 3.1 Download JUnit Libraries

**Windows PowerShell:**
```powershell
# Create lib directory for test dependencies
New-Item -ItemType Directory -Force -Path lib

# Download JUnit 4.13.2
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar" -OutFile "lib\junit-4.13.2.jar"

# Download Hamcrest Core 1.3
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar" -OutFile "lib\hamcrest-core-1.3.jar"
```

**macOS/Linux:**
```bash
# Create lib directory
mkdir -p lib

# Download JUnit 4.13.2
curl -L "https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar" -o "lib/junit-4.13.2.jar"

# Download Hamcrest Core 1.3
curl -L "https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar" -o "lib/hamcrest-core-1.3.jar"
```

### 3.2 Compile Main Application

**Windows:**
```powershell
javac -encoding UTF-8 -source 8 -target 8 -d . *.java
```

**macOS/Linux:**
```bash
javac -encoding UTF-8 -source 8 -target 8 -d . *.java
```

### 3.3 Prepare Test Data

1. Navigate to test resources:
   ```powershell
   cd test\resources
   ```

2. Create test CSV files (see section 6 for details)

3. Return to project root:
   ```powershell
   cd ..\..
   ```

---

## 4. Unit Testing Execution

### 4.1 Compile Unit Tests

**Windows:**
```powershell
javac -encoding UTF-8 -cp ".;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar" -d . test\unit\*.java
```

**macOS/Linux:**
```bash
javac -encoding UTF-8 -cp ".:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar" -d . test/unit/*.java
```

### 4.2 Run All Unit Tests

**Windows:**
```powershell
java -cp ".;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar" org.junit.runner.JUnitCore AuthenticationTest ApplicationTest InternshipTest FilterTest
```

**macOS/Linux:**
```bash
java -cp ".:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore AuthenticationTest ApplicationTest InternshipTest FilterTest
```

### 4.3 Run Individual Test Classes

**Example: Run Authentication Tests Only**

**Windows:**
```powershell
java -cp ".;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar" org.junit.runner.JUnitCore AuthenticationTest
```

**macOS/Linux:**
```bash
java -cp ".:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore AuthenticationTest
```

### 4.4 Unit Test Checklist

For each unit test class, verify:

- [ ] **AuthenticationTest**
  - [ ] UT-AUTH-001: Valid Student Login
  - [ ] UT-AUTH-002: Valid Company Rep Login
  - [ ] UT-AUTH-003: Valid Staff Login
  - [ ] UT-AUTH-004: Invalid Password
  - [ ] UT-AUTH-005: Non-existent User
  - [ ] UT-AUTH-006: Rejected Company Rep
  - [ ] UT-AUTH-007: Valid Registration
  - [ ] UT-AUTH-008: Duplicate Email

- [ ] **ApplicationTest**
  - [ ] UT-APP-001: Valid Submission
  - [ ] UT-APP-002: Duplicate Prevention
  - [ ] UT-APP-003: Filled Internship
  - [ ] UT-APP-004: Accept Application
  - [ ] UT-APP-005: Reject Application
  - [ ] UT-APP-006: Student Accepts Offer
  - [ ] UT-APP-007: Request Withdrawal
  - [ ] UT-APP-008: Approve Withdrawal

- [ ] **InternshipTest**
  - [ ] UT-INT-001: Create Valid Internship
  - [ ] UT-INT-002: Invalid Data
  - [ ] UT-INT-003: Approve Internship
  - [ ] UT-INT-004: Reject Internship
  - [ ] UT-INT-005: Update Details
  - [ ] UT-INT-006: Delete Internship

- [ ] **UserManagementTest**
  - [ ] UT-USER-001: Approve Company Rep
  - [ ] UT-USER-002: Reject Company Rep
  - [ ] UT-USER-003: Update Student Profile

- [ ] **FilterTest**
  - [ ] UT-FILTER-001: Filter by Location
  - [ ] UT-FILTER-002: Filter by Salary
  - [ ] UT-FILTER-003: Filter by Company
  - [ ] UT-FILTER-004: Multiple Criteria

- [ ] **SortTest**
  - [ ] UT-SORT-001: Sort by Salary Ascending
  - [ ] UT-SORT-002: Sort by Salary Descending

---

## 5. Black-Box Testing Execution

Black-box tests are performed manually by interacting with the running application.

### 5.1 Start the Application

```powershell
java InternshipPlacementSystem
```

### 5.2 Execute Test Cases by Category

#### 5.2.1 Student Workflow Tests

##### Test Case BB-STU-001: Student Applies and Accepts Internship

**Setup:**
- Ensure student S001 exists with password
- Ensure at least one approved internship exists

**Steps:**

1. **Launch Application**
   ```
   java InternshipPlacementSystem
   ```
   ✅ **Verify:** Welcome screen displays

2. **Login as Student**
   - Select: `1` (Login)
   - Enter User ID: `S001`
   - Enter Password: `password`
   ✅ **Verify:** Login successful, student menu displays

3. **Browse Internships**
   - Select: `Browse Internships` (option number varies)
   ✅ **Verify:** List of approved internships displays

4. **Apply to Internship**
   - Select an internship from the list
   - Confirm application
   ✅ **Verify:** 
     - "Application submitted successfully" message
     - Application status is "Pending"

5. **Logout**
   - Select: `Logout` option
   ✅ **Verify:** Return to main menu

6. **Login as Company Representative**
   - Select: `1` (Login)
   - Enter User ID: `hr@techcorp.com`
   - Enter Password: `password`
   ✅ **Verify:** Company rep menu displays

7. **Approve Application**
   - Select: `View Applications`
   - Find pending application from S001
   - Select: `Accept Application`
   ✅ **Verify:** Status changes to "Successful"

8. **Logout from Company Rep**
   - Select: `Logout`

9. **Login as Student Again**
   - Select: `1` (Login)
   - Enter User ID: `S001`
   - Enter Password: `password`

10. **View Application Status**
    - Select: `View My Application`
    ✅ **Verify:** Status shows "Successful"

11. **Accept Offer**
    - Select: `Accept Offer`
    ✅ **Verify:** 
      - Status changes to "Accepted"
      - Cannot apply to other internships
      - Internship slot count decremented

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

##### Test Case BB-STU-002: Student Applies and Withdraws

**Steps:**

1. **Login as Student with Active Application**
   - User ID: `S002`
   - Password: `password`
   ✅ **Verify:** Has active application

2. **Request Withdrawal**
   - Select: `View My Application`
   - Select: `Request Withdrawal`
   ✅ **Verify:** Withdrawal request submitted

3. **Logout and Login as Staff**
   - User ID: `STAFF001`
   - Password: `password`

4. **Approve Withdrawal**
   - Select: `Manage Withdrawal Requests`
   - Find S002's withdrawal request
   - Select: `Approve`
   ✅ **Verify:** Withdrawal approved

5. **Login as Student S002**
   - Verify: No active application
   - Verify: Can browse and apply to new internships

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

##### Test Case BB-STU-003: Filter Internships by Multiple Criteria

**Steps:**

1. **Login as Student**
   - User ID: `S003`
   - Password: `password`

2. **Browse Internships**
   - Select: `Browse Internships`

3. **Apply Filters**
   - Select: `Filter Internships`
   - Set Location: `Singapore`
   - Set Min Salary: `1500`
   - Set Company: `Tech Corp`
   - Apply filters

4. **Verify Results**
   - Check each displayed internship
   ✅ **Verify:** All match ALL criteria
   - Location = Singapore
   - Salary >= 1500
   - Company = Tech Corp

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

##### Test Case BB-STU-004: Sort Internships

**Steps:**

1. **Login as Student**
   - User ID: `S001`
   - Password: `password`

2. **Browse Internships**
   - Select: `Browse Internships`

3. **Sort by Salary (High to Low)**
   - Select: `Sort Options`
   - Choose: `Salary (High to Low)`
   ✅ **Verify:** Internships display in descending salary order

4. **Sort by Salary (Low to High)**
   - Choose: `Salary (Low to High)`
   ✅ **Verify:** Internships display in ascending salary order

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

#### 5.2.2 Company Representative Workflow Tests

##### Test Case BB-REP-001: Complete Registration Process

**Steps:**

1. **Start Registration**
   - Launch application
   - Select: `2` (Register as Company Representative)

2. **Enter Registration Details**
   - Email: `newrep@testcompany.com`
   - Name: `Jane Smith`
   - Password: `testpass123`
   - Confirm Password: `testpass123`
   - Company Name: `Test Company Ltd`
   - Department: `Human Resources`
   - Position: `Talent Acquisition Manager`
   ✅ **Verify:** Registration success message displayed

3. **Attempt Login (Should Fail)**
   - Select: `1` (Login)
   - Enter User ID: `newrep@testcompany.com`
   - Enter Password: `testpass123`
   ✅ **Verify:** Login fails or shows "Pending approval" message

4. **Login as Staff and Approve**
   - User ID: `STAFF001`
   - Password: `password`
   - Select: `Manage Company Representatives`
   - Find `newrep@testcompany.com`
   - Select: `Approve`
   ✅ **Verify:** Status changes to "Approved"

5. **Logout and Login as New Rep**
   - User ID: `newrep@testcompany.com`
   - Password: `testpass123`
   ✅ **Verify:** Login successful, company rep menu displays

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

##### Test Case BB-REP-002: Registration with Duplicate Email

**Steps:**

1. **Attempt Duplicate Registration**
   - Select: `2` (Register as Company Representative)
   - Email: `hr@techcorp.com` (already exists)
   - Enter other valid details

2. **Verify Error Handling**
   ✅ **Verify:** 
   - Registration fails
   - Clear error message: "Email already exists"
   - No account created

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

##### Test Case BB-REP-003: Create and Manage Internship Posting

**Steps:**

1. **Login as Approved Company Rep**
   - User ID: `hr@techcorp.com`
   - Password: `password`

2. **Create New Internship**
   - Select: `Post New Internship`
   - Title: `Full Stack Developer Intern`
   - Description: `Work on web applications using React and Node.js`
   - Requirements: `JavaScript, React, Node.js, MongoDB`
   - Location: `Singapore`
   - Salary: `2200`
   - Available Slots: `2`
   ✅ **Verify:** Internship created with "Pending" status

3. **Logout and Login as Staff**
   - User ID: `STAFF001`
   - Password: `password`

4. **Approve Internship**
   - Select: `Manage Internship Postings`
   - Find new internship
   - Select: `Approve`
   ✅ **Verify:** Status changes to "Approved"

5. **Logout and Login as Student**
   - User ID: `S001`
   - Password: `password`
   - Select: `Browse Internships`
   ✅ **Verify:** New internship appears in list

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

##### Test Case BB-REP-004: Manage Applications

**Setup:** Ensure multiple students have applied to company's internship

**Steps:**

1. **Login as Company Rep**
   - User ID: `hr@techcorp.com`
   - Password: `password`

2. **View Applications**
   - Select: `View Applications for My Internships`
   ✅ **Verify:** List of pending applications displays

3. **Accept One Application**
   - Select first application
   - Choose: `Accept`
   ✅ **Verify:** 
     - Status changes to "Successful"
     - Confirmation message displayed

4. **Reject Another Application**
   - Select second application
   - Choose: `Reject`
   ✅ **Verify:** 
     - Status changes to "Unsuccessful"
     - Confirmation message displayed

5. **Logout and Verify Student Notifications**
   - Login as first student
   - Check application status = "Successful"
   - Logout and login as second student
   - Check application status = "Unsuccessful"

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

#### 5.2.3 Career Center Staff Workflow Tests

##### Test Case BB-STAFF-001: Manage Company Representative Approvals

**Setup:** Ensure at least 2 pending company representatives exist

**Steps:**

1. **Login as Career Center Staff**
   - User ID: `STAFF001`
   - Password: `password`

2. **View Pending Representatives**
   - Select: `Manage Company Representatives`
   - Select: `View Pending Representatives`
   ✅ **Verify:** List of pending reps displays with details

3. **Approve One Representative**
   - Select first pending rep
   - Review details (name, company, email)
   - Choose: `Approve`
   ✅ **Verify:** 
     - Status changes to "Approved"
     - Success message displayed

4. **Reject Another Representative**
   - Select second pending rep
   - Choose: `Reject`
   ✅ **Verify:** 
     - Status changes to "Rejected"
     - Success message displayed

5. **Verify Access Control**
   - Logout
   - Login as approved rep (should succeed)
   - Logout
   - Attempt login as rejected rep (should fail)

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

##### Test Case BB-STAFF-002: Manage Internship Approvals

**Setup:** Ensure pending internship postings exist

**Steps:**

1. **Login as Staff**
   - User ID: `STAFF002`
   - Password: `password`

2. **View Pending Internships**
   - Select: `Manage Internship Postings`
   - Select: `View Pending Postings`
   ✅ **Verify:** List of pending internships displays

3. **Review Internship Details**
   - Select an internship
   - View all details (title, description, requirements, salary, etc.)
   ✅ **Verify:** All information displays correctly

4. **Approve Valid Internship**
   - Choose: `Approve`
   ✅ **Verify:** Status changes to "Approved"

5. **Reject Inappropriate Internship**
   - Select another internship
   - Choose: `Reject`
   ✅ **Verify:** Status changes to "Rejected"

6. **Verify Student Visibility**
   - Logout and login as student
   - Browse internships
   ✅ **Verify:** 
     - Approved internship visible
     - Rejected internship NOT visible

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

##### Test Case BB-STAFF-003: Process Withdrawal Requests

**Setup:** Student has submitted withdrawal request

**Steps:**

1. **Student Submits Withdrawal**
   - Login as S001 (with active application)
   - Request withdrawal
   - Logout

2. **Staff Processes Withdrawal**
   - Login as STAFF001
   - Select: `Manage Withdrawal Requests`
   ✅ **Verify:** Withdrawal request appears

3. **Review Request Details**
   - Select withdrawal request
   - View student info, internship info, application status
   ✅ **Verify:** All details correct

4. **Approve Withdrawal**
   - Choose: `Approve Withdrawal`
   ✅ **Verify:** Approval confirmation message

5. **Verify Student Status**
   - Logout and login as S001
   - Verify: No active application
   - Browse internships
   - Verify: Can apply to new internships

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

##### Test Case BB-STAFF-004: Generate Placement Report

**Setup:** System has various applications and placements

**Steps:**

1. **Login as Staff**
   - User ID: `STAFF001`
   - Password: `password`

2. **Access Reporting Feature**
   - Select: `Generate Reports`
   - Select: `Placement Statistics`

3. **Verify Report Content**
   ✅ **Verify report includes:**
   - [ ] Total number of internships posted
   - [ ] Total number of approved internships
   - [ ] Total number of applications submitted
   - [ ] Total number of successful placements
   - [ ] Placement rate (percentage)
   - [ ] List of top companies by application count
   - [ ] List of popular internships

4. **Verify Data Accuracy**
   - Manually count internships and compare
   - Calculate expected placement rate
   ✅ **Verify:** Report data matches manual count

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

#### 5.2.4 Edge Cases and Error Handling Tests

##### Test Case BB-EDGE-001: Maximum Applications per Internship

**Setup:** Create internship with 1 slot

**Steps:**

1. **Create Small Internship**
   - Login as company rep
   - Create internship with 1 slot
   - Get approved by staff

2. **First Student Applies**
   - Login as S001
   - Apply to internship
   - Company rep accepts
   - S001 accepts offer
   ✅ **Verify:** Internship slot filled (1/1)

3. **Attempt Second Application**
   - Login as S002
   - Browse internships
   ✅ **Verify:** 
     - Filled internship marked as "Full" or hidden
     - Cannot apply to filled internship

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

##### Test Case BB-EDGE-002: Empty Search Results

**Steps:**

1. **Login as Student**
   - User ID: `S001`
   - Password: `password`

2. **Apply Impossible Filters**
   - Browse internships
   - Set filters:
     - Location: `Antarctica`
     - Min Salary: `999999`
   - Apply filters

3. **Verify Handling**
   ✅ **Verify:** 
   - "No internships found" message displays
   - System remains stable (no crash)
   - Can clear filters and try again

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

##### Test Case BB-EDGE-003: Invalid Email Format

**Steps:**

1. **Start Registration**
   - Select: Register as Company Representative

2. **Test Invalid Formats**
   
   **Test 2.1:** Email without @
   - Enter: `invalidemail.com`
   ✅ **Verify:** Error message, re-prompt for valid email

   **Test 2.2:** Email with multiple @
   - Enter: `user@@company.com`
   ✅ **Verify:** Error message, re-prompt

   **Test 2.3:** Email without domain
   - Enter: `user@`
   ✅ **Verify:** Error message, re-prompt

   **Test 2.4:** Email without local part
   - Enter: `@company.com`
   ✅ **Verify:** Error message, re-prompt

3. **Enter Valid Email**
   - Enter: `valid@company.com`
   ✅ **Verify:** Accepted, proceed to next field

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

##### Test Case BB-EDGE-004: Empty Required Fields

**Steps:**

1. **Test Empty Title (Internship Posting)**
   - Login as company rep
   - Start posting new internship
   - Leave title empty
   - Attempt to proceed
   ✅ **Verify:** Error or re-prompt for title

2. **Test Empty Name (Registration)**
   - Start company rep registration
   - Enter email
   - Leave name empty
   - Attempt to proceed
   ✅ **Verify:** Error or re-prompt for name

3. **Test Empty Password**
   - Continue registration
   - Leave password empty
   - Attempt to proceed
   ✅ **Verify:** Error or re-prompt for password

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

##### Test Case BB-EDGE-005: Special Characters in Input

**Steps:**

1. **Test Special Chars in Internship Title**
   - Login as company rep
   - Create internship with title: `Software & Data <Analysis> Intern`
   - Complete posting
   ✅ **Verify:** 
     - Data saved correctly
     - Displays correctly in lists
     - No system errors

2. **Test Quotes in Description**
   - Description: `Looking for "talented" developer with 'passion'`
   ✅ **Verify:** Quotes handled correctly

3. **Test Symbols in Requirements**
   - Requirements: `C++, C#, Node.js, .NET`
   ✅ **Verify:** Symbols preserved correctly

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

##### Test Case BB-EDGE-006: Data Persists After Restart

**Steps:**

1. **Create Test Data**
   - Login and create new internship
   - Submit new application
   - Register new company rep
   - Note down specific details

2. **Exit Application Completely**
   - Select: Exit
   - Verify application closed

3. **Restart Application**
   - Launch: `java InternshipPlacementSystem`
   ✅ **Verify:** System starts successfully

4. **Verify Data Persistence**
   - Login and check internship still exists
   - Verify application still exists
   - Verify new company rep still in system
   ✅ **Verify:** All data persisted correctly

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

##### Test Case BB-EDGE-007: CSV File Integrity

**Steps:**

1. **Initial State**
   - Close application
   - Backup existing CSV files

2. **Run Application and Make Changes**
   - Start application
   - Create new internship
   - Submit application
   - Exit application

3. **Inspect CSV Files**
   - Open `internships.csv`
   ✅ **Verify:** 
     - New internship row added
     - Format is correct
     - No corruption

   - Open `internship_status.csv`
   ✅ **Verify:** 
     - New application row added
     - All fields correct
     - Proper CSV format maintained

4. **Restart and Verify**
   - Start application again
   ✅ **Verify:** Data loads correctly from CSV files

**Expected Result:** ✅ PASS / ❌ FAIL

**Notes:** _______________________________________________

---

## 6. Test Data Preparation

### 6.1 Create Test Student Data

Create file: `test/resources/test_students.csv`

```csv
StudentID,Name,Password,GPA,Major
S001,Alice Tan,password,3.8,Computer Science
S002,Bob Lee,password,3.5,Information Systems
S003,Carol Chen,password,3.9,Computer Science
S004,David Wong,password,3.2,Business Analytics
S005,Emily Lim,password,3.7,Computer Engineering
```

### 6.2 Create Test Staff Data

Create file: `test/resources/test_staff.csv`

```csv
StaffID,Name,Password
STAFF001,Grace Koh,password
STAFF002,Henry Ng,password
STAFF003,Irene Tan,password
```

### 6.3 Create Test Company Representative Data

Create file: `test/resources/test_company_representatives.csv`

```csv
Email,Name,Password,CompanyName,Department,Position,Status
hr@techcorp.com,David Wong,password,Tech Corp,HR,HR Manager,Approved
recruiter@innovate.com,Emily Lim,password,Innovate Pte Ltd,Recruitment,Recruiter,Approved
pending@startup.com,Frank Tan,password,StartUp Inc,HR,Talent Lead,Pending
rejected@company.com,George Lee,password,Bad Company,HR,Manager,Rejected
```

### 6.4 Create Test Internship Data

Create file: `test/resources/test_internships.csv`

```csv
InternshipID,Title,Description,Requirements,Location,Salary,AvailableSlots,CompanyName,Status
INT001,Software Engineering Intern,Backend development with Java and Spring Boot,Java 8+|Spring Boot|MySQL|REST APIs,Singapore,2000.0,3,Tech Corp,Approved
INT002,Data Analyst Intern,Analyze data and create visualizations,Python|Pandas|Tableau|SQL,Singapore,1800.0,2,Innovate Pte Ltd,Approved
INT003,Web Developer Intern,Frontend development with React,JavaScript|React|HTML/CSS|Git,Remote,1500.0,1,StartUp Inc,Pending
INT004,Mobile App Developer,iOS and Android development,Swift|Kotlin|React Native,Singapore,2200.0,2,Tech Corp,Approved
INT005,UI/UX Design Intern,Design user interfaces and experiences,Figma|Adobe XD|Sketch,Singapore,1600.0,1,Innovate Pte Ltd,Rejected
```

### 6.5 Using Test Data

**To use test data:**

1. **Backup production data:**
   ```powershell
   Copy-Item *.csv backup\
   ```

2. **Copy test data:**
   ```powershell
   Copy-Item test\resources\test_*.csv .
   Rename-Item test_students.csv sample_student_list.csv
   Rename-Item test_staff.csv sample_staff_list.csv
   Rename-Item test_company_representatives.csv sample_company_representative_list.csv
   Rename-Item test_internships.csv internships.csv
   ```

3. **Run tests**

4. **Restore production data:**
   ```powershell
   Copy-Item backup\*.csv .
   ```

---

## 7. Recording Test Results

### 7.1 Test Results Template

Create file: `test/TEST_RESULTS.md`

```markdown
# Test Results Log

## Test Execution Details
- **Date:** YYYY-MM-DD
- **Tester:** Your Name
- **Environment:** Windows/macOS/Linux
- **Java Version:** X.X.X

## Unit Test Results

| Test ID | Test Name | Status | Notes |
|---------|-----------|--------|-------|
| UT-AUTH-001 | Valid Student Login | ✅ PASS | |
| UT-AUTH-002 | Valid Company Rep Login | ✅ PASS | |
| ... | ... | ... | ... |

## Black-Box Test Results

| Test ID | Test Name | Status | Issues Found |
|---------|-----------|--------|--------------|
| BB-STU-001 | Apply and Accept | ✅ PASS | None |
| BB-STU-002 | Apply and Withdraw | ❌ FAIL | Withdrawal not clearing activeApplication |
| ... | ... | ... | ... |

## Defects Found

### Defect #1
- **Test ID:** BB-STU-002
- **Severity:** High
- **Description:** Student activeApplication not cleared after withdrawal approval
- **Steps to Reproduce:** [detailed steps]
- **Expected:** Application cleared
- **Actual:** Application remains
- **Status:** Open

## Summary
- **Total Tests:** XX
- **Passed:** XX
- **Failed:** XX
- **Pass Rate:** XX%
```

### 7.2 Taking Screenshots

For each failed test:
1. Take screenshot of error
2. Save to `test/screenshots/`
3. Reference in test results

---

## 8. Troubleshooting

### 8.1 Common Issues

**Issue: ClassNotFoundException when running JUnit tests**

**Solution:**
Ensure JUnit JARs are in classpath:
```powershell
java -cp ".;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar" org.junit.runner.JUnitCore TestClassName
```

---

**Issue: UnsupportedClassVersionError**

**Solution:**
Compile with correct Java version:
```powershell
javac -source 8 -target 8 *.java
```

---

**Issue: Test data not loading**

**Solution:**
1. Verify CSV files exist in correct location
2. Check CSV format (no extra spaces, proper encoding)
3. Verify file paths in code match actual files

---

**Issue: Cannot modify CSV files (in use)**

**Solution:**
1. Close application completely
2. Ensure no Java processes running: `taskkill /F /IM java.exe`
3. Reopen CSV files

---

### 8.2 Getting Help

If issues persist:
1. Review test plan documentation
2. Check system logs/error messages
3. Verify prerequisites installed correctly
4. Contact project team lead

---

**Document Version Control**

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-11-20 | Initial test execution guide |
