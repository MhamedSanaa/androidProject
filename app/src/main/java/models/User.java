package models;

public class User {
    public String id;
    public String username;
    public String email;
    public String fullName;
    public String password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String fullName, String username,  String email,String password ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
    }
}
