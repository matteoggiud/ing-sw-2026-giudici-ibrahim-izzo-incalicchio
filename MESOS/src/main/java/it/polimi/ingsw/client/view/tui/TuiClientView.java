package it.polimi.ingsw.client.view.tui;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.client.view.Credentials;
import it.polimi.ingsw.server.model.observer.dto.*;
import java.util.Map;


import java.util.List;
import java.util.Scanner;

public class TuiClientView implements ClientView {
    private final Scanner scanner;

    public TuiClientView(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showError(String message) {
        System.err.println("[ERROR] " + message);
    }

    @Override
    public Credentials requestLoginCredentials() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        return new Credentials(username, password);
    }

    @Override
    public Credentials requestRegisterCredentials() {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Choose a password: ");
        String password = scanner.nextLine().trim();

        return new Credentials(username, password);
    }

    @Override
    public void showAvailableGames(List<GameInfoDTO> games) {
        if (games == null || games.isEmpty()) {
            System.out.println("No available games.");
            return;
        }

        System.out.println("Available games:");
        for (GameInfoDTO game : games) {
            System.out.println("- id: " + game.gameId()
                    + " players: " + game.numOfPlayers() + "/" + game.expectedPlayerCount()
                    + " [" + String.join(", ", game.players()) + "]");
        }
    }

    @Override
    public void showGameInfo(GameInfoDTO gameInfo) {
        System.out.println("Game ID: " + gameInfo.gameId());
        System.out.println("Players [" + gameInfo.numOfPlayers() + "/" + gameInfo.expectedPlayerCount() + "]: "
                + String.join(", ", gameInfo.players()));
    }

    @Override
    public void showGameStarted(int gameId, List<String> playerNicknames) {
        System.out.println("Game " + gameId + " started.");
        System.out.println("Players: " + String.join(", ", playerNicknames));
    }

    @Override
    public void showBoardSummary(BoardDTO board) {
        System.out.println("Board updated.");
        System.out.println("Era: " + board.currentEra());

        System.out.println("Upper row:");
        board.upperCharacterCards().forEach(card ->
                System.out.println("  character #" + card.cardId() + " " + card.name() + " " + card.era()));
        board.upperEventCards().forEach(card ->
                System.out.println("  event     #" + card.cardId() + " " + card.name() + " " + card.era()));
        board.upperBuildingCards().forEach(card ->
                System.out.println("  building  #" + card.cardId() + " " + card.name() + " " + card.era()));

        System.out.println("Lower row:");
        board.lowerCharacterCards().forEach(card ->
                System.out.println("  character #" + card.cardId() + " " + card.name() + " " + card.era()));
        board.lowerEventCards().forEach(card ->
                System.out.println("  event     #" + card.cardId() + " " + card.name() + " " + card.era()));
        board.lowerBuildingCards().forEach(card ->
                System.out.println("  building  #" + card.cardId() + " " + card.name() + " " + card.era()));


        System.out.println("Offer track:");
        for (OfferBoxDTO box : board.offerTrackBoxes()) {
            String player = box.placedPlayerNickname() == null ? "-" : box.placedPlayerNickname();

            System.out.println(box.position()
                    + ") player=" + player
                    + " upper=" + box.upperRowPicks()
                    + " lower=" + box.lowerRowPicks()
                    + " food=" + box.foodReward());

        }

        System.out.println("Turn order:");
        for (TurnOrderSlotDTO slot : board.turnOrderSlots()) {
            String player = slot.placedPlayerNickname() == null ? "-" : slot.placedPlayerNickname();

            System.out.println("pos " + slot.position()
                    + " player=" + player
                    + " foodBonus=" + slot.foodBonus());
        }
    }

    @Override
    public void showPlaceTotemTurn(String currentPlayerNickname, boolean isMyTurn) {
        System.out.println("Place totem turn: " + currentPlayerNickname);

        if (isMyTurn) {
            System.out.println("It is your turn. Use the place command when available.");
        }
    }

    @Override
    public void showResolveActionTurn(String currentPlayerNickname, boolean isMyTurn) {
        System.out.println("Resolve action turn: " + currentPlayerNickname);

        if (isMyTurn) {
            System.out.println("It is your turn. Use: draw <upper|lower> <cardId>");
        }
    }

    @Override
    public void showExtraActionTurn(String currentPlayerNickname, boolean isMyTurn) {
        System.out.println("Extra action turn: " + currentPlayerNickname);

        if (isMyTurn) {
            System.out.println("It is your extra action. Use: draw upper <cardId>");
        }
    }

    @Override
    public void showPlayerResources(String playerNickname, int food, int prestigePoints, boolean isMe) {
        String prefix = isMe ? "Your resources" : playerNickname + " resources";
        System.out.println(prefix + ": food=" + food + " pp=" + prestigePoints);
    }

    @Override
    public void showPlayerDeck(String playerNickname, PlayerDeckDTO playerDeck, boolean isMe) {
        String prefix = isMe ? "Your deck" : playerNickname + " deck";
        System.out.println(prefix + " updated.");

        System.out.println("Characters:");
        playerDeck.characterCards().forEach(card ->
                System.out.println("  #" + card.cardId() + " " + card.name() + " " + card.era()));

        System.out.println("Buildings:");
        playerDeck.buildingCards().forEach(card ->
                System.out.println("  #" + card.cardId() + " " + card.name() + " " + card.era()));
    }

    @Override
    public void showGameEnded(int gameId, Map<String, Integer> finalRanking) {
        System.out.println("Game " + gameId + " ended.");
        System.out.println("Final ranking:");

        finalRanking.forEach((player, score) ->
                System.out.println("  " + player + ": " + score + " pp"));
    }

}
