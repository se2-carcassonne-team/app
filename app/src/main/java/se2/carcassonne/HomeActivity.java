package se2.carcassonne;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        ImageView logo_animated = findViewById(R.id.logo_animation);
        logo_animated.setVisibility(View.INVISIBLE);

        AnimationHelper.fadeIn(logo_animated,2000,null);

        Button joinMapBtn = findViewById(R.id.loadmap);



        joinMapBtn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, GameBoardActivity.class);
            startActivity(intent);
        });
    }
}