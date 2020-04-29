package com.zs.various.adapter

import androidx.recyclerview.widget.RecyclerView
import com.zs.choiceview.bean.GridItemBean

/**
 *
Created by zs
Date：2019年 01月 15日
Time：10:47
—————————————————————————————————————
About:
—————————————————————————————————————
 */
abstract class ChoiceGridAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mData: MutableList<GridItemBean> = mutableListOf()

    abstract fun initData(mData: MutableList<GridItemBean>)

    override fun getItemViewType(position: Int): Int {
        return mData[position].type
    }

    /**
     * 修改选择条目
     */
    fun changeStatus(position: Int){
        var first = 0               // 同类型的第一条数据位置
        var last = mData.size   //  同类型的最后一条数据位置
        for(index in position downTo 0){
            if (mData[index].type != 0){
                first = index
                break
            }
        }
        for (index in (position + 1) until mData.size){
            if (mData[index].type != 0){
                last = index
                break
            }
        }
        if (last > first){
            if (mData[first].isMultiChoice){
                // 是多选
                if (mData[position].isAllChoice){ // 全选按钮
                    for (index in first until last){
                        mData[index].isChoice = false
                    }
                    mData[position].isChoice = true
                    notifyDataSetChanged()
                }else{
                    for (index in first until last){
                        // 重置全选按钮
                        if (mData[index].isAllChoice){
                            mData[index].isChoice = false
                            notifyItemChanged(index)
                            break
                        }
                    }
                    mData[position].isChoice = true
                    notifyItemChanged(position)
                }
            }else{
                // 是单选
                var currentIndex = 0
                for (index in first until last){
                    // 查找当前选择的位置
                    if (mData[index].isChoice){
                        mData[index].isChoice = false
                        currentIndex = index
                        break
                    }
                }
                notifyItemChanged(currentIndex)
                mData[position].isChoice = true
                notifyItemChanged(position)
            }
        }
    }

    /**
     * 查找选择的条目
     */
    fun getChoiceItem(type: Int): GridItemBean?{
        var first = 0               // 同类型的第一条数据位置
        var last = mData.size   //  同类型的最后一条数据位置
        for (index in mData.indices){
            if (mData[index].type == type){
                first = index
                break
            }
        }
        for (index in (first + 1) until mData.size){
            if (mData[index].type != 0){
                last = index
                break
            }
        }
        for (index in first until last){
            if (mData[index].isChoice){
                return mData[index]
                break
            }
        }
        return null
    }

    /**
     * 查找多选选择的条目
     */
    fun getMultiChoiceItem(type: Int): MutableList<GridItemBean>?{
        var multiData = mutableListOf<GridItemBean>()
        var first = 0               // 同类型的第一条数据位置
        var last = mData.size   //  同类型的最后一条数据位置
        for (index in mData.indices){
            if (mData[index].type == type){
                first = index
                break
            }
        }
        for (index in (first + 1) until mData.size){
            if (mData[index].type != 0){
                last = index
                break
            }
        }
        var allChoice = false
        for (index in first until last){
            // 判断是否全选
            if (mData[index].isAllChoice){
                allChoice = mData[index].isChoice
                break
            }
        }
        if (allChoice){
            // 全选
            for (index in first until last){
                if (mData[index].type == 0 && !mData[index].isAllChoice){
                    multiData.add(mData[index])
                }
            }
        }else{
            // 非全选
            for (index in first until last){
                if (mData[index].isChoice){
                    multiData.add(mData[index])
                }
            }
        }
        return multiData
    }

    /**
     * 重置
     */
    fun clearChoice(){
        for (index in mData.indices){
            mData[index].isChoice = false
            notifyDataSetChanged()
        }
    }

}