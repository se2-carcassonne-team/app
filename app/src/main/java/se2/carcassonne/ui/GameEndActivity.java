package se2.carcassonne.ui;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import se2.carcassonne.databinding.GameEndActivityBinding;

import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.helper.resize.FullscreenHelper;

import se2.carcassonne.viewmodel.LobbyViewModel;

public class GameEndActivity extends AppCompatActivity {
    GameEndActivityBinding binding;
    LobbyViewModel lobbyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GameEndActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        lobbyViewModel = new LobbyViewModel();

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
            Intent nextIntent = new Intent(this, GameLobbyActivity.class);

            lobbyViewModel.leaveLobby();

            startActivity(nextIntent);
        });
    }

}