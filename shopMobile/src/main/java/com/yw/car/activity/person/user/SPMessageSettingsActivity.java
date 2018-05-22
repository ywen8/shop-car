package com.yw.car.activity.person.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.adapter.MessageSettingsAdapter;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.person.SPUserRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

import java.util.Arrays;

/**
 * @author liuhao  2017/5/2
 */
@EActivity(com.yw.car.R.layout.message_settings)
public class SPMessageSettingsActivity extends SPBaseActivity {

    private boolean[] mIsOn;               //控制开关是否打开的集合
    private Context mContext;
    private String[] mItemTile;            //小标题
    private boolean[] mIsOnInternet;
    public static final String MESSAGE_SETTINGS_ENABLE_ARRAY = "MessageSettingsEnableArray";

    @ViewById(com.yw.car.R.id.message_settings_lstv)
    ListView messageLstv;

    @Override
    protected void onCreate(Bundle arg0) {
        super.setCustomerTitleMore(true, getString(com.yw.car.R.string.message_settings_title));
        super.onCreate(arg0);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        mContext = this;
        ImageButton settingBtn = (ImageButton) findViewById(com.yw.car.R.id.titlebar_more_btn);
        settingBtn.setVisibility(View.GONE);
        Button clearBtn = (Button) findViewById(com.yw.car.R.id.message_settings_btn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUserRequest.cleanMessage(new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        showToast(msg);
                    }
                }, new SPFailuredListener(SPMessageSettingsActivity.this) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        showToast(msg);
                    }
                });
            }
        });
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
        refreshData();
    }

    private void refreshData() {
        SPUserRequest.getMessageSwitch(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    mIsOnInternet = (boolean[]) response;        //系统消息,物流通知,优惠促销,商品提醒,我的资产,商城好店
                    setSwitch();
                }
            }
        }, new SPFailuredListener(SPMessageSettingsActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showFailedToast(msg);
            }
        });
    }

    private void setSwitch() {
        mIsOn = getMessageSettingsEnableArray(mContext, MESSAGE_SETTINGS_ENABLE_ARRAY);
        if (mIsOnInternet != null) {
            if (mIsOn != mIsOnInternet) {
                saveMessageSettingsEnalbleArray(mContext, mIsOnInternet, MESSAGE_SETTINGS_ENABLE_ARRAY);
                mIsOn = mIsOnInternet;
            }
        }
        MessageSettingsAdapter mAdapter = new MessageSettingsAdapter(mContext, mItemTile, mIsOn);
        messageLstv.setAdapter(mAdapter);
    }

    //保存SharedPreferences的状态值
    public void saveMessageSettingsEnalbleArray(Context context, boolean[] booleanArray, String pref) {
        SharedPreferences prefs = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        for (boolean b : booleanArray)
            jsonArray.put(b);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(pref, jsonArray.toString());
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        saveMessageSettingsEnalbleArray(mContext, mIsOn, MESSAGE_SETTINGS_ENABLE_ARRAY);
        super.onDestroy();
    }

    //得到SharedPreferences中的状态值
    public boolean[] getMessageSettingsEnableArray(Context context, String pref) {
        Resources res = context.getResources();
        mItemTile = res.getStringArray(com.yw.car.R.array.message_settings_item);
        SharedPreferences prefs = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
        boolean[] resArray = new boolean[mItemTile.length];
        try {
            if (prefs.getString(pref, "[]").equals("[]")) {
                JSONArray jsonArray = new JSONArray(prefs.getString(pref, "[]"));
                for (int i = 0; i < jsonArray.length(); i++)
                    resArray[i] = jsonArray.getBoolean(i);
            } else {
                Arrays.fill(resArray, true);         //第一次初始化Preferences,初始数据全部是true
                if (mIsOnInternet != null)
                    saveMessageSettingsEnalbleArray(context, mIsOnInternet, pref);
                else
                    saveMessageSettingsEnalbleArray(context, resArray, pref);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resArray;
    }

}
