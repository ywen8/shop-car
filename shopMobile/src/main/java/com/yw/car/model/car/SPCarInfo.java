package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/3/8.
 */

public class SPCarInfo implements SPModel, Serializable {
    private String car_brand_id;
    private String car_model_id;
    private String car_year;
    private String car_number;
    private String car_frame;
    private String car_engine_number;
    private String car_type;
    private String car_brand_name;
    private String car_model_name;
    private String car_city;
    public String getCar_brand_id() {
        return car_brand_id;
    }

    public String getCar_model_id() {
        return car_model_id;
    }

    public String getCar_year() {
        return car_year;
    }

    public String getCar_brand_name() {
        return car_brand_name;
    }

    public String getCar_model_name() {
        return car_model_name;
    }

    public String getCar_number() {
        return car_number;
    }

    public String getCar_frame() {
        return car_frame;
    }

    public String getCar_engine_number() {
        return car_engine_number;
    }

    public String getCar_type() {
        return car_type;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "car_brand_id", "car_brand_id",
                "car_model_id", "car_model_id",
                "car_year", "car_year",
                "car_number", "car_number",
                "car_frame", "car_frame",
                "car_engine_number", "car_engine_number",
                "car_type", "car_type",
                "car_brand_name","car_brand_name",
                "car_model_name","car_model_name",
                "car_city","car_city"
        };
    }

    public String getCar_city() {
        return car_city;
    }
}
