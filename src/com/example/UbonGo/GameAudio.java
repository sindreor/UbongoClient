package com.example.UbonGo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Debug;
import android.provider.MediaStore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sheep.game.Game;

/**
 * Created by Naptop on 08.04.2016.
 */
public class GameAudio implements OnLoadCompleteListener
{
    private static GameAudio instance = null;
    private SoundPool soundPool;
    private MediaPlayer mediaPlayer;


    public static GameAudio getInstance()
    {
        if (instance == null)
            instance = new GameAudio();
        return instance;
    }

    private GameAudio()
    {
        soundPool = new SoundPool(15, AudioManager.STREAM_MUSIC, 0);
        mediaPlayer = new MediaPlayer();
        soundPool.setOnLoadCompleteListener(this);
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
    {
        soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 0.0f);
    }

    public void play(Context context, int id)
    {
        soundPool.load(context, id, 1);
    }

    public void playMusic(int id)
    {
        mediaPlayer.stop();
        mediaPlayer = MediaPlayer.create(Game.getInstance().getContext(), id);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void setVolume(float volume){
        float log1=(float)(Math.log(100-volume)/Math.log(100));
        mediaPlayer.setVolume(1-log1, 1-log1);

    }


}
