package com.lihang.accounting.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.widget.Toast;

import com.lihang.accounting.database.base.SQLiteDataBaseConfig;
import com.lihang.accounting.service.base.BaseService;
import com.lihang.accounting.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by HARRY on 2016/3/16.
 */
public class DataBackupService extends BaseService {
    private static final String SDCARD_PATH = Environment.
            getExternalStorageDirectory().getPath() + "/Accounting/Backup/";
    private final String DATA_PATH = Environment.
            getDataDirectory() + "/data/" + context.getPackageName() + "/databases/";

    public DataBackupService(Context context) {
        super(context);
    }

    //读取上次数据备份的日期
    public long loadDatabaseBackupDate() {
        long databaseBackupDate = 0;
        SharedPreferences sp = context.getSharedPreferences("databaseBackupDate", Context.MODE_PRIVATE);
        if (sp != null) {
            databaseBackupDate = sp.getLong("databaseBackupDate", 0);
        }
        return databaseBackupDate;
    }

    public boolean databaseBackup(Date backupDate) {
        boolean result = false;
        File sourceFile = new File(DATA_PATH + SQLiteDataBaseConfig.DATABASE_NAME);
        if (sourceFile.exists()) {
            File fileDir = new File(SDCARD_PATH);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            try {
                FileUtil.copy(DATA_PATH + SQLiteDataBaseConfig.DATABASE_NAME, SDCARD_PATH + SQLiteDataBaseConfig.DATABASE_NAME);
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "应用无数据，不需备份", Toast.LENGTH_SHORT).show();
            result = true;
        }
        saveDatabaseBackupDate(backupDate.getTime());
        return result;
    }

    private void saveDatabaseBackupDate(long millisecond) {
        SharedPreferences sp = context.getSharedPreferences("databaseBackupDate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("databaseBackupDate", millisecond);
        editor.commit();
    }

    public boolean databaseRestore() {
        try {
            FileUtil.copy(SDCARD_PATH + SQLiteDataBaseConfig.DATABASE_NAME, DATA_PATH + SQLiteDataBaseConfig.DATABASE_NAME);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
