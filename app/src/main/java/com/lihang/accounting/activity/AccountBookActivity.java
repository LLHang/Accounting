package com.lihang.accounting.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lihang.accounting.R;
import com.lihang.accounting.activity.base.FrameActivity;
import com.lihang.accounting.adapter.AccountBookAdapter;
import com.lihang.accounting.entitys.AccountBook;
import com.lihang.accounting.service.AccountBookService;
import com.lihang.accounting.utils.RegexpUtil;
import com.lihang.accounting.view.SlideMenuItem;
import com.lihang.accounting.view.SlideMenuView;

public class AccountBookActivity extends FrameActivity implements SlideMenuView.OnSlideMenuListener {
    ListView accountBook_list_lv;
    AccountBookAdapter accountBookAdapter;
    AccountBookService accountBookService;
    private AccountBook accountBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.account_book);
        initVariable();
        initView();
        initListeners();
        initData();
        createSlideMenu(R.array.SlidMenuAccountBook);
    }

    private void initVariable() {
        accountBookService = new AccountBookService(this);
    }

    private void initView() {
        accountBook_list_lv = (ListView) findViewById(R.id.account_book_lv);
    }

    private void initListeners() {
        registerForContextMenu(accountBook_list_lv);
    }

    private void initData() {
        if (accountBookAdapter == null) {
            accountBookAdapter = new AccountBookAdapter(this);
            accountBook_list_lv.setAdapter(accountBookAdapter);
        } else {
            accountBookAdapter.clear();
            accountBookAdapter.updateList();
        }
        setTitle();
    }

    private void setTitle() {
        setTopBarTitle(getString(R.string.title_account_book,
                new Object[]{accountBookAdapter.getCount()}));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        ListAdapter listAdapter = accountBook_list_lv.getAdapter();
        accountBook = (AccountBook) listAdapter.getItem(acmi.position);
        menu.setHeaderIcon(R.mipmap.icon_zhichu_type_canyin);
        menu.setHeaderTitle(accountBook.getAccountBookName());
        createContextMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1://修改
                showAccountBookAddOrEditDialog(accountBook);
                break;
            case 2://删除
                if (accountBook.getIsDefault() == 1) {
                    showMessage(getString(R.string.tips_not_delete, new Object[]{"账本"}));
                    break;
                }
                delete();
                accountBookService.hideAccountBookByAccountBookId(accountBook);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void delete() {
        String msg = getString(R.string.dialog_message_accountbook_delete, new Object[]{accountBook.getAccountBookName()});
        showAlertDialog(R.string.dialog_title_delete, msg, new OnDeleteClickListener());
    }

    private class OnDeleteClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            accountBook.setState(0);
            boolean result = accountBookService.hideAccountBookByAccountBookId(accountBook);
            if (result) {
                initData();
            } else {
                //提示删除失败
                showMessage(getString(R.string.tips_delete_fail));
            }
        }
    }

    @Override
    public void OnSlideMenuItemClick(View view, SlideMenuItem item) {
        slideMenuToggle();
        if (item.getItemId() == 0) {
            showAccountBookAddOrEditDialog(null);
        }
    }

    private void showAccountBookAddOrEditDialog(AccountBook accountBook) {
        View view = getInflater().inflate(R.layout.account_add_or_edit, null);
        EditText accountBook_name_et = (EditText) view.findViewById(R.id.account_book_name_et);
        final CheckBox account_book_check_default_cb = (CheckBox) view.findViewById(R.id.account_book_check_default_cb);


        String title;
        if (accountBook == null) {
            title = getString(R.string.dialog_title_account_book,
                    new Object[]{getString(R.string.title_add)});
        } else {
            accountBook_name_et.setText(accountBook.getAccountBookName());
            title = getString(R.string.dialog_title_account_book,
                    new Object[]{getString(R.string.title_edit)});
            if (accountBook.getIsDefault() == 1) {
                if (accountBookAdapter.getCount() == 1) {
                    account_book_check_default_cb.setEnabled(false);
                    account_book_check_default_cb.setText("当前只一个账本，不能取消默认");
                } else {
                    account_book_check_default_cb.setChecked(true);
                    account_book_check_default_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (!isChecked) {
                                new AlertDialog.Builder(AccountBookActivity.this)
                                        .setTitle("提示")
                                        .setMessage("当前账单为默认账单，如果取消将设置第一个账单为默认账单，是否确定！")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                try {
                                                    accountBookService.setIsDefault(((AccountBook) accountBookAdapter.getItem(0)).getAccountBookId());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                account_book_check_default_cb.setChecked(true);
                                            }
                                        })
                                        .show();
                            }
                        }
                    });
                }
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setView(view)
                .setIcon(R.mipmap.lifa)
                .setPositiveButton(getString(R.string.button_text_save),
                        new OnAddOrEditAccountBookListener(accountBook, accountBook_name_et, true, account_book_check_default_cb))
                .setNegativeButton(getString(R.string.button_text_cancel),
                        new OnAddOrEditAccountBookListener(null, null, false, account_book_check_default_cb))
                .show();
    }

    private class OnAddOrEditAccountBookListener implements DialogInterface.OnClickListener {
        private AccountBook accountBook;
        private EditText accountBookNameEdit;
        private boolean isSaveButton;//是否为保存按钮
        private CheckBox accountBookDefaultCB;

        public OnAddOrEditAccountBookListener(AccountBook accountBook, EditText accountBookNameEdit, boolean isSaveButton, CheckBox accountBookDefaultCB) {
            this.accountBook = accountBook;
            this.accountBookNameEdit = accountBookNameEdit;
            this.isSaveButton = isSaveButton;
            this.accountBookDefaultCB = accountBookDefaultCB;
        }

        public void onClick(DialogInterface dialog, int which) {
            if (!isSaveButton) {
                setAlertDialogIsClose(dialog, true);
                return;
            }
            if (accountBook == null) {
                accountBook = new AccountBook();
            }
            String accountBookName = accountBookNameEdit.getText().toString().trim();
            boolean checkResult = RegexpUtil.isHardRegexpValidate(accountBookName, RegexpUtil.my);
            if (!checkResult) {
                showMessage(getString(R.string.check_text_chinese_english_num, new Object[]{accountBookNameEdit.getHint()}));
                setAlertDialogIsClose(dialog, false);
                return;
            } else {
                setAlertDialogIsClose(dialog, true);
            }

            checkResult = accountBookService.isExistAccountBookByAccountBookName(accountBookName, accountBook.getAccountBookId());
            if (checkResult) {
                showMessage(getString(R.string.check_text_account_book_exist));
                setAlertDialogIsClose(dialog, false);
                return;
            } else {
                setAlertDialogIsClose(dialog, true);
            }
            accountBook.setAccountBookName(accountBookName);
            if (accountBookDefaultCB.isChecked()) {
                accountBook.setIsDefault(1);
            } else {
                accountBook.setIsDefault(0);
            }
            boolean result = false;
            if (accountBook.getAccountBookId() == 0) {
                accountBook.setAccountBookId(((AccountBook) accountBookAdapter.getItem(accountBookAdapter.getCount() - 1)).getAccountBookId() + 1);
                Log.i("提示", accountBook.getAccountBookId() + "");
                result = accountBookService.insertAccountBook(accountBook);
            } else {
                result = accountBookService.updateAccountBook(accountBook);
            }

            if (result) {
                initData();
            } else {
                showMessage(getString(R.string.tips_add_fail));
            }
        }
    }
}
