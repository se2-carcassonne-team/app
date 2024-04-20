package se2.carcassonne;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import se2.carcassonne.databinding.InLobbyActivityBinding;
import se2.carcassonne.helper.mapper.MapperHelper;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.lobby.model.GameState;
import se2.carcassonne.lobby.model.Lobby;
import se2.carcassonne.lobby.viewmodel.LobbyViewModel;
import se2.carcassonne.lobby.viewmodel.PlayerListAdapter;
import se2.carcassonne.player.repository.PlayerRepository;

public class InLobbyActivity extends AppCompatActivity {
    InLobbyActivityBinding binding;

    private LobbyViewModel lobbyViewmodel;
    private PlayerListAdapter adapter;
    private final MapperHelper mapperHelper = new MapperHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = InLobbyActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        Lobby currentLobby = mapperHelper.getLobbyFromJsonString(Objects.requireNonNull(intent.getStringExtra(GameState.LOBBY.getDisplayName())));
        if (!Objects.equals(currentLobby.getLobbyAdminId(), PlayerRepository.getInstance().getCurrentPlayer().getId())){
            binding.gameLobbyStartGameBtn.setVisibility(View.GONE);
        }

        RecyclerView recyclerView = findViewById(R.id.rvListOfPlayers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlayerListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        lobbyViewmodel = new LobbyViewModel();
        binding.textViewLobbyName.setText(mapperHelper.getLobbyName(intent.getStringExtra("LOBBY")));

        lobbyViewmodel.getPlayerLeavesLobbyLiveData().observe(this, playerWhoLeft -> {
            if (playerWhoLeft != null){
                finish();
            }
        });
        lobbyViewmodel.getMessageLiveDataListPlayers().observe(this, playerList -> adapter.updateDataWithLobby(playerList, intent.getStringExtra("LOBBY")));
        lobbyViewmodel.getPlayerJoinsOrLeavesLobbyLiveData().observe(this, playerList -> adapter.updateData(playerList));
        lobbyViewmodel.getPlayerInLobbyReceivesUpdatedLobbyLiveData().observe(this, newGameLobby -> {
            if (newGameLobby != null && !newGameLobby.startsWith("RESET")){
                binding.gameLobbyStartGameBtn.setVisibility(View.VISIBLE);
                adapter.updateGameLobby(newGameLobby);
            } else if (newGameLobby != null) {
                adapter.updateGameLobby(newGameLobby.split("\\|")[1]);
                binding.gameLobbyStartGameBtn.setVisibility(View.GONE);
            }
        });
        lobbyViewmodel.getGameToBeStartedLiveData().observe(this, gameSessionId -> {
            Intent startGameIntent = new Intent(this, GameBoardActivity.class);
            startActivity(startGameIntent);
        });
        binding.gameLobbyStartGameBtn.setOnClickListener(view -> lobbyViewmodel.startGame(currentLobby.getId()));
        binding.gameLobbyLeaveBtn.setOnClickListener(view -> lobbyViewmodel.leaveLobby());

        lobbyViewmodel.getAllPlayers(currentLobby);
    }

    @Override
    protected void onDestroy() {
        lobbyViewmodel.getPlayerInLobbyReceivesUpdatedLobbyLiveData().setValue(null);
        lobbyViewmodel.getPlayerLeavesLobbyLiveData().setValue(null);
        super.onDestroy();
    }
}