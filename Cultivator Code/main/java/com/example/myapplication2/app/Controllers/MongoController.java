package com.example.myapplication2.app.Controllers;

import com.example.myapplication2.app.Models.ImageContent;
import com.example.myapplication2.app.Models.Storage;
import com.google.gson.Gson;
import com.mongodb.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Neil Byrne - 2015
 * This is our MongoDB controller that handles passing data back and forth to the mongoDB.
 */
public class MongoController{


    public MongoController(){}

    public void putImages() throws UnknownHostException {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                List<ServerAddress> seeds = new ArrayList<ServerAddress>();
                seeds.add( new ServerAddress( "ds055832.mongolab.com", 55832 ));
                List<MongoCredential> credentials = new ArrayList<MongoCredential>();
                credentials.add(
                        MongoCredential.createMongoCRCredential(
                                "cult",
                                "cultivator",
                                "dpej67eb".toCharArray()
                        )
                );


                DB cvlDB;
                DBCollection collection;
                MongoClient mongo = new MongoClient( seeds, credentials );
                cvlDB = mongo.getDB("cultivator");
                collection = cvlDB.getCollection("images");

                Iterator<ImageContent> it = Storage.getInstance().getImages().values().iterator();
                while (it.hasNext()) {
                    DBObject updateObject = new BasicDBObject();
                    ImageContent imageUpload = it.next();

                    updateObject.put(imageUpload.getImageID(), (new Gson()).toJson(imageUpload));
                    collection.insert(updateObject);
                }
                mongo.close();
            }
        });
        t.start();

    }

    public void getImages() throws UnknownHostException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                List<ServerAddress> seeds = new ArrayList<ServerAddress>();
                seeds.add( new ServerAddress( "ds055832.mongolab.com", 55832 ));
                List<MongoCredential> credentials = new ArrayList<MongoCredential>();
                credentials.add(
                        MongoCredential.createMongoCRCredential(
                                "cult",
                                "cultivator",
                                "dpej67eb".toCharArray()
                        )
                );

                DB cvlDB;
                DBCollection collection;
                MongoClient mongo = new MongoClient( seeds, credentials );
                cvlDB = mongo.getDB("cultivator");
                collection = cvlDB.getCollection("images");

                DBCursor cursor = collection.find();
                HashMap<String, ImageContent> updatedImages = new HashMap<String, ImageContent>();

                while(cursor.hasNext()) {
                    DBObject dbobj = cursor.next();
                    String n = dbobj.toString();

                    try {
                        // strip unwanted mongo return data
                        JSONObject nnn = new JSONObject(n);
                        JSONObject im = new JSONObject();
                        nnn.remove("_id");
                        Iterator<?> keys = nnn.keys();
                        while( keys.hasNext() ) {
                            String key = (String)keys.next();
                            im = new JSONObject(nnn.get(key).toString());
                        }
                    // map mongo json doc to corresponding class
                    ImageContent entry = (new Gson()).fromJson(im.toString(), ImageContent.class);
                    // now we add to new image hashmap and replace old list.
                    updatedImages.put(entry.getImageID(), entry);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Storage.getInstance().setImages(updatedImages);
                mongo.close();
            }
        });
        t.start();
    }

}
