package com.yw.car.activity.maintain;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.loopj.android.http.RequestParams;
import com.yw.car.R;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.adapter.SPWeiZhangCityAdapter;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.maintain.SPMaintainRequest;
import com.yw.car.model.car.SPTown;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by yw on 2018/3/10.
 */
@EActivity(R.layout.activity_shengfen)
public class SPWeiZhangCityActivity extends SPBaseActivity {

    @ViewById(R.id.select_shenegfen)
    RecyclerView recyclerView;
    private String lo;

    private List<SPTown> list;

    SPCommonListModel mCommonListModel;

    SPWeiZhangCityAdapter adapter;

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

    }

    @AfterViews
    public void init() {
        super.init();
    }

    private void refreshData() {
        lo=getIntent().getStringExtra("lo");
        if (lo != ""&&lo!=null) {
            showLoadingSmallToast();
            RequestParams params = new RequestParams();
            params.add("province", lo);
            SPMaintainRequest.getArea(params, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingSmallToast();
                    mCommonListModel = (SPCommonListModel) response;
                    list = mCommonListModel.towns;
                    setdata(list);
                }
            }, new SPFailuredListener() {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingSmallToast();
                    showToast(msg);
                }
            });
        }

    }

    private void setdata(List<SPTown> list) {
        adapter = new SPWeiZhangCityAdapter(list, this, shengFenSelect);
        recyclerView.setAdapter(adapter);
    }

    SPWeiZhangCityAdapter.ShengFenSelect shengFenSelect = new SPWeiZhangCityAdapter.ShengFenSelect() {
        @Override
        public void onClickItemShgneFen(SPTown spProvince) {
            Intent intent = new Intent();
            intent.putExtra("chengshi", spProvince);
            setResult(10012, intent);
            finish();
        }
    };

}
