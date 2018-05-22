package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/24.
 */

public class SPResult implements SPModel,Serializable {
    public String price;
    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "price","est_price_result"
        };
    }
}
