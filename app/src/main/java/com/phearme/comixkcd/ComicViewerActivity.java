package com.phearme.comixkcd;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;


public class ComicViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_viewer);
        String imgUrl = getIntent().getStringExtra("url");
        PhotoView photoView = findViewById(R.id.comic_photo_view);
        Glide.with(photoView).load(imgUrl).apply(new RequestOptions().fitCenter()).into(photoView);
    }
}
