package it.polimi.ingsw.database;

import it.polimi.ingsw.server.database.DatabaseManager;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.gamemodel.GamePortal;
import it.polimi.ingsw.server.model.menu.GameManager;
import it.polimi.ingsw.server.model.menu.PlayerManager;
import it.polimi.ingsw.server.model.observer.IGameObserver;
import it.polimi.ingsw.server.model.observer.event.*;
import it.polimi.ingsw.common.network.socket_messages.server.game.ResolvingEventUpdate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class DataWritingGamePortalTest {

    private static final Logger LOGGER = Logger.getLogger(DataWritingGamePortalTest.class.getName());
    private static final String TEST_PASSWORD_HASH = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    private final List<Integer> createdGameIds = new ArrayList<>();
    private final List<String> createdNicknames = new ArrayList<>();

    private String hostNickname;
    private String joinerNickname;

    @BeforeEach
    void setUp() {
        String suffix = String.valueOf(System.nanoTime());
        hostNickname = "test_host_" + suffix;
        joinerNickname = "test_joiner_" + suffix;

        // Create players directly in database
        createPlayerInDatabase(hostNickname);
        createPlayerInDatabase(joinerNickname);

        // Add players to PlayerManager
        Player host = new Player(hostNickname);
        Player joiner = new Player(joinerNickname);
        PlayerManager.getInstance().addPlayer(host);
        PlayerManager.getInstance().addPlayer(joiner);

        createdNicknames.add(hostNickname);
        createdNicknames.add(joinerNickname);
    }

    @AfterEach
    void tearDown() {
        Connection c = DatabaseManager.getConnection();
        if (c == null) {
            return;
        }

        // Clean up game_player records
        try (PreparedStatement deleteGamePlayer = c.prepareStatement("DELETE FROM game_player WHERE game_id = ?")) {
            for (Integer gameId : createdGameIds) {
                deleteGamePlayer.setInt(1, gameId);
                deleteGamePlayer.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.warning("Error deleting game_player: " + e.getMessage());
        }

        // Clean up game records
        try (PreparedStatement deleteGame = c.prepareStatement("DELETE FROM game WHERE game_id = ?")) {
            for (Integer gameId : createdGameIds) {
                deleteGame.setInt(1, gameId);
                deleteGame.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.warning("Error deleting game: " + e.getMessage());
        }

        // Clean up player records
        try (PreparedStatement deletePlayer = c.prepareStatement("DELETE FROM player WHERE nickname = ?")) {
            for (String nickname : createdNicknames) {
                deletePlayer.setString(1, nickname);
                deletePlayer.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.warning("Error deleting player: " + e.getMessage());
        }

        // Remove from PlayerManager
        for (String nickname : createdNicknames) {
            PlayerManager.getInstance().removePlayer(nickname);
        }

        // Clear GameManager
        for (Integer gameId : createdGameIds) {
            try {
                GameManager.getInstance().removeGame(gameId);
            } catch (Exception e) {
                LOGGER.warning("Error removing game: " + e.getMessage());
            }
        }
    }

    @Test
    void testCreateGameSuccessfully() {
        GamePortal portal = new GamePortal();
        IGameObserver mockObserver = createMockObserver();

        // Create game - should not throw any exception
        int gameId = portal.createGame(hostNickname, mockObserver);
        createdGameIds.add(gameId);

        // Verify game was created in memory
        assertNotNull(GameManager.getInstance().getGame(gameId),
                "Game must exist in GameManager after creation.");

        // Verify game host is correct
        assertEquals(hostNickname,
                GameManager.getInstance().getGame(gameId).getHostPlayer().getNickname(),
                "Host player nickname must match.");
    }

    @Test
    void testJoinGameSuccessfully() {
        GamePortal portal = new GamePortal();
        IGameObserver mockObserver = createMockObserver();

        // Create game
        int gameId = portal.createGame(hostNickname, mockObserver);
        createdGameIds.add(gameId);

        // Join game - should not throw any exception
        portal.joinGame(gameId, joinerNickname, mockObserver);

        // Verify player was added to game in memory
        boolean playerInGame = GameManager.getInstance().getGame(gameId).getPlayers().getPlayers().stream()
                .anyMatch(p -> p.getNickname().equals(joinerNickname));

        assertTrue(playerInGame,
                "Joiner must be in game after joining.");
    }

    @Test
    void testGameCreationWithMultiplePlayers() {
        GamePortal portal = new GamePortal();
        IGameObserver mockObserver = createMockObserver();

        // Create game
        int gameId = portal.createGame(hostNickname, mockObserver);
        createdGameIds.add(gameId);

        // Join game
        portal.joinGame(gameId, joinerNickname, mockObserver);

        // Verify both players are in the game
        int playerCount = GameManager.getInstance().getGame(gameId).getNumberOfPlayers();
        assertEquals(2, playerCount,
                "Game must have 2 players after host creates and joiner joins.");

        // Verify specific players
        boolean hostInGame = GameManager.getInstance().getGame(gameId).getPlayers().getPlayers().stream()
                .anyMatch(p -> p.getNickname().equals(hostNickname));
        boolean joinerInGame = GameManager.getInstance().getGame(gameId).getPlayers().getPlayers().stream()
                .anyMatch(p -> p.getNickname().equals(joinerNickname));

        assertTrue(hostInGame, "Host must be in game.");
        assertTrue(joinerInGame, "Joiner must be in game.");
    }

    private void createPlayerInDatabase(String nickname) {
        try {
            Connection c = DatabaseManager.getConnection();
            if (c == null) {
                return;
            }

            String sql = "INSERT INTO player (nickname, password_hash) VALUES (?, ?) ON CONFLICT (nickname) DO NOTHING";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, nickname);
                ps.setString(2, TEST_PASSWORD_HASH);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.warning("Error creating player: " + e.getMessage());
        }
    }

    private IGameObserver createMockObserver() {
        return new IGameObserver() {
            @Override
            public void update(GameStartedEvent event) {
                // Mock implementation
            }

            @Override
            public void update(PlayersChangedEvent event) {
                // Mock implementation
            }

            @Override
            public void update(BoardUpdatedEvent event) {
                // Mock implementation
            }

            @Override
            public void update(EraChangedEvent event) {
                // Mock implementation
            }

            @Override
            public void update(PlaceTotemTurnChangedEvent event) {
                // Mock implementation
            }

            @Override
            public void update(ResolveActionTurnChangedEvent event) {
                // Mock implementation
            }

            @Override
            public void update(ExtraActionTurnChangedEvent event) {
                // Mock implementation
            }

            @Override
            public void update(PlayerResourcesUpdatedEvent event) {
                // Mock implementation
            }

            @Override
            public void update(PlayerDeckUpdatedEvent event) {
                // Mock implementation
            }

            @Override
            public void update(GameEndedEvent event) {
                // Mock implementation
            }

            @Override
            public void update(JoinedGameEvent event) {
                // Mock implementation
            }

            @Override
            public void update(PlayerInfosChangedEvent event) {
                // Mock implementation
            }

            @Override
            public void update(ResolvingEventUpdate resolvingEventUpdate) {
                // Mock implementation
            }
        };
    }
}
