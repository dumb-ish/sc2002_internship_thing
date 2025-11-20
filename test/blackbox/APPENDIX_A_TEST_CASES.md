# Black-Box Test Scripts
## Based on SC2002 Assignment AppendixA

**Test Execution Date:** ___________________  
**Tester Name:** ___________________

---

## Test Case 1: Valid User Login

**Test ID:** BB-TC001  
**Expected Behavior:** User should be able to access their dashboard based on their roles  
**Failure Indicators:** User cannot log in or receive incorrect error messages

### Test Steps:

**Test 1.1: Student Login**
1. Launch application: `java InternshipPlacementSystem`
2. Select option: `1` (Login)
3. Enter User ID: `U2310001A`
4. Enter Password: `password`

✅ **Expected Result:**
- Login successful message displayed
- Student dashboard/menu appears
- Student name "Alice Tan" displayed

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

**Test 1.2: Company Representative Login**
1. Select option: `1` (Login)
2. Enter User ID: `hr@techcorp.com`
3. Enter Password: `password`

✅ **Expected Result:**
- Login successful
- Company representative menu appears
- Representative name displayed

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

**Test 1.3: Career Center Staff Login**
1. Select option: `1` (Login)
2. Enter User ID: `STAFF001`
3. Enter Password: `password`

✅ **Expected Result:**
- Login successful
- Staff menu appears with administrative options
- Staff name displayed

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 2: Invalid ID

**Test ID:** BB-TC002  
**Expected Behavior:** User receives a notification about incorrect ID  
**Failure Indicators:** User is allowed to log in with invalid ID or fails to provide meaningful error message

### Test Steps:

1. Launch application
2. Select option: `1` (Login)
3. Enter User ID: `INVALID999`
4. Enter Password: `password`

✅ **Expected Result:**
- Login fails
- Error message: "Invalid credentials! Please try again."
- User returned to main menu

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 3: Incorrect Password

**Test ID:** BB-TC003  
**Expected Behavior:** System should deny access and alert the user to incorrect password  
**Failure Indicators:** User logs in successfully with a wrong password or fail to provide meaningful error message

### Test Steps:

1. Launch application
2. Select option: `1` (Login)
3. Enter User ID: `U2310001A` (valid student ID)
4. Enter Password: `wrongpassword123`

✅ **Expected Result:**
- Login fails
- Generic error message (for security): "Invalid credentials! Please try again."
- Does NOT reveal whether ID or password was wrong
- User returned to main menu

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 4: Password Change Functionality

**Test ID:** BB-TC004  
**Expected Behavior:** System updates password, prompts re-login and allows login with new credentials  
**Failure Indicators:** System does not update password or denies access with the new password

### Test Steps:

1. Login as Student (U2310001A / password)
2. Navigate to "Change Password" option
3. Enter current password: `password`
4. Enter new password: `newpass123`
5. Confirm new password: `newpass123`
6. Logout
7. Attempt login with old password: `password`
8. Attempt login with new password: `newpass123`

✅ **Expected Result:**
- Password change successful message
- System logs out user after password change
- Login with old password FAILS
- Login with new password SUCCEEDS

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

**Cleanup:** Change password back to `password` for subsequent tests

---

## Test Case 5: Company Representative Account Creation

**Test ID:** BB-TC005  
**Expected Behavior:** A new Company Representative should only be able to log in to their account after it has been approved by a Career Center Staff  
**Failure Indicators:** Company Representative staff can log in without any authorization

### Test Steps:

**Step 5.1: Self-Registration**
1. From main menu, select: `2` (Register as Company Representative)
2. Enter Email: `newrep@testcompany.com`
3. Enter Name: `John Smith`
4. Enter Password: `testpass123`
5. Confirm Password: `testpass123`
6. Enter Company Name: `Test Company Ltd`
7. Enter Department: `Human Resources`
8. Enter Position: `Talent Acquisition Manager`

✅ **Expected Result:**
- Registration success message
- Message: "Your account is pending approval by Career Center Staff"
- Message: "You will be able to login once approved"
- User ID displayed: `newrep@testcompany.com`

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 5.2: Attempt Login Before Approval**
1. Select option: `1` (Login)
2. Enter User ID: `newrep@testcompany.com`
3. Enter Password: `testpass123`

✅ **Expected Result:**
- Login FAILS or shows limited access
- Message indicates pending approval
- Cannot create internships

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 5.3: Staff Approval**
1. Login as Staff (STAFF001 / password)
2. Select "Manage Company Representatives"
3. View pending representatives
4. Find `newrep@testcompany.com`
5. Select "Approve"

✅ **Expected Result:**
- Approval successful message
- Status changed to "Approved"

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 5.4: Login After Approval**
1. Logout from staff account
2. Login as: `newrep@testcompany.com` / `testpass123`

✅ **Expected Result:**
- Login SUCCESSFUL
- Full company representative menu appears
- Can create internships

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 6: Internship Opportunity Visibility Based on User Profile and Toggle

**Test ID:** BB-TC006  
**Expected Behavior:** Internship opportunities are visible to students based on their year of study, major, internship level eligibility, and the visibility setting  
**Failure Indicators:** Students see internship opportunities not relevant to their profile (wrong major, wrong level for their year) or when visibility is off

### Test Steps:

**Step 6.1: Year 2 Student - Basic Level Only**
1. Login as: `U2310002B` (Bob Lee, IS, Year 2)
2. Select "Browse Internships"
3. View all available internships

✅ **Expected Result:**
- Only "Basic" level internships displayed
- No "Intermediate" or "Advanced" internships visible
- Only internships with visibility=true shown
- Only approved internships shown

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 6.2: Year 3 Student - All Levels**
1. Logout and login as: `U2310001A` (Alice Tan, CS, Year 3)
2. Select "Browse Internships"

✅ **Expected Result:**
- Basic, Intermediate, AND Advanced internships all visible
- Visibility toggle respected
- Only approved internships

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 6.3: Major-Based Filtering**
1. While logged in as Alice (CS major)
2. Browse internships

✅ **Expected Result:**
- Internships with PreferredMajor="Computer Science" are visible
- May also see internships with no major restriction

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 6.4: Toggle Visibility Off**
1. Login as company rep: `hr@techcorp.com`
2. View "My Internship Postings"
3. Select internship: "Software Engineering Intern"
4. Toggle visibility to OFF
5. Logout

6. Login as student: `U2310001A`
7. Browse internships

✅ **Expected Result:**
- "Software Engineering Intern" NO LONGER appears in list
- Other Tech Corp internships with visibility=ON still appear

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 6.5: Toggle Visibility Back On**
1. Login as: `hr@techcorp.com`
2. Toggle visibility back to ON for "Software Engineering Intern"
3. Logout

4. Login as student: `U2310001A`
5. Browse internships

✅ **Expected Result:**
- "Software Engineering Intern" now appears again

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 7: Internship Application Eligibility

**Test ID:** BB-TC007  
**Expected Behavior:** Students can only apply for internship opportunities relevant to their profile (correct major preference, appropriate level for their year of study) and when visibility is on  
**Failure Indicators:** Students can apply for internship opportunities not relevant to their profile (wrong major, wrong level) or when visibility is off

### Test Steps:

**Step 7.1: Eligible Application**
1. Login as: `U2310001A` (Alice, CS, Year 3)
2. Browse internships
3. Select: "Software Engineering Intern" (Intermediate, CS)
4. Submit application

✅ **Expected Result:**
- Application submitted successfully
- Confirmation message displayed
- Application status: "Pending"

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 7.2: Ineligible - Wrong Level for Year**
1. Logout and login as: `U2310002B` (Bob, IS, Year 2)
2. Attempt to apply for "Mobile App Developer" (Advanced level)

✅ **Expected Result:**
- Application BLOCKED
- Error message: "Year 1 and 2 students can only apply for Basic-level internships"

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 7.3: Application When Visibility is Off**
1. Company rep toggles internship visibility OFF
2. Student attempts to browse and find the internship

✅ **Expected Result:**
- Internship does NOT appear in browse list
- Student cannot apply (internship not visible)

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 8: Viewing Application Status after Visibility Toggle Off

**Test ID:** BB-TC008  
**Expected Behavior:** Students continue to have access to their application details regardless of internship opportunities' visibility  
**Failure Indicators:** Application details become inaccessible once visibility is off

### Test Steps:

**Setup:** Student has active application for an internship

1. Login as student with active application: `U2310001A`
2. Verify application exists in "My Applications"
3. Note internship details
4. Logout

5. Login as company rep who owns the internship
6. Toggle internship visibility to OFF
7. Logout

8. Login as student: `U2310001A`
9. Select "View My Applications"
10. View application details

✅ **Expected Result:**
- Application STILL visible in student's application list
- All application details accessible
- Status visible
- Can still request withdrawal
- Internship details still shown

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 10: Single Internship Placement Acceptance per Student

**Test ID:** BB-TC010  
**Expected Behavior:** System allows accepting one internship placement and automatically withdraws all other applications once a placement is accepted  
**Failure Indicators:** Student can accept more than one internship placement, or other applications remain active after accepting a placement

### Test Steps:

**Setup:** Student has multiple successful applications

1. Login as student: `U2310001A`
2. Apply to 3 different internships
3. Logout

4. Company reps approve all 3 applications (status="Successful")

5. Login as student: `U2310001A`
6. View "My Applications"
7. Verify 3 applications all show "Successful"
8. Select first internship
9. Choose "Accept Offer"

✅ **Expected Result:**
- First application status changes to "Accepted"
- Other 2 applications AUTOMATICALLY WITHDRAWN
- Confirmation message shown
- Student can no longer apply to new internships
- Other students can now apply to the 2 freed internships

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 13: Company Representative Internship Opportunity Creation

**Test ID:** BB-TC013  
**Expected Behavior:** System allows Company Representatives to create internship opportunities only when they meet system requirements  
**Failure Indicators:** System allows creation of opportunities with invalid data or exceeds maximum allowed opportunities per representative

### Test Steps:

**Step 13.1: Valid Creation Within Limit**
1. Login as: `hr@techcorp.com` (has < 5 internships)
2. Select "Post New Internship"
3. Enter details:
   - Title: `Backend Engineer Intern`
   - Description: `Work on microservices architecture`
   - Requirements: `Java, Spring Boot, Docker`
   - Location: `Singapore`
   - Salary: `2100`
   - Number of Slots: `2`
   - Level: `Intermediate`
   - Preferred Major: `Computer Science`
   - Start Date: `2025-05-01`
   - End Date: `2025-08-31`
4. Submit

✅ **Expected Result:**
- Internship created successfully
- Status: "Pending" (awaiting staff approval)
- Confirmation message with internship details

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 13.2: Invalid Data - Empty Title**
1. Attempt to create internship with empty title
2. Fill other fields validly

✅ **Expected Result:**
- Creation BLOCKED
- Error message about required field

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 13.3: Invalid Data - Zero or Negative Slots**
1. Attempt to create internship with NumSlots = 0

✅ **Expected Result:**
- Creation BLOCKED
- Error message about invalid slot number

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 13.4: Maximum Limit (5 Opportunities)**
1. Create internships until reaching 5 total
2. Attempt to create 6th internship

✅ **Expected Result:**
- 6th creation BLOCKED
- Error message: "You have reached the maximum number of internship opportunities (5)"

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 14: Internship Opportunity Approval Status

**Test ID:** BB-TC014  
**Expected Behavior:** Company Representatives can view pending, approved, or rejected status updates for their submitted opportunities  
**Failure Indicators:** Status updates are not visible, incorrect, or not properly saved in the system

### Test Steps:

1. Login as company rep: `hr@techcorp.com`
2. View "My Internship Postings"
3. Note status of each internship
4. Logout

5. Login as staff: `STAFF001`
6. Select "Manage Internship Postings"
7. View pending internships
8. Approve one internship
9. Reject another internship
10. Logout

11. Login as company rep: `hr@techcorp.com`
12. View "My Internship Postings"

✅ **Expected Result:**
- Approved internship shows status: "Approved"
- Rejected internship shows status: "Rejected"
- Status updates are saved and persist
- Timestamps or approval info may be shown

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 15: Internship Detail Access for Company Representative

**Test ID:** BB-TC015  
**Expected Behavior:** Company Representatives can always access full details of internship opportunities they created, regardless of visibility setting  
**Failure Indicators:** Opportunity details become inaccessible when visibility is toggled off for their own opportunities

### Test Steps:

1. Login as company rep: `hr@techcorp.com`
2. View "My Internship Postings"
3. Select an internship
4. View full details
5. Toggle visibility to OFF
6. Return to "My Internship Postings"
7. Select the same internship again
8. View details

✅ **Expected Result:**
- Can still view full details even with visibility OFF
- All fields accessible
- Can edit details
- Can toggle visibility back ON

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 16: Restriction on Editing Approved Opportunities

**Test ID:** BB-TC016  
**Expected Behavior:** Edit functionality is restricted for Company Representatives once internship opportunities are approved by Career Center Staff  
**Failure Indicators:** Company Representatives are able to make changes to opportunity details after approval

### Test Steps:

1. Login as company rep: `hr@techcorp.com`
2. View "My Internship Postings"
3. Select an APPROVED internship
4. Attempt to edit title
5. Attempt to edit requirements
6. Attempt to change number of slots

✅ **Expected Result:**
- Edit functionality DISABLED or RESTRICTED
- Message: "Cannot edit approved internships" or similar
- May only allow visibility toggle
- Critical fields (title, slots, requirements) locked

**Alternative Implementation:**
- Edits require re-approval
- Status reverts to "Pending" after edit

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 18: Student Application Management and Placement Confirmation

**Test ID:** BB-TC018  
**Expected Behavior:** Company Representatives retrieve correct student applications, update slot availability accurately, and correctly confirm placement details  
**Failure Indicators:** Incorrect application retrieval, slot counts not updating properly, or failure to reflect placement confirmation details accurately

### Test Steps:

**Setup:** Multiple students have applied to an internship

1. Login as company rep: `hr@techcorp.com`
2. Select "View Applications for My Internships"
3. Select internship: "Software Engineering Intern" (3 slots)
4. View list of applicants
5. Review first application details
6. Accept first application
7. Verify slot count: Should show 2 remaining
8. Accept second application
9. Verify slot count: Should show 1 remaining
10. Student 1 accepts offer
11. Student 2 accepts offer
12. Check internship status

✅ **Expected Result:**
- Application list shows all applicants
- Slot availability updates correctly: 3→2→1→0
- After 2 students accept: Status changes to "Filled" or slots=0
- Placement details accurately recorded
- Internship no longer visible to new applicants

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 19: Internship Placement Confirmation Status Update

**Test ID:** BB-TC019  
**Expected Behavior:** Placement confirmation status is updated to reflect the actual confirmation condition  
**Failure Indicators:** System fails to update or incorrectly records the placement confirmation status

### Test Steps:

1. Student applies to internship
2. Company rep marks application as "Successful"
3. Student accepts offer (status should become "Accepted")
4. Verify placement confirmation recorded

5. Different student's application is rejected
6. Verify that student's application shows "Unsuccessful"

✅ **Expected Result:**
- Accepted student: Status = "Accepted", placement confirmed
- Rejected student: Status = "Unsuccessful", no placement
- Status updates persist across sessions
- Accurate counts for reporting

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 20: Create, Edit, and Delete Internship Opportunity Listings

**Test ID:** BB-TC020  
**Expected Behavior:** Company Representatives should be able to add new opportunities, modify existing opportunity details (before approval by Career Center Staff), and remove opportunities from the system  
**Failure Indicators:** Inability to create, edit, or delete opportunities or errors during these operations

### Test Steps:

**Step 20.1: Create New Opportunity**
- (Already tested in TC013)

**Step 20.2: Edit Before Approval**
1. Login as company rep
2. Create new internship (status="Pending")
3. Select the pending internship
4. Edit title from "Test Intern" to "Updated Test Intern"
5. Save changes

✅ **Expected Result:**
- Edit successful
- Changes saved
- Still in "Pending" status

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 20.3: Delete Opportunity (No Applications)**
1. Select a pending internship with NO applications
2. Choose "Delete" option
3. Confirm deletion

✅ **Expected Result:**
- Internship removed from system
- No longer appears in list
- Confirmation message shown

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 20.4: Cannot Delete with Applications**
1. Select internship that has applications
2. Attempt to delete

✅ **Expected Result:**
- Deletion BLOCKED
- Error message: "Cannot delete internship with existing applications"

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 21: Career Center Staff Internship Opportunity Approval

**Test ID:** BB-TC021  
**Expected Behavior:** Career Center Staff can review and approve/reject internship opportunities submitted by Company Representatives  
**Failure Indicators:** Career Center Staff cannot access submitted opportunities for review, approval/rejection actions fail to update opportunity status, or approved opportunities do not become visible to students as expected

### Test Steps:

1. Login as staff: `STAFF001`
2. Select "Manage Internship Postings"
3. Select "View Pending Postings"
4. Review first pending internship
   - Check all details (title, description, requirements, etc.)
   - Verify company information
5. Select "Approve"
6. Review second pending internship
7. Select "Reject"
8. Logout

9. Login as student: `U2310001A`
10. Browse internships

✅ **Expected Result:**
- Staff can view all pending internships
- Approve action changes status to "Approved"
- Reject action changes status to "Rejected"
- Approved internship NOW visible to eligible students
- Rejected internship NOT visible to students
- Status updates persist

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 22: Toggle Internship Opportunity Visibility

**Test ID:** BB-TC022  
**Expected Behavior:** Changes in visibility should be reflected accurately in the internship opportunity listing as expected  
**Failure Indicators:** Visibility settings do not update or do not affect the opportunity listing as expected

### Test Steps:

**Covered in Test Case 6 (Steps 6.4 and 6.5)**

Additional verification:
1. Toggle visibility multiple times
2. Verify persistence after logout/login
3. Verify only affects specific internship, not others

✅ **Expected Result:**
- Visibility changes immediately
- Changes persist across sessions
- Only affects toggled internship

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 23: Career Center Staff Internship Opportunity Management

**Test ID:** BB-TC023  
**Expected Behavior:** Withdrawal approvals and rejections are processed correctly, with system updates to reflect the decision and slot availability changes  
**Failure Indicators:** Incorrect or failed processing of withdrawal requests or slot counts not updating properly

### Test Steps:

**Step 23.1: Student Requests Withdrawal**
1. Login as student: `U2310001A` (with accepted placement)
2. Select "View My Applications"
3. Select application with status "Accepted"
4. Choose "Request Withdrawal"
5. Logout

✅ **Expected Result:**
- Withdrawal request submitted
- Application shows "Withdrawal Requested" flag
- Application status remains "Accepted" until approved

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 23.2: Staff Approves Withdrawal**
1. Login as staff: `STAFF001`
2. Select "Manage Withdrawal Requests"
3. View pending withdrawal requests
4. Find student's withdrawal request
5. Review details
6. Select "Approve Withdrawal"

✅ **Expected Result:**
- Withdrawal approved
- Application REMOVED from system
- Internship slot count INCREASES by 1
- If internship was "Filled", status reverts to "Approved"
- Internship becomes visible to students again

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 23.3: Staff Rejects Withdrawal**
1. Different student requests withdrawal
2. Staff reviews request
3. Staff selects "Reject Withdrawal"

✅ **Expected Result:**
- Withdrawal rejected
- Application remains active
- Withdrawal flag cleared
- Status unchanged
- Slot count unchanged

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

## Test Case 24: Generate and Filter Internship Opportunities

**Test ID:** BB-TC024  
**Expected Behavior:** Accurate report generation with options to filter by placement status, major, company, level, and other specified categories  
**Failure Indicators:** Reports are inaccurate, incomplete, or filtering does not work as expected

### Test Steps:

**Step 24.1: Generate Complete Report**
1. Login as staff: `STAFF001`
2. Select "Generate Reports"
3. Select "Internship Statistics Report"
4. Generate report without filters

✅ **Expected Result:**
Report contains:
- Total internships posted
- Total approved internships
- Total applications submitted
- Total successful placements
- Placement rate (percentage)
- Breakdown by company
- Breakdown by major
- Breakdown by level

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 24.2: Filter by Placement Status**
1. Generate report
2. Apply filter: Status = "Accepted"

✅ **Expected Result:**
- Only shows internships with accepted applications
- Count is accurate
- Details match filter

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 24.3: Filter by Major**
1. Apply filter: Major = "Computer Science"

✅ **Expected Result:**
- Only CS internships shown
- Accurate counts

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 24.4: Filter by Company**
1. Apply filter: Company = "Tech Corp"

✅ **Expected Result:**
- Only Tech Corp internships
- Accurate totals

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 24.5: Filter by Level**
1. Apply filter: Level = "Basic"

✅ **Expected Result:**
- Only Basic level internships
- Correct counts

**Actual Result:** ☐ PASS ☐ FAIL

---

**Step 24.6: Multiple Filters Combined**
1. Apply filters: Company="Tech Corp" AND Level="Intermediate"

✅ **Expected Result:**
- Only internships matching ALL filters
- Accurate results

**Actual Result:** ☐ PASS ☐ FAIL  
**Notes:** _______________________________________________

---

**End of Black-Box Test Scripts**

**Summary:**
- Total Test Cases: 24
- Tests Passed: _____
- Tests Failed: _____
- Pass Rate: _____% 

**Tester Signature:** _____________________  
**Date:** _____________________
