package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/24.
 */

public class SPCheXing implements Serializable, SPModel {
    public String year;
    public String chexing_list;
    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "pyear","year",
                "chexing_list","chexing_list"
        };
    }
}
