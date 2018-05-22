package com.yw.car.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.yw.car.activity.person.SPTypeSelectActivity;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.category.SPCategoryRequest;
import com.yw.car.model.SPCategory;

import org.json.JSONObject;

import java.util.List;
/**
 * Created by admin on 2016/7/9
 */
public class SPTypeUtil {

    private Context context;
    private Handler handler;

    public SPTypeUtil(final Context context, final Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    /***
     * 初始化一级分类
     */
    public void initFirstCategory() {
        SPCategoryRequest.getCategory(0, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                try {
                    if (response != null) {
                        JSONObject object = (JSONObject) response;
                        if (object.has("categorys")) {
                            List<SPCategory> categorys = (List<SPCategory>) object.get("categorys");
                            Message message = handler.obtainMessage(SPTypeSelectActivity.LEVEL_FIRST);
                            message.obj = categorys;
                            handler.sendMessage(message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                SPDialogUtils.showToast(context, msg);
            }
        });
    }

    /**
     * 根据一级分类获取下级分类
     */
    public void initChildrenCategory(final int parentID, final int level) {
        SPCategoryRequest.getCategory(parentID, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                try {
                    if (response != null) {
                        JSONObject object = (JSONObject) response;
                        if (object.has("categorys")) {
                            List<SPCategory> categorys = (List<SPCategory>) object.get("categorys");
                            Message message = handler.obtainMessage(level);
                            message.obj = categorys;
                            handler.sendMessage(message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                SPDialogUtils.showToast(context, msg);
            }
        });
    }

}
