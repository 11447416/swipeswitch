# swipeswitch
安卓滑动返回，类似IOS的效果。
# 使用方法
- 使用时把你需要返回的activity直接集成自：BaseSwipeSwitchActivity即可
- 使用了沉浸式菜单，所以布局的顶层xml需要添加：fitsSystemWindows="true"。
- 可以覆盖onPreviousSlideBack()，控制是否允许滑动返回。
- 需要注意的是，如果你的项目中已有Application的全局上下文，你需要整合一下。
# 优点
对已有代码侵入极少，只需要修改积累即可，理论上不会影响到activity的回收。
# 缺陷
本实现仍然存在缺陷，就是只能针对launchMode="standard"方式，如果出现其他方式，可能会造成页面返回不正常。
原因是内部根据activity的生命周期来保存activity的而在实际情况中，因为launchMode的不同，可能会造成任务栈中的activity和ActivityManager中的activity顺序不一样。

优化方法：

在ActivityManager#ActivityManager()中，根据activity的启动模式，保持和系统任务栈一样的顺序。目前没有时间，有时间来研究这个。
