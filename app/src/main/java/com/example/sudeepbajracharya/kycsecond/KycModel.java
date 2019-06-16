package com.example.sudeepbajracharya.kycsecond;

public class KycModel {
    private int itemImage;
    private String itemName;
    private String itemDescription;

    public KycModel(int itemImage, String itemName, String itemDescription) {
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
    }

    public int getItemImage() {
        return itemImage;
    }

    public void setItemImage(int itemImage) {
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

