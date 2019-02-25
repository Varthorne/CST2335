package com.example.androidlabs;

public class Message {

    String message;
    boolean wasSent;

    public Message(){

        this(new String(), false);
    }

    public Message(String message, boolean wasSent){

        this.message = message;
        this.wasSent = wasSent;
    }

    public String getMessage(){
        return message;
    }

    public boolean wasSent(){
        return wasSent;
    }
}
