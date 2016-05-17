package com.lihang.accounting.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lihang.accounting.receiver.DatabaseBackupReceiver;

import java.util.Date;

//数据库备份服务
public class ServiceDatabaseBackup extends Service {
    private static final long SPACINGIN_TERVAL = 24*60*60*1000;
    public ServiceDatabaseBackup() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DataBackupService dataBackupService = new DataBackupService(this);
        //获取上次数据备份的日期
        long backupMillise = dataBackupService.loadDatabaseBackupDate();
        Date backupDate = new Date();
        if (backupMillise == 0) {
            dataBackupService.databaseBackup(backupDate);
            backupMillise = dataBackupService.loadDatabaseBackupDate();
        }else {
            if (backupDate.getTime() - backupMillise >= SPACINGIN_TERVAL) {
                dataBackupService.databaseBackup(backupDate);
                backupMillise = dataBackupService.loadDatabaseBackupDate();
            }
        }
        System.out.println("服务------------>" + backupMillise);

        Intent i = new Intent(this, DatabaseBackupReceiver.class);
        i.putExtra("date", backupMillise);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, i,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, backupMillise + SPACINGIN_TERVAL, pendingIntent);

        return super.onStartCommand(intent, flags, startId);
    }
}
