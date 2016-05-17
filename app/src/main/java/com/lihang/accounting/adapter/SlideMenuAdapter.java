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
import com.lihang.accounting.adapter.base.SimpleBaseAdapter;
import com.lihang.accounting.view.SlideMenuItem;

import java.util.List;

/**
 * Created by LiHang on 2016/2/25.
 */
public class SlideMenuAdapter extends SimpleBaseAdapter {
    private Context context;

    public SlideMenuAdapter(Context context, List datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.slide_menu_item, null);
            holder = new Holder();
            holder.textView = (TextView) convertView.findViewById(R.id.slide_menu_list_item_tv);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        SlideMenuItem item = (SlideMenuItem) datas.get(position);
        holder.textView.setText(item.getTitle());
        return convertView;
    }

    private class Holder{
        TextView textView;
    }

}
