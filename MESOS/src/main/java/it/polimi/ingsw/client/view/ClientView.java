package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;

import java.util.List;

public interface ClientView {
    void showMessage(String message);

    void showError(String message);

    Credentials requestLoginCredentials();

    Credentials requestRegisterCredentials();

    void showAvailableGames(List<GameInfoDTO> games);
}
