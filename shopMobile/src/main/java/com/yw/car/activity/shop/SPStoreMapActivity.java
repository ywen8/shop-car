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
 * Date: @date 2015年11月12日 下午8:08:13
 * Description: 店铺街道
 *
 * @version V1.0
 */
package com.yw.car.activity.shop;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.soubao.tpshop.utils.SPStringUtils;
import com.yw.car.R;
import com.yw.car.activity.common.SPBaseActivity;
import com.yw.car.common.SPMobileConstants;
import com.yw.car.global.SPSaveData;
import com.yw.car.model.shop.SPStore;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * @author zw
 */
@EActivity(R.layout.store_map)
public class SPStoreMapActivity extends SPBaseActivity {

    SPStore store;
    MapView mapView;
    BaiduMap mBaiduMap;
    private String lng;
    private String lat;
    private LatLng endPt;
    private String storeName;
    private BitmapDescriptor bd;
    private List<String> mPackageNames = new ArrayList<>();
    public final String BAIDU_PACKAGE_NAME = "com.baidu.BaiduMap";
    public final String GAODE_PACKAGE_NAME = "com.autonavi.minimap";

    @Override
    protected void onCreate(Bundle bundle) {
        setCustomerTitle(true, true, getString(R.string.store_map));
        super.onCreate(bundle);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void initSubViews() {
        store = (SPStore) getIntent().getSerializableExtra("store");
        if (store == null) {
            showToast("店铺异常");
            finish();
            return;
        }
        storeName = (SPStringUtils.isEmpty(store.getStoreName())) ? "店铺名称异常" : store.getStoreName();
        mapView = (MapView) findViewById(R.id.mapView);
        lng = SPSaveData.getString(this, SPMobileConstants.KEY_LONGITUDE);
        lat = SPSaveData.getString(this, SPMobileConstants.KEY_LATITUDE);
        endPt = new LatLng(Double.valueOf(store.getLat()), Double.valueOf(store.getLon()));
        initMapStatus();
        initOverlay();
    }

    @Override
    public void initEvent() {
    }

    private void initMapStatus() {
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(endPt).zoom(17);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    public void initOverlay() {
        View view = LayoutInflater.from(this).inflate(R.layout.store_map_info, null);
        TextView storeNameTxt = (TextView) view.findViewById(R.id.store_name_txt);
        Button naviBtn = (Button) view.findViewById(R.id.navi_btn);
        naviBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SPStringUtils.isEmpty(lng) || SPStringUtils.isEmpty(lat)) {
                    showToast("无法获取当前位置");
                    return;
                }
                if (haveBaiduMap()) {
                    openBaiduMapToGuide();
                } else if (haveGaodeMap()) {
                    openGaodeMapToGuide();
                } else {
                    openBrowserToGuide();
                }
            }
        });
        storeNameTxt.setText(store.getStoreName());
        InfoWindow mInfoWindow = new InfoWindow(view, endPt, -70);
        mBaiduMap.showInfoWindow(mInfoWindow);
        bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
        MarkerOptions markerOptions = new MarkerOptions().position(endPt).icon(bd).zIndex(16).draggable(true);
        mBaiduMap.addOverlay(markerOptions);
    }

    private void initPackageManager() {
        List<PackageInfo> packageInfos = getPackageManager().getInstalledPackages(0);
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                mPackageNames.add(packageInfos.get(i).packageName);
            }
        }
    }

    public boolean haveGaodeMap() {
        initPackageManager();
        return mPackageNames.contains(GAODE_PACKAGE_NAME);
    }

    public boolean haveBaiduMap() {
        initPackageManager();
        return mPackageNames.contains(BAIDU_PACKAGE_NAME);
    }

    private void openBaiduMapToGuide() {
        NaviParaOption para = new NaviParaOption();
        para.startPoint(new LatLng(Double.valueOf(lat), Double.valueOf(lng)));
        para.startName("起点");
        para.endPoint(new LatLng(Double.valueOf(store.getLat()), Double.valueOf(store.getLon())));
        para.endName("终点");
        try {
            BaiduMapNavigation.openBaiduMapNavi(para, getContext());
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showToast("您尚未安装百度地图或地图版本过低");
        }
    }

    private void openGaodeMapToGuide() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        String url = "androidamap://route?sourceApplication=amap&slat=" + lat + "&slon=" + lng
                + "&dlat=" + store.getLat() + "&dlon=" + store.getLon() + "&dname=" + storeName + "&dev=0&t=1";
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        startActivity(intent);
    }


    private void openBrowserToGuide() {
        String url = "http://uri.amap.com/navigation?to=" + store.getLat() + "," + store.getLon() + "," +
                storeName + "&mode=car&policy=1&src=mypage&coordinate=gaode&callnative=0";
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView = null;
        mBaiduMap.setMyLocationEnabled(false);
        bd.recycle();
    }

}
