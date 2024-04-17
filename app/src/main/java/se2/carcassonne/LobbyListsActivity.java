package se2.carcassonne;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import se2.carcassonne.databinding.LobbyListActivityBinding;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.lobby.viewmodel.LobbyListAdapter;
import se2.carcassonne.lobby.viewmodel.LobbyViewModel;

public class LobbyListsActivity extends AppCompatActivity {
    LobbyListActivityBinding binding;
    private LobbyListAdapter adapter;
    private final WebSocketClient webSocketClient = WebSocketClient.getInstance();

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

        LobbyViewModel viewModel = new LobbyViewModel();

        viewModel.getListOfAllLobbiesLiveData().observe(this, lobbyList -> adapter.updateData(lobbyList));

        viewModel.getAllLobbies();

        binding.gameLobbyLeaveBtn.setOnClickListener(view -> {
            webSocketClient.unsubscribe("/topic/lobby-list");
            finish();
        });
    }
}