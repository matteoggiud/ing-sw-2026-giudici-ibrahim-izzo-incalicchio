package it.polimi.ingsw.client.view.tui;

import it.polimi.ingsw.client.network.socket.ClientSessionState;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.client.view.Credentials;
import it.polimi.ingsw.server.model.observer.dto.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TuiClientView implements ClientView {
    private final Scanner scanner;
    private final ClientSessionState sessionState;
    private final TerminalRenderer renderer;

    public TuiClientView(Scanner scanner, ClientSessionState sessionState) {
        this.scanner = scanner;
        this.sessionState = sessionState;
        this.renderer = new TerminalRenderer(sessionState);
    }

    @Override
    public void showMessage(String message) {
        sessionState.setStatusMessage(message);
        renderer.render();
    }

    @Override
    public void showError(String message) {
        sessionState.setErrorMessage(message);
        renderer.render();
    }


    @Override
    public Credentials requestLoginCredentials() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        return new Credentials(username, password);
    }

    @Override
    public Credentials requestRegisterCredentials() {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Choose a password: ");
        String password = scanner.nextLine().trim();

        return new Credentials(username, password);
    }

    @Override
    public void showAvailableGames(List<GameInfoDTO> games) {
        sessionState.setFinalRanking(Map.of());
        sessionState.setAvailableGames(games);
        sessionState.setCurrentPhase("MENU");
        renderer.render();
    }


    @Override
    public void showGameInfo(GameInfoDTO gameInfo) {
        sessionState.setFinalRanking(Map.of());
        sessionState.setCurrentGameInfo(gameInfo);
        sessionState.setActiveGameId(gameInfo.gameId());
        sessionState.setCurrentPhase("LOBBY");
        renderer.render();
    }


    @Override
    public void showGameStarted(int gameId, List<String> playerNicknames) {
        sessionState.setFinalRanking(Map.of());
        sessionState.setActiveGameId(gameId);
        sessionState.setStatusMessage("Game started.");
        sessionState.setCurrentPhase("STARTED");
        renderer.render();
    }


    @Override
    public void showBoardSummary(BoardDTO board) {
        sessionState.setBoard(board);
        renderer.render();
    }


    @Override
    public void showPlaceTotemTurn(String currentPlayerNickname, boolean isMyTurn) {
        sessionState.setCurrentPhase("PLACE_TOTEM");
        sessionState.setCurrentPlayerNickname(currentPlayerNickname);
        renderer.render();
    }


    @Override
    public void showResolveActionTurn(String currentPlayerNickname, boolean isMyTurn) {
        sessionState.setCurrentPhase("RESOLVE_ACTION");
        sessionState.setCurrentPlayerNickname(currentPlayerNickname);
        renderer.render();
    }


    @Override
    public void showExtraActionTurn(String currentPlayerNickname, boolean isMyTurn) {
        sessionState.setCurrentPhase("EXTRA_ACTION");
        sessionState.setCurrentPlayerNickname(currentPlayerNickname);
        renderer.render();
    }


    @Override
    public void showPlayerResources(String playerNickname, int food, int prestigePoints, boolean isMe) {
        // Deprecated for the renderer-based TUI.
    }


    @Override
    public void showPlayersInfo(Collection<PlayerInfoDTO> playersInfo, String myNickname) {
        renderer.render();
    }

    public void showMyDeck() {
        renderer.renderMyDeck();
    }


    @Override
    public void showPlayerDeck(String playerNickname, PlayerDeckDTO playerDeck, boolean isMe) {
        renderer.render();
    }


    @Override
    public void showGameEnded(int gameId, Map<String, Integer> finalRanking) {
        sessionState.setCurrentPhase("GAME_ENDED");
        sessionState.setFinalRanking(finalRanking);
        renderer.render();
    }



    @Override
    public void showRankings(List<LeadBoardDTO> rankings) {
        sessionState.setHistoricalRankings(rankings);
        sessionState.setCurrentPhase("LEADERBOARD");
        renderer.render();
    }

}
