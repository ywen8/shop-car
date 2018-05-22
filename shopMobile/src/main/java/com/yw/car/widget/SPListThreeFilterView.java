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
 * Date: @date 2015年11月14日 下午8:17:18
 * Description: 商品列表排序, 筛选 标题栏
 *
 * @version V1.0
 */
package com.yw.car.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yw.car.entity.FilterSortMode;

import java.util.ArrayList;
import java.util.List;

public class SPListThreeFilterView extends FrameLayout implements View.OnClickListener {

    int mLastSortId;                      //上一次选中的ID
    View sortSecLyout;
    View sortFirstLyout;
    View sortThirdLyout;
    TextView sortSecText;
    TextView sortThirdTxtv;
    TextView sortFirstText;
    ImageView sortStatusSecImgv;
    ImageView sortStatusFirstImgv;
    ImageView sortStatusThirdImgv;
    private OnSortClickListener onSortClickListener;
    List<FilterSortMode[]> datas = new ArrayList<>();

    public SPListThreeFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //团购
        FilterSortMode[] groupFilter = new FilterSortMode[]{
                new FilterSortMode(0, "默认", ""),
                new FilterSortMode(1, "最新", "new"),
                new FilterSortMode(1, "评论", "comment"),
        };
        //金豆商城
        FilterSortMode[] integralFilter = new FilterSortMode[]{
                new FilterSortMode(0, "默认", ""),
                new FilterSortMode(1, "兑换量", "num"),
                new FilterSortMode(1, "金豆值", "integral"),
        };
        //店铺街
        FilterSortMode[] storeStreetFilter = new FilterSortMode[]{
                new FilterSortMode(1, "类型", "class"),
                new FilterSortMode(1, "区域", "region"),
                new FilterSortMode(1, "销量", "sort"),
        };
        datas.add(groupFilter);
        datas.add(integralFilter);
        datas.add(storeStreetFilter);
        View view = LayoutInflater.from(context).inflate(com.yw.car.R.layout.list_three_filter_view, this);
        sortFirstLyout = view.findViewById(com.yw.car.R.id.sort_first_lyout);
        sortFirstText = (TextView) view.findViewById(com.yw.car.R.id.sort_first_txt);
        sortFirstText.setTextColor(getResources().getColor(com.yw.car.R.color.light_red));
        sortStatusFirstImgv = (ImageView) view.findViewById(com.yw.car.R.id.sort_status_first_imgv);
        sortFirstLyout.setOnClickListener(this);
        sortSecLyout = view.findViewById(com.yw.car.R.id.sort_sec_lyout);
        sortSecText = (TextView) view.findViewById(com.yw.car.R.id.sort_sec_txt);
        sortStatusSecImgv = (ImageView) view.findViewById(com.yw.car.R.id.sort_status_sec_imgv);
        sortSecLyout.setOnClickListener(this);
        sortThirdLyout = view.findViewById(com.yw.car.R.id.sort_third_layout);
        sortThirdTxtv = (TextView) view.findViewById(com.yw.car.R.id.sort_third_txt);
        sortStatusThirdImgv = (ImageView) view.findViewById(com.yw.car.R.id.sort_status_third_imgv);
        sortThirdLyout.setOnClickListener(this);
        view.invalidate();
    }

    public void updateType(FilterSortType sortType) {
        FilterSortMode[] modes = datas.get(sortType.value);
        for (int i = 0; i < 3; i++) {
            FilterSortMode filterMode = modes[i];
            int showIcon = filterMode.showIcon;
            String text = filterMode.text;
            String sort = filterMode.sortType;
            switch (i) {
                case 0:
                    sortFirstText.setText(text);
                    sortStatusFirstImgv.setVisibility((showIcon == 1 ? View.VISIBLE : View.INVISIBLE));
                    sortFirstLyout.setTag(com.yw.car.R.id.tag_index, 0);
                    sortFirstLyout.setTag(com.yw.car.R.id.tag_content, sort);
                    break;
                case 1:
                    sortSecText.setText(text);
                    sortStatusSecImgv.setVisibility((showIcon == 1 ? View.VISIBLE : View.INVISIBLE));
                    sortSecLyout.setTag(com.yw.car.R.id.tag_index, 1);
                    sortSecLyout.setTag(com.yw.car.R.id.tag_content, sort);
                    break;
                case 2:
                    sortThirdTxtv.setText(text);
                    sortStatusThirdImgv.setVisibility((showIcon == 1 ? View.VISIBLE : View.INVISIBLE));
                    sortThirdLyout.setTag(com.yw.car.R.id.tag_index, 2);
                    sortThirdLyout.setTag(com.yw.car.R.id.tag_content, sort);
                    break;
            }
        }
    }

    /**
     * 排序类型
     */
    public enum FilterSortType {
        GROUP(0), INTEGRAL_MALL(1), STORE_STREET(2);
        private int value;

        FilterSortType(int v) {
            this.value = v;
        }
    }

    @Override
    public void onClick(View v) {
        if (onSortClickListener == null) return;
        onSortClickListener.onFilterClick(v.getTag(com.yw.car.R.id.tag_content).toString());
        refreshSortViewState(Integer.valueOf(v.getTag(com.yw.car.R.id.tag_index).toString()));
        mLastSortId = v.getId();
    }

    private void refreshSortViewState(int index) {
        sortFirstText.setTextColor(getResources().getColor(com.yw.car.R.color.sort_normal_text_color));
        sortSecText.setTextColor(getResources().getColor(com.yw.car.R.color.sort_normal_text_color));
        sortThirdTxtv.setTextColor(getResources().getColor(com.yw.car.R.color.sort_normal_text_color));
        sortStatusFirstImgv.setImageResource(com.yw.car.R.drawable.icon_filter_sort_normal);
        sortStatusSecImgv.setImageResource(com.yw.car.R.drawable.icon_filter_sort_normal);
        sortStatusThirdImgv.setImageResource(com.yw.car.R.drawable.icon_filter_sort_normal);
        switch (index) {
            case 0:
                sortFirstText.setTextColor(getResources().getColor(com.yw.car.R.color.light_red));
                sortStatusFirstImgv.setImageResource(com.yw.car.R.drawable.icon_filter_sort_checked);
                break;
            case 1:
                sortSecText.setTextColor(getResources().getColor(com.yw.car.R.color.light_red));
                sortStatusSecImgv.setImageResource(com.yw.car.R.drawable.icon_filter_sort_checked);
                break;
            case 2:
                sortThirdTxtv.setTextColor(getResources().getColor(com.yw.car.R.color.light_red));
                sortStatusThirdImgv.setImageResource(com.yw.car.R.drawable.icon_filter_sort_checked);
                break;
        }
    }

    /***
     * 排序item点击
     */
    public interface OnSortClickListener {
        void onFilterClick(String sortType);
    }

    public void setOnSortClickListener(OnSortClickListener onSortClickListener) {
        this.onSortClickListener = onSortClickListener;
    }

}
