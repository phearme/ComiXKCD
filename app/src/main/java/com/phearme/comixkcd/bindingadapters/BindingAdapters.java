package com.phearme.comixkcd.bindingadapters;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.phearme.comixkcd.R;

public class BindingAdapters {

    @BindingAdapter("loadWithGlide")
    public static void setImageWithGlide(ImageView view, String imgUrl) {
        if (imgUrl != null) {
            Glide.with(view)
                    .load(imgUrl)
                    .apply(
                            new RequestOptions()
                                    .error(R.drawable.ic_xkcdholder)
                                    .fitCenter()
                                    .placeholder(R.drawable.ic_xkcdholder))
                    .into(view);
        } else {
            Glide.with(view)
                    .load(R.drawable.ic_xkcdholder)
                    .apply(
                            new RequestOptions()
                                    .centerInside()
                                    .placeholder(R.drawable.ic_xkcdholder))
                    .into(view);
        }
    }

    @BindingAdapter("photoViewerWithGlide")
    public static void setPhotoWithGlide(final ImageView view, String imgUrl) {
        Glide.with(view)
                .load(imgUrl)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        ActivityCompat.startPostponedEnterTransition((AppCompatActivity)view.getContext());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ActivityCompat.startPostponedEnterTransition((AppCompatActivity)view.getContext());
                        return false;
                    }
                })
                .apply(
                        new RequestOptions()
                                .error(R.drawable.ic_xkcdholder)
                                .fitCenter())
                .into(view);
    }

    @BindingAdapter("fadevisible")
    public static void setFadeVisible(View view, boolean visible) {
        toggleViewVisibilityWithAnim(view, visible, R.anim.fade_in, R.anim.fade_out);
    }

    @BindingAdapter("slideupdownvisible")
    public static void setSlideVisible(View view, boolean visible) {
        toggleViewVisibilityWithAnim(view, visible, R.anim.slide_down, R.anim.slide_up);
    }

    @BindingAdapter("fadevisibility")
    public static void setFadeVisibility(View view, boolean visible) {
        if (view.getVisibility() == View.VISIBLE && visible) { return; }
        if (view.getVisibility() == View.INVISIBLE && !visible) { return; }
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), visible ? R.anim.fade_in : R.anim.fade_out));
    }

    private static void toggleViewVisibilityWithAnim(View view, boolean visible, int animIn, int animOut) {
        if (view.getVisibility() == View.VISIBLE && visible) { return; }
        if (view.getVisibility() == View.GONE && !visible) { return; }
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), visible ? animIn : animOut));
    }
}
