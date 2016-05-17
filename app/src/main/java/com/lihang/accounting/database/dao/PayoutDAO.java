package com.lihang.accounting.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lihang.accounting.database.base.SQLiteDAOBase;
import com.lihang.accounting.entitys.Payout;
import com.lihang.accounting.utils.DateUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by LiHang on 2016/2/29.
 */
public class PayoutDAO extends SQLiteDAOBase {
    public PayoutDAO(Context context) {
        super(context);
    }

    @Override
    protected String[] getTableNameAndPK() {
        return new String[]{"payout", "payoutId"};
    }

    @Override
    protected Object findModel(Cursor cursor) {
        Payout payout = new Payout();
        payout.setPayoutId(cursor.getInt(cursor.getColumnIndex("payoutId")));
        payout.setAccountBookId(cursor.getInt(cursor.getColumnIndex("accountBookId")));
        payout.setAccountBookName(cursor.getString(cursor.getColumnIndex("accountBookName")));
        payout.setCategoryId(cursor.getInt(cursor.getColumnIndex("categoryId")));
        payout.setCategoryName(cursor.getString(cursor.getColumnIndex("categoryName")));
        payout.setAmount(new BigDecimal(cursor.getString(cursor.getColumnIndex("amount"))));
        Date payoutDate = DateUtil.string2date(cursor.getString(cursor.getColumnIndex("payoutDate")), DateUtil.YYYY_MM_DD_HH_MM_SS);
        Date createDate = DateUtil.string2date(cursor.getString(cursor.getColumnIndex("createDate")), DateUtil.YYYY_MM_DD_HH_MM_SS);
        payout.setPayoutDate(payoutDate);
        payout.setCreateDate(createDate);
        payout.setPayoutType(cursor.getString(cursor.getColumnIndex("payoutType")));
        payout.setPayoutUserId(cursor.getString(cursor.getColumnIndex("payoutUserId")));
        payout.setComment(cursor.getString(cursor.getColumnIndex("comment")));
        payout.setState(cursor.getInt(cursor.getColumnIndex("state")));
        return payout;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        StringBuilder sql = new StringBuilder();
        sql.append("");
        sql.append("Create TABLE MAIN.[payout](");
        sql.append(" [payoutId] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
        sql.append(",[accountBookId] int NOT NULL");
        sql.append(",[categoryId] integer NOT NULL");
        sql.append(",[amount] decimal NOT NULL");
        sql.append(",[payoutDate] datetime NOT NULL");
        sql.append(",[payoutType] varchar(20) NOT NULL");
        sql.append(",[payoutUserId] text NOT NULL");
        sql.append(",[comment] text NOT NULL");
        sql.append(",[createDate] datetime NOT NULL");
        sql.append(",[state] int NOT NULL");
        sql.append(")");
        database.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase database) {

    }

    public boolean insertPayout(Payout payout) {
        ContentValues contentValues = createParms(payout);
        long newid = getDatabase().insert(getTableNameAndPK()[0], null, contentValues);
        payout.setPayoutId((int) newid);
        return newid > 0;
    }

    public ContentValues createParms(Payout payout) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountBookId", payout.getAccountBookId());
        contentValues.put("categoryId", payout.getCategoryId());
        contentValues.put("amount", payout.getAmount().toString());
        contentValues.put("payoutDate", DateUtil.date2string(payout.getPayoutDate(), DateUtil.YYYY_MM_DD_HH_MM_SS));
        contentValues.put("payoutType", payout.getPayoutType());
        contentValues.put("payoutUserId", payout.getPayoutUserId());
        contentValues.put("comment", payout.getComment());
        contentValues.put("createDate", DateUtil.date2string(payout.getCreateDate(), DateUtil.YYYY_MM_DD_HH_MM_SS));
        contentValues.put("state", payout.getState());
        return contentValues;
    }

    public boolean deletePayout(String condition) {
        return delete(getTableNameAndPK()[0], condition);
    }

    public boolean updatePayout(String condition, Payout payout) {
        ContentValues contentValues = createParms(payout);
        return updatePayout(condition, contentValues);
    }

    public boolean updatePayout(String condition, ContentValues contentValues) {
        return getDatabase().update(getTableNameAndPK()[0], contentValues, " 1=1 " + condition, null) >= 0;
    }

    public List<Payout> getPayouts(String condition) {
        String sql = "select * from v_payout where 1=1 " + condition;
        return getList(sql);
    }

}
