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

    ObjectMapper objectMapper = new ObjectMapper();

    Player currentPlayer = PlayerRepository.getInstance().getCurrentPlayer();
    Long nextTileId;
    private GridView gridView;
    private GameboardAdapter gameboardAdapter;
    private Tile tile;
    private Button buttonleft;
    private Button buttonright;
    private Button buttonup;
    private Button buttondown;
    private Button buttonConfirm;
    private Button buttonNextTurn;
    private GameBoard gameBoard;
    private ImageView playingCard;
    private GameBoardActivityViewModel gameBoardActivityViewModel;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GameboardActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

//        Create a new game board and place a random tile on it
        gameBoard = new GameBoard();
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(13, 12));

//        Set up the grid view
        gridView = binding.gridview;
        gridView.setScaleX(3.5f);
        gridView.setScaleY(3.5f);
        gridView.setStretchMode(GridView.NO_STRETCH);
//        Set up the playing card
        playingCard = binding.gameplayCard;

//        Instantiate gameBoardActivityViewModel
        gameBoardActivityViewModel = new GameBoardActivityViewModel();
//        Get the next turn message from the previous activity
        intent = getIntent();
        String currentLobbyAdmin = intent.getStringExtra("LOBBY_ADMIN_ID");

        gameBoardActivityViewModel.getNextTurnMessageLiveData().observe(this, nextTurn -> {
            NextTurn nextTurnObj = null;
            try {
                nextTurnObj = objectMapper.readValue(nextTurn, NextTurn.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            nextTileId = nextTurnObj.getTileId();
            tile = gameBoard.getAllTiles().get(Math.toIntExact(nextTileId));
            playingCard.setImageResource(
                    getResources().getIdentifier(tile.getImageName() + "_" + tile.getRotation(), "drawable", getPackageName()));
        });

        if (currentPlayer.getId().toString().equals(currentLobbyAdmin)) {
            gameBoardActivityViewModel.getNextTurn(currentPlayer.getGameSessionId());
        }

        buttonNextTurn = binding.btnNextTurn;
        buttonNextTurn.setOnClickListener(v -> gameBoardActivityViewModel.getNextTurn(currentPlayer.getGameSessionId()));

        gameboardAdapter = new GameboardAdapter(this, gameBoard, tile);
        gridView.setAdapter(gameboardAdapter);

        buttonleft = binding.leftScrlBtn;
        buttonright = binding.rightScrlBtn;
        buttondown = binding.downScrlBtn;
        buttonup = binding.upScrlBtn;

        buttonConfirm = binding.buttonConfirmTilePlacement;

        setupRotationButtons();


        final Button closeGame = binding.button3;
        closeGame.setOnClickListener(v -> {
            Intent intent = new Intent(GameBoardActivity.this, StartupActivity.class);
            startActivity(intent);
        });

        buttonright.setOnClickListener(v -> {
            if (gridView != null) {
                float currentTranslationX = gridView.getTranslationX();
                float newX = currentTranslationX - 300;
                gridView.setTranslationX(newX);
            }
        });

        buttonleft.setOnClickListener(v -> {
            if (gridView != null) {
                float currentTranslationX = gridView.getTranslationX();
                float newX = currentTranslationX + 300;
                gridView.setTranslationX(newX);
            }
        });

        buttonup.setOnClickListener(v -> {
            if (gridView != null) {
                float currentTranslationY = gridView.getTranslationY();
                float newY = currentTranslationY + 300;
                gridView.setTranslationY(newY);
            }
        });

        buttondown.setOnClickListener(v -> {
            if (gridView != null) {
                float currentTranslationY = gridView.getTranslationY();
                float newY = currentTranslationY - 300;
                gridView.setTranslationY(newY);
            }
        });

        buttonConfirm.setOnClickListener(v -> {
            // TODO : Check if it's actually my turn when placing tile
            if (gameboardAdapter.getToPlaceCoordinates() != null) {
                int xToPlace = gameboardAdapter.getToPlaceCoordinates().getXPosition();
                int yToPlace = gameboardAdapter.getToPlaceCoordinates().getYPosition();
                gameBoard.placeTile(tile, new Coordinates(xToPlace, yToPlace));
            } else {
                Toast.makeText(this, "Please select a valid position", Toast.LENGTH_SHORT).show();
            }
        });
        gameboardAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        gameboardAdapter.setCurrentTileRotation(tile);
    }

    private void setupRotationButtons() {
        ImageView playingCard = binding.gameplayCard;
        final ImageRotator imageRotator = new ImageRotator(playingCard);

        binding.rightButton.setOnClickListener(v -> {
            imageRotator.rotateRight();
            tile.rotate(true);
            gameboardAdapter.setCurrentTileRotation(tile);
        });

        binding.leftButton.setOnClickListener(v -> {
            imageRotator.rotateLeft();
            tile.rotate(false);
            gameboardAdapter.setCurrentTileRotation(tile);
        });
    }
}
