package se2.carcassonne.lobby.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

import se2.carcassonne.InLobbyActivity;
import se2.carcassonne.R;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.lobby.model.GameState;
import se2.carcassonne.lobby.model.Lobby;

public class LobbyListAdapter extends RecyclerView.Adapter<LobbyListAdapter.LobbyViewHolder> {
    private List<Lobby> lobbyList;
    public LobbyListAdapter(List<Lobby> lobbyList) {
        this.lobbyList = lobbyList;
    }

    public void updateData(String newLobbyList) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            lobbyList = objectMapper.readValue(newLobbyList, new TypeReference<List<Lobby>>() {});
            notifyDataSetChanged(); // Notify RecyclerView about the changes
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle parsing exception if needed
        }
    }

    public void updateSingleData(String singeLobby) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Lobby lobbyToAdd = objectMapper.readValue(singeLobby, Lobby.class);
            lobbyList.add(lobbyToAdd);
            notifyDataSetChanged(); // Notify RecyclerView about the changes
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public LobbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_lobby_item_row, parent, false);
        return new LobbyViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull LobbyViewHolder holder, int position) {
        Lobby lobby = lobbyList.get(position);
        if (Objects.equals(lobby.getGameState(), GameState.LOBBY.getDisplayName())){
            holder.bind(lobby);
        }
    }

    @Override
    public int getItemCount() {
        return lobbyList.size();
    }


    public static class LobbyViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView lobbyNameTextView;
        private final TextView currentPlayersTextView;
        private Lobby currentLobby;
        private final WebSocketClient webSocketClient = WebSocketClient.getInstance();

        public LobbyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            lobbyNameTextView = itemView.findViewById(R.id.lobbyNameTextView);
            currentPlayersTextView = itemView.findViewById(R.id.currentPlayersTextView);

            if (currentLobby != null && currentLobby.getNumPlayers() == 5){
                cardView.setClickable(false);
            }

            cardView.setOnClickListener(view -> {
                LobbyViewModel viewModel = new LobbyViewModel();
                viewModel.joinLobby(currentLobby);
                Intent intent = new Intent(context, InLobbyActivity.class);
                intent.putExtra("LOBBY", currentLobby.toJsonString());
                webSocketClient.unsubscribe("/topic/lobby-list");
                context.startActivity(intent);
            });
        }

        public void bind(Lobby lobby) {
            currentLobby = lobby;
            lobbyNameTextView.setText(lobby.getName());
            currentPlayersTextView.setText("Current Players: " + lobby.getNumPlayers());
        }
    }
}
