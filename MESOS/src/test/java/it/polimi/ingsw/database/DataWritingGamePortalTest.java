package it.polimi.ingsw.database;

import it.polimi.ingsw.server.database.DataWriting;
import it.polimi.ingsw.server.database.DatabaseManager;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.gamemodel.GamePortal;
import it.polimi.ingsw.server.model.menu.PlayerManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataWritingGamePortalTest {

    private static final String TEST_PASSWORD_HASH = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    private final List<Integer> createdGameIds = new ArrayList<>();
    private final List<String> createdNicknames = new ArrayList<>();

    private String hostNickname;
    private String joinerNickname;

    @BeforeEach
    void setUp() throws SQLException {
        String suffix = String.valueOf(System.nanoTime());
        hostNickname = "test_host_" + suffix;
        joinerNickname = "test_joiner_" + suffix;
    }

    @AfterEach
    void tearDown() throws SQLException {
        Connection c = DatabaseManager.getConnection();

        try (PreparedStatement deleteGamePlayer = c.prepareStatement("DELETE FROM game_player WHERE game_id = ?")) {
            for (Integer gameId : createdGameIds) {
                deleteGamePlayer.setInt(1, gameId);
                deleteGamePlayer.executeUpdate();
            }
        }

        try (PreparedStatement deleteGame = c.prepareStatement("DELETE FROM game WHERE game_id = ?")) {
            for (Integer gameId : createdGameIds) {
                deleteGame.setInt(1, gameId);
                deleteGame.executeUpdate();
            }
        }

        try (PreparedStatement deletePlayer = c.prepareStatement("DELETE FROM player WHERE nickname = ?")) {
            for (String nickname : createdNicknames) {
                deletePlayer.setString(1, nickname);
                deletePlayer.executeUpdate();
            }
        }
    }

    @Test
    void addNewGameShouldInsertGameAndHostLink() throws SQLException {
        insertPlayer(hostNickname);

        DataWriting dataWriting = new DataWriting();
        int gameId = dataWriting.addNewGame(hostNickname);

        assertTrue(gameId > 0, "addNewGame deve restituire il game_id generato dal DB.");
        createdGameIds.add(gameId);

        assertTrue(gameExists(gameId), "La riga su tabella game deve esistere.");
        assertTrue(gamePlayerLinkExists(gameId, hostNickname), "L'host deve essere collegato in game_player.");
    }

    @Test
    void gamePortalCreateAndJoinGameShouldPersistBothPlayers() throws Exception {
        insertPlayer(hostNickname);
        insertPlayer(joinerNickname);

        GamePortal portal = new GamePortal();
        addLoggedInPlayer(portal, hostNickname);
        addLoggedInPlayer(portal, joinerNickname);

        int gameId = portal.createGame(hostNickname, null);
        assertTrue(gameId > 0, "createGame deve restituire un id DB valido.");
        createdGameIds.add(gameId);

        assertTrue(gameExists(gameId), "createGame deve creare la riga su game.");
        assertTrue(gamePlayerLinkExists(gameId, hostNickname), "createGame deve collegare l'host su game_player.");

        portal.joinGame(gameId, joinerNickname, null);

        assertTrue(gamePlayerLinkExists(gameId, joinerNickname), "joinGame deve inserire il collegamento player-game nel DB.");
        assertEquals(2, portal.getGame(gameId).getNumberOfPlayers(), "Nel model il player deve risultare aggiunto alla partita.");
    }

    private void insertPlayer(String nickname) throws SQLException {
        Connection c = DatabaseManager.getConnection();
        String sql = "INSERT INTO player (nickname, password_hash) VALUES (?, ?) ON CONFLICT (nickname) DO NOTHING";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nickname);
            ps.setString(2, TEST_PASSWORD_HASH);
            ps.executeUpdate();
        }

        createdNicknames.add(nickname);
    }

    private boolean gameExists(int gameId) throws SQLException {
        Connection c = DatabaseManager.getConnection();
        try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM game WHERE game_id = ?")) {
            ps.setInt(1, gameId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean gamePlayerLinkExists(int gameId, String nickname) throws SQLException {
        Connection c = DatabaseManager.getConnection();
        String sql = "SELECT 1 FROM game_player gp JOIN player p ON gp.player_id = p.player_id WHERE gp.game_id = ? AND p.nickname = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, gameId);
            ps.setString(2, nickname);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void addLoggedInPlayer(GamePortal portal, String nickname) throws Exception {
        Field playerManagerField = GamePortal.class.getDeclaredField("playerManager");
        playerManagerField.setAccessible(true);
        PlayerManager playerManager = (PlayerManager) playerManagerField.get(portal);
        playerManager.addPlayer(new Player(nickname));
    }
}

