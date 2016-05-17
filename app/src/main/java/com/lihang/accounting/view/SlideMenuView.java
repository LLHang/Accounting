package com.lihang.accounting.view;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lihang.accounting.R;
import com.lihang.accounting.adapter.SlideMenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiHang on 2016/2/26.
 */
public class SlideMenuView {
    private Activity activity;
    private List<SlideMenuItem> menuList;
    private boolean isClosed;
    private RelativeLayout bottomBoxLayout;

    //声明菜单监听器接口
    private OnSlideMenuListener slideMenuListener;

    public void removeBottomBox() {
        RelativeLayout main_rl = (RelativeLayout) activity.findViewById(R.id.main_rl);
        main_rl.removeView(bottomBoxLayout);
        bottomBoxLayout = null;
    }

    //定义菜单监听器接口
    public interface OnSlideMenuListener {
        public abstract void OnSlideMenuItemClick(View view, SlideMenuItem item);
    }

    public SlideMenuView(Activity activity) {
        this.activity = activity;
        if (activity instanceof OnSlideMenuListener) {
            this.slideMenuListener = (OnSlideMenuListener) activity;
        }
        initVariable();
        initView();
        initListeners();
    }

    /**
     * 初始化变量
     */
    private void initVariable() {
        menuList = new ArrayList<>();
        isClosed = true;

    }

    /**
     * 初始化控件
     */
    private void initView() {
        bottomBoxLayout = (RelativeLayout) activity.findViewById(R.id.include_bottom);
    }

    /**
     * 初始化监听事件
     */
    private void initListeners() {
        bottomBoxLayout.setOnClickListener(new OnSlideMenuClick());
        //在点击的模式下能够获取焦点
        //bottomBoxLayout.setFocusableInTouchMode(true);
        bottomBoxLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_UP) {
                    toggle();
                }
                return false;
            }
        });
    }

    private class OnSlideMenuClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            toggle();
        }
    }

    /**
     * 打开菜单
     */
    private void open() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.BELOW, R.id.include_title);
        bottomBoxLayout.setLayoutParams(layoutParams);
        isClosed = false;
    }

    /**
     * 关闭菜单
     */
    public void close() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                150//68是在bottom_box.xml中定义的高度
        );
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bottomBoxLayout.setLayoutParams(layoutParams);
        isClosed = true;
    }

    /**
     * 开关方法控制打开/关闭
     */
    public void toggle() {
        if (isClosed) {
            open();
        } else {
            close();
        }
    }

    /**
     * 添加菜单项
     */

    public void add(SlideMenuItem slideMenuItem) {
        menuList.add(slideMenuItem);
    }

    /**
     * 绑定数据源
     */
    public void bindList() {
        SlideMenuAdapter adapter = new SlideMenuAdapter(activity, menuList);
        ListView listView = (ListView) activity.findViewById(R.id.slide_list_lv);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnSlideMenuItemClick());
    }

    private class OnSlideMenuItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SlideMenuItem slideMenuItem = (SlideMenuItem) parent.getItemAtPosition(position);
            slideMenuListener.OnSlideMenuItemClick(bottomBoxLayout, slideMenuItem);
        }
    }
}
