package it.polimi.ingsw.server.database;

import it.polimi.ingsw.server.model.core.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataWriting {

    /**
     * Creates a new game in the database and links it to the host player specified by their nickname
     * @param hostNickname the nickname of the player who will host the new game
     * @return the unique identifier of the newly created game
     */
    public int addNewGame(String hostNickname) {
        String insertGameSql = "INSERT INTO game DEFAULT VALUES";
        String insertHostLinkSql = "INSERT INTO game_player (game_id, player_id) " +
                "VALUES (?, (SELECT player_id FROM player WHERE nickname = ?))";

        try (Connection c = DatabaseManager.getConnection()) {
            c.setAutoCommit(false); // create transaction

            int generatedGameId;

            // 1) crea game e recupera SERIAL game_id
            try (PreparedStatement psGame = c.prepareStatement(insertGameSql, Statement.RETURN_GENERATED_KEYS)) {

                int rowsAffected = psGame.executeUpdate();
                if (rowsAffected == 0) {
                    c.rollback();
                    throw new SQLException("Creating game failed.");
                }

                try (ResultSet rs = psGame.getGeneratedKeys()) {
                    if (!rs.next()) {
                        c.rollback();
                        throw new SQLException("Creating game failed.");
                    }
                    generatedGameId = rs.getInt(1);
                }
            }


            try (PreparedStatement psHost = c.prepareStatement(insertHostLinkSql)) {
                psHost.setInt(1, generatedGameId);
                psHost.setString(2, hostNickname);

                int rowsHost = psHost.executeUpdate();
                if (rowsHost == 0) {
                    c.rollback();
                    throw new SQLException("Linking host to game failed.");
                }
            }

            c.commit();
            return generatedGameId;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinExistingGame(int gameId, String playerNickname) {
        String insertLinkSql = "INSERT INTO game_player (game_id, player_id) VALUES (?, (SELECT player_id FROM player WHERE nickname = ?))";

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(insertLinkSql)) {

            ps.setInt(1, gameId);
            ps.setString(2, playerNickname);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Joining game failed.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}