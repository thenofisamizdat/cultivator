package com.example.myapplication2.app.Dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.example.myapplication2.app.MainActivity;

/**
 * Neil Byrne - 2015
 * The CustomAlertDialog can be called and displayed from any activity to show a custom alert popup message.
 * The calling activity is passed as a parameter, along with the message contents.
 */
public class CustomAlertDialog extends AppCompatActivity {



    public static void showAlert(Activity _v, String _title, String _message, String _button1, String _button2){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_v);

        final Activity v = _v;

        // set title
        alertDialogBuilder.setTitle(_title);

        // we can add a customised view here using the following
        //View v = getInflater().inflate(R.id.customView, null);
        //alertDialogBuilder.setView(v);

        // set dialog message
        alertDialogBuilder
                .setMessage(_message)
                .setCancelable(false)
                .setPositiveButton(_button1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity

                        /*
                        MongoController mc = new MongoController();
                        try {
                            mc.putImages();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                        */
                        v.finish();
                    }
                })
                .setNegativeButton(_button2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}
