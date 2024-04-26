package se2.carcassonne.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import se2.carcassonne.R;
import se2.carcassonne.databinding.HomeActivityBinding;
import se2.carcassonne.helper.animation.AnimationHelper;
import se2.carcassonne.helper.mapper.MapperHelper;
import se2.carcassonne.helper.resize.FullscreenHelper;

import se2.carcassonne.viewmodel.PlayerViewModel;

public class HomeActivity extends AppCompatActivity {
    HomeActivityBinding binding;
    private PlayerViewModel playerViewModel;
    private final MapperHelper mapperHelper = new MapperHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);
        
        ImageView logo_animated = findViewById(R.id.logo_animation);
        logo_animated.setVisibility(View.INVISIBLE);

        AnimationHelper.fadeIn(logo_animated,2000,null);

        playerViewModel = new PlayerViewModel();

        showChooseUsernameDialog();
        playerViewModel.getMessageLiveData().observe(this, message -> binding.textView2.setText(String.format(getString(R.string.welcome_homescreen), mapperHelper.getPlayerName(message))));
        binding.newGameBtn.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, GameLobbyActivity.class);
            startActivity(intent);
        });
    }

    private void showChooseUsernameDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ChooseUsernameDialogFragment dialogFragment = new ChooseUsernameDialogFragment(playerViewModel);
        dialogFragment.show(fragmentManager, "ChooseUsernameDialogFragment");
    }
}
