package se2.carcassonne.lobby.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import se2.carcassonne.R;
import se2.carcassonne.lobby.model.Lobby;

public class LobbyListAdapter extends RecyclerView.Adapter<LobbyListAdapter.LobbyViewHolder> {

    private List<Lobby> lobbyList;

    public LobbyListAdapter(List<Lobby> lobbyList) {
        this.lobbyList = lobbyList;
    }

    public void updateData(List<Lobby> lobbyList) {
        this.lobbyList = lobbyList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LobbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lobby_list_activity, parent, false);
        return new LobbyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LobbyViewHolder holder, int position) {
        Lobby lobby = lobbyList.get(position);
        holder.bind(lobby);
    }

    @Override
    public int getItemCount() {
        return lobbyList.size();
    }

    public void updateData(String lobbyList) {
    }

    static class LobbyViewHolder extends RecyclerView.ViewHolder {

        private TextView lobbyNameTextView;
        private TextView maxPlayersTextView;

        LobbyViewHolder(@NonNull View itemView) {
            super(itemView);
            lobbyNameTextView = itemView.findViewById(R.id.lobbyItem);
            maxPlayersTextView = itemView.findViewById(R.id.maxPlayersLobbyItem);
        }

        void bind(Lobby lobby) {
            lobbyNameTextView.setText(lobby.getName());
            maxPlayersTextView.setText("Max Players: 6");
        }
    }
}
