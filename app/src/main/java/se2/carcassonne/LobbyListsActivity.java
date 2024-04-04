package se2.carcassonne;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.lobby.model.Lobby;
import se2.carcassonne.lobby.repository.LobbyRepository;
import se2.carcassonne.lobby.viewmodel.LobbyListAdapter;
import se2.carcassonne.lobby.viewmodel.LobbyListViewModel;
import se2.carcassonne.player.repository.PlayerRepository;

import java.sql.Timestamp;

public class LobbyListsActivity extends AppCompatActivity {

    private LobbyRepository lobbyRepository;
    private LobbyListAdapter adapter;
    private LobbyListViewModel viewModel;

//    Dummy lobbies
// Create lobby instances
Lobby lobby1 = new Lobby(1L, "Lobby 1", new Timestamp(System.currentTimeMillis()), "lobby", 2);
Lobby lobby2 = new Lobby(2L, "Lobby 2", new Timestamp(System.currentTimeMillis()), "lobby", 1);
Lobby lobby3 = new Lobby(3L, "Lobby 3", new Timestamp(System.currentTimeMillis()), "lobby", 3);

//    public LobbyListsActivity() {
//    }
//
//    public LobbyListsActivity(LobbyRepository lobbyRepository, LobbyListViewModel viewModel) {
//        this.viewModel = viewModel;
//        this.lobbyRepository = lobbyRepository;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_list_activity);

        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        RecyclerView recyclerView = findViewById(R.id.list_of_lobbies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LobbyListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(LobbyListViewModel.class);

        lobbyRepository = new LobbyRepository(PlayerRepository.getInstance());
        lobbyRepository.connectToWebSocketServer();
        Log.d("connection", "connection performed ");
//      Create dummy lobbies  - very primitive I KNOW
        lobbyRepository.createLobby(lobby1);
        lobbyRepository.createLobby(lobby2);
        lobbyRepository.createLobby(lobby3);



        viewModel.getLobbyListLiveData().observe(this, lobbyList -> {
            // Update UI with the new lobby list
            adapter.updateData(lobbyList);
        });

        viewModel.fetchLobbies();
    }

}