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
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);

                        Disposable d = ColorUtil.getColorFromBitmap(context, resource).subscribe(imageView::setBackgroundColor, t -> {
                            Log.d("TAG", "onResourceReady: " + t.getMessage());
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        imageView.setImageBitmap(null);
                    }
                });

    }
}
