package com.yw.car.model.car;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * Created by yw on 2018/2/24.
 */

public class SPPin implements SPModel, Serializable {
    public String A;
    public String B;
    public String C;
    public String D;
    public String E;
    public String F;
    public String G;
    public String H;
    public String I;
    public String J;
    public String K;
    public String L;
    public String M;
    public String N;
    public String O;
    public String P;
    public String Q;
    public String R;
    public String S;
    public String T;
    public String U;
    public String V;
    public String W;
    public String X;
    public String Y;
    public String Z;


    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "A","A",
                "B","B",
                "C","C",
                "D","D",
                "E","E",
                "F","F",
                "G","G",
                "H","H",
                "I","I",
                "J","J",
                "K","K",
                "L","L",
                "M","M",
                "N","N",
                "O","O",
                "P","P",
                "Q","Q",
                "R","R",
                "S","S",
                "T","T",
                "U","U",
                "V","V",
                "W","W",
                "X","X",
                "Y","Y",
                "Z","Z"
        };
    }
}
