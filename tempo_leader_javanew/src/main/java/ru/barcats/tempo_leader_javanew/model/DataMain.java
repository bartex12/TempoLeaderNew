package ru.barcats.tempo_leader_javanew.model;

import android.graphics.drawable.Drawable;

public class DataMain {

    Drawable picture;
    String head;

    DataMain( ){

    }

    public Drawable getPicture() {
        return picture;
    }

    public String getHead() {
        return head;
    }

    public void setPicture(Drawable picture) {
        this.picture = picture;
    }

    public void setHead(String head) {
        this.head = head;
    }
}
