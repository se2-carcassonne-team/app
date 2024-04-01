package se2.carcassonne;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import se2.carcassonne.databinding.StartupActivityBinding;
import se2.carcassonne.helper.resize.FullscreenHelper;

public class StartupActivity extends AppCompatActivity {
    StartupActivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = StartupActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        binding.button.setOnClickListener(v -> {
            Intent intent = new Intent(StartupActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}