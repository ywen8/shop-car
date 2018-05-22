package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yw on 2018/2/15.
 */

public class SPArea implements SPModel, Serializable {
    public String province;
    public List<SPTown> citys;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{"province", "province", "citys", "citys"};
    }
}
