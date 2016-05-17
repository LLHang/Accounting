package com.lihang.accounting.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.lihang.accounting.R;
import com.lihang.accounting.activity.MainActivity;
import com.lihang.accounting.service.ServiceDatabaseBackup;

/**
 * Created by HARRY on 2016/3/16.
 */
public class DatabaseBackupReceiver extends BroadcastReceiver {

    NotificationManager notificationManager;
    Notification notification;
    Intent i;
    PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        //初始化通知管理器
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        System.out.println("广播---------->" + intent.getLongExtra("date", 0));
        String contentTitle = context.getString(R.string.app_name);
        String contentText = "随心记账已执行数据备份";
        //点击通知时打开MainActivity
        i = new Intent(context, MainActivity.class);
        //如果Intent要启动的Activity在栈顶，则无需创建新的实例
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivities(context, 100, new Intent[]{i}, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT < 11) {

        } else {
            Notification.Builder builder = new Notification.Builder(context);
            //设置通知显示的标题
            builder.setContentTitle(contentTitle);
            //设置通知显示的内容
            builder.setContentText(contentText);
            //设置在状态栏显示的图标
            builder.setSmallIcon(R.mipmap.ic_launcher);
            //通知时发出的默认声音
            builder.setDefaults(Notification.DEFAULT_SOUND);
            builder.setContentIntent(pendingIntent);
            notification = builder.build();
        }

        notificationManager.notify(10, notification);

        Intent serviceIntent = new Intent(context, ServiceDatabaseBackup.class);
        context.startService(serviceIntent);
    }
}
