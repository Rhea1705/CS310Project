package com.example.cs310project;

public class Image {
    public String imageUrl;

    public Image() {
        // Default constructor required for calls to DataSnapshot.getValue(ImageInfo.class)
    }

    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


