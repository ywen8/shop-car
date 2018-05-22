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

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * @author zw
 */
public class SPView {

    private View view;                                   //视图对象
    private String urlPath;                              //视频(图片)地址
    private boolean isVideo;                             //是否是视频
    private Drawable drawable;                           //视频图片

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

}
