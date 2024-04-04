package se2.carcassonne;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class StartupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_activity);
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        final ImageView castle = findViewById(R.id.imageView);
        castle.setVisibility(View.INVISIBLE);

        final ImageView carcassonneLogo = findViewById(R.id.imageView2);
        carcassonneLogo.setVisibility(View.INVISIBLE);

        final Button startGameBtn = findViewById(R.id.startgame);
        startGameBtn.setVisibility(View.INVISIBLE);

        AnimationHelper.fadeIn(castle, 2000, new Runnable(){
            @Override
            public void run(){
                AnimationHelper.fadeIn(carcassonneLogo, 2000, new Runnable(){
                    @Override
                    public void run(){
                        AnimationHelper.fadeIn(startGameBtn, 2000, new Runnable(){
                            @Override
                            public void run(){
                                AnimationHelper.startButtonAnimation(startGameBtn);
                            }
                        });
                    }
                });
            }
        });

        startGameBtn.setOnClickListener(v -> {
            Intent intent = new Intent(StartupActivity.this, HomeActivity.class);
            startActivity(intent);
        });

    }
}