package com.yw.car.activity.maintain;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.loopj.android.http.RequestParams;
import com.yw.car.R;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.adapter.SPAssessCityAdapter;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.maintain.SPSecondCarRequest;
import com.yw.car.model.car.SPAssessCity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by yw on 2018/2/24.
 */
@EActivity(R.layout.activity_shengfen)
public class SPAssessCityActivity extends SPBaseActivity {
    @ViewById(R.id.select_shenegfen)
    RecyclerView recyclerView;

    private SPCommonListModel spCommonListModel;
    private List<SPAssessCity> list;
    SPAssessCityAdapter adapter;
    private String shengfenID;
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
        setCustomerTitle(true, true, "选择城市");
        super.onCreate(savedInstanceState);
        shengfenID=getIntent().getStringExtra("shengfenID");

    }

    @AfterViews
    public void init() {
        super.init();
    }


    private void refreshData() {
        showLoadingSmallToast();
        RequestParams params = new RequestParams();
        params.add("province", shengfenID);
        SPSecondCarRequest.assesscity(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                spCommonListModel = (SPCommonListModel) response;
                if (spCommonListModel.provinces != null) {
                    list = spCommonListModel.assessCities;
                    setData(list);
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

    private void setData(List<SPAssessCity> list) {
        adapter = new SPAssessCityAdapter(list, this, shengFenSelect);
        recyclerView.setAdapter(adapter);
    }

    private SPAssessCityAdapter.ShengFenSelect shengFenSelect = new SPAssessCityAdapter.ShengFenSelect() {
        @Override
        public void onClickItemShgneFen(SPAssessCity spProvince) {
            Intent intent = new Intent();
            intent.putExtra("assessCity", spProvince);
            setResult(10116, intent);
            finish();
        }
    };
}
