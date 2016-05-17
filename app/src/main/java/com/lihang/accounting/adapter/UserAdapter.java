package com.lihang.accounting.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lihang.accounting.R;
import com.lihang.accounting.adapter.base.SimpleBaseAdapter;
import com.lihang.accounting.entitys.User;
import com.lihang.accounting.service.UserService;
import com.lihang.accounting.view.SlideMenuItem;

import java.util.List;

/**
 * Created by LiHang on 2016/3/1.
 */
public class UserAdapter extends SimpleBaseAdapter {

    public UserAdapter(Context context) {
        super(context, null);
        setListFromService();
    }

    public void clear() {
        datas.clear();
    }

    public void updateDisplay() {
        notifyDataSetChanged();
    }

    public void updateList() {
        setListFromService();
        updateDisplay();
    }

    private void setListFromService() {
        UserService userService = new UserService(context);
        List<User> list = userService.getNotHideUser();
        setList(list);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.user_list_item, null);
            holder = new Holder();
            holder.user_item_name_tv = (TextView) convertView.findViewById(R.id.user_item_name_tv);
            holder.user_item_icon_iv = (ImageView) convertView.findViewById(R.id.user_item_icon_iv);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        User user= (User) datas.get(position);
        holder.user_item_name_tv.setText(user.getUserName());
        holder.user_item_icon_iv.setImageResource(R.mipmap.lifa);
        return convertView;
    }

    private class Holder{
        TextView user_item_name_tv;
        ImageView user_item_icon_iv;
    }
}
