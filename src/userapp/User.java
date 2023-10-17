package userapp;

public class User {
    private int userId;
    private String username;
    private String firstName;
    private String lastName;
    private boolean vipUser;

    public User(int userId, String username, String firstName, String lastName, boolean vipUser) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.vipUser = vipUser;
    }

    // Getters for the user properties
    public int getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    
    public boolean isVip() {
        return vipUser;
    }
    
    public void setVip(boolean vipUser) {
        this.vipUser = vipUser;
    }
    
}
