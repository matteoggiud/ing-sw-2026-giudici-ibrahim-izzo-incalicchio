package it.polimi.ingsw.client.view.tui;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.client.view.Credentials;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;

import java.util.List;
import java.util.Scanner;

public class TuiClientView implements ClientView {
    private final Scanner scanner;

    public TuiClientView(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Displays a message to the user by printing it to the standard output stream.
     *
     * @param message the message to be displayed to the user
     */
    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error message to the user by printing it to the standard error stream with a prefix of [ERROR].
     *
     * @param message the error message to be displayed to the user
     */
    @Override
    public void showError(String message) {
        System.err.println("[ERROR] " + message);
    }

    /**
     * Prompts the user to enter a username and password for login. The method reads the input from the console, trims any leading or trailing whitespace, and returns a Credentials object containing the entered username and password.
     *
     * @return a Credentials object containing the username and password entered by the user for login
     */
    @Override
    public Credentials requestLoginCredentials() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        return new Credentials(username, password);
    }

    /**
     * Prompts the user to enter a username and password for registration. The method reads the input from the console, trims any leading or trailing whitespace, and returns a Credentials object containing the entered username and password.
     *
     * @return a Credentials object containing the username and password entered by the user for registration
     */
    @Override
    public Credentials requestRegisterCredentials() {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Choose a password: ");
        String password = scanner.nextLine().trim();

        return new Credentials(username, password);
    }

    /**
     * Displays a list of available games to the user. Each game is displayed with its ID and the number of players currently in the game. If there are no available games, a message indicating that is displayed.
     *
     * @param games a list of GameInfoDTO objects representing the available games to be displayed
     */
    @Override
    public void showAvailableGames(List<GameInfoDTO> games) {
        if (games == null || games.isEmpty()) {
            System.out.println("No available games.");
            return;
        }

        System.out.println("Available games:");
        for (GameInfoDTO game : games) {
            System.out.println("- id: \"" + game.gameId() + "\" players: \" " + game.numOfPlayers() + "\"");
        }
    }

    /**
     * Displays detailed information about a specific game, including its ID and the list of players currently in the game.
     *
     * @param gameInfo the GameInfoDTO object containing the game's information to be displayed
     */
    @Override
    public void showGameInfo(GameInfoDTO gameInfo) {
        System.out.println("Game ID: " + gameInfo.gameId());
        System.out.println("Players [" + gameInfo.numOfPlayers() + "]: " + String.join(", ", gameInfo.players()));

    }
}
