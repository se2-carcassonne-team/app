package se2.carcassonne;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
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

        ZoomFunction zoomfunktion = new ZoomFunction(this);
        gridView.setOnTouchListener(zoomfunktion);


    }
}

