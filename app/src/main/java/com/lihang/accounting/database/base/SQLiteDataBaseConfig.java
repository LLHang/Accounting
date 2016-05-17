package com.lihang.accounting.database.base;

import android.content.Context;

import com.lihang.accounting.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 数据库配置信息类
 */
public class SQLiteDataBaseConfig implements Serializable {
    //数据库名
    public static final String DATABASE_NAME = "accounting.db";
    //数据库版本
    private static final int VERSION = 1;

    private static SQLiteDataBaseConfig INSTANCE;

    private static Context CONTEXT;

    private SQLiteDataBaseConfig() {

    }

    private static class ConfigHolder {
        private ConfigHolder(Context context) {
            CONTEXT = context;
        }
        //单例对象实例
        static final SQLiteDataBaseConfig INSTANCE = new SQLiteDataBaseConfig();
    }

    /**
     *
     * @return
     */
    public static SQLiteDataBaseConfig getINSTANCE() {
        return ConfigHolder.INSTANCE;
    }

    public static SQLiteDataBaseConfig getINSTANCE(Context context) {
        return new ConfigHolder(context).INSTANCE;
    }

    //readResolve方法应对单利对象被序列化的时候
    private Object readResolve() {
        return getINSTANCE();
    }

    //返回数据库的名称
    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    //返回数据库的版本
    public int getVersion() {
        return VERSION;
    }

    /**
     * 获取表名
     * @return
     */
    public ArrayList<String> getTables() {
        ArrayList<String> list = new ArrayList<>();
        String[] sqliteDAOClassName = CONTEXT.getResources().getStringArray(R.array.SQLiteDAOClassName);
        String packagePath = CONTEXT.getPackageName() + ".database.dao.";
        for (int i = 0; i < sqliteDAOClassName.length; i++) {
            list.add(packagePath + sqliteDAOClassName[i]);
        }
        return list;
    }
}
