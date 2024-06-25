package se2.carcassonne.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se2.carcassonne.R;
import se2.carcassonne.model.FinishedTurnDto;
import se2.carcassonne.model.Player;
import se2.carcassonne.repository.PlayerRepository;


public class ScoreboardAdapter extends RecyclerView.Adapter<ScoreboardAdapter.ViewHolder> {

    private final List<Map.Entry<String, Integer>> playersPoints;

    private final List<Player> playerList;
    private final GameSessionViewModel gameSessionViewModel;


    public ScoreboardAdapter(Map<String, Integer> playersPoints, List<Player> playerList) {
        this.playersPoints = new ArrayList<>(playersPoints.entrySet());
        this.playerList = playerList;
        this.gameSessionViewModel = new GameSessionViewModel();
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
        Map.Entry<String, Integer> playerPoints = this.playersPoints.get(position);

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

        if (Boolean.TRUE.equals(PlayerRepository.getInstance().getCurrentPlayer().getCanCheat())
                || Boolean.TRUE.equals(PlayerRepository.getInstance().getCurrentPlayer().getHasAccused())
                || Boolean.TRUE.equals(gameSessionViewModel.getCheaterFound().getValue())) {
            holder.accuseButton.setVisibility(View.GONE);
        }


        // Handle accuse button click
        holder.accuseButton.setOnClickListener(v -> {
            // send a message to the server that the player wants to accuse the player at the current position (with the player's ID)

            // 1) get the player's ids from the playerList using the player's name
            Long accusedPlayerId = null;
            for (Player player : playerList) {
                if (player.getUsername().equals(playerPoints.getKey())) {
                    accusedPlayerId = player.getId();
                }
            }

            if (accusedPlayerId != null) {
                // Get the current gameSessionId from the Player object
                Long gameSessionId = PlayerRepository.getInstance().getCurrentPlayer().getGameSessionId();

                // Initialize a Map with all player IDs and 0 as each value
                Map<Long, Integer> idsPoints = new HashMap<>();
                for (Player player : playerList) {
                    idsPoints.put(player.getId(), 0);
                }

                // Initialize the FinishedTurnDto with the current gameSessionId
                FinishedTurnDto finishedTurnDto = new FinishedTurnDto(gameSessionId, idsPoints, null);


                // send the accuse request to the server
                gameSessionViewModel.sendAccuseRequest(PlayerRepository.getInstance().getCurrentPlayer().getId(), accusedPlayerId, finishedTurnDto);

                // set the players hasAccused to true
                PlayerRepository.getInstance().getCurrentPlayer().setHasAccused(true);

                // show a toast message that the player with the username has been accused
                Toast.makeText(v.getContext(), "You have accused " + playerPoints.getKey(), Toast.LENGTH_SHORT).show();

                this.notifyDataSetChanged();
            } else {
                // handle error
            }

        });
    }

    @Override
    public int getItemCount() {
        return playersPoints.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView playerNameTextView;
        final TextView playerPointsTextView;
        final ImageView meepleImageView;
        final Button accuseButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameTextView = itemView.findViewById(R.id.playerNameTextView);
            playerPointsTextView = itemView.findViewById(R.id.playerPointsTextView);
            meepleImageView = itemView.findViewById(R.id.meepleImageView);
            accuseButton = itemView.findViewById(R.id.button_accuse);
        }
    }

    public void updatePlayerPoints(Map<String, Integer> newPlayerPoints) {
        this.playersPoints.clear();
        this.playersPoints.addAll(newPlayerPoints.entrySet());
        notifyDataSetChanged();
    }

}
