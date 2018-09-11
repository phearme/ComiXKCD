package com.phearme.comixkcd.bindingadapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phearme.comixkcd.BR;
import com.phearme.comixkcd.R;
import com.phearme.comixkcd.viewmodels.MainViewModel;
import com.phearme.xkcdclient.Comic;

public class ComicsScrollViewAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    private MainViewModel viewModel;

    private final View.OnAttachStateChangeListener dispatchTransition = new View.OnAttachStateChangeListener() {
        @Override public void onViewAttachedToWindow(View v) {
            ((AppCompatActivity)v.getContext()).startPostponedEnterTransition();
            v.removeOnAttachStateChangeListener(this);
        }

        @Override public void onViewDetachedFromWindow(View v) {
            v.removeOnAttachStateChangeListener(this);
        }
    };

    public ComicsScrollViewAdapter(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.comics_scroll_view_item, parent, false);
        return new BindingViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        Comic comic = viewModel.getComics().get(position);
        holder.getViewDataBinding().setVariable(BR.comic, comic);
        holder.getViewDataBinding().setVariable(BR.viewModel, viewModel);
        holder.getViewDataBinding().executePendingBindings();
        holder.itemView.addOnAttachStateChangeListener(dispatchTransition);
    }

    @Override
    public int getItemCount() {
        return viewModel.getComics().size();
    }
}
