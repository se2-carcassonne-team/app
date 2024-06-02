package se2.carcassonne.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

import se2.carcassonne.R;
import se2.carcassonne.databinding.GameEndActivityBinding;
import se2.carcassonne.databinding.LobbyListActivityBinding;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.helper.resize.FullscreenHelper;

public class GameEndActivity extends AppCompatActivity {
    GameEndActivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GameEndActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);
        if (getIntent().hasExtra("PLAYER0")) {
            String first = getIntent().getStringExtra("PLAYER0");
            binding.tvFirstPlace.setText(first);
        }
        if (getIntent().hasExtra("PLAYER1")) {
            binding.tvSecondPlace.setText(getIntent().getStringExtra("PLAYER1"));
        }
        if (getIntent().hasExtra("PLAYER2")) {
            binding.tvThirdPlace.setText(getIntent().getStringExtra("PLAYER2"));
        }


        binding.btnMainMenu.setOnClickListener(v -> {
            Intent intent = new Intent(GameEndActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            WebSocketClient.getInstance().cancelAllSubscriptions();
        });
    }
}