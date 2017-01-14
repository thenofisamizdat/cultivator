package com.example.myapplication2.app.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.myapplication2.app.Dialogs.CustomAlertDialog;
import com.example.myapplication2.app.MainActivity;
import com.example.myapplication2.app.Models.ImageContent;
import com.example.myapplication2.app.Models.NoteContent;
import com.example.myapplication2.app.Models.Storage;
import com.example.myapplication2.app.R;

/**
 * Neil Byrne - 2015
 * NewNote inflates the view that allows users to input text content and save.
 * Text can be manually input into an EditText fiel, or shared from another app on the device.
 * A custom dialog is then loaded when the user creates content.
 * It also listens for button presses to return to the main menu.
 */
public class NewNote extends AppCompatActivity{

    private int[] buttonIds = new int[]{R.id.logo, R.id.create};
    EditText note;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note);
        initButtons();
        checkShareDataIncoming();
    }

    private void checkShareDataIncoming()  {
        if (Storage.getInstance().isTemp()){
            displayPreviewNote(Storage.getInstance().getTempNote().getNote());

            Storage.getInstance().setTempImage(new ImageContent());
            Storage.getInstance().setTemp(false);
        }
    }

    private void displayPreviewNote(String _note) {
        note = (EditText) findViewById(R.id.noteContent);
        note.setText(_note);
    }

    private void createNote(){
        note = (EditText) findViewById(R.id.noteContent);
        String _note = note.getText().toString();
        if (_note.length() > 0){
            pDialog = new ProgressDialog(NewNote.this);
            pDialog.setMessage("Saving ....");
            pDialog.show();
            NoteContent newNote = new NoteContent(_note);
            Storage.getInstance().getNotes().put(newNote.getNoteID(), newNote);
            Storage.getInstance().setTempNote(new NoteContent());
            Toast.makeText(NewNote.this, "Note " + newNote.getNoteID() + " created", Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
           // CustomAlertDialog.showAlert(this, "NOTE SAVED", "Note content now saved...", "ok", "nok");
            Intent ml = new Intent(this, MainActivity.class);
            startActivity(ml);
        }
        else{
            Toast.makeText(NewNote.this, "Note is Empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void initButtons(){
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
        for (int i = 0; i < buttonIds.length; i++){
            Button b = (Button) findViewById(buttonIds[i]);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.start();
                    switch (v.getId()){
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
                        case R.id.create:
                            createNote();
                            break;
                        case R.id.logo:
                            Intent ml = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(ml);
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
