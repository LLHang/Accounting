package com.lihang.accounting.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.lihang.accounting.R;
import com.lihang.accounting.database.dao.CreateViewDAO;
import com.lihang.accounting.database.dao.PayoutDAO;
import com.lihang.accounting.entitys.Payout;
import com.lihang.accounting.service.base.BaseService;

import java.util.List;

/**
 * Created by HARRY on 2016/3/11.
 */
public class PayoutService extends BaseService {
    private PayoutDAO payoutDAO;
    private Context context;
    private CreateViewDAO createViewDAO;

    public PayoutService(Context context) {
        super(context);
        payoutDAO = new PayoutDAO(context);
        createViewDAO = new CreateViewDAO(context);
        this.context = context;
    }

    public List<Payout> getPayoutListByAccountBookId(int accountBookId) {
        return payoutDAO.getPayouts(" and accountBookId = " + accountBookId + " and state = 1 order by payoutDate DESC");
    }

    public String getPayoutTotalMessage(String payoutDate,int accountBookId) {
        String condition = " and payoutDate = '" + payoutDate + " 00:00:00' and accountBookId = " + accountBookId;
        String sql = " select ifnull(sum(amount),0) as sumAmount,count(amount) as count from payout where 1=1 and state = 1" + condition;
        String[] total = new String[2];
        Cursor cursor = payoutDAO.execSql(sql);
        if (cursor.getCount() == 1) {
            while (cursor.moveToNext()) {
                total[0] = cursor.getString(cursor.getColumnIndex("count"));
                total[1] = cursor.getString(cursor.getColumnIndex("sumAmount"));
            }
        }
        return context.getString(R.string.title_payout_total, new Object[]{total[0], total[1]});
    }

    public boolean insertPayout(Payout payout) {
        payoutDAO.getDatabase().beginTransaction();
        try {
            Log.i("提示", payout.toString());
            boolean result = payoutDAO.insertPayout(payout);
            Log.i("提示", "" + result + payout.getPayoutId());
            if (result) {
                payoutDAO.setTransactionSuccessful();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            payoutDAO.endTransaction();
        }
    }

    public boolean updatePayout(Payout payout) {
        return payoutDAO.updatePayout(" and payoutId = " + payout.getPayoutId(), payout);
    }

    public List<Payout> getPayoutOrderByPayoutUserId(String condition) {
        condition += " order by payoutUserId";
        List<Payout> list = payoutDAO.getPayouts(condition);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    public boolean hidePayoutByAccountBookId(int accountBookId){
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", 0);
        String condition = " and accountBookId = " + accountBookId;
        return payoutDAO.updatePayout(condition, contentValues);
    }


    public boolean hidePayout(Payout payout) {
        return payoutDAO.updatePayout(" and payoutId = " + payout.getPayoutId(), payout);
    }

    public String getAccountBookCountByAccountBookId(int accountBookId) {
        String sql = " select count(accountBookId) as count, ifnull(sum(amount),0) as sum from payout where state = 1 and accountBookId = " + accountBookId;
        Cursor cursor = payoutDAO.execSql(sql);
        String result = "";
        while (cursor.moveToNext()) {
            result += cursor.getString(cursor.getColumnIndex("sum")) + "," +cursor.getString(cursor.getColumnIndex("count"));
        }
        return result;
    }
}
