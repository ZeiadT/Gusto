package com.example.gustozo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.example.gustozo.R;

public class PolygonView extends View {

    private Paint paint;
    private Path path;

    private float gapOffsetPercentage;


    public PolygonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Pass the attributes to init
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        path = new Path();
        gapOffsetPercentage = 0.2f;

        int defaultColor = Color.parseColor("#CA2E55");
        int color = defaultColor;

        if (attrs != null) {
            TypedArray attrArray = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.PolygonView,
                    0, 0);

            try {
                color = attrArray.getColor(R.styleable.PolygonView_color, defaultColor);
            } finally {
                attrArray.recycle();
            }
        }

        paint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();

        path.reset();

        path.moveTo(0, h);
        path.lineTo(w, h - h * gapOffsetPercentage);
        path.lineTo(w, 0);
        path.lineTo(0, h * gapOffsetPercentage);
        path.close();

        canvas.drawPath(path, paint);
    }

    public float getGapOffsetPercentage() {
        return gapOffsetPercentage;
    }
}