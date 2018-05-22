package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/15.
 */

public class SPMaintain implements SPModel, Serializable {
    private String informationName;
    private String address;
    private String phone;
    private String longitude;
    private String latitude;
    private String businessHours;
    private String informationImg;

    public void setInformationName(String informationName) {
        this.informationName = informationName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public void setInformationImg(String informationImg) {
        this.informationImg = informationImg;
    }

    public String getInformationName() {

        return informationName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public String getInformationImg() {
        return informationImg;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{"informationName", "information_name", "address", "address", "phone", "phone", "longitude", "longitude", "latitude", "latitude", "businessHours"
                , "business_hours", "informationImg", "information_img"};
    }
}
