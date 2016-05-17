package com.lihang.accounting.activity.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lihang.accounting.R;
import com.lihang.accounting.view.SlideMenuItem;
import com.lihang.accounting.view.SlideMenuView;

/**
 * Activity框架类，主要是设定好Activity的整体布局
 */
public class FrameActivity extends BaseActivity {
    private SlideMenuView slideMenuView;
    private ImageButton back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置Activity没有标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //为Activity设置布局文件
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 为框架中的body添加布局
     * @param resId
     */
    protected void appendMainBody(int resId) {
        View view = LayoutInflater.from(this).inflate(resId, null);
        appendMainBody(view);
    }

    private void initView() {
        back_btn = (ImageButton) findViewById(R.id.top_title_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void appendMainBody(View view) {
        LinearLayout mainBody = (LinearLayout) findViewById(R.id.body);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mainBody.addView(view, params);
    }

    /**
     * 创建滑动菜单
     */
    protected void createSlideMenu(int resId) {
        slideMenuView = new SlideMenuView(this);
        //得到数据源
        String[] menuItemArray = getResources().getStringArray(resId);
        //循环遍历数据源，将其实例化为对应的SlideMenuItem条目将其添加到SlideMenuView中
        for (int i = 0; i < menuItemArray.length; i++) {
            SlideMenuItem item = new SlideMenuItem(i, menuItemArray[i]);
            slideMenuView.add(item);
        }
        //为SlideMenuView添加相应的数据
        slideMenuView.bindList();
    }

    //切换菜单开闭
    protected void slideMenuToggle() {
        slideMenuView.toggle();
    }
    //创建上下文菜单
    protected void createContextMenu(Menu menu) {
        menu.add(0, 1, 0, R.string.menu_text_edit);
        menu.add(0, 2, 0, R.string.menu_text_fail);
    }
    //为自定义标题的TextView设置标题的方法
    protected void setTopBarTitle(String title) {
        TextView top_title_tv = (TextView) findViewById(R.id.top_title_textview);
        top_title_tv.setText(title);
    }

    protected void removeBottomBox() {
        slideMenuView = new SlideMenuView(this);
        slideMenuView.removeBottomBox();
    }

    protected void removeBackButton() {
        back_btn.setVisibility(View.GONE);
    }
}
