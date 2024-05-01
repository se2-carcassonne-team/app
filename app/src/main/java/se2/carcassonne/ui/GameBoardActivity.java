package se2.carcassonne.ui;

import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import se2.carcassonne.R;
import se2.carcassonne.databinding.GameboardActivityBinding;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.model.Coordinates;
import se2.carcassonne.model.GameBoard;
import se2.carcassonne.model.Tile;


public class GameBoardActivity extends AppCompatActivity {
    GameboardActivityBinding binding;
    private GameBoard gameBoard;
    private GridView gridView;
    private GameboardAdapter gameboardAdapter;
    private Tile tileToPlace;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GameboardActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        tileToPlace = new Tile(1L, "road_junction_large", new int[]{2, 2, 2, 2}, new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1});
        gameBoard = new GameBoard();

        gridView = binding.gridview;
        gridView.setScaleX(3.5f);
        gridView.setScaleY(3.5f);

        gameboardAdapter = new GameboardAdapter(this, gameBoard.getGameBoardMatrix().length,
                gameBoard.getGameBoardMatrix()[0].length);
        gridView.setAdapter(gameboardAdapter);

        binding.gameplayCard.setImageResource(R.drawable.road_junction_large_0);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            // Calculate the row and column based on the position
            int numColumns = gridView.getNumColumns();
            int x = position / numColumns;
            int y = position % numColumns;

            gameBoard.placeTile(tileToPlace, new Coordinates(x, y));
            gameboardAdapter.notifyDataSetChanged();
        });
        setupRotationButtons();
    }


    private void setupRotationButtons() {
        final ImageRotator imageRotator = new ImageRotator(binding.gameplayCard);

        binding.rightButton.setOnClickListener(v -> {
            imageRotator.rotateRight();
            tileToPlace.rotate(true);
        });

        binding.leftButton.setOnClickListener(v -> {
            imageRotator.rotateLeft();
            tileToPlace.rotate(false);
        });
    }
}

