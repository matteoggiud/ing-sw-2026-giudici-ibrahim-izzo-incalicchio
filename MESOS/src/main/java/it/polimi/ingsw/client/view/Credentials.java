package it.polimi.ingsw.client.view;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public record Credentials(String nickname, String passwordHash) {
    public Credentials{
        passwordHash = hashing(passwordHash);
    }

    private static String hashing(String password) {
        try {
            MessageDigest translator = MessageDigest.getInstance("SHA-256");
            //Gives the password hashed, but it's in bytes so we need to make it string
            byte[] hashed = translator.digest(password.getBytes());

            //Make it string with the use of String Builder
            StringBuilder pwd_hashed = new StringBuilder();

            for (byte b : hashed) {
                pwd_hashed.append(String.format("%02x", b));
            }
            return pwd_hashed.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
