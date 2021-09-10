package com.sameerkumar021197.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

public class MusicPlayer extends AppCompatActivity {

    SeekBar seekBar;
    Button btnPlay, btnNext, btnPrev, btnForward, btnRewind;
    TextView textView, txtCnt, txtEnd;
    BarVisualizer barVisualizer;
    ImageView imageView;

    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;
    public static final String EXTRA_NAME = "song_name";
    String sname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);

        seekBar = findViewById(R.id.seekBar);
        btnPlay = findViewById(R.id.btnPlay);
        btnForward = findViewById(R.id.btnForward);
        btnRewind = findViewById(R.id.btnRewind);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        textView = findViewById(R.id.txtSongName);
        barVisualizer = findViewById(R.id.barVisulizer);
        txtCnt = findViewById(R.id.txtCnt);
        txtEnd = findViewById(R.id.txtEnd);
        imageView = findViewById(R.id.imageView);

        textView.setSelected(true);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        String songName = i.getStringExtra("songName");
        position = bundle.getInt("pos", 0);
        Uri uri = Uri.parse(mySongs.get(position).toString());

        textView.setText(songName);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                } else {
                    mediaPlayer.start();
                    btnPlay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
                mediaPlayer.release();
                position = (position + 1) % mySongs.size();
                Uri nextSong = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), nextSong);
                String songName = mySongs.get(position).getName();
                textView.setText(songName);
                startAnimation(imageView);
                mediaPlayer.start();
                //btnPlay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
                mediaPlayer.release();
                position = (position == 0) ? mySongs.size() - 1 : position - 1;
                Uri nextSong = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), nextSong);
                String songName = mySongs.get(position).getName();
                textView.setText(songName);
                startAnimation(imageView);
                mediaPlayer.start();
//                btnPlay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btnNext.performClick();
            }
        });
    }

    public void startAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }
}