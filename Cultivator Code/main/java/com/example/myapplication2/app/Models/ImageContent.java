package com.example.myapplication2.app.Models;

import java.io.Serializable;

/**
 * Neil Byrne - 2015
 * ImageContent contains all data for each image content item that is created.
 */
public class ImageContent implements Serializable{

    private String imageUri;
    private String description;
    private String imageID;
    private byte[] saveImage;

    public byte[] getSaveImage() {
        return saveImage;
    }

    public void setSaveImage(byte[] saveImage) {
        this.saveImage = saveImage;
    }

    public ImageContent(){}

    public ImageContent(String _imageUri){
        imageUri = _imageUri;
    }

    public ImageContent(String _imageUri, String _description, byte[] _saveImage){
        saveImage = _saveImage;
        imageUri = _imageUri;
        description = _description;
        imageID = createUniqueContentID(description, Storage.getInstance().getUid());
    }

    private String createUniqueContentID(String _description, String _uid){
        Long unixTime = System.currentTimeMillis() / 1000L;
        String id = unixTime.toString() + _description + _uid;
        return id;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }


}
