package se2.carcassonne;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import se2.carcassonne.databinding.InLobbyActivityBinding;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.lobby.model.Lobby;
import se2.carcassonne.lobby.repository.LobbyRepository;
import se2.carcassonne.lobby.viewmodel.LobbyListViewModel;
import se2.carcassonne.lobby.viewmodel.PlayersInLobbyListAdapter;
import se2.carcassonne.player.repository.PlayerRepository;

public class InLobbyActivity extends AppCompatActivity {

    Lobby lobby;
    InLobbyActivityBinding binding;
    private LobbyListViewModel lobbyViewmodel;
    private PlayersInLobbyListAdapter adapter;
    private LobbyRepository lobbyRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = InLobbyActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        FullscreenHelper.setFullscreenAndImmersiveMode(this);


        RecyclerView recyclerView = findViewById(R.id.rvListOfPlayers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlayersInLobbyListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        lobbyRepository = new LobbyRepository(PlayerRepository.getInstance());
        lobbyRepository.connectToWebSocketServer();

        lobbyViewmodel = new LobbyListViewModel(lobbyRepository);
//        Setting the name of the lobby
        binding.textViewLobbyName.setText(lobbyViewmodel.getLobbyName(intent.getStringExtra("LOBBY")));


        lobbyViewmodel.getMessageLiveDataListPlayers().observe(this, playerList -> {
            Log.d("Players in lobby", "Players " + playerList);
            System.out.println("blablabla" + playerList);
            adapter.updateData(playerList);
        });



        lobbyViewmodel.getAllPlayers(lobbyViewmodel.getLobbyFromJsonString(intent.getStringExtra("LOBBY")));
        
        LobbyRepository lobbyRepository = new LobbyRepository(PlayerRepository.getInstance());
        lobbyViewmodel = new LobbyListViewModel(lobbyRepository);
        lobbyRepository.connectToWebSocketServer();
        binding.textViewLobbyName.setText(lobbyViewmodel.getLobbyName(intent.getStringExtra("LOBBY")));

        binding.gameLobbyLeaveBtn.setOnClickListener(view -> {
            lobbyViewmodel.leaveLobby();
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