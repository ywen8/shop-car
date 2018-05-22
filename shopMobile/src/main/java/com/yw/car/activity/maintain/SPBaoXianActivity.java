package com.yw.car.activity.maintain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.yw.car.R;
import com.yw.car.activity.common.PinYinActivity_;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.adapter.SPChannelAdapter;
import com.yw.car.adapter.SPChannelAdapter.OnclickCallBackListener;
import com.yw.car.entity.SPCommonListModel;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.maintain.SPInsuranceRequest;
import com.yw.car.model.car.SPInsurance;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by yw on 2018/2/24.
 */

@EActivity(R.layout.activity_baoxian)
public class SPBaoXianActivity extends SPBaseActivity implements View.OnClickListener {
    @ViewById(R.id.baoxian_chengshi)
    TextView chengshi;
    private int requestCode;
    @ViewById(R.id.baoxian_search)
    Button search;
    @ViewById(R.id.insurance_recycler)
    RecyclerView recyclerView;
    private SPCommonListModel spCommonListModel;
    private List<SPInsurance> list;
    private SPChannelAdapter adapter;

    @ViewById(R.id.baoxian_chepai)
    EditText chepai;
    @ViewById(R.id.baoxian_phone)
    EditText phone;
    private String insurance_id;

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {
        refreshData();
    }

    @Override
    public void initEvent() {
        chengshi.setOnClickListener(this);
        search.setOnClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "保险");
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.baoxian_chengshi:
                Intent pinyinintent = new Intent(this, PinYinActivity_.class);
                requestCode = 10101;
                startActivityForResult(pinyinintent, requestCode);
                break;
            case R.id.baoxian_search:
                if (chengshi.getText().toString().trim().length() == 0 || chengshi.getText().toString().trim() == "") {
                    showToast("请选择城市!");
                    return;
                }
                if (phone.getText().toString().trim().length() == 0 || phone.getText().toString().trim() == "") {
                    showToast("请输入手机号!");
                    return;
                }
                addBaoxian();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String city = data.getStringExtra("city");
        switch (requestCode) {
            case 10101:
                chengshi.setText(city);
                break;
        }
    }

    private void addBaoxian() {
        showLoadingSmallToast();
        RequestParams params = new RequestParams();
        params.add("insurance_id", insurance_id);
        params.add("area", chengshi.getText().toString().trim());
        params.add("car_no", chepai.getText().toString().trim());
        params.add("phone", phone.getText().toString().trim());
        SPInsuranceRequest.comitBaoJia(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                Intent intent = new Intent(SPBaoXianActivity.this, SPSuccessActivity_.class);
                startActivity(intent);
                finish();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
            }
        });
    }

    private void refreshData() {
        showLoadingSmallToast();
        SPInsuranceRequest.takeInurance(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                spCommonListModel = (SPCommonListModel) response;
                if (spCommonListModel.spInsurances != null) {
                    list = spCommonListModel.spInsurances;
                    adapter = new SPChannelAdapter(SPBaoXianActivity.this, list, callBackListener);
                    recyclerView.setAdapter(adapter);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
            }
        });

    }

    private OnclickCallBackListener callBackListener = new OnclickCallBackListener() {

        @Override
        public void clickCallBack(SPInsurance spInsurance) {

            if (spInsurance != null) {
                insurance_id = spInsurance.insuranceid;
            }
        }
    };
}
