package com.lihang.accounting.database.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lihang.accounting.utils.Reflection;

import java.util.List;

/**
 * Created by LiHang on 2016/2/29.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private static SQLiteDataBaseConfig CONFIG;
    private static SQLiteHelper INSTANCE;
    private Reflection reflection;
    private Context context;

    public interface SQLiteDataTable {
        public void onCreate(SQLiteDatabase database);
        public void onUpgrade(SQLiteDatabase database);
    }

    private SQLiteHelper(Context context) {
        super(context, CONFIG.getDatabaseName(), null, CONFIG.getVersion());
        this.context = context;
    }

    public static SQLiteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            CONFIG = SQLiteDataBaseConfig.getINSTANCE(context);
            INSTANCE = new SQLiteHelper(context);
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        List<String> list = CONFIG.getTables();
        reflection = new Reflection();
        for (int i = 0; i < list.size(); i++) {
            try {
                SQLiteDataTable sqLiteDataTable = (SQLiteDataTable) reflection.newInstance(
                        list.get(i),
                        new Object[]{context},
                        new Class[]{Context.class});
                sqLiteDataTable.onCreate(db);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
