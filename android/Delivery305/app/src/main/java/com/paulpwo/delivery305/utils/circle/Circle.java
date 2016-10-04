package com.paulpwo.delivery305.utils.circle;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Keep;
import android.util.AttributeSet;
import android.view.View;

import com.paulpwo.delivery305.R;

/**
 * Created by paulpwo on 12/7/16.
 */
@Keep
public class Circle extends View {

    private static final int START_ANGLE_POINT = 20;

    private final Paint paint;
    private final RectF rect;
    private final RectF rect2;

    private float angle;
    public Integer _width = 300;
    public Integer _height = 300;
    Boolean mShowText;
    Integer size;
    Integer stroke;
    ColorStateList color;

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);


        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Circle,
                0, 0);

        try {
            mShowText = a.getBoolean(R.styleable.Circle_showText, false);
            size = a.getInteger(R.styleable.Circle_size, 0);
            stroke = a.getInteger(R.styleable.Circle_stroke, 0);
            color = a.getColorStateList(R.styleable.Circle_color);
            angle = a.getInteger(R.styleable.Circle_initAngle,300);

        } finally {
            a.recycle();
        }

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stroke);
        //Circle color
        paint.setColor(color.getDefaultColor());

        //size 200x200 example
        rect = new RectF(stroke, stroke, size + stroke, size + stroke);
        rect2 = new RectF(50, 50, size - stroke, size - stroke);


    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(rect, START_ANGLE_POINT, angle, false, paint);
        //canvas.drawArc(rect2, START_ANGLE_POINT, 380, false, paint);

    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
