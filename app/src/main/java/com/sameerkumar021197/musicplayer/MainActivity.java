package com.sameerkumar021197.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listViewSongs);
        String ss = (String)listView.getItemAtPosition(0);
        runtimePermission();
    }

    public void runtimePermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> findSongs(File file) {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();
        //assert files != null;
        for (File singleFile : files) {
            if (singleFile.isDirectory()) {
                arrayList.addAll(findSongs(singleFile));
            } else if (singleFile.getName().endsWith(".mp3")) {
                arrayList.add(singleFile);
            }
        }
        return arrayList;
    }

    public void displaySongs() {
        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());

        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) {
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "")
                    .replace(".wav", "");
        }

//        ArrayAdapter<String> arrayAdapter =
//                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
//        listView.setAdapter(arrayAdapter);

        CustomAdaptor customAdaptor = new CustomAdaptor();
        listView.setAdapter(customAdaptor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String songName = (String) items[i];
                startActivity(new Intent(getApplicationContext(), MusicPlayer.class)
                        .putExtra("songs", mySongs)
                        .putExtra("songName", songName)
                        .putExtra("pos", i));
            }
        });
    }

    class CustomAdaptor extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View myView = getLayoutInflater().inflate(R.layout.item_list, null);
            TextView textSong = myView.findViewById(R.id.tvSongName);
            textSong.setSelected(true);
            textSong.setText(items[i]);
            return myView;
        }
    }
}