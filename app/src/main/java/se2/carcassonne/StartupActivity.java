package se2.carcassonne;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import se2.carcassonne.databinding.StartupActivityBinding;
import se2.carcassonne.helper.animation.AnimationHelper;
import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.player.model.Player;
import se2.carcassonne.player.repository.PlayerRepository;

public class StartupActivity extends AppCompatActivity {
    StartupActivityBinding binding;
    PlayerRepository repository = PlayerRepository.getInstance();
    private final WebSocketClient webSocketClient = WebSocketClient.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webSocketClient.connect();
        binding = StartupActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        final ImageView castle = findViewById(R.id.imageView);
        castle.setVisibility(View.INVISIBLE);

        final ImageView carcassonneLogo = findViewById(R.id.imageView2);
        carcassonneLogo.setVisibility(View.INVISIBLE);

        final Button startGameBtn = findViewById(R.id.startgame);
        startGameBtn.setVisibility(View.INVISIBLE);

        AnimationHelper.fadeIn(castle, 2000, new Runnable(){
            @Override
            public void run(){
                AnimationHelper.fadeIn(carcassonneLogo, 2000, new Runnable(){
                    @Override
                    public void run(){
                        AnimationHelper.fadeIn(startGameBtn, 2000, new Runnable(){
                            @Override
                            public void run(){
                                AnimationHelper.startButtonAnimation(startGameBtn);
                            }
                        });
                    }
                });
            }
        });

        startGameBtn.setOnClickListener(v -> {

        //binding.button.setOnClickListener(v -> {
            Intent intent = new Intent(StartupActivity.this, HomeActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Player player = PlayerRepository.getInstance().getCurrentPlayer();
        if(player != null) repository.deletePlayer(player);
    }

}