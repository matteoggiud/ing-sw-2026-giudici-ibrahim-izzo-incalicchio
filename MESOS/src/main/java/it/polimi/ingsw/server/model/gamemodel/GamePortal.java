package it.polimi.ingsw.server.model.gamemodel;

import it.polimi.ingsw.server.database.DataDepository;
import it.polimi.ingsw.server.database.DataWriting;
import it.polimi.ingsw.server.model.board.CardRow;
import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.menu.GameManager;
import it.polimi.ingsw.server.model.menu.PlayerManager;
import it.polimi.ingsw.server.model.observer.IGameObserver;
import it.polimi.ingsw.common.network.dto.GameInfoDTO;
import it.polimi.ingsw.common.network.dto.LeadBoardDTO;
import it.polimi.ingsw.common.network.dto.mapper.BoardDTOMapper;
import it.polimi.ingsw.common.network.dto.mapper.GameInfoDTOMapper;
import it.polimi.ingsw.common.network.dto.mapper.PlayerInfosDTOMapper;
import it.polimi.ingsw.server.model.observer.event.BoardUpdatedEvent;
import it.polimi.ingsw.server.model.observer.event.GameStartedEvent;
import it.polimi.ingsw.server.model.observer.event.JoinedGameEvent;
import it.polimi.ingsw.server.model.observer.event.PlayerInfosChangedEvent;
import it.polimi.ingsw.server.model.ruleset.StandardRuleSet;
import it.polimi.ingsw.server.network.EventHandler;

import java.util.*;

import java.sql.SQLException;

/**
 * The GamePortal class serves as the main entry point for interacting with the game model.
 * It implements the IGameModel interface, providing concrete implementations for user authentication and game actions such as drawing cards and placing totems.
 *
 */
public class GamePortal implements IGameModel {

    /**
     * The PlayerManager instance used for managing player-related operations such as login, logout, and player retrieval.
     */
    private final PlayerManager playerManager;

    /**
     * The GameManager instance used for managing game-related operations such as creating games, joining games, and retrieving game information.
     */
    private final GameManager gameManager;


    /**
     * Constructor for the GamePortal class, which initializes the player manager and game manager instances.
     */
    public GamePortal() {
        this.playerManager = PlayerManager.getInstance();
        this.gameManager = GameManager.getInstance();
    }


    /**
     * Allows a player to draw a card from either the upper or lower row of the game board.
     * @param gameId         the current game context
     * @param nickname       the player attempting to draw the card
     * @param selectedCardId the card the player has selected to draw
     * @param fromUpperRow   a boolean indicating whether the card is being drawn from the upper row (true) or lower row (false)
     */
    @Override
    public void drawCard(int gameId, String nickname, int selectedCardId, boolean fromUpperRow) {
        if (gameId < 0) {
            throw new IllegalArgumentException("Game id cannot be negative.");
        }
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Nickname cannot be null or blank.");
        }

        Game currentGame = gameManager.getGame(gameId);
        Player currentPlayer = playerManager.getPlayer(nickname);

        if (currentPlayer == null) {
            throw new IllegalStateException("Player with nickname " + nickname + " is not logged in.");
        }

        CardRow row = fromUpperRow
                ? currentGame.getBoard().getUpperRow()
                : currentGame.getBoard().getLowerRow();

        Card card = row.getSelectableCardById(selectedCardId);
        currentGame.getGameState().resolveAction(currentGame, currentPlayer, card, fromUpperRow);
    }

    /**
     * Allows a player to place their totem on a specific offer box position on the offer track.
     *
     * @param gameId           the unique identifier of the game in which the player is placing their totem
     * @param nickname         the nickname of the player placing their totem
     * @param offerBoxPosition the 1-based position of the offer box on which the player wants to place their totem
     */
    @Override
    public void placing(int gameId, String nickname, int offerBoxPosition) {
        if (gameId < 0) {
            throw new IllegalArgumentException("Game id cannot be negative.");
        }
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Nickname cannot be null or blank.");
        }

        Game currentGame = gameManager.getGame(gameId);
        Player player = playerManager.getPlayer(nickname);

        if (player == null) {
            throw new IllegalStateException("Player with nickname " + nickname + " is not logged in.");
        }

        currentGame.getGameState().placeTotem(
                currentGame,
                player,
                currentGame.getBoard().getOfferTrack().getBoxByPosition(offerBoxPosition)
        );
    }


    /**
     * Creates a new game with the specified host player.
     *
     * @param hostNickname the player who will host the new game
     * @return the unique identifier (UUID) of the newly created game
     */
    @Override
    public int createGame(String hostNickname, IGameObserver observer) {
        return createGame(hostNickname, StandardRuleSet.MIN_PLAYERS, observer);
    }

    /**
     * Creates a new game with the specified host player and expected player count.
     *
     * @param hostNickname        the player who will host the new game
     * @param expectedPlayerCount the expected number of players for the game
     * @param observer            the observer associated with the host player, which will be added to the game's observers list
     * @return the unique identifier of the newly created game
     */
    @Override
    public int createGame(String hostNickname, int expectedPlayerCount, IGameObserver observer) {
        if (expectedPlayerCount < StandardRuleSet.MIN_PLAYERS || expectedPlayerCount > StandardRuleSet.MAX_PLAYERS) {
            throw new IllegalArgumentException("Expected player count must be between "
                    + StandardRuleSet.MIN_PLAYERS + " and " + StandardRuleSet.MAX_PLAYERS);
        }

        DataWriting dw = new DataWriting();
        Player host = playerManager.getPlayer(hostNickname);

        if (host == null) {
            throw new IllegalStateException("Host player with nickname " + hostNickname + " does not exist.");
        }

        int gameId = dw.addNewGame(hostNickname, expectedPlayerCount);
        gameManager.createGame(host, gameId, expectedPlayerCount);

        // Add the observer for the host player to the newly created game
        addObserver(gameManager.getGame(gameId), observer);

        // Notify the host player's observer about the game creation so they can update their lobby view
        Game game = gameManager.getGame(gameId);
        game.getObserverManager().notifyObservers(new JoinedGameEvent(GameInfoDTOMapper.toGameInfoDTO(game)));

        return gameId;
    }

    /**
     * Checks if the game with the specified id is ready to start.
     * @param gameId the unique identifier of the game to check and potentially start
     */
    @Override
    public void startGameIfReady(int gameId) {
        if (gameId < 0) {
            throw new IllegalArgumentException("Game id cannot be negative.");
        }

        Game game = gameManager.getGame(gameId);

        if (game.getNumberOfPlayers() == game.getExpectedPlayerCount()) {
            game.getGameState().startGame(game);
        }
    }


    /**
     * Retrieves an active game by its unique identifier.
     *
     * @param gameId the unique identifier of the game
     * @return the game associated with the given id
     * @throws IllegalArgumentException if the game id is null or no game exists with that id
     */
    @Override
    public Game getGame(int gameId) {
        return gameManager.getGame(gameId);
    }



    /**
     * Gets all available games that players can join.
     *
     * @return a list of available games
     */
    @Override
    public List<GameInfoDTO> getAvailableGames() {
        return GameInfoDTOMapper.toGameInfoDTOs(gameManager.getAvailableGames());
    }

    /**
     * Allows a player to join an existing game using the game's unique identifier (UUID).
     *
     * @param gameId the unique identifier of the game the player wants to join
     * @param player the player attempting to join the game
     */
    @Override
    public void joinGame(int gameId, String player, IGameObserver observer) {
        Player playerToJoin = playerManager.getPlayer(player);

        if (playerToJoin == null) {
            throw new IllegalStateException("Player with nickname " + player + " does not exist.");
        }

        gameManager.joinGame(gameId, playerToJoin);

        // Add the observer for the player to the newly joined game
        addObserver(gameManager.getGame(gameId), observer);

        // Notify the player's observer about the join so they can update their lobby view
        Game game = gameManager.getGame(gameId);
        game.getObserverManager().notifyObservers(new JoinedGameEvent(GameInfoDTOMapper.toGameInfoDTO(game)));

    }

    /**
     * Allows a player to leave a game they have joined using the game's unique identifier (UUID).
     *
     * @param gameId the unique identifier of the game the player wants to leave
     * @param player the player attempting to leave the game
     */
    @Override
    public List<String> leaveGame(int gameId, String player) {
        if (playerManager.getPlayer(player) == null) {
            throw new IllegalStateException("Player with nickname " + player + " does not exist.");
        }

        // We first retrieve the game
        Game game = gameManager.getGame(gameId);

        // We save the players remaining in the game after the player leaves
        List<String> playerNames = game.getPlayers().getPlayerNicknames();
        playerNames = game.getPlayers().getPlayerNicknames().stream().filter(p -> !p.equals(player)).toList();


        // We then execute the remove game logic, which deletes the game
        gameManager.removeGame(gameId);

        // Update db
        new DataWriting().updateGameInProgress(gameId, false);


        return playerNames;
    }

    /**
     * Attempts to log in a user with the provided nickname and passwordHash.
     *
     * @param nickname the nickname of the user trying to log in
     * @param password the passwordHash of the user trying to log in
     * @throws SQLException if there is an error accessing the database or if the login credentials are invalid
     */
    @Override
    public void login(String nickname, String password) throws SQLException {
        Authenticator db = new Authenticator();

        if (nickname == null || password == null) {
            throw new IllegalArgumentException("Nickname and passwordHash cannot be null");
        }

        Player existingPlayer = playerManager.getPlayer(nickname);

        if (existingPlayer != null) {
            if (existingPlayer.isConnected()) {
                throw new IllegalStateException("Nickname is already connected.");
            }

            boolean successful = db.loginUser(nickname, password);
            if (!successful) {
                throw new IllegalArgumentException("Invalid nickname or passwordHash");
            }

            existingPlayer.reconnect();
            return;
        }

        boolean successful = db.loginUser(nickname, password);
        if (successful) {
            playerManager.addPlayer(new Player(nickname));
        } else {
            throw new IllegalArgumentException("Invalid nickname or passwordHash");
        }
    }


    /**
     * Logs out the user with the specified nickname.
     *
     * @param nickname the nickname of the user to log out
     * @throws IllegalArgumentException if the nickname is null
     */
    @Override
    public void logout(String nickname) throws IllegalArgumentException {
        if (nickname == null) {
            throw new IllegalArgumentException("Nickname cannot be null");
        }

        Player player = playerManager.getPlayer(nickname);
        if (player != null) {
            player.disconnect();
        }
        playerManager.removePlayer(nickname);
    }

    /**
     * Retrieves a currently logged-in player by nickname.
     *
     * @param nickname the nickname of the player to retrieve
     * @return the player associated with the given nickname
     * @throws IllegalArgumentException if the nickname is null
     * @throws IllegalStateException    if no logged-in player exists with that nickname
     */
    @Override
    public Player getLoggedPlayer(String nickname) {
        if (nickname == null) {
            throw new IllegalArgumentException("Nickname cannot be null.");
        }

        Player player = playerManager.getPlayer(nickname);
        if (player == null) {
            throw new IllegalStateException("Player with nickname " + nickname + " is not logged in.");
        }

        return player;
    }

    /**
     * Checks whether a player is currently logged in.
     *
     * @param nickname the nickname of the player to check
     * @return true if the player is logged in, false otherwise
     * @throws IllegalArgumentException if the nickname is null
     */
    @Override
    public boolean isLoggedIn(String nickname) {
        if (nickname == null) {
            throw new IllegalArgumentException("Nickname cannot be null.");
        }

        return playerManager.isLoggedIn(nickname);
    }


    /**
     * Attempts to register a new user with the provided nickname and passwordHash.
     *
     * @param nickname the nickname of the user trying to register
     * @param password the passwordHash of the user trying to register
     * @throws SQLException if there is an error accessing the database or if the registration fails
     */
    @Override
    public void register(String nickname, String password) throws SQLException {
        boolean successfull = false;
        Authenticator db = new Authenticator();
        if (nickname == null || password == null) {
            throw new IllegalArgumentException("Nickname and passwordHash cannot be null");
        }
        successfull = db.registerUser(nickname, password);
        if (!successfull) {
            throw new IllegalArgumentException("Registration failed");
        } else {
            login(nickname, password);
        }
    }

    /**
     * Retrieves the final ranking of a game when the game has reached
     * the scoring phase.
     *
     * @param gameId the unique identifier of the game
     * @return a map of players and their final scores sorted by ranking
     * @throws IllegalArgumentException if the game id is null
     * @throws IllegalStateException    if the game is not in a phase that exposes the final ranking yet
     */
    @Override
    public Map<Player, Integer> getFinalRanking(int gameId) {
        if (gameId < 0) {
            throw new IllegalArgumentException("Game id cannot be null.");
        }

        Game game = gameManager.getGame(gameId);
        return game.getFinalRanking();
    }


    /**
     * Adds a virtual view as an observer to the game with the specified id, allowing the virtual view to receive updates about the game's state and events.
     *
     * @param game        the game to which the virtual view will be added as an observer
     * @param virtualView the virtual view to be added as an observer to the specified game
     */
    @Override
    public void addObserver(Game game, IGameObserver virtualView) {
        if (game == null) {
            throw new IllegalArgumentException("Game id cannot be null.");
        }
        if (virtualView == null) {
            throw new IllegalArgumentException("Virtual view cannot be null.");
        }

        game.getObserverManager().addObserver(virtualView);
    }

    /**
     * Removes a virtual view as an observer from the game with the specified id, preventing the virtual view from receiving further updates about the game's state and events.
     *
     * @param game        the unique identifier of the game from which the virtual view will be removed as an observer
     * @param virtualView the virtual view to be removed as an observer from the specified game
     */
    @Override
    public void removeObserver(Game game, IGameObserver virtualView) {
        if (game == null) {
            throw new IllegalArgumentException("Game id cannot be null.");
        }
        if (virtualView == null) {
            throw new IllegalArgumentException("Virtual view cannot be null.");
        }

        game.getObserverManager().removeObserver(virtualView);
    }

    /**
     * Marks a player as reconnected in the game they are currently participating in, if any.
     *
     * @param nickname the nickname of the player to be marked as reconnected
     * @return the unique identifier of the game in which the player is participating
     */
    @Override
    public Integer getActiveGameIdForPlayer(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Nickname cannot be null or blank.");
        }

        Optional<Integer> gameId = gameManager.findGameIdByPlayerNickname(nickname);
        return gameId.orElse(null);
    }

    /**
     * Retrieves the leaderboard ranking for a specified number of players.
     * @param numPlayer the number of players for which to retrieve the leaderboard ranking.
     * @return a list of LeadBoardDTO objects representing the leaderboard ranking for the specified number of players.
     */
    @Override
    public List<LeadBoardDTO> getRanking(int numPlayer) {
        if (numPlayer < 2 || numPlayer > 5) {
            throw new IllegalArgumentException("Number of players must be between 2 and 5.");
        }
        DataDepository db = new DataDepository();
        return db.getLeadBoard(numPlayer);
    }

    /**
     * Allows a player to leave the lobby of a game they have joined using the game's unique identifier.
     * @param player the nickname of the player attempting to leave the lobby
     * @param gameId the unique identifier of the game whose lobby the player wants to leave
     * @return a list of the remaining players' nicknames in the lobby after the player leaves
     */
    @Override
    public List<String> leaveLobby(String player, int gameId) {
        if (gameId < 0) {
            throw new IllegalArgumentException("Game id cannot be negative.");
        }

        Player leavingPlayer = playerManager.getPlayer(player);

        if (leavingPlayer == null) {
            throw new IllegalArgumentException("Player with nickname " + player + " is not logged in.");
        }

        // We first retrieve the game and the players in the game, then we save the players remaining in the game after the player leaves
        List<String> playerNames = new ArrayList<>();

        // If the leaving player is the host, we need to save the names of the other players before deleting the game
        if (player.equals(gameManager.getGame(gameId).getHostPlayer().getNickname())) {
            playerNames = gameManager.getGame(gameId).getPlayers().getPlayerNicknames().stream()
                    .filter(p -> !p.equals(player))
                    .toList();
        }

        Game game = gameManager.getGame(gameId);

        if (!game.getGameState().isJoinable(game)) {
            throw new IllegalStateException("leaveLobby is allowed only while game is in lobby.");
        }

        // Leave the game and if he's the host delete the game from the game manager
        gameManager.leaveGame(gameId, leavingPlayer);

        return playerNames;

    }

    /**
     * Restores a game from a previously saved state, allowing players to resume a game that was interrupted or paused.
     *
     * @param restoredGame the game object representing the restored game state.
     */
    @Override
    public void restoreGame(Game restoredGame) {
        gameManager.restoreGame(restoredGame);

        for (Player player : restoredGame.getPlayers().getPlayers()) {
            if (!playerManager.isLoggedIn(player.getNickname())) {
                playerManager.addPlayer(player);
            }
        }

        restoredGame.getGameState().executePhase(restoredGame);
    }

    /**
     * Allows a player to reconnect to an ongoing game by providing the game's unique identifier.
     * @param gameId the unique identifier of the game to which the player wants to reconnect.
     * @param username the nickname of the player attempting to reconnect to the game.
     * @param eventHandler the event handler associated with the player's connection.
     */
    @Override
    public void reconnectToGame(Integer gameId, String username, EventHandler eventHandler) {
        Game game = gameManager.getGame(gameId);
        Player player = playerManager.getPlayer(username);

        addObserver(game, eventHandler);


        // 1. We tell the client that the game has started, so it switches to the Board screen
        eventHandler.update(new GameStartedEvent(game));

        // 2. We send player info FIRST (so the client knows who the Totems belong to and what color they are!)
        eventHandler.update(new PlayerInfosChangedEvent(
                gameId,
                PlayerInfosDTOMapper.toPlayerInfoDTOList(game.getPlayers().getPlayers())
        ));

        // 3. THEN we send him the current state of the Board, so when he renders the Turn Order Track the Totems will have the right color
        eventHandler.update(new BoardUpdatedEvent(
                gameId,
               BoardDTOMapper.toBoardDTO(game)
        ));

        game.getGameState().addPlayer(game, player);

        // N.B: Il turno esatto verrà notificato automaticamente a TUTTI i client
        // non appena WaitingForReconnectState scatta su nextState()!
    }
}
