package com.phearme.xkcdclient;

public class Comic {
    private int num;
    private String title;
    private String alt;
    private String img;

    public Comic(int num, String title, String alt, String img) {
        this.num = num;
        this.title = title;
        this.alt = alt;
        this.img = img;
    }

    public int getNum() {
        return num;
    }

    public String getTitle() {
        return title;
    }

    public String getAlt() {
        return alt;
    }

    public String getImg() {
        return img;
    }
}
