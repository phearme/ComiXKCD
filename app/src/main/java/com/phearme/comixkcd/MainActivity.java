package com.phearme.comixkcd;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.phearme.comixkcd.bindingadapters.ComicsScrollViewAdapter;
import com.phearme.comixkcd.databinding.ActivityMainBinding;
import com.phearme.comixkcd.viewmodels.MainViewModel;
import com.phearme.xkcdclient.Comic;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

public class MainActivity extends AppCompatActivity implements MainViewModel.IMainViewModelEvents {

    private ComicsScrollViewAdapter comicsScrollViewAdapter;
    private DiscreteScrollView comicsScrollView;
    private View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MainViewModel viewModel = new MainViewModel(this, this);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(viewModel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        parentLayout = findViewById(R.id.parentLayout);
        comicsScrollViewAdapter = new ComicsScrollViewAdapter(viewModel);
        comicsScrollView = findViewById(R.id.comicsScrollView);
        comicsScrollView.setAdapter(comicsScrollViewAdapter);
        comicsScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.CENTER)
                .build());
        comicsScrollView.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
                viewModel.loadBufferContentFromPosition(adapterPosition);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDatasetChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (comicsScrollViewAdapter != null) {
                    comicsScrollViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemChanged(final int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (comicsScrollViewAdapter != null) {
                    comicsScrollViewAdapter.notifyItemChanged(position);
                }
            }
        });
    }

    @Override
    public void onScrollToPosition(final int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (comicsScrollView != null) {
                    comicsScrollView.smoothScrollToPosition(position);
                }
            }
        });
    }

    @Override
    public void onComicClick(final View view, final Comic comic) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (comic == null) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, ComicViewerActivity.class);
                intent.putExtra("url", comic.getImg());
                startActivity(
                        intent,
                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, view, "comicimage")
                                .toBundle());
            }
        });
    }

    @Override
    public void onFailedLoadingData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(parentLayout, R.string.something_went_wrong, Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
