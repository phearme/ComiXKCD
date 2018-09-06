package com.phearme.comixkcd;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.phearme.comixkcd.databinding.ActivityComicViewerBinding;


public class ComicViewerActivity extends AppCompatActivity {
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int FIRST_HIDE_DELAY_MILLIS = 2000;
    private final Handler mHideHandler = new Handler();
    private View mControlsView;
    private final Runnable mHideControlsRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private boolean mControlsVisible;
    private Animation fadeIn, fadeOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ComicViewerViewModel viewModel = new ComicViewerViewModel();
        ActivityComicViewerBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_comic_viewer);
        binding.setViewModel(viewModel);

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        PhotoView photoView = findViewById(R.id.comic_photo_view);
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        String imgUrl = getIntent().getStringExtra("url");
        Glide.with(photoView)
                .load(imgUrl)
                .apply(new RequestOptions().error(R.drawable.ic_xkcdholder).fitCenter())
                .into(photoView);

        mControlsVisible = true;
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
                close();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        delayedHide(FIRST_HIDE_DELAY_MILLIS);
    }

    private void close() {
        finish();
        overridePendingTransition(R.anim.to_left, R.anim.to_left);
    }

    @Override
    public void onBackPressed() {
        close();
    }

    private void hide() {
        mControlsView.setVisibility(View.GONE);
        mControlsView.startAnimation(fadeOut);
        mControlsVisible = false;
    }

    private void show() {
        mControlsVisible = true;
        mControlsView.startAnimation(fadeIn);
        mControlsView.setVisibility(View.VISIBLE);
        delayedHide(AUTO_HIDE_DELAY_MILLIS);
    }

    private void toggle() {
        mHideHandler.removeCallbacks(mHideControlsRunnable);
        if (mControlsVisible) {
            hide();
        } else {
            show();
        }
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideControlsRunnable);
        mHideHandler.postDelayed(mHideControlsRunnable, delayMillis);
    }

}
