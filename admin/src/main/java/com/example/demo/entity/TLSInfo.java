package com.example.demo.entity;

public class TLSInfo extends InfoEntity{
    private String tlsname;
    private String desc;

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getTlsname() {
        return tlsname;
    }

    public void setTlsname(String tlsname) {
        this.tlsname = tlsname;
    }
}
