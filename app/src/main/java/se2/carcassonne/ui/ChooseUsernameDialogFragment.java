package se2.carcassonne.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;

import se2.carcassonne.R;
import se2.carcassonne.model.Player;
import se2.carcassonne.repository.PlayerRepository;
import se2.carcassonne.viewmodel.PlayerViewModel;

public class ChooseUsernameDialogFragment extends DialogFragment {
    private final PlayerViewModel playerViewModel;
    private LiveData<String> userAlreadyExistsLiveData;

    public ChooseUsernameDialogFragment(PlayerViewModel playerViewModel){
        this.playerViewModel = playerViewModel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.username_dialog, null);
        EditText text = dialogView.findViewById(R.id.usernameInput);
        setCancelable(false);
        Button chooseUsernameStartGameButton = dialogView.findViewById(R.id.btnConfirmUsername);

        userAlreadyExistsLiveData = playerViewModel.getUserAlreadyExistsErrorMessage();
        LiveData<String> invalidUsernameLiveData = playerViewModel.getInvalidUsernameErrorMessage();
        LiveData<String> messageLiveData = playerViewModel.getMessageLiveData();

        userAlreadyExistsLiveData.observe(this, errorMessage -> Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show());
        invalidUsernameLiveData.observe(this, errorMessage -> Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show());

        PlayerRepository playerRepository = PlayerRepository.getInstance();

        messageLiveData.observe(this, message -> {
            // ensure that the player object is set
            if(playerRepository.getCurrentPlayer() == null) {
                // wait for the player object to be set
                return;
            } else {
                dismiss();
            }
        });

        chooseUsernameStartGameButton.setOnClickListener(view -> playerViewModel.createPlayer(new Player(null, text.getText().toString(), null, null, null, null)));

        return builder.setView(dialogView).create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        userAlreadyExistsLiveData.removeObservers(this);
        playerViewModel.getMessageLiveData().removeObservers(this);
    }
}