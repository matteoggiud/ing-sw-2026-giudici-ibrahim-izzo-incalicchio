package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.observer.dto.BoardDTO;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;
import it.polimi.ingsw.server.model.observer.dto.PlayerDeckDTO;

import java.util.List;
import java.util.Map;

public interface ClientView {
    void showMessage(String message);

    void showError(String message);

    Credentials requestLoginCredentials();

    Credentials requestRegisterCredentials();

    void showAvailableGames(List<GameInfoDTO> games);

    void showGameInfo(GameInfoDTO gameInfo);

    void showGameStarted(int gameId, List<String> playerNicknames);

    void showBoardSummary(BoardDTO board);

    void showPlaceTotemTurn(String currentPlayerNickname, boolean isMyTurn);

    void showResolveActionTurn(String currentPlayerNickname, boolean isMyTurn);

    void showExtraActionTurn(String currentPlayerNickname, boolean isMyTurn);

    void showPlayerResources(String playerNickname, int food, int prestigePoints, boolean isMe);

    void showPlayerDeck(String playerNickname, PlayerDeckDTO playerDeck, boolean isMe);

    void showGameEnded(int gameId, Map<String, Integer> finalRanking);

}
