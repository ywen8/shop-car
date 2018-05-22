package com.yw.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yw.car.model.shop.SPFilter;
import com.yw.car.model.shop.SPFilterItem;
import com.yw.car.widget.tagview.Tag;
import com.yw.car.widget.tagview.TagListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/23
 */
public class SPProductListFilterAdapter extends BaseAdapter {

    private Context mContext;
    private List<SPFilter> mFilters;
    private TagListView.OnTagClickListener mTagClickListener;

    public SPProductListFilterAdapter(Context context, TagListView.OnTagClickListener tagClickListener) {
        this.mContext = context;
        this.mTagClickListener = tagClickListener;
    }

    public void setData(List<SPFilter> filters) {
        if (filters == null) return;
        this.mFilters = filters;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mFilters == null) return 0;
        return mFilters.size();
    }

    @Override
    public Object getItem(int position) {
        if (mFilters == null) return null;
        return mFilters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(com.yw.car.R.layout.fragment_spproduct_list_filter_item, parent, false);
            holder.tagListv = (TagListView) convertView.findViewById(com.yw.car.R.id.filter_taglstv);
            holder.tagListv.setTagViewBackgroundRes(com.yw.car.R.drawable.tag_button_bg_unchecked);
            holder.tagListv.setTagViewTextColorRes(com.yw.car.R.color.color_font_gray);
            holder.tagListv.setOnTagClickListener(mTagClickListener);
            holder.titleTxtv = (TextView) convertView.findViewById(com.yw.car.R.id.filter_title_txtv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == -1)         //选中的行:背景白色,否则灰色
            convertView.setBackgroundResource(com.yw.car.R.drawable.list_row_pressed);
        else
            convertView.setBackgroundResource(com.yw.car.R.drawable.list_row_normal);
        SPFilter filter = mFilters.get(position);
        List<Tag> tags = getTags(filter);
        holder.tagListv.setTags(tags);
        holder.titleTxtv.setText(filter.getName());
        return convertView;
    }

    private List<Tag> getTags(SPFilter filter) {
        if (filter == null) return null;
        List<Tag> mTags = new ArrayList<>();
        int size = filter.getItems().size();
        for (int i = 0; i < size; i++) {
            SPFilterItem filterItem = filter.getItems().get(i);
            Tag tag = new Tag();
            tag.setId(i);
            if (filterItem.isHighLight())
                tag.setChecked(true);
            else
                tag.setChecked(false);
            tag.setValue(filterItem.getHref());
            tag.setTitle(filterItem.getName());
            mTags.add(tag);
        }
        return mTags;
    }

    class ViewHolder {
        TextView titleTxtv;
        TagListView tagListv;
    }

}
