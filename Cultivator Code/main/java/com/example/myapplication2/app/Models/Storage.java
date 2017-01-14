package com.example.myapplication2.app.Models;

import android.content.Context;
import android.util.Log;
import com.example.myapplication2.app.Controllers.LoadSave;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Neil Byrne - 2015
 * Storage uses the Singleton design pattern and acts as a static state machine and data store while the app is running.
 * It contains an instance of the LoadSave class and methods for accessing its functions, whichit will run on a separate thread.
 * Storage is Serializable and contains Hashmap data structures of
 * ImageContent, NoteContent, and StoryContent which are also Serializable.
 */
public class Storage implements Serializable {

    private String uid = "xxx";

    private static final long serialVersionUID = -7604766932017737115L;

    private Storage(){}

    public static int page = 3;

    private HashMap<String, ImageContent> images;

    private HashMap<String, NoteContent> notes;
    private HashMap<String, StoryContent> stories;

    private ImageContent tempImage = new ImageContent();
    private NoteContent tempNote = new NoteContent();
    private StoryContent tempStory = new StoryContent();

    private LoadSave loadSave = new LoadSave();

    public void loadData(Context _c){
        final Context c = _c;
        Thread t = new Thread(new Runnable() {
            public void run() {
                loadSave.loadData(c);
            }
        });
        t.start();
    }
    public void saveData(Context _c){
        final Context c = _c;
        Thread t = new Thread(new Runnable() {
            public void run() {
                loadSave.saveData(c);
                Log.d("saving", "data");
            }
        });
        t.start();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public HashMap<String, NoteContent> getNotes() {
        if (notes == null){
            notes = new HashMap<String, NoteContent>();
        }
        return notes;
    }

    public void setNotes(HashMap<String, NoteContent> notes) {
        this.notes = notes;
    }

    public HashMap<String, StoryContent> getStories() {
        if (stories == null){
            stories = new HashMap<String, StoryContent>();
        }
        return stories;
    }

    public void setImages(HashMap<String, ImageContent> images) {
        this.images = images;
    }

    public HashMap<String, ImageContent> getImages(){
        if (images == null){
            images = new HashMap<String, ImageContent>();
        }
        return images;
    }

    public void setStories(HashMap<String, StoryContent> stories) {
        this.stories = stories;
    }

    public boolean isTemp() {
        return temp;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }

    private boolean temp = false;

    public ImageContent getTempImage() {
        return tempImage;
    }

    public void setTempImage(ImageContent tempImage) {
        this.tempImage = tempImage;
    }

    public NoteContent getTempNote() {
        return tempNote;
    }

    public void setTempNote(NoteContent tempNote) {
        this.tempNote = tempNote;
    }

    public StoryContent getTempStory() {
        return tempStory;
    }

    public void setTempStory(StoryContent tempStory) {
        this.tempStory = tempStory;
    }



    private static class SingletonHelper{
        private static final Storage instance = new Storage();
    }

    public static Storage getInstance(){
        return SingletonHelper.instance;
    }

    protected Object readResolve() {
        return getInstance();
    }


}
