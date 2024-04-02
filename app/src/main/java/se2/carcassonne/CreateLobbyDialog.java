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

import se2.carcassonne.gamelobby.repository.GameLobbyRepository;
import se2.carcassonne.gamelobby.viewmodel.GameLobbyViewModel;

public class CreateLobbyDialog extends DialogFragment {
    GameLobbyViewModel viewModel;
    GameLobbyRepository gameLobbyRepository;
    public CreateLobbyDialog(GameLobbyViewModel viewModel, GameLobbyRepository gameLobbyRepository){
        this.viewModel = viewModel;
        this.gameLobbyRepository = gameLobbyRepository;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_lobby_dialog, null);
        EditText text = dialogView.findViewById(R.id.lobbyNameInput);
        setCancelable(false);
        Button createLobbyBtn = dialogView.findViewById(R.id.button);
        builder.setView(dialogView);
        return builder.create();
    }
}
