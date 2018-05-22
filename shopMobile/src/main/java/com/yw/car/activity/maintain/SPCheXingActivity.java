package com.yw.car.activity.maintain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.yw.car.R;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.adapter.SPCarAdapter;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.maintain.SPSecondCarRequest;
import com.yw.car.model.car.SPCar;
import com.yw.car.model.car.SPCheXing;

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
public class SPCheXingActivity extends SPBaseActivity {
    @ViewById(R.id.select_shenegfen)
    RecyclerView recyclerView;

    private SPCommonListModel mCommonListModel;
    private List<SPCheXing> spCheXings;
    private String xileiID;
    private List<SPCar> cars;
    private SPCarAdapter adapter;

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
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "选择车型");
        super.onCreate(savedInstanceState);
        xileiID = getIntent().getStringExtra("xileiID");
    }

    @AfterViews
    public void init() {
        super.init();
    }

    private void refreshData() {
        showLoadingSmallToast();
        RequestParams params = new RequestParams();
        params.add("series", xileiID);
        SPSecondCarRequest.chexing(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                mCommonListModel = (SPCommonListModel) response;
                if (mCommonListModel.cheXings != null) {
                    cars = new ArrayList<>();
                    spCheXings = mCommonListModel.cheXings;
                    for (int i = 0; i < spCheXings.size(); i++) {
                        try {
                            JSONArray jsonArray = new JSONArray(spCheXings.get(i).chexing_list);
                            List<SPCar> spCars = SPJsonUtil.fromJsonArrayToList(jsonArray, SPCar.class);
                            cars.addAll(spCars);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    setData(cars);
                }

            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showLoadingSmallToast();
            }
        });
    }

    public void setData(List<SPCar> cars) {
        adapter = new SPCarAdapter(cars, this, shengFenSelect);
        recyclerView.setAdapter(adapter);
    }

    private SPCarAdapter.ShengFenSelect shengFenSelect = new SPCarAdapter.ShengFenSelect() {
        @Override
        public void onClickItemShgneFen(SPCar spProvince) {
            Intent intent = new Intent();
            intent.putExtra("car", spProvince);
            setResult(10119, intent);
            finish();
        }
    };
}
