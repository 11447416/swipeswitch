package com.dingliqc.swipeswitch.swipeswitch;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * 滑动手势管理
 * Created by jie on 17/3/25.
 */

public class SwipeSwitchHelper {
    //可配置信息
    private int effectiveWidth = 80;//滑动识别的距离
    private float parallax = 0.3f;//视差因子
    private float successPercent = 0.3f;//需要滑动多少，才会执行换页，不然就会恢复之前的状态
    private int animationTime = 200;//恢复动画的时间
    private int shadowWidth = 80;//阴影宽度


    private boolean animating = false;//是否在动画中
    private boolean sliding = false;//是否在滑动
    private ViewGroup currentViewGroup;//当前页面容器
    private View currentView;//当前页面的根试图
    private View backgroundView;//背景 颜色的view
    private Activity currentActivity;
    private CacheView previousCacheView;//缓存的前一个页面的试图
    private View shadowView;//阴影
    private float startPos = 0f;//开始滑动的位置
    private float pointX, pointY;//手指按下的位置
    private String TAG = "SwipeSwitchHelper";

    public SwipeSwitchHelper(Activity currentActivity) {
        this.currentActivity = currentActivity;
        //避免activity在加载动画未完成的时候滑动
        sliding = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sliding = false;
            }
        }, 500);
    }

    /**
     * @param ev
     * @return shifou yao
     */
    public boolean slideManager(MotionEvent ev) {
        float rawX = ev.getX() - startPos;//滑动的原始x坐标
        //如果是第二个手指，就不处理，也就是只处理第一个按上去的手指,或者在动画中
        if (animating) return false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointX = ev.getX();
                pointY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float diffX = ev.getX() - pointX;
                float diffY = Math.abs(ev.getY() - pointY);
                if (sliding || (diffX > diffY && pointX < effectiveWidth)) {
                    //是滑动返回的手势
                    if (sliding) {
                        //正在滑动
                        slide(rawX < 0 ? 0 : rawX);//只能向右，如果是滑倒左边，就恢复到没有滑动状态
                    } else {
                        //没有开始滑动
                        if (startSlide()) {
                            sliding = true;
                            startPos = ev.getX();
                        } else {
                            return false;
                        }
                    }
                    return true;//这里表示需要拦截滑动事件
                }
                break;
            case MotionEvent.ACTION_UP:
                //结束滑动
                if (sliding && !animating && null != currentView) {
                    endSlideWithAnimation(currentView.getX() / currentView.getWidth() > successPercent);
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * 开始滑动activity
     *
     * @return 是否支持滑动，不支持，就返回false
     */
    public boolean startSlide() {
        //前面一个页面
        Activity previousActivity = BaseApplication.getActivityManager().getPreviousActivity();
        if (null == previousActivity) return false;
        //找出2个页面的根试图,也就是试图的容器
        ViewGroup previousViewGroup = (ViewGroup) previousActivity.getWindow().getDecorView();
        currentViewGroup = (ViewGroup) currentActivity.getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
        if (currentViewGroup.getChildCount() <= 0) return false;//没有内容
        //当前页面的内容
        currentView = currentViewGroup.getChildAt(0);
        //背景颜色的view
        backgroundView = new View(currentActivity);
        backgroundView.setBackgroundColor(Color.WHITE);
        ViewGroup.LayoutParams paramBack = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //创建前一个页面的快照
        previousCacheView = new CacheView(currentActivity, previousViewGroup);

        //阴影
        shadowView = new ShadowView(currentActivity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(shadowWidth, FrameLayout.LayoutParams.MATCH_PARENT);

        currentViewGroup.addView(backgroundView, 0, paramBack);
        currentViewGroup.addView(shadowView, 0, params);//把阴影添加到当前试图
        currentViewGroup.addView(previousCacheView, 0);
        return true;
    }

    /**
     * 开始跟随手势滑动
     */
    public void slide(float x) {
        if (null == previousCacheView || null == currentView || null == shadowView) return;
        //公式很简单，化简之前：－previousCacheView.getWidth()＊parallax＋x＊parallax
        previousCacheView.setX((x - previousCacheView.getWidth()) * parallax);
        currentView.setX(x);
        backgroundView.setX(x);
        shadowView.setX(x - shadowWidth);
    }

    /**
     * 手指释放以后，切换后者恢复页面的动画
     *
     * @param switchPage 是否切换页面
     */
    public void endSlideWithAnimation(final boolean switchPage) {
        animating = true;
        ValueAnimator animator = ValueAnimator.ofFloat(currentView.getX(), switchPage ? currentView.getWidth() : 0);
        animator.setInterpolator(new DecelerateInterpolator());//开始快，然后慢
        animator.setDuration(animationTime);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                previousCacheView.setX((value - previousCacheView.getWidth()) * parallax);
                currentView.setX(value);
                backgroundView.setX(value);
                shadowView.setX(value - shadowWidth);

            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (switchPage) {
                    currentActivity.onBackPressed();//这里调用返回键，方便统一处理返回事件
                    currentActivity.overridePendingTransition(0, 0);//禁用默认的activity动画
                } else {
                    currentViewGroup.removeView(previousCacheView);
                    currentViewGroup.removeView(shadowView);
                    currentViewGroup.removeView(backgroundView);
                    sliding = false;
                    animating = false;
                    currentView = null;
                    currentViewGroup = null;
                    previousCacheView = null;
                    shadowView = null;
                    backgroundView = null;
                    startPos = 0;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }
}
