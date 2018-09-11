package com.phearme.appmediator;

import android.content.Context;
import android.content.SharedPreferences;

import com.phearme.comixkcd.R;
import com.phearme.xkcdclient.Comic;
import com.phearme.xkcdclient.IXKCDClientEventHandler;
import com.phearme.xkcdclient.XKCDClient;

public class Mediator {
    private static Mediator mInstance;
    private XKCDClient xkcdClient;
    private SharedPreferences sharedPreferences;
    private static final String PREF_FILE_KEY = "PREF_FILE_KEY";
    private static final String PREF_CURRENT_COMIC_KEY = "PREF_CURRENT_COMIC_KEY";
    private static final String PREF_LAST_COMIC_INDEX = "PREF_LAST_COMIC_INDEX";

    public synchronized static Mediator getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Mediator(context.getApplicationContext());
        }
        return mInstance;
    }

    private Mediator(Context context) {
        this.xkcdClient = new XKCDClient(context, context.getString(R.string.xkcd_client_api_url));
        String preferenceFileKey = String.format("%s.%s", context.getPackageName(), PREF_FILE_KEY);
        sharedPreferences = context.getSharedPreferences(preferenceFileKey, Context.MODE_PRIVATE);
    }

    public void getLastComicIndex(final IMediatorEventHandler<Integer> callback) {
        xkcdClient.getLastComic(new IXKCDClientEventHandler() {
            @Override
            public void onComicSuccess(Comic comic) {
                if (comic != null) {
                    sharedPreferences.edit().putInt(PREF_LAST_COMIC_INDEX, comic.getNum()).apply();
                }

                if (callback != null) {
                    callback.onEvent(getLastComicIndexFromPrefs());
                }
            }

            @Override
            public void onError(Exception e) {
                if (callback != null) {
                    callback.onEvent(getLastComicIndexFromPrefs());
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

    public void setCurrentComicNumber(int comicNumber) {
        sharedPreferences.edit().putInt(PREF_CURRENT_COMIC_KEY, comicNumber).apply();
    }

    public int getCurrentComicNumber() {
        return sharedPreferences.getInt(PREF_CURRENT_COMIC_KEY, -1);
    }

    private Integer getLastComicIndexFromPrefs() {
        Integer lastIndex = sharedPreferences.getInt(PREF_LAST_COMIC_INDEX, -1);
        if (lastIndex == -1) {
            return null;
        }
        return lastIndex;
    }
}
