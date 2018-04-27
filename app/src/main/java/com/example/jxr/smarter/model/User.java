package com.example.jxr.smarter.model;

/**
 * Created by jxr on 25/4/18.
 */

// the attribute name should be exactly same as those in the backend

public class User {
    private String username;
    private String passwordhash;
    private String regisdate;
    private Resident resid;


    public User(String username, String password, String regisdate, Resident resid) {
        this.username = username;
        this.passwordhash = password;
        this.regisdate = regisdate;
        this.resid = resid;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return passwordhash;
    }

    public void setPassword(String password) {
        this.passwordhash = password;
    }

    public String getRegisdate() {
        return regisdate;
    }

    public void setRegisdate(String regisdate) {
        this.regisdate = regisdate;
    }

    public Resident getResid() {
        return resid;
    }

    public void setResid(Resident resid) {
        this.resid = resid;
    }
}
