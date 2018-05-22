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
package com.yw.car.model;

import java.io.Serializable;

/**
 * @author 飞龙
 */
public class SPStoreClass implements SPModel, Serializable {

    private int id;
    private String name;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "id", "sc_id",
                "name", "sc_name"
        };
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
