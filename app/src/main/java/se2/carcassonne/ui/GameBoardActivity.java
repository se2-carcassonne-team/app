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
    static int rota=0;
    private Button buttonleft;
    private Button buttonright;
    private Button buttonup;
    private Button buttondown;

    private void updateRotationIdTextView() {
        TextView textViewRoutineId = findViewById(R.id.textViewRoutineId);
        textViewRoutineId.setText("Routine ID: " + tile.getRotation());
        rota=(tile.getRotation()) ;
    }

    static int getText()
    {

        return rota;
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
        gridView.setNestedScrollingEnabled(true);
        buttonleft = findViewById(R.id.left_scrl_btn);
        buttonright = findViewById(R.id.right_scrl_btn);
        buttondown = findViewById(R.id.down_scrl_btn);
        buttonup = findViewById(R.id.up_scrl_btn);



        setupRotationButtons(gameplay_card);
        tile = new Tile(0L,"castle_wall_road", new int[]{3,2,1,2}, new int[]{3,3,3, 2,2,2, 1,1,1});
        updateRotationIdTextView();



        closeGame.setOnClickListener(v -> {

            //binding.button.setOnClickListener(v -> {
            Intent intent = new Intent(GameBoardActivity.this, StartupActivity.class);
            startActivity(intent);
        });

        buttonright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gridView != null) {
                    float currentTranslationX = gridView.getTranslationX();
                    float newX = currentTranslationX - 50;
                    gridView.setTranslationX(newX);


                }

            }
        });
        buttonleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gridView != null) {
                    float currentTranslationX = gridView.getTranslationX();
                    float newX = currentTranslationX + 50;
                    gridView.setTranslationX(newX);


                }

            }
        });

        buttonup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gridView != null) {
                    float currentTranslationY = gridView.getTranslationY();
                    float newY = currentTranslationY + 50;
                    gridView.setTranslationY(newY);


                }

            }
        });
        buttondown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gridView != null) {
                    float currentTranslationY = gridView.getTranslationY();
                    float newY = currentTranslationY - 50;
                    gridView.setTranslationY(newY);


                }

            }
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