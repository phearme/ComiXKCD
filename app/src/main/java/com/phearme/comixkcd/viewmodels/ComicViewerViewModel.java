package com.phearme.comixkcd.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;

import com.phearme.appmediator.IMediatorEventHandler;
import com.phearme.appmediator.Mediator;
import com.phearme.comixkcd.BR;


public class ComicViewerViewModel extends BaseObservable {
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int FIRST_HIDE_DELAY_MILLIS = 2000;

    private String imgUrl;
    private int comicNumber;
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

    public ComicViewerViewModel(String imgUrl, int comicNumber, IComicViewerEvents events) {
        setImgUrl(imgUrl);
        setComicNumber(comicNumber);
        setCloseButtonVisible(true);
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

    @Bindable
    public int getComicNumber() {
        return comicNumber;
    }

    public void setComicNumber(int comicNumber) {
        this.comicNumber = comicNumber;
        notifyPropertyChanged(BR.comicNumber);
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

    public void onShareClick(final Context context) {
        Mediator.getInstance(context).getImageUriFromGlide(context, imgUrl, new IMediatorEventHandler<Uri>() {
            @Override
            public void onEvent(Uri uri) {
                if (uri != null) {
                    ShareCompat.IntentBuilder.from((AppCompatActivity) context)
                            .setType("image/*")
                            .setStream(uri)
                            .startChooser();
                }
            }
        });
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

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideControlsRunnable);
        mHideHandler.postDelayed(mHideControlsRunnable, delayMillis);
    }

    public void firstDelayedHide() {
        delayedHide(FIRST_HIDE_DELAY_MILLIS);
    }
}
