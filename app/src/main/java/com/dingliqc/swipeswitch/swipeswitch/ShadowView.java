package com.dingliqc.swipeswitch.swipeswitch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

/**
 * 绘制滑动边界的阴影，这样才体现的出来层次感
 * Created by jie on 17/3/25.
 */

public class ShadowView extends View {
    private Drawable mDrawable;

    public ShadowView(Context context) {
        super(context);
        int[] colors=new int[]{0x00000000, 0x9000000,0x15000000, 0x25000000,0x43000000, 0x60000000};
        mDrawable=new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDrawable.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());
        mDrawable.draw(canvas);
    }
}
