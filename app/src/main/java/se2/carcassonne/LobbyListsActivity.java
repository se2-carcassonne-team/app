package se2.carcassonne;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import se2.carcassonne.databinding.LobbyListActivityBinding;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.lobby.repository.LobbyRepository;
import se2.carcassonne.lobby.viewmodel.LobbyListAdapter;
import se2.carcassonne.lobby.viewmodel.LobbyListViewModel;
import se2.carcassonne.player.repository.PlayerRepository;

public class LobbyListsActivity extends AppCompatActivity {

    private LobbyListActivityBinding binding;
    private LobbyRepository lobbyRepository;
    private LobbyListAdapter adapter;
    private LobbyListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LobbyListActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        RecyclerView recyclerView = findViewById(R.id.list_of_lobbies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LobbyListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        lobbyRepository = new LobbyRepository(PlayerRepository.getInstance());
        lobbyRepository.connectToWebSocketServer();
        viewModel = new LobbyListViewModel(lobbyRepository);

        viewModel.getMessageLiveDataListLobbies().observe(this, lobbyList -> {
            adapter.updateData(lobbyList);
        });

        viewModel.getCreateLobbyLiveData().observe(this, lobby -> {
            adapter.updateSingleData(lobby);
        });

        viewModel.getPlayerLeavesLobbyLiveData().observe(this, player -> {
            viewModel.getAllLobbies();
        });

        viewModel.getPlayerJoinsLobbyLiveData().observe(this, player -> {
            viewModel.getAllLobbies();
        });

        viewModel.getAllLobbies();
        binding.gameLobbyLeaveBtn.setOnClickListener(view -> finish());
    }
}