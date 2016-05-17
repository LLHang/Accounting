package com.lihang.accounting.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.lihang.accounting.R;
import com.lihang.accounting.activity.base.FrameActivity;
import com.lihang.accounting.adapter.CategoryAdapter;
import com.lihang.accounting.entitys.Category;
import com.lihang.accounting.entitys.CategoryTotal;
import com.lihang.accounting.service.CategoryService;
import com.lihang.accounting.view.SlideMenuItem;
import com.lihang.accounting.view.SlideMenuView;

import java.io.Serializable;
import java.util.List;

public class CategoryActivity extends FrameActivity implements SlideMenuView.OnSlideMenuListener {
    ExpandableListView category_list_elv;
    CategoryAdapter categoryAdapter;
    CategoryService categoryService;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.category);
        initVariable();
        initView();
        initListeners();
        bindData();
        createSlideMenu(R.array.SlidMenuCategory);
    }

    private void bindData() {
        if (categoryAdapter == null) {
            categoryAdapter = new CategoryAdapter(this);
            category_list_elv.setAdapter(categoryAdapter);
        } else {
            categoryAdapter.clear();
            categoryAdapter.updateList();
        }

        setTitle();
    }

    private void initVariable() {
        categoryService = new CategoryService(this);
    }

    private void initView() {
        category_list_elv = (ExpandableListView) findViewById(R.id.category_list_elv);
    }

    private void initListeners() {
        registerForContextMenu(category_list_elv);
    }

    /*private void initData() {
        if (categoryAdapter == null) {
            categoryAdapter = new CategoryAdapter(this);
            category_list_elv.setAdapter(categoryAdapter);
        } else {
            categoryAdapter.clear();
            categoryAdapter.updateList();
        }
        setTitle();
    }*/

    private void setTitle() {
        int count = categoryService.getNotHideCount();
        setTopBarTitle(getString(R.string.title_category,
                new Object[]{count}));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ExpandableListView.ExpandableListContextMenuInfo elcm = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
        //获取菜单的位置信息
        long position = elcm.packedPosition;
        //要根据位置信息之道它是组还是子，得到一个类型
        int type = ExpandableListView.getPackedPositionType(position);
        //通过位置信息得到组位置
        int groupPosition = ExpandableListView.getPackedPositionGroup(position);
        switch (type) {
            case ExpandableListView.PACKED_POSITION_TYPE_GROUP://如果是组
                //根据组位置得到实体
                category = (Category) categoryAdapter.getGroup(groupPosition);
                break;
            case ExpandableListView.PACKED_POSITION_TYPE_CHILD://如果是子
                //先获取子位置
                int childPosition = ExpandableListView.getPackedPositionChild(position);
                //在获取某组下的某子位置的实体
                category = (Category) categoryAdapter.getChild(groupPosition, childPosition);
                break;
        }
        menu.setHeaderIcon(R.mipmap.icon_shezhi);
        if (category != null) {
            menu.setHeaderTitle(category.getCategoryName());
        }
        createContextMenu(menu);
        menu.add(0, 3, 0, R.string.category_total);
        if (categoryAdapter.getChildrenCount(groupPosition) != 0 && category.getParentId() == 0) {
            menu.findItem(2).setEnabled(false);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case 1://修改
                //showCategoryAddOrEditDialog(category);
                intent = new Intent(this, CategoryAddOrEditActivity.class);
                intent.putExtra("category", category);
                startActivityForResult(intent, 1);
                break;
            case 2://删除
                delete(category);
                break;

            case 3://统计
                List<CategoryTotal> list = categoryService.getCategoryTotalByParentId(category.getParentId());
                intent = new Intent(this, CategoryCharActivity.class);
                intent.putExtra("total", (Serializable) list);
                startActivity(intent);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void delete(Category category) {
        String msg = getString(R.string.dialog_message_category_delete, new Object[]{category.getCategoryName()});
        showAlertDialog(R.string.dialog_title_delete, msg, new OnDeleteClickListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bindData();
        super.onActivityResult(requestCode, resultCode, data);

    }

    private class OnDeleteClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            boolean result = categoryService.hideCategoryByPath(category.getPath());
            if (result) {
                bindData();
            } else {
                //提示删除失败
                showMessage(getString(R.string.tips_delete_fail));
            }
        }
    }

    public void OnSlideMenuItemClick(View view, SlideMenuItem item) {
        slideMenuToggle();
        Intent intent;
        if (item.getItemId() == 0) {
            intent = new Intent(this, CategoryAddOrEditActivity.class);
            startActivityForResult(intent, 1);
            return;
        }
        if (item.getItemId() == 1) {
            List<CategoryTotal> list = categoryService.getCategoryTotalByRootCategory();
            intent = new Intent(this, CategoryCharActivity.class);
            intent.putExtra("total", (Serializable) list);
            startActivity(intent);
        }
    }

    /*private void showCategoryAddOrEditDialog(Category category) {
        View view = getInflater().inflate(R.layout.category_add_or_edit, null);
        EditText category_name_et = (EditText) view.findViewById(R.id.category_name_et);
        String title;
        if (category == null) {
            title = getString(R.string.dialog_title_category,
                    new Object[]{getString(R.string.title_add)});
        } else {
            category_name_et.setText(category.getCategoryName());
            title = getString(R.string.dialog_title_category,
                    new Object[]{getString(R.string.title_edit)});
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setView(view)
                .setIcon(R.mipmap.lifa)
                .setPositiveButton(getString(R.string.button_text_save),
                        new OnAddOrEditCategoryListener(category, category_name_et, true))
                .setNegativeButton(getString(R.string.button_text_cancel),
                        new OnAddOrEditCategoryListener(null, null, false))
                .show();
    }

    private class OnAddOrEditCategoryListener implements DialogInterface.OnClickListener {
        private Category category;
        private EditText categoryNameEdit;
        private boolean isSaveButton;//是否为保存按钮

        public OnAddOrEditCategoryListener(Category category, EditText categoryNameEdit, boolean isSaveButton) {
            this.category = category;
            this.categoryNameEdit = categoryNameEdit;
            this.isSaveButton = isSaveButton;
        }

        public void onClick(DialogInterface dialog, int which) {
            if (!isSaveButton) {
                setAlertDialogIsClose(dialog, true);
                return;
            }
            if (category == null) {
                category = new Category();
            }
            String categoryName = categoryNameEdit.getText().toString().trim();
            boolean checkResult = RegexpUtil.isHardRegexpValidate(categoryName, RegexpUtil.letter_number_regexp);
            if (!checkResult) {
                showMessage(getString(R.string.check_text_chinese_english_num, new Object[]{categoryNameEdit.getHint()}));
                setAlertDialogIsClose(dialog, false);
                return;
            } else {
                setAlertDialogIsClose(dialog, true);
            }

            checkResult = categoryService.isExistCategoryByCategoryName(categoryName, category.getCategoryId());
            if (checkResult) {
                showMessage(getString(R.string.check_text_category_exist));
                setAlertDialogIsClose(dialog, false);
                return;
            } else {
                setAlertDialogIsClose(dialog, true);
            }

            category.setCategoryName(categoryName);
            boolean result = false;
            if (category.getCategoryId() == 0) {
                result = categoryService.insertCategory(category);
            } else {
                result = categoryService.updateCategory(category);
            }

            if (result) {
                initData();
            } else {
                showMessage(getString(R.string.tips_add_fail));
            }
        }
    }*/


}
