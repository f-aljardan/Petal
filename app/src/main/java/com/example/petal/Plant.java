package com.example.petal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Plant {
    private int id;
    private String name;
    private String description;
    private String size;
    private String sunExposure;
    private String soilType;
    private byte[] imageBytes;
    private  String username;
    private String rentalDate;
    public Plant(String name, String description, String size, String sunExposure, String soilType, byte[] imageBytes, String username) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.sunExposure = sunExposure;
        this.soilType = soilType;
        this.imageBytes = imageBytes;
        this.username = username;
    }

    public  Plant( String name, String description , String username){
        this.name = name;
        this.description =description;
        this.username = username;
    }
    public Plant() {
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSunExposure() {
        return sunExposure;
    }

    public void setSunExposure(String sunExposure) {
        this.sunExposure = sunExposure;
    }

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public Bitmap getImageBitmap() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        return bitmap;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public void setRentalDate(String rentalDate) {
        this.rentalDate = rentalDate;
    }

    public String getRentalDate() {
        return rentalDate;
    }
}
