package com.yw.car.activity.maintain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yw.car.R;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.adapter.SPModelTypeAdapter;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.maintain.SPMaintainRequest;
import com.yw.car.model.car.SPMotorcycle;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by yw on 2018/2/24.
 */
@EActivity(R.layout.activity_cartype)
public class SPModelTypeActivity extends SPBaseActivity {
    @ViewById(R.id.chexing)
    RecyclerView chexing;
    private SPModelTypeAdapter adapter;
    private SPCommonListModel mCommonListModel;
    private List<SPMotorcycle> carModels;

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {
        refreshData();
    }

    private void refreshData() {
        showLoadingSmallToast();
        SPMaintainRequest.getCarBrandModel(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                mCommonListModel = (SPCommonListModel) response;
                if (mCommonListModel.carmodels != null) {
                    carModels = mCommonListModel.carmodels;
                    setCarModels(carModels);
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
        setCustomerTitle(true, true, "选择型号");
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }



    private SPModelTypeAdapter.CarTypeItemListener listener = new SPModelTypeAdapter.CarTypeItemListener() {
        @Override
        public void carTypeItemClick(SPMotorcycle name) {
            Intent intent = new Intent();
            intent.putExtra("model", name);
            setResult(10103, intent);
            finish();
        }
    };

    public void setCarModels(List<SPMotorcycle> carModels) {
        adapter = new SPModelTypeAdapter(carModels, SPModelTypeActivity.this, listener);
        chexing.setAdapter(adapter);
    }
}
