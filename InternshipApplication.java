import java.util.Objects;

class InternshipApplication {
    private String internshipId;
    private String studentId;
    private String status;
    private String companyApprovalStatus;
    
    public InternshipApplication(String internshipId, String studentId, String status, String companyApprovalStatus) {
        this.internshipId = internshipId;
        this.studentId = studentId;
        this.status = status;
        this.companyApprovalStatus = companyApprovalStatus;
    }
    
    public InternshipApplication(String internshipId, String studentId, String status) {
        this(internshipId, studentId, status, "Pending");
    }
    
    public String getInternshipId() { return internshipId; }
    public String getStudentId() { return studentId; }
    public String getStatus() { return status; }
    public String getCompanyApprovalStatus() { return companyApprovalStatus; }
    public void setStatus(String status) { this.status = status; }
    public void setCompanyApprovalStatus(String companyApprovalStatus) { this.companyApprovalStatus = companyApprovalStatus; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        InternshipApplication that = (InternshipApplication) obj;
        return Objects.equals(internshipId, that.internshipId) && 
               Objects.equals(studentId, that.studentId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(internshipId, studentId);
    }
}