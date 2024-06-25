package se2.carcassonne.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import se2.carcassonne.R;
import se2.carcassonne.databinding.GameboardActivityBinding;
import se2.carcassonne.helper.mapper.MapperHelper;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.model.Coordinates;
import se2.carcassonne.model.FinishedTurnDto;
import se2.carcassonne.model.GameBoard;
import se2.carcassonne.model.Meeple;
import se2.carcassonne.model.PlacedTileDto;
import se2.carcassonne.model.Player;
import se2.carcassonne.model.PointCalculator;
import se2.carcassonne.model.RoadResult;
import se2.carcassonne.model.Scoreboard;
import se2.carcassonne.model.Tile;
import se2.carcassonne.repository.GameSessionRepository;
import se2.carcassonne.repository.PlayerRepository;
import se2.carcassonne.viewmodel.GameSessionViewModel;
import se2.carcassonne.viewmodel.ScoreboardAdapter;

public class GameBoardActivity extends AppCompatActivity {
    GameboardActivityBinding binding;
    ObjectMapper objectMapper;
    Player currentPlayer;
    private GridView gridView;
    private GameboardAdapter gameboardAdapter;
    private MeepleAdapter meepleAdapter;
    private Tile tileToPlace;
    private Button buttonLeft;
    private Button buttonRight;
    private Button buttonUp;
    private Button buttonDown;
    private Button buttonConfirm;
    private GameBoard gameBoard;
    private ImageView previewTileToPlace;
    private GameSessionViewModel gameSessionViewModel;
    private Button zoomInBtn;
    private Button zoomOutBtn;
    private PointCalculator roadCalculator;
    Animation scaleAnimation = null;
    private static final String DRAWABLE = "drawable";

    private final Map<String, Integer> playerPoints = new HashMap<>();

    private FinishedTurnDto finishedTurnDto;
    private boolean hasCheated = false;
    private static final String TAG = "GameBoardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        binding = GameboardActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

//        Animation for scaling the buttons
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);

        //Bind all UI elements
        bindGameBoardUiElements();

        finishedTurnDto = new FinishedTurnDto();

        objectMapper = new ObjectMapper();
        MapperHelper mapperHelper = new MapperHelper();
        currentPlayer = PlayerRepository.getInstance().getCurrentPlayer();

//        Create a new game board and place a random tile on it
        gameBoard = new GameBoard();
        roadCalculator = new PointCalculator(gameBoard);

//        Set up the grid view
        gridView.setScaleX(3.0f);
        gridView.setScaleY(3.0f);
        gridView.setStretchMode(GridView.NO_STRETCH);

//        Instantiate gameBoardActivityViewModel
        gameSessionViewModel = new GameSessionViewModel();

        GameSessionRepository gameSessionRepository = GameSessionRepository.getInstance();

//        Get the next turn message from the previous activity
        Intent intent = getIntent();
        String currentLobbyAdmin = intent.getStringExtra("LOBBY_ADMIN_ID");
        List<Long> allPlayersInLobby = mapperHelper.getListFromJsonString(intent.getStringExtra("ALL_PLAYERS"));

        gameBoard.initGamePoints(allPlayersInLobby);

        List<Player> playerList = (List<Player>) getIntent().getSerializableExtra("playerList");
        // initialize playerPoints with 0 points for each player
        assert playerList != null;
        for (Player player : playerList) {
            playerPoints.put(player.getUsername(), 0);
        }
        ScoreboardAdapter scoreboardAdapter = new ScoreboardAdapter(playerPoints, playerList);

        // make a network call to the endpoint /app/can-i-cheat with the playerId as the request body to check if this player can cheat
        // set the value of iCanCheat to the response of the network call
        gameSessionViewModel.sendCanICheat(currentPlayer.getId());
        gameSessionViewModel.getICanCheat().observe(this, iCanCheat -> {
            if (iCanCheat != null) {
                scoreboardAdapter.notifyDataSetChanged();
            }
        });

        gameboardAdapter = new GameboardAdapter(this, gameBoard, tileToPlace);
        gridView.setAdapter(gameboardAdapter);


        String resourceName = "meeple_" + currentPlayer.getPlayerColour().name().toLowerCase();
        binding.ivMeepleWithPlayerColor.setImageResource(getResources().getIdentifier(resourceName, DRAWABLE, getPackageName()));

        //binding.tvPlayerPoints.setText(String.valueOf(currentPlayer.getPoints()));


        /*
         * placed tile observable
         */
        gameSessionViewModel.getPlacedTileLiveData().observe(this, tilePlaced -> {
            if(tilePlaced != null) {
                Tile tilePlacedByOtherPlayer = gameboardAdapter.getGameBoard().getAllTiles().get(Math.toIntExact(tilePlaced.getTileId()));
                tilePlacedByOtherPlayer.setRotation(tilePlaced.getRotation());
                tilePlacedByOtherPlayer.setPlacedMeeple(tilePlaced.getPlacedMeeple());
                gameboardAdapter.getGameBoard().placeTile(tilePlacedByOtherPlayer, tilePlaced.getCoordinates());
                gameboardAdapter.notifyDataSetChanged();
            }
        });

        /*
         * finished turn observable (to update points)
         */
        gameSessionViewModel.finishedTurnLiveData().observe(this, finishedTurnDto -> {
            if (finishedTurnDto != null) {

                Map<Long, Integer> newPoints = finishedTurnDto.getPoints();
                for (Player player : playerList) {
                    if (newPoints.containsKey(player.getId())) {
                        playerPoints.put(player.getUsername(), newPoints.get(player.getId()) + playerPoints.get(player.getUsername()));
                    }
                }

                // Update the ScoreboardAdapter with the new player points
                scoreboardAdapter.updatePlayerPoints(playerPoints);

                gameBoard.updatePoints(finishedTurnDto);
                updatePlayerPoints();

                // Remove meeples and get the count of removed meeples
                if(finishedTurnDto.getPlayersWithMeeples() != null){
                    Map<Long, Integer> removedMeeplesMap = gameBoard.finishedTurnRemoveMeeplesOnRoad(finishedTurnDto.getPlayersWithMeeples());

                    // Update the meeple count only if the current player's meeples were removed
                    if (removedMeeplesMap.containsKey(currentPlayer.getId())) {
                        Integer meeplesRemovedForCurrentPlayer = removedMeeplesMap.get(currentPlayer.getId());
                        if (meeplesRemovedForCurrentPlayer != null) {
                            // Subtract the removed meeples from the total count
                            gameboardAdapter.setMeepleCount(gameboardAdapter.getMeepleCount() + meeplesRemovedForCurrentPlayer);
                            binding.tvMeepleCount.setText(gameboardAdapter.getMeepleCount() + "x");
                        }
                    }
                }



                gameboardAdapter.notifyDataSetChanged();
            }
        });



        /*
         * next turn observable
         */
        gameSessionViewModel.getNextTurnMessageLiveData().observe(this, nextTurn -> {
            if (nextTurn != null){
                if (Objects.equals(nextTurn.getPlayerId(), currentPlayer.getId())) {
                    Vibrator vibrator;
                    if (android.os.Build.VERSION.SDK_INT >= 31) {
                        VibratorManager vibratorManager = (VibratorManager) getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                        vibrator = vibratorManager.getDefaultVibrator();
                    } else {
                        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    }

                    // Vibrate for 500 milliseconds to inform user that ii is his/her turn
                    if (vibrator.hasVibrator()) {
                        vibrator.vibrate(500); // for 500 ms
                    }
                    tileToPlace = gameBoard.getAllTiles().get(Math.toIntExact(nextTurn.getTileId()));
                    if (!gameBoard.hasValidPositionForAnyRotation(tileToPlace)) {
                        previewTileToPlace.setImageResource(
                                getResources().getIdentifier(tileToPlace.getImageName() + "_0", DRAWABLE, getPackageName()));

                        // Display a Toast or some notification to the user
                        Toast.makeText(this, "No valid positions to place tile. Next turn will start shortly.", Toast.LENGTH_SHORT).show();

                        // Handler to add a delay before the next turn starts
                        new Handler(Looper.getMainLooper()).postDelayed(this::confirmNextTurnToStart, 3000); // 3000 milliseconds == 3 seconds
                    } else {
                        previewTileToPlace.setRotation(0);
                        gameboardAdapter.setCanPlaceTile(true);
                        gameboardAdapter.setYourTurn(true);
                        gameboardAdapter.setTileToPlace(tileToPlace);
                        previewTileToPlace.setImageResource(
                                getResources().getIdentifier(tileToPlace.getImageName() + "_0", DRAWABLE, getPackageName()));
                        binding.buttonConfirmTilePlacement.setVisibility(View.VISIBLE);
                        binding.buttonRotateClockwise.setVisibility(View.VISIBLE);
                        binding.buttonRotateCounterClockwise.setVisibility(View.VISIBLE);
                        binding.previewTileToPlace.setVisibility(View.VISIBLE);
                        binding.backgroundRight.setVisibility(View.VISIBLE);

                        moveButtonsLeft();
                    }
                } else {
                    tileToPlace = null;
                    gameboardAdapter.setYourTurn(false);
                    gameboardAdapter.setCanPlaceTile(false);
                    previewTileToPlace.setRotation(0);
                    previewTileToPlace.setImageResource(R.drawable.backside);
                    buttonConfirm.setVisibility(View.GONE);
                    binding.buttonRotateClockwise.setVisibility(View.GONE);
                    binding.buttonRotateCounterClockwise.setVisibility(View.GONE);
                    binding.previewTileToPlace.setVisibility(View.GONE);
                    binding.backgroundRight.setVisibility(View.GONE);

                    moveButtonsRight();

                }
                if (gameboardAdapter != null) {
                    gameboardAdapter.notifyDataSetChanged();
                }
            }
        });

        gameSessionRepository.subscribeToForwardedScoreboard(currentPlayer.getGameSessionId());

        /*
         * game ended observable, forwarding the scoreboard
         */
        gameSessionViewModel.gameEndedLiveData().observe(this, gameEnded -> {
            assert currentLobbyAdmin != null;
            if (gameEnded && currentLobbyAdmin.equals(currentPlayer.getId().toString())) {
                HashMap<Long, String> playerIdsWithNames = new HashMap<>();
                List<Long> topThreePlayers = gameBoard.getTopThreePlayers();

                // Assuming you have a method to get player IDs from their names
                for (Long playerId : topThreePlayers) {
                    playerIdsWithNames.put(playerId, null);
                }

                Scoreboard scoreboardDto = new Scoreboard(
                        currentPlayer.getGameSessionId(),
                        currentPlayer.getGameLobbyId(),
                        playerIdsWithNames
                );
                gameSessionViewModel.sendScoreboardRequest(scoreboardDto);
            }
        });


        /*
         * scoreboard observable, go to end-game (winners) screen
         */
        gameSessionViewModel.scoreboardLiveData().observe(this, scoreboard -> {
            if (scoreboard != null) {
                Intent gameEndIntent = new Intent(this, GameEndActivity.class);

            List<String> topThree = gameBoard.sortTopThreePlayersAfterForwarding(scoreboard);

            for (int i = 0; i < topThree.size(); i++) {
                gameEndIntent.putExtra("PLAYER" + i, topThree.get(i));
            }

                startActivity(gameEndIntent);
            }
        });


        if (currentPlayer.getId().toString().equals(currentLobbyAdmin)) {
            gameSessionViewModel.getNextTurn(currentPlayer.getGameSessionId());
        }

        // GameLogic
        confirmTilePlacement();
        confirmMeeplePlacement();
        setupRotationButtons();

        JoystickView joystick = (JoystickView) findViewById(R.id.joystickView);
        joystick.setOnMoveListener((angle, strength) -> {
//            TODO: Future adjust strength based on the scale of the gridView
//               Move across the gridView
            // Convert angle to radians
            double rad = Math.toRadians(angle);

            // Calculate change in X and Y coordinates
            float deltaX = (float) (-strength * Math.cos(rad)); // Negate this to reverse the direction
            float deltaY = (float) (strength * Math.sin(rad));

            // Check if gridView is not null
            if (gridView != null) {
                // Get current translations
                float currentTranslationX = gridView.getTranslationX();
                float currentTranslationY = gridView.getTranslationY();

                // Update translations
                float newX = currentTranslationX + deltaX;
                float newY = currentTranslationY + deltaY;
                gridView.setTranslationX(newX);
                gridView.setTranslationY(newY);
            }
        });

        // Navigating across the game board with buttons
        moveRight();
        moveLeft();
        moveUp();
        moveDown();

        // Zooming In and Out of the game board
        zoomIn();
        zoomOut();

        binding.tvMeepleCount.setText(gameboardAdapter.getMeepleCount() + "x");


        // Show the current scoreboard in a dialog
        Button showScoreboardButton = findViewById(R.id.button_show_scoreboard);
        showScoreboardButton.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(GameBoardActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.scoreboard_dialog, null);
            builder.setView(view);

            // RecyclerView in dialog:
            RecyclerView scoreboardList = view.findViewById(R.id.scoreboard_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            scoreboardList.setLayoutManager(layoutManager); // Set the layout manager
            scoreboardList.setAdapter(scoreboardAdapter);


            // Create the AlertDialog instance
            AlertDialog dialog = builder.create();

            // Hide cheat button if player cannot cheat
            Button cheatButton = view.findViewById(R.id.button_cheat);
            if(Boolean.TRUE.equals(GameSessionRepository.getInstance().getICanCheat().getValue()) && !hasCheated) {
                cheatButton.setVisibility(View.VISIBLE);
                PlayerRepository.getInstance().getCurrentPlayer().setCanCheat(true);
            } else {
                cheatButton.setVisibility(View.GONE);
            }



            cheatButton.setOnClickListener(v2 -> {

                Map<Long, Integer> idsPoints = new HashMap<>();
                for (Long id: gameBoard.getPlayerWithPoints().keySet()) {
                    idsPoints.put(id, 0);
                }
                finishedTurnDto = new FinishedTurnDto(currentPlayer.getGameSessionId(), idsPoints, null);

                gameSessionViewModel.sendCheatRequest(PlayerRepository.getInstance().getCurrentPlayer().getId(), finishedTurnDto);
                cheatButton.setVisibility(View.GONE);
                PlayerRepository.getInstance().getCurrentPlayer().setCanCheat(false);
                hasCheated = true;
            });

            // Button in dialog to close it
            Button closeButton = view.findViewById(R.id.button_close_scoreboard);
            closeButton.setOnClickListener(v1 ->
                // Dismiss the dialog when the close button is clicked
                dialog.dismiss()
            );

            dialog.show();
        });

        //
        gameSessionViewModel.getCheaterFound().observe(this, cheaterFound -> {
            if (Boolean.TRUE.equals(cheaterFound)) {
                // display toast that the cheater was found
                Toast.makeText(this, "Cheater found!", Toast.LENGTH_SHORT).show();
                scoreboardAdapter.notifyDataSetChanged(); // set visibility of accuse buttons to GONE in the scoreboard adapter
            }
        });

        // Handle back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Display a toast instead of calling super.handleOnBackPressed()
                Toast.makeText(GameBoardActivity.this, "leave the game via red X button instead", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePlayerPoints() {
        Integer points = gameBoard.getPlayerWithPoints().get(currentPlayer.getId());
        if (points == null) {
            points = 0;  // Assume 0 points if none are found
        }
        // Note: commented out, looks nicer since we have a scoreboard now
        //binding.tvPlayerPoints.setText(String.valueOf(points));
    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop");
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();

        currentPlayer.setPoints(0);

        gameSessionViewModel.gameEndedLiveData().postValue(false);
        gameSessionViewModel.scoreboardLiveData().postValue(null);
        gameSessionViewModel.getNextTurnMessageLiveData().postValue(null);
        gameSessionViewModel.getPlacedTileLiveData().postValue(null);
        gameSessionViewModel.finishedTurnLiveData().postValue(null);
        gameSessionViewModel.getICanCheat().postValue(null);
        gameSessionViewModel.getCheaterFound().postValue(false);
    }

    private void moveButtonsRight() {
        ConstraintLayout constraintLayout = binding.main;
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(zoomInBtn.getId(), ConstraintSet.END, binding.main.getId(), ConstraintSet.END, 0);
        constraintSet.connect(zoomOutBtn.getId(), ConstraintSet.END, binding.main.getId(), ConstraintSet.END, 0);
        constraintSet.applyTo(constraintLayout);
    }

    private void moveButtonsLeft() {
        ConstraintLayout constraintLayout = binding.main;
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(zoomInBtn.getId(), ConstraintSet.END, binding.backgroundRight.getId(), ConstraintSet.START, 0);
        constraintSet.connect(zoomOutBtn.getId(), ConstraintSet.END, binding.backgroundRight.getId(), ConstraintSet.START, 0);
        constraintSet.applyTo(constraintLayout);
    }


    private void confirmNextTurnToStart() {
        gameSessionViewModel.getNextTurn(currentPlayer.getGameSessionId());
        gameboardAdapter.setYourTurn(false);
    }

    private void bindGameBoardUiElements() {
        // Init Grid View
        gridView = binding.gridview;

        // Init Movement Buttons
        buttonLeft = binding.leftScrlBtn;
        buttonRight = binding.rightScrlBtn;
        buttonDown = binding.downScrlBtn;
        buttonUp = binding.upScrlBtn;

        // Init Zoom Buttons
        zoomInBtn = binding.btnZoomIn;
        zoomOutBtn = binding.btnZoomOut;

        // Init Control Elements
        buttonConfirm = binding.buttonConfirmTilePlacement;
        previewTileToPlace = binding.previewTileToPlace;
    }

    private void confirmTilePlacement() {
        buttonConfirm.setOnClickListener(v -> {
            if (gameboardAdapter.getToPlaceCoordinates() != null && gameboardAdapter.isYourTurn() && gameboardAdapter.isCanPlaceTile()) {
                v.startAnimation(scaleAnimation);
                // get the x and y coordinates of the field where the tile should be placed
                int xToPlace = gameboardAdapter.getToPlaceCoordinates().getXPosition();
                int yToPlace = gameboardAdapter.getToPlaceCoordinates().getYPosition();

                tileToPlace.setCoordinates(gameboardAdapter.getToPlaceCoordinates());

                // place the Tile on the gameBoard
                PlacedTileDto placedTileDto = new PlacedTileDto(currentPlayer.getGameSessionId(), tileToPlace.getId(), new Coordinates(xToPlace, yToPlace), tileToPlace.getRotation(), null);
                gameSessionViewModel.sendPlacedTile(placedTileDto);

                buttonConfirm.setVisibility(View.GONE);
                binding.buttonRotateClockwise.setVisibility(View.GONE);
                binding.buttonRotateCounterClockwise.setVisibility(View.GONE);


                gameboardAdapter.setCanPlaceTile(false);
                gameboardAdapter.setCanPlaceMeeple(true);
                gameboardAdapter.notifyDataSetChanged();
                showMeepleGrid();

            } else {
                if (!gameboardAdapter.isYourTurn()) {
                    Toast.makeText(this, "It's not your turn. Please wait.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Please select a valid position", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void confirmMeeplePlacement() {
        binding.buttonConfirmMeeplePlacement.setOnClickListener(v -> {
            if (gameboardAdapter.isYourTurn() && gameboardAdapter.getMeepleCount() > 0) {
                if (meepleAdapter.getPlaceMeepleCoordinates() != null) {
                    Meeple placedMeeple = new Meeple();
                    placedMeeple.setColor(currentPlayer.getPlayerColour());
                    placedMeeple.setPlayerId(currentPlayer.getId());
                    placedMeeple.setPlaced(true);
                    placedMeeple.setCoordinates(meepleAdapter.getPlaceMeepleCoordinates());

                    tileToPlace.setPlacedMeeple(placedMeeple);

                    gameboardAdapter.setMeepleCount(gameboardAdapter.getMeepleCount() - 1);
                    String formattedString = String.format(getString(R.string.meepleCount), gameboardAdapter.getMeepleCount());
                    binding.tvMeepleCount.setText(formattedString);

                    int xToPlace = gameboardAdapter.getToPlaceCoordinates().getXPosition();
                    int yToPlace = gameboardAdapter.getToPlaceCoordinates().getYPosition();
                    PlacedTileDto placedTileDto = new PlacedTileDto(currentPlayer.getGameSessionId(), tileToPlace.getId(), new Coordinates(xToPlace, yToPlace), tileToPlace.getRotation(), placedMeeple);
                    gameSessionViewModel.sendPlacedTile(placedTileDto);

                }

                gameboardAdapter.setCanPlaceMeeple(false);
                gameboardAdapter.notifyDataSetChanged();

                binding.buttonConfirmMeeplePlacement.setVisibility(View.GONE);

                hideMeepleGrid();
                gameboardAdapter.setToPlaceCoordinates(null);


                calculatePointsForCurrentTurn();
                confirmNextTurnToStart();
            }
        });
    }

    private void showMeepleGrid() {
        if (gameboardAdapter.getMeepleCount() > 0) {
            binding.overlayGridview.setVisibility(View.VISIBLE);
            RoadResult roadResult = roadCalculator.getAllTilesThatArePartOfRoad(tileToPlace);
            meepleAdapter = new MeepleAdapter(this, tileToPlace, !roadResult.hasMeepleOnRoad());
            binding.overlayGridview.setAdapter(meepleAdapter);
            binding.buttonConfirmMeeplePlacement.setVisibility(View.VISIBLE);
        } else {
            gameboardAdapter.setCanPlaceMeeple(false);
            gameboardAdapter.setToPlaceCoordinates(null);
            hideMeepleGrid();
            calculatePointsForCurrentTurn();
            confirmNextTurnToStart();
            gameboardAdapter.notifyDataSetChanged();
        }
    }

    private void calculatePointsForCurrentTurn() {
        if (tileToPlace == null) return;

        // Calculate the potential changes resulting from placing this tile
        RoadResult roadResult = roadCalculator.getAllTilesThatArePartOfRoad(tileToPlace);



        if (roadResult.isRoadCompleted()) {

            // Create the FinishedTurnDto with the results of the point calculation
            finishedTurnDto = new FinishedTurnDto(
                    currentPlayer.getGameSessionId(),
                    roadResult.getPoints(),
                    roadResult.getPlayersWithMeeplesOnRoad()
            );

            // Send the FinishedTurnDto through the WebSocket to handle the results server-side
            gameSessionViewModel.sendPointsForCompletedRoad(finishedTurnDto);
        }
    }

    private void hideMeepleGrid() {
        binding.overlayGridview.setVisibility(View.GONE);
    }

    private void zoomOut() {
        zoomOutBtn.setOnClickListener(v -> {
            v.startAnimation(scaleAnimation);
            float currentScaleX = gridView.getScaleX();
            float currentScaleY = gridView.getScaleY();
            if (gridView != null && currentScaleX > 1.0f && currentScaleY > 1.0f) {
                float newScaleX = currentScaleX - 0.5f;
                float newScaleY = currentScaleY - 0.5f;
                gridView.setScaleX(newScaleX);
                gridView.setScaleY(newScaleY);
            }
        });
    }

    private void zoomIn() {
        zoomInBtn.setOnClickListener(v -> {
            v.startAnimation(scaleAnimation);
            float currentScaleX = gridView.getScaleX();
            float currentScaleY = gridView.getScaleY();
            if (gridView != null && currentScaleX < 5.0f && currentScaleY < 5.0f) {
                float newScaleX = currentScaleX + 0.5f;
                float newScaleY = currentScaleY + 0.5f;
                gridView.setScaleX(newScaleX);
                gridView.setScaleY(newScaleY);
            }
        });
    }


    private void moveDown() {
        buttonDown.setOnClickListener(v -> {
            if (gridView != null) {
                v.startAnimation(scaleAnimation);
                float currentTranslationY = gridView.getTranslationY();
                float newY = currentTranslationY - 300;
                gridView.setTranslationY(newY);
            }
        });
    }

    private void moveUp() {
        buttonUp.setOnClickListener(v -> {
            if (gridView != null) {
                v.startAnimation(scaleAnimation);
                float currentTranslationY = gridView.getTranslationY();
                float newY = currentTranslationY + 300;
                gridView.setTranslationY(newY);
            }
        });
    }

    private void moveLeft() {
        buttonLeft.setOnClickListener(v -> {
            if (gridView != null) {
                v.startAnimation(scaleAnimation);
                float currentTranslationX = gridView.getTranslationX();
                float newX = currentTranslationX + 300;
                gridView.setTranslationX(newX);
            }
        });
    }

    private void moveRight() {
        buttonRight.setOnClickListener(v -> {
            if (gridView != null) {
                v.startAnimation(scaleAnimation);
                float currentTranslationX = gridView.getTranslationX();
                float newX = currentTranslationX - 300;
                gridView.setTranslationX(newX);
            }
        });
    }

    private void setupRotationButtons() {
        final ImageRotator imageRotator = new ImageRotator(previewTileToPlace);
        binding.buttonRotateClockwise.setOnClickListener(v -> {
            if (gameboardAdapter.isYourTurn() && gameboardAdapter.isCanPlaceTile()) {
                v.startAnimation(scaleAnimation);
                imageRotator.rotateRight();
                tileToPlace.rotate(true);
                gameboardAdapter.setCurrentTileRotation(tileToPlace);
            }
        });

        binding.buttonRotateCounterClockwise.setOnClickListener(v -> {
            if (gameboardAdapter.isYourTurn() && gameboardAdapter.isCanPlaceTile()) {
                v.startAnimation(scaleAnimation);
                imageRotator.rotateLeft();
                tileToPlace.rotate(false);
                gameboardAdapter.setCurrentTileRotation(tileToPlace);
            }
        });
    }
}
