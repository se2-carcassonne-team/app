package se2.carcassonne.ui;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import se2.carcassonne.databinding.GameEndActivityBinding;

import se2.carcassonne.helper.network.WebSocketClient;
import se2.carcassonne.helper.resize.FullscreenHelper;

import se2.carcassonne.viewmodel.LobbyViewModel;

public class GameEndActivity extends AppCompatActivity {
    private static final String KEY_RESTART_INTENT = "KEY_RESTART";
    GameEndActivityBinding binding;
    LobbyViewModel lobbyViewModel;
    private final WebSocketClient webSocketClient = WebSocketClient.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GameEndActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);

        lobbyViewModel = new LobbyViewModel();

        if (getIntent().hasExtra("PLAYER0")) {
            String first = getIntent().getStringExtra("PLAYER0");
            binding.tvFirstPlace.setText(first);
        }
        if (getIntent().hasExtra("PLAYER1")) {
            binding.tvSecondPlace.setText(getIntent().getStringExtra("PLAYER1"));
        }
        if (getIntent().hasExtra("PLAYER2")) {
            binding.tvThirdPlace.setText(getIntent().getStringExtra("PLAYER2"));
        }

        binding.btnMainMenu.setOnClickListener(v -> {
            Intent nextIntent = new Intent(this, StartupActivity.class); // Replace with your actual intent
            //resetApp(this, nextIntent);
            webSocketClient.cancelAllSubscriptions();
            startActivity(nextIntent);
        });
    }

    private void resetApp(Context context, Intent nextIntent) {

        Intent intent = new Intent(context, StartupActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_RESTART_INTENT, nextIntent);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }

        Runtime.getRuntime().exit(0);
    }
}