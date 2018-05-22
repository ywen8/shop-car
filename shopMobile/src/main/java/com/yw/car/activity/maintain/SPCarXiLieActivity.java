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
import com.yw.car.adapter.SPCarXiLieAdapter;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.maintain.SPSecondCarRequest;
import com.yw.car.model.car.SPXiLie;
import com.yw.car.model.car.SPXiLieConent;

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
public class SPCarXiLieActivity extends SPBaseActivity {
    @ViewById(R.id.select_shenegfen)
    RecyclerView recyclerView;

    private SPCommonListModel spCommonListModel;
    private List<SPXiLie> xiLies;
    private String brandID;
    private List<SPXiLieConent> conents;
    private SPCarXiLieAdapter adapter;

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
        setCustomerTitle(true, true, "车辆系列");
        super.onCreate(savedInstanceState);
        brandID = getIntent().getStringExtra("brandID");
    }

    @AfterViews
    public void init() {
        super.init();
    }

    private void refreshData() {
        showLoadingSmallToast();
        RequestParams params = new RequestParams();
        params.add("brand", brandID);
        SPSecondCarRequest.xilei(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                spCommonListModel = (SPCommonListModel) response;
                if (spCommonListModel.xiLies != null) {
                    conents = new ArrayList<>();
                    xiLies = spCommonListModel.xiLies;
                    for (int i = 0; i < xiLies.size(); i++) {
                        try {
                            JSONArray jsonArray = new JSONArray(xiLies.get(i).xilie);
                            List<SPXiLieConent> xiLieConents = SPJsonUtil.fromJsonArrayToList(jsonArray, SPXiLieConent.class);
                            conents.addAll(xiLieConents);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    setdata(conents);
                }

            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
                hideLoadingSmallToast();
            }
        });
    }

    private void setdata(List<SPXiLieConent> lieConents) {
        adapter = new SPCarXiLieAdapter(lieConents, this, shengFenSelect);
        recyclerView.setAdapter(adapter);
    }

    private SPCarXiLieAdapter.ShengFenSelect shengFenSelect = new SPCarXiLieAdapter.ShengFenSelect() {
        @Override
        public void onClickItemShgneFen(SPXiLieConent spProvince) {
            Intent intent = new Intent();
            intent.putExtra("xilieContent", spProvince);
            setResult(10118, intent);
            finish();
        }
    };
}
