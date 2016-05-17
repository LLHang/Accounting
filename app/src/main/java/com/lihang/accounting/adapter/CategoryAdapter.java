package com.lihang.accounting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lihang.accounting.R;
import com.lihang.accounting.adapter.base.SimpleBaseAdapter;
import com.lihang.accounting.entitys.Category;
import com.lihang.accounting.entitys.User;
import com.lihang.accounting.service.CategoryService;
import com.lihang.accounting.service.UserService;

import java.util.List;

/**
 * Created by LiHang on 2016/3/1.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {
    private Context context;
    private CategoryService categoryService;
    private List list;

    public CategoryAdapter(Context context) {
        this.context = context;
        categoryService = new CategoryService(context);
        list = categoryService.getNotHideRootCategory();
    }

    public void clear() {
        list.clear();
    }

    public void updateList() {
        list = categoryService.getNotHideRootCategory();
        updateDisplay();
    }

    public void updateDisplay() {
        notifyDataSetChanged();
    }

    private void setList(List list) {
        this.list = list;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Category parentCategory = (Category) getGroup(groupPosition);
        int count = categoryService.getNotHideCountByParentId(parentCategory.getCategoryId());
        return count;
    }

    @Override
    public Object getGroup(int groupPosition) {

        return (Category)list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Category parentCategory = (Category) getGroup(groupPosition);
        List<Category> childList = categoryService.getNotHideCategoryListByParentId(parentCategory.getCategoryId());
        return childList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.category_group_list_item, null);
            groupHolder = new GroupHolder();
            groupHolder.category_group_name_tv = (TextView) convertView.findViewById(R.id.category_group_name_tv);
            groupHolder.category_group_count_tv = (TextView) convertView.findViewById(R.id.category_group_count_tv);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        Category category = (Category) getGroup(groupPosition);

        groupHolder.category_group_name_tv.setText(category.getCategoryName());
        int count = getChildrenCount(groupPosition);
        groupHolder.category_group_count_tv.setText(context.getString(
                R.string.textview_text_children_category,new Object[]{count}));

        return convertView;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.category_children_list_item, null);
            childHolder = new ChildHolder();
            childHolder.category_children_name_tv = (TextView) convertView.findViewById(R.id.category_children_name_tv);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        Category category = (Category) getChild(groupPosition, childPosition);
        childHolder.category_children_name_tv.setText(category.getCategoryName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ChildHolder {
        TextView category_children_name_tv;
    }

    private class GroupHolder {
        TextView category_group_name_tv;
        TextView category_group_count_tv;
    }
}
