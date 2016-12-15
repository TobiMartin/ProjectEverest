package com.linxu.mounteverest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lin xu on 15.12.2016.
 */

public class CustomSliderView extends View{
    private Rect rectangle;
    private Paint paint;

    public CustomSliderView(Context context) {
        super(context);
        init();
    }

    public CustomSliderView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init();
    }

    public void init(){
        int x = 50;
        int y = 50;
        int sideLength = 200;

        // create a rectangle that we'll draw later
        rectangle = new Rect(x, y, sideLength, sideLength);

        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(Color.GRAY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(rectangle, paint);
    }
}
