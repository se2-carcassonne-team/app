package se2.carcassonne;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import se2.carcassonne.databinding.InLobbyActivityBinding;
import se2.carcassonne.helper.mapper.MapperHelper;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.lobby.viewmodel.LobbyViewModel;
import se2.carcassonne.lobby.viewmodel.PlayerListAdapter;

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

        RecyclerView recyclerView = findViewById(R.id.rvListOfPlayers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlayerListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        lobbyViewmodel = new LobbyViewModel();
        binding.textViewLobbyName.setText(mapperHelper.getLobbyName(intent.getStringExtra("LOBBY")));

        lobbyViewmodel.getPlayerLeavesLobbyLiveData().observe(this, playerList -> finish());
        lobbyViewmodel.getMessageLiveDataListPlayers().observe(this, playerList -> adapter.updateData(playerList));
        lobbyViewmodel.getPlayerJoinsOrLeavesLobbyLiveData().observe(this, playerList -> adapter.updateData(playerList));

        binding.gameLobbyStartGameBtn.setOnClickListener(view -> {
            Intent startGameIntent = new Intent(InLobbyActivity.this, GameBoardActivity.class);
            startActivity(startGameIntent);
        });
        binding.gameLobbyLeaveBtn.setOnClickListener(view -> lobbyViewmodel.leaveLobby());

        lobbyViewmodel.getAllPlayers(mapperHelper.getLobbyFromJsonString(Objects.requireNonNull(intent.getStringExtra("LOBBY"))));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}