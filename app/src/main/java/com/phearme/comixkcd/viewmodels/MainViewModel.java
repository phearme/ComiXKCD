package com.phearme.comixkcd.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import android.widget.ImageView;

import com.phearme.appmediator.IMediatorEventHandler;
import com.phearme.appmediator.Mediator;
import com.phearme.comixkcd.BR;
import com.phearme.comixkcd.R;
import com.phearme.xkcdclient.Comic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainViewModel extends BaseObservable {
    private ArrayList<Comic> comics;
    private Mediator mediator;
    private IMainViewModelEvents events;
    private static final int LOAD_ITEMS_BUFFER_COUNT = 10;
    private Integer lastComicIndex;
    private int currentPosition = 0;
    private Comic currentComic;

    public interface IMainViewModelEvents {
        void onDatasetChanged();
        void onItemChanged(int position);
        void onScrollToPosition(int position);
        void onComicClick(View view, Comic comic);
        void onFailedLoadingData();
    }

    public MainViewModel(Context context, final IMainViewModelEvents events) {
        this.events = events;
        init(context);
    }

    private void init(Context context) {
        comics = new ArrayList<>();
        mediator = Mediator.getInstance(context);
        mediator.getLastComicIndex(new IMediatorEventHandler<Integer>() {
            @Override
            public void onEvent(Integer result) {
                if (result == null && events != null) {
                    events.onFailedLoadingData();
                    return;
                }

                if (result != null) {
                    lastComicIndex = result;
                    comics = new ArrayList<>(Collections.nCopies(lastComicIndex, (Comic)null));
                    if (events != null) {
                        events.onDatasetChanged();
                    }
                    int previousComicNumber = mediator.getCurrentComicNumber();
                    if (previousComicNumber >= 0 && lastComicIndex != null && events != null) {
                        int previousPosition = lastComicIndex - previousComicNumber;
                        events.onScrollToPosition(previousPosition);
                        return;
                    }
                    loadFirstComicsContent();
                }
            }
        });
    }

    private void loadFirstComicsContent() {
        int itemsCount = 0;
        for (int i = 0; i < comics.size(); i++) {
            Comic comic = comics.get(i);
            if (comic == null) {
                loadComic(i);
                itemsCount++;
                if (itemsCount == LOAD_ITEMS_BUFFER_COUNT) {
                    break;
                }
            }
        }
    }

    private void loadComic(final int itemPosition) {
        if (lastComicIndex != null) {
            int comicNumber = lastComicIndex - itemPosition;
            mediator.getComic(comicNumber, new IMediatorEventHandler<Comic>() {
                @Override
                public void onEvent(Comic comic) {
                    comics.set(itemPosition, comic);
                    if (events != null) {
                        events.onItemChanged(itemPosition);
                    }
                    if (itemPosition == currentPosition) {
                        setCurrentComic(comic);
                    }
                }
            });
        }
    }

    public ArrayList<Comic> getComics() {
        return comics;
    }

    public void onShuffleButtonClick(View view) {
        if (lastComicIndex == null) {
            init(view.getContext());
            return;
        }
        if (events != null) {
            int randomPosition = new Random().nextInt(lastComicIndex + 1);
            events.onScrollToPosition(randomPosition);
        }
    }

    public void loadBufferContentFromPosition(int position) {
        setCurrentComic(null);
        setCurrentPosition(position);
        Comic comic = comics.get(currentPosition);
        if (comic == null) {
            loadComic(currentPosition);
        } else {
            setCurrentComic(comic);
        }
        for (int i = position - LOAD_ITEMS_BUFFER_COUNT; i < position + LOAD_ITEMS_BUFFER_COUNT; i++) {
            if (i >=0 && i < comics.size() && i != currentPosition) {
                comic = comics.get(i);
                if (comic == null) {
                    loadComic(i);
                }
            }
        }
    }

    @Bindable
    public Comic getCurrentComic() {
        return currentComic;
    }

    public void setCurrentComic(Comic currentComic) {
        this.currentComic = currentComic;
        if (currentComic != null) {
            mediator.setCurrentComicNumber(currentComic.getNum());
        }
        notifyPropertyChanged(BR.currentComic);
    }

    public void onComicClick(View view, Comic comic) {
        ImageView img = view.findViewById(R.id.imgviewComicItem);
        if (img != null && events != null) {
            events.onComicClick(img, comic);
        }
    }

    public void onRewindClick() {
        if (events != null && comics.size() > 0) {
            events.onScrollToPosition(0);
        }
    }

    public void onFastForwardClick() {
        if (events != null && comics.size() > 0) {
            events.onScrollToPosition(comics.size() - 1);
        }
    }

    @Bindable
    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        notifyPropertyChanged(BR.currentPosition);
    }
}
