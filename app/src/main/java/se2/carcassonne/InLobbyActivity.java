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
        LobbyRepository lobbyRepository = new LobbyRepository(PlayerRepository.getInstance());
        lobbyViewmodel = new LobbyListViewModel(lobbyRepository);
        lobbyRepository.connectToWebSocketServer();
        binding.textViewLobbyName.setText(lobbyViewmodel.getLobbyName(intent.getStringExtra("LOBBY")));

        binding.gameLobbyLeaveBtn.setOnClickListener(view -> {
            lobbyViewmodel.leaveLobby();
        });

        binding.gameLobbyStartGameBtn.setOnClickListener(view -> {
            Intent startGameIntent = new Intent(InLobbyActivity.this, GameBoardActivity.class);
            startActivity(startGameIntent);
        });


        lobbyViewmodel.getPlayerJoinsLobbyLiveData().observe(this, playerWhoJoined -> {
            PlayerRepository.getInstance().updateCurrentPlayerLobby(lobbyViewmodel.getLobbyFromPlayer(playerWhoJoined));
        });

        lobbyViewmodel.getPlayerLeavesLobbyLiveData().observe(this, playerWhoLeft -> {
            if (lobbyViewmodel.getPlayerId(playerWhoLeft) == (PlayerRepository.getInstance().getCurrentPlayer().getId())){
                PlayerRepository.getInstance().updateCurrentPlayerLobby(null);
                finish();
            } else {
                PlayerRepository.getInstance().getCurrentPlayer().getGameLobbyDto().setNumPlayers(PlayerRepository.getInstance().getCurrentPlayer().getGameLobbyDto().getNumPlayers() - 1);
            }
        });
    }
}