package com.yw.car.activity.maintain;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.yw.car.R;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.adapter.SPWeiZhangAdapter;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.maintain.SPMaintainRequest;
import com.yw.car.model.car.SPArea;
import com.yw.car.model.car.SPCity;
import com.yw.car.model.car.SPRecord;
import com.yw.car.model.car.SPShengFen;
import com.yw.car.model.car.SPTown;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


/**
 * Created by yw on 2018/2/15.
 */
@EActivity(R.layout.activity_weizhang)
public class SPWeiZhangAcitivty extends SPBaseActivity implements View.OnClickListener {
    @ViewById(R.id.shengfen)
    TextView shengfen;
    @ViewById(R.id.chengshi)
    TextView chengshi;
    private List<SPCity> list;
    private List<SPRecord> records;
    SPCommonListModel mCommonListModel;
    @ViewById(R.id.chexing)
    RadioGroup chexing;
    @ViewById(R.id.small_xing)
    RadioButton small_xing;
    @ViewById(R.id.big_xing)
    RadioButton big_xing;
    @ViewById(R.id.chaxun)
    TextView chaxun;
    @ViewById(R.id.chepai)
    EditText chepai;
    @ViewById(R.id.chejia)
    EditText chejia;
    @ViewById(R.id.fadongji)
    EditText fadongji;
    @ViewById(R.id.weizhang_item)
    RecyclerView weizhang_item;
    private SPArea spArea;
    private List<SPTown> spTowns;
    private String cityCode;
    private int type = 2;
    SPWeiZhangAdapter adapter;
    @ViewById(R.id.item_li)
    RelativeLayout layout;
    private int resultCode;
    private String sf;

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {
        refreshData();
    }

    @Override
    public void initEvent() {
        weizhang_item.setLayoutManager(new LinearLayoutManager(this));
        chaxun.setOnClickListener(this);
        shengfen.setOnClickListener(this);
        chengshi.setOnClickListener(this);
//        shengfen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                getCity(list.get(i).lo);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        chengshi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                cityCode = spTowns.get(i).citycode;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        chexing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                switch (i) {
                    case R.id.small_xing:
                        type = 2;
                        small_xing.setBackgroundResource(R.drawable.btn_pressed_true);
                        big_xing.setBackgroundResource(R.drawable.btn_pressed_false);
                        break;
                    case R.id.big_xing:
                        type = 1;
                        small_xing.setBackgroundResource(R.drawable.btn_pressed_false);
                        big_xing.setBackgroundResource(R.drawable.btn_pressed_true);
                        break;
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setCustomerTitle(true, true, "维章查询");
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    public void init() {
        super.init();
    }

    private void refreshData() {

    }

    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chaxun:
                if (shengfen.getText().toString().length() == 0) {
                    showToast("请先选择省份！");
                    break;
                }
                if (chengshi.getText().toString().length() == 0) {
                    showToast("请先选择城市！");
                    break;
                }
                if (chepai.getText().toString().length() == 0) {
                    showToast("请输入车牌号");
                    break;
                }
                if (chejia.getText().toString().length() == 0) {
                    showToast("请输入车架号");
                    break;
                }
                if (fadongji.getText().length() == 0) {
                    showToast("请输入发动机号");
                    break;
                }
                layout.setVisibility(View.VISIBLE);
                searchWeiZhang();
                break;
            case R.id.shengfen:
                resultCode = 10011;
                Intent intent = new Intent(this, SPWeiZhangSFActivity_.class);
                startActivityForResult(intent, resultCode);
                sf = "";
                chengshi.setText("");
                break;
            case R.id.chengshi:
                if (shengfen.getText().length() == 0) {
                    showToast("请先选择省份！");
                    return;
                }
                resultCode = 10012;
                Intent intent1 = new Intent(this, SPWeiZhangCityActivity_.class);
                intent1.putExtra("lo", sf);
                startActivityForResult(intent1, resultCode);
                break;

        }
    }

    public void searchWeiZhang() {
        showLoadingSmallToast();
        RequestParams params = new RequestParams();
        params.add("city", cityCode);
        try {
            String chepaistr = URLEncoder.encode(chepai.getText().toString(), "utf-8");
            params.add("hphm", chepaistr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.add("hpzl", type + "");
        params.add("engineno", fadongji.getText().toString());
        params.add("classno", chejia.getText().toString());
        SPMaintainRequest.searchWeiZhang(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                mCommonListModel = (SPCommonListModel) response;
                records = mCommonListModel.records;
                if (records != null) {
                    setRecord();
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

    public void setRecord() {
        adapter = new SPWeiZhangAdapter(records, this);
        weizhang_item.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (resultCode) {
                case 10011:
                    SPShengFen spShengFen = (SPShengFen) data.getSerializableExtra("shengfen");
                    if (spShengFen != null) {
                        shengfen.setText(spShengFen.getProvince());
                        sf = spShengFen.getProvince_code();
                    }
                    break;
                case 10012:
                    SPTown spTown = (SPTown) data.getSerializableExtra("chengshi");
                    if (spTown != null) {
                        chengshi.setText(spTown.cityname);
                        cityCode = spTown.citycode;
                    }
                    break;
            }
        }

    }
}
