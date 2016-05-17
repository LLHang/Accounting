package com.lihang.accounting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lihang.accounting.R;

/**
 * Created by LiHang on 2016/2/25.
 */
public class MyAdapter extends BaseAdapter {
    private Context context;
    private Integer[] imageInteger = {
            R.mipmap.lifa,
            R.mipmap.lvyoudujia,
            R.mipmap.majiang,
            R.mipmap.icon_zhichu_type_canyin,
            R.mipmap.icon_zhichu_type_gouwu,
            R.mipmap.icon_zhichu_type_jiaotong
    };
    private String[] imageString = new String[6];

    public MyAdapter(Context context) {
        this.context = context;
        imageString[0] = context.getString(R.string.grid_add_payout);
        imageString[1] = context.getString(R.string.grid_query_payout);
        imageString[2] = context.getString(R.string.grid_accountbook);
        imageString[3] = context.getString(R.string.grid_statistics);
        imageString[4] = context.getString(R.string.grid_category);
        imageString[5] = context.getString(R.string.grid_user);

    }
    public int getCount() {
        return imageInteger.length;
    }

    @Override
    public Object getItem(int position) {
        return imageString[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.main_body_item, null);
            holder = new Holder();
            holder.icon_iv = (ImageView) convertView.findViewById(R.id.main_body_item_icon_imageview);
            holder.name_tv = (TextView) convertView.findViewById(R.id.main_body_item_textview);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.icon_iv.setImageResource(imageInteger[position]);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);
        holder.icon_iv.setLayoutParams(params);
        holder.icon_iv.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.name_tv.setText(imageString[position]);


        return convertView;
    }

    private class Holder {
        ImageView icon_iv;
        TextView name_tv;
    }
}
