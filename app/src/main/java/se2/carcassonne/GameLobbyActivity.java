package se2.carcassonne;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import se2.carcassonne.databinding.GameLobbyActivityBinding;
import se2.carcassonne.helper.resize.FullscreenHelper;

public class GameLobbyActivity extends AppCompatActivity {
    GameLobbyActivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GameLobbyActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.showListOfLobbies.setOnClickListener(view -> {
            Intent intent = new Intent(GameLobbyActivity.this, LobbyListsActivity.class);
            startActivity(intent);
        });
        FullscreenHelper.setFullscreenAndImmersiveMode(this);
    }
}