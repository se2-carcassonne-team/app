package se2.carcassonne.viewmodel;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import se2.carcassonne.R;
import se2.carcassonne.model.Player;

// ScoreboardAdapter.java
public class ScoreboardAdapter extends RecyclerView.Adapter<ScoreboardAdapter.ViewHolder> {

    // map of username + points
//    private final Map<String, Integer> playerPoints;
//
//    public ScoreboardAdapter(Map<String, Integer> playerPoints) {
//        this.playerPoints = playerPoints;
//    }
    private final List<Map.Entry<String, Integer>> playerPoints;

    public ScoreboardAdapter(Map<String, Integer> playerPoints) {
        this.playerPoints = new ArrayList<>(playerPoints.entrySet());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scoreboard, parent, false);
        return new ViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        // set text of playerNameTextView and playerPointsTextView using the player's name and points (both from the map)
//        for (Map.Entry<String, Integer> playerPoints : playerPoints.entrySet()) {
//            Log.e("ScoreboardAdapter", "onBindViewHolder: " + playerPoints.getKey() + " " + playerPoints.getValue());
//            holder.playerNameTextView.setText(playerPoints.getKey());
//            holder.playerPointsTextView.setText(String.valueOf(playerPoints.getValue()));
//        }
//    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the current entry
        Map.Entry<String, Integer> playerPoints = this.playerPoints.get(position);

        // set text of playerNameTextView and playerPointsTextView using the player's name and points from the current entry
        holder.playerNameTextView.setText(playerPoints.getKey());
        holder.playerPointsTextView.setText(String.valueOf(playerPoints.getValue()));
    }

    @Override
    public int getItemCount() {
        return playerPoints.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView playerNameTextView;
        final TextView playerPointsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameTextView = itemView.findViewById(R.id.playerNameTextView);
            playerPointsTextView = itemView.findViewById(R.id.playerPointsTextView);
        }
    }

//    public void updatePlayerPoints(Map<String, Integer> newPlayerPoints) {
//        Log.e("ScoreboardAdapter", "updatePlayerPoints: " + newPlayerPoints.toString());
//        this.playerPoints.putAll(newPlayerPoints);
//        notifyDataSetChanged();
//    }
    public void updatePlayerPoints(Map<String, Integer> newPlayerPoints) {
        this.playerPoints.clear();
        this.playerPoints.addAll(newPlayerPoints.entrySet());
        notifyDataSetChanged();
    }
}
