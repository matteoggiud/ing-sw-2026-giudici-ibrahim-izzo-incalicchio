package it.polimi.ingsw.server.model.gamemodel;

import it.polimi.ingsw.server.database.DataWriting;
import it.polimi.ingsw.server.model.board.CardRow;
import it.polimi.ingsw.server.model.card.Card;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.menu.GameManager;
import it.polimi.ingsw.server.model.menu.PlayerManager;
import it.polimi.ingsw.server.model.observer.IGameObserver;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;
import it.polimi.ingsw.server.model.observer.dto.mapper.GameInfoDTOMapper;
import it.polimi.ingsw.server.model.observer.event.JoinedGameEvent;
import it.polimi.ingsw.server.model.ruleset.StandardRuleSet;

import java.util.*;

import java.sql.SQLException;

/**
 * The GamePortal class serves as the main entry point for interacting with the game model.
 * It implements the IGameModel interface, providing concrete implementations for user authentication and game actions such as drawing cards and placing totems.
 *
 */
public class GamePortal implements IGameModel {

    private final PlayerManager playerManager;

    private final GameManager gameManager;

    public GamePortal() {
        this.playerManager = new PlayerManager();
        this.gameManager = new GameManager();
    }


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

        int gameId = dw.addNewGame(hostNickname); //TODO: add also expected player count
        gameManager.createGame(host, gameId, expectedPlayerCount);

        // Add the observer for the host player to the newly created game
        addObserver(gameManager.getGame(gameId), observer);

        // Notify the host player's observer about the game creation so they can update their lobby view
        Game game = gameManager.getGame(gameId);
        game.getObserverManager().notifyObservers(new JoinedGameEvent(GameInfoDTOMapper.toGameInfoDTO(game)));

        return gameId;
    }

    @Override
    public void startGameIfReady(int gameId) {
        if (gameId < 0) {
            throw new IllegalArgumentException("Game id cannot be negative.");
        }

        Game game = gameManager.getGame(gameId);

        if (game.getNumberOfPlayers() == game.getExpectedPlayerCount()) {
            game.getGameState().startGame(game);
            game.getGameState().executePhase(game);
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
     * Starts an existing game.
     * Only the host player can start the game, and the game must have enough players.
     * Starting the game first moves it from LobbyState to SetupState, then executes
     * the setup phase so the board, decks, turn order, and totems are initialized.
     *
     * @param gameId       the unique identifier of the game to start
     * @param hostNickname the nickname of the player requesting the start
     * @throws IllegalArgumentException if any argument is null
     * @throws IllegalStateException    if the requester is not the host or there are not enough players
     */
    @Override
    public void startGame(int gameId, String hostNickname) {
        if (gameId < 0) {
            throw new IllegalArgumentException("Game id cannot be null.");
        }
        if (hostNickname == null) {
            throw new IllegalArgumentException("Host nickname cannot be null.");
        }

        Game game = gameManager.getGame(gameId);

        if (!game.getHostPlayer().getNickname().equals(hostNickname)) {
            throw new IllegalStateException("Only the host can start the game.");
        }

        if (game.getNumberOfPlayers() < StandardRuleSet.MIN_PLAYERS) {
            throw new IllegalStateException("Not enough players to start the game.");
        }

        game.getGameState().startGame(game);
        game.getGameState().executePhase(game);
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
        DataWriting dw = new DataWriting();
        if (playerToJoin == null) {
            throw new IllegalStateException("Player with nickname " + player + " does not exist.");
        }
        dw.joinExistingGame(gameId, player);
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
    public void leaveGame(int gameId, String player, IGameObserver observer) {
        Player playerToLeave = playerManager.getPlayer(player);
        if (playerToLeave == null) {
            throw new IllegalStateException("Player with nickname " + player + " does not exist.");
        }
        Game game = gameManager.getGame(gameId);
        gameManager.leaveGame(gameId, playerToLeave);
        removeObserver(game, observer);


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
     * Checks whether the specified player has a pending extra action in the given game.
     *
     * @param gameId   the unique identifier of the game
     * @param nickname the nickname of the player to check
     * @return true if the player has a pending extra action, false otherwise
     * @throws IllegalArgumentException if any argument is null or invalid
     * @throws IllegalStateException    if the player is not logged in
     */
    @Override
    public boolean hasPendingExtraAction(int gameId, String nickname) {
        if (gameId < 0) {
            throw new IllegalArgumentException("Game id cannot be null.");
        }
        if (nickname == null) {
            throw new IllegalArgumentException("Nickname cannot be null.");
        }

        Game game = gameManager.getGame(gameId);

        Player player = playerManager.getPlayer(nickname);
        if (player == null) {
            throw new IllegalStateException("Player with nickname " + nickname + " is not logged in.");
        }

        return game.getGameState().getExtraAction(player);
    }

    /**
     * Retrieves the final ranking of a game using player nicknames as keys.
     * This representation is more suitable for TUI and controller code.
     *
     * @param gameId the unique identifier of the game
     * @return a map of player nicknames and their final scores sorted by ranking
     * @throws IllegalArgumentException if the game id is null
     * @throws IllegalStateException    if the game is not in a phase that exposes the final ranking yet
     */
    @Override
    public Map<String, Integer> getFinalRankingByNickname(int gameId) {
        if (gameId < 0) {
            throw new IllegalArgumentException("Game id cannot be null.");
        }

        Map<Player, Integer> ranking = getFinalRanking(gameId);
        Map<String, Integer> rankingByNickname = new LinkedHashMap<>();

        for (Map.Entry<Player, Integer> entry : ranking.entrySet()) {
            rankingByNickname.put(entry.getKey().getNickname(), entry.getValue());
        }

        return rankingByNickname;
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
     * Marks a player as disconnected in the specified game
     *
     * @param gameId   the unique identifier of the game in which the player is participating
     * @param nickname the nickname of the player to be marked as disconnected
     */
    @Override
    public void markPlayerDisconnected(int gameId, String nickname) {
        if (gameId < 0) {
            throw new IllegalArgumentException("Game id cannot be negative.");
        }
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Nickname cannot be null or blank.");
        }

        Game game = gameManager.getGame(gameId);

        Player player = game.getPlayers().getPlayers().stream()
                .filter(p -> p.getNickname().equals(nickname))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Player " + nickname + " is not in game " + gameId + "."));

        player.disconnect();
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

}
