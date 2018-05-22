package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/27.
 */

public class SPAreaPrice implements SPModel, Serializable {
    public String price;
    public String area;
    public String areaid;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "price", "price",
                "area", "area",
                "areaid", "areaid"
        };
    }
}
