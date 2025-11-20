# Test Results Log

**Project:** Internship Placement Management System  
**Test Execution Date:** ___________________  
**Tester Name:** ___________________  
**Environment:** Windows / macOS / Linux (circle one)  
**Java Version:** ___________________

---

## Executive Summary

| Metric | Count |
|--------|-------|
| **Total Test Cases** | |
| **Tests Passed** | |
| **Tests Failed** | |
| **Tests Blocked** | |
| **Pass Rate** | % |

---

## Unit Test Results

### Authentication Tests (UT-AUTH)

| Test ID | Test Name | Status | Notes |
|---------|-----------|--------|-------|
| UT-AUTH-001 | Valid Student Login | ☐ PASS ☐ FAIL | |
| UT-AUTH-002 | Valid Company Rep Login | ☐ PASS ☐ FAIL | |
| UT-AUTH-003 | Valid Staff Login | ☐ PASS ☐ FAIL | |
| UT-AUTH-004 | Invalid Password | ☐ PASS ☐ FAIL | |
| UT-AUTH-005 | Non-existent User | ☐ PASS ☐ FAIL | |
| UT-AUTH-006 | Rejected Company Rep | ☐ PASS ☐ FAIL | |
| UT-AUTH-007 | Valid Registration | ☐ PASS ☐ FAIL | |
| UT-AUTH-008 | Duplicate Email | ☐ PASS ☐ FAIL | |

**Subsection Pass Rate:** _____ / 8 (_____%)

---

### Application Tests (UT-APP)

| Test ID | Test Name | Status | Notes |
|---------|-----------|--------|-------|
| UT-APP-001 | Valid Submission | ☐ PASS ☐ FAIL | |
| UT-APP-002 | Duplicate Prevention | ☐ PASS ☐ FAIL | |
| UT-APP-003 | Filled Internship | ☐ PASS ☐ FAIL | |
| UT-APP-004 | Accept Application | ☐ PASS ☐ FAIL | |
| UT-APP-005 | Reject Application | ☐ PASS ☐ FAIL | |
| UT-APP-006 | Student Accepts Offer | ☐ PASS ☐ FAIL | |
| UT-APP-007 | Request Withdrawal | ☐ PASS ☐ FAIL | |
| UT-APP-008 | Approve Withdrawal | ☐ PASS ☐ FAIL | |

**Subsection Pass Rate:** _____ / 8 (_____%)

---

### Internship Tests (UT-INT)

| Test ID | Test Name | Status | Notes |
|---------|-----------|--------|-------|
| UT-INT-001 | Create Valid Internship | ☐ PASS ☐ FAIL | |
| UT-INT-002 | Invalid Data | ☐ PASS ☐ FAIL | |
| UT-INT-003 | Approve Internship | ☐ PASS ☐ FAIL | |
| UT-INT-004 | Reject Internship | ☐ PASS ☐ FAIL | |
| UT-INT-005 | Update Details | ☐ PASS ☐ FAIL | |
| UT-INT-006 | Delete Internship | ☐ PASS ☐ FAIL | |

**Subsection Pass Rate:** _____ / 6 (_____%)

---

### User Management Tests (UT-USER)

| Test ID | Test Name | Status | Notes |
|---------|-----------|--------|-------|
| UT-USER-001 | Approve Company Rep | ☐ PASS ☐ FAIL | |
| UT-USER-002 | Reject Company Rep | ☐ PASS ☐ FAIL | |
| UT-USER-003 | Update Student Profile | ☐ PASS ☐ FAIL | |

**Subsection Pass Rate:** _____ / 3 (_____%)

---

### Filter Tests (UT-FILTER)

| Test ID | Test Name | Status | Notes |
|---------|-----------|--------|-------|
| UT-FILTER-001 | Filter by Location | ☐ PASS ☐ FAIL | |
| UT-FILTER-002 | Filter by Salary | ☐ PASS ☐ FAIL | |
| UT-FILTER-003 | Filter by Company | ☐ PASS ☐ FAIL | |
| UT-FILTER-004 | Multiple Criteria | ☐ PASS ☐ FAIL | |

**Subsection Pass Rate:** _____ / 4 (_____%)

---

### Sort Tests (UT-SORT)

| Test ID | Test Name | Status | Notes |
|---------|-----------|--------|-------|
| UT-SORT-001 | Sort Salary Ascending | ☐ PASS ☐ FAIL | |
| UT-SORT-002 | Sort Salary Descending | ☐ PASS ☐ FAIL | |

**Subsection Pass Rate:** _____ / 2 (_____%)

---

**Unit Test Total Pass Rate:** _____ / 31 (_____%)

---

## Black-Box Test Results

### Student Workflow Tests (BB-STU)

| Test ID | Test Name | Status | Issues Found |
|---------|-----------|--------|--------------|
| BB-STU-001 | Apply and Accept | ☐ PASS ☐ FAIL | |
| BB-STU-002 | Apply and Withdraw | ☐ PASS ☐ FAIL | |
| BB-STU-003 | Filter Multiple Criteria | ☐ PASS ☐ FAIL | |
| BB-STU-004 | Sort Internships | ☐ PASS ☐ FAIL | |

**Subsection Pass Rate:** _____ / 4 (_____%)

---

### Company Representative Workflow Tests (BB-REP)

| Test ID | Test Name | Status | Issues Found |
|---------|-----------|--------|--------------|
| BB-REP-001 | Complete Registration | ☐ PASS ☐ FAIL | |
| BB-REP-002 | Duplicate Email | ☐ PASS ☐ FAIL | |
| BB-REP-003 | Create Internship | ☐ PASS ☐ FAIL | |
| BB-REP-004 | Manage Applications | ☐ PASS ☐ FAIL | |

**Subsection Pass Rate:** _____ / 4 (_____%)

---

### Career Center Staff Workflow Tests (BB-STAFF)

| Test ID | Test Name | Status | Issues Found |
|---------|-----------|--------|--------------|
| BB-STAFF-001 | Manage Rep Approvals | ☐ PASS ☐ FAIL | |
| BB-STAFF-002 | Manage Internships | ☐ PASS ☐ FAIL | |
| BB-STAFF-003 | Process Withdrawals | ☐ PASS ☐ FAIL | |
| BB-STAFF-004 | Generate Reports | ☐ PASS ☐ FAIL | |

**Subsection Pass Rate:** _____ / 4 (_____%)

---

### Edge Cases and Error Handling Tests (BB-EDGE)

| Test ID | Test Name | Status | Issues Found |
|---------|-----------|--------|--------------|
| BB-EDGE-001 | Max Applications | ☐ PASS ☐ FAIL | |
| BB-EDGE-002 | Empty Results | ☐ PASS ☐ FAIL | |
| BB-EDGE-003 | Invalid Email | ☐ PASS ☐ FAIL | |
| BB-EDGE-004 | Empty Fields | ☐ PASS ☐ FAIL | |
| BB-EDGE-005 | Special Characters | ☐ PASS ☐ FAIL | |
| BB-EDGE-006 | Data Persistence | ☐ PASS ☐ FAIL | |
| BB-EDGE-007 | CSV Integrity | ☐ PASS ☐ FAIL | |

**Subsection Pass Rate:** _____ / 7 (_____%)

---

**Black-Box Test Total Pass Rate:** _____ / 19 (_____%)

---

## Defects Found

### Defect #1
- **Test ID:** _______________
- **Severity:** ☐ Critical  ☐ High  ☐ Medium  ☐ Low
- **Component:** _______________
- **Description:** 
  
  _______________________________________________________________
  
  _______________________________________________________________

- **Steps to Reproduce:**
  1. _______________________________________________________________
  2. _______________________________________________________________
  3. _______________________________________________________________

- **Expected Result:** _______________________________________________

- **Actual Result:** _______________________________________________

- **Screenshot/Evidence:** _______________________________________________

- **Status:** ☐ Open  ☐ In Progress  ☐ Fixed  ☐ Won't Fix

---

### Defect #2
- **Test ID:** _______________
- **Severity:** ☐ Critical  ☐ High  ☐ Medium  ☐ Low
- **Component:** _______________
- **Description:** 
  
  _______________________________________________________________
  
  _______________________________________________________________

- **Steps to Reproduce:**
  1. _______________________________________________________________
  2. _______________________________________________________________
  3. _______________________________________________________________

- **Expected Result:** _______________________________________________

- **Actual Result:** _______________________________________________

- **Screenshot/Evidence:** _______________________________________________

- **Status:** ☐ Open  ☐ In Progress  ☐ Fixed  ☐ Won't Fix

---

### Defect #3
- **Test ID:** _______________
- **Severity:** ☐ Critical  ☐ High  ☐ Medium  ☐ Low
- **Component:** _______________
- **Description:** 
  
  _______________________________________________________________
  
  _______________________________________________________________

- **Steps to Reproduce:**
  1. _______________________________________________________________
  2. _______________________________________________________________
  3. _______________________________________________________________

- **Expected Result:** _______________________________________________

- **Actual Result:** _______________________________________________

- **Screenshot/Evidence:** _______________________________________________

- **Status:** ☐ Open  ☐ In Progress  ☐ Fixed  ☐ Won't Fix

---

*(Add more defects as needed)*

---

## Test Environment Details

### Hardware
- **Processor:** _______________________________________________
- **RAM:** _______________________________________________
- **Storage:** _______________________________________________

### Software
- **Operating System:** _______________________________________________
- **Java Version:** _______________________________________________
- **JUnit Version:** _______________________________________________

### Test Data
- **Student Accounts Used:** _______________________________________________
- **Company Rep Accounts Used:** _______________________________________________
- **Staff Accounts Used:** _______________________________________________
- **Internships Created:** _______________________________________________

---

## Notes and Observations

_______________________________________________________________

_______________________________________________________________

_______________________________________________________________

_______________________________________________________________

_______________________________________________________________

---

## Recommendations

_______________________________________________________________

_______________________________________________________________

_______________________________________________________________

_______________________________________________________________

---

## Sign-Off

| Role | Name | Signature | Date |
|------|------|-----------|------|
| **Tester** | | | |
| **Test Lead** | | | |
| **Project Manager** | | | |
| **Quality Assurance** | | | |

---

**End of Test Results Report**
