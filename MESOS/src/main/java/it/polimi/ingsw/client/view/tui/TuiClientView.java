package it.polimi.ingsw.client.view.tui;

import it.polimi.ingsw.client.network.socket.ClientState;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.client.view.Credentials;
import it.polimi.ingsw.server.model.observer.dto.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TuiClientView implements ClientView {
    private final Scanner scanner;
    private final ClientState sessionState;
    private final TerminalRenderer renderer;

    public TuiClientView(Scanner scanner, ClientState sessionState) {
        this.scanner = scanner;
        this.sessionState = sessionState;
        this.renderer = new TerminalRenderer(sessionState);
    }

    @Override
    public void showMessage(String message) {
        sessionState.setStatusMessage(message);
        renderer.render();
    }

    @Override
    public void showError(String message) {
        sessionState.setErrorMessage(message);
        renderer.render();
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
        sessionState.setAvailableGames(games);
        sessionState.setCurrentPhase("MENU");
        renderer.render();
    }


    @Override
    public void showGameInfo(GameInfoDTO gameInfo) {
        sessionState.setCurrentGameInfo(gameInfo);
        sessionState.setActiveGameId(gameInfo.gameId());
        sessionState.setCurrentPhase("LOBBY");
        renderer.render();
    }


    @Override
    public void showGameStarted(int gameId, List<String> playerNicknames) {
        sessionState.setActiveGameId(gameId);
        sessionState.setStatusMessage("Game started.");
        sessionState.setCurrentPhase("STARTED");
        renderer.render();
    }


    @Override
    public void showBoardSummary(BoardDTO board) {
        sessionState.setBoard(board);
        renderer.render();
    }


    @Override
    public void showPlaceTotemTurn(String currentPlayerNickname, boolean isMyTurn) {
        sessionState.setCurrentPhase("PLACE_TOTEM");
        sessionState.setCurrentPlayerNickname(currentPlayerNickname);
        renderer.render();
    }


    @Override
    public void showResolveActionTurn(String currentPlayerNickname, boolean isMyTurn) {
        sessionState.setCurrentPhase("RESOLVE_ACTION");
        sessionState.setCurrentPlayerNickname(currentPlayerNickname);
        renderer.render();
    }


    @Override
    public void showExtraActionTurn(String currentPlayerNickname, boolean isMyTurn) {
        sessionState.setCurrentPhase("EXTRA_ACTION");
        sessionState.setCurrentPlayerNickname(currentPlayerNickname);
        renderer.render();
    }


    @Override
    public void showPlayerResources(String playerNickname, int food, int prestigePoints, boolean isMe) {
        // Deprecated for the renderer-based TUI.
    }


    @Override
    public void showPlayersInfo(Collection<PlayerInfoDTO> playersInfo, String myNickname) {
        renderer.render();
    }

    public void showMyDeck() {
        renderer.renderMyDeck();
    }


    @Override
    public void showPlayerDeck(String playerNickname, PlayerDeckDTO playerDeck, boolean isMe) {
        renderer.render();
    }


    @Override
    public void showGameEnded(int gameId, Map<String, Integer> finalRanking) {
        sessionState.setCurrentPhase("GAME_ENDED");
        sessionState.setFinalRanking(finalRanking);
        renderer.render();
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

    @Override
    public void showRankings(List<LeadBoardDTO> rankings) {
        printSection("Rankings");
        if (rankings == null || rankings.isEmpty()) {
            System.out.println("No rankings available.");
            return;
        }

        System.out.printf("%-4s %-18s %-8s %-20s%n", "Pos", "Player", "Score", "Finished at");
        printLine(56);

        for (LeadBoardDTO dto : rankings) {
            String nickname = dto.nickname() == null ? "-" : dto.nickname();
            int score = dto.finalScore();
            int pos = dto.position();
            String finishedAt = dto.finished_at() == null ? "-" : dto.finished_at();
            System.out.printf("%-4d %-18s %-8d %-20s%n", pos, nickname, score, finishedAt);
        }
    }
}
