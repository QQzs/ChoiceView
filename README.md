# ChoiceView
Android 一个RecyclerView实现筛选列表
### 效果图
![choice.gif](https://upload-images.jianshu.io/upload_images/3183047-0e0163222ed5f6e3.gif?imageMogr2/auto-orient/strip)
### 简介
如上图展示的内容，筛选条件的功能很常见，一般情况如果条件很多，那么布局文件就会写的很复杂，这篇文章可以提供一个简洁的方案，布局文件只用一个RecyclerView就可以了。当然，处理逻辑可能要少费点功夫，不过这些逻辑可以复用，如果有多个地方用到就省很多事了。
### 代码分析
先看看Bean类的处理：
数据中有几个必须要添加的属性
type：用于区分是标题item还是内容item，标题item也可以分很多类。
choice：标记item是否选中
multiChoice：这一类型标签是不是多选
allChoice：标签item是不是全选按钮（根据需求调整）

```Java
public class GridItemBean {

    /**
     * type:
     * 标题item 自定义
     * 内容item 默认为0
     */
    private int type;
    /**
     * 标题（标题item）
     */
    private String title;

    private String id;
    private String name;
    /**
     * 内容item 是否选择（内容item）
     */
    private boolean choice;
    /**
     * 是否是多选（标题item）
     */
    private boolean multiChoice;
    /**
     * 是否是全选按钮（内容item）
     */
    private boolean allChoice;

    public GridItemBean(int type, String title) {
        this.type = type;
        this.title = title;
    }

    public GridItemBean(int type, String title, boolean multiChoice) {
        this.type = type;
        this.title = title;
        this.multiChoice = multiChoice;
    }

    public GridItemBean(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public GridItemBean(String id, String name, boolean allChoice) {
        this.id = id;
        this.name = name;
        this.allChoice = allChoice;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public boolean isMultiChoice() {
        return multiChoice;
    }

    public void setMultiChoice(boolean multiChoice) {
        this.multiChoice = multiChoice;
    }

    public boolean isAllChoice() {
        return allChoice;
    }

    public void setAllChoice(boolean allChoice) {
        this.allChoice = allChoice;
    }

    @Override
    public String toString() {
        return "name='" + name + '\'';
    }
}
```

既然布局只是一个RecyclerView，那么逻辑都在adapter中了,首先是adapter的ViewHolder，分为两类，标题类和内容标签，根据bean里面的type来区分，这部分没什么要多说的。
```Java
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
```
下面是处理逻辑，写在了adapter的基类中，方便复用，布局毕竟是都不一样的，但是逻辑可以通用，具体要按项目需求来定，灵活修改。
```Java
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
```
changeStatus()方法是用来更新item选中的，更新的时候，要先查出当前选的这个标签的所有同类型的坐标，第一个和最后一个，逻辑就是查上一个标题类和下一个标题类，中间所有的标签都是这个类型的，然后根据是多选还是单选，更新标签的choice属性。
getChoiceItem（）和getMultiChoiceItem（）是获取单选或者多选选中的标签，也是要先获取同类标签的第一个和最后一个位置，遍历获取。

最后看看activity中数据的准备（根据需求调整）
```Java
override fun initData() {

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

        var layoutManager : GridLayoutManager = recycler_grid?.layoutManager as GridLayoutManager
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                var type = mAdapter?.getItemViewType(position)
                return if (type == 0) 1 else 4
            }
        }

    }

    fun getData(view: View){

        var data1 = mAdapter?.getMultiChoiceItem(1)
        LogUtil.logShow(data1?.toString())
        var data2 = mAdapter?.getChoiceItem(2)
        LogUtil.logShow(data2?.toString())
        var data3 = mAdapter?.getMultiChoiceItem(3)
        LogUtil.logShow(data3?.toString())

        tv_show?.text = "data1 = " + data1.toString() + "\ndata2 = " + data2.toString() + "\ndata3 = " + data3.toString()
    }


    fun backData(view: View){
        mAdapter?.clearChoice()
        tv_show?.text = ""
    }
```
### 源码地址
[https://github.com/QQzs/ChoiceView](https://github.com/QQzs/ChoiceView)

### 简书地址
https://www.jianshu.com/p/b28bcfdbdca1
