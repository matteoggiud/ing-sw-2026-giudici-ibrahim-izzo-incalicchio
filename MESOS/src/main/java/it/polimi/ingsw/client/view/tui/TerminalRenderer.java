package it.polimi.ingsw.client.view.tui;

import it.polimi.ingsw.client.network.socket.ClientSessionState;
import it.polimi.ingsw.server.model.observer.dto.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TerminalRenderer {
    private final ClientSessionState state;
    private boolean promptHelpPrinted = false;

    public TerminalRenderer(ClientSessionState state) {
        if (state == null) {
            throw new IllegalArgumentException("Client session state cannot be null");
        }
        this.state = state;
    }

    public void render() {
        clearTerminal();

        printHeader();
        printMessages();

        if (!state.getFinalRanking().isEmpty()) {
            printFinalRanking(state.getFinalRanking());
            printPromptHelp();
            return;
        }

        if (!state.getHistoricalRankings().isEmpty()) {
            printHistoricalRankings(state.getHistoricalRankings());
            printPromptHelp();
            return;
        }

        if (state.getActiveGameId() == null) {
            printAvailableGames(state.getAvailableGames());
            printPromptHelp();
            return;
        }

        printGameInfo();
        printBoard(state.getBoard());
        printPlayersStatistics(state.getPlayersInfo(), state.getNickname());
        printCurrentTurn();
        printPromptHelp();
    }

    private void clearTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void printHeader() {
        printLine(96);
        System.out.println("MESOS TUI");
        printLine(96);

        String nickname = state.getNickname() == null ? "-" : state.getNickname();
        String gameId = state.getActiveGameId() == null ? "-" : state.getActiveGameId().toString();
        String phase = state.getCurrentPhase() == null ? "-" : state.getCurrentPhase();

        System.out.printf("Player: %s | Game: %s | Phase: %s%n", nickname, gameId, phase);
    }

    private void printMessages() {
        if (state.getErrorMessage() != null && !state.getErrorMessage().isBlank()) {
            System.out.println("[ERROR] " + state.getErrorMessage());
        }

        if (state.getStatusMessage() != null && !state.getStatusMessage().isBlank()) {
            System.out.println(state.getStatusMessage());
        }

        if (state.getResolvingEventName() != null && !state.getResolvingEventName().isBlank()) {
            System.out.println("Resolving event: " + state.getResolvingEventName());
        }
    }

    private void printAvailableGames(List<GameInfoDTO> games) {
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

    private void printGameInfo() {
        GameInfoDTO gameInfo = state.getCurrentGameInfo();

        if (gameInfo == null) {
            return;
        }

        printSection("Game");
        System.out.println("ID: " + gameInfo.gameId());
        System.out.println("Players: " + gameInfo.numOfPlayers() + "/" + gameInfo.expectedPlayerCount());
        System.out.println("Nicknames: " + String.join(", ", gameInfo.players()));
    }

    private void printBoard(BoardDTO board) {
        if (board == null) {
            printSection("Board");
            System.out.println("Waiting for board update...");
            return;
        }

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

        characters.forEach(card -> printCardLine("character", card.cardId(), card.name(), card.era()));
        events.forEach(card -> printCardLine("event", card.cardId(), card.name(), card.era()));
        buildings.forEach(card -> printCardLine("building", card.cardId(), card.name(), card.era()));
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

    private void printPlayersStatistics(Collection<PlayerInfoDTO> playersInfo, String myNickname) {
        if (playersInfo == null || playersInfo.isEmpty()) {
            return;
        }

        printSection("Players statistics");

        System.out.printf(
                "%-18s %-5s %-6s %-7s %-10s %-10s %-10s %-10s%n",
                "Player", "PP", "Food", "Cards", "SustDisc", "ShamStars", "BuildDisc", "InvIcons"
        );
        printLine(86);

        for (PlayerInfoDTO playerInfo : playersInfo) {
            PlayerStatsDTO stats = playerInfo.stats();
            String name = playerName(playerInfo.nickname(), myNickname);

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
                "Player", "Gatherers", "Hunters", "Builders", "Shamans", "Artists", "Inventors"
        );
        printLine(84);

        for (PlayerInfoDTO playerInfo : playersInfo) {
            PlayerStatsDTO stats = playerInfo.stats();
            String name = playerName(playerInfo.nickname(), myNickname);

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

    public void renderMyDeck() {
        clearTerminal();

        printHeader();

        PlayerInfoDTO myInfo = null;
        for (PlayerInfoDTO playerInfo : state.getPlayersInfo()) {
            if (playerInfo.nickname() != null && playerInfo.nickname().equals(state.getNickname())) {
                myInfo = playerInfo;
                break;
            }
        }

        printSection("Your deck");

        if (myInfo == null || myInfo.deck() == null) {
            System.out.println("Your deck is not available yet.");
            printPromptHelp();
            return;
        }

        PlayerDeckDTO deck = myInfo.deck();

        System.out.println("Characters:");
        if (deck.characterCards().isEmpty()) {
            System.out.println("  none");
        } else {
            deck.characterCards().forEach(card ->
                    printCardLine("character", card.cardId(), card.name(), card.era()));
        }

        System.out.println();
        System.out.println("Buildings:");
        if (deck.buildingCards().isEmpty()) {
            System.out.println("  none");
        } else {
            deck.buildingCards().forEach(card ->
                    printCardLine("building", card.cardId(), card.name(), card.era()));
        }

        printPromptHelp();
    }


    private void printCurrentTurn() {
        String currentPlayer = state.getCurrentPlayerNickname();

        if (currentPlayer == null || currentPlayer.isBlank()) {
            return;
        }

        printSection("Current turn");
        System.out.println("Current player: " + currentPlayer);

        boolean isMyTurn = currentPlayer.equals(state.getNickname());
        if (!isMyTurn) {
            return;
        }

        String phase = state.getCurrentPhase();

        if ("PLACE_TOTEM".equals(phase)) {
            System.out.println("Your turn: use place <position>");
        } else if ("RESOLVE_ACTION".equals(phase)) {
            System.out.println("Your turn: use draw <upper|lower> <cardId>");
        } else if ("EXTRA_ACTION".equals(phase)) {
            System.out.println("Your extra action: use draw-extra <cardId>");
        }
    }

    private void printFinalRanking(Map<String, Integer> finalRanking) {
        printSection("Game ended");
        System.out.println("Final ranking:");

        finalRanking.forEach((player, score) ->
                System.out.printf("  %-20s %d pp%n", player, score));
    }

    private void printHistoricalRankings(List<LeadBoardDTO> rankings) {
        printSection("Historical leaderboard");

        System.out.printf("%-4s %-18s %-8s %-20s%n", "Pos", "Player", "Score", "Finished at");
        printLine(56);

        for (LeadBoardDTO dto : rankings) {
            String nickname = dto.nickname() == null ? "-" : dto.nickname();
            String finishedAt = dto.finished_at() == null ? "-" : dto.finished_at();

            System.out.printf(
                    "%-4d %-18s %-8d %-20s%n",
                    dto.position(),
                    nickname,
                    dto.finalScore(),
                    finishedAt
            );
        }
    }


    private void printPromptHelp() {
        if(promptHelpPrinted){
            return;
        }
        promptHelpPrinted = true;

        System.out.println();
        printLine(96);
        System.out.println("help | login | register | games | create <players> | join <gameId> | left | place <pos> | draw <upper|lower> <cardId> | draw-extra <cardId> | deck | rankings <numPlayers> | quit F/n\n");
    }

    private String playerName(String nickname, String myNickname) {
        if (nickname != null && nickname.equals(myNickname)) {
            return nickname + " (you)";
        }
        return nickname == null ? "-" : nickname;
    }

    private void printCardLine(String type, int cardId, String name, String era) {
        System.out.printf("  %-9s #%03d  %-28s %s%n", type, cardId, name, era);
    }

    private void printSection(String title) {
        System.out.println();
        printLine(96);
        System.out.println(title);
        printLine(96);
    }

    private void printLine(int length) {
        System.out.println("-".repeat(length));
    }
}
