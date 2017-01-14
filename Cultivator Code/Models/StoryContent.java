package com.example.myapplication2.app.Models;

import java.io.Serializable;

/**
 * Neil Byrne - 2015
 * StoryContent contains all data for each image content item that is created.
 */
public class StoryContent implements Serializable {

    private String title;
    private String storyID;

    public int getImportanceOfStory() {
        return importanceOfStory;
    }

    public void setImportanceOfStory(int importanceOfStory) {
        this.importanceOfStory = importanceOfStory;
    }

    private int importanceOfStory;

    public StoryContent(){}

    public StoryContent(String _title, int _importance){
        title = _title;
        importanceOfStory = _importance;
        storyID = createUniqueContentID(_title, Storage.getInstance().getUid());
    }

    private String createUniqueContentID(String _description, String _uid){
        Long unixTime = System.currentTimeMillis() / 1000L;
        String id = unixTime.toString() + _description + _uid;
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStoryID() {
        return storyID;
    }

    public void setStoryID(String storyID) {
        this.storyID = storyID;
    }

}
