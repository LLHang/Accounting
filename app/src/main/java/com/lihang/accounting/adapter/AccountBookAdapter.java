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
import com.lihang.accounting.service.PayoutService;

import java.util.List;

/**
 * Created by LiHang on 2016/3/1.
 */
public class AccountBookAdapter extends SimpleBaseAdapter {
    private PayoutService payoutService;
    public AccountBookAdapter(Context context) {
        super(context, null);
        payoutService = new PayoutService(context);
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
        AccountBookService accountBookService = new AccountBookService(context);
        List<AccountBook> list = accountBookService.getNotHideAccountBook();
        setList(list);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.account_book_list_item, null);
            holder = new Holder();
            holder.account_book_item_icon_iv = (ImageView) convertView.findViewById(R.id.account_book_item_icon_iv);
            holder.account_book_item_name_tv = (TextView) convertView.findViewById(R.id.account_book_item_name_tv);
            holder.account_book_item_money_tv = (TextView) convertView.findViewById(R.id.account_book_item_money_tv);
            holder.account_book_item_total_tv = (TextView) convertView.findViewById(R.id.account_book_item_total_tv);
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
        String[] total = payoutService.getAccountBookCountByAccountBookId(((AccountBook) getItem(position)).getAccountBookId()).split(",");
        holder.account_book_item_money_tv.setText(context.getString(R.string.text_accountbook_money,new Object[]{total[0]}));
        holder.account_book_item_total_tv.setText(context.getString(R.string.text_accountbook_count,new Object[]{total[1]}));
        return convertView;
    }

    private class Holder{
        ImageView account_book_item_icon_iv;
        TextView account_book_item_name_tv;
        TextView account_book_item_money_tv;
        TextView account_book_item_total_tv;
    }
}
