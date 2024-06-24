package se2.carcassonne.helper.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import se2.carcassonne.R;

public class MusicPlayer {
    private static MediaPlayer mediaPlayer;
    private static boolean isMuted = false;
    private static final String TAG = "MusicPlayer";

    private MusicPlayer() {
        // Leerer Konstruktor, um die Instanziierung zu verhindern
    }

    public static void playBackgroundMusic(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.background_music);
            mediaPlayer.setLooping(true);
        }
        if (!isMuted) {
            mediaPlayer.start();
        }
    }

    public static void toggleMute() {
        if (mediaPlayer != null) {
            if (isMuted) {
                mediaPlayer.start(); // Resume music if muted
                Log.d(TAG, "Music resumed");
            } else {
                mediaPlayer.pause(); // Pause music if not muted
                Log.d(TAG, "Music paused");
            }
            isMuted = !isMuted; // Toggle mute state
            Log.d(TAG, "Mute state is now: " + isMuted);
        } else {
            Log.d(TAG, "MediaPlayer is null, cannot toggle mute");
        }
    }

}
