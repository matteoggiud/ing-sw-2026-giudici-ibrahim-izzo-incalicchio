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
     * @param message
     */
    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * @param message
     */
    @Override
    public void showError(String message) {
        System.err.println("[ERROR] " + message);
    }

    /**
     * @return
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
     * @return
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
     * @param games
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
}
