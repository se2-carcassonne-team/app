package se2.carcassonne.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import se2.carcassonne.R;
import se2.carcassonne.databinding.GameboardActivityBinding;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.model.Coordinates;
import se2.carcassonne.model.GameBoard;
import se2.carcassonne.model.Tile;

public class GameBoardActivity extends AppCompatActivity {
    GameboardActivityBinding binding;
    private GridView gridView;
    private GameboardAdapter gameboardAdapter;
    private Tile tileToPlace;
    private Button buttonLeft;
    private Button buttonRight;
    private Button buttonUp;
    private Button buttonDown;
    private Button buttonConfirm;
    private Button buttonGetCard;
    private GameBoard gameBoard;
    private ImageView previewTileToPlace;

    // remove later, only here for testing
    private static int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GameboardActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);


        gameBoard = new GameBoard();
        gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(13, 12));


        gridView = binding.gridview;
        gridView.setScaleX(3.5f);
        gridView.setScaleY(3.5f);
        gridView.setStretchMode(GridView.NO_STRETCH);

        tileToPlace = gameBoard.getAllTiles().get(3);
        gameboardAdapter = new GameboardAdapter(this, gameBoard, tileToPlace);
        gridView.setAdapter(gameboardAdapter);

        buttonLeft = binding.leftScrlBtn;
        buttonRight = binding.rightScrlBtn;
        buttonDown = binding.downScrlBtn;
        buttonUp = binding.upScrlBtn;

        buttonConfirm = binding.buttonConfirmTilePlacement;
        buttonGetCard = binding.buttonGetNextCard;

        previewTileToPlace = binding.previewTileToPlace;
        setupRotationButtons();
        previewTileToPlace.setImageResource(
                getResources().getIdentifier(tileToPlace.getImageName()+"_"+ tileToPlace.getRotation(), "drawable", getPackageName()));

        final Button closeGame = binding.button3;
        closeGame.setOnClickListener(v -> {
            Intent intent = new Intent(GameBoardActivity.this, StartupActivity.class);
            startActivity(intent);
        });

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
            // TODO : Check if it's actually my turn when placing tile
            if (gameboardAdapter.getToPlaceCoordinates() != null && gameboardAdapter.isYourTurn()){

                // get the x and y coordinates of the field where the tile should be placed
                int xToPlace = gameboardAdapter.getToPlaceCoordinates().getXPosition();
                int yToPlace = gameboardAdapter.getToPlaceCoordinates().getYPosition();

                // place the Tile on the gameBoard
                gameBoard.placeTile(tileToPlace, new Coordinates(xToPlace, yToPlace));

                // end tile-placement for this turn & reset values
//                gameboardAdapter.setTileToPlace(null);
//                this.tileToPlace = null;

                gameboardAdapter.setYourTurn(false);
                previewTileToPlace.setImageResource(R.drawable.backside);
                gameboardAdapter.notifyDataSetChanged();

                //gameboardAdapter.getGameBoard().getPlaceablePositions();
            } else {
                Toast.makeText(this, "Please select a valid position", Toast.LENGTH_SHORT).show();
            }
        });

        buttonGetCard.setOnClickListener(v -> {
            if (!gameboardAdapter.isYourTurn()){
                gameboardAdapter.setYourTurn(true);
                counter += 2;
                Tile nextTile = gameBoard.getAllTiles().get(counter);
                gameboardAdapter.setTileToPlace(nextTile);
                this.tileToPlace = nextTile;

                previewTileToPlace.setImageResource(getResources().getIdentifier(tileToPlace.getImageName()+"_0", "drawable", getPackageName()));
                previewTileToPlace.setRotation(0);
                gameboardAdapter.notifyDataSetChanged();

                Log.e("placed", gameBoard.getPlacedTiles().toString());
                Log.e("placeable", gameBoard.getPlaceablePositions().toString());
                Log.e("highlighted", gameBoard.highlightValidPositions(tileToPlace).toString());
            }
        });

        gameboardAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        gameboardAdapter.setCurrentTileRotation(tileToPlace);
    }

    private void setupRotationButtons() {
        //ImageView playingCard = binding.previewTileToPlace;
        final ImageRotator imageRotator = new ImageRotator(previewTileToPlace);

        binding.buttonRotateClockwise.setOnClickListener(v -> {
            if (gameboardAdapter.isYourTurn()) {
                imageRotator.rotateRight();
                tileToPlace.rotate(true);
                gameboardAdapter.setCurrentTileRotation(tileToPlace);
                Log.e("placed", gameBoard.getPlacedTiles().toString());
                Log.e("placeable", gameBoard.getPlaceablePositions().toString());
                Log.e("highlighted", gameBoard.highlightValidPositions(tileToPlace).toString());
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
