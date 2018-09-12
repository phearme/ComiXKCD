package com.phearme.comixkcd.uihelpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

public class FloatingActionButtonBehavior extends CoordinatorLayout.Behavior<View> {
    public FloatingActionButtonBehavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }
}
