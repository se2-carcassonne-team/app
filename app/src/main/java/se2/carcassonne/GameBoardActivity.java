package se2.carcassonne;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;

public class GameBoardActivity extends AppCompatActivity {

    private GridView gridView;
    private GameboardAdapter gameboardAdapter;

    // Größe der Matrix (10x10)
    private static final int ROWS = 5;
    private static final int COLS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameboard_activity);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        gridView = findViewById(R.id.gridview);
        gameboardAdapter = new GameboardAdapter(this, ROWS, COLS);
        gridView.setAdapter(gameboardAdapter);


    }
}

