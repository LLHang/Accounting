package com.lihang.accounting.activity;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.lihang.accounting.R;
import com.lihang.accounting.activity.base.FrameActivity;
import com.lihang.accounting.adapter.CategoryAdapter;
import com.lihang.accounting.entitys.Category;
import com.lihang.accounting.service.CategoryService;
import com.lihang.accounting.utils.RegexpUtil;
import com.lihang.accounting.view.MyDialog;
import com.lihang.accounting.view.SlideMenuItem;
import com.lihang.accounting.view.SlideMenuView;

public class CategoryAddOrEditActivity extends FrameActivity implements View.OnClickListener {
    Button category_save_btn, category_cancel_btn;
    EditText category_name_et;
    Spinner category_parentid_sp;

    private CategoryService categoryService;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.category_add_or_edit);
        initVariable();
        initView();
        initListeners();
        bindData();
        setTitle();
        removeBottomBox();
    }

    private void initVariable() {
        categoryService = new CategoryService(this);
        //从Intent当中传过来的自定义类
        category = (Category) getIntent().getSerializableExtra("category");
    }

    private void initView() {
        category_cancel_btn = (Button) findViewById(R.id.category_cancel_btn);
        category_save_btn = (Button) findViewById(R.id.category_save_btn);
        category_name_et = (EditText) findViewById(R.id.category_name_et);
        category_parentid_sp = (Spinner) findViewById(R.id.category_parentid_sp);

    }

    private void initListeners() {
        category_cancel_btn.setOnClickListener(this);
        category_save_btn.setOnClickListener(this);
    }

    private void bindData() {
        ArrayAdapter<Category> arrayAdapter = categoryService.getRootCategoryArrayAdapter();
        Log.i("提示", arrayAdapter.getCount() + "");
        category_parentid_sp.setAdapter(arrayAdapter);
    }

    private void setTitle() {
        String title;
        if (category == null) {
            title = getString(R.string.title_category_add_or_edit,
                    new Object[]{getString(R.string.title_add)});

        } else {
            title = getString(R.string.title_category_add_or_edit,
                    new Object[]{getString(R.string.title_edit)});
            initData(category);
        }
        setTopBarTitle(title);
    }

    private void initData(Category category) {
        category_name_et.setText(category.getCategoryName());
        ArrayAdapter<Category> arrayAdapter = (ArrayAdapter<Category>) category_parentid_sp.getAdapter();
        if (category.getParentId() != 0) {
            int position = 0;
            for (int i = 0; i < arrayAdapter.getCount(); i++) {
                Category categoryItem = arrayAdapter.getItem(i);
                if (categoryItem.getCategoryId() == category.getParentId()) {
                    position = arrayAdapter.getPosition(categoryItem);
                    break;
                }
            }
            category_parentid_sp.setSelection(position);
        } else {
            int count = categoryService.getNotHideCountByParentId(category.getCategoryId());
            if (count != 0) {
                category_parentid_sp.setEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.category_save_btn://保存
                addOrEditCategory();
                break;
            case R.id.category_cancel_btn://取消
                finish();
                break;
        }
    }

    private void addOrEditCategory() {
        String categoryName = category_name_et.getText().toString().trim();
        boolean checkResult = RegexpUtil.isHardRegexpValidate(categoryName, RegexpUtil.letter_number_regexp);
        if (!checkResult) {
            showMessage(getString(R.string.check_text_chinese_english_num, new Object[]{getString(R.string.textview_text_category_name)}));
            return;
        }
        if (category == null) {
            category = new Category();
            category.setPath("");
        }
        category.setCategoryName(categoryName);
        if (!getString(R.string.spinner_please_choose).equals(category_parentid_sp.getSelectedItem().toString())) {
            Category parentCategory = (Category) category_parentid_sp.getSelectedItem();

            if (parentCategory != null) {
                category.setParentId(parentCategory.getCategoryId());
            }
        } else {
            category.setParentId(0);
        }
        boolean result = false;
        if (category.getCategoryId() == 0) {
            result = categoryService.insertCategory(category);
        } else {
            if (category.getCategoryId() == ((Category) category_parentid_sp.getSelectedItem()).getCategoryId()) {
                showMessage(getString(R.string.tips_not_category));
                result = false;
            } else {
                result = categoryService.updateCategory(category);
            }
        }

        if (result) {
            showMessage(getString(R.string.tips_add_success));
            finish();
        } else {
            showMessage(getString(R.string.tips_add_fail));
        }
    }
}
