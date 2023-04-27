package models;

public class Post {
    public String UserId;
    public String subject;
    public String address;
    public String user;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Post(String UserId, String subject,  String address,String user ) {
        this.UserId = UserId;
        this.subject = subject;
        this.address = address;
        this.user = user;
    }
}
