package com.example.demo.entity;

public class protocoltaskInfo extends InfoEntity{
    private String taskname;
    private String target;
    private String des;
    private String postdate;
    private String protocol;
    private String raw;

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

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getRaw() {
        return raw;
    }
}
