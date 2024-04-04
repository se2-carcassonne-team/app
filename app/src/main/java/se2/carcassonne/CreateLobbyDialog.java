package se2.carcassonne;

import android.app.Dialog;
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
import se2.carcassonne.lobby.repository.LobbyRepository;
import se2.carcassonne.lobby.viewmodel.LobbyListViewModel;

public class CreateLobbyDialog extends DialogFragment {
    LobbyListViewModel lobbyViewmodel;
    LobbyRepository lobbyRepository;
    private LiveData<String> lobbyAlreadyExistsLiveData;
    private LiveData<String> invalidLobbyLiveData;
    private LiveData<String> createLobbyLiveData;
    public CreateLobbyDialog(LobbyListViewModel lobbyViewmodel, LobbyRepository lobbyRepository) {
        this.lobbyViewmodel = lobbyViewmodel;
        this.lobbyRepository = lobbyRepository;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_lobby_dialog, null);
        EditText text = dialogView.findViewById(R.id.lobbyNameInput);
        Button createLobbyBtn = dialogView.findViewById(R.id.button);


        lobbyAlreadyExistsLiveData = lobbyViewmodel.getLobbyAlreadyExistsErrorMessage();
        invalidLobbyLiveData = lobbyViewmodel.getInvalidLobbyNameErrorMessage();
        createLobbyLiveData = lobbyViewmodel.getCreateLobbyLiveData();

        createLobbyBtn.setOnClickListener(view -> {
            String lobbyName = text.getText().toString();
            lobbyRepository.connectToWebSocketServer();
            lobbyViewmodel.createLobby(
                    new Lobby(null, lobbyName,
                            new Timestamp(System.currentTimeMillis()),
                            "LOBBY",
                            null
                    ));
        });

        lobbyAlreadyExistsLiveData.observe(this, errorMessage -> {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });

        invalidLobbyLiveData.observe(this, errorMessage -> {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });

        createLobbyLiveData.observe(this, message -> {
            Intent intent = new Intent(requireActivity(), InLobbyActivity.class);
            intent.putExtra("LOBBY", message); // Pass the message as an extra
            startActivity(intent);
            dismiss();
        });



        builder.setView(dialogView);
        return builder.create();
    }
}
