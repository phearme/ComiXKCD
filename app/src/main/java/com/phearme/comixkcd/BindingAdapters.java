package com.phearme.comixkcd;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

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

    @BindingAdapter("fadevisible")
    public static void setFadeVisible(View view, boolean visible) {
        if (view.getVisibility() == View.VISIBLE && visible) { return; }
        if (view.getVisibility() == View.GONE && !visible) { return; }
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), visible ? R.anim.fade_in : R.anim.fade_out));

    }
}
