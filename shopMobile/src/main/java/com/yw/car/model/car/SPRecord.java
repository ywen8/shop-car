package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/24.
 */

public class SPRecord implements SPModel, Serializable {
    public String date;
    public String area;
    public String act;
    public String code;
    public String fen;
    public String wzcity;
    public String money;
    public String handled;
    public String archiveno;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "date", "date",
                "area", "area",
                "act", "act",
                "code", "code",
                "fen", "fen",
                "wzcity", "wzcity",
                "money", "money",
                "handled", "handled",
                "archiveno", "archiveno"

        };
    }
}
