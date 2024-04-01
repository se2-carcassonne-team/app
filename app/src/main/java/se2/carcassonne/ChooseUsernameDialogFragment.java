package se2.carcassonne;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import se2.carcassonne.player.model.Player;
import se2.carcassonne.player.repository.PlayerRepository;
import se2.carcassonne.player.viewmodel.ChooseUsernameViewModel;

public class ChooseUsernameDialogFragment extends DialogFragment {
    private final ChooseUsernameViewModel viewModel;
    private final PlayerRepository playerRepository;

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
        Button chooseUsernameStartGameButton = dialogView.findViewById(R.id.button);
        chooseUsernameStartGameButton.setOnClickListener(view -> {
            playerRepository.connectToWebSocketServer();
            viewModel.createPlayer(new Player(null, text.getText().toString(), null));
            dismiss();
        });
        builder.setView(dialogView);
        return builder.create();
    }
}
