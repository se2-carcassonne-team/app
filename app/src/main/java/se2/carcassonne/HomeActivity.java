package se2.carcassonne;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import se2.carcassonne.databinding.HomeActivityBinding;
import se2.carcassonne.gamelobby.repository.GameLobbyRepository;
import se2.carcassonne.gamelobby.viewmodel.GameLobbyViewModel;
import se2.carcassonne.helper.resize.FullscreenHelper;
import se2.carcassonne.player.repository.PlayerRepository;
import se2.carcassonne.player.viewmodel.ChooseUsernameViewModel;

public class HomeActivity extends AppCompatActivity {
    HomeActivityBinding binding;
    private ChooseUsernameViewModel chooseUsernameViewModel;
    private GameLobbyViewModel gameLobbyViewModel;
    PlayerRepository playerRepository;
    GameLobbyRepository gameLobbyRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);
        playerRepository = new PlayerRepository();
        chooseUsernameViewModel = new ChooseUsernameViewModel(playerRepository);
        showChooseUsernameDialog();
        chooseUsernameViewModel.getMessageLiveData().observe(this, message -> binding.textView2.setText(String.format(getString(R.string.welcome_homescreen), chooseUsernameViewModel.getPlayerName(message))));
        binding.newGameBtn.setOnClickListener(view -> showCreateLobbyDialog());
    }

    private void showChooseUsernameDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ChooseUsernameDialogFragment dialogFragment = new ChooseUsernameDialogFragment(chooseUsernameViewModel, playerRepository);
        dialogFragment.show(fragmentManager, "ChooseUsernameDialogFragment");
    }

    private void showCreateLobbyDialog(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        CreateLobbyDialog dialogFragment = new CreateLobbyDialog(gameLobbyViewModel, gameLobbyRepository);
        dialogFragment.show(fragmentManager, "CreateLobbyDialog");
    }
}
