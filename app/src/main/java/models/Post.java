package models;



public class Post {
    public String UserId;
    public String user;
    public String subject;
    public String date;
    public String time;
    public String address;

    public double latitude;
    public double longitude;
    public int numberOfParticipants;


    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Post(String UserId, String subject,String address, String date, String time,  double latitude,double longitude,String user) {
        this.UserId = UserId;
        this.subject = subject;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.date=date;
        this.time=time;
        this.user = user;
        this.numberOfParticipants=0;
    }

    public String toString(){
        return "{\n"+this.UserId+",\n"+
                this.subject+",\n"+
                this.address+",\n"+
                this.date+",\n"+
                this.time+",\n"+
                this.numberOfParticipants+",\n"+
                this.user+",\n"+
                "}";
    }
}
