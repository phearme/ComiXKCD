package com.phearme.appmediator;

import android.content.Context;

import com.phearme.comixkcd.R;
import com.phearme.xkcdclient.Comic;
import com.phearme.xkcdclient.IXKCDClientEventHandler;
import com.phearme.xkcdclient.XKCDClient;

public class Mediator {
    private static Mediator mInstance;
    private XKCDClient xkcdClient;

    public synchronized static Mediator getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Mediator(context.getApplicationContext());
        }
        return mInstance;
    }

    private Mediator(Context context) {
        this.xkcdClient = new XKCDClient(context, context.getString(R.string.xkcd_client_api_url));
    }

    public void getLastComicIndex(final IMediatorEventHandler<Integer> callback) {
        xkcdClient.getLastComic(new IXKCDClientEventHandler() {
            @Override
            public void onComicSuccess(Comic comic) {
                if (callback != null) {
                    if (comic == null) {
                        callback.onEvent(null);
                        return;
                    }
                    callback.onEvent(comic.getNum());
                }
            }

            @Override
            public void onError(Exception e) {
                if (callback != null) {
                    callback.onEvent(null);
                }
            }
        });
    }

    public void getComic(int comicNumber, final IMediatorEventHandler<Comic> callback) {
        xkcdClient.getComic(comicNumber, new IXKCDClientEventHandler() {
            @Override
            public void onComicSuccess(Comic comic) {
                if (callback != null) {
                    callback.onEvent(comic);
                }
            }

            @Override
            public void onError(Exception e) {
                if (callback != null) {
                    callback.onEvent(null);
                }
            }
        });
    }
}
