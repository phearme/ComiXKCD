package com.phearme.appmediator;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.phearme.comixkcd.R;
import com.phearme.storage.GlideImageFileTask;
import com.phearme.storage.IStorageEventHandler;
import com.phearme.storage.RoomComic;
import com.phearme.storage.Storage;
import com.phearme.xkcdclient.Comic;
import com.phearme.xkcdclient.IXKCDClientEventHandler;
import com.phearme.xkcdclient.XKCDClient;

import java.io.File;

public class Mediator {
    private static Mediator mInstance;
    private XKCDClient xkcdClient;
    private SharedPreferences sharedPreferences;
    private Storage storage;
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
        this.storage = Storage.getInstance(context);
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

    public void getComic(final int comicNumber, final IMediatorEventHandler<Comic> callback) {
        xkcdClient.getComic(comicNumber, new IXKCDClientEventHandler() {

            private void getCachedComic() {
                getComicFromStorage(comicNumber, new IMediatorEventHandler<Comic>() {
                    @Override
                    public void onEvent(Comic result) {
                        if (callback != null) {
                            callback.onEvent(result);
                        }
                    }
                });
            }

            @Override
            public void onComicSuccess(Comic comic) {
                if (comic == null) {
                    getCachedComic();
                    return;
                }

                saveComic(comic);

                if (callback != null) {
                    callback.onEvent(comic);
                }
            }

            @Override
            public void onError(Exception e) {
                getCachedComic();
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

    private void saveComic(Comic comic) {
        saveComic(comic, null);
    }

    @SuppressWarnings("unchecked")
    private void saveComic(Comic comic, final IMediatorEventHandler callback) {
        RoomComic roomComic = new RoomComic(comic.getNum(), comic.getTitle(), comic.getAlt(), comic.getImg());
        storage.saveComic(roomComic, new IStorageEventHandler() {
            @Override
            public void onEvent(Object result) {
                if (callback != null) {
                    callback.onEvent(null);
                }
            }
        });
    }

    private void getComicFromStorage(int num, final IMediatorEventHandler<Comic> callback) {
        storage.getComic(num, new IStorageEventHandler<RoomComic>() {
            @Override
            public void onEvent(RoomComic result) {
                if (callback != null) {
                    if (result == null) {
                        callback.onEvent(null);
                        return;
                    }
                    Comic comic = new Comic(result.getNum(), result.getTitle(), result.getAlt(), result.getImg());
                    callback.onEvent(comic);
                }
            }
        });
    }

    public void getImageUriFromGlide(final Context context, String imageUrl, final IMediatorEventHandler<Uri> callback) {
        new GlideImageFileTask(imageUrl, new GlideImageFileTask.IGlideImageFileTaskEvent() {
            @Override
            public void onComplete(File file) {
                if (callback == null || file == null) {
                    return;
                }
                Uri uri = FileProvider.getUriForFile(context, String.format("%s.provider", context.getPackageName()), file);
                callback.onEvent(uri);
            }
        }).execute(context);
    }
}
