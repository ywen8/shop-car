package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/27.
 */

public class SPInsurance implements SPModel, Serializable {
    public String insuranceid;
    public String insurancename;
    public String insuranceimg;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "insuranceid", "insurance_id",
                "insurancename", "insurance_name",
                "insuranceimg", "insurance_img"
        };
    }
}
