package com.phearme.comixkcd;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class BindingAdapters {

    @BindingAdapter("loadWithGlide")
    public static void setImageWithGlide(ImageView view, String imgUrl) {
        if (imgUrl != null) {
            Glide.with(view).load(imgUrl).apply(new RequestOptions().fitCenter().placeholder(R.drawable.ic_xkcdholder)).into(view);
        } else {
            Glide.with(view).load(R.drawable.ic_xkcdholder).apply(new RequestOptions().centerInside().placeholder(R.drawable.ic_xkcdholder)).into(view);
        }
    }
}
