package com.lihang.accounting.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lihang.accounting.R;
import com.lihang.accounting.database.base.SQLiteDAOBase;
import com.lihang.accounting.entitys.User;
import com.lihang.accounting.utils.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by LiHang on 2016/2/29.
 */
public class UserDAO extends SQLiteDAOBase {
    public UserDAO(Context context) {
        super(context);
        Log.i("提示", "0");
    }

    @Override
    protected String[] getTableNameAndPK() {
        return new String[]{"user" , "userid"};
    }

    @Override
    protected Object findModel(Cursor cursor) {
        User user = new User();
        user.setUserId(cursor.getInt(cursor.getColumnIndex("userid")));
        user.setUserName(cursor.getString(cursor.getColumnIndex("username")));
        Date createDate = DateUtil.string2date(cursor.getString(cursor.getColumnIndex("createDate")), DateUtil.YYYY_MM_DD_HH_MM_SS);
        user.setCreateDate(createDate);
        user.setState(cursor.getInt(cursor.getColumnIndex("state")));
        return user;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.i("提示", "创建表");
        StringBuilder sql = new StringBuilder();
        sql.append("");
        sql.append("Create TABLE MAIN.[user](");
        sql.append(" [userid] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
        sql.append(",[username]varchar(20) NOT NULL");
        sql.append(",[createDate]datetime NOT NULL");
        sql.append(",[state]int NOT NULL");
        sql.append(")");
        database.execSQL(sql.toString());
        initDefaultData(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database) {

    }

    public boolean insertUser(User user) {
        ContentValues contentValues = createParms(user);
        long newid = getDatabase().insert(getTableNameAndPK()[0], null, contentValues);
        return newid > 0;
    }

    public ContentValues createParms(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("username" , user.getUserName());
        contentValues.put("createDate" , DateUtil.date2string(user.getCreateDate(), DateUtil.YYYY_MM_DD_HH_MM_SS));
        contentValues.put("state" , user.getState());
        return contentValues;
    }

    public boolean deleteUser(String condition) {
        return delete(getTableNameAndPK()[0], condition);
    }

    public boolean updateUser(String condition, User user) {
        ContentValues contentValues = createParms(user);
        return updateUser(condition, contentValues);
    }

    public boolean updateUser(String condition, ContentValues contentValues) {
        return getDatabase().update(getTableNameAndPK()[0], contentValues, condition, null) > 0;
    }

    public List<User> getUsers(String condition) {
        String sql = "select * from user where 1=1 " + condition;
        return getList(sql);
    }

    private void initDefaultData(SQLiteDatabase database) {
        User user = new User();
        String[] username = getContext().getResources().getStringArray(R.array.InitDefaultUserName);
        for (int i = 0; i < username.length; i++) {
            user.setUserName(username[i]);
            ContentValues contentValues = createParms(user);
            database.insert(getTableNameAndPK()[0], null, contentValues);
        }
    }
}
