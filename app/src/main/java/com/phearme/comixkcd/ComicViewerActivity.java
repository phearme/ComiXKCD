package com.phearme.comixkcd;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.phearme.comixkcd.databinding.ActivityComicViewerBinding;
import com.phearme.comixkcd.viewmodels.ComicViewerViewModel;


public class ComicViewerActivity extends AppCompatActivity {

    ComicViewerViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String imgUrl = getIntent().getStringExtra("url");
        int comicNumber = getIntent().getIntExtra("number", 0);

        viewModel = new ComicViewerViewModel(imgUrl, comicNumber, new ComicViewerViewModel.IComicViewerEvents() {
            @Override
            public void onCloseButtonClick() {
                close();
            }
        });
        ActivityComicViewerBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_comic_viewer);
        binding.setViewModel(viewModel);
        ActivityCompat.postponeEnterTransition(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.firstDelayedHide();
    }

    private void close() {
        finishAfterTransition();
    }

    @Override
    public void onBackPressed() {
        close();
    }

}
