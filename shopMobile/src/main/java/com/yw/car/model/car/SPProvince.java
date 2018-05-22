package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/24.
 */

public class SPProvince implements SPModel, Serializable {
    public String proID;
    public String proName;
    public String pinyin;
    public String pin;
    public String logo;
    public String hits;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "proID", "proID",
                "proName", "proName",
                "pinyin", "pinyin",
                "pin", "pin",
                "logo", "logo",
                "hits", "hits"
        };
    }
}
