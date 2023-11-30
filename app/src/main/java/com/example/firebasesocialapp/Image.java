package com.example.firebasesocialapp;

import java.io.Serializable;

public class Image implements Serializable {
    private String imageId;
    private String imageTitle;
    private String imageUrl;
    private String imageBody;

    public Image() {
    }
    public Image(String imageId, String imageTitle, String imageUrl, String imageBody) {
        this.imageId = imageId;
        this.imageTitle = imageTitle;
        this.imageUrl = imageUrl;
        this.imageBody = imageBody;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageBody() {
        return imageBody;
    }

    public void setImageBody(String imageBody) {
        this.imageBody = imageBody;
    }
}
