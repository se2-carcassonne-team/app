package se2.carcassonne.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import se2.carcassonne.databinding.GameboardActivityBinding;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.model.Coordinates;
import se2.carcassonne.model.GameBoard;
import se2.carcassonne.model.Tile;

public class GameBoardActivity extends AppCompatActivity {
    GameboardActivityBinding binding;
    private GridView gridView;
    private GameboardAdapter gameboardAdapter;
    private Tile tile;
    private Button buttonleft;
    private Button buttonright;
    private Button buttonup;
    private Button buttondown;
    private Button buttonConfirm;
    private GameBoard gameBoard;
    private ImageView playingCard;

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

        tile = gameBoard.getAllTiles().get(3);
        gameboardAdapter = new GameboardAdapter(this, gameBoard, tile);
        gridView.setAdapter(gameboardAdapter);

        buttonleft = binding.leftScrlBtn;
        buttonright = binding.rightScrlBtn;
        buttondown = binding.downScrlBtn;
        buttonup = binding.upScrlBtn;

        buttonConfirm = binding.buttonConfirmTilePlacement;

        playingCard = binding.gameplayCard;
        setupRotationButtons();
        playingCard.setImageResource(
                getResources().getIdentifier(tile.getImageName()+"_"+tile.getRotation(), "drawable", getPackageName()));

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
