package se2.carcassonne;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_activity);
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        Button startGameBtn = findViewById(R.id.button);
        startGameBtn.setOnClickListener(v -> {
            Intent intent = new Intent(StartupActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}