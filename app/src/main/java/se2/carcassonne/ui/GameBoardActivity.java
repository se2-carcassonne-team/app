package se2.carcassonne.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import se2.carcassonne.R;
import se2.carcassonne.model.Tile;


public class GameBoardActivity extends AppCompatActivity {

    private static final int ROWS = 30;
    private static final int COLS = 30;

    private GridView gridView;
    private GameboardAdapter gameboardAdapter;
    private Tile tile;
    private TextView textViewRoutineId;
    private ImageView gameplay_card;

    private void updateRotationIdTextView() {
        TextView textViewRoutineId = findViewById(R.id.textViewRoutineId);
        textViewRoutineId.setText("Routine ID: " + tile.getRotation());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameboard_activity);

        setupFullscreen();

        gridView = findViewById(R.id.gridview);
        gridView.setScaleX(3.5f);
        gridView.setScaleY(3.5f);
        gameboardAdapter = new GameboardAdapter(this, ROWS, COLS);
        gridView.setAdapter(gameboardAdapter);
        final Button closeGame = findViewById(R.id.button3);


        setupRotationButtons(gameplay_card);
        tile = new Tile(0L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1});
        updateRotationIdTextView();

        //ZoomFunction zoomfunktion = new ZoomFunction(this);
        //gridView.setOnTouchListener(zoomfunktion);
        ZoomFunction zoomFunction = new ZoomFunction(this, gridView);
        gridView.setOnTouchListener(zoomFunction);

        closeGame.setOnClickListener(v -> {

            //binding.button.setOnClickListener(v -> {
            Intent intent = new Intent(GameBoardActivity.this, StartupActivity.class);
            startActivity(intent);
        });


    }


    private void setupFullscreen() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void setupRotationButtons(ImageView imageView) {
        ImageView playingCard = findViewById(R.id.gameplay_card);
        final ImageRotator imageRotator = new ImageRotator(playingCard);

        findViewById(R.id.right_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageRotator.rotateRight();
                tile.rotate(true);
                updateRotationIdTextView();
            }
        });

        findViewById(R.id.left_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageRotator.rotateLeft();
                tile.rotate(false);
                updateRotationIdTextView();
            }
        });



    }
}

