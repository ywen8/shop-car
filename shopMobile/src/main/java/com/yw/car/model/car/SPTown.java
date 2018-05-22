package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/24.
 */

public class SPTown extends SPCity implements SPModel, Serializable {
    public String cityname;
    public String citycode;
    public String abbr;
    public String engine;
    public String engineno;
    public String classcity;
    public String classno;
    public String regist;
    public String registno;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "name","city_name",
                "cityname", "city_name",
                "citycode", "city_code",
                "abbr", "abbr",
                "engine", "engine",
                "engineno", "engineno",
                "classcity", "class",
                "classno", "classno",
                "regist", "regist",
                "registno", "registno"


        };
    }
}
