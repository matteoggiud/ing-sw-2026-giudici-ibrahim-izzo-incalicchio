package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.server.model.observer.dto.*;
import it.polimi.ingsw.server.network.GenericVirtualView;
import it.polimi.ingsw.server.network.socket.messages.GenericErrorMessage;
import it.polimi.ingsw.server.network.socket.messages.ServerMessage;
import it.polimi.ingsw.server.network.socket.messages.game.*;
import it.polimi.ingsw.server.network.socket.messages.login.LoginFailedMessage;
import it.polimi.ingsw.server.network.socket.messages.login.LoginNeededMessage;
import it.polimi.ingsw.server.network.socket.messages.login.LoginSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.AvailableGamesMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.DisconnectSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.LeftGameSuccessMessage;
import it.polimi.ingsw.server.network.socket.messages.menu.RankingsMessage;
import it.polimi.ingsw.server.network.socket.messages.register.RegisterFailedMessage;
import it.polimi.ingsw.server.network.socket.messages.register.RegisterSuccessMessage;

import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

public class SocketGenericVirtualView implements GenericVirtualView {
    private final ObjectOutputStream outputStream;

    SocketGenericVirtualView(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Sends a message to the client through the socket connection.
     *
     * @param message the message to be sent to the client
     * @throws RuntimeException if there is an error while sending the message
     */
    public void sendMessage(ServerMessage message) throws RuntimeException {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message to client: " + e.getMessage(), e);
        }
    }

    /**
     * Called when a game starts, providing the game ID and the list of player nicknames.
     *
     * @param gameId          the unique identifier of the started game
     * @param playerNicknames the list of nicknames of the players participating in the game
     */
    @Override
    public void onGameStarted(int gameId, List<String> playerNicknames) {
        ServerMessage serverMessage = new GameStartedMessage(gameId, playerNicknames);

        sendMessage(serverMessage);
    }

    /**
     * Called when authentication is needed, indicating that the client must provide credentials to proceed with the login process.
     *
     * @param message the message indicating that authentication is needed, which can be used to inform the client about the requirement for authentication and prompt them to provide their credentials (e.g., "Please enter your nickname and password to log in.").
     */
    @Override
    public void onAuthenticationNeeded(String message) {
        ServerMessage serverMessage = new LoginNeededMessage(message);

        sendMessage(serverMessage);
    }

    /**
     * Called when a login attempt is successful, providing the nickname of the logged-in player.
     *
     * @param nickname the nickname of the player who successfully logged in
     * @param message  the message indicating the successful login, which can be used to inform the client about the successful login and allow them to proceed with other actions (e.g., viewing available games, joining a game, etc.).
     */
    @Override
    public void onLoginSuccess(String nickname, String message) {
        ServerMessage serverMessage = new LoginSuccessMessage(nickname, message);

        sendMessage(serverMessage);
    }

    /**
     * Called when a login attempt fails, providing the reason for the failure.
     *
     * @param reason the reason for the login failure, which can be used to inform the client about the issue (e.g., "Nickname already taken", "Invalid credentials", etc.)
     */
    @Override
    public void onLoginFailed(String reason, String nickname) {
        ServerMessage serverMessage = new LoginFailedMessage(reason, nickname);

        sendMessage(serverMessage);
    }

    /**
     * Called when the list of available games is updated, providing a list of GameInfoDTO objects representing the currently available games that clients can join.
     *
     * @param games the list of GameInfoDTO objects representing the currently available games that clients can join, which can be used to update the client's view of available games and allow them to select a game to join.
     */
    @Override
    public void onAvailableGames(List<GameInfoDTO> games) {
        ServerMessage serverMessage = new AvailableGamesMessage(games);

        sendMessage(serverMessage);
    }

    /**
     * Called when a generic error occurs, providing the reason for the error.
     *
     * @param reason the reason for the error, which can be used to inform the client about the issue
     */
    @Override
    public void onGenericError(String reason) {
        ServerMessage serverMessage = new GenericErrorMessage(reason);

        sendMessage(serverMessage);
    }

    /**
     * Called when a registration attempt is successful, providing the nickname of the registered player.
     *
     * @param nickname the nickname of the player who successfully registered, which can be used to inform the client about the successful registration and allow them to proceed with the login process or other actions.
     * @param message  the message indicating the successful registration
     */
    @Override
    public void onRegisterSuccess(String nickname, String message) {
        ServerMessage serverMessage = new RegisterSuccessMessage(nickname, message);

        sendMessage(serverMessage);
    }

    /**
     * Called when a registration attempt fails, providing the reason for the failure.
     *
     * @param reason the reason for the registration failure, which can be used to inform the client about the issue (e.g., "Nickname already taken", "Invalid input", etc.) and allow them to correct the issue and attempt registration again.
     */
    @Override
    public void onRegisterFailed(String reason, String nickname) {
        ServerMessage serverMessage = new RegisterFailedMessage(reason, nickname);

        sendMessage(serverMessage);
    }

    /**
     * Called when a disconnection attempt is successful, providing the nickname of the disconnected player.
     *
     * @param message the message indicating the successful disconnection
     */
    @Override
    public void onDisconnectionSuccess(String message) {
        ServerMessage serverMessage = new DisconnectSuccessMessage(message);

        sendMessage(serverMessage);
    }

    /**
     * Called when a player successfully leaves a game, providing a message that can be used to inform the client about the successful departure and allow them to update their view accordingly (e.g., returning to the main menu, etc.).
     *
     * @param message the message
     */
    @Override
    public void onLeftGameSuccess(String message) {
        ServerMessage serverMessage = new LeftGameSuccessMessage(message);

        sendMessage(serverMessage);
    }

    /**
     * Called when a generic informational message needs to be sent to the client, providing the information that can be used to inform the client about various events or updates (e.g., "Game will start in 30 seconds", "Player X has joined the game", etc.).
     *
     * @param info the informational message that can be used to inform the client about various events or updates, which can be displayed in the client's view to keep them informed about the current state of the game or other relevant information.
     */
    @Override
    public void onGenericInfo(String info) {
        // not implemented
    }

    /**
     * Called when the list of players in a game changes
     *
     * @param playerNicknames the list of nicknames of the players currently in the game
     */
    @Override
    public void onPlayerChanged(List<String> playerNicknames) {
        ServerMessage serverMessage = new UpdatePlayersMessage(playerNicknames);

        sendMessage(serverMessage);
    }

    /**
     * Called when the game board changes
     *
     * @param gameId the unique identifier of the game
     * @param board  the updated state of the game board
     */
    @Override
    public void onBoardChanged(int gameId, BoardDTO board) {
        ServerMessage serverMessage = new BoardUpdatedMessage(gameId, board);

        sendMessage(serverMessage);
    }

    /**
     * Called when the current era of the game changes
     *
     * @param gameId the unique identifier of the game
     * @param era    the new era of the game
     */
    @Override
    public void onEraChanged(int gameId, String era) {
        ServerMessage serverMessage = new EraChangedMessage(gameId, era);

        sendMessage(serverMessage);
    }

    /**
     * Called when the current player's turn changes during the "Place Totem" phase of the game.
     *
     * @param gameId                the unique identifier of the game
     * @param currentPlayerNickname the nickname of the player whose turn it currently is during the "Place Totem" phase.
     */
    @Override
    public void onPlaceTotemTurnChanged(int gameId, String currentPlayerNickname) {
        ServerMessage serverMessage = new PlaceTotemTurnChangedMessage(gameId, currentPlayerNickname);

        sendMessage(serverMessage);
    }

    /**
     * Called when the current player's turn changes during the "Resolve Action" phase of the game.
     *
     * @param gameId                the unique identifier of the game
     * @param currentPlayerNickname the nickname of the player whose turn it currently is during the "Resolve Action" phase.
     */
    @Override
    public void onResolveActionTurnChanged(int gameId, String currentPlayerNickname) {
        ServerMessage serverMessage = new ResolveActionTurnChangedMessage(gameId, currentPlayerNickname);

        sendMessage(serverMessage);
    }

    /**
     * Called when the current player's turn changes during the "Extra Action" phase of the game.
     *
     * @param gameId                the unique identifier of the game
     * @param currentPlayerNickname the nickname of the player whose turn it currently is during the "Extra Action" phase.
     */
    @Override
    public void onExtraActionTurnChanged(int gameId, String currentPlayerNickname) {
        ServerMessage serverMessage = new ExtraActionTurnChangedMessage(gameId, currentPlayerNickname);

        sendMessage(serverMessage);
    }

    /**
     * Called when a player's deck is updated
     *
     * @param gameId     the unique identifier of the game
     * @param nickname   the nickname of the player whose deck is updated
     * @param playerDeck the updated state of the player's deck
     */
    @Override
    public void onPlayerDeckUpdated(int gameId, String nickname, PlayerDeckDTO playerDeck) {
        ServerMessage serverMessage = new PlayerDeckUpdatedMessage(gameId, nickname, playerDeck);

        sendMessage(serverMessage);
    }

    /**
     * Called when a game ends, providing the final ranking of players in the game.
     *
     * @param gameId       the unique identifier of the game
     * @param finalRanking a map containing the final ranking of players in the game, where the key is the player's nickname and the value is their final score or position in the ranking. This information can be used to inform the client about the outcome of the game and display the final results to the players.
     */
    @Override
    public void onGameEnded(int gameId, Map<String, Integer> finalRanking, int numPlayers, List<LeadBoardDTO> historicalLeaderboard) {
        ServerMessage serverMessage = new GameEndedMessage(gameId, finalRanking, numPlayers, historicalLeaderboard);

        sendMessage(serverMessage);
    }

    /**
     * Called when a player successfully joins a game
     *
     * @param gameInfo the GameInfoMessage object containing information about the game
     */
    @Override
    public void onJoinedGame(GameInfoDTO gameInfo) {
        ServerMessage serverMessage = new GameInfoMessage(gameInfo);

        sendMessage(serverMessage);
    }

    /**
     * Called when some player stats changed
     *
     * @param gameId      the unique identifier of the game
     * @param playerInfos the list of PlayerInfoDTO objects representing the updated information about the players in the game
     */
    @Override
    public void onPlayerInfosChanged(int gameId, List<PlayerInfoDTO> playerInfos) {
        ServerMessage serverMessage = new PlayerInfoChangedMessage(gameId, playerInfos);

        sendMessage(serverMessage);
    }

    /**
     * Called when an event card is being resolved, providing the game ID and the details of the event card being resolved. This information can be used to inform the client about the ongoing resolution of an event card and allow them to update their view accordingly (e.g., displaying the effects of the event card, etc.).
     *
     * @param gameId    the unique identifier of the game
     * @param eventCard the EventCardDTO object containing the details of the event card being resolved.
     */
    @Override
    public void onResolvingEvent(int gameId, EventCardDTO eventCard) {
        ServerMessage serverMessage = new ResolvingEventMessage(gameId, eventCard);

        sendMessage(serverMessage);
    }

    /**
     * Called when the rankings are received, providing a list of LeadBoardDTO objects representing the rankings of players in the game. This information can be used to inform the client about the current standings of players in the game and allow them to update their view accordingly (e.g., displaying the rankings on a leaderboard, etc.).
     *
     * @param rankings the list of LeadBoardDTO objects representing the rankings of players in the game, which can be used to inform the client about the current standings of players in the game and allow them to update their view accordingly (e.g., displaying the rankings on a leaderboard, etc.).
     */
    @Override
    public void onRankingsReceived(List<LeadBoardDTO> rankings) {
        ServerMessage serverMessage = new RankingsMessage(rankings);

        sendMessage(serverMessage);
    }
}
