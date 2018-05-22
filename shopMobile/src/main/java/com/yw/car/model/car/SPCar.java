package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/24.
 */

public class SPCar implements SPModel, Serializable {
    public String id;
    public String cxname;
    public String pyear;
    public String price;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "id", "id",
                "cxname", "cxname",
                "pyear", "pyear",
                "price", "price"
        };
    }
}
