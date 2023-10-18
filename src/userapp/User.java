package userapp;

public class User {
    private int userId;
    private String username;
    private String firstName;
    private String lastName;
    private boolean vipUser;

    /**
     * Constructor to create a new User object with specified properties.
     *
     * @param userId     The unique identifier for the user.
     * @param username   The username of the user.
     * @param firstName  The first name of the user.
     * @param lastName   The last name of the user.
     * @param vipUser    The VIP status of the user.
     */
    public User(int userId, String username, String firstName, String lastName, boolean vipUser) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.vipUser = vipUser;
    }

    /**
     * Get the user's unique identifier (user ID).
     *
     * @return The user's unique identifier.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Get the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the first name of the user.
     *
     * @return The first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the last name of the user.
     *
     * @return The last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Check if the user is a VIP user.
     *
     * @return `true` if the user is a VIP user; otherwise, `false`.
     */
    public boolean isVip() {
        return vipUser;
    }

    /**
     * Set the VIP status of the user.
     *
     * @param vipUser `true` to make the user a VIP user, `false` to remove VIP status.
     */
    public void setVip(boolean vipUser) {
        this.vipUser = vipUser;
    }
}
