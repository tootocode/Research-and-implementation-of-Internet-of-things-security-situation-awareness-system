package database.entity;

public class devicetaskInfo extends InfoEntity{

    private String target;
    private String country;
    private String province;
    private String city;

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public String getCountry(){return country;}

    public void setCountry(String country){this.country=country;}

    public String getProvince(){return province;}

    public void setProvince(String province){this.province=province;}

    public String getCity(){return city;}

    public void setCity(String city){this.city=city;}
}
