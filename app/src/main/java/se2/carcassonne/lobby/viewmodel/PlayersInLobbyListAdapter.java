package se2.carcassonne.lobby.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;
import java.util.List;

import se2.carcassonne.R;
import se2.carcassonne.lobby.model.Lobby;
import se2.carcassonne.player.model.Player;

public class PlayersInLobbyListAdapter extends RecyclerView.Adapter<PlayersInLobbyListAdapter.PlayerViewHolder> {
    private List<Lobby> lobbyList;
    private Lobby lobbyToAdd;

    private List<Player> playerList;
    private Player playerToEdit;

    public PlayersInLobbyListAdapter(List<Player> playerList) {
        this.playerList = playerList;
    }

    public void updateData(String newPlayerList) {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("NewPlayerList: " + newPlayerList);
        try {
            playerList = objectMapper.readValue(newPlayerList, new TypeReference<List<Player>>() {});
            notifyDataSetChanged();// Notify RecyclerView about the changes
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle parsing exception if needed
        }
    }

    public void updateSingleDataAdd(String singlePlayer) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            playerToEdit = objectMapper.readValue(singlePlayer, Player.class);
            playerList.add(playerToEdit);
            notifyDataSetChanged(); // Notify RecyclerView about the changes
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle parsing exception if needed
        }
    }
    public void updateSingleDataDelete(String singlePlayer) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            playerToEdit = objectMapper.readValue(singlePlayer, Player.class);
            // Find and delete the player with the specified ID
            Iterator<Player> iterator = playerList.iterator();
            while (iterator.hasNext()) {
                Player player = iterator.next();
                if (player.getId().equals(playerToEdit.getId())) {
                    iterator.remove(); // Remove the player from the list
                    break; // Assuming there's only one player with the given ID, so we can exit the loop
                }
            }
            notifyDataSetChanged(); // Notify RecyclerView about the changes
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle parsing exception if needed
        }
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_player_in_lobby_item_row, parent, false);
        return new PlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = playerList.get(position);
        holder.bind(player);
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }


    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
//        private final CardView cardView;
        private final TextView playerNameTextView;

        private final ConstraintLayout layout;


        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
//            cardView = itemView.findViewById(R.id.cardView);
            layout = itemView.findViewById(R.id.cvPlayerList);
            playerNameTextView = itemView.findViewById(R.id.tvPlayerName);
//            cardView.setOnClickListener(view -> {
//                // TODO: Implement joining a lobby
//            });
        }

        public void bind(Player player) {
            playerNameTextView.setText(player.getUsername());
            System.out.println("Player username: " + player.getUsername());
        }
    }
}
