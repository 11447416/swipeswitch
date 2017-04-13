package com.dingliqc.swipeswitch.swipeswitch;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理activity的生命周期，主要是把所有的activity都加入到一个列表，方便取用前一个activity
 * Created by jie on 17/3/25.
 */

public class ActivityManager implements Application.ActivityLifecycleCallbacks {
    private static List<Activity> activities = new ArrayList<>();
    private String TAG="ActivityManager";

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        activities.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 返回上一个activity，如果没有就返回空
     *
     * @return
     */
    public Activity getPreviousActivity() {
        if (activities.size() < 2) {
            return null;
        }
        return activities.get(activities.size() - 2);
    }
}
