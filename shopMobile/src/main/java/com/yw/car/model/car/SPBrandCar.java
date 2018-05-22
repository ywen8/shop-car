package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/24.
 */

public class SPBrandCar implements SPModel, Serializable {
    public String id;
    public String big_ppname;
    public String pin;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "id", "id",
                "big_ppname", "big_ppname",
                "pin", "pin"
        };
    }
}
