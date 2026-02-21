package iti.mad.gusto.presentation.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import iti.mad.gusto.R;

public class ImageUtil {
    public static void loadFromNetwork(
            Context context,
            ImageView imageView,
            String imageUrl
    ) {
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo_white)
                .into(imageView);
    }

    public static void loadFromNetworkWithMatchingBackground(
            Context context,
            ImageView imageView,
            String imageUrl
    ) {
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo_white)
                .into(new CustomTarget<Bitmap>() {
                    private Disposable colorDisposable;

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);

                        colorDisposable = ColorUtil.getColorFromBitmap(context, resource)
                                .subscribe(
                                        imageView::setBackgroundColor,
                                        error -> Log.e("ImageUtil", "Color extraction failed: " + error.getMessage())
                                );

                        imageView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                            @Override
                            public void onViewAttachedToWindow(@NonNull View v) {
                            }

                            @Override
                            public void onViewDetachedFromWindow(@NonNull View v) {
                                if (colorDisposable != null && !colorDisposable.isDisposed()) {
                                    colorDisposable.dispose();
                                }
                                imageView.removeOnAttachStateChangeListener(this);
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        imageView.setImageBitmap(null);
                        imageView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent, context.getTheme()));

                        if (colorDisposable != null && !colorDisposable.isDisposed()) {
                            colorDisposable.dispose();
                        }
                    }
                });
    }
}