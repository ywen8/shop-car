package com.yw.car.activity.maintain;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.yw.car.R;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.adapter.SPAreaPriceAdapter;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.maintain.SPSecondCarRequest;
import com.yw.car.model.car.SPAssessCity;
import com.yw.car.model.car.SPBrandCar;
import com.yw.car.model.car.SPCar;
import com.yw.car.model.car.SPProvince;
import com.yw.car.model.car.SPSearch;
import com.yw.car.model.car.SPXiLieConent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by yw on 2018/2/24.
 */
@EActivity(R.layout.activity_secondcar)
public class SPSecodeCarActivity extends SPBaseActivity implements View.OnClickListener {
    @ViewById(R.id.assess_shengfen)
    TextView assess_shengfen;
    @ViewById(R.id.assess_chengshi)
    TextView assess_chengshi;
    @ViewById(R.id.yongtu)
    RadioGroup yongtu;
    @ViewById(R.id.ziyong)
    RadioButton ziyong;
    @ViewById(R.id.gongwuyong)
    RadioButton gongwuyong;
    @ViewById(R.id.yingyong)
    RadioButton yingyong;
    @ViewById(R.id.leixing)
    RadioGroup leixing;
    @ViewById(R.id.chengyong)
    RadioButton chengyong;
    @ViewById(R.id.shangyong)
    RadioButton shangyong;
    @ViewById(R.id.qingkuang)
    RadioGroup qingkuang;
    @ViewById(R.id.youxiu)
    RadioButton youxiu;
    @ViewById(R.id.yiban)
    RadioButton yiban;
    @ViewById(R.id.jiaocha)
    RadioButton jiaocha;
    @ViewById(R.id.chebrand)
    TextView cheBrand;
    @ViewById(R.id.car_xilie)
    TextView xilie;
    @ViewById(R.id.car_chexing)
    TextView chexing;
    @ViewById(R.id.chaxun)
    TextView chaxun;
    @ViewById(R.id.time_year)
    EditText year;
    @ViewById(R.id.time_month)
    EditText month;
    @ViewById(R.id.licheng)
    EditText licheng;
    @ViewById(R.id.price)
    EditText price;
    @ViewById(R.id.ershougou)
    TextView ershougou;
    @ViewById(R.id.gerenjiaoyi)
    TextView ershoujiaoyi;
    @ViewById(R.id.ershoumaichu)
    TextView ershoumaichu;
    @ViewById(R.id.search_l)
    LinearLayout search;
    @ViewById(R.id.secondcar_recycler)
    RecyclerView recyclerView;
    private int ResultCode;
    private String shengfenID;
    private String cityID;
    private int yongtuType = 1;
    private int leixingType = 1;
    private int qingkuangType = 1;
    private String brandID;
    private String xileiID;
    private String carID;
    SPAreaPriceAdapter adapter;


    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {
        refreshData();
    }

    @Override
    public void initEvent() {
        assess_shengfen.setOnClickListener(this);
        assess_chengshi.setOnClickListener(this);
        cheBrand.setOnClickListener(this);
        xilie.setOnClickListener(this);
        chexing.setOnClickListener(this);
        chaxun.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        yongtu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.ziyong:
                        yongtuType = 1;
                        ziyong.setBackgroundResource(R.drawable.btn_pressed_true);
                        gongwuyong.setBackgroundResource(R.drawable.btn_pressed_false);
                        yingyong.setBackgroundResource(R.drawable.btn_pressed_false);
                        break;
                    case R.id.gongwuyong:
                        yongtuType = 2;
                        ziyong.setBackgroundResource(R.drawable.btn_pressed_false);
                        gongwuyong.setBackgroundResource(R.drawable.btn_pressed_true);
                        yingyong.setBackgroundResource(R.drawable.btn_pressed_false);
                        break;
                    case R.id.yingyong:
                        yongtuType = 3;
                        ziyong.setBackgroundResource(R.drawable.btn_pressed_false);
                        gongwuyong.setBackgroundResource(R.drawable.btn_pressed_false);
                        yingyong.setBackgroundResource(R.drawable.btn_pressed_true);
                        break;

                }
            }
        });
        leixing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.chengyong:
                        leixingType = 1;
                        chengyong.setBackgroundResource(R.drawable.btn_pressed_true);
                        shangyong.setBackgroundResource(R.drawable.btn_pressed_false);
                        break;
                    case R.id.shangyong:
                        leixingType = 2;
                        chengyong.setBackgroundResource(R.drawable.btn_pressed_false);
                        shangyong.setBackgroundResource(R.drawable.btn_pressed_true);
                        break;
                }
            }
        });
        qingkuang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.youxiu:
                        qingkuangType = 1;
                        youxiu.setBackgroundResource(R.drawable.btn_pressed_true);
                        yiban.setBackgroundResource(R.drawable.btn_pressed_false);
                        jiaocha.setBackgroundResource(R.drawable.btn_pressed_false);
                        break;
                    case R.id.yiban:
                        qingkuangType = 2;
                        youxiu.setBackgroundResource(R.drawable.btn_pressed_false);
                        yiban.setBackgroundResource(R.drawable.btn_pressed_true);
                        jiaocha.setBackgroundResource(R.drawable.btn_pressed_false);
                        break;
                    case R.id.jiaocha:
                        qingkuangType = 3;
                        youxiu.setBackgroundResource(R.drawable.btn_pressed_false);
                        yiban.setBackgroundResource(R.drawable.btn_pressed_false);
                        jiaocha.setBackgroundResource(R.drawable.btn_pressed_true);
                        break;
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setCustomerTitle(true, true, "二车手估值");
        super.onCreate(savedInstanceState);

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
            case R.id.assess_shengfen:
                Intent intent = new Intent(this, SPShengFenActivity_.class);
                ResultCode = 10115;
                startActivityForResult(intent, ResultCode);
                break;
            case R.id.assess_chengshi:
                if (shengfenID == null || shengfenID.length() == 0) {
                    showToast("请先选择省份!");
                    return;
                }
                Intent cityIntent = new Intent(this, SPAssessCityActivity_.class);
                cityIntent.putExtra("shengfenID", shengfenID);
                ResultCode = 10116;
                startActivityForResult(cityIntent, ResultCode);
                break;
            case R.id.chebrand:
                Intent chebrandintent = new Intent(this, SPCarBrandActivity_.class);
                ResultCode = 10117;
                chebrandintent.putExtra("vehicle", leixingType + "");
                startActivityForResult(chebrandintent, ResultCode);
                break;
            case R.id.car_xilie:
                if (brandID != null && brandID.length() > 0) {
                    Intent xilieintent = new Intent(this, SPCarXiLieActivity_.class);
                    ResultCode = 10118;
                    xilieintent.putExtra("brandID", brandID);
                    startActivityForResult(xilieintent, ResultCode);
                } else {
                    showToast("请先选择品牌");
                    return;
                }
                break;
            case R.id.car_chexing:
                if (xileiID != null && xileiID.length() > 0) {
                    Intent xilieintent = new Intent(this, SPCheXingActivity_.class);
                    ResultCode = 10119;
                    xilieintent.putExtra("xileiID", xileiID);
                    startActivityForResult(xilieintent, ResultCode);
                } else {
                    showToast("请先选择车辆系列");
                }
                break;
            case R.id.chaxun:
                if (assess_shengfen.getText().toString().length() == 0) {
                    showToast("请先选择省份!");
                    return;
                }
                if (assess_chengshi.getText().toString().length() == 0) {
                    showToast("请选择城市!");
                    return;
                }
                if (cheBrand.getText().toString().length() == 0) {
                    showToast("请选择品牌!");
                    return;
                }
                if (xilie.getText().toString().length() == 0) {
                    showToast("请选择系列!");
                    return;
                }
                if (chexing.getText().toString().length() == 0) {
                    showToast("请选择车型!");
                    return;
                }
                if (year.getText().toString().length() == 0) {
                    showToast("请输入年份!");
                    return;
                }
                if (month.getText().toString().length() == 0) {
                    showToast("请输入月份!");
                    return;
                }
                if (licheng.getText().toString().length() == 0) {
                    showToast("请输入里程!");
                    return;
                }
                if (price.getText().toString().length() == 0) {
                    showToast("请输入价格!");
                    return;
                }
                search();
                break;

        }
    }

    private void search() {
        showLoadingSmallToast();
        RequestParams params = new RequestParams();
        params.add("carstatus", qingkuangType + "");
        params.add("purpose", yongtuType + "");
        params.add("city", cityID);
        params.add("province", shengfenID);
        params.add("car", carID + "");
        params.add("useddate", year.getText().toString());
        params.add("useddateMonth", month.getText().toString());
        params.add("mileage", licheng.getText().toString());
        params.add("price", price.getText().toString());
        SPSecondCarRequest.searchAssess(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                SPSearch spSearch = (SPSearch) response;
                String[] strings = spSearch.str;
                ershougou.setText(strings[0].replace("[", "") + "");
                ershoujiaoyi.setText(strings[1] + "");
                ershoumaichu.setText(strings[2] + "");
                adapter = new SPAreaPriceAdapter(SPSecodeCarActivity.this, spSearch.list);
                recyclerView.setAdapter(adapter);
                search.setVisibility(View.VISIBLE);
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case 10115:
                    SPProvince province = (SPProvince) data.getSerializableExtra("province");
                    if (province != null) {
                        assess_shengfen.setText(province.proName);
                        shengfenID = province.proID;
                        assess_chengshi.setText("");
                        cityID = "";
                    }
                    break;
                case 10116:
                    SPAssessCity assessCity = (SPAssessCity) data.getSerializableExtra("assessCity");
                    if (assessCity != null) {
                        assess_chengshi.setText(assessCity.cityName);
                        cityID = assessCity.cityID;
                    }
                    break;
                case 10117:
                    SPBrandCar brandCar = (SPBrandCar) data.getSerializableExtra("brandcar");
                    if (brandCar != null) {
                        cheBrand.setText(brandCar.big_ppname);
                        brandID = brandCar.id;
                        xilie.setText("");
                        xileiID = "";
                        chexing.setText("");
                        carID = "";
                    }
                    break;
                case 10118:
                    SPXiLieConent spXiLieConent = (SPXiLieConent) data.getSerializableExtra("xilieContent");
                    if (spXiLieConent != null) {
                        xilie.setText(spXiLieConent.xlname);
                        xileiID = spXiLieConent.xlid;
                        chexing.setText("");
                        carID = "";
                    }
                    break;
                case 10119:
                    SPCar spCar = (SPCar) data.getSerializableExtra("car");
                    if (spCar != null) {
                        chexing.setText(spCar.cxname);
                        carID = spCar.id;
                    }
                    break;
            }
        }
    }
}
