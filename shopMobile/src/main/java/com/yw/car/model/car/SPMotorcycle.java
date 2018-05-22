package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/9.
 */

public class SPMotorcycle implements SPModel, Serializable {
    private int modelID;
    private String modelName;

    public void setModelID(int modelID) {
        this.modelID = modelID;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getModelID() {

        return modelID;
    }

    public String getModelName() {
        return modelName;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "modelID", "model_id", "modelName", "model_name"
        };
    }
}
