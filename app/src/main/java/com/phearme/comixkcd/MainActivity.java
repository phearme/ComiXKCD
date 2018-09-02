package com.phearme.comixkcd;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.phearme.appmediator.IMediatorEventHandler;
import com.phearme.appmediator.Mediator;
import com.phearme.comixkcd.databinding.ActivityMainBinding;
import com.phearme.xkcdclient.Comic;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

public class MainActivity extends AppCompatActivity implements MainViewModel.IMainViewModelEvents {

    private ComicsScrollViewAdapter comicsScrollViewAdapter;
    private DiscreteScrollView comicsScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MainViewModel viewModel = new MainViewModel(this, this);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(viewModel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mediator.getInstance(MainActivity.this).getLastComicIndex(new IMediatorEventHandler<Integer>() {
                    @Override
                    public void onEvent(Integer result) {
                        if (result != null) {
                            Log.d("raf", result.toString());
                        }
                    }
                });
/*                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
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
                Intent intent = new Intent(MainActivity.this, ComicViewerActivity.class);
                intent.putExtra("url", comic.getImg());
                startActivity(
                        intent,
                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, view, "comicimage")
                                .toBundle());
            }
        });
    }

}
