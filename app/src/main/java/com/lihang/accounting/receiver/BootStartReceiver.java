package com.lihang.accounting.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lihang.accounting.service.ServiceDatabaseBackup;

/**
 * Created by HARRY on 2016/3/16.
 */
public class BootStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, ServiceDatabaseBackup.class);
        context.startService(serviceIntent);
    }
}