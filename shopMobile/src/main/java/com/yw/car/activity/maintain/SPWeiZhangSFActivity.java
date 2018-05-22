package com.yw.car.activity.maintain;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yw.car.R;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.adapter.SPWeiZhangShengFenAdapter;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.maintain.SPSecondCarRequest;
import com.yw.car.model.car.SPShengFen;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by yw on 2018/3/10.
 */
@EActivity(R.layout.activity_shengfen)
public class SPWeiZhangSFActivity extends SPBaseActivity {

    @ViewById(R.id.select_shenegfen)
    RecyclerView recyclerView;

    SPWeiZhangShengFenAdapter adapter;

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
        setCustomerTitle(true, true, "选择省份");
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    public void init() {
        super.init();
    }

    private void refreshData() {
        showLoadingSmallToast();
        SPSecondCarRequest.getWeiZhang(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                List<SPShengFen> list = (List<SPShengFen>) response;
                setData(list);
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
                hideLoadingSmallToast();
            }
        });
    }

    private SPWeiZhangShengFenAdapter.ShengFenSelect shengFenSelect = new SPWeiZhangShengFenAdapter.ShengFenSelect() {
        @Override
        public void onClickItemShgneFen(SPShengFen spProvince) {
            Intent intent = new Intent();
            intent.putExtra("shengfen", spProvince);
            setResult(10011, intent);
            finish();
        }
    };

    private void setData(List<SPShengFen> list) {
        adapter = new SPWeiZhangShengFenAdapter(list, this, shengFenSelect);
        recyclerView.setAdapter(adapter);
    }
}
