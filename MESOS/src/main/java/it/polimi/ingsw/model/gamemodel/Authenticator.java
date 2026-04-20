package it.polimi.ingsw.model.gamemodel;

import it.polimi.ingsw.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authenticator {

    public boolean loginUser(String nickname, String password) throws SQLException {
        String sql = "SELECT player_id, nickname, password, created_at FROM player WHERE nickname = ? AND password = ?";

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nickname);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean registerUser(String nickname, String password) throws SQLException {
        String registration = "INSERT INTO player (nickname, password) VALUES (?, ?)";
        String players = "SELECT player_id, nickname, password, created_at FROM player WHERE nickname = ?";

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(players)) {

            ps.setString(1, nickname);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    throw new IllegalArgumentException("Nickname already exists");
                }
            }
        }

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(registration)){

            ps.setString(1, nickname);
            ps.setString(2, password);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
