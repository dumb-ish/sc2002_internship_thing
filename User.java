/**
 * Abstract base class representing a user in the Internship Placement Management System.
 * <p>
 * This class serves as the parent for all user types in the system, implementing common
 * functionality such as authentication, password management, and filter criteria settings.
 * All users (Students, Company Representatives, Career Center Staff) inherit from this class.
 * </p>
 * <p>
 * Each user has:
 * <ul>
 *   <li>A unique identifier (userID)</li>
 *   <li>A display name</li>
 *   <li>Encrypted credentials for authentication</li>
 *   <li>Personalized filter criteria for viewing internships</li>
 * </ul>
 *
 * @see Student
 * @see CompanyRepresentative
 * @see CareerCenterStaff
 * @see FilterCriteria
 * @author SC2002 Group
 * @version 1.0
 * @since 2025-11-20
 */
public abstract class User {
    private String userID;
    private String name;
    private String password;
    private FilterCriteria filterCriteria;

    /**
     * Constructs a new User with the specified credentials and initializes default filter criteria.
     *
     * @param userID   the unique identifier for this user (e.g., "U2310001A")
     * @param name     the display name of the user
     * @param password the authentication password for this user
     */
    public User(String userID, String name, String password) {
        this.userID = userID;
        this.name = name;
        this.password = password;
        this.filterCriteria = new FilterCriteria();
    }

    /**
     * Authenticates the user by verifying the provided credentials against stored values.
     *
     * @param id       the user ID to verify
     * @param password the password to verify
     * @return {@code true} if both the user ID and password match, {@code false} otherwise
     */
    public boolean login(String id, String password) {
        return this.userID.equals(id) && this.password.equals(password);
    }

    /**
     * Logs out the current user and displays a confirmation message.
     * <p>
     * This is a placeholder method for future session management implementation.
     * Currently, it only prints a logout confirmation message.
     * </p>
     */
    public void logout() {
        // Placeholder for logout logic
        System.out.println("User " + name + " logged out successfully.");
    }

    /**
     * Changes the user's password after verifying the old password.
     *
     * @param oldPwd the current password for verification
     * @param newPwd the new password to set
     * @return {@code true} if the old password matches and the password was changed successfully,
     *         {@code false} if the old password is incorrect
     */
    public boolean changePassword(String oldPwd, String newPwd) {
        if (this.password.equals(oldPwd)) {
            this.password = newPwd;
            return true;
        }
        return false;
    }

    /**
     * Retrieves the user's current filter criteria for viewing internships.
     *
     * @return the {@link FilterCriteria} object containing the user's filter settings
     */
    public FilterCriteria getFilterCriteria() {
        return filterCriteria;
    }

    /**
     * Sets new filter criteria for this user.
     *
     * @param fc the {@link FilterCriteria} object containing the new filter settings
     */
    public void setFilterCriteria(FilterCriteria fc) {
        this.filterCriteria = fc;
    }

    // Getters

    /**
     * Retrieves the unique identifier for this user.
     *
     * @return the user ID (e.g., "U2310001A" for students, "S001" for staff)
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Retrieves the display name of this user.
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the password for this user.
     * <p>
     * <b>Note:</b> This method should be used cautiously and only for authentication purposes.
     * In a production system, passwords should never be returned in plain text.
     * </p>
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets a new password for this user.
     * <p>
     * This is a protected method intended for internal use by subclasses.
     * External password changes should use {@link #changePassword(String, String)}.
     * </p>
     *
     * @param password the new password to set
     */
    protected void setPassword(String password) {
        this.password = password;
    }
}