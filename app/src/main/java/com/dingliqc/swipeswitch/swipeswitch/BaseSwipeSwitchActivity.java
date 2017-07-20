package com.dingliqc.swipeswitch.swipeswitch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;

/**
 * 滑动返回的基础类，继承自AppCompatActivity
 * Created by jie on 17/3/25.
 */

public class BaseSwipeSwitchActivity extends AppCompatActivity implements SlideBackListener {
    private SwipeSwitchHelper swipeSwitchHelper;
    private boolean canBack = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        swipeSwitchHelper = new SwipeSwitchHelper(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //在手指按下的时候，检测是否可以滑动返回
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                canBack = onPreviousSlideBack();
                if (canBack) {
                    swipeSwitchHelper.slideManager(ev);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (canBack && swipeSwitchHelper.slideManager(ev)) {
                    return true;
                }
            case MotionEvent.ACTION_UP:
                if (canBack&&swipeSwitchHelper.slideManager(ev)) {
                    return true;//直接返回，避免触发其他控件的事件
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 覆盖本方法，确定是否允许滑动返回，默认返回true，表示可以
     *
     * @return
     */
    @Override
    public boolean onPreviousSlideBack() {
        return true;
    }
}