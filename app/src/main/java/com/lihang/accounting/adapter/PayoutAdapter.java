package com.lihang.accounting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihang.accounting.R;
import com.lihang.accounting.adapter.base.SimpleBaseAdapter;
import com.lihang.accounting.entitys.Payout;
import com.lihang.accounting.service.PayoutService;
import com.lihang.accounting.service.UserService;
import com.lihang.accounting.utils.DateUtil;

import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by HARRY on 2016/3/11.
 */
public class PayoutAdapter extends SimpleBaseAdapter {
    private PayoutService payoutService;
    private Context context;
    private int accountBookId;

    public PayoutAdapter(Context context, int accountBookId) {
        super(context, null);
        payoutService = new PayoutService(context);
        this.accountBookId = accountBookId;
        this.context = context;
        List<Payout> list = payoutService.getPayoutListByAccountBookId(accountBookId);
        setList(list);
    }

    public void setAccountBookId(int accountBookId) {
        this.accountBookId = accountBookId;
    }

    public void update() {
        setList(payoutService.getPayoutListByAccountBookId(accountBookId));
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.payout_list_item, null);
            viewHolder.payoutListItemTopRl = (RelativeLayout) convertView.findViewById(R.id.payout_list_item_top_rl);
            viewHolder.payoutListItemBottomIconIv = (ImageView) convertView.findViewById(R.id.payout_list_item_bottom_icon_iv);
            viewHolder.payoutListItemBottomCategoryTv = (TextView) convertView.findViewById(R.id.payout_list_item_bottom_category_tv);
            viewHolder.payoutListItemBottomUserTv = (TextView) convertView.findViewById(R.id.payout_list_item_bottom_user_tv);
            viewHolder.payoutListItemBottomHejiTv = (TextView) convertView.findViewById(R.id.payout_list_item_bottom_heji_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.payoutListItemTopRl.setVisibility(View.GONE);
        Payout payout = (Payout) getItem(position);

        String payoutDate = DateUtil.date2string(payout.getPayoutDate(), DateUtil.YYYY_MM_DD);

        boolean isShow = false;
        if (position > 0) {
            //获取它上一个实体
            Payout payoutLast = (Payout) getItem(position - 1);
            //获取上一个实体的日期
            String payoutDateLast = DateUtil.date2string(payoutLast.getPayoutDate(), DateUtil.YYYY_MM_DD);
            //如果当前日期与上一个实体的日期不等，就显示
            isShow = !payoutDate.equals(payoutDateLast);
        }
        if (isShow || position == 0) {
            viewHolder.payoutListItemTopRl.setVisibility(View.VISIBLE);
            String msg = payoutService.getPayoutTotalMessage(payoutDate,payout.getAccountBookId());

            ((TextView) viewHolder.payoutListItemTopRl.findViewById(R.id.payout_list_item_top_date_tv)).setText(payoutDate);
            ((TextView) viewHolder.payoutListItemTopRl.findViewById(R.id.payout_list_item_top_count_tv)).setText(msg);
        }
        viewHolder.payoutListItemBottomIconIv.setImageResource(R.mipmap.majiang);
        viewHolder.payoutListItemBottomCategoryTv.setText(payout.getCategoryName());
        viewHolder.payoutListItemBottomHejiTv.setText(context.getString(
                R.string.textview_text_payout_amount,
                new Object[]{payout.getAmount().toString()}));
        UserService userService = new UserService(context);
        String userName = userService.getUserNameByUserId(payout.getPayoutUserId());
        viewHolder.payoutListItemBottomUserTv.setText(userName + "  " + payout.getPayoutType());
        return convertView;
    }


    static class ViewHolder {
        TextView payoutListItemTopDateTv;
        TextView payoutListItemTopCountTv;
        RelativeLayout payoutListItemTopRl;
        ImageView payoutListItemBottomIconIv;
        TextView payoutListItemBottomCategoryTv;
        TextView payoutListItemBottomUserTv;
        TextView payoutListItemBottomHejiTv;
    }
}
