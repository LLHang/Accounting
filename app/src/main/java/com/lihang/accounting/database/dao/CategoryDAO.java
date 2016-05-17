package com.lihang.accounting.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lihang.accounting.R;
import com.lihang.accounting.database.base.SQLiteDAOBase;
import com.lihang.accounting.entitys.Category;
import com.lihang.accounting.utils.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by LiHang on 2016/2/29.
 */
public class CategoryDAO extends SQLiteDAOBase {
    public CategoryDAO(Context context) {
        super(context);
        Log.i("提示", "0");
    }

    @Override
    protected String[] getTableNameAndPK() {
        return new String[]{"category", "categoryId"};
    }

    @Override
    protected Object findModel(Cursor cursor) {
        Category category = new Category();
        category.setCategoryId(cursor.getInt(cursor.getColumnIndex("categoryId")));
        category.setCategoryName(cursor.getString(cursor.getColumnIndex("categoryName")));
        category.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
        category.setPath(cursor.getString(cursor.getColumnIndex("path")));
        Date createDate = DateUtil.string2date(cursor.getString(cursor.getColumnIndex("createDate")), DateUtil.YYYY_MM_DD_HH_MM_SS);
        category.setCreateDate(createDate);
        category.setState(cursor.getInt(cursor.getColumnIndex("state")));
        return category;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.i("提示", "创建表");
        StringBuilder sql = new StringBuilder();
        sql.append("");
        sql.append("Create TABLE MAIN.[category](");
        sql.append(" [categoryId] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
        sql.append(",[categoryName] varchar(20) NOT NULL");
        sql.append(",[parentId] int NOT NULL");
        sql.append(",[path] text NOT NULL");
        sql.append(",[createDate] datetime NOT NULL");
        sql.append(",[state] int NOT NULL");
        sql.append(")");
        database.execSQL(sql.toString());
        initDefaultData(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database) {

    }

    public boolean insertCategory(Category category) {
        ContentValues contentValues = createParms(category);
        long newid = getDatabase().insert(getTableNameAndPK()[0], null, contentValues);
        return newid > 0;
    }

    public long insertCategoryReNewId(Category category) {
        ContentValues contentValues = createParms(category);
        long newid = getDatabase().insert(getTableNameAndPK()[0], null, contentValues);
        return newid;
    }

    public ContentValues createParms(Category category) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("categoryName", category.getCategoryName());
        contentValues.put("parentId", category.getParentId());
        contentValues.put("path", category.getPath());
        contentValues.put("createDate", DateUtil.date2string(category.getCreateDate(), DateUtil.YYYY_MM_DD_HH_MM_SS));
        contentValues.put("state", category.getState());
        return contentValues;
    }

    public boolean deleteCategory(String condition) {
        return delete(getTableNameAndPK()[0], condition);
    }

    public boolean updateCategory(String condition, Category category) {
        ContentValues contentValues = createParms(category);
        return updateCategory(condition, contentValues);
    }

    public boolean updateCategory(String condition, ContentValues contentValues) {
        return getDatabase().update(getTableNameAndPK()[0], contentValues, "1=1" + condition, null) > 0;
    }

    public List<Category> getCategorys(String condition) {
        String sql = "select * from category where 1=1 " + condition;
        return getList(sql);
    }

    private void initDefaultData(SQLiteDatabase database) {
        Category category = new Category();
        category.setPath("");
        category.setParentId(0);
        String[] categoryName = getContext().getResources().getStringArray(R.array.InitDefaultCategoryName);

        for (int i = 0; i < categoryName.length; i++) {
            category.setCategoryName(categoryName[i]);
            ContentValues contentValues = createParms(category);
            long newid = database.insert(getTableNameAndPK()[0], null, contentValues);
            category.setPath(newid + ".");
            contentValues = createParms(category);
            database.update(getTableNameAndPK()[0], contentValues, "categoryId = ?", new String[]{newid + ""});
        }

    }
}
