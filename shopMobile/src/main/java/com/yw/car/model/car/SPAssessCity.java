package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/24.
 */

public class SPAssessCity implements SPModel, Serializable {
    public String cityID;
    public String cityName;
    public String proID;
    public String value;
    public String area;
    public String pinyin;
    public String enter;
    public String hits;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "cityID", "cityID",
                "cityName", "cityName",
                "proID", "proID",
                "value", "value",
                "area", "area",
                "pinyin", "pinyin",
                "enter", "enter",
                "hits", "hits"
        };
    }
}
