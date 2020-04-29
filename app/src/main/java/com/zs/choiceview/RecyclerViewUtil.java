
package com.zs.choiceview;

import android.content.Context;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


/**
 * RecyclerView
 */
public class RecyclerViewUtil {

    /**
     * 竖向分割线
     * @param context
     * @param recyclerView
     * @param adapter
     */
    public static  void init(Context context, RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    /**
     * 没有分割线
     * @param context
     * @param recyclerView
     * @param adapter
     */
    public static  void initNoDecoration(Context context, RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 横向分割线
     * @param context
     * @param recyclerView
     * @param adapter
     */
    public static  void initHorizontal(Context context, RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(adapter);
    }

    /**
     * 当 RecyclerView 外围嵌套 ScrollView 时，将滚动事件交予上层处理
     * @param context
     * @param recyclerView
     * @param adapter
     */
    public static  void initScroll(Context context, RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        if (adapter != null){
            recyclerView.setAdapter(adapter);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 当 RecyclerView 外围嵌套 ScrollView 时，将滚动事件交予上层处理
     * @param context
     * @param recyclerView
     * @param adapter
     * @param i
     */
    public static  void initGridScroll(Context context, RecyclerView recyclerView, RecyclerView.Adapter adapter, int i) {
        GridLayoutManager layoutManager = new GridLayoutManager(context,i);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * GridLayout布局
     * @param context
     * @param recyclerView
     * @param adapter
     * @param i
     */
    public static  void initGrid(Context context, RecyclerView recyclerView, RecyclerView.Adapter adapter, int i) {
        recyclerView.setLayoutManager(new GridLayoutManager(context, i));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 瀑布流布局
     * @param recyclerView
     * @param adapter
     */
    public static void initStaggeredGrid(RecyclerView recyclerView, RecyclerView.Adapter adapter){
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//可防止Item切换
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager.invalidateSpanAssignments(); //防止第一行到顶部有空白区域
            }
        });
    }

}
