package com.yw.car.activity.maintain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yw.car.R;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.adapter.SPCarTypeAdapter;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.maintain.SPMaintainRequest;
import com.yw.car.model.car.SPCarBrand;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by yw on 2018/2/24.
 */
@EActivity(R.layout.activity_cartype)
public class SPCarTypeActivity extends SPBaseActivity {
    @ViewById(R.id.chexing)
    RecyclerView chexing;
    private SPCommonListModel mCommonListModel;
    private List<SPCarBrand> carBrands;
    private SPCarTypeAdapter adapter;

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {
        refreshData();
    }

    private void refreshData() {
        showLoadingSmallToast();
        SPMaintainRequest.getCarBrand(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                mCommonListModel = (SPCommonListModel) response;
                if (mCommonListModel.carbrands != null) {
                    carBrands = mCommonListModel.carbrands;
                    setCarBrands(carBrands);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showToast(msg);
            }
        });
    }

    @Override
    public void initEvent() {
        chexing.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "选择品牌");
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    public void setCarBrands(List<SPCarBrand> carBrands) {
        adapter = new SPCarTypeAdapter(carBrands, SPCarTypeActivity.this, listener);
        chexing.setAdapter(adapter);
    }

    private SPCarTypeAdapter.CarTypeItemListener listener = new SPCarTypeAdapter.CarTypeItemListener() {
        @Override
        public void carTypeItemClick(SPCarBrand name) {
            Intent intent=new Intent();
            intent.putExtra("name",name);
            setResult(10102,intent);
            finish();
        }
    };
}
