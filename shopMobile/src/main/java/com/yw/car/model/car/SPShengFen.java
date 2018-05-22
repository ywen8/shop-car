package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/3/10.
 */

public class SPShengFen implements SPModel, Serializable {
    private String province;
    private String province_code;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "province", "province",
                "province_code", "province_code"
        };
    }

    public String getProvince() {
        return province;
    }

    public String getProvince_code() {
        return province_code;
    }
}
