package com.example.androidlabs;

public class Message {

    String message;
    boolean wasSent;
    long dbID;

    public Message(){

        this(new String(), false, 0);
    }

    public Message(String message, boolean wasSent, long dbID){

        this.message = message;
        this.wasSent = wasSent;
        this.dbID = dbID;
    }

    public String getMessage(){
        return message;
    }

    public boolean wasSent(){
        return wasSent;
    }

    public long getDbID(){

        return dbID;
    }
}
