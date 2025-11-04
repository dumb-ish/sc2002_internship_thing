/**
 * Abstract base class representing a User in the Internship Placement System.
 * All users (Students, Company Representatives, Career Center Staff) inherit from this class.
 */
public abstract class User {
    private String userID;
    private String name;
    private String password;
    private FilterCriteria filterCriteria;

    /**
     * Constructor for User
     */
    public User(String userID, String name, String password) {
        this.userID = userID;
        this.name = name;
        this.password = password;
        this.filterCriteria = new FilterCriteria();
    }

    /**
     * Authenticate user login with ID and password
     */
    public boolean login(String id, String password) {
        return this.userID.equals(id) && this.password.equals(password);
    }

    /**
     * Logout user - placeholder for future session management
     */
    public void logout() {
        // Placeholder for logout logic
        System.out.println("User " + name + " logged out successfully.");
    }

    /**
     * Change user password
     */
    public boolean changePassword(String oldPwd, String newPwd) {
        if (this.password.equals(oldPwd)) {
            this.password = newPwd;
            return true;
        }
        return false;
    }

    /**
     * Get the user's filter criteria
     */
    public FilterCriteria getFilterCriteria() {
        return filterCriteria;
    }

    /**
     * Set the user's filter criteria
     */
    public void setFilterCriteria(FilterCriteria fc) {
        this.filterCriteria = fc;
    }

    // Getters
    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    // Setter for password (for internal use)
    protected void setPassword(String password) {
        this.password = password;
    }
}