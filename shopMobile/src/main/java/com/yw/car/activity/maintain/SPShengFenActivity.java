package com.yw.car.activity.maintain;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yw.car.R;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.adapter.SPShengFenAdapter;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.maintain.SPSecondCarRequest;
import com.yw.car.model.car.SPProvince;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by yw on 2018/2/24.
 */
@EActivity(R.layout.activity_shengfen)
public class SPShengFenActivity extends SPBaseActivity {
    @ViewById(R.id.select_shenegfen)
    RecyclerView recyclerView;

    private SPCommonListModel spCommonListModel;
    private List<SPProvince> list;

    SPShengFenAdapter adapter;

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {
        refreshData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setCustomerTitle(true, true, "选择省份");
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initEvent() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void refreshData() {
        showLoadingSmallToast();
        SPSecondCarRequest.getshengfen(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                spCommonListModel = (SPCommonListModel) response;
                if (spCommonListModel.provinces != null) {
                    list = spCommonListModel.provinces;
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

    private void setData(List<SPProvince> list) {
        adapter = new SPShengFenAdapter(list, this, shengFenSelect);
        recyclerView.setAdapter(adapter);
    }

    private SPShengFenAdapter.ShengFenSelect shengFenSelect = new SPShengFenAdapter.ShengFenSelect() {
        @Override
        public void onClickItemShgneFen(SPProvince spProvince) {
            Intent intent = new Intent();
            intent.putExtra("province", spProvince);
            setResult(10105, intent);
            finish();
        }
    };
}
