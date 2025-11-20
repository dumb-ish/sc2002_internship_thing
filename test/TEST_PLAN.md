# Test Plan Document
## Internship Placement Management System

**Document Version:** 1.0  
**Date:** November 20, 2025  
**Project:** SC2002 Internship Placement System  
**Authors:** SC2002 Group

---

## Table of Contents

1. [Introduction](#1-introduction)
   - 1.1 [Purpose](#11-purpose)
   - 1.2 [Scope](#12-scope)
   - 1.3 [Test Objectives](#13-test-objectives)
2. [Test Strategy](#2-test-strategy)
   - 2.1 [Unit Testing](#21-unit-testing)
   - 2.2 [Black-Box Testing](#22-black-box-testing)
3. [Unit Test Cases](#3-unit-test-cases)
   - 3.1 [Authentication Tests](#31-authentication-tests)
   - 3.2 [Application Management Tests](#32-application-management-tests)
   - 3.3 [Internship Management Tests](#33-internship-management-tests)
   - 3.4 [User Management Tests](#34-user-management-tests)
   - 3.5 [Filter and Sort Tests](#35-filter-and-sort-tests)
4. [Black-Box Test Cases](#4-black-box-test-cases)
   - 4.1 [Student Workflow Tests](#41-student-workflow-tests)
   - 4.2 [Company Representative Workflow Tests](#42-company-representative-workflow-tests)
   - 4.3 [Career Center Staff Workflow Tests](#43-career-center-staff-workflow-tests)
   - 4.4 [Edge Cases and Error Handling](#44-edge-cases-and-error-handling)
5. [Test Data](#5-test-data)
6. [Test Environment](#6-test-environment)
7. [Test Deliverables](#7-test-deliverables)

---

## 1. Introduction

### 1.1 Purpose

This document describes the test plan for the Internship Placement Management System. It defines the testing approach, test cases, and procedures to ensure the system meets all functional and non-functional requirements.

### 1.2 Scope

The testing scope includes:
- Unit testing of individual components and methods
- Black-box testing of complete user workflows
- Functional testing of all user roles (Student, Company Representative, Career Center Staff)
- Validation of data persistence and retrieval
- Error handling and boundary condition testing

### 1.3 Test Objectives

- Verify that all system functionalities work as specified
- Ensure data integrity across operations
- Validate user authentication and authorization
- Test error handling and recovery mechanisms
- Confirm system reliability and stability

---

## 2. Test Strategy

### 2.1 Unit Testing

Unit tests focus on testing individual components in isolation:
- **Framework:** JUnit 4.13.2
- **Coverage Target:** 80% code coverage minimum
- **Approach:** White-box testing with method-level granularity
- **Mocking:** Use of mock objects for dependencies

### 2.2 Black-Box Testing

Black-box tests validate complete user workflows:
- **Approach:** Functional testing without knowledge of internal implementation
- **Method:** Manual testing with documented test scripts
- **Coverage:** All user stories and use cases
- **Data:** Predefined test data sets

---

## 3. Unit Test Cases

### 3.1 Authentication Tests

#### 3.1.1 User Authentication

##### 3.1.1.1 Valid Login - Student
- **Test ID:** UT-AUTH-001
- **Description:** Verify successful student login with valid credentials
- **Preconditions:** Student account exists in system
- **Test Data:**
  - User ID: `S001`
  - Password: `password`
- **Steps:**
  1. Call `SystemManager.authenticateUser("S001", "password")`
  2. Verify returned User object is not null
  3. Verify User is instance of Student class
- **Expected Result:** Authentication successful, Student object returned
- **Priority:** High

##### 3.1.1.2 Valid Login - Company Representative
- **Test ID:** UT-AUTH-002
- **Description:** Verify successful company rep login with valid credentials
- **Preconditions:** Company representative account exists with "Approved" status
- **Test Data:**
  - User ID: `hr@techcorp.com`
  - Password: `password`
- **Steps:**
  1. Call `SystemManager.authenticateUser("hr@techcorp.com", "password")`
  2. Verify returned User object is not null
  3. Verify User is instance of CompanyRepresentative class
  4. Verify status is "Approved"
- **Expected Result:** Authentication successful, CompanyRepresentative object returned
- **Priority:** High

##### 3.1.1.3 Valid Login - Career Center Staff
- **Test ID:** UT-AUTH-003
- **Description:** Verify successful staff login with valid credentials
- **Preconditions:** Staff account exists in system
- **Test Data:**
  - User ID: `STAFF001`
  - Password: `password`
- **Steps:**
  1. Call `SystemManager.authenticateUser("STAFF001", "password")`
  2. Verify returned User object is not null
  3. Verify User is instance of CareerCenterStaff class
- **Expected Result:** Authentication successful, CareerCenterStaff object returned
- **Priority:** High

##### 3.1.1.4 Invalid Login - Wrong Password
- **Test ID:** UT-AUTH-004
- **Description:** Verify login fails with incorrect password
- **Preconditions:** User account exists
- **Test Data:**
  - User ID: `S001`
  - Password: `wrongpassword`
- **Steps:**
  1. Call `SystemManager.authenticateUser("S001", "wrongpassword")`
  2. Verify returned value is null
- **Expected Result:** Authentication fails, null returned
- **Priority:** High

##### 3.1.1.5 Invalid Login - Non-existent User
- **Test ID:** UT-AUTH-005
- **Description:** Verify login fails for non-existent user
- **Preconditions:** None
- **Test Data:**
  - User ID: `INVALID123`
  - Password: `password`
- **Steps:**
  1. Call `SystemManager.authenticateUser("INVALID123", "password")`
  2. Verify returned value is null
- **Expected Result:** Authentication fails, null returned
- **Priority:** High

##### 3.1.1.6 Invalid Login - Rejected Company Representative
- **Test ID:** UT-AUTH-006
- **Description:** Verify login is blocked for rejected company representative
- **Preconditions:** Company representative exists with "Rejected" status
- **Test Data:**
  - User ID: `rejected@company.com`
  - Password: `password`
  - Status: `Rejected`
- **Steps:**
  1. Create CompanyRepresentative with "Rejected" status
  2. Verify status equals "Rejected"
  3. Attempt login through UI boundary
- **Expected Result:** Login blocked with appropriate error message
- **Priority:** High

#### 3.1.2 Company Representative Registration

##### 3.1.2.1 Valid Registration
- **Test ID:** UT-AUTH-007
- **Description:** Verify successful company representative registration
- **Preconditions:** Email address not already registered
- **Test Data:**
  - Email: `newrep@company.com`
  - Name: `John Doe`
  - Password: `password123`
  - Company: `Tech Corp`
  - Department: `HR`
  - Position: `Recruiter`
- **Steps:**
  1. Call `SystemManager.registerCompanyRepresentative(...)` with valid data
  2. Verify returned CompanyRepresentative object is not null
  3. Verify status is "Pending"
  4. Verify all fields are correctly set
- **Expected Result:** Registration successful, account created with "Pending" status
- **Priority:** High

##### 3.1.2.2 Duplicate Email Registration
- **Test ID:** UT-AUTH-008
- **Description:** Verify registration fails for duplicate email
- **Preconditions:** Email address already registered
- **Test Data:**
  - Email: `existing@company.com` (already exists)
  - Other fields: valid data
- **Steps:**
  1. Attempt to register with existing email
  2. Verify returned value is null
- **Expected Result:** Registration fails, null returned
- **Priority:** High

### 3.2 Application Management Tests

#### 3.2.1 Application Submission

##### 3.2.1.1 Valid Application Submission
- **Test ID:** UT-APP-001
- **Description:** Verify student can submit application for internship
- **Preconditions:** 
  - Student has no active application
  - Internship is available
- **Test Data:**
  - Student: Valid student object
  - Internship: Valid internship object
- **Steps:**
  1. Call `ApplicationManager.submitApplication(student, internship)`
  2. Verify application is created
  3. Verify application status is "Pending"
  4. Verify student's activeApplication is set
  5. Verify internship's applications list contains new application
- **Expected Result:** Application submitted successfully
- **Priority:** High

##### 3.2.1.2 Duplicate Application Prevention
- **Test ID:** UT-APP-002
- **Description:** Verify student cannot submit multiple applications
- **Preconditions:** Student already has an active application
- **Test Data:**
  - Student: Student with existing application
  - Internship: Different internship
- **Steps:**
  1. Attempt to call `ApplicationManager.submitApplication(...)`
  2. Verify method returns false or throws exception
- **Expected Result:** Application submission blocked
- **Priority:** High

##### 3.2.1.3 Application to Filled Internship
- **Test ID:** UT-APP-003
- **Description:** Verify cannot apply to already filled internship
- **Preconditions:** Internship has reached maximum capacity
- **Test Data:**
  - Internship: Filled internship (applications count = slots)
- **Steps:**
  1. Verify internship.isFilled() returns true
  2. Attempt application submission
  3. Verify submission is blocked
- **Expected Result:** Application blocked for filled internship
- **Priority:** Medium

#### 3.2.2 Application Status Updates

##### 3.2.2.1 Accept Application
- **Test ID:** UT-APP-004
- **Description:** Verify company rep can accept application
- **Preconditions:** Application exists with "Pending" status
- **Test Data:**
  - Application: Pending application
  - New Status: "Successful"
- **Steps:**
  1. Call `application.updateStatus("Successful")`
  2. Verify application status changed to "Successful"
  3. Verify student is notified (if notification system exists)
- **Expected Result:** Application status updated to "Successful"
- **Priority:** High

##### 3.2.2.2 Reject Application
- **Test ID:** UT-APP-005
- **Description:** Verify company rep can reject application
- **Preconditions:** Application exists with "Pending" status
- **Test Data:**
  - Application: Pending application
  - New Status: "Unsuccessful"
- **Steps:**
  1. Call `application.updateStatus("Unsuccessful")`
  2. Verify application status changed to "Unsuccessful"
  3. Verify student's activeApplication is cleared
- **Expected Result:** Application status updated to "Unsuccessful", student freed
- **Priority:** High

##### 3.2.2.3 Student Accepts Offer
- **Test ID:** UT-APP-006
- **Description:** Verify student can accept successful application
- **Preconditions:** Application status is "Successful"
- **Test Data:**
  - Application: Application with "Successful" status
- **Steps:**
  1. Call `application.updateStatus("Accepted")`
  2. Verify status changed to "Accepted"
  3. Verify internship slot is reduced
- **Expected Result:** Application accepted, internship slot filled
- **Priority:** High

#### 3.2.3 Application Withdrawal

##### 3.2.3.1 Request Withdrawal
- **Test ID:** UT-APP-007
- **Description:** Verify student can request withdrawal
- **Preconditions:** Student has active application
- **Test Data:**
  - Application: Active application
- **Steps:**
  1. Call `application.markWithdrawalRequested()`
  2. Verify withdrawalRequested flag is true
  3. Verify application status remains unchanged
- **Expected Result:** Withdrawal request marked, awaiting approval
- **Priority:** High

##### 3.2.3.2 Approve Withdrawal
- **Test ID:** UT-APP-008
- **Description:** Verify staff can approve withdrawal request
- **Preconditions:** Application has withdrawal request
- **Test Data:**
  - Application: Application with withdrawalRequested = true
- **Steps:**
  1. Process withdrawal approval
  2. Verify application is removed
  3. Verify student's activeApplication is cleared
- **Expected Result:** Withdrawal approved, student freed
- **Priority:** High

### 3.3 Internship Management Tests

#### 3.3.1 Internship Creation

##### 3.3.1.1 Create Valid Internship
- **Test ID:** UT-INT-001
- **Description:** Verify company rep can create internship posting
- **Preconditions:** Company rep is approved
- **Test Data:**
  - Title: `Software Engineering Intern`
  - Description: `Backend development role`
  - Requirements: `Java, Spring Boot`
  - Location: `Singapore`
  - Salary: `2000.0`
  - Slots: `3`
- **Steps:**
  1. Call `InternshipManager.createInternship(...)` with valid data
  2. Verify InternshipOpportunity object is created
  3. Verify all fields are correctly set
  4. Verify initial status is "Pending"
- **Expected Result:** Internship created with "Pending" status
- **Priority:** High

##### 3.3.1.2 Create Internship with Invalid Data
- **Test ID:** UT-INT-002
- **Description:** Verify internship creation fails with invalid data
- **Preconditions:** None
- **Test Data:**
  - Title: `` (empty)
  - Slots: `0` or negative
- **Steps:**
  1. Attempt to create internship with empty title
  2. Attempt to create internship with invalid slots
  3. Verify creation fails or validation occurs
- **Expected Result:** Creation fails with appropriate error
- **Priority:** Medium

#### 3.3.2 Internship Approval

##### 3.3.2.1 Approve Internship
- **Test ID:** UT-INT-003
- **Description:** Verify staff can approve internship posting
- **Preconditions:** Internship exists with "Pending" status
- **Test Data:**
  - Internship: Pending internship
- **Steps:**
  1. Call `internship.updateStatus("Approved")`
  2. Verify status changed to "Approved"
  3. Verify internship appears in student search results
- **Expected Result:** Internship approved and visible to students
- **Priority:** High

##### 3.3.2.2 Reject Internship
- **Test ID:** UT-INT-004
- **Description:** Verify staff can reject internship posting
- **Preconditions:** Internship exists with "Pending" status
- **Test Data:**
  - Internship: Pending internship
- **Steps:**
  1. Call `internship.updateStatus("Rejected")`
  2. Verify status changed to "Rejected"
  3. Verify internship does not appear in student search
- **Expected Result:** Internship rejected and hidden from students
- **Priority:** High

#### 3.3.3 Internship Updates

##### 3.3.3.1 Update Internship Details
- **Test ID:** UT-INT-005
- **Description:** Verify company rep can update internship details
- **Preconditions:** Internship exists, company rep owns it
- **Test Data:**
  - Original title: `Software Intern`
  - New title: `Senior Software Intern`
- **Steps:**
  1. Update internship fields
  2. Verify changes are persisted
  3. Verify existing applications are not affected
- **Expected Result:** Internship updated successfully
- **Priority:** Medium

##### 3.3.3.2 Delete Internship
- **Test ID:** UT-INT-006
- **Description:** Verify company rep can delete internship with no applications
- **Preconditions:** Internship has no applications
- **Test Data:**
  - Internship: Internship with 0 applications
- **Steps:**
  1. Call delete/remove method
  2. Verify internship is removed from system
- **Expected Result:** Internship deleted successfully
- **Priority:** Medium

### 3.4 User Management Tests

#### 3.4.1 Company Representative Approval

##### 3.4.1.1 Approve Company Representative
- **Test ID:** UT-USER-001
- **Description:** Verify staff can approve pending company representative
- **Preconditions:** Company rep with "Pending" status exists
- **Test Data:**
  - Representative: Pending company rep
- **Steps:**
  1. Call status update to "Approved"
  2. Verify status changed
  3. Verify rep can now login and post internships
- **Expected Result:** Representative approved and granted full access
- **Priority:** High

##### 3.4.1.2 Reject Company Representative
- **Test ID:** UT-USER-002
- **Description:** Verify staff can reject pending company representative
- **Preconditions:** Company rep with "Pending" status exists
- **Test Data:**
  - Representative: Pending company rep
- **Steps:**
  1. Call status update to "Rejected"
  2. Verify status changed
  3. Verify rep cannot login
- **Expected Result:** Representative rejected and denied access
- **Priority:** High

#### 3.4.2 Student Data Management

##### 3.4.2.1 Update Student Profile
- **Test ID:** UT-USER-003
- **Description:** Verify student can update profile information
- **Preconditions:** Student account exists
- **Test Data:**
  - Student: Valid student
  - New data: Updated name, GPA, major
- **Steps:**
  1. Update student fields
  2. Verify changes are saved
  3. Verify data persists after logout/login
- **Expected Result:** Profile updated successfully
- **Priority:** Medium

### 3.5 Filter and Sort Tests

#### 3.5.1 Filtering Tests

##### 3.5.1.1 Filter by Location
- **Test ID:** UT-FILTER-001
- **Description:** Verify internships can be filtered by location
- **Preconditions:** Multiple internships with different locations exist
- **Test Data:**
  - Filter criteria: Location = "Singapore"
- **Steps:**
  1. Create FilterCriteria with location = "Singapore"
  2. Apply filter to internship list
  3. Verify all returned internships have location "Singapore"
- **Expected Result:** Only Singapore internships returned
- **Priority:** Medium

##### 3.5.1.2 Filter by Salary Range
- **Test ID:** UT-FILTER-002
- **Description:** Verify internships can be filtered by minimum salary
- **Preconditions:** Internships with various salaries exist
- **Test Data:**
  - Filter criteria: MinSalary = 1500.0
- **Steps:**
  1. Create FilterCriteria with minSalary = 1500.0
  2. Apply filter
  3. Verify all returned internships have salary >= 1500.0
- **Expected Result:** Only internships meeting salary threshold returned
- **Priority:** Medium

##### 3.5.1.3 Filter by Company
- **Test ID:** UT-FILTER-003
- **Description:** Verify internships can be filtered by company name
- **Preconditions:** Multiple companies with internships exist
- **Test Data:**
  - Filter criteria: CompanyName = "Tech Corp"
- **Steps:**
  1. Create FilterCriteria with companyName = "Tech Corp"
  2. Apply filter
  3. Verify all returned internships are from Tech Corp
- **Expected Result:** Only Tech Corp internships returned
- **Priority:** Medium

##### 3.5.1.4 Multiple Filter Criteria
- **Test ID:** UT-FILTER-004
- **Description:** Verify multiple filters can be applied simultaneously
- **Preconditions:** Diverse internship data exists
- **Test Data:**
  - Filter: Location = "Singapore" AND MinSalary = 1500.0
- **Steps:**
  1. Create FilterCriteria with multiple conditions
  2. Apply combined filters
  3. Verify results meet ALL criteria
- **Expected Result:** Only internships matching all filters returned
- **Priority:** High

#### 3.5.2 Sorting Tests

##### 3.5.2.1 Sort by Salary Ascending
- **Test ID:** UT-SORT-001
- **Description:** Verify internships can be sorted by salary (low to high)
- **Preconditions:** Multiple internships with different salaries exist
- **Test Data:**
  - Internships with salaries: 1000, 2000, 1500
- **Steps:**
  1. Apply salary sorting (ascending)
  2. Verify result order: 1000, 1500, 2000
- **Expected Result:** Internships sorted correctly by salary ascending
- **Priority:** Medium

##### 3.5.2.2 Sort by Salary Descending
- **Test ID:** UT-SORT-002
- **Description:** Verify internships can be sorted by salary (high to low)
- **Preconditions:** Multiple internships with different salaries exist
- **Test Data:**
  - Internships with salaries: 1000, 2000, 1500
- **Steps:**
  1. Apply salary sorting (descending)
  2. Verify result order: 2000, 1500, 1000
- **Expected Result:** Internships sorted correctly by salary descending
- **Priority:** Medium

---

## 4. Black-Box Test Cases

### 4.1 Student Workflow Tests

#### 4.1.1 Complete Application Process

##### 4.1.1.1 Student Applies and Accepts Internship
- **Test ID:** BB-STU-001
- **Description:** End-to-end test of student applying and accepting internship
- **Preconditions:** 
  - Student account exists (S001/password)
  - Approved internship exists
- **Test Steps:**
  1. Launch application: `java InternshipPlacementSystem`
  2. Select option 1 (Login)
  3. Enter Student ID: `S001`, Password: `password`
  4. From student menu, select "Browse Internships"
  5. View available internships
  6. Select an internship and apply
  7. Logout
  8. Login as Company Rep and approve application
  9. Logout and login as Student again
  10. View application status (should be "Successful")
  11. Accept the offer
- **Expected Result:** 
  - Application submitted successfully
  - Status changes from Pending → Successful → Accepted
  - Student can no longer apply to other internships
- **Priority:** Critical

##### 4.1.1.2 Student Applies and Withdraws
- **Test ID:** BB-STU-002
- **Description:** Test student requesting application withdrawal
- **Preconditions:** Student has active application
- **Test Steps:**
  1. Login as student with active application
  2. Select "View My Application"
  3. Request withdrawal
  4. Logout
  5. Login as Career Center Staff
  6. Approve withdrawal request
  7. Logout and login as student
  8. Verify no active application
  9. Verify can apply to new internship
- **Expected Result:** 
  - Withdrawal request submitted
  - After approval, student freed to apply elsewhere
- **Priority:** High

#### 4.1.2 Search and Filter Tests

##### 4.1.2.1 Filter Internships by Multiple Criteria
- **Test ID:** BB-STU-003
- **Description:** Test advanced filtering functionality
- **Preconditions:** Multiple diverse internships exist
- **Test Steps:**
  1. Login as student
  2. Select "Browse Internships"
  3. Apply filters:
     - Location: Singapore
     - Min Salary: 1500
     - Company: Tech Corp
  4. View filtered results
  5. Verify all results match criteria
- **Expected Result:** Only internships matching ALL criteria displayed
- **Priority:** Medium

##### 4.1.2.2 Sort Internships
- **Test ID:** BB-STU-004
- **Description:** Test sorting functionality
- **Preconditions:** Multiple internships with different salaries exist
- **Test Steps:**
  1. Login as student
  2. Browse internships
  3. Sort by salary (high to low)
  4. Verify order is correct
  5. Sort by salary (low to high)
  6. Verify order is correct
- **Expected Result:** Internships displayed in correct sorted order
- **Priority:** Medium

### 4.2 Company Representative Workflow Tests

#### 4.2.1 Registration and Approval

##### 4.2.1.1 Complete Registration Process
- **Test ID:** BB-REP-001
- **Description:** End-to-end company representative registration
- **Preconditions:** None
- **Test Steps:**
  1. Launch application
  2. Select option 2 (Register as Company Representative)
  3. Enter valid details:
     - Email: `newrep@company.com`
     - Name: `John Doe`
     - Password: `password123`
     - Confirm Password: `password123`
     - Company: `Tech Corp`
     - Department: `HR`
     - Position: `Recruiter`
  4. Verify registration success message
  5. Attempt to login (should fail - pending approval)
  6. Login as Career Center Staff
  7. Approve the new representative
  8. Logout
  9. Login as new representative
  10. Verify successful login
- **Expected Result:** 
  - Registration successful with Pending status
  - Cannot login until approved
  - After approval, can login successfully
- **Priority:** Critical

##### 4.2.1.2 Registration with Duplicate Email
- **Test ID:** BB-REP-002
- **Description:** Test duplicate email validation
- **Preconditions:** Email already registered
- **Test Steps:**
  1. Attempt registration with existing email
  2. Verify error message displayed
  3. Verify registration fails
- **Expected Result:** Registration fails with clear error message
- **Priority:** High

#### 4.2.2 Internship Management

##### 4.2.2.1 Create and Manage Internship Posting
- **Test ID:** BB-REP-003
- **Description:** Complete internship posting workflow
- **Preconditions:** Approved company rep account exists
- **Test Steps:**
  1. Login as company representative
  2. Select "Post New Internship"
  3. Enter internship details:
     - Title: `Software Engineering Intern`
     - Description: `Backend development with Java`
     - Requirements: `Java, Spring Boot, MySQL`
     - Location: `Singapore`
     - Salary: `2000`
     - Slots: `3`
  4. Submit posting
  5. Verify pending approval message
  6. Logout and login as Staff
  7. Approve internship
  8. Logout and verify internship visible to students
- **Expected Result:** 
  - Internship created with Pending status
  - After approval, visible to all students
- **Priority:** Critical

##### 4.2.2.2 Manage Applications
- **Test ID:** BB-REP-004
- **Description:** Review and process student applications
- **Preconditions:** 
  - Company rep has posted internship
  - Students have applied
- **Test Steps:**
  1. Login as company representative
  2. Select "View Applications"
  3. Review pending applications
  4. Accept one application
  5. Reject another application
  6. Verify status updates are reflected
  7. Logout and login as affected students
  8. Verify each student sees correct status
- **Expected Result:** 
  - Applications processed correctly
  - Students notified of status changes
- **Priority:** Critical

### 4.3 Career Center Staff Workflow Tests

#### 4.3.1 Approval Management

##### 4.3.1.1 Manage Company Representative Approvals
- **Test ID:** BB-STAFF-001
- **Description:** Process pending company representative registrations
- **Preconditions:** Pending representatives exist
- **Test Steps:**
  1. Login as Career Center Staff
  2. Select "Manage Company Representatives"
  3. View pending representatives
  4. Approve one representative
  5. Reject another representative
  6. Verify status updates
  7. Logout
  8. Verify approved rep can login
  9. Verify rejected rep cannot login
- **Expected Result:** 
  - Approved reps gain access
  - Rejected reps denied access
- **Priority:** Critical

##### 4.3.1.2 Manage Internship Approvals
- **Test ID:** BB-STAFF-002
- **Description:** Review and approve internship postings
- **Preconditions:** Pending internship postings exist
- **Test Steps:**
  1. Login as Career Center Staff
  2. Select "Manage Internship Postings"
  3. View pending internships
  4. Review internship details
  5. Approve valid internships
  6. Reject inappropriate internships
  7. Logout and login as student
  8. Verify only approved internships visible
- **Expected Result:** 
  - Only approved internships visible to students
  - Rejected internships hidden
- **Priority:** Critical

#### 4.3.2 Withdrawal Management

##### 4.3.2.1 Process Withdrawal Requests
- **Test ID:** BB-STAFF-003
- **Description:** Handle student withdrawal requests
- **Preconditions:** Student has requested withdrawal
- **Test Steps:**
  1. Student submits withdrawal request
  2. Login as Career Center Staff
  3. Select "Manage Withdrawal Requests"
  4. View pending withdrawals
  5. Review and approve withdrawal
  6. Logout and login as student
  7. Verify application removed
  8. Verify can apply to new internship
- **Expected Result:** 
  - Withdrawal processed
  - Student freed to apply elsewhere
- **Priority:** High

#### 4.3.3 Reporting

##### 4.3.3.1 Generate Placement Report
- **Test ID:** BB-STAFF-004
- **Description:** Generate and view system reports
- **Preconditions:** System has application data
- **Test Steps:**
  1. Login as Career Center Staff
  2. Select "Generate Reports"
  3. Generate placement statistics report
  4. Verify report contains:
     - Total internships
     - Total applications
     - Placement rate
     - Popular companies
  5. Verify data accuracy
- **Expected Result:** Report generated with accurate statistics
- **Priority:** Medium

### 4.4 Edge Cases and Error Handling

#### 4.4.1 Boundary Conditions

##### 4.4.1.1 Maximum Applications per Internship
- **Test ID:** BB-EDGE-001
- **Description:** Test behavior when internship reaches capacity
- **Preconditions:** Internship with 1 slot available
- **Test Steps:**
  1. Have student apply to internship
  2. Company rep accepts application
  3. Student accepts offer
  4. Verify internship is now full
  5. Attempt to have another student apply
  6. Verify application is blocked or internship hidden
- **Expected Result:** Cannot apply to filled internship
- **Priority:** High

##### 4.4.1.2 Empty Search Results
- **Test ID:** BB-EDGE-002
- **Description:** Test handling of no matching internships
- **Preconditions:** System has internships
- **Test Steps:**
  1. Login as student
  2. Apply very restrictive filters that match nothing
  3. Verify appropriate message displayed
  4. Verify system remains stable
- **Expected Result:** "No internships found" message, no errors
- **Priority:** Low

#### 4.4.2 Invalid Input Handling

##### 4.4.2.1 Invalid Email Format
- **Test ID:** BB-EDGE-003
- **Description:** Test email validation during registration
- **Preconditions:** None
- **Test Steps:**
  1. Start company rep registration
  2. Enter invalid email formats:
     - No @ symbol: `invalidemail.com`
     - Multiple @: `user@@company.com`
     - No domain: `user@`
  3. Verify validation error for each
  4. Enter valid email and complete registration
- **Expected Result:** Invalid emails rejected with clear error messages
- **Priority:** Medium

##### 4.4.2.2 Empty Required Fields
- **Test ID:** BB-EDGE-004
- **Description:** Test required field validation
- **Preconditions:** None
- **Test Steps:**
  1. Attempt to create internship with empty title
  2. Attempt registration with empty name
  3. Attempt to leave password blank
  4. Verify each validation works correctly
- **Expected Result:** Cannot submit with empty required fields
- **Priority:** Medium

##### 4.4.2.3 Special Characters in Input
- **Test ID:** BB-EDGE-005
- **Description:** Test handling of special characters
- **Preconditions:** None
- **Test Steps:**
  1. Enter internship title with special chars: `<script>alert('test')</script>`
  2. Enter description with quotes and symbols
  3. Verify data is stored correctly without breaking system
  4. Verify data displays correctly
- **Expected Result:** Special characters handled safely
- **Priority:** Medium

#### 4.4.3 Data Persistence

##### 4.4.3.1 Data Persists After Restart
- **Test ID:** BB-EDGE-006
- **Description:** Verify all data persists between sessions
- **Preconditions:** None
- **Test Steps:**
  1. Create new internship
  2. Submit application
  3. Register new company rep
  4. Exit application completely
  5. Restart application
  6. Login as various users
  7. Verify all data still exists
- **Expected Result:** All data persisted correctly
- **Priority:** Critical

##### 4.4.3.2 CSV File Integrity
- **Test ID:** BB-EDGE-007
- **Description:** Test CSV file reading and writing
- **Preconditions:** CSV files exist
- **Test Steps:**
  1. Verify system loads data from CSV on startup
  2. Make changes (new internship, application, etc.)
  3. Exit system
  4. Manually inspect CSV files
  5. Verify changes are written correctly
  6. Verify CSV format is valid
- **Expected Result:** CSV files updated correctly, valid format maintained
- **Priority:** High

---

## 5. Test Data

### 5.1 Sample Users

#### Students
- **S001**: Name: Alice Tan, GPA: 3.8, Major: Computer Science, Password: password
- **S002**: Name: Bob Lee, GPA: 3.5, Major: Information Systems, Password: password
- **S003**: Name: Carol Chen, GPA: 3.9, Major: Computer Science, Password: password

#### Company Representatives
- **hr@techcorp.com**: Name: David Wong, Company: Tech Corp, Status: Approved, Password: password
- **recruiter@innovate.com**: Name: Emily Lim, Company: Innovate Pte Ltd, Status: Approved, Password: password
- **pending@startup.com**: Name: Frank Tan, Company: StartUp Inc, Status: Pending, Password: password

#### Career Center Staff
- **STAFF001**: Name: Grace Koh, Password: password
- **STAFF002**: Name: Henry Ng, Password: password

### 5.2 Sample Internships

1. **Software Engineering Intern**
   - Company: Tech Corp
   - Location: Singapore
   - Salary: 2000
   - Slots: 3
   - Status: Approved

2. **Data Analyst Intern**
   - Company: Innovate Pte Ltd
   - Location: Singapore
   - Salary: 1800
   - Slots: 2
   - Status: Approved

3. **Web Developer Intern**
   - Company: StartUp Inc
   - Location: Remote
   - Salary: 1500
   - Slots: 1
   - Status: Pending

### 5.3 Test Data Files

Test CSV files should be placed in `test/resources/` directory:
- `test_students.csv`
- `test_staff.csv`
- `test_company_representatives.csv`
- `test_internships.csv`
- `test_applications.csv`

---

## 6. Test Environment

### 6.1 Hardware Requirements
- **Processor:** Intel i3 or equivalent
- **RAM:** 4GB minimum
- **Storage:** 100MB free space

### 6.2 Software Requirements
- **JDK:** Version 8 or higher
- **JUnit:** Version 4.13.2
- **Operating System:** Windows 10/11, macOS 10.15+, or Linux

### 6.3 Test Environment Setup

1. **Compilation:**
   ```bash
   javac -encoding UTF-8 -source 8 -target 8 -d . *.java
   ```

2. **Run Tests:**
   ```bash
   java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore [TestClassName]
   ```

3. **Test Data:**
   - Use dedicated test CSV files
   - Reset data between test runs
   - Maintain separate test and production data

---

## 7. Test Deliverables

### 7.1 Test Documentation
- ✅ This Test Plan Document
- Test Execution Guide (see `TEST_EXECUTION_GUIDE.md`)
- Test Case Results Log

### 7.2 Test Code
- Unit test classes in `test/unit/` directory
- Black-box test scripts in `test/blackbox/` directory
- Test data files in `test/resources/` directory

### 7.3 Test Reports
- Test execution summary
- Code coverage report
- Defect log (if issues found)
- Test completion sign-off

---

**Document Control**

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | 2025-11-20 | SC2002 Group | Initial test plan creation |

---

**Approval**

| Role | Name | Signature | Date |
|------|------|-----------|------|
| Test Lead | | | |
| Project Manager | | | |
| Quality Assurance | | | |
