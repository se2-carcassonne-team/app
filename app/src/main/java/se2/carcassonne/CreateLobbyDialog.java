package se2.carcassonne;

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
import androidx.lifecycle.LiveData;

import java.sql.Timestamp;

import se2.carcassonne.lobby.model.Lobby;
import se2.carcassonne.lobby.viewmodel.LobbyViewModel;
import se2.carcassonne.player.repository.PlayerRepository;

public class CreateLobbyDialog extends DialogFragment {
    private final LobbyViewModel lobbyViewmodel;
    private LiveData<String> lobbyAlreadyExistsLiveData;
    private LiveData<String> invalidLobbyLiveData;
    private LiveData<String> createLobbyLiveData;

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

        lobbyAlreadyExistsLiveData = lobbyViewmodel.getLobbyAlreadyExistsErrorMessage();
        invalidLobbyLiveData = lobbyViewmodel.getInvalidLobbyNameErrorMessage();
        createLobbyLiveData = lobbyViewmodel.getCreateLobbyLiveData();

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

        lobbyAlreadyExistsLiveData.observe(this, errorMessage -> Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show());
        invalidLobbyLiveData.observe(this, errorMessage -> Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show());
        createLobbyLiveData.observe(this, message -> {
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
        lobbyViewmodel.getCreateLobbyLiveData().setValue(null);
        lobbyAlreadyExistsLiveData.removeObservers(this);
        invalidLobbyLiveData.removeObservers(this);
        createLobbyLiveData.removeObservers(this);
    }
}
