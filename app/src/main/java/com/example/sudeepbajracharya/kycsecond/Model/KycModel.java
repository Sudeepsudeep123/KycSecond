package com.example.sudeepbajracharya.kycsecond.Model;

import android.graphics.Bitmap;

public class KycModel {
    private Bitmap itemImage;
    private String itemName;
    private String itemDescription;

    public KycModel(Bitmap itemImage, String itemName, String itemDescription) {
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
    }

    public Bitmap getItemImage() {
        return itemImage;
    }

    public void setItemImage(Bitmap itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}

