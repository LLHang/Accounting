package com.lihang.accounting.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.lihang.accounting.R;
import com.lihang.accounting.activity.base.BaseActivity;
import com.lihang.accounting.activity.base.FrameActivity;
import com.lihang.accounting.entitys.CategoryTotal;
import com.lihang.accounting.service.CategoryService;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.List;

/**
 * Created by HARRY on 2016/3/15.
 */
public class CategoryCharActivity extends FrameActivity{
    private List<CategoryTotal> categoryTotalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
        View pie_view = categoryStatistics();
        appendMainBody(pie_view);
        removeBottomBox();
        setTitle();
    }

    private void initVariable() {
        categoryTotalList = (List<CategoryTotal>) getIntent().getSerializableExtra("total");
    }

    private void setTitle() {
        setTopBarTitle(getString(R.string.category_total));
    }

    private View categoryStatistics() {
        int[] color = new int[]{Color.parseColor("#FF5552"),
                Color.parseColor("#FF0052"),
                Color.parseColor("#005552"),
                Color.parseColor("#FF5500"),
                Color.parseColor("#000052"),
                Color.parseColor("#FF0000"),};
        //调用自定义的构建渲染器方法得到一个渲染器
        DefaultRenderer defaultRenderer = buildCategoryRenderer(color);
        //获取数据源
        CategorySeries categorySeries = buildCategoryDataset("测试饼图", categoryTotalList);
        View pie_view = ChartFactory.getPieChartView(this,
                categorySeries, defaultRenderer);
        return pie_view;
    }

    //构建渲染器
    private DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        //显示缩放按钮，默认为false
        renderer.setZoomButtonsVisible(true);
        //设置图标的标题
        renderer.setChartTitle("消费类别统计");
        //设置图标标题的文字大小
        renderer.setChartTitleTextSize(30);
        //设置标签文字颜色
        renderer.setLabelsColor(Color.BLUE);
        //设置标签文字大小，就是蓝色字的大小
        renderer.setLabelsTextSize(15);
        //设置介绍说明文字的大小
        renderer.setLegendTextSize(15);
        //?
        renderer.setMargins(new int[]{20,30,15,10});
        int color = 0;
        for (int i = 0; i < categoryTotalList.size(); i++) {
            //每条数据就生成一个扇瓣
            SimpleSeriesRenderer ssr = new SimpleSeriesRenderer();
            //设置扇瓣颜色
            ssr.setColor(colors[color]);
            renderer.addSeriesRenderer(ssr);
            color++;
            if (color >= colors.length) {
                color = 0;
            }
        }
        return renderer;
    }

    //构建数据源
    private CategorySeries buildCategoryDataset(String title, List<CategoryTotal> values) {
        CategorySeries categorySeries = new CategorySeries(title);
        for (CategoryTotal value : values) {
            categorySeries.add(value.CategoryName+":"+value.count+"条\r\n合计："
            +value.sumAmount+"元",
                    Double.parseDouble(value.sumAmount));
        }
        return categorySeries;
    }
}
