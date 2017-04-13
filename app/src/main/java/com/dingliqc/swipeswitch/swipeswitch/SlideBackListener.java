package com.dingliqc.swipeswitch.swipeswitch;
/**
 * 滑动的监听器
 * Created by jie on 17/3/25.
 */

public interface SlideBackListener {

    /**
     * 是否允许滑动返回，在开始滑动的时候调用
     * @return 默认返回true，表示可以返回
     */
    boolean onPreviousSlideBack();
}

