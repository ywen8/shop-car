package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/15.
 */

public class SPCity implements SPModel, Serializable {

    public String name;
    public String lo;
    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "name", "name", "lo", "lo"
        };
    }
}
