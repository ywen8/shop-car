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
 * Description: 商品详情, 评论标题
 *
 * @version V1.0
 */
package com.yw.car.entity;

import com.yw.car.model.SPModel;

import java.io.Serializable;

/**
 * @author 飞龙
 */
public class SPCommentTitleModel implements SPModel, Serializable {

    private int lowSum;                  //差评数
    private int imgSum;                  //晒图数
    private int highSum;                 //好评数
    private int totalSum;                //全部评论数
    private int centerSum;               //中评数
    private double lowRate;              //差评率
    private double highRate;             //好评率
    private double centerRate;           //中评率

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "totalSum", "total_sum",
                "highSum", "high_sum",
                "centerSum", "center_sum",
                "lowSum", "low_sum",
                "imgSum", "img_sum",
                "highRate", "high_rate",
                "centerRate", "center_rate",
                "lowRate", "low_rate",
        };
    }

    public int getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(int totalSum) {
        this.totalSum = totalSum;
    }

    public double getHighRate() {
        return highRate;
    }

    public void setHighRate(double highRate) {
        this.highRate = highRate;
    }

    public int getHighSum() {
        return highSum;
    }

    public void setHighSum(int highSum) {
        this.highSum = highSum;
    }

    public int getCenterSum() {
        return centerSum;
    }

    public void setCenterSum(int centerSum) {
        this.centerSum = centerSum;
    }

    public int getLowSum() {
        return lowSum;
    }

    public void setLowSum(int lowSum) {
        this.lowSum = lowSum;
    }

    public int getImgSum() {
        return imgSum;
    }

    public void setImgSum(int imgSum) {
        this.imgSum = imgSum;
    }

    public double getCenterRate() {
        return centerRate;
    }

    public void setCenterRate(double centerRate) {
        this.centerRate = centerRate;
    }

    public double getLowRate() {
        return lowRate;
    }

    public void setLowRate(double lowRate) {
        this.lowRate = lowRate;
    }

}
