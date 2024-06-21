package se2.carcassonne.ui;


import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import se2.carcassonne.databinding.GameEndActivityBinding;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.viewmodel.LobbyViewModel;
import se2.carcassonne.viewmodel.PlayerViewModel;

public class GameEndActivity extends AppCompatActivity {
    GameEndActivityBinding binding;
    LobbyViewModel lobbyViewModel;
    PlayerViewModel playerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GameEndActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        lobbyViewModel = new LobbyViewModel();
        playerViewModel = new PlayerViewModel();

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
            playerViewModel.resetCurrentPlayer();
            startActivity(nextIntent);
        });

        // Handle back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent nextIntent = new Intent(GameEndActivity.this, GameLobbyActivity.class);
                playerViewModel.resetCurrentPlayer();
                startActivity(nextIntent);
            }
        });
    }

}