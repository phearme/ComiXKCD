package com.phearme.storage;

import android.content.Context;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;

public class GlideImageFileTask extends AsyncTask<Context, Void, File> {
    private String imageUrl;
    private IGlideImageFileTaskEvent callback;

    public interface IGlideImageFileTaskEvent {
        void onComplete(File file);
    }

    public GlideImageFileTask(String imageUrl, IGlideImageFileTaskEvent callback) {
        this.imageUrl = imageUrl;
        this.callback = callback;
    }

    @Override
    protected File doInBackground(Context... contexts) {
        try {
            return Glide
                    .with(contexts[0])
                    .downloadOnly()
                    .load(imageUrl)
                    .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(File file) {
        if (callback != null) {
            callback.onComplete(file);
        }
    }
}
