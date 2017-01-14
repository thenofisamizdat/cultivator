package com.example.myapplication2.app.Adapters;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.myapplication2.app.MainActivity;
import com.example.myapplication2.app.Models.ImageContent;
import com.example.myapplication2.app.Models.NoteContent;
import com.example.myapplication2.app.Models.Storage;
import com.example.myapplication2.app.Models.StoryContent;
import com.example.myapplication2.app.R;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Neil Byrne - 2015
 * This class handles content list generation and formatting.
 * Depending on the list selected it will show either story, note or image content list.
 * If displaying images it will generate a preview image in each list item.
 * When a new list type is selected it switches the data source and rebuilds the list accordingly
 */
public class ContentListAdapter extends ListActivity {

    public class ContentItem {
        String name;
        String description;
        int type;
        String uri;
        byte[] imageBytes;
    }
    private int[] buttonIds = new int[]{R.id.logo, R.id.notes, R.id.images, R.id.stories};

    ContentAdapter contentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_list);

        initButtons();
        listName = (TextView) findViewById(R.id.listName);

        contentAdapter = new ContentAdapter();

        setListAdapter(contentAdapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        ContentItem item = contentAdapter.getCodeLearnChapter(position);

        Toast.makeText(ContentListAdapter.this, item.name, Toast.LENGTH_LONG).show();
    }
    public class ContentAdapter extends BaseAdapter {

        List<ContentItem> listData = getDataForListView();

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listData.size();
        }

        @Override
        public ContentItem getItem(int arg0) {
            // TODO Auto-generated method stub
            return listData.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {

            if(arg1==null)
            {
                LayoutInflater inflater = (LayoutInflater) ContentListAdapter.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.list_item, arg2,false);
            }

            TextView name = (TextView)arg1.findViewById(R.id.textView1);
            TextView desc = (TextView)arg1.findViewById(R.id.textView2);

            ContentItem content = listData.get(arg0);

            name.setText(content.name);
            desc.setText(content.description);

            if (content.type == 3){
                savedImage = content.imageBytes;
                previewImage(content.uri);
            }

            return arg1;
        }
        Bitmap bitmap;
        byte[] savedImage;

        public void previewImage(String path){
            new LoadImage().execute(path);
        }

        public class LoadImage extends AsyncTask<String, String, Bitmap> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            protected Bitmap doInBackground(String... args) {

                try {

                    if (args[0].charAt(0) == 'h'){
                        bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
                    }
                    else{
                        bitmap = BitmapFactory.decodeByteArray(savedImage, 0, savedImage.length);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            protected void onPostExecute(Bitmap image) {
                if(image != null){
                    ImageView img = (ImageView) findViewById(R.id.imageView1);
                    img.setImageBitmap(image);

                }else{
                    Toast.makeText(ContentListAdapter.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

                }
            }
        }


        public ContentItem getCodeLearnChapter(int position)
        {
            return listData.get(position);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_view_with_simple_adapter, menu);
        return true;
    }

    private void singleList(){
        Intent base = new Intent(ContentListAdapter.this,ContentListAdapter.class);
        startActivity(base);
    }

    TextView listName;
    private void initButtons(){
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
        for (int i = 0; i < buttonIds.length; i++){
            Button b = (Button) findViewById(buttonIds[i]);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mp.start();
                    switch (v.getId()){
                        case R.id.stories:
                            Storage.page = 1;
                            singleList();
                            break;
                        case R.id.notes:
                            Storage.page = 2;
                            singleList();
                            break;
                        case R.id.images:
                            Storage.page = 3;
                            singleList();
                            break;
                        case R.id.logo:
                            Intent ml = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(ml);
                    }
                }
            });
        }
    }

    public List<ContentItem> getDataForListView()
    {
        List<ContentItem> listData = new ArrayList<ContentItem>();

        if (Storage.page == 1){
            listName.setText("MY STORIES");
            Iterator<StoryContent> it = Storage.getInstance().getStories().values().iterator();
            while (it.hasNext()) {
                StoryContent ic = it.next();
                ContentItem item = new ContentItem();
                item.name = ic.getTitle();
                item.description = ic.getStoryID();
                item.type = 1;
                item.uri = "";
                listData.add(item);
            }
        }
        else if (Storage.page == 2){
            listName.setText("MY NOTES");
            Iterator<NoteContent> it = Storage.getInstance().getNotes().values().iterator();
            while (it.hasNext()) {
                NoteContent ic = it.next();
                ContentItem item = new ContentItem();
                item.name = ic.getNote();
                item.description = ic.getNoteID();
                item.type = 2;
                item.uri = "";
                listData.add(item);
            }
        }
        else {
            listName.setText("MY IMAGES");
            Iterator<ImageContent> it = Storage.getInstance().getImages().values().iterator();
            while (it.hasNext()) {
                ImageContent ic = it.next();
                ContentItem item = new ContentItem();
                item.name = ic.getDescription();
                item.description = ic.getImageID();
                item.type = 3;
                item.uri = ic.getImageUri();
                item.imageBytes = ic.getSaveImage();
                listData.add(item);
            }
        }
        return listData;
    }
}
