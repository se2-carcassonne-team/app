package se2.carcassonne.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import se2.carcassonne.R;
import se2.carcassonne.databinding.InLobbyActivityBinding;
import se2.carcassonne.helper.mapper.MapperHelper;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.model.GameState;
import se2.carcassonne.model.Lobby;
import se2.carcassonne.repository.GameSessionRepository;
import se2.carcassonne.viewmodel.GameSessionViewModel;
import se2.carcassonne.viewmodel.LobbyViewModel;
import se2.carcassonne.viewmodel.PlayerListAdapter;
import se2.carcassonne.repository.PlayerRepository;

public class InLobbyActivity extends AppCompatActivity {
    InLobbyActivityBinding binding;
    private LobbyViewModel lobbyViewmodel;
    private GameSessionViewModel gameSessionViewModel;
    private PlayerListAdapter adapter;
    private final MapperHelper mapperHelper = new MapperHelper();
    private GameSessionRepository gameSessionRepository;
    String allPlayersInLobby;
    Animation pulseAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = InLobbyActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        Lobby currentLobby = mapperHelper.getLobbyFromJsonString(Objects.requireNonNull(intent.getStringExtra(GameState.LOBBY.getDisplayName())));
        if (!Objects.equals(currentLobby.getLobbyAdminId(), PlayerRepository.getInstance().getCurrentPlayer().getId())) {
            binding.gameLobbyStartGameBtn.setVisibility(View.GONE);
        }

        RecyclerView recyclerView = findViewById(R.id.rvListOfPlayers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlayerListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        gameSessionRepository = GameSessionRepository.getInstance();
        lobbyViewmodel = new LobbyViewModel();
        gameSessionViewModel = new GameSessionViewModel();
        binding.textViewLobbyName.setText(mapperHelper.getLobbyName(intent.getStringExtra("LOBBY")));

        /*
         * Get All Players In Lobby
         */
        gameSessionViewModel.sendGetAllPlayersInLobby(currentLobby.getId());


        lobbyViewmodel.getPlayerLeavesLobbyLiveData().observe(this, playerWhoLeft -> {
            if (playerWhoLeft != null) {
                finish();
            }
        });
        lobbyViewmodel.getMessageLiveDataListPlayers().observe(this, playerList -> adapter.updateDataWithLobby(playerList, intent.getStringExtra("LOBBY")));
        lobbyViewmodel.getPlayerJoinsOrLeavesLobbyLiveData().observe(this, playerList -> adapter.updateData(playerList));
        lobbyViewmodel.getPlayerInLobbyReceivesUpdatedLobbyLiveData().observe(this, newGameLobby -> {
            if (newGameLobby != null && !newGameLobby.startsWith("RESET")) {
                binding.gameLobbyStartGameBtn.setVisibility(View.VISIBLE);
                // Load the animation
                pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse);
                // Set the animation to repeat indefinitely
//                pulseAnimation.setRepeatCount(Animation.INFINITE);
                binding.gameLobbyStartGameBtn.startAnimation(pulseAnimation);
                adapter.updateGameLobby(newGameLobby);
            } else if (newGameLobby != null) {
                adapter.updateGameLobby(newGameLobby.split("\\|")[1]);
                binding.gameLobbyStartGameBtn.clearAnimation();
                binding.gameLobbyStartGameBtn.setVisibility(View.GONE);
            }
        });
        lobbyViewmodel.getGameToBeStartedLiveData().observe(this, gameSessionId -> {
            Intent startGameIntent = new Intent(this, GameBoardActivity.class);
            Long gameSessionIdLong = Long.parseLong(gameSessionId);
//            Set the gameSessionId for the currentPlayer
            PlayerRepository.getInstance().getCurrentPlayer().setGameSessionId(gameSessionIdLong);
            gameSessionRepository.subscribeToNextTurn(gameSessionIdLong);
            gameSessionRepository.subscribeToPlacedTile(gameSessionIdLong);

            /*
             * All Players In Lobby Observable
             */
            gameSessionViewModel.allPlayersInLobbyLiveData().observe(this, allPlayers -> {
                allPlayersInLobby = mapperHelper.getJsonStringFromList(allPlayers);
                gameSessionRepository.subscribeToGetAllPlayersInLobby(gameSessionIdLong);
            });
            startGameIntent.putExtra("ALL_PLAYERS", allPlayersInLobby);
            startGameIntent.putExtra("LOBBY_ADMIN_ID", currentLobby.getLobbyAdminId() + "");
            startGameIntent.putExtra("LOBBY_ID", currentLobby.getId() + "");
            startActivity(startGameIntent);
        });
        binding.gameLobbyStartGameBtn.setOnClickListener(view -> {
                binding.gameLobbyStartGameBtn.clearAnimation();
                lobbyViewmodel.startGame(currentLobby.getId());
        });
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