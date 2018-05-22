package com.yw.car.activity.maintain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yw.car.R;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.adapter.SPNianFenAdapter;
import com.yw.car.entity.SPCommonListModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yw on 2018/2/24.
 */
@EActivity(R.layout.activity_cartype)
public class SPNianFenActivity extends SPBaseActivity {
    @ViewById(R.id.chexing)
    RecyclerView chexing;
    private SPCommonListModel mCommonListModel;
    private List<String> list;
    private SPNianFenAdapter adapter;

    @Override
    public void initSubViews() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "选择年份");
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initData() {
        refreshData();
    }

    private void refreshData() {
        List<String> strings = new ArrayList<>();
        strings.add("2005");
        strings.add("2006");
        strings.add("2007");
        strings.add("2008");
        strings.add("2009");
        strings.add("2010");
        strings.add("2011");
        strings.add("2012");
        strings.add("2013");
        strings.add("2014");
        strings.add("2015");
        strings.add("2016");
        strings.add("2017");
        strings.add("2018");
        setparticular(strings);
    }

    private void setparticular(List<String> ls) {
        adapter = new SPNianFenAdapter(ls, this, listener);
        chexing.setAdapter(adapter);
    }

    @Override
    public void initEvent() {
        chexing.setLayoutManager(new LinearLayoutManager(this));
    }

    private SPNianFenAdapter.CarTypeItemListener listener = new SPNianFenAdapter.CarTypeItemListener() {
        @Override
        public void carTypeItemClick(String name) {
            Intent intent = new Intent();
            intent.putExtra("nianfen", name);
            setResult(10104, intent);
            finish();
        }
    };
}
