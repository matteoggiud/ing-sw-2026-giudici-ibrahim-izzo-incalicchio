package it.polimi.ingsw.model.gamemodel;

import it.polimi.ingsw.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Authenticator class is responsible for handling user authentication
 * and registration by interacting with the database.
 */
public class Authenticator {
    /**
     * Attempts to log in a user with the provided nickname and password.
     *
     * @param nickname the nickname of the user trying to log in
     * @param password the password of the user trying to log in
     * @return true if the login is successful, false otherwise
     * @throws SQLException if there is an error accessing the database or if the login credentials are invalid
     */
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

    /**
     * Attempts to register a new user with the provided nickname and password.
     *
     * @param nickname the nickname of the user trying to register
     * @param password the password of the user trying to register
     * @return true if the registration is successful, false otherwise
     * @throws SQLException if there is an error accessing the database or if the registration fails
     */
    public boolean registerUser(String nickname, String password) throws SQLException {
        String registration = "INSERT INTO player (nickname, password) VALUES (?, ?)";
        String players = "SELECT player_id, nickname, password, created_at FROM player WHERE nickname = ?";

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(players)) {

            ps.setString(1, nickname);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    throw new IllegalArgumentException("Nickname already exists");
                }
            }
        }

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(registration)) {

            ps.setString(1, nickname);
            ps.setString(2, password);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
