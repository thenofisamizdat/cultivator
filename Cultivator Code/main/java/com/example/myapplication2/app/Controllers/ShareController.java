package com.example.myapplication2.app.Controllers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.myapplication2.app.Models.ImageContent;
import com.example.myapplication2.app.Models.NoteContent;
import com.example.myapplication2.app.Models.Storage;
import com.example.myapplication2.app.R;
import com.example.myapplication2.app.Views.NewImage;
import com.example.myapplication2.app.Views.NewNote;
import java.util.ArrayList;

/**
 * Neil Byrne - 2015
 * This activity is assigned the role in the manifest to listen for incoming shared data from other apps.
 * It handles text and images and will launch either then NewNote or NewImage activity accordingly
 */
public class ShareController extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {

                handleSendText(intent); // Handle text being sent
            }
            else if (type.startsWith("image/")) {
                try {
                    handleSendImage(intent); // Handle single image being sent
                }
                catch(Exception e){
                    handleSendText(intent); // Some programs mistakenly send data as image when it is actually text. This catches that
                }

            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }

    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            Storage.getInstance().setTempNote(new NoteContent(sharedText));
            Storage.getInstance().setTemp(true);
            Intent ns = new Intent(getApplicationContext(), NewNote.class);
            startActivity(ns);
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        Storage.getInstance().setTempImage(new ImageContent(imageUri.toString()));
        Storage.getInstance().setTemp(true);
        Intent ns = new Intent(getApplicationContext(), NewImage.class);
        startActivity(ns);
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }
}
