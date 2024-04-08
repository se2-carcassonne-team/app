package se2.carcassonne;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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

import se2.carcassonne.player.model.Player;
import se2.carcassonne.player.repository.PlayerRepository;
import se2.carcassonne.player.viewmodel.ChooseUsernameViewModel;

public class ChooseUsernameDialogFragment extends DialogFragment {
    private ChooseUsernameViewModel viewModel;
    private PlayerRepository playerRepository;
    private LiveData<String> userAlreadyExistsLiveData;
    private LiveData<String> invalidUsernameLiveData;
    private LiveData<String> messageLiveData;

    public ChooseUsernameDialogFragment(ChooseUsernameViewModel viewModel, PlayerRepository playerRepository){
        this.viewModel = viewModel;
        this.playerRepository = playerRepository;
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

        userAlreadyExistsLiveData = viewModel.getUserAlreadyExistsErrorMessage();
        invalidUsernameLiveData = viewModel.getInvalidUsernameErrorMessage();
        messageLiveData = viewModel.getMessageLiveData();

        userAlreadyExistsLiveData.observe(this, errorMessage -> {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });

        invalidUsernameLiveData.observe(this, errorMessage -> {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });

        chooseUsernameStartGameButton.setOnClickListener(view -> {
            String username = text.getText().toString();
            playerRepository.connectToWebSocketServer();
            viewModel.createPlayer(new Player(null, username, null));
            Log.d("CLU", "Username created!");

        });

        messageLiveData.observe(this, message -> {
            dismiss();
            Log.d("CLU", "User Fragment dismissed!");
        });

        return builder.setView(dialogView).create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        userAlreadyExistsLiveData.removeObservers(this);
        userAlreadyExistsLiveData.removeObservers(this);
        viewModel.getMessageLiveData().removeObservers(this);
    }
}