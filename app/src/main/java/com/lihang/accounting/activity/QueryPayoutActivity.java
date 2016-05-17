package com.lihang.accounting.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lihang.accounting.R;
import com.lihang.accounting.activity.base.FrameActivity;
import com.lihang.accounting.adapter.AccountBookSelectAdapter;
import com.lihang.accounting.adapter.PayoutAdapter;
import com.lihang.accounting.entitys.AccountBook;
import com.lihang.accounting.entitys.Payout;
import com.lihang.accounting.service.AccountBookService;
import com.lihang.accounting.service.PayoutService;
import com.lihang.accounting.view.SlideMenuItem;
import com.lihang.accounting.view.SlideMenuView;

public class QueryPayoutActivity extends FrameActivity implements SlideMenuView.OnSlideMenuListener {
    private ListView query_payout_list;
    private PayoutService payoutService;
    private PayoutAdapter payoutAdapter;
    private AccountBook accountBook;
    private Payout payout;
    private AccountBookService accountBookService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.query_payout_activity);
        initVariable();
        initView();
        initListeners();
        initData();
        createSlideMenu(R.array.slidMenuQueryPayout);
    }

    private void initVariable() {
        payoutService = new PayoutService(this);
        accountBookService = new AccountBookService(this);
    }

    private void initView() {
        query_payout_list = (ListView) findViewById(R.id.query_payout_list);
    }

    private void initListeners() {
        registerForContextMenu(query_payout_list);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        ListAdapter listAdapter = query_payout_list.getAdapter();
        payout = (Payout) listAdapter.getItem(acmi.position);
        menu.setHeaderIcon(R.mipmap.icon_zhichu_type_canyin);
        menu.setHeaderTitle(accountBook.getAccountBookName());
        createContextMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            Intent intent = new Intent(QueryPayoutActivity.this, PayoutAddOrEditActivity.class);
            intent.putExtra("payout", payout);
            startActivityForResult(intent, 1);
        }
        if (item.getItemId() == 2) {
            showDeleteOkDialog();
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        payoutAdapter.update();
    }

    private void showDeleteOkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton(getString(R.string.button_text_no), null);
        builder.setPositiveButton(getString(R.string.button_text_yes), new DeleteOkClickListener());
        builder.setTitle(getString(R.string.dialog_title_delete));
        builder.setMessage(getString(R.string.dialog_message_payout_delete));
        builder.show();
    }

    private class DeleteOkClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            payout.setState(0);
            boolean result = payoutService.hidePayout(payout);
            if (result) {
                showMessage(R.string.tips_delete_success);
            } else {
                showMessage(R.string.tips_delete_fail);
            }
            payoutAdapter.update();
            setTitle();
        }
    }

    private void initData() {
        if (accountBook == null) {
            accountBook = accountBookService.getDefaultAccountBook();
        }
        if (payoutAdapter == null) {
            payoutAdapter = new PayoutAdapter(this, accountBook.getAccountBookId());
            query_payout_list.setAdapter(payoutAdapter);

        }
        setTitle();
    }

    private void setTitle() {
        setTopBarTitle(getString(R.string.title_query_payout, new Object[]{accountBook.getAccountBookName(), payoutAdapter.getCount()}));
    }

    @Override
    public void OnSlideMenuItemClick(View view, SlideMenuItem item) {
        if (item.getItemId() == 0) {
            showAccountBookSelectDialog();
            slideMenuToggle();
        }
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
            payoutAdapter.setAccountBookId(accountBook.getAccountBookId());
            payoutAdapter.update();
            setTitle();
            dialog.dismiss();
        }
    }
}
