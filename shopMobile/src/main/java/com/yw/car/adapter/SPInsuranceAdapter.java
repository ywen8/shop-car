package com.yw.car.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yw.car.R;
import com.yw.car.model.car.SPMaintain;
import com.yw.car.service.OnFragmentInteractionListener;
import com.yw.car.utils.SPUtils;

import java.util.List;

/**
 * Created by yw on 2018/2/15.
 */

public class SPInsuranceAdapter extends RecyclerView.Adapter<SPInsuranceAdapter.MyViewHolder> {

    private List<SPMaintain> list;
    Context mContext;
    private OnFragmentInteractionListener listener;

    public SPInsuranceAdapter(Context mContext, List<SPMaintain> list, OnFragmentInteractionListener listener) {
        this.list = list;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_insurance_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SPMaintain maintain = list.get(position);
        Glide.with(mContext).load(SPUtils.getImageUrl(maintain.getInformationImg())).asBitmap().fitCenter()
                .placeholder(com.yw.car.R.drawable.icon_product_null).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.imageView);
        holder.title.setText(maintain.getInformationName() + "");
        holder.time.setText(maintain.getBusinessHours() + "");
        holder.phone.setText(maintain.getPhone() + "");
        holder.address.setText(maintain.getAddress() + "");
        holder.ph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + maintain.getPhone());
                intent.setData(data);
                mContext.startActivity(intent);
            }
        });
        holder.service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFragmentInteraction(maintain);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView address;
        TextView time;
        TextView phone;
        TextView ph;
        TextView service;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.insurance_item_image);
            title = (TextView) itemView.findViewById(R.id.insurance_item_title);
            address = (TextView) itemView.findViewById(R.id.insurance_item_address);
            time = (TextView) itemView.findViewById(R.id.insurance_item_time);
            phone = (TextView) itemView.findViewById(R.id.insurance_item_phone);
            ph = (TextView) itemView.findViewById(R.id.insurance_item_ph);
            service = (TextView) itemView.findViewById(R.id.insurance_item_service);
        }
    }

}
