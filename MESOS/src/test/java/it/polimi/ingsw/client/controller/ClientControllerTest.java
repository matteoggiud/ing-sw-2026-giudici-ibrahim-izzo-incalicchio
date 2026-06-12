package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.GenericVirtualServer;
import it.polimi.ingsw.client.view.Credentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientControllerTest {

    private ClientController clientController;
    private MockVirtualServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = new MockVirtualServer();
        clientController = new ClientController(mockServer);
    }

    /**
     * Tests submitting a login request with user credentials, verifying that the controller
     * successfully forwards the correct username and password hash to the virtual server.
     */
    @Test
    void testSubmitLogin() {
        Credentials creds = new Credentials("Alice", "password");
        clientController.submitLogin(creds);
        assertEquals("Alice", mockServer.username);
        assertEquals(creds.passwordHash(), mockServer.password);
        assertEquals("onLoginRequest", mockServer.lastAction);
    }

    /**
     * Tests submitting a registration request with user credentials, verifying that the controller
     * successfully forwards the correct username and password hash to the virtual server.
     */
    @Test
    void testSubmitRegistration() {
        Credentials creds = new Credentials("Bob", "pass123");
        clientController.submitRegistration(creds);
        assertEquals("Bob", mockServer.username);
        assertEquals(creds.passwordHash(), mockServer.password);
        assertEquals("onRegisterMessage", mockServer.lastAction);
    }

    /**
     * Tests the creation of a new game with a specified number of players, ensuring the
     * request reaches the virtual server properly.
     */
    @Test
    void testCreateGame() {
        clientController.createGame(4);
        assertEquals(4, mockServer.numPlayers);
        assertEquals("onCreateGameMessage", mockServer.lastAction);
    }

    /**
     * Tests joining an existing game via a given game ID, verifying the action is
     * forwarded to the virtual server correctly.
     */
    @Test
    void testJoinGame() {
        clientController.joinGame(101);
        assertEquals(101, mockServer.gameId);
        assertEquals("onJoinGameMessage", mockServer.lastAction);
    }

    /**
     * Tests requesting the list of available games, verifying the appropriate message
     * is triggered on the virtual server.
     */
    @Test
    void testGetAvailableGames() {
        clientController.getAvailableGames();
        assertEquals("onGetAvailableGamesMessage", mockServer.lastAction);
    }

    /**
     * Tests the disconnect functionality, ensuring the disconnection message is properly
     * sent to the underlying virtual server.
     */
    @Test
    void testDisconnect() {
        clientController.disconnect();
        assertEquals("onDisconnectMessage", mockServer.lastAction);
    }

    /**
     * Tests placing a totem on a specific offer box position, verifying the index is
     * accurately communicated to the virtual server.
     */
    @Test
    void testPlaceTotem() {
        clientController.placeTotem(2);
        assertEquals(2, mockServer.offerBoxPosition);
        assertEquals("onPlaceTotemMessage", mockServer.lastAction);
    }

    /**
     * Tests the action of leaving an active game, verifying the message is sent to
     * the virtual server appropriately.
     */
    @Test
    void testLeftGame() {
        clientController.leftGame();
        assertEquals("onLeftGameMessage", mockServer.lastAction);
    }

    /**
     * Tests the action of leaving the lobby, verifying the appropriate message is
     * transmitted to the virtual server.
     */
    @Test
    void testLeftLobby() {
        clientController.leftLobby();
        assertEquals("onLeftLobbyMessage", mockServer.lastAction);
    }

    /**
     * Tests drawing a card by its ID from a specific row, verifying that both the card ID
     * and the row origin flag are sent correctly to the virtual server.
     */
    @Test
    void testDrawCard() {
        clientController.drawCard(42, true);
        assertEquals(42, mockServer.cardId);
        assertTrue(mockServer.fromUpperRow);
        assertEquals("onDrawCardMessage", mockServer.lastAction);
    }

    /**
     * Tests drawing an extra card by its ID, ensuring the action is relayed properly
     * to the virtual server.
     */
    @Test
    void testDrawExtraCard() {
        clientController.drawExtraCard(7);
        assertEquals(7, mockServer.cardId);
        assertEquals("onDrawExtraCardMessage", mockServer.lastAction);
    }

    /**
     * Tests requesting player rankings, verifying that the number of players is correctly
     * passed along to the virtual server.
     */
    @Test
    void testGetRankings() {
        clientController.getRankings(3);
        assertEquals(3, mockServer.numPlayers);
        assertEquals("onGetRankings", mockServer.lastAction);
    }

    // --- Mock Implementation ---
    private static class MockVirtualServer implements GenericVirtualServer {
        String lastAction;
        String username;
        String password;
        int numPlayers;
        int gameId;
        int offerBoxPosition;
        int cardId;
        boolean fromUpperRow;

        @Override
        public void onLoginRequest(String username, String password) {
            this.lastAction = "onLoginRequest";
            this.username = username;
            this.password = password;
        }

        @Override
        public void onRegisterMessage(String username, String password) {
            this.lastAction = "onRegisterMessage";
            this.username = username;
            this.password = password;
        }

        @Override
        public void onCreateGameMessage(int numPlayers) {
            this.lastAction = "onCreateGameMessage";
            this.numPlayers = numPlayers;
        }

        @Override
        public void onJoinGameMessage(int gameId) {
            this.lastAction = "onJoinGameMessage";
            this.gameId = gameId;
        }

        @Override
        public void onGetAvailableGamesMessage() {
            this.lastAction = "onGetAvailableGamesMessage";
        }

        @Override
        public void onDisconnectMessage() {
            this.lastAction = "onDisconnectMessage";
        }

        @Override
        public void onPlaceTotemMessage(int offerBoxPosition) {
            this.lastAction = "onPlaceTotemMessage";
            this.offerBoxPosition = offerBoxPosition;
        }

        @Override
        public void onLeftGameMessage() {
            this.lastAction = "onLeftGameMessage";
        }

        @Override
        public void onDrawCardMessage(int cardId, boolean fromUpperRow) {
            this.lastAction = "onDrawCardMessage";
            this.cardId = cardId;
            this.fromUpperRow = fromUpperRow;
        }

        @Override
        public void onDrawExtraCardMessage(int cardId) {
            this.lastAction = "onDrawExtraCardMessage";
            this.cardId = cardId;
        }

        @Override
        public void onGetRankings(int numPlayers) {
            this.lastAction = "onGetRankings";
            this.numPlayers = numPlayers;
        }

        @Override
        public void onLeftLobbyMessage() {
            this.lastAction = "onLeftLobbyMessage";
        }
    }
}
