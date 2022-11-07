package com.example.demo.entity;

public class devicetaskInfo extends InfoEntity{
    private String taskname;
    private String target;
    private String des;
    private String postdate;
    private String device;
    private String devicetype;
    private String vendor;

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDevice() {
        return device;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVendor() {
        return vendor;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getPostdate() {
        return postdate;
    }
}
