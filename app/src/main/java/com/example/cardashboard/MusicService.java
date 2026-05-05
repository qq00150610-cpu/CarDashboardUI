package com.example.cardashboard;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化媒体播放器
        // mediaPlayer = MediaPlayer.create(this, R.raw.sample_music);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case "PLAY":
                        playMusic();
                        break;
                    case "PAUSE":
                        pauseMusic();
                        break;
                    case "STOP":
                        stopMusic();
                        break;
                    case "NEXT":
                        nextSong();
                        break;
                    case "PREVIOUS":
                        previousSong();
                        break;
                }
            }
        }
        return START_STICKY;
    }

    private void playMusic() {
        if (mediaPlayer == null) {
            // mediaPlayer = MediaPlayer.create(this, R.raw.sample_music);
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPlaying = true;
            Toast.makeText(this, "开始播放", Toast.LENGTH_SHORT).show();
        }
    }

    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
            Toast.makeText(this, "暂停播放", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            isPlaying = false;
            Toast.makeText(this, "停止播放", Toast.LENGTH_SHORT).show();
        }
    }

    private void nextSong() {
        // 切换到下一首歌曲的逻辑
        Toast.makeText(this, "下一首", Toast.LENGTH_SHORT).show();
    }

    private void previousSong() {
        // 切换到上一首歌曲的逻辑
        Toast.makeText(this, "上一首", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}