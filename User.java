import java.util.*;

abstract class User {
    protected String userId;
    protected String name;
    protected String password;
    protected String role;
    
    public User(String userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }
    
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }

    public abstract void displayMenu();
    
    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }
    
    public abstract void performAction(int choice, InternshipSystem system);
}