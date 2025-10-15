import java.io.*;
import java.util.*;
import java.time.LocalDate;

class InternshipSystem {
    private Map<String, User> users;
    private Map<String, InternshipOpportunity> opportunities;
    private Map<String, InternshipApplication> applications;
    private User currentUser;
    private Scanner scanner;
    private int nextOpportunityId;
    
    public InternshipSystem() {
        users = new HashMap<>();
        opportunities = new HashMap<>();
        applications = new HashMap<>();
        currentUser = null;
        scanner = new Scanner(System.in);
        nextOpportunityId = 1;
    }
    
    public Scanner getScanner() {
        return scanner;
    }
    
    public void initializeSystem() {
        loadUsersFromFile("sample_staff_list.csv", "staff");
        loadUsersFromFile("sample_student_list.csv", "student");
        loadUsersFromFile("sample_company_representative_list.csv", "company_rep");
        loadInternshipsFromFile("internships.csv");
        loadInternshipApplicationsFromFile("internship_status.csv");
    }
    
    private void loadUsersFromFile(String filename, String type) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (type.equals("staff")) {
                    if (parts.length >= 5) {
                        String userId = parts[0].trim();
                        String name = parts[1].trim();
                        String role = parts[2].trim();
                        String department = parts[3].trim();
                        String email = parts[4].trim();
                        
                        User staff = new CareerCenterStaff(userId, name, "password", department);
                        users.put(userId, staff);
                    }
                } else if (type.equals("student")) {
                    if (parts.length >= 5) {
                        String userId = parts[0].trim();
                        String name = parts[1].trim();
                        String major = parts[2].trim();
                        int year = Integer.parseInt(parts[3].trim());
                        String email = parts[4].trim();
                        
                        User student = new Student(userId, name, "password", year, major);
                        users.put(userId, student);
                    }
                } else if (type.equals("company_rep")) {
                    if (parts.length >= 7) {
                        String userId = parts[0].trim();
                        String name = parts[1].trim();
                        String companyName = parts[2].trim();
                        String department = parts[3].trim();
                        String position = parts[4].trim();
                        String email = parts[5].trim();
                        String status = parts[6].trim();
                        
                        User rep = new CompanyRepresentative(userId, name, "password", companyName, department, position, email);
                        ((CompanyRepresentative)rep).setStatus(status);
                        users.put(userId, rep);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + filename);
        } catch (Exception e) {
            System.out.println("Error processing file: " + e.getMessage());
        }
    }
    
    private void loadInternshipsFromFile(String filename) {
        opportunities.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 12) {
                    String id = parts[0].trim();
                    String title = parts[1].trim();
                    String description = parts[2].trim();
                    String level = parts[3].trim();
                    String preferredMajor = parts[4].trim();
                    LocalDate openingDate = LocalDate.parse(parts[5].trim());
                    LocalDate closingDate = LocalDate.parse(parts[6].trim());
                    String status = parts[7].trim();
                    String companyName = parts[8].trim();
                    String companyRepId = parts[9].trim();
                    int slots = Integer.parseInt(parts[10].trim());
                    boolean visible = Boolean.parseBoolean(parts[11].trim());
                    
                    InternshipOpportunity opportunity = new InternshipOpportunity(
                        id, title, description, level, preferredMajor, openingDate, closingDate,
                        status, companyName, companyRepId, slots, visible
                    );
                    
                    opportunities.put(id, opportunity);
                    
                    String numericPart = id.substring(2);
                    try {
                        int idNum = Integer.parseInt(numericPart);
                        if (idNum >= nextOpportunityId) {
                            nextOpportunityId = idNum + 1;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Internships file not found or error loading: " + filename + ". Creating new file.");
            try (PrintWriter pw = new PrintWriter(new FileWriter(filename, false))) {
                pw.println("ID,Title,Description,Level,PreferredMajor,OpeningDate,ClosingDate,Status,CompanyName,CompanyRepId,Slots,Visible");
            } catch (IOException ex) {
                System.out.println("Error creating internships file: " + ex.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error processing internships file: " + e.getMessage());
        }
    }
    
    private void loadInternshipApplicationsFromFile(String filename) {
        applications.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String internshipId = parts[0].trim();
                    String studentId = parts[1].trim();
                    String status = parts[2].trim();
                    String companyApprovalStatus = parts[3].trim();
                    
                    InternshipApplication application = new InternshipApplication(
                        internshipId, studentId, status, companyApprovalStatus
                    );
                    
                    applications.put(internshipId + "_" + studentId, application);
                    
                    InternshipOpportunity opportunity = opportunities.get(internshipId);
                    if (opportunity != null) {
                        opportunity.addApplication(application);
                    }
                    
                    User user = users.get(studentId);
                    if (user instanceof Student) {
                        Student student = (Student) user;
                        if (!student.getApplications().contains(application)) {
                            student.getApplications().add(application);
                        }
                        if (status.equals("Accepted")) {
                            student.setAcceptedInternshipId(internshipId);
                        }
                    }
                } else if (parts.length >= 3) {
                    String internshipId = parts[0].trim();
                    String studentId = parts[1].trim();
                    String status = parts[2].trim();
                    
                    InternshipApplication application = new InternshipApplication(
                        internshipId, studentId, status, "Pending"
                    );
                    
                    applications.put(internshipId + "_" + studentId, application);
                    
                    InternshipOpportunity opportunity = opportunities.get(internshipId);
                    if (opportunity != null) {
                        opportunity.addApplication(application);
                    }
                    
                    User user = users.get(studentId);
                    if (user instanceof Student) {
                        Student student = (Student) user;
                        if (!student.getApplications().contains(application)) {
                            student.getApplications().add(application);
                        }
                        if (status.equals("Accepted")) {
                            student.setAcceptedInternshipId(internshipId);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Internship status file not found or error loading: " + filename + ". Creating new file.");
            try (PrintWriter pw = new PrintWriter(new FileWriter(filename, false))) {
                pw.println("InternshipID,StudentID,Status,CompanyApproval");
            } catch (IOException ex) {
                System.out.println("Error creating internship status file: " + ex.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error processing internship status file: " + e.getMessage());
        }
    }
    
    public void reloadInternshipsFromFile() {
        loadInternshipsFromFile("internships.csv");
    }
    
    public void reloadInternshipApplicationsFromFile() {
        loadInternshipApplicationsFromFile("internship_status.csv");
    }
    
    public void reloadInternshipApplicationsForUser(String userId) {
        if (users.get(userId) instanceof Student) {
            Student student = (Student) users.get(userId);
            student.getApplications().clear();
            
            for (InternshipApplication app : applications.values()) {
                if (app.getStudentId().equals(userId)) {
                    student.getApplications().add(app);
                }
            }
        }
    }
    
    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }
    
    public User getUserById(String userId) {
        return users.get(userId);
    }
    
    public boolean authenticateUser(String userId, String password) {
        User user = users.get(userId);
        return user != null && user.getPassword().equals(password);
    }
    
    public void setCurrentUser(String userId) {
        this.currentUser = users.get(userId);
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public void addInternshipOpportunity(InternshipOpportunity opportunity) {
        opportunities.put(opportunity.getId(), opportunity);
    }
    
    public InternshipOpportunity getInternshipOpportunityById(String id) {
        return opportunities.get(id);
    }
    
    public List<InternshipOpportunity> getVisibleOpportunities(Student student) {
        LocalDate today = LocalDate.now();
        return opportunities.values().stream()
            .filter(opp -> opp.isVisible() && 
                         opp.getStatus().equals("Approved") &&
                         (opp.getOpeningDate().isBefore(today) || opp.getOpeningDate().isEqual(today)) &&
                         (opp.getClosingDate().isAfter(today) || opp.getClosingDate().isEqual(today)) &&
                         (opp.getPreferredMajor().equals("All") || 
                          opp.getPreferredMajor().equals(student.getMajor())) &&
                         (student.getYearOfStudy() > 2 || opp.getLevel().equals("Basic")))
            .sorted((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()))
            .toList();
    }
    
    public List<User> getPendingCompanyRepresentatives() {
        return users.values().stream()
            .filter(user -> user instanceof CompanyRepresentative)
            .filter(user -> ((CompanyRepresentative) user).getStatus().equals("Pending"))
            .toList();
    }
    
    public List<InternshipOpportunity> getPendingInternshipOpportunities() {
        return opportunities.values().stream()
            .filter(opp -> opp.getStatus().equals("Pending"))
            .toList();
    }
    
    public List<InternshipApplication> getWithdrawalRequests() {
        return applications.values().stream()
            .filter(app -> app.getStatus().equals("Withdrawal Requested"))
            .toList();
    }
    
    public List<InternshipOpportunity> getAllInternshipOpportunities() {
        return new ArrayList<>(opportunities.values());
    }
    
    public List<InternshipOpportunity> getOpportunitiesByCompanyRepId(String companyRepId) {
        return opportunities.values().stream()
            .filter(opp -> opp.getCompanyRepId().equals(companyRepId))
            .sorted((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()))
            .toList();
    }
    
    public List<InternshipOpportunity> getInternshipOpportunitiesByStatus(String status) {
        return opportunities.values().stream()
            .filter(opp -> opp.getStatus().equalsIgnoreCase(status))
            .sorted((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()))
            .toList();
    }
    
    public List<InternshipOpportunity> getInternshipOpportunitiesByMajor(String major) {
        return opportunities.values().stream()
            .filter(opp -> opp.getPreferredMajor().equalsIgnoreCase(major))
            .sorted((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()))
            .toList();
    }
    
    public List<InternshipOpportunity> getInternshipOpportunitiesByLevel(String level) {
        return opportunities.values().stream()
            .filter(opp -> opp.getLevel().equalsIgnoreCase(level))
            .sorted((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()))
            .toList();
    }
    
    public int getNextOpportunityId() {
        return nextOpportunityId++;
    }
    
    public void run() {
        initializeSystem();
        
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                currentUser.displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();
                currentUser.performAction(choice, this);
            }
        }
    }
    
    private void showLoginMenu() {
        System.out.println("\n=== Internship Placement Management System ===");
        System.out.println("1. Login");
        System.out.println("2. Register as Company Representative");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                performLogin();
                break;
            case 2:
                registerCompanyRepresentative();
                break;
            case 3:
                System.out.println("Thank you for using the system!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option!");
        }
    }
    
    private void performLogin() {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        if (authenticateUser(userId, password)) {
            setCurrentUser(userId);
            System.out.println("Login successful! Welcome, " + currentUser.getName());
        } else {
            System.out.println("Invalid credentials!");
        }
    }
    
    private void registerCompanyRepresentative() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Company Email (as User ID): ");
        String email = scanner.nextLine();
        
        if (users.containsKey(email)) {
            System.out.println("A user with this email already exists. Registration failed.");
            return;
        }
        
        System.out.print("Enter Company Name: ");
        String companyName = scanner.nextLine();
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        System.out.print("Enter Position: ");
        String position = scanner.nextLine();
        
        User rep = new CompanyRepresentative(email, name, "password", companyName, department, position, email);
        addUser(rep);
        
        saveCompanyRepresentativeToFile((CompanyRepresentative) rep);
        
        System.out.println("Registration successful! Awaiting approval from Career Center Staff.");
    }
    
    private void saveCompanyRepresentativeToFile(CompanyRepresentative rep) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("sample_company_representative_list.csv", true))) {
            File file = new File("sample_company_representative_list.csv");
            if (file.length() == 0) {
                pw.println("CompanyRepID,Name,CompanyName,Department,Position,Email,Status");
            }
            
            pw.println(rep.getUserId() + "," + 
                      rep.getName() + "," + 
                      rep.getCompanyName() + "," + 
                      rep.getDepartment() + "," + 
                      rep.getPosition() + "," + 
                      rep.getEmail() + "," + 
                      rep.getStatus());
        } catch (IOException e) {
            System.out.println("Error saving company representative to file: " + e.getMessage());
        }
    }
    
    public void updateCompanyRepresentativeInFile(CompanyRepresentative rep) {
        List<CompanyRepresentative> allReps = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("sample_company_representative_list.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String userId = parts[0].trim();
                    String name = parts[1].trim();
                    String companyName = parts[2].trim();
                    String department = parts[3].trim();
                    String position = parts[4].trim();
                    String email = parts[5].trim();
                    String status = parts[6].trim();
                    
                    CompanyRepresentative existingRep = new CompanyRepresentative(userId, name, "password", 
                        companyName, department, position, email);
                    existingRep.setStatus(status);
                    allReps.add(existingRep);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }
        
        for (int i = 0; i < allReps.size(); i++) {
            if (allReps.get(i).getUserId().equals(rep.getUserId())) {
                allReps.set(i, rep);
                break;
            }
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter("sample_company_representative_list.csv", false))) {
            pw.println("CompanyRepID,Name,CompanyName,Department,Position,Email,Status");
            for (CompanyRepresentative r : allReps) {
                pw.println(r.getUserId() + "," + 
                          r.getName() + "," + 
                          r.getCompanyName() + "," + 
                          r.getDepartment() + "," + 
                          r.getPosition() + "," + 
                          r.getEmail() + "," + 
                          r.getStatus());
            }
        } catch (IOException e) {
            System.out.println("Error updating file: " + e.getMessage());
        }
    }
    
    public void saveInternshipOpportunityToFile(InternshipOpportunity opportunity) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("internships.csv", true))) {
            File file = new File("internships.csv");
            if (file.length() == 0) {
                pw.println("ID,Title,Description,Level,PreferredMajor,OpeningDate,ClosingDate,Status,CompanyName,CompanyRepId,Slots,Visible");
            }
            
            pw.println(opportunity.getId() + "," + 
                      opportunity.getTitle() + "," + 
                      opportunity.getDescription() + "," + 
                      opportunity.getLevel() + "," + 
                      opportunity.getPreferredMajor() + "," + 
                      opportunity.getOpeningDate() + "," + 
                      opportunity.getClosingDate() + "," + 
                      opportunity.getStatus() + "," + 
                      opportunity.getCompanyName() + "," + 
                      opportunity.getCompanyRepId() + "," + 
                      opportunity.getSlots() + "," + 
                      opportunity.isVisible());
        } catch (IOException e) {
            System.out.println("Error saving internship opportunity to file: " + e.getMessage());
        }
    }
    
    public void updateInternshipOpportunityInFile(InternshipOpportunity opportunity) {
        List<InternshipOpportunity> allOpps = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("internships.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 12) {
                    String id = parts[0].trim();
                    String title = parts[1].trim();
                    String description = parts[2].trim();
                    String level = parts[3].trim();
                    String preferredMajor = parts[4].trim();
                    LocalDate openingDate = LocalDate.parse(parts[5].trim());
                    LocalDate closingDate = LocalDate.parse(parts[6].trim());
                    String status = parts[7].trim();
                    String companyName = parts[8].trim();
                    String companyRepId = parts[9].trim();
                    int slots = Integer.parseInt(parts[10].trim());
                    boolean visible = Boolean.parseBoolean(parts[11].trim());
                    
                    InternshipOpportunity existingOpp = new InternshipOpportunity(
                        id, title, description, level, preferredMajor, openingDate, closingDate,
                        status, companyName, companyRepId, slots, visible
                    );
                    
                    allOpps.add(existingOpp);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading internships file: " + e.getMessage());
            return;
        }
        
        for (int i = 0; i < allOpps.size(); i++) {
            if (allOpps.get(i).getId().equals(opportunity.getId())) {
                allOpps.set(i, opportunity);
                break;
            }
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter("internships.csv", false))) {
            pw.println("ID,Title,Description,Level,PreferredMajor,OpeningDate,ClosingDate,Status,CompanyName,CompanyRepId,Slots,Visible");
            for (InternshipOpportunity opp : allOpps) {
                pw.println(opp.getId() + "," + 
                          opp.getTitle() + "," + 
                          opp.getDescription() + "," + 
                          opp.getLevel() + "," + 
                          opp.getPreferredMajor() + "," + 
                          opp.getOpeningDate() + "," + 
                          opp.getClosingDate() + "," + 
                          opp.getStatus() + "," + 
                          opp.getCompanyName() + "," + 
                          opp.getCompanyRepId() + "," + 
                          opp.getSlots() + "," + 
                          opp.isVisible());
            }
        } catch (IOException e) {
            System.out.println("Error updating internships file: " + e.getMessage());
        }
    }
    
    public void saveInternshipApplicationToFile(InternshipApplication application) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("internship_status.csv", true))) {
            File file = new File("internship_status.csv");
            if (file.length() == 0) {
                pw.println("InternshipID,StudentID,Status,CompanyApproval");
            }
            
            pw.println(application.getInternshipId() + "," + 
                      application.getStudentId() + "," + 
                      application.getStatus() + "," + 
                      application.getCompanyApprovalStatus());
        } catch (IOException e) {
            System.out.println("Error saving internship application to file: " + e.getMessage());
        }
    }
    
    public void updateInternshipApplicationInFile(InternshipApplication application) {
        List<InternshipApplication> allApps = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("internship_status.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String internshipId = parts[0].trim();
                    String studentId = parts[1].trim();
                    String status = parts[2].trim();
                    String companyApprovalStatus = parts[3].trim();
                    
                    InternshipApplication existingApp = new InternshipApplication(
                        internshipId, studentId, status, companyApprovalStatus
                    );
                    
                    allApps.add(existingApp);
                } else if (parts.length >= 3) {
                    String internshipId = parts[0].trim();
                    String studentId = parts[1].trim();
                    String status = parts[2].trim();
                    
                    InternshipApplication existingApp = new InternshipApplication(
                        internshipId, studentId, status, "Pending"
                    );
                    
                    allApps.add(existingApp);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading internship status file: " + e.getMessage());
            return;
        }
        
        for (int i = 0; i < allApps.size(); i++) {
            if (allApps.get(i).getInternshipId().equals(application.getInternshipId()) &&
                allApps.get(i).getStudentId().equals(application.getStudentId())) {
                allApps.set(i, application);
                break;
            }
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter("internship_status.csv", false))) {
            pw.println("InternshipID,StudentID,Status,CompanyApproval");
            for (InternshipApplication app : allApps) {
                pw.println(app.getInternshipId() + "," + 
                          app.getStudentId() + "," + 
                          app.getStatus() + "," + 
                          app.getCompanyApprovalStatus());
            }
        } catch (IOException e) {
            System.out.println("Error updating internship status file: " + e.getMessage());
        }
    }
    
    public void removeInternshipApplicationFromFile(InternshipApplication application) {
        List<InternshipApplication> allApps = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("internship_status.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String internshipId = parts[0].trim();
                    String studentId = parts[1].trim();
                    String status = parts[2].trim();
                    String companyApprovalStatus = parts[3].trim();
                    
                    if (!(internshipId.equals(application.getInternshipId()) && 
                          studentId.equals(application.getStudentId()))) {
                        InternshipApplication existingApp = new InternshipApplication(
                            internshipId, studentId, status, companyApprovalStatus
                        );
                        allApps.add(existingApp);
                    }
                } else if (parts.length >= 3) {
                    String internshipId = parts[0].trim();
                    String studentId = parts[1].trim();
                    String status = parts[2].trim();
              
                    if (!(internshipId.equals(application.getInternshipId()) && 
                          studentId.equals(application.getStudentId()))) {
                        InternshipApplication existingApp = new InternshipApplication(
                            internshipId, studentId, status, "Pending"
                        );
                        allApps.add(existingApp);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading internship status file: " + e.getMessage());
            return;
        }
        
        try (PrintWriter pw = new PrintWriter(new FileWriter("internship_status.csv", false))) {
            pw.println("InternshipID,StudentID,Status,CompanyApproval");
            for (InternshipApplication app : allApps) {
                pw.println(app.getInternshipId() + "," + 
                          app.getStudentId() + "," + 
                          app.getStatus() + "," + 
                          app.getCompanyApprovalStatus());
            }
        } catch (IOException e) {
            System.out.println("Error updating internship status file: " + e.getMessage());
        }
        
        String key = application.getInternshipId() + "_" + application.getStudentId();
        applications.remove(key);
        
        User user = users.get(application.getStudentId());
        if (user instanceof Student) {
            Student student = (Student) user;
            student.getApplications().removeIf(app -> 
                app.getInternshipId().equals(application.getInternshipId()) &&
                app.getStudentId().equals(application.getStudentId()));
        }
        
        InternshipOpportunity opportunity = opportunities.get(application.getInternshipId());
        if (opportunity != null) {
            opportunity.getApplications().removeIf(app -> 
                app.getInternshipId().equals(application.getInternshipId()) &&
                app.getStudentId().equals(application.getStudentId()));
        }
    }
    
    public boolean hasStudentAcceptedInternship(String studentId) {
        try (BufferedReader br = new BufferedReader(new FileReader("internship_status.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String csvStudentId = parts[1].trim();
                    String status = parts[2].trim();
                    
                    if (csvStudentId.equals(studentId) && status.equals("Accepted")) {
                        return true;
                    }
                } else if (parts.length >= 3) {
                    String csvStudentId = parts[1].trim();
                    String status = parts[2].trim();
                    
                    if (csvStudentId.equals(studentId) && status.equals("Accepted")) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading internship status file: " + e.getMessage());
        }
        return false;
    }
    
    public int getAcceptedStudentsCount(String internshipId) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("internship_status.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String csvInternshipId = parts[0].trim();
                    String status = parts[2].trim();
                    
                    if (csvInternshipId.equals(internshipId) && status.equals("Accepted")) {
                        count++;
                    }
                } else if (parts.length >= 3) {
                    String csvInternshipId = parts[0].trim();
                    String status = parts[2].trim();
                    
                    if (csvInternshipId.equals(internshipId) && status.equals("Accepted")) {
                        count++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading internship status file: " + e.getMessage());
        }
        return count;
    }
}