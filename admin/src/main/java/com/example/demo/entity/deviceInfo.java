package com.example.demo.entity;

public class deviceInfo extends InfoEntity{
    private String device;
    private String devicetype;
    private String vendor;

    public void setDevice(String device) {
        this.device = device;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getDevice() {
        return device;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public String getVendor() {
        return vendor;
    }
}
