package com.yw.car.activity.maintain;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.yw.car.R;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.adapter.SPCarBrandAdapter;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.maintain.SPSecondCarRequest;
import com.yw.car.model.car.SPBrandCar;
import com.yw.car.model.car.SPPin;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yw on 2018/2/24.
 */
@EActivity(R.layout.activity_shengfen)
public class SPCarBrandActivity extends SPBaseActivity {
    @ViewById(R.id.select_shenegfen)
    RecyclerView recyclerView;

    List<SPBrandCar> list;
    private SPCommonListModel spCommonListModel;
    SPCarBrandAdapter adapter;
    private String vehicle="1";
    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {
        refreshData();
    }

    @Override
    public void initEvent() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setCustomerTitle(true, true, "选择品牌");
        super.onCreate(savedInstanceState);
        vehicle=getIntent().getStringExtra("vehicle");
    }

    @AfterViews
    public void init() {
        super.init();
    }

    private void refreshData() {
        RequestParams params = new RequestParams();
        if(Integer.valueOf(vehicle)==1){
            params.add("vehicle", "passenger");
        }else{
            params.add("vehicle", "commercial");
        }

        SPSecondCarRequest.brandcar(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                spCommonListModel = (SPCommonListModel) response;
                if (spCommonListModel.pins != null) {
                    list = new ArrayList<>();
                    addBrandCar(list, spCommonListModel.pins);
                    adapter = new SPCarBrandAdapter(addBrandCar(list, spCommonListModel.pins), SPCarBrandActivity.this, shengFenSelect);
                    recyclerView.setAdapter(adapter);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {

            }
        });
    }

    private SPCarBrandAdapter.ShengFenSelect shengFenSelect = new SPCarBrandAdapter.ShengFenSelect() {
        @Override
        public void onClickItemShgneFen(SPBrandCar spProvince) {
            Intent intent = new Intent();
            intent.putExtra("brandcar", spProvince);
            setResult(10117, intent);
            finish();
        }
    };

    public List<SPBrandCar> addBrandCar(List<SPBrandCar> list, SPPin pin) {
        if (pin != null) {
            try {
                if (pin.A!=null&&pin.A.length()>0){
                    JSONArray A = new JSONArray(pin.A);
                    List<SPBrandCar> pinA = SPJsonUtil.fromJsonArrayToList(A, SPBrandCar.class);
                    list.addAll(pinA);
                }
                if(pin.B!=null&&pin.B.length()>0){
                    JSONArray B = new JSONArray(pin.B);
                    List<SPBrandCar> pinB = SPJsonUtil.fromJsonArrayToList(B, SPBrandCar.class);
                    list.addAll(pinB);
                }
                if(pin.C!=null&&pin.C.length()>0){
                    JSONArray C = new JSONArray(pin.C);
                    List<SPBrandCar> pinC = SPJsonUtil.fromJsonArrayToList(C, SPBrandCar.class);
                    list.addAll(pinC);
                }
                if(pin.D!=null&&pin.D.length()>0){
                    JSONArray D = new JSONArray(pin.G);
                    List<SPBrandCar> pinD = SPJsonUtil.fromJsonArrayToList(D, SPBrandCar.class);
                    list.addAll(pinD);
                }
                if(pin.E!=null&&pin.E.length()>0){
                    JSONArray E = new JSONArray(pin.E);
                    List<SPBrandCar> pinE = SPJsonUtil.fromJsonArrayToList(E, SPBrandCar.class);
                    list.addAll(pinE);
                }
                if(pin.F!=null&&pin.F.length()>0){
                    JSONArray F = new JSONArray(pin.F);
                    List<SPBrandCar> pinF = SPJsonUtil.fromJsonArrayToList(F, SPBrandCar.class);
                    list.addAll(pinF);
                }
                if(pin.G!=null&&pin.G.length()>0){
                    JSONArray G = new JSONArray(pin.G);
                    List<SPBrandCar> pinG = SPJsonUtil.fromJsonArrayToList(G, SPBrandCar.class);
                    list.addAll(pinG);
                }
                if(pin.H!=null&&pin.H.length()>0){
                    JSONArray H = new JSONArray(pin.H);
                    List<SPBrandCar> pinH = SPJsonUtil.fromJsonArrayToList(H, SPBrandCar.class);
                    list.addAll(pinH);
                }
                if(pin.I!=null&&pin.I.length()>0){
                    JSONArray I = new JSONArray(pin.I);
                    List<SPBrandCar> pinI = SPJsonUtil.fromJsonArrayToList(I, SPBrandCar.class);
                    list.addAll(pinI);
                }
                if(pin.J!=null&&pin.J.length()>0){
                    JSONArray J = new JSONArray(pin.G);
                    List<SPBrandCar> pinJ = SPJsonUtil.fromJsonArrayToList(J, SPBrandCar.class);
                    list.addAll(pinJ);
                }
                if(pin.K!=null&&pin.K.length()>0){
                    JSONArray K = new JSONArray(pin.K);
                    List<SPBrandCar> pinK = SPJsonUtil.fromJsonArrayToList(K, SPBrandCar.class);
                    list.addAll(pinK);
                }
                if(pin.L!=null&&pin.L.length()>0){
                    JSONArray L = new JSONArray(pin.L);
                    List<SPBrandCar> pinL = SPJsonUtil.fromJsonArrayToList(L, SPBrandCar.class);
                    list.addAll(pinL);
                }
                if(pin.M!=null&&pin.M.length()>0){
                    JSONArray M = new JSONArray(pin.M);
                    List<SPBrandCar> pinM = SPJsonUtil.fromJsonArrayToList(M, SPBrandCar.class);
                    list.addAll(pinM);
                }
                if(pin.N!=null&&pin.N.length()>0){
                    JSONArray N = new JSONArray(pin.N);
                    List<SPBrandCar> pinN = SPJsonUtil.fromJsonArrayToList(N, SPBrandCar.class);
                    list.addAll(pinN);
                }
                if(pin.O!=null&&pin.O.length()>0){
                    JSONArray O = new JSONArray(pin.O);
                    List<SPBrandCar> pinO = SPJsonUtil.fromJsonArrayToList(O, SPBrandCar.class);
                    list.addAll(pinO);
                }
                if(pin.P!=null&&pin.P.length()>0){
                    JSONArray P = new JSONArray(pin.P);
                    List<SPBrandCar> pinP = SPJsonUtil.fromJsonArrayToList(P, SPBrandCar.class);
                    list.addAll(pinP);
                }
                if(pin.Q!=null&&pin.Q.length()>0){
                    JSONArray Q = new JSONArray(pin.Q);
                    List<SPBrandCar> pinQ = SPJsonUtil.fromJsonArrayToList(Q, SPBrandCar.class);
                    list.addAll(pinQ);
                }
                if(pin.R!=null&&pin.R.length()>0){
                    JSONArray R = new JSONArray(pin.R);
                    List<SPBrandCar> pinR = SPJsonUtil.fromJsonArrayToList(R, SPBrandCar.class);
                    list.addAll(pinR);
                }
                if(pin.S!=null&&pin.S.length()>0){
                    JSONArray S = new JSONArray(pin.S);
                    List<SPBrandCar> pinS = SPJsonUtil.fromJsonArrayToList(S, SPBrandCar.class);
                    list.addAll(pinS);
                }
                if(pin.T!=null&&pin.T.length()>0){
                    JSONArray T = new JSONArray(pin.S);
                    List<SPBrandCar> pinT = SPJsonUtil.fromJsonArrayToList(T, SPBrandCar.class);
                    list.addAll(pinT);
                }
                if(pin.U!=null&&pin.U.length()>0){
                    JSONArray U = new JSONArray(pin.U);
                    List<SPBrandCar> pinU = SPJsonUtil.fromJsonArrayToList(U, SPBrandCar.class);
                    list.addAll(pinU);
                }
                if(pin.V!=null&&pin.V.length()>0){
                    JSONArray V = new JSONArray(pin.V);
                    List<SPBrandCar> pinV = SPJsonUtil.fromJsonArrayToList(V, SPBrandCar.class);
                    list.addAll(pinV);
                }
                if(pin.W!=null&&pin.W.length()>0){
                    JSONArray W = new JSONArray(pin.W);
                    List<SPBrandCar> pinW = SPJsonUtil.fromJsonArrayToList(W, SPBrandCar.class);
                    list.addAll(pinW);
                }
                if(pin.X!=null&&pin.X.length()>0){
                    JSONArray X = new JSONArray(pin.X);
                    List<SPBrandCar> pinX = SPJsonUtil.fromJsonArrayToList(X, SPBrandCar.class);
                    list.addAll(pinX);
                }
                if(pin.Y!=null&&pin.Y.length()>0){
                    JSONArray Y = new JSONArray(pin.Y);
                    List<SPBrandCar> pinY = SPJsonUtil.fromJsonArrayToList(Y, SPBrandCar.class);
                    list.addAll(pinY);
                }
                if(pin.Z!=null&&pin.Z.length()>0){
                    JSONArray Z = new JSONArray(pin.Z);
                    List<SPBrandCar> pinZ = SPJsonUtil.fromJsonArrayToList(Z, SPBrandCar.class);
                    list.addAll(pinZ);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return list;
    }
}
