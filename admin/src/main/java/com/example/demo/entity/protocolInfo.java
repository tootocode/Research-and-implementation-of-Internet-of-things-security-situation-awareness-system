package com.example.demo.entity;

import lombok.Data;

@Data
public class protocolInfo extends InfoEntity{
    private String protocol;
    private String desc;
    public String getProtocol(){
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
