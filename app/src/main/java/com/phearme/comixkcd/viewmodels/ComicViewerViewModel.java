package com.phearme.comixkcd.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Handler;

import com.phearme.comixkcd.BR;


public class ComicViewerViewModel extends BaseObservable {
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    public static final int FIRST_HIDE_DELAY_MILLIS = 2000;

    private String imgUrl;
    private boolean closeButtonVisible;
    private IComicViewerEvents events;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHideControlsRunnable = new Runnable() {
        @Override
        public void run() {
            setCloseButtonVisible(false);
        }
    };

    public interface IComicViewerEvents {
        void onCloseButtonClick();
    }

    public ComicViewerViewModel(String imgUrl, IComicViewerEvents events) {
        this.imgUrl = imgUrl;
        this.closeButtonVisible = true;
        this.events = events;
    }

    @Bindable
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        notifyPropertyChanged(BR.imgUrl);
    }

    public void onPhotoClick() {
        toggleCloseButtonVisible();
    }

    public void onCloseClick() {
        closeButtonVisible = false;
        if (events != null) {
            events.onCloseButtonClick();
        }
    }

    private void toggleCloseButtonVisible() {
        mHideHandler.removeCallbacks(mHideControlsRunnable);
        setCloseButtonVisible(!isCloseButtonVisible());
        delayedHide(AUTO_HIDE_DELAY_MILLIS);
    }

    @Bindable
    public boolean isCloseButtonVisible() {
        return closeButtonVisible;
    }

    public void setCloseButtonVisible(boolean closeButtonVisible) {
        this.closeButtonVisible = closeButtonVisible;
        notifyPropertyChanged(BR.closeButtonVisible);
    }

    public void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideControlsRunnable);
        mHideHandler.postDelayed(mHideControlsRunnable, delayMillis);
    }
}
