package com.example.myapplication2.app.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.myapplication2.app.Dialogs.CustomAlertDialog;
import com.example.myapplication2.app.MainActivity;
import com.example.myapplication2.app.Models.ImageContent;
import com.example.myapplication2.app.Models.Storage;
import com.example.myapplication2.app.R;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

/**
 * Neil Byrne - 2015
 * NewImage inflates the view that allows users to attch an image from the device or a url, add text, and save.
 * Once a valid path has been selected, either by attaching, entering url, or sharing from another app,
 * an image preview is generated and the image edittext path populated.
 * A custom dialog is then loaded when the user creates content.
 * It also listens for button presses to return to the main menu.
 */
public class NewImage extends AppCompatActivity{

    private int[] buttonIds = new int[]{R.id.logo, R.id.attachFromDevice, R.id.previewImage, R.id.create};

    EditText description, path;
    Uri imageURI;

    Bitmap bitmap;
    ProgressDialog pDialog;
    byte[] saveImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_image);
        initButtons();
        checkShareDataIncoming();
    }

    private void createImage(){
        description = (EditText) findViewById(R.id.imageDescription);
        if (imageURI != null){
            pDialog = new ProgressDialog(NewImage.this);
            pDialog.setMessage("Saving ....");
            pDialog.show();

            ImageContent newImage = new ImageContent(imageURI.toString(), description.getText().toString(), saveImage);
            Storage.getInstance().getImages().put(newImage.getImageID(), newImage);
            Storage.getInstance().setTempImage(new ImageContent());
            Toast.makeText(NewImage.this, "Image " + newImage.getImageID() + " created", Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
           // CustomAlertDialog.showAlert(this, "IMAGE SAVED", "Image content is now saved...", "ok", "nok");
            Intent ml = new Intent(this, MainActivity.class);
            startActivity(ml);
        }
        else{
            Toast.makeText(NewImage.this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkShareDataIncoming()  {
        if (Storage.getInstance().isTemp()){
            displayPreviewImage(Uri.parse(Storage.getInstance().getTempImage().getImageUri()));

            Storage.getInstance().setTempImage(new ImageContent());
            Storage.getInstance().setTemp(false);
        }
    }

    private void displayPreviewImage(Uri imageUri){
        imageURI = imageUri;
        path = (EditText) findViewById(R.id.imagePath);
        path.setText(imageUri.getPath(), TextView.BufferType.EDITABLE);

        Bitmap bitmap= null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            boolean success = bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
            saveImage = byteStream.toByteArray();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Drawable verticalImage = new BitmapDrawable(getResources(), bitmap);
        ImageView first = (ImageView) findViewById(R.id.imagePreview);
        first.setImageDrawable(verticalImage);
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
                        case R.id.attachFromDevice:
                            attachImage();
                            break;
                        case R.id.previewImage:
                            previewImage();
                            break;
                        case R.id.create:
                            createImage();
                            break;
                        case R.id.logo:
                            Intent ml = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(ml);
                    }
                }
            });
        }
    }

    public void previewImage(){
        path = (EditText) findViewById(R.id.imagePath);
        imageURI = Uri.parse(path.getText().toString());
        new LoadImage().execute(path.getText().toString());
    }

    public class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewImage.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            boolean success = bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
            saveImage = byteStream.toByteArray();

            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            if(image != null){
                ImageView img = (ImageView) findViewById(R.id.imagePreview);
                img.setImageBitmap(image);
                pDialog.dismiss();

            }else{
                pDialog.dismiss();
                Toast.makeText(NewImage.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private static final int SELECT_PICTURE = 1;

    public void attachImage(){
        // in onCreate or any event where your want the user to
        // select a file
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                displayPreviewImage(selectedImageUri);
            }
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
