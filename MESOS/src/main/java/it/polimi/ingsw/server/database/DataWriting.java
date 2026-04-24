package it.polimi.ingsw.server.database;

import it.polimi.ingsw.server.model.core.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataWriting {

    public boolean addNewGame(Game game) {
        String sql = "INSERT INTO game (game_id, num_players) VALUES (?, ?)";

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error adding new game to the database");
            return false;
        }
    }
}
