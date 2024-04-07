package se2.carcassonne;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import se2.carcassonne.databinding.InLobbyActivityBinding;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.lobby.repository.LobbyRepository;
import se2.carcassonne.lobby.viewmodel.LobbyListViewModel;
import se2.carcassonne.player.repository.PlayerRepository;

public class InLobbyActivity extends AppCompatActivity {
    InLobbyActivityBinding binding;
    private LobbyListViewModel lobbyViewmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = InLobbyActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        FullscreenHelper.setFullscreenAndImmersiveMode(this);
        PlayerRepository.getInstance();
        LobbyRepository lobbyRepository = new LobbyRepository(PlayerRepository.getInstance());
        lobbyViewmodel = new LobbyListViewModel(lobbyRepository);
        lobbyRepository.connectToWebSocketServer();
        binding.textViewLobbyName.setText(lobbyViewmodel.getLobbyName(intent.getStringExtra("LOBBY")));
        binding.gameLobbyLeaveBtn.setOnClickListener(view -> {
            System.out.println(PlayerRepository.getInstance().getCurrentPlayer());
            lobbyViewmodel.leaveLobby(PlayerRepository.getInstance().getCurrentPlayer());
            finish();
        });
        System.out.println("Now In Lobby" + PlayerRepository.getInstance().getCurrentPlayer());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lobbyViewmodel.leaveLobby(PlayerRepository.getInstance().getCurrentPlayer());
    }
}