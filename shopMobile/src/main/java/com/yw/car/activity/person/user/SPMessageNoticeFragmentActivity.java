package com.yw.car.activity.person.user;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.fragment.SPMessageNoticeFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(com.yw.car.R.layout.message_notice_fragment_activity)
public class SPMessageNoticeFragmentActivity extends SPBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Resources res = getResources();
        String[] title = res.getStringArray(com.yw.car.R.array.message_settings_item);
        Intent intert = getIntent();
        int id = intert.getIntExtra("fragmentIndex", -1);
        if (id >= 0) super.setCustomerTitle(true, true, title[id]);
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (id >= 0) {
            ft.replace(com.yw.car.R.id.frame_content, new SPMessageNoticeFragment().newInstance(this, id));      //这里是指定跳转到指定的fragment
            ft.commit();
        }
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
    }

}