package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.common.network.dto.*;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ViewController {
    private final ClientState clientState;
    private final ClientView view;

    public ViewController(ClientState clientState, ClientView view) {
        this.clientState = clientState;
        this.view = view;
    }

    /**
     * Called when a game starts, providing the game ID and the list of player nicknames.
     *
     * @param gameId          the unique identifier of the started game
     * @param playerNicknames the list of nicknames of the players participating in the game
     * @throws RemoteException if a remote communication error occurs
     */
    public void onGameStarted(int gameId, List<String> playerNicknames) {
        clientState.clearGameState();

        clientState.setFinalRanking(Map.of());
        clientState.setHistoricalRankings(List.of());
        clientState.setActiveGameId(gameId);
        clientState.clearPlayersInfo();
        clientState.setCurrentPhase("STARTED");
        clientState.setStatusMessage("Game started.");

        view.showGameStarted(gameId, playerNicknames);
    }

    /**
     * Called when authentication is needed, indicating that the client must provide credentials to proceed with the login process.
     *
     * @param message the message indicating that authentication is needed, which can be used to inform the client about the requirement for authentication and prompt them to provide their credentials (e.g., "Please enter your nickname and password to log in.").
     */
    public void onAuthenticationNeeded(String message) {
        clientState.setStatusMessage(message);
        view.showAuthenticator(message);
    }

    /**
     * Called when a login attempt is successful, providing the nickname of the logged-in player.
     *
     * @param nickname the nickname of the player who successfully logged in
     * @param message  the message indicating the successful login, which can be used to inform the client about the successful login and allow them to proceed with other actions (e.g., viewing available games, joining a game, etc.).
     */
    public void onLoginSuccess(String nickname, String message) {
        clientState.setNickname(nickname);
        clientState.setCurrentPhase("MENU");
        clientState.setStatusMessage(message);
        view.showMessage(message);
    }

    /**
     * Called when a login attempt fails, providing the reason for the failure.
     *
     * @param reason   the reason for the login failure, which can be used to inform the client about the issue (e.g., "Nickname already taken", "Invalid credentials", etc.)
     * @param nickname
     */
    public void onLoginFailed(String reason, String nickname) {
        clientState.setErrorMessage(reason);
        clientState.setNickname(null);
        view.showError(reason);
    }

    /**
     * Called when the list of available games is updated, providing a list of GameInfoDTO objects representing the currently available games that clients can join.
     *
     * @param games the list of GameInfoDTO objects representing the currently available games that clients can join, which can be used to update the client's view of available games and allow them to select a game to join.
     */
    public void onAvailableGames(List<GameInfoDTO> games) {
        clientState.setAvailableGames(games);
        clientState.setCurrentPhase("MENU");
        clientState.setFinalRanking(Map.of());
        clientState.setHistoricalRankings(List.of());
        view.showAvailableGames(games);
    }

    /**
     * Called when a generic error occurs, providing the reason for the error.
     *
     * @param reason the reason for the error, which can be used to inform the client about the issue
     */
    public void onGenericError(String reason) {
        clientState.setErrorMessage(reason);
        view.showError(reason);
    }

    /**
     * Called when a registration attempt is successful, providing the nickname of the registered player.
     *
     * @param nickname the nickname of the player who successfully registered, which can be used to inform the client about the successful registration and allow them to proceed with the login process or other actions.
     * @param message  the message indicating the successful registration
     */
    public void onRegisterSuccess(String nickname, String message) {
        clientState.setNickname(nickname);
        clientState.setStatusMessage(message);
        view.showMessage(message);
    }

    /**
     * Called when a registration attempt fails, providing the reason for the failure.
     *
     * @param reason   the reason for the registration failure, which can be used to inform the client about the issue (e.g., "Nickname already taken", "Invalid input", etc.) and allow them to correct the issue and attempt registration again.
     * @param nickname
     */
    public void onRegisterFailed(String reason, String nickname) {
        clientState.setErrorMessage(reason);
        clientState.setNickname(null);
        view.showError(reason);
    }

    /**
     * Called when a disconnection attempt is successful, providing the nickname of the disconnected player.
     *
     * @param message the message indicating the successful disconnection
     */
    public void onDisconnectionSuccess(String message) {
        clientState.disconnect();
        clientState.setStatusMessage(message);
        view.showMessage(message);
    }

    /**
     * Called when a player successfully leaves a game, providing a message that can be used to inform the client about the successful departure and allow them to update their view accordingly (e.g., returning to the main menu, etc.).
     *
     * @param message the message
     */
    public void onLeftGameSuccess(String message) {
        clientState.clearGameState();
        clientState.setStatusMessage(message);
        view.showMessage(message);
    }

    /**
     * Called when a generic informational message needs to be sent to the client, providing the information that can be used to inform the client about various events or updates (e.g., "Game will start in 30 seconds", "Player X has joined the game", etc.).
     *
     * @param info the informational message that can be used to inform the client about various events or updates, which can be displayed in the client's view to keep them informed about the current state of the game or other relevant information.
     */
    public void onGenericInfo(String info) {
        clientState.setStatusMessage(info);
        view.showMessage(info);
    }

    /**
     * Called when the list of players in a game changes
     *
     * @param playerNicknames the list of nicknames of the players currently in the game
     */
    public void onPlayerChanged(List<String> playerNicknames) {
        String msg = "Players: " + String.join(", ", playerNicknames);
        clientState.setStatusMessage(msg);
        view.showMessage(msg);
    }

    /**
     * Called when the game board changes
     *
     * @param gameId the unique identifier of the game
     * @param board  the updated state of the game board
     */
    public void onBoardChanged(int gameId, BoardDTO board) {
        clientState.setBoard(board);
        view.showBoardSummary(board);
    }

    /**
     * Called when a player successfully joins a game, providing the updated game information that can be used to inform the client about the current state of the game and allow them to update their view accordingly (e.g., displaying the updated list of players in the game, etc.).
     *
     * @param gameInfo the updated game information that can be used to inform the client about the current state of the game and allow them to update their view accordingly (e.g., displaying the updated list of players in the game, etc.).
     */
    public void onJoinedGame(GameInfoDTO gameInfo) {
        clientState.setActiveGameId(gameInfo.gameId());
        clientState.setCurrentGameInfo(gameInfo);
        clientState.setCurrentPhase("LOBBY");
        clientState.setFinalRanking(Map.of());
        clientState.setHistoricalRankings(List.of());
        view.showGameInfo(gameInfo);
    }

    /**
     * Called when the current era of the game changes
     *
     * @param gameId the unique identifier of the game
     * @param era    the new era of the game
     */
    public void onEraChanged(int gameId, String era) {
        String msg = "Era changed: " + era;
        clientState.setStatusMessage(msg);
        view.showMessage(msg);
    }

    /**
     * Called when the current player's turn changes during the "Place Totem" phase of the game.
     *
     * @param gameId                the unique identifier of the game
     * @param currentPlayerNickname the nickname of the player whose turn it currently is during the "Place Totem" phase.
     */
    public void onPlaceTotemTurnChanged(int gameId, String currentPlayerNickname) {
        clientState.setCurrentPhase("PLACE_TOTEM");
        clientState.setCurrentPlayerNickname(currentPlayerNickname);
        boolean isMyTurn = currentPlayerNickname.equals(clientState.getNickname());
        view.showPlaceTotemTurn(currentPlayerNickname, isMyTurn);
    }

    /**
     * Called when the current player's turn changes during the "Resolve Action" phase of the game.
     *
     * @param gameId                the unique identifier of the game
     * @param currentPlayerNickname the nickname of the player whose turn it currently is during the "Resolve Action" phase.
     */
    public void onResolveActionTurnChanged(int gameId, String currentPlayerNickname) {
        clientState.setCurrentPhase("RESOLVE_ACTION");
        clientState.setCurrentPlayerNickname(currentPlayerNickname);
        boolean isMyTurn = currentPlayerNickname.equals(clientState.getNickname());
        view.showResolveActionTurn(currentPlayerNickname, isMyTurn);
    }

    /**
     * Called when the current player's turn changes during the "Extra Action" phase of the game.
     *
     * @param gameId                the unique identifier of the game
     * @param currentPlayerNickname the nickname of the player whose turn it currently is during the "Extra Action" phase.
     */
    public void onExtraActionTurnChanged(int gameId, String currentPlayerNickname) {
        clientState.setCurrentPhase("EXTRA_ACTION");
        clientState.setCurrentPlayerNickname(currentPlayerNickname);
        boolean isMyTurn = currentPlayerNickname.equals(clientState.getNickname());
        view.showExtraActionTurn(currentPlayerNickname, isMyTurn);
    }

    /**
     * Called when a player's deck is updated
     *
     * @param gameId     the unique identifier of the game
     * @param nickname   the nickname of the player whose deck is updated
     * @param playerDeck the updated state of the player's deck
     */
    public void onPlayerDeckUpdated(int gameId, String nickname, PlayerDeckDTO playerDeck) {
        view.showPlayerDeck(nickname, playerDeck, nickname.equals(clientState.getNickname()));
    }

    /**
     * Called when a game ends, providing the final ranking of players in the game.
     *
     * @param gameId       the unique identifier of the game
     * @param finalRanking a map containing the final ranking of players in the game, where the key is the player's nickname and the value is their final score or position in the ranking. This information can be used to inform the client about the outcome of the game and display the final results to the players.
     */
    public void onGameEnded(int gameId, Map<String, Integer> finalRanking, int numPlayers, List<LeadBoardDTO> historicalLeaderboard) {
        clientState.setActiveGameId(null);
        clientState.setCurrentPhase("GAME_ENDED");
        clientState.setFinalRanking(finalRanking);
        clientState.setHistoricalRankings(historicalLeaderboard);
        clientState.setStatusMessage("Game ended. Historical leaderboard for " + numPlayers + " players updated.");
        view.showGameEnded();
    }

    /**
     * Called when some player stats changed
     *
     * @param gameId      the unique identifier of the game
     * @param playerInfos the list of PlayerInfoDTO objects representing the updated information about the players in the game
     */
    public void onPlayerInfosChanged(int gameId, List<PlayerInfoDTO> playerInfos) {
        clientState.updatePlayersInfo(playerInfos);
        view.showPlayersInfo(clientState.getPlayersInfo(), clientState.getNickname());
    }

    /**
     * Called when an event card is being resolved, providing the game ID and the details of the event card being resolved. This information can be used to inform the client about the ongoing resolution of an event card and allow them to update their view accordingly (e.g., displaying the effects of the event card, etc.).
     *
     * @param gameId    the unique identifier of the game
     * @param eventCard the EventCardDTO object containing the details of the event card being resolved.
     */
    public void onResolvingEvent(int gameId, EventCardDTO eventCard) {
        clientState.setResolvingEventName(eventCard.name());
        clientState.pushEventCard(eventCard);
        clientState.setStatusMessage("Resolving event: " + eventCard.name());
        view.showResolvingEvent(gameId, eventCard);
    }

    /**
     * Called when the rankings are received, providing a list of LeadBoardDTO objects representing the rankings of players in the game. This information can be used to inform the client about the current standings of players in the game and allow them to update their view accordingly (e.g., displaying the rankings on a leaderboard, etc.).
     *
     * @param rankings the list of LeadBoardDTO objects representing the rankings of players in the game, which can be used to inform the client about the current standings of players in the game and allow them to update their view accordingly (e.g., displaying the rankings on a leaderboard, etc.).
     */
    public void onRankingsReceived(List<LeadBoardDTO> rankings) {
        clientState.setFinalRanking(Map.of());
        clientState.setHistoricalRankings(rankings);
        clientState.setCurrentPhase("LEADERBOARD");
        view.showRankings(rankings);
    }

    /**
     * Called when a game crashes, providing the reason for the crash. This information can be used to inform the client about the issue and allow them to update their view accordingly (e.g., displaying an error message, returning to the main menu, etc.).
     *
     * @param reason the reason for the game crash
     */
    public void onGameCrashed(String reason) {
        String message = "Game ended: " + reason;

        clientState.clearGameState();
        clientState.setErrorMessage(message);
        clientState.setStatusMessage(message);

        view.showDisconnected(message);
    }
}
