package com.example.gustozo.presentation.common.component;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.gustozo.R;
import com.example.gustozo.presentation.common.util.ColorUtil;

public class SecondaryIconButton extends FrameLayout {

    private ImageView iconView;
    private TextView textView;
    private ConstraintLayout container;

    public SecondaryIconButton(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public SecondaryIconButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SecondaryIconButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.btn_secondary_icon, this, true);

        container = findViewById(R.id.container);
        iconView = findViewById(R.id.iv_icon);
        textView = findViewById(R.id.tv_text);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SecondaryIconButton);

            String text = a.getString(R.styleable.SecondaryIconButton_sb_text);
            int iconResId = a.getResourceId(R.styleable.SecondaryIconButton_sb_icon, 0);
            int backgroundColor = a.getColor(R.styleable.SecondaryIconButton_sb_backgroundColor, Color.WHITE);
            int textColor = a.getColor(R.styleable.SecondaryIconButton_sb_textColor, Color.BLACK);
            int iconTint = a.getColor(R.styleable.SecondaryIconButton_sb_iconTint, 0);

            float cornerRadius = a.getDimension(R.styleable.SecondaryIconButton_sb_cornerRadius, 0f);
            int strokeColor = a.getColor(R.styleable.SecondaryIconButton_sb_strokeColor, Color.TRANSPARENT);
            int strokeWidth = (int) a.getDimension(R.styleable.SecondaryIconButton_sb_strokeWidth, 0f);

            if (text != null && !text.isEmpty()) {
                textView.setText(text);
                textView.setTextColor(textColor);
                textView.setVisibility(VISIBLE);
            } else {
                textView.setVisibility(GONE);
            }

            if (iconResId != 0) {
                iconView.setImageResource(iconResId);
                if (iconTint != 0) {
                    iconView.setColorFilter(iconTint);
                }
            } else {
                iconView.setVisibility(GONE);
            }

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(cornerRadius);
            shape.setColor(backgroundColor);
            if (strokeWidth > 0) {
                shape.setStroke(strokeWidth, strokeColor);
            }

            ColorStateList rippleStateList = ColorStateList.valueOf(ColorUtil.adjustAlpha(strokeColor, 0.3f));
            Drawable finalDrawable = new RippleDrawable(rippleStateList, shape, null);

            container.setBackground(finalDrawable);

            a.recycle();
        }
    }
}