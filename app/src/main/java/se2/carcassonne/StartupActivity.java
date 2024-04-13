package se2.carcassonne;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import se2.carcassonne.databinding.StartupActivityBinding;
import se2.carcassonne.helper.animation.AnimationHelper;
import se2.carcassonne.helper.resize.FullscreenHelper;

public class StartupActivity extends AppCompatActivity {
    StartupActivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = StartupActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        final ImageView castle = findViewById(R.id.imageView);
        castle.setVisibility(View.INVISIBLE);


        final ImageView carcassonneLogo = findViewById(R.id.imageView2);
        carcassonneLogo.setVisibility(View.INVISIBLE);

        final Button startGameBtn = findViewById(R.id.startgame);
        startGameBtn.setVisibility(View.INVISIBLE);

        final Button loggameboard = findViewById(R.id.button2);


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

        //binding.button.setOnClickListener(v -> {
            Intent intent = new Intent(StartupActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        loggameboard.setOnClickListener(v -> {

            //binding.button.setOnClickListener(v -> {
            Intent intent = new Intent(StartupActivity.this, GameBoardActivity.class);
            startActivity(intent);
        });



    }
}