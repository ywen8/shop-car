package com.yw.car.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yw.car.activity.common.SPImagePreviewActivity_;
import com.yw.car.global.SPMobileApplication;
import com.yw.car.model.shop.SPView;
import com.yw.car.widget.SimpleVideoView;

import java.util.List;

/**
 * Created by zw on 2018/1/8
 */
public class SPBannerAdaper extends PagerAdapter {

    private Context context;
    private List<String> imgUrls;
    private List<SPView> viewList;

    public SPBannerAdaper(Context context, List<SPView> viewList, List<String> imgUrls) {
        this.context = context;
        this.viewList = viewList;
        this.imgUrls = imgUrls;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        final SPView spView = viewList.get(position);
        View view = spView.getView();
        if (spView.isVideo()) {
            SimpleVideoView videoView = (SimpleVideoView) view.findViewById(com.yw.car.R.id.videoview);
            videoView.setVideoUri(Uri.parse(spView.getUrlPath()));
            if (spView.getDrawable() != null)
                videoView.setInitPicture(spView.getDrawable());
        } else {
            PhotoView photoView = (PhotoView) view;
            photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(context).load(spView.getUrlPath()).asBitmap().fitCenter().placeholder(com.yw.car.R.drawable.icon_product_null)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(photoView);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SPMobileApplication.getInstance().setImageUrl(imgUrls);
                    Intent previewIntent = new Intent(context, SPImagePreviewActivity_.class);
                    int pos = imgUrls.indexOf(spView.getUrlPath());
                    previewIntent.putExtra("index", pos);
                    context.startActivity(previewIntent);
                }
            });
        }
        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return viewList.get(position).getView();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
