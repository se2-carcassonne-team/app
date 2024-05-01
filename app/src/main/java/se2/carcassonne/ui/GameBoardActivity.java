package se2.carcassonne.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

import se2.carcassonne.R;
import se2.carcassonne.databinding.GameboardActivityBinding;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.model.Coordinates;
import se2.carcassonne.model.GameBoard;
import se2.carcassonne.model.Lobby;
import se2.carcassonne.model.NextTurn;
import se2.carcassonne.model.Player;
import se2.carcassonne.model.Tile;
import se2.carcassonne.repository.PlayerRepository;
import se2.carcassonne.viewmodel.GameBoardActivityViewModel;

public class GameBoardActivity extends AppCompatActivity {
    GameboardActivityBinding binding;
    ObjectMapper objectMapper;
    Player currentPlayer;
    private GridView gridView;
    private GameboardAdapter gameboardAdapter;
    private Tile tileToPlace;
    private Button buttonLeft;
    private Button buttonRight;
    private Button buttonUp;
    private Button buttonDown;
    private Button buttonConfirm;
    private Button buttonNextTurn;
    private GameBoard gameBoard;
    private ImageView previewTileToPlace;
    // remove later, only here for testing
    private static int counter = 0;
    private GameBoardActivityViewModel gameBoardActivityViewModel;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GameboardActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        objectMapper = new ObjectMapper();
        currentPlayer = PlayerRepository.getInstance().getCurrentPlayer();

//        Create a new game board and place a random tile on it
        gameBoard = new GameBoard();

//        Set up the grid view
        gridView = binding.gridview;
        gridView.setScaleX(3.5f);
        gridView.setScaleY(3.5f);
        gridView.setStretchMode(GridView.NO_STRETCH);

//        Set up the playing card
        previewTileToPlace = binding.previewTileToPlace;

//        Instantiate gameBoardActivityViewModel
        gameBoardActivityViewModel = new GameBoardActivityViewModel();

//        Get the next turn message from the previous activity
        intent = getIntent();
        String currentLobbyAdmin = intent.getStringExtra("LOBBY_ADMIN_ID");

        gameboardAdapter = new GameboardAdapter(this, gameBoard, tileToPlace);
        gridView.setAdapter(gameboardAdapter);

        gameBoardActivityViewModel.getNextTurnMessageLiveData().observe(this, nextTurn -> {
            NextTurn nextTurnObj = null;
            try {
                nextTurnObj = objectMapper.readValue(nextTurn, NextTurn.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            if (Objects.equals(nextTurnObj.getPlayerId(), currentPlayer.getId())) {
                tileToPlace = gameBoard.getAllTiles().get(Math.toIntExact(nextTurnObj.getTileId()));
                previewTileToPlace.setRotation(0);
                gameboardAdapter.setYourTurn(true);
                gameboardAdapter.setTileToPlace(tileToPlace);
                previewTileToPlace.setImageResource(
                        getResources().getIdentifier(tileToPlace.getImageName() + "_0", "drawable", getPackageName()));
                if (gameboardAdapter != null) {
                    gameboardAdapter.notifyDataSetChanged();
                }
            } else {
                tileToPlace = null;
                gameboardAdapter.setYourTurn(false);
                previewTileToPlace.setRotation(0);
                previewTileToPlace.setImageResource(R.drawable.backside);
                if (gameboardAdapter != null) {
                    gameboardAdapter.notifyDataSetChanged();
                }
            }
        });

        if (currentPlayer.getId().toString().equals(currentLobbyAdmin)) {
            gameBoardActivityViewModel.getNextTurn(currentPlayer.getGameSessionId());
        }

        buttonNextTurn = binding.btnNextTurn;
        buttonNextTurn.setOnClickListener(v -> {
            if (gameboardAdapter.isYourTurn()){
                gameBoardActivityViewModel.getNextTurn(currentPlayer.getGameSessionId());
            }
        });

        buttonLeft = binding.leftScrlBtn;
        buttonRight = binding.rightScrlBtn;
        buttonDown = binding.downScrlBtn;
        buttonUp = binding.upScrlBtn;

        buttonConfirm = binding.buttonConfirmTilePlacement;

        setupRotationButtons();

        buttonRight.setOnClickListener(v -> {
            if (gridView != null) {
                float currentTranslationX = gridView.getTranslationX();
                float newX = currentTranslationX - 300;
                gridView.setTranslationX(newX);
            }
        });

        buttonLeft.setOnClickListener(v -> {
            if (gridView != null) {
                float currentTranslationX = gridView.getTranslationX();
                float newX = currentTranslationX + 300;
                gridView.setTranslationX(newX);
            }
        });

        buttonUp.setOnClickListener(v -> {
            if (gridView != null) {
                float currentTranslationY = gridView.getTranslationY();
                float newY = currentTranslationY + 300;
                gridView.setTranslationY(newY);
            }
        });

        buttonDown.setOnClickListener(v -> {
            if (gridView != null) {
                float currentTranslationY = gridView.getTranslationY();
                float newY = currentTranslationY - 300;
                gridView.setTranslationY(newY);
            }
        });

        buttonConfirm.setOnClickListener(v -> {
            if (gameboardAdapter.getToPlaceCoordinates() != null && gameboardAdapter.isYourTurn()) {

                // get the x and y coordinates of the field where the tile should be placed
                int xToPlace = gameboardAdapter.getToPlaceCoordinates().getXPosition();
                int yToPlace = gameboardAdapter.getToPlaceCoordinates().getYPosition();

                // place the Tile on the gameBoard
                gameBoard.placeTile(tileToPlace, new Coordinates(xToPlace, yToPlace));

                gameboardAdapter.setYourTurn(false);
                previewTileToPlace.setImageResource(R.drawable.backside);
                gameboardAdapter.notifyDataSetChanged();

                //TODO : Check for impl
                //gameBoardActivityViewModel.placeTile(currentPlayer.getGameSessionId(), tileToPlace.getId(), xToPlace, yToPlace);
            } else {
                Toast.makeText(this, "Please select a valid position", Toast.LENGTH_SHORT).show();
            }
        });

        gameboardAdapter.notifyDataSetChanged();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        gameboardAdapter.setCurrentTileRotation(tileToPlace);
//    }

    private void setupRotationButtons() {
        //ImageView playingCard = binding.previewTileToPlace;
        final ImageRotator imageRotator = new ImageRotator(previewTileToPlace);

        binding.buttonRotateClockwise.setOnClickListener(v -> {
            if (gameboardAdapter.isYourTurn()) {
                imageRotator.rotateRight();
                tileToPlace.rotate(true);
                gameboardAdapter.setCurrentTileRotation(tileToPlace);
            }
        });

        binding.buttonRotateCounterClockwise.setOnClickListener(v -> {
            if (gameboardAdapter.isYourTurn()) {
                imageRotator.rotateLeft();
                tileToPlace.rotate(false);
                gameboardAdapter.setCurrentTileRotation(tileToPlace);
            }
        });
    }
}
