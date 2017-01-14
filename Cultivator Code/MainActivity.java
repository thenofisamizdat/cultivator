package com.example.myapplication2.app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.example.myapplication2.app.Adapters.ContentListAdapter;
import com.example.myapplication2.app.Controllers.MongoController;
import com.example.myapplication2.app.Models.Storage;
import com.example.myapplication2.app.Views.NewImage;
import com.example.myapplication2.app.Views.NewNote;
import com.example.myapplication2.app.Views.NewStory;

/**
 * Neil Byrne - 2015
 * MainActivity is our Launch activity.
 * It contains the main menu to move between activities,
 * and will initialize data load from the device, as well as loading from mongoDB
 */


public class MainActivity extends AppCompatActivity {

    MongoController mc;

    private int[] buttonIds = new int[]{R.id.newStory, R.id.newNote, R.id.newImage, R.id.myList};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons();
        Storage.getInstance().loadData(this);

        /*
        mc = new MongoController();
        try {
            mc.putImages();
            mc.getImages();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        */
    }

    private void initButtons(){
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
        for (int i = 0; i < buttonIds.length; i++){
            Button b = (Button) findViewById(buttonIds[i]);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.start();
                    switch (v.getId()) {
                        case R.id.newStory:
                            Intent ns = new Intent(getApplicationContext(), NewStory.class);
                            startActivity(ns);
                            break;
                        case R.id.newNote:
                            Intent nn = new Intent(getApplicationContext(), NewNote.class);
                            startActivity(nn);
                            break;
                        case R.id.newImage:
                            Intent ni = new Intent(getApplicationContext(), NewImage.class);
                            startActivity(ni);
                            break;
                        case R.id.myList:
                            Intent ml = new Intent(getApplicationContext(), ContentListAdapter.class);
                            startActivity(ml);
                            break;
                    }
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
