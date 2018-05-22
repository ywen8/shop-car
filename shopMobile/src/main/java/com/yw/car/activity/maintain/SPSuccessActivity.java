package com.yw.car.activity.maintain;

import android.os.Bundle;

import com.yw.car.R;
import com.yw.car.activity.common.SPBaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by yw on 2018/2/15.
 */
@EActivity(R.layout.activity_success)
public class SPSuccessActivity extends SPBaseActivity {
    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "提交成功");
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }
}
