package com.yw.car.activity.maintain;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.yw.car.R;
import com.yw.car.activity.baidu.SPBaiDuBaseActivity;
import com.yw.car.adapter.SPInsuranceAdapter;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.maintain.SPMaintainRequest;
import com.yw.car.model.car.SPMaintain;
import com.yw.car.service.OnFragmentInteractionListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.List;

/**
 * Created by yw on 2018/2/15.
 */
@EActivity(R.layout.fragment_insurance)
public class SPMainTainActivity extends SPBaiDuBaseActivity implements OnFragmentInteractionListener {

    private RecyclerView insurance_cyclerview;
    SPCommonListModel mCommonListModel;
    SPInsuranceAdapter adapter;
    TextView title_name;
    private OnFragmentInteractionListener onFragmentInteractionListener;

    @Override
    public void initSubViews() {
        title_name = (TextView) findViewById(R.id.insurance_title_name);
        insurance_cyclerview = (RecyclerView) findViewById(R.id.insurance_cyclerview);
    }

    @Override
    public void initData() {
        refreshData();
    }

    @Override
    public void initEvent() {
        insurance_cyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "维修点查询");
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    private void refreshData() {
        showLoadingSmallToast();
        RequestParams params=new RequestParams();
        params.add("type","3");
        SPMaintainRequest.getCarService(params,new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                mCommonListModel = (SPCommonListModel) response;
                if (mCommonListModel != null) {
                    if (mCommonListModel.maintains != null) {
                        setbaoxian(mCommonListModel.maintains);
                    }
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
            }
        });
    }

    private void setbaoxian(List<SPMaintain> maintains) {
        adapter = new SPInsuranceAdapter(this, maintains, this);
        insurance_cyclerview.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(SPMaintain maintain) {
        setLatWithLog(maintain);
    }
}
