package com.lihang.accounting.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lihang.accounting.R;
import com.lihang.accounting.activity.base.FrameActivity;
import com.lihang.accounting.adapter.UserAdapter;
import com.lihang.accounting.entitys.User;
import com.lihang.accounting.service.UserService;
import com.lihang.accounting.utils.RegexpUtil;
import com.lihang.accounting.view.SlideMenuItem;
import com.lihang.accounting.view.SlideMenuView;

public class UserActivity extends FrameActivity implements SlideMenuView.OnSlideMenuListener {
    ListView user_list_lv;
    UserAdapter userAdapter;
    UserService userService;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.activity_user);
        initVariable();
        initView();
        initListeners();
        initData();
        createSlideMenu(R.array.SlidMenuUser);
    }

    private void initVariable() {
        userService = new UserService(this);
    }

    private void initView() {
        user_list_lv = (ListView) findViewById(R.id.user_list_lv);
    }

    private void initListeners() {
        registerForContextMenu(user_list_lv);
    }

    private void initData() {
        if (userAdapter == null) {
            userAdapter = new UserAdapter(this);
            user_list_lv.setAdapter(userAdapter);
        } else {
            userAdapter.clear();
            userAdapter.updateList();
        }
        setTitle();
    }

    private void setTitle() {
        setTopBarTitle(getString(R.string.title_user,
                new Object[]{userAdapter.getCount()}));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        ListAdapter listAdapter = user_list_lv.getAdapter();
        user = (User) listAdapter.getItem(acmi.position);
        menu.setHeaderIcon(R.mipmap.icon_zhichu_type_canyin);
        menu.setHeaderTitle(user.getUserName());
        createContextMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1://修改
                showUserAddOrEditDialog(user);
                break;
            case 2://删除
                delete();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void delete() {
        String msg = getString(R.string.dialog_message_user_delete, new Object[]{user.getUserName()});
        showAlertDialog(R.string.dialog_title_delete, msg, new OnDeleteClickListener());
    }

    private class OnDeleteClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            boolean result = userService.hideUserByUserId(user.getUserId());
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
            showUserAddOrEditDialog(null);
        }
    }

    private void showUserAddOrEditDialog(User user) {
        View view = getInflater().inflate(R.layout.user_add_or_edit, null);
        EditText user_name_et = (EditText) view.findViewById(R.id.user_name_et);
        String title;
        if (user == null) {
            title = getString(R.string.dialog_title_user,
                    new Object[]{getString(R.string.title_add)});
        } else {
            user_name_et.setText(user.getUserName());
            title = getString(R.string.dialog_title_user,
                    new Object[]{getString(R.string.title_edit)});
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setView(view)
                .setIcon(R.mipmap.lifa)
                .setPositiveButton(getString(R.string.button_text_save),
                        new OnAddOrEditUserListener(user, user_name_et, true))
                .setNegativeButton(getString(R.string.button_text_cancel),
                        new OnAddOrEditUserListener(null, null, false))
                .show();
    }


    private class OnAddOrEditUserListener implements DialogInterface.OnClickListener {
        private User user;
        private EditText userNameEdit;
        private boolean isSaveButton;//是否为保存按钮

        public OnAddOrEditUserListener(User user, EditText userNameEdit, boolean isSaveButton) {
            this.user = user;
            this.userNameEdit = userNameEdit;
            this.isSaveButton = isSaveButton;
        }

        public void onClick(DialogInterface dialog, int which) {
            if (!isSaveButton) {
                setAlertDialogIsClose(dialog, true);
                return;
            }
            if (user == null) {
                user = new User();
            }
            String userName = userNameEdit.getText().toString().trim();
            boolean checkResult = RegexpUtil.isHardRegexpValidate(userName, RegexpUtil.my);
            if (!checkResult) {
                showMessage(getString(R.string.check_text_chinese_english_num, new Object[]{userNameEdit.getHint()}));
                setAlertDialogIsClose(dialog, false);
                return;
            } else {
                setAlertDialogIsClose(dialog, true);
            }

            checkResult = userService.isExistUserByUserName(userName, user.getUserId());
            if (checkResult) {
                showMessage(getString(R.string.check_text_user_exist));
                setAlertDialogIsClose(dialog, false);
                return;
            } else {
                setAlertDialogIsClose(dialog, true);
            }

            user.setUserName(userName);
            boolean result = false;
            if (user.getUserId() == 0) {
                result = userService.insertUser(user);
            } else {
                result = userService.updateUser(user);
            }

            if (result) {
                initData();
            } else {
                showMessage(getString(R.string.tips_add_fail));
            }
        }
    }
}
