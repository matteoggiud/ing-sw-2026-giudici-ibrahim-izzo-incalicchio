package it.polimi.ingsw.server.database;

import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.gamelogic.ScoreCalculator;

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
                    //TODO: I think the statement might be against the rules of the project, so we need to review this
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

    public void rankingsUpdate(Game game) {
        String updateGameSql = "UPDATE game SET finished_at = CURRENT_TIMESTAMP WHERE game_id = ?";
        String updateGamePlayerSql = "UPDATE game_player SET final_score = ?, game_ranking = ? WHERE game_id = ? AND player_id = (SELECT player_id FROM player WHERE nickname = ?)";

        try (Connection c = DatabaseManager.getConnection()) {
            c.setAutoCommit(false);

            try (PreparedStatement psUpdateGame = c.prepareStatement(updateGameSql)) {
                psUpdateGame.setInt(1, game.getGameId());
                psUpdateGame.executeUpdate();
            } catch (SQLException e) {
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
            throw new RuntimeException(e);
        }
    }
}