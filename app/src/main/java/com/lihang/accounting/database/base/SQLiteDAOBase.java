package com.lihang.accounting.database.base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 各业务类的抽象类实现了SQLitDataTable接口,由继承该类的子类实现接口的方法
 */
public abstract class SQLiteDAOBase  implements SQLiteHelper.SQLiteDataTable{
    private Context context;
    private SQLiteDatabase database;

    public SQLiteDAOBase(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return context;
    }

    public SQLiteDatabase getDatabase() {
        if (database == null) {
            database = SQLiteHelper.getInstance(context).getWritableDatabase();
        }
        return database;
    }

    //开启事务
    public void beginTransaction() {
        database.beginTransaction();
    }

    //设置事务已经成功
    public void setTransactionSuccessful() {
        database.setTransactionSuccessful();
    }

    //结束事务
    public void endTransaction() {
        database.endTransaction();
    }

    protected abstract String[] getTableNameAndPK();

    public Cursor execSql(String sql) {
        return getDatabase().rawQuery(sql, null);
    }

    //获取总数
    public int getCount(String pk, String tableName, String condition) {
        Cursor cursor = execSql(" select " + pk + " from " + tableName + " where 1=1 " + condition);
        int count = cursor.getCount();
        return count;
    }

    //获取总数
    public int getCount(String condition) {
        String[] str = getTableNameAndPK();

        return getCount(str[1], str[0], condition);
    }

    protected boolean delete(String tableName, String condition) {
        return getDatabase().delete(tableName, "1=1" + condition, null)>0;
    }

    protected List cursorToList(Cursor cursor) {
        List list = new ArrayList();
        cursor.moveToPrevious();
        while (cursor.moveToNext()) {
            Object object = findModel(cursor);
            list.add(object);
        }
        cursor.close();
        return list;
    }

    protected abstract Object findModel(Cursor cursor);

    protected List getList(String sql) {
        Cursor cursor = execSql(sql);
        return cursorToList(cursor);
    }

}
