package com.lihang.accounting.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lihang.accounting.R;
import com.lihang.accounting.adapter.base.SimpleBaseAdapter;
import com.lihang.accounting.entitys.AccountBook;
import com.lihang.accounting.service.AccountBookService;

import java.util.List;

/**
 * Created by LiHang on 2016/3/1.
 */
public class AccountBookSelectAdapter extends SimpleBaseAdapter {
    private AccountBookService accountBookService;
    public AccountBookSelectAdapter(Context context) {
        super(context, null);
        accountBookService = new AccountBookService(context);
        setListFromService();
    }

    private void setListFromService() {
        List<AccountBook> list = accountBookService.getNotHideAccountBook();
        setList(list);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.account_book_select_list_item, null);
            holder = new Holder();
            holder.account_book_item_icon_iv = (ImageView) convertView.findViewById(R.id.account_book_item_icon_iv);
            holder.account_book_item_name_tv = (TextView) convertView.findViewById(R.id.account_book_item_name_tv);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        AccountBook accountBook= (AccountBook) datas.get(position);
        if (accountBook.getIsDefault() == 1) {
            holder.account_book_item_icon_iv.setImageResource(R.mipmap.isdefault);
        }else {
            holder.account_book_item_icon_iv.setImageResource(R.mipmap.unisdefault);
        }
        holder.account_book_item_name_tv.setText(accountBook.getAccountBookName());
        return convertView;
    }

    private class Holder{
        ImageView account_book_item_icon_iv;
        TextView account_book_item_name_tv;
    }
}
