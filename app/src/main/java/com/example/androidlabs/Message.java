package com.example.androidlabs;

public class Message {

    String message;
    boolean wasSent;
    long id;

    public Message(){

        this(new String(), false, -1);
    }

    public Message(String message, boolean wasSent){

        this(message, wasSent, -1);
    }

    public Message(String message, boolean wasSent, long id){

        this.message = message;
        this.wasSent = wasSent;
        this.id = id;
    }

    public String getMessage(){
        return message;
    }

    public boolean wasSent(){
        return wasSent;
    }

    public long getId(){

        return id;
    }

    public void setId(long id){
        this.id = id;
    }

}
