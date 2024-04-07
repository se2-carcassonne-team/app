package se2.carcassonne;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import se2.carcassonne.databinding.HomeActivityBinding;
import se2.carcassonne.helper.resize.FullscreenHelper;

import se2.carcassonne.player.repository.PlayerRepository;
import se2.carcassonne.player.viewmodel.ChooseUsernameViewModel;

public class HomeActivity extends AppCompatActivity {
    HomeActivityBinding binding;
    private ChooseUsernameViewModel chooseUsernameViewModel;
    PlayerRepository playerRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);
        
        ImageView logo_animated = findViewById(R.id.logo_animation);
        logo_animated.setVisibility(View.INVISIBLE);

        AnimationHelper.fadeIn(logo_animated,2000,null);

       

        playerRepository = PlayerRepository.getInstance();
        chooseUsernameViewModel = new ChooseUsernameViewModel(playerRepository);
        showChooseUsernameDialog();
        chooseUsernameViewModel.getMessageLiveData().observe(this, message -> {
            binding.textView2.setText(String.format(getString(R.string.welcome_homescreen), chooseUsernameViewModel.getPlayerName(message)));
        });
        binding.newGameBtn.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, GameLobbyActivity.class);
            startActivity(intent);
        });
    }

    private void showChooseUsernameDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ChooseUsernameDialogFragment dialogFragment = new ChooseUsernameDialogFragment(chooseUsernameViewModel, playerRepository);
        dialogFragment.show(fragmentManager, "ChooseUsernameDialogFragment");

    }
}
