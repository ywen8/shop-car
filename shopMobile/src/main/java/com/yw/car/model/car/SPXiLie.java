package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/24.
 */

public class SPXiLie implements SPModel ,Serializable {
    public String ppid;
    public String ppname;
    public String xilie;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "ppid","ppid",
                "ppname","ppname",
                "xilie","xilie"
        };
    }
}
