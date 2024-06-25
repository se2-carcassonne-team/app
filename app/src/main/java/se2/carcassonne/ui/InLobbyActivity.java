package se2.carcassonne.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import se2.carcassonne.R;
import se2.carcassonne.databinding.InLobbyActivityBinding;
import se2.carcassonne.helper.mapper.MapperHelper;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.model.GameState;
import se2.carcassonne.model.Lobby;
import se2.carcassonne.model.Player;
import se2.carcassonne.repository.GameSessionRepository;
import se2.carcassonne.repository.PlayerRepository;
import se2.carcassonne.viewmodel.GameSessionViewModel;
import se2.carcassonne.viewmodel.LobbyViewModel;
import se2.carcassonne.viewmodel.PlayerListAdapter;

public class InLobbyActivity extends AppCompatActivity {
    InLobbyActivityBinding binding;
    private LobbyViewModel lobbyViewmodel;
    private final MapperHelper mapperHelper = new MapperHelper();
    String allPlayersInLobby;
    Animation pulseAnimation;
    private static final String INLOBBY_ACTIVITY = "InLobbyActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GameSessionRepository gameSessionRepository;
        PlayerListAdapter adapter;
        GameSessionViewModel gameSessionViewModel;
        Log.e(INLOBBY_ACTIVITY, "onCreate");
        super.onCreate(savedInstanceState);
        binding = InLobbyActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        AtomicReference<Lobby> currentLobby = new AtomicReference<>(mapperHelper.getLobbyFromJsonString(Objects.requireNonNull(intent.getStringExtra(GameState.LOBBY.getDisplayName()))));
        if (!Objects.equals(currentLobby.get().getLobbyAdminId(), PlayerRepository.getInstance().getCurrentPlayer().getId())) {
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
        gameSessionViewModel.sendGetAllPlayersInLobby(currentLobby.get().getId());


        lobbyViewmodel.getPlayerLeavesLobbyLiveData().observe(this, playerWhoLeft -> {
            if (playerWhoLeft != null) {
                finish();
            }
        });
        lobbyViewmodel.getMessageLiveDataListPlayers().observe(this, playerList ->
                    adapter.updateDataWithLobby(playerList, intent.getStringExtra("LOBBY"))
        );
        lobbyViewmodel.getPlayerJoinsOrLeavesLobbyLiveData().observe(this, adapter::updateData);
        lobbyViewmodel.getPlayerInLobbyReceivesUpdatedLobbyLiveData().observe(this, newGameLobby -> {
            if (newGameLobby != null && !newGameLobby.startsWith("RESET")) {
                binding.gameLobbyStartGameBtn.setVisibility(View.VISIBLE);
                // Load the animation
                pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse);
                // Set the animation to repeat indefinitely
                binding.gameLobbyStartGameBtn.startAnimation(pulseAnimation);
                adapter.updateGameLobby(newGameLobby);
                currentLobby.set(mapperHelper.getLobbyFromJsonString(newGameLobby));
            } else if (newGameLobby != null) {
                adapter.updateGameLobby(newGameLobby.split("\\|")[1]);
                binding.gameLobbyStartGameBtn.clearAnimation();
                binding.gameLobbyStartGameBtn.setVisibility(View.GONE);
            }
        });
        lobbyViewmodel.getGameToBeStartedLiveData().observe(this, gameSessionId -> {
            if(gameSessionId != null){
                Intent startGameIntent = new Intent(this, GameBoardActivity.class);
                Long gameSessionIdLong = Long.parseLong(gameSessionId);
                // Set the gameSessionId for the currentPlayer
                PlayerRepository.getInstance().getCurrentPlayer().setGameSessionId(gameSessionIdLong);
                gameSessionRepository.subscribeToNextTurn(gameSessionIdLong);
                gameSessionRepository.subscribeToPlacedTile(gameSessionIdLong);
                gameSessionRepository.subscribeToGameFinished(gameSessionIdLong);
                gameSessionRepository.subscribeToPointsForCompletedRoad(gameSessionIdLong);
                gameSessionRepository.subscribeToCanICheat();

                /*
                 * All Players In Lobby Observable
                 */
                gameSessionViewModel.allPlayersInLobbyLiveData().observe(this, allPlayers -> {
                    allPlayersInLobby = mapperHelper.getJsonStringFromList(allPlayers);
                    gameSessionRepository.subscribeToGetAllPlayersInLobby(gameSessionIdLong);
                });

                // new!!!!
                List<Player> playerList = adapter.getPlayerList();
                startGameIntent.putExtra("playerList", (Serializable) playerList);

                startGameIntent.putExtra("ALL_PLAYERS", allPlayersInLobby);
                startGameIntent.putExtra("LOBBY_ADMIN_ID", currentLobby.get().getLobbyAdminId() + "");
                startGameIntent.putExtra("LOBBY_ID", currentLobby.get().getId() + "");
                startActivity(startGameIntent);
            }

        });
        binding.gameLobbyStartGameBtn.setOnClickListener(view -> {
                binding.gameLobbyStartGameBtn.clearAnimation();
                lobbyViewmodel.startGame(currentLobby.get().getId());
        });
        binding.gameLobbyLeaveBtn.setOnClickListener(view -> lobbyViewmodel.leaveLobby());

        lobbyViewmodel.getAllPlayers(currentLobby.get());

        // Handle back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // execute leave-lobby logic
                lobbyViewmodel.leaveLobby();
            }
        });
    }

    @Override
    protected void onStop() {
        Log.e(INLOBBY_ACTIVITY, "onStop");
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        Log.e(INLOBBY_ACTIVITY, "onDestroy");

        lobbyViewmodel.getPlayerInLobbyReceivesUpdatedLobbyLiveData().setValue(null);
        lobbyViewmodel.getPlayerLeavesLobbyLiveData().setValue(null);
        lobbyViewmodel.getGameToBeStartedLiveData().postValue(null);

        super.onDestroy();
    }
}