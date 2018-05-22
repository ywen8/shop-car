package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/9.
 */

public class SPParticular implements SPModel,Serializable {

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[0];
    }
}
