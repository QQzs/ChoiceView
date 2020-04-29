package com.zs.choiceview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zs.choiceview.bean.GridItemBean
import com.zs.various.adapter.ChoiceAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mData: MutableList<GridItemBean> = mutableListOf()
    var mAdapter: ChoiceAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
    }

    fun initData(){

        mData.add(GridItemBean(1, "类型（多选）" , true))
        mData.add(GridItemBean("1" , "全部" , true))
        mData.add(GridItemBean("2" , "11"))
        mData.add(GridItemBean("3" , "12"))
        mData.add(GridItemBean("4" , "13"))
        mData.add(GridItemBean("4" , "14"))

        mData.add(GridItemBean(2 , "标准（单选）"))
        mData.add(GridItemBean("5" , "20"))
        mData.add(GridItemBean("6" , "21"))
        mData.add(GridItemBean("7" , "22"))
        mData.add(GridItemBean("8" , "23"))
        mData.add(GridItemBean("8" , "24"))

        mData.add(GridItemBean(3 , "属性（多选）", true))
        mData.add(GridItemBean("5" , "30"))
        mData.add(GridItemBean("6" , "31"))
        mData.add(GridItemBean("7" , "32"))
        mData.add(GridItemBean("8" , "33"))
        mData.add(GridItemBean("8" , "34"))

        mAdapter = ChoiceAdapter()
        mAdapter?.initData(mData)

        RecyclerViewUtil.initGrid(this, recycler_grid , mAdapter,4)

        var layoutManager : androidx.recyclerview.widget.GridLayoutManager = recycler_grid?.layoutManager as androidx.recyclerview.widget.GridLayoutManager
        layoutManager.spanSizeLookup = object : androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                var type = mAdapter?.getItemViewType(position)
                return if (type == 0) 1 else 4
            }
        }
    }

    fun getData(view: View){
        var data1 = mAdapter?.getMultiChoiceItem(1)
        var data2 = mAdapter?.getChoiceItem(2)
        var data3 = mAdapter?.getMultiChoiceItem(3)

        tv_show?.text = "data1 = " + data1.toString() + "\ndata2 = " + data2.toString() + "\ndata3 = " + data3.toString()
    }


    fun backData(view: View){
        mAdapter?.clearChoice()
        tv_show?.text = ""
    }
}
