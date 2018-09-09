package com.phearme.comixkcd;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.phearme.comixkcd.databinding.ActivityAboutBinding;
import com.phearme.comixkcd.viewmodels.AboutViewModel;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AboutViewModel viewModel = new AboutViewModel(this, new AboutViewModel.IAboutViewModelEvents() {
            @Override
            public void onCloseButtonClick() {
                AboutActivity.this.finish();
            }
        });
        ActivityAboutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        binding.setViewModel(viewModel);
    }
}
