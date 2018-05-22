package com.yw.car.activity.maintain;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.yw.car.R;
import com.yw.car.activity.common.PinYinActivity_;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.maintain.SPMaintainRequest;
import com.yw.car.model.car.SPCarBrand;
import com.yw.car.model.car.SPMotorcycle;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by yw on 2018/2/14.
 */
@EActivity(R.layout.activity_sprim_smallmaintain)
public class SPBigMainTainActivity extends SPBaseActivity implements View.OnClickListener {
    @ViewById(R.id.rim_smallmaintain_car_type)
    TextView car_type;

    @ViewById(R.id.rim_smallmaintain_model)
    TextView car_model;

    @ViewById(R.id.rim_smallmaintain_particular)
    TextView car_particular;

    @ViewById(R.id.rim_smallmaintain_city)
    TextView car_city;

    @ViewById(R.id.rimsmallmaintain_commit)
    Button rim_commit;

    @ViewById(R.id.rim_small_licheng)
    EditText licheng;

    private SPCommonListModel mCommonListModel;
    private List<SPCarBrand> carBrands;
    private List<SPMotorcycle> carModels;
    private String carTypeId;
    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "保养");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {
        refreshData();
    }

    @Override
    public void initEvent() {
        car_city.setOnClickListener(this);
        car_type.setOnClickListener(this);
        car_model.setOnClickListener(this);
        car_particular.setOnClickListener(this);
        rim_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (licheng.getText().toString().trim().length() == 0) {
                    showFailedToast("请输入里程");
                    return;
                }
                if (car_particular.getText().toString().trim().length() == 0) {
                    showFailedToast("请选择年份");
                    return;
                }
                if (car_city.getText().toString().trim().length() == 0) {
                    showFailedToast("请选择城市");
                    return;
                }
                if (car_type.getText().toString().trim().length() == 0) {
                    showFailedToast("请选择车的品牌");
                    return;
                }
                if (car_model.getText().toString().trim().length() == 0) {
                    showFailedToast("请选择车的型号");
                    return;
                }
                showLoadingSmallToast();
                RequestParams params = new RequestParams();
                params.add("maintain_type", "1");
                params.add("brand_model_id", car_type.getText().toString().trim() + car_model.getText().toString().trim());
                params.add("mileage", licheng.getText() + "");
                params.add("area_id", car_city.getText().toString().trim() + "");
                params.add("maintain_year", car_particular.getText() + "");
                SPMaintainRequest.addbaoxian(params, new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingSmallToast();
                        Intent intent = new Intent(SPBigMainTainActivity.this, SPSuccessActivity_.class);
                        startActivity(intent);
                        finish();
                    }
                }, new SPFailuredListener() {

                    @Override
                    public void onRespone(String msg, int errorCode) {
                        showToast(msg);
                        hideLoadingSmallToast();
                    }
                });
            }
        });

    }

    @AfterViews
    public void init() {
        super.init();
    }

    private void refreshData() {
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rim_smallmaintain_city:
                Intent pinyinintent = new Intent(this, PinYinActivity_.class);
                requestCode = 10101;
                startActivityForResult(pinyinintent, requestCode);
                break;
            case R.id.rim_smallmaintain_car_type:
                Intent chexing = new Intent(this, SPCarTypeActivity_.class);
                requestCode = 10102;
                startActivityForResult(chexing, requestCode);
                break;
            case R.id.rim_smallmaintain_model:
                Intent xinghao = new Intent(this, SPModelTypeActivity_.class);
                requestCode = 10103;
                startActivityForResult(xinghao, requestCode);
                break;
            case R.id.rim_smallmaintain_particular:
                Intent nianfen = new Intent(this, SPNianFenActivity_.class);
                requestCode = 10104;
                startActivityForResult(nianfen, requestCode);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 10101:
                if (data != null) {
                    String city = data.getStringExtra("city");
                    car_city.setText(city);
                }
                break;
            case 10102:
                if (data != null) {
                    SPCarBrand carBrand = (SPCarBrand) data.getSerializableExtra("name");
                    car_type.setText(carBrand.getBrandName());
                }
                break;
            case 10103:
                if (data != null) {
                    SPMotorcycle motorcycle =  (SPMotorcycle) data.getSerializableExtra("model");
                    car_model.setText(motorcycle.getModelName());
                }
                break;
            case 10104:
                if (data != null) {
                    String nianfen = data.getStringExtra("nianfen");
                    car_particular.setText(nianfen);
                }
                break;
        }
    }
}
