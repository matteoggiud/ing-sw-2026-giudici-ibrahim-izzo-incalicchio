package it.polimi.ingsw.server.network.socket;

import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.controller.ViewController;
import it.polimi.ingsw.client.network.socket.SocketServerConnection;
import it.polimi.ingsw.client.network.socket.SocketVirtualServer;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.common.network.dto.*;
import it.polimi.ingsw.common.network.socket_messages.visitors.ServerVisitor;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.gamemodel.GamePortal;
import it.polimi.ingsw.server.network.ClientSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the socket networking layer.
 * Validates concurrent connections, login/registration flows, and graceful disconnections.
 */
@Timeout(10)
class SocketLayerIntegrationTest {

    private ServerSocket serverSocket;
    private SocketAcceptor socketAcceptor;
    private Thread acceptorThread;
    private int port;

    @AfterEach
    void tearDown() {
        if (socketAcceptor != null) {
            // Unregister clients if needed, though they will close on socket close
        }
        
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ignored) {}
        
        if (acceptorThread != null) {
            acceptorThread.interrupt();
        }
    }

    private void startSocketServer() throws IOException {
        serverSocket = new ServerSocket(0);
        port = serverSocket.getLocalPort();

        FakeGameController fakeController = new FakeGameController();
        socketAcceptor = new SocketAcceptor(serverSocket, fakeController);
        
        acceptorThread = new Thread(() -> {
            try {
                socketAcceptor.run();
            } catch (IOException ignored) {
            }
        });
        acceptorThread.start();
    }

    private ConnectedClient connectClient() throws IOException, InterruptedException {
        ClientState state = new ClientState();
        FakeClientView view = new FakeClientView();
        ViewController viewController = new ViewController(state, view);
        ServerVisitor visitor = new ServerVisitor(viewController);

        SocketServerConnection connection = new SocketServerConnection("127.0.0.1", port, visitor, state, view);
        connection.connect();
        
        SocketVirtualServer server = new SocketVirtualServer(connection);

        // Sleep briefly to ensure connection is established and streams are flushed
        Thread.sleep(100);

        return new ConnectedClient(view, state, server);
    }

    /**
     * Tests that a client can connect via sockets, send a login request, and correctly
     * receive the login success response via the socket callbacks.
     */
    @Test
    void loginThroughSocketNotifiesClientAndSendsAvailableGames() throws Exception {
        startSocketServer();

        ConnectedClient client = connectClient();

        client.server().onLoginRequest("alice", "pw");

        // Wait for the message to propagate over the network
        assertTrue(client.view().loginLatch.await(3, TimeUnit.SECONDS));

        assertEquals("alice", client.state().getNickname());
        assertTrue(client.view().availableGamesReceived);
    }

    /**
     * Tests the registration flow over sockets.
     */
    @Test
    void registerThroughSocketNotifiesClientAndSendsAvailableGames() throws Exception {
        startSocketServer();

        ConnectedClient client = connectClient();

        client.server().onRegisterMessage("bob", "pw");

        // Wait for the message to propagate
        assertTrue(client.view().loginLatch.await(3, TimeUnit.SECONDS));

        assertEquals("bob", client.state().getNickname());
        assertTrue(client.view().availableGamesReceived);
    }

    /**
     * Verifies that the socket server can handle multiple clients connecting and 
     * interacting concurrently.
     */
    @Test
    void multipleClientsCanConnectAndInteractConcurrently() throws Exception {
        startSocketServer();

        ConnectedClient client1 = connectClient();
        ConnectedClient client2 = connectClient();
        ConnectedClient client3 = connectClient();

        client1.server().onLoginRequest("user1", "pw");
        client2.server().onLoginRequest("user2", "pw");
        client3.server().onLoginRequest("user3", "pw");

        assertTrue(client1.view().loginLatch.await(3, TimeUnit.SECONDS));
        assertTrue(client2.view().loginLatch.await(3, TimeUnit.SECONDS));
        assertTrue(client3.view().loginLatch.await(3, TimeUnit.SECONDS));

        assertEquals("user1", client1.state().getNickname());
        assertEquals("user2", client2.state().getNickname());
        assertEquals("user3", client3.state().getNickname());
    }

    /**
     * Tests normal disconnections, ensuring the server remains alive and can
     * accept new socket connections after one drops.
     */
    @Test
    void secondClientCanConnectAfterFirstClientDisconnects() throws Exception {
        startSocketServer();

        ConnectedClient firstClient = connectClient();
        firstClient.server().onLoginRequest("first", "pw");
        assertTrue(firstClient.view().loginLatch.await(3, TimeUnit.SECONDS));
        
        firstClient.server().onDisconnectMessage();
        
        Thread.sleep(100); // Give server time to handle disconnect

        ConnectedClient secondClient = connectClient();
        secondClient.server().onLoginRequest("second", "pw");

        assertTrue(secondClient.view().loginLatch.await(3, TimeUnit.SECONDS));
        assertEquals("second", secondClient.state().getNickname());
    }

    // --- Mocks ---

    private record ConnectedClient(
            FakeClientView view,
            ClientState state,
            SocketVirtualServer server
    ) {}

    private static class FakeGameController extends GameController {
        FakeGameController() {
            super(new GamePortal());
        }

        @Override
        public void login(ClientSession clientSession, String username, String password) {
            if (!clientSession.isLoggedIn()) {
                clientSession.setNickname(username);
                // Send success & available games
                clientSession.getEventHandler().getView().onLoginSuccess(username, "Welcome " + username);
                clientSession.getEventHandler().getView().onAvailableGames(List.of());
            }
        }

        @Override
        public void register(ClientSession clientSession, String username, String password) {
            if (!clientSession.isLoggedIn()) {
                clientSession.setNickname(username);
                clientSession.getEventHandler().getView().onRegisterSuccess(username, "Welcome " + username);
                clientSession.getEventHandler().getView().onAvailableGames(List.of());
            }
        }

        @Override
        public List<GameInfoDTO> getAvailableGames(ClientSession clientSession) {
            return List.of();
        }

        @Override
        public List<ClientSession> handleDisconnect(ClientSession clientSession) {
            clientSession.disconnectSession();
            return List.of();
        }
        
        @Override
        public void handleUnexpectedDisconnect(ClientSession clientSession) {
            clientSession.disconnectSession();
        }
    }

    private static class FakeClientView implements ClientView {
        boolean availableGamesReceived = false;
        final CountDownLatch loginLatch = new CountDownLatch(1);

        @Override public void showMessage(String message) {}
        @Override public void showError(String message) {}
        @Override public void showDisconnected(String message) {}
        
        @Override 
        public void showAvailableGames(List<GameInfoDTO> games) {
            availableGamesReceived = true;
            loginLatch.countDown();
        }
        
        @Override public void showGameInfo(GameInfoDTO gameInfo) {}
        @Override public void showGameStarted(int gameId, List<String> playerNicknames) {}
        @Override public void showBoardSummary(BoardDTO board) {}
        @Override public void showPlaceTotemTurn(String currentPlayerNickname, boolean isMyTurn) {}
        @Override public void showResolveActionTurn(String currentPlayerNickname, boolean isMyTurn) {}
        @Override public void showExtraActionTurn(String currentPlayerNickname, boolean isMyTurn) {}
        @Override public void showPlayerResources(String playerNickname, int food, int prestigePoints, boolean isMe) {}
        @Override public void showPlayerDeck(String playerNickname, PlayerDeckDTO playerDeck, boolean isMe) {}
        @Override public void showGameEnded() {}
        @Override public void showPlayersInfo(Collection<PlayerInfoDTO> playersInfo, String myNickname) {}
        @Override public void showRankings(List<LeadBoardDTO> rankings) {}
        @Override public void showAuthenticator(String message) {}
        @Override public void showResolvingEvent(int gameId, EventCardDTO eventCardDTO) {}
        @Override public void shutdown(String reason) {}
    }
}
