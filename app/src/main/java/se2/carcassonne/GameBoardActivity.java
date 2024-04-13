package se2.carcassonne;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class GameBoardActivity extends AppCompatActivity {

    private GridView gridView;
    private GameboardAdapter gameboardAdapter;


    private static final int ROWS = 30;
    private static final int COLS = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameboard_activity);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        gridView = findViewById(R.id.gridview);
        gameboardAdapter = new GameboardAdapter(this, ROWS, COLS);
        gridView.setAdapter(gameboardAdapter);
        Button rotateRightButton = findViewById(R.id.button4);
        Button rotateLeftButton = findViewById(R.id.button5);

        ZoomFunction zoomfunktion = new ZoomFunction(this);
        gridView.setOnTouchListener(zoomfunktion);


        rotateRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView playingCard = findViewById(R.id.imageView3);
                ImageRotator imageRotator = new ImageRotator(playingCard);
                imageRotator.rotateRight();
            }
        });


        rotateRightButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView playingCard = findViewById(R.id.imageView3);
                ImageRotator imageRotator = new ImageRotator(playingCard);
                imageRotator.rotateRight();
            }
        }));

        rotateLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView playingCard = findViewById(R.id.imageView3);
                ImageRotator imageRotator = new ImageRotator(playingCard);
                imageRotator.rotateLeft();
            }
        });





    }
}
