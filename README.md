## <font color=#C4573C size=5 face="黑体">需求</font>
### <font color=#C4573C size=4 face="黑体">直播入口</font>

![这里写图片描述](https://github.com/soulrelay/StickyListHeadersWithRefreshAndLoadMore/blob/master/sample/res/raw/1.gif)

### <font color=#C4573C size=4 face="黑体">功能点</font>
>*  下拉刷新历史数据（这里自定义了refresh header，颠球，射门一气呵成~~）
>*  上拉加载更多比赛
>*   时间头悬浮
>*   两个“今天”的定位锚点
>*   内部相关的业务模型采用MVP构建
算是对[Android-architecture之MVC、MVP、MVVM、Data-Binding](http://blog.csdn.net/s003603u/article/details/51393218%20%E2%80%9CAndroid-architecture%E4%B9%8BMVC%E3%80%81MVP%E3%80%81MVVM%E3%80%81Data-Binding%E2%80%9D)中的MVP进一步的升级，抽取出了BasePresenter、BaseView、MVPBaseActivity、MVPBaseFragment，并通过使用弱引用预防可能发生的内存泄露问题
这里就不做赘述，后续有时间会整理一下（基于MVP模型构建多接口，多嵌套复杂数据类型，多视图的的多交互模型）

### <font color=#C4573C size=4 face="黑体">我的预约</font>

![这里写图片描述](https://github.com/soulrelay/StickyListHeadersWithRefreshAndLoadMore/blob/master/sample/res/raw/2.gif)

### <font color=#C4573C size=4 face="黑体">功能点</font>
>* 相对于直播入口少了些相关交互功能
>* 增加了左滑删除取消预约的功能
>* 还有一个隐藏功能，就是多页面的比赛预约同步处理，这里采用[EventBus](http://blog.csdn.net/s003603u/article/details/51852019)事件总线库进行组件间的通信，然后根据通知做相应的业务处理
## <font color=#C4573C size=5 face="黑体">实战</font>
### <font color=#C4573C size=4 face="黑体">控件原型搭建</font>
秉着不重复造轮子的宗旨，使用了如下著名的开源控件
>* 悬浮头置顶功能
    StickyListHeaders 
    An android library for section headers that stick to the top
    https://github.com/emilsjolander/StickyListHeaders
>* 下拉刷新 上拉加载更多
   SwipeToLoadLayout
   A reusable pull-to-refresh and pull-to-loadmore widget
   https://github.com/Aspsine/SwipeToLoadLayout
   >* 左滑删除见代码中的SwipeLayout，暂时找不到来源了

### <font color=#C4573C size=4 face="黑体">最终的效果图</font>
这个是在StickyListHeaders的基础上，整合了SwipeToLoadLayout和SwipeLayout

注意：  基于项目需求对部分开源控件做了相应的调整 

![这里写图片描述](https://github.com/soulrelay/StickyListHeadersWithRefreshAndLoadMore/blob/master/sample/res/raw/3.gif)






