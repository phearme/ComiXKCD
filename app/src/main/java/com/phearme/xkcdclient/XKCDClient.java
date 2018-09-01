package com.phearme.xkcdclient;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

public class XKCDClient {
    private final String apiUrl;
    private final RequestQueue requestQueue;
    private static final int API_CONNECTION_TIMEOUT = 5000;
    private final DefaultRetryPolicy retryPolicy;
    private Comic lastComic;

    public XKCDClient(Context context, String apiUrl) {
        this.apiUrl = apiUrl;
        this.requestQueue = Volley.newRequestQueue(context);
        this.retryPolicy = new DefaultRetryPolicy(
                API_CONNECTION_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    private void addToQueue(Request request) {
        request.setRetryPolicy(retryPolicy);
        request.setShouldCache(true);
        requestQueue.add(request);
    }

    private void apiRequest(String url, final IXKCDClientEventHandler callback) {
        JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (callback != null) {
                        if (response == null) {
                            callback.onComicSuccess(null);
                            return;
                        }
                        Comic comic = new Gson().fromJson(response.toString(), Comic.class);
                        callback.onComicSuccess(comic);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (callback != null) {
                        callback.onError(error);
                    }
                }
            });
        addToQueue(request);
    }

    public void getComic(Integer comicIndex, IXKCDClientEventHandler callback) {
        String indexToString = comicIndex == null ? "" : String.valueOf(comicIndex);
        apiRequest(String.format(apiUrl, indexToString), callback);
    }

    public void getLastComic(final IXKCDClientEventHandler callback) {
        if (lastComic != null && callback != null) {
            callback.onComicSuccess(lastComic);
            return;
        }
        getComic(null, new IXKCDClientEventHandler() {
            @Override
            public void onComicSuccess(Comic comic) {
                lastComic = comic;
                if (callback != null) {
                    callback.onComicSuccess(lastComic);
                }
            }

            @Override
            public void onError(Exception e) {
                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }

}
