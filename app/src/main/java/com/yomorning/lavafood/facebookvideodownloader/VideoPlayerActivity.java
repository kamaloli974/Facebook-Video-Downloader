package com.yomorning.lavafood.facebookvideodownloader;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {
    VideoView videoView;
    MediaController mediaController;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_fb_video_downloader);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.navigation_clear);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorTextPrimary));
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        url=intent.getStringExtra("videoUrl");
        videoView=(VideoView)findViewById(R.id.video_player);
        mediaController=new MediaController(VideoPlayerActivity.this);
        playVideo();
    }

    private void playVideo() {
        Uri uri= Uri.parse(url);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setVolume(0,10);
            }
        });
        videoView.requestFocus();
        videoView.start();
    }


}
