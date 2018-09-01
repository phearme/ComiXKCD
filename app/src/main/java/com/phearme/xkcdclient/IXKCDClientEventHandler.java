package com.phearme.xkcdclient;

public interface IXKCDClientEventHandler {
    void onComicSuccess(Comic comic);
    void onError(Exception e);
}
