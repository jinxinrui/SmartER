package com.example.jxr.smarter.model;

import java.sql.Date;

/**
 * Created by jxr on 26/4/18.
 */

// the attribute name should be exactly same as those in the backend

public class Resident {

    private Integer resid;
    private String firstname;
    private String surname;
    private String dob;
    private String address;
    private String postcode;
    private String email;
    private String mobile;
    private String numofres;
    private String provider;

    public Resident() {
    }

    public Resident(Integer resid, String fName, String sName, String dob, String address, String postcode, String email, String mobile, String numofRes, String provider) {
        this.resid = resid;
        this.firstname = fName;
        this.surname = sName;
        this.dob = dob + "T00:00:00+10:00";
        this.address = address;
        this.postcode = postcode;
        this.email = email;
        this.mobile = mobile;
        this.numofres = numofRes;
        this.provider = provider;
    }

    public Integer getResid() {
        return resid;
    }

    public void setResid(Integer resid) {
        this.resid = resid;
    }

    public String getfName() {
        return firstname;
    }

    public void setfName(String fName) {
        this.firstname = fName;
    }

    public String getsName() {
        return surname;
    }

    public void setsName(String sName) {
        this.surname = sName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob + "T00:00:00+10:00";
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNumofRes() {
        return numofres;
    }

    public void setNumofRes(String numofRes) {
        this.numofres = numofRes;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

}
