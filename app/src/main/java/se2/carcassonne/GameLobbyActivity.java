package se2.carcassonne;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import se2.carcassonne.databinding.GameLobbyActivityBinding;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.lobby.repository.LobbyRepository;
import se2.carcassonne.lobby.viewmodel.LobbyListViewModel;
import se2.carcassonne.player.repository.PlayerRepository;

public class GameLobbyActivity extends AppCompatActivity {
    GameLobbyActivityBinding binding;
    private LobbyListViewModel gameLobbyViewModel;
    LobbyRepository gameLobbyRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GameLobbyActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);
        gameLobbyRepository = new LobbyRepository(PlayerRepository.getInstance());
        gameLobbyViewModel = new LobbyListViewModel(gameLobbyRepository);
        binding.createLobbyBtn.setOnClickListener(view -> showCreateLobbyDialog());
        binding.showListOfLobbies.setOnClickListener(view -> {
            Intent intent = new Intent(GameLobbyActivity.this, LobbyListsActivity.class);
            startActivity(intent);
        });
        binding.gameLobbyLeaveBtn.setOnClickListener(view -> finish());
    }

    private void showCreateLobbyDialog(){
        new CreateLobbyDialog().show(getSupportFragmentManager(), "createLobbyDialog");
    }


}