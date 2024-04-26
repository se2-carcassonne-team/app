package se2.carcassonne.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import se2.carcassonne.R;
import se2.carcassonne.databinding.GameboardActivityBinding;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.model.Tile;


public class GameBoardActivity extends AppCompatActivity {
    GameboardActivityBinding binding;
    private static final int ROWS = 30;
    private static final int COLS = 30;

    private GridView gridView;
    private GameboardAdapter gameboardAdapter;
    private Tile tile;
    private ImageView gameplay_card;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameboard_activity);
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        gridView = findViewById(R.id.gridview);
        gridView.setScaleX(3.5f);
        gridView.setScaleY(3.5f);
        gameboardAdapter = new GameboardAdapter(this, ROWS, COLS);
        gridView.setAdapter(gameboardAdapter);



        setupRotationButtons();
        tile = new Tile(0L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1});

        // ZoomFunction zoomFunction = new ZoomFunction(this, gridView);
        // gridView.setOnTouchListener(zoomFunction);

    }


    private void setupRotationButtons() {
        ImageView playingCard = findViewById(R.id.gameplay_card);
        final ImageRotator imageRotator = new ImageRotator(playingCard);

        findViewById(R.id.right_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageRotator.rotateRight();
                tile.rotate(true);
            }
        });

        findViewById(R.id.left_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageRotator.rotateLeft();
                tile.rotate(false);
            }
        });
    }
}

