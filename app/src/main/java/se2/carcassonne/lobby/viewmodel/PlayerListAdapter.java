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
import java.util.Objects;

import se2.carcassonne.R;
import se2.carcassonne.helper.mapper.MapperHelper;
import se2.carcassonne.lobby.model.Lobby;
import se2.carcassonne.player.model.Player;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder> {
    private List<Player> playerList;
    private Player playerToEdit;

    private final MapperHelper mapperHelper = new MapperHelper();
    private Lobby currentLobby;

    public PlayerListAdapter(List<Player> playerList) {
        this.playerList = playerList;
    }

    public void updateDataWithLobby(String newPlayerList, String currentLobbyString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            playerList = objectMapper.readValue(newPlayerList, new TypeReference<List<Player>>() {});
            this.currentLobby = mapperHelper.getLobbyFromJsonString(currentLobbyString);
            notifyDataSetChanged();// Notify RecyclerView about the changes
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void updateData(String newPlayerList) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            playerList = objectMapper.readValue(newPlayerList, new TypeReference<List<Player>>() {});
            notifyDataSetChanged();// Notify RecyclerView about the changes
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    public void updateGameLobby(String currentLobbyString) {
        this.currentLobby = mapperHelper.getLobbyFromJsonString(currentLobbyString);
        notifyDataSetChanged();// Notify RecyclerView about the changes
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
        holder.bind(player, currentLobby);
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public Lobby getCurrentLobby(){
        return this.currentLobby;
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final TextView playerNameTextView;
        private final ConstraintLayout layout;
        private Lobby currentLobby;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.cvPlayerList);
            playerNameTextView = itemView.findViewById(R.id.tvPlayerName);
        }

        public void bind(Player player, Lobby updatedLobby) {
            playerNameTextView.setText(player.getUsername());
            this.currentLobby = updatedLobby;
            if (Objects.equals(player.getId(), currentLobby.getLobbyAdminId())){
                itemView.findViewById(R.id.ivCrownIcon).setVisibility(View.VISIBLE);
            } else {
                itemView.findViewById(R.id.ivCrownIcon).setVisibility(View.GONE);
            }
        }
    }
}
