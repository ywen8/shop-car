package com.yw.car.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yw.car.R;
import com.yw.car.model.car.SPInsurance;

import java.util.List;

/**
 * Created by yw on 2018/2/27.
 */

public class SPChannelAdapter extends RecyclerView.Adapter<SPChannelAdapter.MyViewHolder> {
    private List<SPInsurance> list;
    private Context mContext;
    private int selectID;
    private OnclickCallBackListener onclickCallBackListener;


    public SPChannelAdapter(Context mContext, List<SPInsurance> list, OnclickCallBackListener onclickCallBackListener) {
        this.list = list;
        this.mContext = mContext;
        this.onclickCallBackListener = onclickCallBackListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_channel_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final SPInsurance spInsurance = list.get(position);
        if (selectID == position) {
            onclickCallBackListener.clickCallBack(spInsurance);
            holder.imageView.setBackgroundResource(R.drawable.img_pressed_true);
        } else {
            holder.imageView.setBackgroundResource(R.drawable.img_pressed_false);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectID = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.channel_image);
        }
    }

    public interface OnclickCallBackListener {
        void clickCallBack(SPInsurance spInsurance);
    }

}
