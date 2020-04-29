package com.zs.various.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.zs.choiceview.R
import com.zs.choiceview.bean.GridItemBean
import kotlinx.android.synthetic.main.item_choice_tab.view.*
import kotlinx.android.synthetic.main.item_choice_title.view.*

/**
 *
Created by zs
Date：2019年 01月 16日
Time：17:19
—————————————————————————————————————
About:
—————————————————————————————————————
 */
class ChoiceAdapter : ChoiceGridAdapter() {

    override fun initData(mData: MutableList<GridItemBean>) {
        this.mData = mData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0){
            TabHolder(View.inflate(parent?.context , R.layout.item_choice_tab , null))
        }else{
            TitleHolder(View.inflate(parent?.context , R.layout.item_choice_title , null))
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0){
            (holder as TabHolder).bindData(position)
        }else{
            (holder as TitleHolder).bindData(position)
        }
    }

    inner class TabHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindData(position: Int)= with(itemView){
            var data = mData[position]
            tv_item_tab?.text = data.name
            if (data.isChoice){
                tv_item_tab?.setBackgroundResource(R.mipmap.search_label_bg_sel02)
            }else{
                tv_item_tab?.setBackgroundResource(R.mipmap.search_label_bg_nor)
            }
            setOnClickListener {
                if(data.isChoice){
                    mData[position].isChoice = false
                    notifyItemChanged(position)
                }else{
                    changeStatus(position)
                }
            }
        }

    }

    inner class TitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindData(position: Int) = with(itemView){
            tv_item_title?.text = mData[position].title
        }

    }

}