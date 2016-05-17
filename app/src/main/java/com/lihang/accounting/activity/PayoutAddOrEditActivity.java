package com.lihang.accounting.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lihang.accounting.R;
import com.lihang.accounting.activity.base.FrameActivity;
import com.lihang.accounting.adapter.AccountBookSelectAdapter;
import com.lihang.accounting.adapter.CategoryAdapter;
import com.lihang.accounting.adapter.UserAdapter;
import com.lihang.accounting.entitys.AccountBook;
import com.lihang.accounting.entitys.Category;
import com.lihang.accounting.entitys.Payout;
import com.lihang.accounting.entitys.User;
import com.lihang.accounting.service.AccountBookService;
import com.lihang.accounting.service.CategoryService;
import com.lihang.accounting.service.PayoutService;
import com.lihang.accounting.service.UserService;
import com.lihang.accounting.utils.DateUtil;
import com.lihang.accounting.utils.RegexpUtil;
import com.lihang.accounting.view.MyDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PayoutAddOrEditActivity extends FrameActivity implements View.OnClickListener {
    Button payout_save_btn, payout_cancel_btn, payout_select_account_book_btn,
            payout_enter_amount_btn, payout_select_category_btn, payout_select_date_btn,
            payout_select_type_btn, payout_select_user_btn;
    EditText payout_select_account_book_et, payout_enter_amount_et, payout_select_date_et,
            payout_select_type_et, payout_select_user_et, payout_comment_et;

    AutoCompleteTextView payout_select_category_actv;

    //private PayoutService payoutService;
    private AccountBookService accountBookService;
    private CategoryService categoryService;
    private UserService userService;
    private PayoutService payoutService;

    private AccountBook accountBook;
    private Payout payout;

    private Integer accountBookId;
    private Integer categoryId;
    private String payoutUserId;
    private String payoutTypeArray[];
    private List<LinearLayout> itemColor;
    private List<User> userSelectedList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.payout_add_or_edit);
        initVariable();
        initView();
        initListeners();
        bindData();
        setTitle();
        removeBottomBox();
    }

    private void initVariable() {
        payoutService = new PayoutService(this);
        accountBookService = new AccountBookService(this);
        categoryService = new CategoryService(this);
        userService = new UserService(this);
        //从Intent当中传过来的自定义类
        payout = (Payout) getIntent().getSerializableExtra("payout");
        accountBook = accountBookService.getDefaultAccountBook();
    }

    private void initView() {
        payout_cancel_btn = (Button) findViewById(R.id.payout_cancel_btn);
        payout_save_btn = (Button) findViewById(R.id.payout_save_btn);
        payout_enter_amount_btn = (Button) findViewById(R.id.payout_enter_amount_btn);
        payout_select_category_btn = (Button) findViewById(R.id.payout_select_category_btn);
        payout_select_date_btn = (Button) findViewById(R.id.payout_select_date_btn);
        payout_select_type_btn = (Button) findViewById(R.id.payout_select_type_btn);
        payout_select_user_btn = (Button) findViewById(R.id.payout_select_user_btn);
        payout_select_account_book_btn = (Button) findViewById(R.id.payout_select_account_book_btn);
        payout_comment_et = (EditText) findViewById(R.id.payout_comment_et);
        payout_enter_amount_et = (EditText) findViewById(R.id.payout_enter_amount_et);
        payout_select_category_actv = (AutoCompleteTextView) findViewById(R.id.payout_select_category_et);
        payout_select_date_et = (EditText) findViewById(R.id.payout_select_date_et);
        payout_select_type_et = (EditText) findViewById(R.id.payout_select_type_et);
        payout_select_user_et = (EditText) findViewById(R.id.payout_select_user_et);
        payout_select_account_book_et = (EditText) findViewById(R.id.payout_select_account_book_et);

        payout_select_date_et.setInputType(InputType.TYPE_NULL);
    }

    private void initListeners() {
        payout_cancel_btn.setOnClickListener(this);
        payout_save_btn.setOnClickListener(this);
        payout_enter_amount_btn.setOnClickListener(this);
        payout_select_account_book_btn.setOnClickListener(this);
        payout_select_category_btn.setOnClickListener(this);
        payout_select_date_btn.setOnClickListener(this);
        payout_select_type_btn.setOnClickListener(this);
        payout_enter_amount_btn.setOnClickListener(this);
        payout_select_user_btn.setOnClickListener(this);

        payout_select_category_actv.setOnItemClickListener(new OnAutoCompeletTextViewItemClickListener());
    }

    private void bindData() {
        accountBookId = accountBook.getAccountBookId();
        payout_select_account_book_et.setText(accountBook.getAccountBookName());
        payout_select_category_actv.setAdapter(categoryService.getAllCategoryArrayAdapter());
        payout_select_date_et.setText(DateUtil.date2string(new Date(), DateUtil.YYYY_MM_DD));
        payoutTypeArray = getResources().getStringArray(R.array.PayoutType);
        payout_select_type_et.setText(payoutTypeArray[0]);

    }

    private void setTitle() {
        String title;
        if (payout == null) {
            title = getString(R.string.title_payout_add_or_edit,
                    new Object[]{getString(R.string.title_add)});
        } else {
            title = getString(R.string.title_payout_add_or_edit,
                    new Object[]{getString(R.string.title_edit)});
            initData(payout);
        }
        setTopBarTitle(title);
    }

    private void initData(Payout payout) {
        payout_select_account_book_et.setText(payout.getAccountBookName());
        accountBookId = payout.getAccountBookId();
        payout_enter_amount_et.setText(payout.getAmount().toString());
        payout_select_category_actv.setText(payout.getCategoryName());
        categoryId = payout.getCategoryId();
        payout_select_date_et.setText(DateUtil.date2string(payout.getPayoutDate(), DateUtil.YYYY_MM_DD));
        payout_select_type_et.setText(payout.getPayoutType());
        String userName = userService.getUserNameByUserId(payout.getPayoutUserId());
        payout_select_user_et.setText(userName);
        payoutUserId = payout.getPayoutUserId();
        payout_comment_et.setText(payout.getComment());

    }

    private void addOrEditPayout() {
        boolean checkResult = checkData();
        if (!checkResult) {
            return;
        }
        if (payout == null) {
            payout = new Payout();
        }
        payout.setAccountBookId(accountBookId);
        payout.setCategoryId(categoryId);
        payout.setAmount(new BigDecimal(payout_enter_amount_et.getText().toString().trim()));
        payout.setPayoutDate(DateUtil.string2date(payout_select_date_et.getText().toString().trim(),
                DateUtil.YYYY_MM_DD));
        payout.setPayoutType(payout_select_type_et.getText().toString().trim());
        payout.setPayoutUserId(payoutUserId);
        payout.setComment(payout_comment_et.getText().toString().trim());

        boolean result = false;
        if (payout.getPayoutId() == 0) {
            result = payoutService.insertPayout(payout);
        } else {
            result = payoutService.updatePayout(payout);
        }

        if (result&&checkResult) {
            showMessage(getString(R.string.tips_add_success));
            finish();
        } else {
            showMessage(getString(R.string.tips_add_fail));
        }
    }

    private boolean checkData() {
        boolean checkResult = RegexpUtil.isMoney(payout_enter_amount_et.getText().toString().trim());
        if (!checkResult) {
            //获取焦点让用户重填
            payout_enter_amount_et.setText("");
            payout_enter_amount_et.requestFocus();
            showMessage(getString(R.string.check_text_money));
            return false;
        }

        checkResult = RegexpUtil.isNull(categoryId);
        if (checkResult) {
            payout_select_category_btn.setFocusable(true);
            payout_select_category_btn.setFocusableInTouchMode(true);
            payout_select_category_btn.requestFocus();
            showMessage(getString(R.string.check_text_category_is_null));
            return false;
        }

        //日期验证，不许穿越
        checkResult = DateUtil.isAfter(DateUtil.string2date(payout_select_date_et.getText().toString().trim(), DateUtil.YYYY_MM_DD));
        if (checkResult) {
            payout_select_date_btn.setFocusable(true);
            payout_select_date_btn.setFocusableInTouchMode(true);
            payout_select_date_btn.requestFocus();
            showMessage(getString(R.string.check_text_payout_date_is_after));
            return false;
        }


        if (payoutUserId == null) {
            payout_select_user_btn.setFocusable(true);
            payout_select_user_btn.setFocusableInTouchMode(true);
            payout_select_user_btn.requestFocus();
            showMessage(getString(R.string.check_text_user_is_null));
            return false;
        }

        String payoutType = payout_select_type_et.getText().toString().trim();
        if (payoutType.equals(payoutTypeArray[0]) || payoutType.equals(payoutTypeArray[1])) {
            if (payoutUserId.split(",").length <= 1) {
                payout_select_user_btn.setFocusable(true);
                payout_select_user_btn.setFocusableInTouchMode(true);
                payout_select_user_btn.requestFocus();
                showMessage(getString(R.string.check_text_payout_user));
                return false;
            }
        } else {
            if ("".equals(payoutUserId)) {
                payout_select_user_btn.setFocusable(true);
                payout_select_user_btn.setFocusableInTouchMode(true);
                payout_select_user_btn.requestFocus();
                showMessage(getString(R.string.check_text_payout_user2));
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payout_select_account_book_btn://选择账本
                showAccountBookSelectDialog();
                break;
            case R.id.payout_enter_amount_btn://输入金额
                MyDialog myDialog = new MyDialog(this);
                myDialog.show();
                myDialog.setOnMyDialogDisMiss(new MyDialog.MydialogDisMiss() {
                    @Override
                    public void setString(String amount) {
                        payout_enter_amount_et.setText(amount);
                    }
                });
                break;
            case R.id.payout_select_category_btn://选择类别
                showCategorySelectDialog();
                break;
            case R.id.payout_select_date_btn://选择日期:
                Calendar calendar = Calendar.getInstance();
                showDateSelectDialog(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                //showDateSelectDialog(calendar.get(Calendar.YEAR),
                //calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                break;
            case R.id.payout_select_type_btn://计算方式
                showPayoutTypeSelectDialog();
                break;
            case R.id.payout_select_user_btn://选择消费人
                showUserSelectDialog(payout_select_type_et.getText().toString().trim());
                break;
            case R.id.payout_save_btn://保存
                addOrEditPayout();
                break;
            case R.id.payout_cancel_btn://取消
                finish();
                break;
        }
    }

    private boolean dateCompare(Date date) {
        if (date.before(new Date()) || date.equals(new Date())) {
            return true;
        }
        return false;
    }

    private class OnAutoCompeletTextViewItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Category category = (Category) parent.getAdapter().getItem(position);
            categoryId = category.getCategoryId();
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

    /**
     * 显示类别选择的Dialog
     */
    private void showCategorySelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getInflater().inflate(R.layout.category, null);
        ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.category_list_elv);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this);
        builder.setView(view)
                .setNegativeButton(getString(R.string.button_text_cancel), null);
        expandableListView.setAdapter(categoryAdapter);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        expandableListView.setOnChildClickListener(new OnCategoryChildItemClickListener(alertDialog, categoryAdapter));
        expandableListView.setOnGroupClickListener(new OnCategoryGroupItemClickListener(alertDialog, categoryAdapter));
    }

    /**
     * 类别选择组监听器
     */
    private class OnCategoryGroupItemClickListener implements ExpandableListView.OnGroupClickListener {
        private AlertDialog alertDialog;
        private CategoryAdapter categoryAdapter;

        public OnCategoryGroupItemClickListener(AlertDialog alertDialog, CategoryAdapter categoryAdapter) {
            this.alertDialog = alertDialog;
            this.categoryAdapter = categoryAdapter;
        }

        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {
            int count = categoryAdapter.getChildrenCount(groupPosition);
            if (count == 0) {
                Category category = (Category) categoryAdapter.getGroup(groupPosition);
                payout_select_category_actv.setText(category.getCategoryName());
                categoryId = category.getCategoryId();
                alertDialog.dismiss();
            }
            return false;
        }
    }

    /**
     * 类别选择Child的监听器
     */
    private class OnCategoryChildItemClickListener implements ExpandableListView.OnChildClickListener {
        private AlertDialog alertDialog;
        private CategoryAdapter categoryAdapter;

        public OnCategoryChildItemClickListener(AlertDialog p_AlertDialog, CategoryAdapter categoryAdapter) {
            this.alertDialog = p_AlertDialog;
            this.categoryAdapter = categoryAdapter;
        }

        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {
            Category category = (Category) categoryAdapter.getChild(groupPosition, childPosition);
            payout_select_category_actv.setText(category.getCategoryName());
            categoryId = category.getCategoryId();
            alertDialog.dismiss();
            return false;
        }

    }

    /**
     * 显示消费类型选择的Dialog
     */
    private void showPayoutTypeSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getInflater().inflate(R.layout.payout_type_list, null);
        ListView listView = (ListView) view.findViewById(R.id.payout_select_dialog_list);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        listView.setOnItemClickListener(new OnPayoutTypeSelectClickListener(alertDialog, payout_select_user_et));
        alertDialog.show();
    }

    private class OnPayoutTypeSelectClickListener implements AdapterView.OnItemClickListener {
        private AlertDialog alertDialog;
        private EditText userSelectEditText;
        private String payoutUsers;

        public OnPayoutTypeSelectClickListener(AlertDialog alertDialog, EditText userSelectEditText) {
            this.alertDialog = alertDialog;
            this.userSelectEditText = userSelectEditText;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String payoutTypeA = (String) parent.getAdapter().getItem(position);
            payoutUsers = userSelectEditText.getText().toString().trim();
            if (payoutTypeA.equals(payoutTypeArray[2])) {
                if (payoutUsers.lastIndexOf(",") != payoutUsers.indexOf(",")) {
                    payoutUsers = payoutUsers.substring(0, payoutUsers.indexOf(","));
                    userSelectEditText.setText(payoutUsers+",");
                    showMessage(getString(R.string.check_text_payout_type_not_user));
                }
            }
            payout_select_type_et.setText(payoutTypeA);
            alertDialog.dismiss();
        }
    }

    /**
     * 弹出用户选择的dialog
     * @param payoutType
     */
    private void showUserSelectDialog(String payoutType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getInflater().inflate(R.layout.activity_user, null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.user_list_ll);
        linearLayout.setBackgroundResource(R.color.blue);
        ListView select_lv = (ListView) view.findViewById(R.id.user_list_lv);
        UserAdapter adapter = new UserAdapter(this);
        select_lv.setAdapter(adapter);
        builder.setTitle(R.string.button_text_select_date)
                .setNegativeButton(R.string.button_text_back, new OnSelectUserBack())
                .setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        select_lv.setOnItemClickListener(new OnUserItemClickListener(dialog, payoutType));
    }

    /**
     * 选择用户的ListView的自定义监听器，如果消费类型是借贷或均分可以选择多人，个人消费只能选择一个人
     */
    private class OnUserItemClickListener implements AdapterView.OnItemClickListener {
        private AlertDialog dialog;
        private String payoutType;

        public OnUserItemClickListener(AlertDialog dialog, String payoutType) {
            this.dialog = dialog;
            this.payoutType = payoutType;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String[] payoutTypeArray = getResources().getStringArray(R.array.PayoutType);
            User user = (User) parent.getAdapter().getItem(position);
            //均分或借贷时可以选择多人
            if (payoutType.equals(payoutTypeArray[0]) || payoutType.equals(payoutTypeArray[1])) {
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.user_item_ll);
                if (itemColor == null && userSelectedList == null) {
                    itemColor = new ArrayList<>();
                    userSelectedList = new ArrayList<>();
                }
                if (itemColor.contains(linearLayout)) {
                    linearLayout.setBackgroundResource(R.color.blue);
                    itemColor.remove(linearLayout);
                    userSelectedList.remove(user);
                } else {
                    linearLayout.setBackgroundResource(R.color.red);
                    itemColor.add(linearLayout);
                    userSelectedList.add(user);
                }
                return;
            }
            //个人消费只能选择一个人
            if (payoutType.equals(payoutTypeArray[2])) {
                userSelectedList = new ArrayList<>();
                userSelectedList.add(user);
                payout_select_user_et.setText("");
                String name = "";
                payoutUserId = "";
                for (int i = 0; i < userSelectedList.size(); i++) {
                    name += userSelectedList.get(i).getUserName()+",";
                    payoutUserId += userSelectedList.get(i).getUserId() + ",";
                }
                payout_select_user_et.setText(name);
                itemColor = null;
                userSelectedList = null;
                dialog.dismiss();
            }
        }
    }

    /**
     * 用户选择后，点击dialog的返回按钮的监听事件，把选择好的用户设置到对应的EditText上
     */
    private class OnSelectUserBack implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            payout_select_user_et.setText("");
            String name = "";
            payoutUserId = "";
            if (userSelectedList != null) {
                for (int i = 0; i < userSelectedList.size(); i++) {
                    name += userSelectedList.get(i).getUserName() + ",";
                    payoutUserId += userSelectedList.get(i).getUserId() + ",";
                }
                payout_select_user_et.setText(name);
            }
            itemColor = null;
            userSelectedList = null;
        }
    }



    private void showDateSelectDialog(int year, int month, int day) {
        //使用系统自带的日期控件
        new DatePickerDialog(this, new OnDateSelectedListener(), year, month, day)
                .show();
    }

    private class OnDateSelectedListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
            payout_select_date_et.setText(DateUtil.date2string(date, DateUtil.YYYY_MM_DD));
        }
    }

    private class OnAccountBookItemClickListener implements AdapterView.OnItemClickListener {
        private AlertDialog dialog;

        public OnAccountBookItemClickListener(AlertDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AccountBook accountBook1 = (AccountBook) parent.getAdapter().getItem(position);
            payout_select_account_book_et.setText(accountBook1.getAccountBookName());
            accountBookId = accountBook1.getAccountBookId();
            dialog.dismiss();
        }
    }



}