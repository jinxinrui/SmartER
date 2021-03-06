package com.example.jxr.smarter.model;

/**
 * Created by jxr on 28/4/18.
 */

public class ElectricityUsage {
    private String usageid;
    private String acusage;
    private String fridgeusage;
    private String washusage;
    private String date;
    private String time;
    private String temperature;
    private Resident resid;

    public ElectricityUsage(String usageid, String acusage, String fridgeusage, String washusage, String date, String time, String temperature, Resident resid) {
        this.usageid = usageid;
        this.acusage = acusage;
        this.fridgeusage = fridgeusage;
        this.washusage = washusage;
        this.temperature = temperature;
        this.date = date;
        this.time = time;
        this.resid = resid;
    }

    public ElectricityUsage() {

    }

    public String getUsageid() {
        return usageid;
    }

    public void setUsageid(String usageid) {
        this.usageid = usageid;
    }

    public String getAcusage() {
        return acusage;
    }

    public void setAcusage(String acusage) {
        this.acusage = acusage;
    }

    public String getFridgeusage() {
        return fridgeusage;
    }

    public void setFridgeusage(String fridgeusage) {
        this.fridgeusage = fridgeusage;
    }

    public String getWashusage() {
        return washusage;
    }

    public void setWashusage(String washusage) {
        this.washusage = washusage;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Resident getResid() {
        return resid;
    }

    public void setResid(Resident resid) {
        this.resid = resid;
    }


    @Override
    public String toString() {
        return "ElectricityUsage{" +
                "usageid='" + usageid + '\'' +
                ", acusage='" + acusage + '\'' +
                ", fridgeusage='" + fridgeusage + '\'' +
                ", washusage='" + washusage + '\'' +
                ", temperature='" + temperature + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' + '}';
    }
}
