package com.phearme.comixkcd.viewmodels;

import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.phearme.comixkcd.BR;
import com.phearme.comixkcd.R;

public class AboutViewModel extends BaseObservable {
    private String versionName;
    private String versionPrefix;
    private IAboutViewModelEvents events;

    public interface IAboutViewModelEvents {
        void onCloseButtonClick();
    }

    public AboutViewModel(Context context, IAboutViewModelEvents events) {
        this.events = events;
        try {
            setVersionName(context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        this.versionPrefix = context.getString(R.string.version);
    }

    @Bindable
    public String getVersionName() {
        return String.format("%s %s", versionPrefix, versionName);
    }

    private void setVersionName(String versionName) {
        this.versionName = versionName;
        notifyPropertyChanged(BR.versionName);
    }

    public void onCloseButtonClick() {
        if (events != null) {
            events.onCloseButtonClick();
        }
    }
}
