package com.dingliqc.swipeswitch.swipeswitch;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * 一个缓存试图，主要是用来显示，欺骗用户的眼睛
 * Created by jie on 17/3/25.
 */

public class CacheView extends View {
    private View view;
    public CacheView(Context context,View view) {
        super(context);
        this.view=view;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        view.draw(canvas);
    }
}
