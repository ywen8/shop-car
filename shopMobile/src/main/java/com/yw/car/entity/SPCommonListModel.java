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
 * Date: @date 2015年10月27日 下午9:14:42
 * Description: 广告  model
 *
 * @version V1.0
 */
package com.yw.car.entity;

import com.yw.car.model.SPAffiche;
import com.yw.car.model.SPHomeBanners;
import com.yw.car.model.SPPlugin;
import com.yw.car.model.SPProduct;
import com.yw.car.model.SPRimHeadlin;
import com.yw.car.model.SPServiceConfig;
import com.yw.car.model.car.SPAssessCity;
import com.yw.car.model.car.SPCarBrand;
import com.yw.car.model.car.SPCheXing;
import com.yw.car.model.car.SPInsurance;
import com.yw.car.model.car.SPMaintain;
import com.yw.car.model.car.SPMotorcycle;
import com.yw.car.model.car.SPPin;
import com.yw.car.model.car.SPProvince;
import com.yw.car.model.car.SPRecord;
import com.yw.car.model.car.SPResult;
import com.yw.car.model.car.SPTown;
import com.yw.car.model.car.SPXiLie;
import com.yw.car.model.shop.SPBrand;
import com.yw.car.model.shop.SPFlashSale;
import com.yw.car.model.shop.SPFlashTime;
import com.yw.car.model.shop.SPGroup;
import com.yw.car.model.shop.SPStore;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author 飞龙
 */
public class SPCommonListModel implements Serializable {

    public SPHomeBanners ad;                       //广告模型
    public SPHomeBanners ad1;                      //广告模型
    public List<SPGroup> groups;                   //团购
    public List<SPStore> stores;                   //店铺信息
    public List<SPBrand> brands;                   //品牌列表
    public List<SPHomeBanners> ads;                //首页广告
    public List<SPProduct> products;               //商品列表
    public List<SPHomeBanners> banners;            //广告轮播列表
    public List<SPFlashTime> flashTimes;           //抢购时间节点列表
    public List<SPFlashSale> flashSales;           //秒杀
    public Map<String, SPPlugin> payPluginMap;     //支付插件
    public Map<String, SPPlugin> loginPlugins;     //登录插件
    public List<SPServiceConfig> serviceConfigs;   //服务端配置
    public SPRimHeadlin newAds;                    //周边精彩头条
    public List<SPCarBrand> carbrands;                          //小保养车品牌
    public List<SPMotorcycle> carmodels;                  //车型号
    public List<SPMaintain> maintains;
    public List<SPTown> towns;
    public List<SPRecord> records;
    public List<SPProvince> provinces;
    public List<SPAssessCity> assessCities;
    public SPPin pins;
    public List<SPXiLie> xiLies;
    public List<SPCheXing> cheXings;
    public SPResult result;
    public List<SPInsurance> spInsurances;
    public List<SPAffiche> affiches;
}
