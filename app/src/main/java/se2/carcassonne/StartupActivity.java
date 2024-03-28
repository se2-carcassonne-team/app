package se2.carcassonne;

import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class StartupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_activity);

        getWindow().getDecorView().getRootView().setOnApplyWindowInsetsListener(
                new View.OnApplyWindowInsetsListener() {
                    @NonNull
                    @Override
                    public WindowInsets onApplyWindowInsets(@NonNull View v, @NonNull WindowInsets insets) {
                        setFullscreenAndImmersiveMode();
                        return getWindow().getDecorView().getRootView().onApplyWindowInsets(insets);
                    }
                }
        );
        final ImageView carcassonneLogoImg = findViewById(R.id.imageView1);
        final ImageView carcassonneTitleImg = findViewById(R.id.imageView2);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            setFullscreenAndImmersiveMode();
        }
    }
    private void setFullscreenAndImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}