package se2.carcassonne.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.sql.Timestamp;

import se2.carcassonne.R;
import se2.carcassonne.model.Lobby;
import se2.carcassonne.repository.PlayerRepository;
import se2.carcassonne.viewmodel.LobbyViewModel;

public class CreateLobbyDialog extends DialogFragment {
    private final LobbyViewModel lobbyViewmodel;

    public CreateLobbyDialog() {
        this.lobbyViewmodel = new LobbyViewModel();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_lobby_dialog, null);
        EditText text = dialogView.findViewById(R.id.lobbyNameInput);
        Button createLobbyBtn = dialogView.findViewById(R.id.btnCreateLobby);

        createLobbyBtn.setOnClickListener(view -> {
            String lobbyName = text.getText().toString();
            lobbyViewmodel.createLobby(
                    new Lobby(null, lobbyName,
                            new Timestamp(System.currentTimeMillis()),
                            "LOBBY",
                            null,
                            PlayerRepository.getInstance().getCurrentPlayer().getId()
                    ));
        });

        lobbyViewmodel.getLobbyAlreadyExistsErrorMessage().observe(this, errorMessage -> {
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        lobbyViewmodel.getInvalidLobbyNameErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        lobbyViewmodel.getCreateLobbyLiveData().observe(this, message -> {
            if (message != null) {
                Intent intent = new Intent(requireActivity(), InLobbyActivity.class);
                intent.putExtra("LOBBY", message);
                startActivity(intent);
                dismiss();
            }
        });

        return builder.setView(dialogView).create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        lobbyViewmodel.getLobbyAlreadyExistsErrorMessage().removeObservers(this);
        lobbyViewmodel.getInvalidLobbyNameErrorMessage().removeObservers(this);
        lobbyViewmodel.getCreateLobbyLiveData().removeObservers(this);

        lobbyViewmodel.getCreateLobbyLiveData().setValue(null);
        lobbyViewmodel.getInvalidLobbyNameErrorMessage().setValue(null);
        lobbyViewmodel.getLobbyAlreadyExistsErrorMessage().setValue(null);
    }
}
