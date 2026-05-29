package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.common.network.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ViewControllerTest {

    private ViewController viewController;
    private ClientState clientState;
    private MockClientView mockView;

    @BeforeEach
    void setUp() {
        clientState = new ClientState();
        mockView = new MockClientView();
        viewController = new ViewController(clientState, mockView);
    }

    /**
     * Test verifying the behavior when a game successfully starts.
     * It ensures the client's internal state reflects the active game ID, updates the current phase
     * to STARTED, records a corresponding status message, and invokes the appropriate view callback.
     */
    @Test
    void testOnGameStarted() {
        viewController.onGameStarted(100, List.of("Alice", "Bob"));
        
        assertEquals(100, clientState.getActiveGameId());
        assertEquals("STARTED", clientState.getCurrentPhase());
        assertEquals("Game started.", clientState.getStatusMessage());
        
        assertEquals("showGameStarted", mockView.lastAction);
        assertEquals(100, mockView.gameId);
        assertEquals(List.of("Alice", "Bob"), mockView.playerNicknames);
    }

    /**
     * Test verifying the system's reaction to a successful login event.
     * It checks that the client's nickname is saved, the game phase moves to MENU,
     * a success message is internally stored, and the view is commanded to display the message.
     */
    @Test
    void testOnLoginSuccess() {
        viewController.onLoginSuccess("Alice", "Welcome!");
        
        assertEquals("Alice", clientState.getNickname());
        assertEquals("MENU", clientState.getCurrentPhase());
        assertEquals("Welcome!", clientState.getStatusMessage());
        
        assertEquals("showMessage", mockView.lastAction);
        assertEquals("Welcome!", mockView.message);
    }

    /**
     * Test validating the handling of login failures.
     * It simulates an unsuccessful login attempt, asserting that the client's nickname
     * state is reverted/cleared, an error message is stored, and the view appropriately displays the error.
     */
    @Test
    void testOnLoginFailed() {
        clientState.setNickname("Alice");
        viewController.onLoginFailed("Invalid credentials", "Alice");
        
        assertNull(clientState.getNickname());
        assertEquals("Invalid credentials", clientState.getErrorMessage());
        
        assertEquals("showError", mockView.lastAction);
        assertEquals("Invalid credentials", mockView.message);
    }

    /**
     * Test demonstrating the reception and processing of available games data.
     * It verifies that the provided list of games is properly stored within the client state,
     * the system phase is aligned to MENU, and the view correctly renders the list of available games.
     */
    @Test
    void testOnAvailableGames() {
        GameInfoDTO game1 = new GameInfoDTO(1, 1, List.of("Alice"), 2);
        List<GameInfoDTO> games = List.of(game1);
        
        viewController.onAvailableGames(games);
        
        assertEquals(games, clientState.getAvailableGames());
        assertEquals("MENU", clientState.getCurrentPhase());
        
        assertEquals("showAvailableGames", mockView.lastAction);
        assertEquals(games, mockView.games);
    }

    /**
     * Test checking the logic when the player successfully joins an existing game.
     * It ensures the active game ID and game information are populated in the state, the current
     * phase switches to LOBBY, and the specific game information is forwarded to the client's view.
     */
    @Test
    void testOnJoinedGame() {
        GameInfoDTO game1 = new GameInfoDTO(200, 2, List.of("Alice", "Bob"), 3);
        viewController.onJoinedGame(game1);
        
        assertEquals(200, clientState.getActiveGameId());
        assertEquals(game1, clientState.getCurrentGameInfo());
        assertEquals("LOBBY", clientState.getCurrentPhase());
        
        assertEquals("showGameInfo", mockView.lastAction);
        assertEquals(game1, mockView.gameInfo);
    }

    /**
     * Test detailing the procedure triggered at the end of a game.
     * It checks that the active game ID is cleared, the system phase is updated to GAME_ENDED,
     * the final player rankings are recorded, and the view is commanded to display the game-ended screen.
     */
    @Test
    void testOnGameEnded() {
        Map<String, Integer> rankings = Map.of("Alice", 10);
        viewController.onGameEnded(100, rankings, 2, List.of());
        
        assertNull(clientState.getActiveGameId());
        assertEquals("GAME_ENDED", clientState.getCurrentPhase());
        assertEquals(rankings, clientState.getFinalRanking());
        
        assertEquals("showGameEnded", mockView.lastAction);
    }

    /**
     * Test explaining the handling of abrupt game crashes or server disconnections.
     * It confirms that the underlying error handling clears conventional error states, sets an
     * appropriate status message reflecting the crash, and prompts the view to show a disconnection alert.
     */
    @Test
    void testOnGameCrashed() {
        viewController.onGameCrashed("Server disconnected");
        
        // Error message gets cleared by setStatusMessage because they are mutually exclusive in ClientState
        assertNull(clientState.getErrorMessage());
        assertEquals("Game ended: Server disconnected", clientState.getStatusMessage());
        
        assertEquals("showDisconnected", mockView.lastAction);
        assertEquals("Game ended: Server disconnected", mockView.message);
    }

    // --- Mock Implementation ---
    private static class MockClientView implements ClientView {
        String lastAction;
        String message;
        int gameId;
        List<String> playerNicknames;
        List<GameInfoDTO> games;
        GameInfoDTO gameInfo;

        @Override public void showMessage(String message) { this.lastAction = "showMessage"; this.message = message; }
        @Override public void showError(String message) { this.lastAction = "showError"; this.message = message; }
        @Override public void showDisconnected(String message) { this.lastAction = "showDisconnected"; this.message = message; }
        @Override public void showAvailableGames(List<GameInfoDTO> games) { this.lastAction = "showAvailableGames"; this.games = games; }
        @Override public void showGameInfo(GameInfoDTO gameInfo) { this.lastAction = "showGameInfo"; this.gameInfo = gameInfo; }
        @Override public void showGameStarted(int gameId, List<String> playerNicknames) { this.lastAction = "showGameStarted"; this.gameId = gameId; this.playerNicknames = playerNicknames; }
        @Override public void showBoardSummary(BoardDTO board) { this.lastAction = "showBoardSummary"; }
        @Override public void showPlaceTotemTurn(String currentPlayerNickname, boolean isMyTurn) { this.lastAction = "showPlaceTotemTurn"; }
        @Override public void showResolveActionTurn(String currentPlayerNickname, boolean isMyTurn) { this.lastAction = "showResolveActionTurn"; }
        @Override public void showExtraActionTurn(String currentPlayerNickname, boolean isMyTurn) { this.lastAction = "showExtraActionTurn"; }
        @Override public void showPlayerResources(String playerNickname, int food, int prestigePoints, boolean isMe) { this.lastAction = "showPlayerResources"; }
        @Override public void showPlayerDeck(String playerNickname, PlayerDeckDTO playerDeck, boolean isMe) { this.lastAction = "showPlayerDeck"; }
        @Override public void showGameEnded() { this.lastAction = "showGameEnded"; }
        @Override public void showPlayersInfo(Collection<PlayerInfoDTO> playersInfo, String myNickname) { this.lastAction = "showPlayersInfo"; }
        @Override public void showRankings(List<LeadBoardDTO> rankings) { this.lastAction = "showRankings"; }
        @Override public void showAuthenticator(String message) { this.lastAction = "showAuthenticator"; }
        @Override public void showResolvingEvent(int gameId, EventCardDTO eventCardDTO) { this.lastAction = "showResolvingEvent"; }
        @Override public void shutdown(String reason) { this.lastAction = "shutdown"; }
    }
}
