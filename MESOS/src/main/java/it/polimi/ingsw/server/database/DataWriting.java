package it.polimi.ingsw.server.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.gamelogic.ScoreCalculator;
import it.polimi.ingsw.server.model.observer.dto.GameSnapshotDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DataWriting {

    /**
     * Creates a new game in the database with the specified host nickname and expected player count.
     *
     * @param hostNickname        the nickname of the player who will host the game
     * @param expectedPlayerCount the expected number of players for the game (must be between 2 and 5)
     * @return the unique identifier of the created game
     */
    public int addNewGame(String hostNickname, int expectedPlayerCount) {
        if (hostNickname == null || hostNickname.isBlank()) {
            throw new IllegalArgumentException("Host nickname cannot be null or blank.");
        }
        if (expectedPlayerCount < 2 || expectedPlayerCount > 5) {
            throw new IllegalArgumentException("Expected player count must be between 2 and 5.");
        }

        String selectHostIdSql = "SELECT player_id FROM player WHERE nickname = ?";
        String insertGameSql = "INSERT INTO game (expected_players, host_id) VALUES (?, ?)";
        //String insertHostLinkSql = "INSERT INTO game_player (game_id, player_id) VALUES (?, ?)";

        try (Connection c = DatabaseManager.getConnection()) {
            c.setAutoCommit(false);

            try {
                int hostId;

                //HostId reading for the game creation
                try (PreparedStatement psHostId = c.prepareStatement(selectHostIdSql)) {
                    psHostId.setString(1, hostNickname);

                    try (ResultSet rs = psHostId.executeQuery()) {
                        if (!rs.next()) {
                            c.rollback();
                            throw new SQLException("Host player not found.");
                        }
                        hostId = rs.getInt("player_id");
                    }
                }

                //Creates the game and return the auto-generated game_id thanks to the java standard RuleSet of the PreparedStatement
                int generatedGameId;
                try (PreparedStatement psGame = c.prepareStatement(insertGameSql, Statement.RETURN_GENERATED_KEYS)) {
                    psGame.setInt(1, expectedPlayerCount);
                    psGame.setInt(2, hostId);

                    int rowsAffected = psGame.executeUpdate();
                    if (rowsAffected == 0) {
                        c.rollback();
                        throw new SQLException("Creating game failed.");
                    }

                    //We set into the java standard RuleSet the auto-generated param of the query that in this case is the game_id
                    try (ResultSet rs = psGame.getGeneratedKeys()) {
                        if (!rs.next()) {
                            c.rollback();
                            throw new SQLException("Creating game failed.");
                        }
                        generatedGameId = rs.getInt(1);
                    }
                }

                //We connect the two tables game and player into game_player
//                try (PreparedStatement psHost = c.prepareStatement(insertHostLinkSql)) {
//                    psHost.setInt(1, generatedGameId);
//                    psHost.setInt(2, hostId);
//
//                    int rowsHost = psHost.executeUpdate();
//                    if (rowsHost == 0) {
//                        c.rollback();
//                        throw new SQLException("Linking host to game failed.");
//                    }
//                }

                c.commit();
                return generatedGameId;

            } catch (SQLException e) {
                c.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Exception caught in DataWriting: " + e.getMessage());
            e.printStackTrace();
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
            System.err.println("[ERROR] Exception caught in DataWriting: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void rankingsUpdate(Game game) {
        String updateGameSql = "UPDATE game SET finished_at = CURRENT_TIMESTAMP WHERE game_id = ?";
        String updateGamePlayerSql = "UPDATE game_player SET final_score = ?, game_ranking = ? WHERE game_id = ? AND player_id = (SELECT player_id FROM player WHERE nickname = ?)";

        try (Connection c = DatabaseManager.getConnection()) {
            c.setAutoCommit(false);

            try (PreparedStatement psUpdateGame = c.prepareStatement(updateGameSql)) {
                psUpdateGame.setInt(1, game.getGameId());
                psUpdateGame.executeUpdate();
            } catch (SQLException e) {
                System.err.println("[ERROR] Exception caught in DataWriting: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            Map<Player, Integer> rankings = ScoreCalculator.rankPlayer(game);

            int position = 1;

            for (Player player : rankings.keySet()) {
                int final_score = rankings.get(player);
                try (PreparedStatement psUpdateRank = c.prepareStatement(updateGamePlayerSql)) {
                    psUpdateRank.setInt(1, final_score);
                    psUpdateRank.setInt(2, position);
                    psUpdateRank.setInt(3, game.getGameId());
                    psUpdateRank.setString(4, player.getNickname());
                    psUpdateRank.executeUpdate();
                    position++;
                } catch (SQLException e) {
                    c.rollback();
                    throw new SQLException("Updating player ranking failed for player: " + player.getNickname(), e);
                }
            }
            c.commit();
        } catch (SQLException e) {
            System.err.println("[ERROR] Exception caught in DataWriting: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * It updates the game snapshot in the database for a given game ID. The snapshot is provided as a GameSnapshotDTO object, which is converted to JSON format before being stored in the database.
     * Is used for game recovery in case of server crash, so it is crucial to keep it updated after every turn.
     *
     * @param gameId   the unique identifier of the game for which the snapshot is being updated
     * @param snapshot the GameSnapshotDTO object containing the current state of the game, which will be converted to JSON and stored in the database
     */
    public void updateGameSnapshot(int gameId, GameSnapshotDTO snapshot) {
        String sql = "UPDATE game SET game_snapshot = ? WHERE game_id = ?";

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // Convert the GameSnapshotDTO to JSON using Jackson ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonSnapshot = objectMapper.writeValueAsString(snapshot);

            org.postgresql.util.PGobject jsonObject = new org.postgresql.util.PGobject();
            jsonObject.setType("jsonb");
            jsonObject.setValue(jsonSnapshot);

            // Set the JSON snapshot and game ID parameters in the prepared statement
            ps.setObject(1, jsonObject);
            ps.setInt(2, gameId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Impossible to update the game snapshot for game ID: " + gameId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating game snapshot for game ID: " + gameId, e);
        }
    }

    public void updateGameInProgress(int gameId, boolean inProgress) {
        String sql = "UPDATE game SET in_progress = ? WHERE game_id = ?";

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {


            ps.setBoolean(1, inProgress);
            ps.setInt(2, gameId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Impossible to update the game in progress");
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Exception caught in DataWriting: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error updating game in_progress", e);
        }
    }
}