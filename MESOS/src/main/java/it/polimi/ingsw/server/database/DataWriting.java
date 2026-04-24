package it.polimi.ingsw.server.database;

import it.polimi.ingsw.server.model.core.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataWriting {

    /**
     * Crea un nuovo game vuoto e collega l'host nella tabella game_player.
     * @param game game da persistere (serve solo per ricavare nickname host)
     * @return game_id generato dal DB, oppure -1 se fallisce
     */
    public int addNewGame(Game game) {
        String insertGameSql = "INSERT INTO game () VALUES ()";
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
                    return -1;
                }

                try (ResultSet rs = psGame.getGeneratedKeys()) {
                    if (!rs.next()) {
                        c.rollback();
                        return -1;
                    }
                    generatedGameId = rs.getInt(1);
                }
            }


            try (PreparedStatement psHost = c.prepareStatement(insertHostLinkSql)) {
                psHost.setInt(1, generatedGameId);
                psHost.setString(2, game.getHostPlayer().getNickname());

                int rowsHost = psHost.executeUpdate();
                if (rowsHost == 0) {
                    c.rollback();
                    return -1;
                }
            }

            c.commit();
            return generatedGameId;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error adding new game to the database");
            return -1;
        }
    }
}