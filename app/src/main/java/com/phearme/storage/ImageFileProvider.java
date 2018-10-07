package com.phearme.storage;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

public class ImageFileProvider extends FileProvider {
    @Override
    public String getType(@NonNull Uri uri) {
        return "image/jpeg";
    }
}
