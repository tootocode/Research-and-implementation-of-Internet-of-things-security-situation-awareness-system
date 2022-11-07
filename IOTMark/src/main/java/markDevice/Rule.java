package markDevice;

public class Rule {
    private String Vendor;
    private String Devicetype;
    private int num;
    public String Device="";
    public Rule(String Vendor,String Devicetype,int num){
        this.Vendor=Vendor;
        this.Devicetype=Devicetype;
        this.num=num;
    }

    public void setVendor(String Vendor){
        this.Vendor=Vendor;
    }

    public void setDevicetype(String Devicetype){
        this.Devicetype=Devicetype;
    }

    public String getDevicetype(){return this.Devicetype;}

    public String getVendor(){return this.Vendor;}

    public void addNum(){
        this.num++;
    }

    public int getNum(){return this.num;}

    public boolean equals(Rule rule){
        if(this.Vendor.equals(rule.Vendor)&&this.Devicetype.equals(rule.Devicetype))
            return true;
        else
            return false;
    }
}
