package ru.barcats.tempo_leader_javanew.model;

import android.graphics.drawable.Drawable;

public class DataHome {

    private Drawable picture;
    private String head;
    private String subHead;

    public DataHome(Drawable picture, String head, String subHead ){

        this.picture = picture;
        this.head = head;
        this.subHead = subHead;
    }

    public Drawable getPicture() {
        return picture;
    }

    public String getHead() {
        return head;
    }

    public String getSubHead() {
        return subHead;
    }


}
