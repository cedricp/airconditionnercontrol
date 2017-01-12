package com.mycode.cedric.airconditionnercontrol;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by cedric on 1/10/17.
 */
public class barSlider extends View {
    private Paint painter = new Paint();
    private float xpos = 0;
    private float range = 20.f;

    OnPositionListener mOnPositionListener = null;

    public interface OnPositionListener {
        void OnPositionChanged(int position);
        void OnPositionSet();
    }

    public void setPos(int p){
        xpos = p / range;
        invalidate();
    }

    public barSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                xpos = event.getX() / getWidth();
                invalidate();
                int currentPos = (int)(xpos * range);
                if (mOnPositionListener != null){
                    mOnPositionListener.OnPositionChanged(currentPos);
                }
                if (mOnPositionListener != null && event.getAction() == android.view.MotionEvent.ACTION_UP)
                    mOnPositionListener.OnPositionSet();
                return true;
            }
        });
    }

    public void setOnPositionListener(@Nullable OnPositionListener listener) {
        mOnPositionListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int x = getWidth();
        int y = getHeight();
        int radius;
        radius = getWidth() / 4;
        painter.setStyle(Paint.Style.FILL);
        painter.setColor(Color.BLACK);
        canvas.drawPaint(painter);

        float rect_height = getHeight() / 2.f;
        float startY = (getHeight() - rect_height) / 2;
        float stepY = getWidth() / range;
        for (int i = 0; i < range; ++i){
            if ((float)i / range < xpos){
                painter.setColor(Color.parseColor("#2353c4"));
            } else {
                painter.setColor(Color.parseColor("#93A8DA"));
            }
            canvas.drawRect((float)(i * stepY), (float)(startY), (float)((i * stepY) + stepY - (stepY / 2)), (float)(startY + rect_height), painter);
        }
    }
}
