package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/9.
 */

public class SPCarBrand implements SPModel, Serializable {
    private int brandID;
    private String brandName;

    public void setBrandID(int brandID) {
        this.brandID = brandID;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getBrandID() {

        return brandID;
    }

    public String getBrandName() {
        return brandName;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "brandID", "brand_id", "brandName", "brand_name"
        };
    }
}
