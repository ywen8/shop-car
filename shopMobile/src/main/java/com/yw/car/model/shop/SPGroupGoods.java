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
 * Description: 促销活动信息  model
 *
 * @version V1.0
 */
package com.yw.car.model.shop;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * @author zw
 */
public class SPGroupGoods implements SPModel, Serializable {

    private String teamId;
    private String itemId;
    private String actName;
    private String goodsId;
    private String salesSum;
    private String teamPrice;
    private String goodsName;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "teamId", "team_id",
                "actName", "act_name",
                "teamPrice", "team_price",
                "goodsName", "goods_name",
                "goodsId", "goods_id",
                "itemId", "item_id",
                "salesSum", "sales_sum",
        };
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSalesSum() {
        return salesSum;
    }

    public void setSalesSum(String salesSum) {
        this.salesSum = salesSum;
    }

    public String getTeamPrice() {
        return teamPrice;
    }

    public void setTeamPrice(String teamPrice) {
        this.teamPrice = teamPrice;
    }

}
