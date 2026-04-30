package it.polimi.ingsw.client.view.tui;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.client.view.Credentials;
import it.polimi.ingsw.server.model.observer.dto.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
        printSection("Available games");

        if (games == null || games.isEmpty()) {
            System.out.println("No available games.");
            return;
        }

        System.out.printf("%-8s %-10s %s%n", "Game", "Players", "Nicknames");
        printLine(56);

        for (GameInfoDTO game : games) {
            System.out.printf(
                    "%-8d %-10s %s%n",
                    game.gameId(),
                    game.numOfPlayers() + "/" + game.expectedPlayerCount(),
                    String.join(", ", game.players())
            );
        }
    }

    @Override
    public void showGameInfo(GameInfoDTO gameInfo) {
        printSection("Game");
        System.out.println("ID: " + gameInfo.gameId());
        System.out.println("Players: " + gameInfo.numOfPlayers() + "/" + gameInfo.expectedPlayerCount());
        System.out.println("Nicknames: " + String.join(", ", gameInfo.players()));
    }

    @Override
    public void showGameStarted(int gameId, List<String> playerNicknames) {
        printSection("Game started");
        System.out.println("Game ID: " + gameId);
        System.out.println("Players: " + String.join(", ", playerNicknames));
    }

    @Override
    public void showBoardSummary(BoardDTO board) {
        printSection("Board");
        System.out.println("Era: " + board.currentEra());

        printCards(
                "Upper row",
                board.upperCharacterCards(),
                board.upperEventCards(),
                board.upperBuildingCards()
        );

        printCards(
                "Lower row",
                board.lowerCharacterCards(),
                board.lowerEventCards(),
                board.lowerBuildingCards()
        );

        printOfferTrack(board.offerTrackBoxes());
        printTurnOrder(board.turnOrderSlots());
    }

    @Override
    public void showPlaceTotemTurn(String currentPlayerNickname, boolean isMyTurn) {
        printSection("Place totem");
        System.out.println("Current player: " + currentPlayerNickname);

        if (isMyTurn) {
            System.out.println("Your turn: use place <position>");
        }
    }

    @Override
    public void showResolveActionTurn(String currentPlayerNickname, boolean isMyTurn) {
        printSection("Resolve action");
        System.out.println("Current player: " + currentPlayerNickname);

        if (isMyTurn) {
            System.out.println("Your turn: use draw <upper|lower> <cardId>");
        }
    }

    @Override
    public void showExtraActionTurn(String currentPlayerNickname, boolean isMyTurn) {
        printSection("Extra action");
        System.out.println("Current player: " + currentPlayerNickname);

        if (isMyTurn) {
            System.out.println("Your extra action: use draw-extra <cardId>");
        }
    }

    @Override
    public void showPlayerResources(String playerNickname, int food, int prestigePoints, boolean isMe) {
        String owner = isMe ? "You" : playerNickname;
        System.out.printf("%s: food=%d, pp=%d%n", owner, food, prestigePoints);
    }

    @Override
    public void showPlayersInfo(Collection<PlayerInfoDTO> playersInfo, String myNickname) {
        if (playersInfo == null || playersInfo.isEmpty()) {
            return;
        }

        printSection("Players statistics");

        System.out.printf(
                "%-18s %-5s %-6s %-7s %-10s %-10s %-10s %-10s%n",
                "Player",
                "PP",
                "Food",
                "Cards",
                "SustDisc",
                "ShamStars",
                "BuildDisc",
                "InvIcons"
        );
        printLine(86);

        for (PlayerInfoDTO playerInfo : playersInfo) {
            PlayerStatsDTO stats = playerInfo.stats();

            String name = playerInfo.nickname();
            if (name != null && name.equals(myNickname)) {
                name = name + " (you)";
            }

            System.out.printf(
                    "%-18s %-5d %-6d %-7d %-10d %-10d %-10d %-10d%n",
                    name,
                    stats.pp(),
                    stats.food(),
                    stats.characterCount(),
                    stats.sustenanceDiscount(),
                    stats.shamanStars(),
                    stats.builderDiscount(),
                    stats.differentInventorIcons()
            );
        }

        System.out.println();
        System.out.println("Tribe character counts:");
        System.out.printf(
                "%-18s %-10s %-10s %-10s %-10s %-10s %-10s%n",
                "Player",
                "Gatherers",
                "Hunters",
                "Builders",
                "Shamans",
                "Artists",
                "Inventors"
        );
        printLine(84);

        for (PlayerInfoDTO playerInfo : playersInfo) {
            PlayerStatsDTO stats = playerInfo.stats();

            String name = playerInfo.nickname();
            if (name != null && name.equals(myNickname)) {
                name = name + " (you)";
            }

            System.out.printf(
                    "%-18s %-10d %-10d %-10d %-10d %-10d %-10d%n",
                    name,
                    stats.gathererCount(),
                    stats.hunterCount(),
                    stats.builderCount(),
                    stats.shamanCount(),
                    stats.artistCount(),
                    stats.inventorCount()
            );
        }
    }




    @Override
    public void showPlayerDeck(String playerNickname, PlayerDeckDTO playerDeck, boolean isMe) {
        printSection(isMe ? "Your deck" : playerNickname + " deck");

        System.out.println("Characters:");
        if (playerDeck.characterCards().isEmpty()) {
            System.out.println("  none");
        } else {
            playerDeck.characterCards().forEach(card ->
                    printCardLine("character", card.cardId(), card.name(), card.era()));
        }

        System.out.println("Buildings:");
        if (playerDeck.buildingCards().isEmpty()) {
            System.out.println("  none");
        } else {
            playerDeck.buildingCards().forEach(card ->
                    printCardLine("building", card.cardId(), card.name(), card.era()));
        }
    }

    @Override
    public void showGameEnded(int gameId, Map<String, Integer> finalRanking) {
        printSection("Game ended");
        System.out.println("Game ID: " + gameId);
        System.out.println("Final ranking:");

        finalRanking.forEach((player, score) ->
                System.out.printf("  %-20s %d pp%n", player, score));
    }

    private void printCards(
            String title,
            List<CharacterCardDTO> characters,
            List<EventCardDTO> events,
            List<BuildingCardDTO> buildings
    ) {
        System.out.println();
        System.out.println(title + ":");

        if (characters.isEmpty() && events.isEmpty() && buildings.isEmpty()) {
            System.out.println("  empty");
            return;
        }

        characters.forEach(card ->
                printCardLine("character", card.cardId(), card.name(), card.era()));

        events.forEach(card ->
                printCardLine("event", card.cardId(), card.name(), card.era()));

        buildings.forEach(card ->
                printCardLine("building", card.cardId(), card.name(), card.era()));
    }

    private void printOfferTrack(List<OfferBoxDTO> boxes) {
        System.out.println();
        System.out.println("Offer track:");
        System.out.printf("%-5s %-16s %-7s %-7s %-5s%n", "Pos", "Player", "Upper", "Lower", "Food");
        printLine(48);

        for (OfferBoxDTO box : boxes) {
            String player = box.placedPlayerNickname() == null ? "-" : box.placedPlayerNickname();

            System.out.printf(
                    "%-5d %-16s %-7d %-7d %-5d%n",
                    box.position(),
                    player,
                    box.upperRowPicks(),
                    box.lowerRowPicks(),
                    box.foodReward()
            );
        }
    }

    private void printTurnOrder(List<TurnOrderSlotDTO> slots) {
        System.out.println();
        System.out.println("Turn order:");
        System.out.printf("%-5s %-16s %-9s%n", "Pos", "Player", "Food");
        printLine(34);

        for (TurnOrderSlotDTO slot : slots) {
            String player = slot.placedPlayerNickname() == null ? "-" : slot.placedPlayerNickname();

            System.out.printf(
                    "%-5d %-16s %-9d%n",
                    slot.position(),
                    player,
                    slot.foodBonus()
            );
        }
    }

    private void printCardLine(String type, int cardId, String name, String era) {
        System.out.printf("  %-9s #%03d  %-28s %s%n", type, cardId, name, era);
    }

    private void printSection(String title) {
        System.out.println();
        printLine(56);
        System.out.println(title);
        printLine(56);
    }

    private void printLine(int length) {
        System.out.println("-".repeat(length));
    }
}
