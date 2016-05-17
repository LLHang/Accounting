package com.lihang.accounting.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lihang.accounting.R;
import com.lihang.accounting.activity.base.FrameActivity;
import com.lihang.accounting.adapter.AccountBookSelectAdapter;
import com.lihang.accounting.entitys.AccountBook;
import com.lihang.accounting.service.AccountBookService;
import com.lihang.accounting.service.StatisticsService;
import com.lihang.accounting.view.SlideMenuItem;
import com.lihang.accounting.view.SlideMenuView;

public class StatisticsActivity extends FrameActivity implements SlideMenuView.OnSlideMenuListener {
    private TextView statistics_tv;
    private StatisticsService statisticsService;
    AccountBookService accountBookService;
    private AccountBook accountBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.statistics);
        initVariable();
        initView();
        initListeners();
        bindData();
        createSlideMenu(R.array.SlidMenuStatistics);
    }

    private void initVariable() {
        statisticsService = new StatisticsService(this);
        accountBookService = new AccountBookService(this);
        accountBook = accountBookService.getDefaultAccountBook();
    }

    private void initView() {
        statistics_tv = (TextView) findViewById(R.id.statistics_tv);

    }

    private void initListeners() {

    }

    private void bindData() {
        showProgressDialog(R.string.dialog_title_statistics, R.string.dialog_waiting_statistics_progress);
        new BindDataThread().start();
        setTitle();
    }
    private class BindDataThread extends Thread{
        @Override
        public void run() {
            String result = statisticsService.getPayoutUserIdByAccountBookId(accountBook.getAccountBookId());
            Message msg = handler.obtainMessage();
            msg.obj = result;
            msg.what = 1;
            handler.sendMessage(msg);
        }
    }

    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    statistics_tv.setText(result);
                    dismissProgressDialog();
                    break;
            }
        }
    };

    private void setTitle() {
        setTopBarTitle(getString(R.string.title_statistics, new Object[]{accountBook.getAccountBookName()}));
    }

    @Override
    public void OnSlideMenuItemClick(View view, SlideMenuItem item) {
        slideMenuToggle();
        if (item.getItemId() == 0) {
            showAccountBookSelectDialog();
        }
        if (item.getItemId() == 1) {//导出数据
            exportData();
        }
    }

    private void exportData() {
        String result = "";
        try {
            result = statisticsService.exportStatistics(accountBook.getAccountBookId());
        } catch (Exception e) {
            e.printStackTrace();
            result = getString(R.string.export_data_fail);
        }
        showMessage(result);
    }

    /**
     * 显示账本选择的dialog
     */
    private void showAccountBookSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getInflater().inflate(R.layout.account_book, null);
        ListView select_lv = (ListView) view.findViewById(R.id.account_book_lv);
        AccountBookSelectAdapter adapter = new AccountBookSelectAdapter(this);
        select_lv.setAdapter(adapter);

        builder.setTitle(R.string.button_text_select_account_book)
                .setNegativeButton(R.string.button_text_back, null)
                .setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        select_lv.setOnItemClickListener(new OnAccountBookItemClickListener(dialog));
    }

    private class OnAccountBookItemClickListener implements AdapterView.OnItemClickListener {
        private AlertDialog dialog;

        public OnAccountBookItemClickListener(AlertDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            accountBook = (AccountBook) parent.getAdapter().getItem(position);
            bindData();
            dialog.dismiss();
        }
    }
}
