package com.yw.car.widget.swipetoloadlayout;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class RefreshHeaderView extends FrameLayout implements SwipeRefreshTrigger, SwipeTrigger {

    private TextView tvTip;
    private ImageView iconRefresh;
    private AnimationDrawable animationDrawable;

    public RefreshHeaderView(Context context) {
        super(context);
        init(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(com.yw.car.R.layout.layout_refresh_header, this);
        tvTip = (TextView) findViewById(com.yw.car.R.id.tv_tip);
        iconRefresh = (ImageView) findViewById(com.yw.car.R.id.icon_refresh);
    }

    @Override
    public void onRefresh() {
        //下拉到一定位置松开之后调用此方法
        tvTip.setText("正在刷新...");
        iconRefresh.setRotationX(0);
        iconRefresh.setImageResource(com.yw.car.R.drawable.anim_refresh);
        animationDrawable = (AnimationDrawable) iconRefresh.getDrawable();
        animationDrawable.start();
        Log.i("info", "onRefresh");
    }

    @Override
    public void onPrepare() {
        //下拉之前调用此方法
        tvTip.setText("下拉刷新");
        Log.i("info", "onPrepare");
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled >= getHeight()) {       //当前Y轴偏移量大于控件高度时,标识下拉到界限,显示“释放立即刷新”
                iconRefresh.setRotationX(0);
                tvTip.setText("释放立即刷新");
                tvTip.postInvalidate();
            } else {      //未达到偏移量
                tvTip.setText("下拉刷新");
                iconRefresh.setRotationX(0);
            }
        }
    }

    @Override
    public void onRelease() {
        //达到一定滑动距离,松开刷新时调用
        Log.i("info", "onRelease");
    }

    @Override
    public void onComplete() {
        //加载完成之后调用此方法
        tvTip.setText("刷新完成");
        animationDrawable.stop();
        Log.i("info", "onComplete");
    }

    @Override
    public void onReset() {
        //重置
        tvTip.setText("下拉刷新");
        iconRefresh.setImageResource(com.yw.car.R.drawable.refresh1);
        Log.i("info", "onReset");
    }

}
