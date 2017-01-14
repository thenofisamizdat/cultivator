package com.example.myapplication2.app.Controllers;

import android.content.Context;
import com.example.myapplication2.app.Models.ImageContent;
import com.example.myapplication2.app.Models.NoteContent;
import com.example.myapplication2.app.Models.Storage;
import com.example.myapplication2.app.Models.StoryContent;
import java.io.*;
import java.util.HashMap;

/**
 * Neil Byrne - 2015
 * This class handles data persistence on the user's device.
 * It loads and saves the app#s serialized data structures
 */
public class LoadSave implements Serializable{

    private static final long serialVersionUID = -3625400529225626533L;

    public LoadSave(){}


    @SuppressWarnings("unchecked")
    public Storage loadData(Context c) {
        HashMap<String, StoryContent> stories = null;
        HashMap<String, NoteContent> notes = null;
        HashMap<String, ImageContent> images = null;

        FileInputStream fis;
        //deserailize from file to object
        try {

            fis = c.openFileInput("dataPersist.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            try {

                stories = (HashMap<String, StoryContent>) in.readObject();
                Storage.getInstance().setStories(stories);

                notes = (HashMap<String, NoteContent>) in.readObject();
                Storage.getInstance().setNotes(notes);

                images = (HashMap<String, ImageContent>) in.readObject();
                Storage.getInstance().setImages(images);

            }
            catch ( FileNotFoundException fnf) {
                fnf.printStackTrace();
            } catch (ClassNotFoundException e) {

                e.printStackTrace();
            }
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public String saveData(Context c) {
        FileOutputStream fos;
        try {
            fos = c.openFileOutput("dataPersist.ser", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Storage.getInstance().getStories());
            oos.writeObject(Storage.getInstance().getNotes());
            oos.writeObject(Storage.getInstance().getImages());
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
