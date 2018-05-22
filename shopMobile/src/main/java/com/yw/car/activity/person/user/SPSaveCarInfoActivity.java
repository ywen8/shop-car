package com.yw.car.activity.person.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.yw.car.R;
import com.yw.car.SPMainActivity;
import com.yw.car.activity.common.PinYinActivity_;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.activity.maintain.SPCarTypeActivity_;
import com.yw.car.activity.maintain.SPModelTypeActivity_;
import com.yw.car.activity.maintain.SPNianFenActivity_;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.register.SPRegisterRequest;
import com.yw.car.model.car.SPCarBrand;
import com.yw.car.model.car.SPCarInfo;
import com.yw.car.model.car.SPMotorcycle;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by yw on 2018/3/8.
 */

@EActivity(R.layout.activity_savecar)
public class SPSaveCarInfoActivity extends SPBaseActivity implements View.OnClickListener {
    private int requestCode;
    @ViewById(R.id.car_band)
    TextView car_band;
    @ViewById(R.id.car_city)
    TextView car_city;
    @ViewById(R.id.car_nianfen)
    TextView car_nianfen;
    @ViewById(R.id.car_type)
    TextView car_type;
    @ViewById(R.id.car_fadongji)
    EditText fadongji;
    @ViewById(R.id.car_chejia)
    EditText chejia;
    @ViewById(R.id.car_chepai)
    EditText chepai;
    @ViewById(R.id.car_leixing)
    EditText leixing;
    @ViewById(R.id.car_commit)
    Button commit;
    @ViewById(R.id.car_skip)
    TextView car_skip;
    private String carBrandID;
    private String carTypeID;
    private boolean isShow;
    @ViewById(R.id.savecar_top)
    LinearLayout car_top;

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {
        refreshData();
    }

    @Override
    public void initEvent() {
        car_nianfen.setOnClickListener(this);
        car_band.setOnClickListener(this);
        car_city.setOnClickListener(this);
        car_type.setOnClickListener(this);
        car_skip.setOnClickListener(this);
        commit.setOnClickListener(this);
        if (!isShow) {
            car_top.setVisibility(View.VISIBLE);
        } else {
            car_top.setVisibility(View.GONE);
        }

    }

    @AfterViews
    public void init() {
        super.init();
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
                    car_band.setText(carBrand.getBrandName());
                    carBrandID = carBrand.getBrandID() + "";
                }
                break;
            case 10103:
                if (data != null) {
                    SPMotorcycle motorcycle = (SPMotorcycle) data.getSerializableExtra("model");
                    car_type.setText(motorcycle.getModelName());
                    carTypeID = motorcycle.getModelID() + "";
                }
                break;
            case 10104:
                if (data != null) {
                    String nianfen = data.getStringExtra("nianfen");
                    car_nianfen.setText(nianfen);
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "查看汽车信息");
        super.onCreate(savedInstanceState);
        isShow = getIntent().getBooleanExtra("isShow", false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.car_city:
                Intent pinyinintent = new Intent(this, PinYinActivity_.class);
                requestCode = 10101;
                startActivityForResult(pinyinintent, requestCode);
                break;
            case R.id.car_band:
                Intent chexing = new Intent(this, SPCarTypeActivity_.class);
                requestCode = 10102;
                startActivityForResult(chexing, requestCode);
                break;
            case R.id.car_type:
                Intent xinghao = new Intent(this, SPModelTypeActivity_.class);
                requestCode = 10103;
                startActivityForResult(xinghao, requestCode);
                break;
            case R.id.car_nianfen:
                Intent nianfen = new Intent(this, SPNianFenActivity_.class);
                requestCode = 10104;
                startActivityForResult(nianfen, requestCode);
                break;
            case R.id.car_skip:
                startActivity(new Intent(SPSaveCarInfoActivity.this, SPMainActivity.class));
                finish();
                break;
            case R.id.car_commit:
                if (car_band.getText().toString().trim().length() == 0 || car_band.getText().toString().trim() == "") {
                    showToast("请选择车的品牌！");
                    return;
                }
                if (car_type.getText().toString().trim().length() == 0 || car_type.getText().toString().trim() == "") {
                    showToast("请选择车的型号!");
                    return;
                }
                if (car_nianfen.getText().toString().trim().length() == 0 || car_nianfen.getText().toString().trim() == "") {
                    showToast("请选择年份!");
                    return;
                }
                if (chepai.getText().toString().trim().length() == 0 || chepai.getText().toString().trim() == "") {
                    showToast("请输入车牌号!");
                    return;
                }
                if (chejia.getText().toString().trim().length() == 0 || chejia.getText().toString().trim() == "") {
                    showToast("请输入车架号!");
                    return;
                }
                if (fadongji.getText().toString().trim().length() == 0 || fadongji.getText().toString().trim() == "") {
                    showToast("请输入发动机号!");
                    return;
                }
                if (leixing.getText().toString().trim().length() == 0 || leixing.getText().toString().trim() == "") {
                    showToast("请输入车辆类型!");
                    return;
                }
                showLoadingSmallToast();
                RequestParams params = new RequestParams();
                params.add("car_brand_id", carBrandID);
                params.add("car_model_id", carTypeID);
                params.add("car_year", car_nianfen.getText().toString().trim());
                params.add("car_number", chepai.getText().toString().trim());
                params.add("car_frame", chejia.getText().toString().trim());
                params.add("car_engine_number", fadongji.getText().toString().trim());
                params.add("car_type", leixing.getText().toString().trim());
                params.add("car_city", car_city.getText().toString().trim());
                SPRegisterRequest.getRimData(params, new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingSmallToast();
                        showSuccessToast("提交成功!");
                        startActivity(new Intent(SPSaveCarInfoActivity.this, SPMainActivity.class));
                        finish();
                    }
                }, new SPFailuredListener() {

                    @Override
                    public void onRespone(String msg, int errorCode) {
                        showToast(msg);
                        hideLoadingSmallToast();
                    }
                });

                break;
        }
    }

    private void refreshData() {
        showLoadingSmallToast();
        SPRegisterRequest.takeInfo(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                SPCarInfo spCarInfo = (SPCarInfo) response;
                if (spCarInfo != null) {
                    setCarInfo(spCarInfo);
                }

            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
//                showToast(msg);
            }
        });
    }

    private void setCarInfo(SPCarInfo carInfo) {
        car_band.setText(carInfo.getCar_brand_name() + "");
        car_type.setText(carInfo.getCar_model_name() + "");
        car_nianfen.setText(carInfo.getCar_year() + "");
        chepai.setText(carInfo.getCar_number() + "");
        chejia.setText(carInfo.getCar_frame() + "");
        fadongji.setText(carInfo.getCar_engine_number() + "");
        leixing.setText(carInfo.getCar_type() + "");
        car_city.setText(carInfo.getCar_city() + "");
    }
}
