package se2.carcassonne.ui;

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
        FullscreenHelper.setFullscreenAndImmersiveMode(this);
        binding.createLobbyBtn.setOnClickListener(view -> showCreateLobbyDialog());
        binding.showListOfLobbies.setOnClickListener(view -> {
            Intent intent = new Intent(GameLobbyActivity.this, LobbyListActivity.class);
            startActivity(intent);
        });
        binding.gameLobbyLeaveBtn.setOnClickListener(view -> finish());
    }

    private void showCreateLobbyDialog(){
        new CreateLobbyDialog().show(getSupportFragmentManager(), "createLobbyDialog");
    }


}