package se2.carcassonne.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import se2.carcassonne.R;
import se2.carcassonne.model.Player;

public class ScoreboardAdapter extends RecyclerView.Adapter<ScoreboardAdapter.ViewHolder> {

    private final List<Map.Entry<String, Integer>> playerPoints;

    private final List<Player> playerList;

    public ScoreboardAdapter(Map<String, Integer> playerPoints, List<Player> playerList) {
        this.playerPoints = new ArrayList<>(playerPoints.entrySet());
        this.playerList = playerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scoreboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the current entry
        Map.Entry<String, Integer> playerPoints = this.playerPoints.get(position);

        // set text of playerNameTextView and playerPointsTextView using the player's name and points from the current entry
        holder.playerNameTextView.setText(playerPoints.getKey());
        holder.playerPointsTextView.setText(String.valueOf(playerPoints.getValue()));

        // player color:
        String resourceName = "";
        // 1. get the player object from the playerList using the player's name
        for (Player player : playerList) {
            if (player.getUsername().equals(playerPoints.getKey())) {
                // 2. set the meepleImageView to the player's color
                resourceName = "meeple_" + player.getPlayerColour().name().toLowerCase();
            }
        }

        // 3. set the image resource of the meepleImageView
        int resourceId = holder.itemView.getResources().getIdentifier(resourceName, "drawable", holder.itemView.getContext().getPackageName());
        if (resourceId != 0) {
            holder.meepleImageView.setImageResource(resourceId);
        }
    }

    @Override
    public int getItemCount() {
        return playerPoints.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView playerNameTextView;
        final TextView playerPointsTextView;
        final ImageView meepleImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameTextView = itemView.findViewById(R.id.playerNameTextView);
            playerPointsTextView = itemView.findViewById(R.id.playerPointsTextView);
            meepleImageView = itemView.findViewById(R.id.meepleImageView);
        }
    }

    public void updatePlayerPoints(Map<String, Integer> newPlayerPoints) {
        this.playerPoints.clear();
        this.playerPoints.addAll(newPlayerPoints.entrySet());
        notifyDataSetChanged();
    }
}
