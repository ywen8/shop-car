/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015年10月20日 下午7:52:58
 * Description:Activity 基类
 *
 * @version V1.0
 */
package com.yw.car.activity.common;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.soubao.tpshop.utils.SPStringUtils;
import com.yw.car.R;
import com.yw.car.SPMainActivity;
import com.yw.car.activity.person.user.SPLoginActivity;
import com.yw.car.activity.person.user.SPLoginActivity_;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.global.SPSaveData;
import com.yw.car.http.base.SPFailuredListener;
import com.yw.car.http.base.SPSuccessListener;
import com.yw.car.http.shop.SPShopRequest;
import com.yw.car.model.SPProduct;
import com.yw.car.model.order.SPOrder;
import com.yw.car.utils.SPConfirmDialog;
import com.yw.car.utils.SPDialogUtils;
import com.yw.car.utils.SPLoadingSmallDialog;
import com.yw.car.utils.SPServerUtils;
import com.yw.car.widget.CustomToast;

import org.json.JSONObject;

import java.util.List;

/**
 * @author 飞龙
 */
public abstract class SPBaseActivity extends AppCompatActivity implements SPIViewController {

    ImageButton mMoreBtn;
    private String mTtitle;                              //标题栏
    public boolean isBackShow;                           //是否显示返回箭头
    public boolean isHotelShow;
    public JSONObject mDataJson;                         //包含网络请求所有结果
    public boolean isMoreTtitle;                         //是否自定义标题栏:包含更多按钮
    private TextView mTitleTxtv;
    private TextView mRimRight;
    protected ImageView mBackImgv;
    private RelativeLayout closeRl;
    protected RelativeLayout backRl;
    public boolean isCustomerTtitle;                     //是否自定义标题栏
    public boolean isWebViewTitleBar = false;            //是否显示返回箭头
    public LocationClient mLocationClient = null;
    public SPLoadingSmallDialog mLoadingSmallDialog;
    public BDLocationListener myListener = new MyLocationListener();

    /**
     * 是否自定义标题,该方法必须在子Activity的super.onCreate()之前调用,否则无效
     */
    public void setCustomerTitle(boolean backShow, boolean customerTtitle, String title) {
        isCustomerTtitle = customerTtitle;
        isBackShow = backShow;
        mTtitle = title;
    }

    public void setCustoRimTitle(boolean backShow, boolean customerTtitle, String title, boolean hotel) {
        isCustomerTtitle = customerTtitle;
        isBackShow = backShow;
        mTtitle = title;
        isHotelShow = hotel;
    }

    /**
     * 是否自定义标题,该方法必须在子Activity的super.onCreate()之前调用,否则无效
     */
    public void setCustomerTitleMore(boolean backShow, String title) {
        isCustomerTtitle = true;
        isMoreTtitle = true;
        isBackShow = backShow;
        mTtitle = title;
    }

    public void setTitle(String title) {
        mTtitle = title;
        if (mTitleTxtv != null) mTitleTxtv.setText(mTtitle);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);       //强制竖屏
        if (isMoreTtitle || isCustomerTtitle)
            requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);      //自定义标题
        mLocationClient = new LocationClient(this);             //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);            //注册监听函数
        initLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationClient.start();
    }

    public void init() {
        init(-1);
    }

    public void initWithWebTitle() {
        isWebViewTitleBar = true;
        init(R.layout.webview_titlebar);
    }

    public void init(int layoutId) {
        if (layoutId != -1) {
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, layoutId);
            closeRl = (RelativeLayout) findViewById(R.id.close_rl);      //默认隐藏
            closeRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SPBaseActivity.this.finish();
                }
            });
            closeRl.setVisibility(View.GONE);
        } else {
            if (isMoreTtitle)
                getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_more);
            else if (isCustomerTtitle)
                getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);          //设置标题为某个layout
        }
        backRl = (RelativeLayout) findViewById(R.id.titlebar_back_rl);
        mBackImgv = (ImageView) findViewById(R.id.titlebar_back_imgv);
        mRimRight = (TextView) findViewById(R.id.rim_titlbar_right);
        if (isBackShow) {
            backRl.setVisibility(View.VISIBLE);
            backRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isWebViewTitleBar && getWebView() != null && getWebView().canGoBack()) {
                        getWebView().goBack();        //goBack()表示返回WebView
                        closeRl.setVisibility(View.VISIBLE);
                    } else if (isDealBack()) {
                        dealBack();
                    } else {
                        SPBaseActivity.this.finish();
                    }
                }
            });
        } else {
            if (backRl != null) backRl.setVisibility(View.GONE);
        }
        if (isHotelShow) {
            mRimRight.setVisibility(View.VISIBLE);
            mRimRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("SPBaseActivity","-----------");
                }
            });
        } else {
            if (mRimRight != null) mRimRight.setVisibility(View.GONE);
        }
        mMoreBtn = (ImageButton) findViewById(R.id.titlebar_more_btn);
        if (mMoreBtn != null)
            mMoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopuMenu(v);
                }
            });
        mTitleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        if (mTitleTxtv != null) mTitleTxtv.setText(mTtitle);
        initSubViews();
        initEvent();
        initData();
    }

    public void showCloseImageView() {
        if (closeRl != null) closeRl.setVisibility(View.VISIBLE);
    }

    public void showToast(String msg) {
        SPDialogUtils.showToast(this, msg);
    }

    public void showSuccessToast(String txt) {
        CustomToast.getToast().ToastShow(this, txt, R.drawable.success);
    }

    public void showFailedToast(String txt) {
        CustomToast.getToast().ToastShow(this, txt, R.drawable.fail);
    }

    public void showLoadingSmallToast() {
        if (mLoadingSmallDialog != null) {
            mLoadingSmallDialog.dismiss();
            mLoadingSmallDialog = null;
        }
        mLoadingSmallDialog = new SPLoadingSmallDialog(this);
        mLoadingSmallDialog.setCanceledOnTouchOutside(false);
        mLoadingSmallDialog.show();
    }

    public void hideLoadingSmallToast() {
        if (mLoadingSmallDialog != null) mLoadingSmallDialog.dismiss();
    }

    public void showConfirmDialog(String message, String title, final SPConfirmDialog.ConfirmDialogListener confirmDialogListener,
                                  final int actionType) {
        SPConfirmDialog.Builder builder = new SPConfirmDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (confirmDialogListener != null)
                    confirmDialogListener.clickOk(actionType);         //设置你的操作事项
            }
        });
        builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void showToastUnLogin() {
        showToast(getString(R.string.toast_person_unlogin));
    }

    public void toLoginPage() {
        toLoginPage(null);
    }

    public void toLoginPage(String from) {
        Intent loginIntent = new Intent(this, SPLoginActivity_.class);
        if (!SPStringUtils.isEmpty(from)) loginIntent.putExtra(SPLoginActivity.KEY_FROM, from);
        startActivity(loginIntent);
        this.finish();
    }

    /**
     * 再次购买
     */
    public void gotoProductDetail(SPOrder mOrder) {
        List<SPProduct> products = mOrder.getProducts();
        for (SPProduct product : products) {
            SPShopRequest.shopCartGoodsOperation(product.getGoodsID(), product.getItemId(), product.getGoodsNum(), new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    showToast(msg);
                    gotoShopcart();
                }
            }, new SPFailuredListener(SPBaseActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    showToast(msg);
                }
            });
        }
    }

    private void gotoShopcart() {
        Intent intent = new Intent(SPMobileConstants.ACTION_SHOPCART_CHNAGE);
        sendBroadcast(intent);
        Intent shopcartIntent = new Intent(this, SPMainActivity.class);
        shopcartIntent.putExtra(SPMainActivity.SELECT_INDEX, SPMainActivity.INDEX_SHOPCART);
        startActivity(shopcartIntent);
    }

    /**
     * 联系客服
     */
    public void connectCustomer(String qq) {
        try {
            String customerUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq + "&version=1";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(customerUrl)));
        } catch (Exception e) {
            showToast(getString(R.string.no_install_qq));
            e.printStackTrace();
        }
    }

    /**
     * 联系客服
     */
    public void connectCustomer() {
        String qq = SPServerUtils.getCustomerQQ();
        if (!SPStringUtils.isEmpty(qq))
            connectCustomer(qq);
        else
            showToast("暂无联系方式");
    }

    /**
     * 启动webActivity
     */
    public void startWebViewActivity(String url, String title) {
        Intent shippingIntent = new Intent(this, SPCommonWebActivity.class);
        shippingIntent.putExtra(SPMobileConstants.KEY_WEB_URL, url);
        shippingIntent.putExtra(SPMobileConstants.KEY_WEB_TITLE, title);
        startActivity(shippingIntent);
    }

    /**
     * 初始化界面
     */
    abstract public void initSubViews();

    /**
     * 基类函数,初始化数据
     */
    abstract public void initData();

    /**
     * 基类函数,绑定事件
     */
    abstract public void initEvent();

    public void showPopuMenu(View v) {
    }

    /**
     * 处理网络加载过的数据
     */
    public void dealModel() {
    }

    /**
     * 子类实现方法
     */
    public WebView getWebView() {
        return null;
    }

    /**
     * 是否需要自己处理"返回按钮"
     */
    public boolean isDealBack() {
        return false;
    }

    public void dealBack() {
    }

    @Override
    public void gotoLoginPage() {
        toLoginPage();
    }

    @Override
    public void gotoLoginPage(String from) {
        toLoginPage(from);
    }

    /**
     * 重写activity切换方法,消除系统自带动画
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void setTitleBarLienHide(boolean hide) {
        if (hide) findViewById(R.id.titlebar_line).setVisibility(View.GONE);
    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            mLocationClient.stop();                                                    //停止定位
            StringBuffer sb = new StringBuffer(256);                                   //获取定位结果
            sb.append("time : ");
            sb.append(location.getTime());                                             //获取定位时间
            sb.append("\nerror code : ");
            sb.append(location.getLocType());                                          //获取类型类型
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());                                         //获取纬度信息
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());                                        //获取经度信息
            sb.append("\nradius : ");
            sb.append(location.getRadius());                                           //获取定位精准度
            if (location.getLocType() == BDLocation.TypeGpsLocation) {                 //GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());                                        //单位:公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());                              //获取卫星数
                sb.append("\nheight : ");
                sb.append(location.getAltitude());                                     //获取海拔高度信息,单位米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());                                    //获取方向信息,单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());                                      //获取地址信息
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {      //网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());                                      //获取地址信息
                sb.append("\n区县 : ");
                sb.append(location.getDistrict());                                     //获取地址信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());                                    //获取运营商信息
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {      //离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());      //位置语义化信息
            List<Poi> list = location.getPoiList();         // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            if (location == null || location.getCity() == null) {
                SPSaveData.putValue(getApplicationContext(), SPMobileConstants.KEY_LONGITUDE, "");
                SPSaveData.putValue(getApplicationContext(), SPMobileConstants.KEY_LATITUDE, "");
                SPSaveData.putValue(getApplicationContext(), SPMobileConstants.KEY_LOCATION, "");
            } else {
                SPSaveData.putValue(getApplicationContext(), SPMobileConstants.KEY_LONGITUDE, String.valueOf(location.getLongitude()));
                SPSaveData.putValue(getApplicationContext(), SPMobileConstants.KEY_LATITUDE, String.valueOf(location.getLatitude()));
                String city = location.getCity().replace('市', ' ').trim();
                SPSaveData.putValue(getApplicationContext(), SPMobileConstants.KEY_LOCATION, city);
            }
        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选,默认高精度,设置定位模式,高精度,低功耗,仅设备
        option.setCoorType("bd09ll");
        //可选,默认gcj02,设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);
        //可选,默认0,即仅定位一次,设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选,设置是否需要地址信息,默认不需要
        option.setOpenGps(true);
        //可选,默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选,默认false,设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选,默认false,设置是否需要位置语义化结果,可以在BDLocation.getLocationDescribe里得到,结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选,默认false,设置是否需要POI结果,可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选,默认true,定位SDK内部是一个SERVICE,并放到了独立进程,设置是否在stop的时候杀死这个进程,默认不杀死
        option.SetIgnoreCacheException(false);
        //可选,默认false,设置是否收集CRASH信息,默认收集
        option.setEnableSimulateGps(false);
        //可选,默认false,设置是否需要过滤GPS仿真结果,默认需要
        mLocationClient.setLocOption(option);
    }

}
