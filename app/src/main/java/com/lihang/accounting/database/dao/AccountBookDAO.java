package com.lihang.accounting.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lihang.accounting.R;
import com.lihang.accounting.database.base.SQLiteDAOBase;
import com.lihang.accounting.entitys.AccountBook;
import com.lihang.accounting.utils.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by LiHang on 2016/2/29.
 */
public class AccountBookDAO extends SQLiteDAOBase {
    public AccountBookDAO(Context context) {
        super(context);
    }

    @Override
    protected String[] getTableNameAndPK() {
        return new String[]{"accountBook", "accountBookId"};
    }

    @Override
    protected Object findModel(Cursor cursor) {
        AccountBook accountBook = new AccountBook();
        accountBook.setAccountBookId(cursor.getInt(cursor.getColumnIndex("accountBookId")));
        accountBook.setAccountBookName(cursor.getString(cursor.getColumnIndex("accountBookName")));
        Date createDate = DateUtil.string2date(cursor.getString(cursor.getColumnIndex("createDate")), DateUtil.YYYY_MM_DD_HH_MM_SS);
        accountBook.setCreateDate(createDate);
        accountBook.setState(cursor.getInt(cursor.getColumnIndex("state")));
        accountBook.setIsDefault(cursor.getInt(cursor.getColumnIndex("isDefault")));
        return accountBook;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.i("提示", "创建表");
        StringBuilder sql = new StringBuilder();
        sql.append("");
        sql.append("Create TABLE MAIN.[accountBook](");
        sql.append(" [accountBookId] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
        sql.append(",[accountBookName]varchar(20) NOT NULL");
        sql.append(",[createDate]datetime NOT NULL");
        sql.append(",[state]int NOT NULL");
        sql.append(",[isDefault]int NOT NULL");
        sql.append(")");
        database.execSQL(sql.toString());
        initDefaultData(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database) {

    }

    public boolean insertAccountBook(AccountBook accountBook) {
        ContentValues contentValues = createParms(accountBook);
        long newid = getDatabase().insert(getTableNameAndPK()[0], null, contentValues);
        return newid > 0;
    }

    public long insertAccountBookIdReNewId(AccountBook accountBook) {
        ContentValues contentValues = createParms(accountBook);
        long newid = getDatabase().insert(getTableNameAndPK()[0], null, contentValues);
        return newid;
    }

    public ContentValues createParms(AccountBook accountBook) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountBookName", accountBook.getAccountBookName());
        contentValues.put("createDate", DateUtil.date2string(accountBook.getCreateDate(), DateUtil.YYYY_MM_DD_HH_MM_SS));
        contentValues.put("state", accountBook.getState());
        contentValues.put("isDefault", accountBook.getIsDefault());
        return contentValues;
    }

    public boolean deleteAccountBook(String condition) {
        return delete(getTableNameAndPK()[0], condition);
    }

    public boolean updateAccountBook(String condition, AccountBook accountBook) {
        ContentValues contentValues = createParms(accountBook);
        return updateAccountBook("1=1"+condition, contentValues);
    }

    public boolean updateAccountBook(String condition, ContentValues contentValues) {
        return getDatabase().update(getTableNameAndPK()[0], contentValues, condition, null) > 0;
    }

    public List<AccountBook> getAccountBooks(String condition) {
        String sql = "select * from accountBook where 1=1 " + condition;
        return getList(sql);
    }

    private void initDefaultData(SQLiteDatabase database) {
        AccountBook accountBook = new AccountBook();
        String[] accountBookName = getContext().getResources().getStringArray(R.array.InitDefaultAccountBookName);

        accountBook.setAccountBookName(accountBookName[0]);
        accountBook.setIsDefault(1);
        ContentValues contentValues = createParms(accountBook);
        database.insert(getTableNameAndPK()[0], null, contentValues);
    }
}
