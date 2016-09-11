package com.example.pavan.cardoctor;

/**
 * Created by Pavan on 9/11/2016.
 */
public class AddAddressList {

    private String id;
    private String country;
    private String state;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSubregion() {
        return subregion;
    }

    public void setSubregion(String subregion) {
        this.subregion = subregion;
    }

    public String getSteetaddress() {
        return steetaddress;
    }

    public void setSteetaddress(String steetaddress) {
        this.steetaddress = steetaddress;
    }

    private String city;
    private String subregion;
    private String steetaddress;
}
