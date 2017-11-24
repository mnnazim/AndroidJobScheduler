package com.example.androidjobscheduler;

/**
 * Created by USER on 11/19/2017.
 */

public class StatusGetSet {
    private int ID;
    private String DateTime;
    private String Status;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
