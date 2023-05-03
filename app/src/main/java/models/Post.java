package models;

import org.checkerframework.common.returnsreceiver.qual.This;

public class Post {
    public String UserId;
    public String subject;
    public String address;
    public String locationName;
    public String date;
    public String time;
    public int numberOfParticipants;

    public String user;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Post(String UserId, String subject,String locationName, String date, String time,  String address,int numberOfParticipants,String user) {
        this.UserId = UserId;
        this.subject = subject;
        this.address = address;
        this.locationName=locationName;
        this.date=date;
        this.time=time;
        this.user = user;
        this.numberOfParticipants=numberOfParticipants;
    }

    public String toString(){
        return "{\n"+this.UserId+",\n"+
                this.subject+",\n"+
                this.address+",\n"+
                this.locationName+",\n"+
                this.date+",\n"+
                this.time+",\n"+
                this.numberOfParticipants+",\n"+
                this.user+",\n"+
                "}";
    }
}
