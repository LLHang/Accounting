package com.lihang.accounting.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.nfc.tech.IsoDep;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.lihang.accounting.R;
import com.lihang.accounting.database.dao.CategoryDAO;
import com.lihang.accounting.database.dao.UserDAO;
import com.lihang.accounting.entitys.Category;
import com.lihang.accounting.entitys.CategoryTotal;
import com.lihang.accounting.service.base.BaseService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiHang on 2016/3/1.
 */
public class CategoryService extends BaseService {
    private CategoryDAO categoryDAO;

    public CategoryService(Context context) {
        super(context);

        categoryDAO = new CategoryDAO(context);
    }

    //获取所有大类的列表
    public List<Category> getNotHideRootCategory() {
        return categoryDAO.getCategorys(" and parentId = 0 and state = 1");
    }

    //根据父类ID获取子类列表
    public List<Category> getNotHideCategoryListByParentId(int parentId) {
        return categoryDAO.getCategorys(" and parentId = " + parentId + " and state = 1");
    }

    //根据父类ID获取未隐藏的子类总数
    public int getNotHideCountByParentId(int parentId) {
        return categoryDAO.getCount(" and parentId = " + parentId + " and state = 1");
    }

    public int getNotHideCount() {
        return categoryDAO.getCount(" and parentId = 0 and state = 1");
    }

    public ArrayAdapter<Category> getRootCategoryArrayAdapter() {
        List<Category> list = getNotHideRootCategory();
        list.add(0, new Category(0, context.getString(R.string.spinner_please_choose)));
        ArrayAdapter<Category> arrayAdapter = new ArrayAdapter(context, R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return arrayAdapter;
    }

    public boolean insertCategory(Category category) {
        categoryDAO.beginTransaction();
        try {
            boolean result = false;
            category.setCategoryId((int) categoryDAO.insertCategoryReNewId(category));
            result = category.getCategoryId() > 0 ? true : false;
            boolean result2 = false;
            Category parentCategory = getParentCategoryByCategoryParentId(category.getParentId());
            String path;
            if (parentCategory != null) {
                path = parentCategory.getPath() + category.getCategoryId() + ".";
            } else {
                path = category.getCategoryId() + ".";
            }
            category.setPath(path);

            result2 = updateCategory(category);
            if (result && result2) {
                categoryDAO.setTransactionSuccessful();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            categoryDAO.endTransaction();
        }
    }

    private Category getParentCategoryByCategoryParentId(int parentId) {
        List<Category> categorys = categoryDAO.getCategorys(" and categoryId = " + parentId);
        if (categorys.size() == 0) {
            return null;
        }
        return categorys.get(0);
    }

    public boolean updateCategory(Category category) {
        if (category.getParentId() != 0) {
            category.setPath(category.getParentId() + "." + category.getCategoryId() + ".");
        }
        boolean result = categoryDAO.updateCategory(" and categoryId = " + category.getCategoryId(), category);

        return result;
    }

    public boolean hideCategoryByPath(String path) {
        String condition = " and path like '" + path + "%'";
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", 0);
        boolean result = categoryDAO.updateCategory(condition, contentValues);

        return result;
    }

    public ArrayAdapter<Category> getAllCategoryArrayAdapter() {
        List<Category> list = getNotHideCategory();
        ArrayAdapter<Category> arrayAdapter = new ArrayAdapter<Category>(context, R.layout.common_auto_complete, list);
        return arrayAdapter;
    }

    /**
     * 获取所有未隐藏类别
     *
     * @return
     */
    private List<Category> getNotHideCategory() {
        return categoryDAO.getCategorys(" and state = 1 ");
    }

    public String getCategoryNameById(int categoryId) {
        return categoryDAO.getCategorys(" and categoryId = " + categoryId).get(0).getCategoryName();
    }

    public String getCategroyPathById(Integer categoryId) {
        return categoryDAO.getCategorys(" and categoryId = " + categoryId).get(0).getPath();
    }

    public List<CategoryTotal> getCategoryTotalByRootCategory() {
        String condition = " and parentId = 0 and state = 1";
        return getCategoryTotal(condition);
    }

    public List<CategoryTotal> getCategoryTotalByParentId(int parentId) {
        String condition = " and parentId = " + parentId + " and state = 1";
        return getCategoryTotal(condition);
    }

    public List<CategoryTotal> getCategoryTotal(String condition) {
        String sql = "select count(payoutId) as count, sum(amount) as sumAmount," +
                " categoryName from v_payout where 1=1 " + condition +
                " group by categoryId";
        Cursor cursor = categoryDAO.execSql(sql);
        List<CategoryTotal> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            CategoryTotal categoryTotal = new CategoryTotal();
            categoryTotal.count = cursor.getString(cursor.getColumnIndex("count"));
            categoryTotal.sumAmount = cursor.getString(cursor.getColumnIndex("sumAmount"));
            categoryTotal.CategoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
            list.add(categoryTotal);
        }
        return list;
    }
}
