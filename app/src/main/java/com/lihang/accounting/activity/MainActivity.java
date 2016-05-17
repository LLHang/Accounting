package com.lihang.accounting.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.lihang.accounting.R;
import com.lihang.accounting.adapter.MyAdapter;
import com.lihang.accounting.activity.base.FrameActivity;
import com.lihang.accounting.service.DataBackupService;
import com.lihang.accounting.service.ServiceDatabaseBackup;
import com.lihang.accounting.view.SlideMenuItem;
import com.lihang.accounting.view.SlideMenuView;

import java.util.Date;

public class MainActivity extends FrameActivity implements View.OnClickListener,SlideMenuView.OnSlideMenuListener {

    GridView main_body_gv;
    MyAdapter myAdapter;
    DataBackupService dataBackupService;
    AlertDialog databaseBackupDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.main_body);
        initVariable();
        initView();
        initListeners();
        initData();
        createSlideMenu(R.array.SlideMenuActivityMain);
        startService();
        removeBackButton();
    }

    private void startService() {
        Intent intent = new Intent(this, ServiceDatabaseBackup.class);
        startService(intent);
    }


    private void initVariable() {
        myAdapter = new MyAdapter(this);
        dataBackupService = new DataBackupService(this);
    }

    private void initView() {
        main_body_gv = (GridView) findViewById(R.id.gridview);
    }

    private void initListeners() {
        main_body_gv.setOnItemClickListener(new OnGridItemClickListener());
    }

    private void initData() {
        main_body_gv.setAdapter(myAdapter);
    }

    @Override
    public void OnSlideMenuItemClick(View view, SlideMenuItem item) {
        slideMenuToggle();
        if (item.getTitle().equals("数据备份")) {
            showDatabaseBackupDialog();
        }
    }



    private class OnGridItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String menuName = (String) parent.getAdapter().getItem(position);
            if (menuName.equals(getString(R.string.grid_user))) {
                openActivity(UserActivity.class);
                return;
            } else if(menuName.equals(getString(R.string.grid_accountbook))){
                openActivity(AccountBookActivity.class);
                return;
            } else if (menuName.equals(getString(R.string.grid_category))) {
                openActivity(CategoryActivity.class);
                return;
            } else if (menuName.equals(getString(R.string.grid_add_payout))) {
                openActivity(PayoutAddOrEditActivity.class);
                return;
            } else if (menuName.equals(getString(R.string.grid_query_payout))) {
                openActivity(QueryPayoutActivity.class);
                return;
            } else if (menuName.equals(getString(R.string.grid_statistics))) {
                openActivity(StatisticsActivity.class);
                return;
            }

        }
    }

    private void showDatabaseBackupDialog() {

        View view = getInflater().inflate(R.layout.database_backup, null);
        Button database_backup_btn = (Button) view.findViewById(R.id.database_backup_btn);
        Button database_restore_btn = (Button) view.findViewById(R.id.database_restore_btn);
        database_backup_btn.setOnClickListener(this);
        database_restore_btn.setOnClickListener(this);
        String title = getString(R.string.dialog_title_database_backup);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setView(view)
                .setIcon(R.mipmap.ic_launcher)
                .setNegativeButton(getString(R.string.button_text_back), null);
        databaseBackupDialog = builder.show();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.database_backup_btn://数据备份
                databaseBackup();
                break;
            case R.id.database_restore_btn://数据还原
                databaseRestore();
                break;
        }
    }

    private void databaseRestore() {
        if (dataBackupService.databaseRestore()){
            showMessage(R.string.dialog_message_restore_success);
        }else {
            showMessage(R.string.dialog_message_restore_fail);
        }
        databaseBackupDialog.dismiss();
    }

    private void databaseBackup() {
        if (dataBackupService.databaseBackup(new Date())){
            showMessage(R.string.dialog_message_backup_success);
        }else {
            showMessage(R.string.dialog_message_backup_fail);
        }
        databaseBackupDialog.dismiss();
    }

}
