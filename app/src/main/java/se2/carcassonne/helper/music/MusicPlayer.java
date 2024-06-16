package se2.carcassonne.helper.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import se2.carcassonne.R;

public class MusicPlayer {
    private static MediaPlayer mediaPlayer;
    private static boolean isMuted = false;

    public static void playBackgroundMusic(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.background_music);
            mediaPlayer.setLooping(true);
        }
        if (!isMuted) {
            mediaPlayer.start();
        }
    }

    public static void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isMuted = false; // Reset mute state when music is stopped
    }

    public static void toggleMute() {
        if (mediaPlayer != null) {
            if (isMuted) {
                mediaPlayer.start(); // Resume music if muted
                Log.d("MusicPlayer", "Music resumed");
            } else {
                mediaPlayer.pause(); // Pause music if not muted
                Log.d("MusicPlayer", "Music paused");
            }
            isMuted = !isMuted; // Toggle mute state
            Log.d("MusicPlayer", "Mute state is now: " + isMuted);
        } else {
            Log.d("MusicPlayer", "MediaPlayer is null, cannot toggle mute");
        }
    }

    public static boolean isMuted() {
        return isMuted;
    }
}
