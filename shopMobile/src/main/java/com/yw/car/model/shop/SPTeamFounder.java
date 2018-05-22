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
public class SPTeamFounder implements SPModel, Serializable {

    private int status;
    private String join;
    private String teamId;
    private long foundTime;
    private long timeLimit;
    private String headPic;
    private String foundId;
    private String orderId;
    private String surplus;
    private String nickname;
    private String cutPrice;
    private long foundEndTime;
    private String addressRegion;


    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "addressRegion", "address_region",
                "foundTime", "found_time",
                "nickname", "nickname",
                "timeLimit", "time_limit",
                "headPic", "head_pic",
                "foundId", "found_id",
                "orderId", "order_id",
                "surplus", "surplus",
                "join", "join",
                "status", "status",
                "teamId", "team_id",
                "cutPrice", "cut_price",
                "foundEndTime", "found_end_time",
        };
    }

    public long getFoundTime() {
        return foundTime;
    }

    public void setFoundTime(long foundTime) {
        this.foundTime = foundTime;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getAddressRegion() {
        return addressRegion;
    }

    public void setAddressRegion(String addressRegion) {
        this.addressRegion = addressRegion;
    }

    public String getFoundId() {
        return foundId;
    }

    public void setFoundId(String foundId) {
        this.foundId = foundId;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getCutPrice() {
        return cutPrice;
    }

    public void setCutPrice(String cutPrice) {
        this.cutPrice = cutPrice;
    }

    public long getFoundEndTime() {
        return foundEndTime;
    }

    public void setFoundEndTime(long foundEndTime) {
        this.foundEndTime = foundEndTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
