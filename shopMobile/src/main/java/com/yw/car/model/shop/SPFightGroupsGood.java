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
 * Description: 团购  model
 *
 * @version V1.0
 */
package com.yw.car.model.shop;

import com.yw.car.model.SPModel;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author 飞龙
 */
public class SPFightGroupsGood implements SPModel, Serializable {

    private String price;                         //规格价格
    private String needer;                        //拼团人数
    private String teamId;
    private String itemId;
    private String goodsId;                       //商品ID
    private String actName;                       //拼团名称
    private String storeId;                       //店铺ID
    private int storeCount;                       //库存
    private String shareImg;                      //分享图片
    private String salesSum;                      //已拼人数
    private String goodsName;                     //商品名称
    private String teamPrice;                     //拼团价格
    private String shopPrice;                     //单买价格
    private String shareDesc;                     //分享描述
    private String statusDesc;                    //活动状态
    private JSONObject goodsObj;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "goodsId", "goods_id",
                "teamId", "team_id",
                "teamPrice", "team_price",
                "needer", "needer",
                "itemId", "item_id",
                "actName", "act_name",
                "goodsName", "goods_name",
                "shopPrice", "shop_price",
                "price", "price",
                "salesSum", "sales_sum",
                "shareImg", "share_img",
                "shareDesc", "share_desc",
                "storeId", "store_id",
                "storeCount", "store_count",
                "statusDesc", "front_status_desc",
                "goodsObj", "spec_goods_price",
        };
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getTeamPrice() {
        return teamPrice;
    }

    public void setTeamPrice(String teamPrice) {
        this.teamPrice = teamPrice;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getNeeder() {
        return needer;
    }

    public void setNeeder(String needer) {
        this.needer = needer;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShareImg() {
        return shareImg;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg;
    }

    public String getShareDesc() {
        return shareDesc;
    }

    public void setShareDesc(String shareDesc) {
        this.shareDesc = shareDesc;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public JSONObject getGoodsObj() {
        return goodsObj;
    }

    public void setGoodsObj(JSONObject goodsObj) {
        this.goodsObj = goodsObj;
    }

    public void setSalesSum(String salesSum) {
        this.salesSum = salesSum;
    }

    public String getSalesSum() {
        return salesSum;
    }

    public int getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(int storeCount) {
        this.storeCount = storeCount;
    }

}
