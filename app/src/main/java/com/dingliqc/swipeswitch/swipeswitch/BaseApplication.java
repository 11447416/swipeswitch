package com.dingliqc.swipeswitch.swipeswitch;

import android.app.Application;

/**
 * Created by jie on 17/3/25.
 */

public class BaseApplication extends Application{
    private static ActivityManager activityManager =new ActivityManager();
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(activityManager);
    }

    public static ActivityManager getActivityManager() {
        return activityManager;
    }
}
