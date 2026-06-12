package it.polimi.ingsw.server.network.rmi;

import it.polimi.ingsw.common.network.rmi.client.RemoteServer;
import it.polimi.ingsw.common.network.dto.*;
import it.polimi.ingsw.common.network.rmi.server.RemoteClientView;
import it.polimi.ingsw.common.network.rmi.server.RmiBootstrap;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.gamemodel.GamePortal;
import it.polimi.ingsw.server.network.ClientSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Timeout(10)
class RmiLayerIntegrationTest {
    private Registry registry;
    private RmiBootstrapServer bootstrapServer;
    private RmiBootstrap bootstrapStub;
    private String bindingName;
    private int port;

    private final List<Object> strongRefs = new ArrayList<>();

    @AfterEach
    void tearDown() {
        try {
            if (registry != null && bindingName != null) {
                registry.unbind(bindingName);
            }
        } catch (Exception ignored) {
        }

        for (Object ref : strongRefs) {
            try {
                UnicastRemoteObject.unexportObject((Remote) ref, true);
            } catch (Exception ignored) {
            }
        }

        try {
            if (bootstrapServer != null) {
                UnicastRemoteObject.unexportObject(bootstrapServer, true);
            }
        } catch (Exception ignored) {
        }

        try {
            if (registry != null) {
                UnicastRemoteObject.unexportObject(registry, true);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Test that logging in through the RMI layer properly authenticates the client, triggering a
     * success notification and immediately sending the list of available games. It verifies that
     * the RMI communication channel transmits the credentials and relays the expected responses back.
     *
     * @throws Exception if an error occurs during RMI connection or invocation
     */
    @Test
    void loginThroughRmiNotifiesClientAndSendsAvailableGames() throws Exception {
        startRmiServer();

        ConnectedClient client = connectClient();

        client.server.loginRequest("alice", "pw");

        assertEquals("alice", client.view.loginSuccessNickname);
        assertEquals("Login successful, welcome alice", client.view.loginSuccessMessage);
        assertTrue(client.view.availableGamesReceived);
    }

    /**
     * Test that registering a new account through the RMI layer properly registers the client,
     * triggering a success notification and immediately sending the list of available games.
     * It ensures the RMI handler correctly processes the registration request and populates the client's view.
     *
     * @throws Exception if an error occurs during RMI connection or invocation
     */
    @Test
    void registerThroughRmiNotifiesClientAndSendsAvailableGames() throws Exception {
        startRmiServer();

        ConnectedClient client = connectClient();

        client.server.onRegisterMessage("bob", "pw");

        assertEquals("bob", client.view.registerSuccessNickname);
        assertEquals("Registration successful, welcome bob", client.view.registerSuccessMessage);
        assertTrue(client.view.availableGamesReceived);
    }

    /**
     * Test verifying the robustness of the RMI server when dealing with multiple sequential connections.
     * It checks that a second client can connect, authenticate, and receive data successfully even after
     * a first client connects, disconnects, and a garbage collection cycle runs.
     *
     * @throws Exception if an error occurs during RMI connection or invocation
     */
    @Test
    void secondClientCanConnectAfterFirstClientDisconnectsAndGarbageCollectionRuns() throws Exception {
        startRmiServer();

        ConnectedClient firstClient = connectClient();
        firstClient.server.loginRequest("first", "pw");
        firstClient.server.onDisconnectMessage();

        forceGarbageCollection();

        ConnectedClient secondClient = connectClient();
        secondClient.server.loginRequest("second", "pw");

        assertEquals("second", secondClient.view.loginSuccessNickname);
        assertTrue(secondClient.view.availableGamesReceived);
    }

    /**
     * Test that checks the persistence and stability of the RemoteServer stub.
     * It verifies that the server stub assigned to a connected client remains valid and functional
     * even if a garbage collection cycle occurs before the client issues a request.
     *
     * @throws Exception if an error occurs during RMI connection or invocation
     */
    @Test
    void remoteServerStubSurvivesGarbageCollection() throws Exception {
        startRmiServer();

        ConnectedClient client = connectClient();

        forceGarbageCollection();

        client.server.loginRequest("gc_user", "pw");

        assertEquals("gc_user", client.view.loginSuccessNickname);
    }

    /**
     * Test that ensures the {@code RmiGenericVirtualView} accurately detects when the actual RMI client
     * becomes unreachable. It verifies that unexporting the remote client object correctly updates the
     * reachable state within the virtual view wrap.
     *
     * @throws Exception if an error occurs during RMI connection or object unexporting
     */
    @Test
    void rmiGenericVirtualViewDetectsUnreachableClientCallback() throws Exception {
        FakeRemoteClientView realClientView = new FakeRemoteClientView();
        RemoteClientView clientStub = (RemoteClientView) UnicastRemoteObject.exportObject(realClientView, 0);

        RmiGenericVirtualView virtualView = new RmiGenericVirtualView(clientStub);

        assertTrue(virtualView.isReachable());

        UnicastRemoteObject.unexportObject(realClientView, true);

        assertFalse(virtualView.isReachable());
    }

    /**
     * Test that checks if game crash notifications correctly propagate to the client view over RMI.
     * It simulates a game crash scenario (e.g., due to a disconnection) and validates that the
     * exact crash reason is delivered to the remote client.
     *
     * @throws Exception if an error occurs during RMI connection or invocation
     */
    @Test
    void gameCrashedCallbackArrivesToClient() throws Exception {
        startRmiServer();

        ConnectedClient client = connectClient();

        client.server.loginRequest("carol", "pw");

        RmiGenericVirtualView virtualView = new RmiGenericVirtualView(client.clientStub);
        virtualView.onGameCrashed("carol has disconnected, the game is ended");

        assertEquals("carol has disconnected, the game is ended", client.view.gameCrashedReason);
    }

    private void startRmiServer() throws Exception {
        port = findFreePort();
        bindingName = "MESOS_RMI_TEST_" + UUID.randomUUID();

        FakeGameController fakeController = new FakeGameController();

        bootstrapServer = new RmiBootstrapServer(fakeController);
        bootstrapStub = (RmiBootstrap) UnicastRemoteObject.exportObject(bootstrapServer, 0);

        registry = LocateRegistry.createRegistry(port);
        registry.rebind(bindingName, bootstrapStub);

        strongRefs.add(bootstrapServer);
        strongRefs.add(bootstrapStub);
        strongRefs.add(fakeController);
    }

    private ConnectedClient connectClient() throws Exception {
        FakeRemoteClientView realClientView = new FakeRemoteClientView();
        RemoteClientView clientStub = (RemoteClientView) UnicastRemoteObject.exportObject(realClientView, 0);

        Registry remoteRegistry = LocateRegistry.getRegistry("localhost", port);
        RmiBootstrap bootstrap = (RmiBootstrap) remoteRegistry.lookup(bindingName);

        RemoteServer remoteServer = bootstrap.connect(clientStub);

        strongRefs.add(realClientView);
        strongRefs.add(clientStub);
        strongRefs.add(remoteServer);

        return new ConnectedClient(realClientView, clientStub, remoteServer);
    }

    private static int findFreePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    private static void forceGarbageCollection() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            System.gc();
            Thread.sleep(100);
        }
    }

    private record ConnectedClient(
            FakeRemoteClientView view,
            RemoteClientView clientStub,
            RemoteServer server
    ) {
    }

    private static class FakeGameController extends GameController {
        FakeGameController() {
            super(new GamePortal());
        }

        @Override
        public void login(ClientSession clientSession, String username, String password) {
            if (!clientSession.isLoggedIn()) {
                clientSession.setNickname(username);
            }
        }

        @Override
        public void register(ClientSession clientSession, String username, String password) {
            if (!clientSession.isLoggedIn()) {
                clientSession.setNickname(username);
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
    }

    private static class FakeRemoteClientView implements RemoteClientView {
        String loginSuccessNickname;
        String loginSuccessMessage;

        String registerSuccessNickname;
        String registerSuccessMessage;

        String disconnectionSuccessMessage;
        String leftGameSuccessMessage;
        String genericError;
        String gameCrashedReason;

        boolean availableGamesReceived;

        @Override
        public void onGameStarted(int gameId, List<String> playerNicknames) {
        }

        @Override
        public void onAuthenticationNeeded(String message) {
        }

        @Override
        public void onLoginSuccess(String nickname, String message) {
            this.loginSuccessNickname = nickname;
            this.loginSuccessMessage = message;
        }

        @Override
        public void onLoginFailed(String reason, String nickname) {
        }

        @Override
        public void onAvailableGames(List<GameInfoDTO> games) {
            this.availableGamesReceived = true;
        }

        @Override
        public void onGenericError(String reason) {
            this.genericError = reason;
        }

        @Override
        public void onRegisterSuccess(String nickname, String message) {
            this.registerSuccessNickname = nickname;
            this.registerSuccessMessage = message;
        }

        @Override
        public void onRegisterFailed(String reason, String nickname) {
        }

        @Override
        public void onDisconnectionSuccess(String message) {
            this.disconnectionSuccessMessage = message;
        }

        @Override
        public void onLeftGameSuccess(String message) {
            this.leftGameSuccessMessage = message;
        }

        @Override
        public void onGenericInfo(String info) {
        }

        @Override
        public void onPlayerChanged(List<String> playerNicknames) {
        }

        @Override
        public void onBoardChanged(int gameId, BoardDTO board) {
        }

        @Override
        public void onEraChanged(int gameId, String era) {
        }

        @Override
        public void onPlaceTotemTurnChanged(int gameId, String currentPlayerNickname) {
        }

        @Override
        public void onResolveActionTurnChanged(int gameId, String currentPlayerNickname) {
        }

        @Override
        public void onExtraActionTurnChanged(int gameId, String currentPlayerNickname) {
        }

        @Override
        public void onPlayerDeckUpdated(int gameId, String nickname, PlayerDeckDTO playerDeck) {
        }

        @Override
        public void onGameEnded(
                int gameId,
                Map<String, Integer> finalRanking,
                int numPlayers,
                List<LeadBoardDTO> historicalLeaderboard
        ) {
        }

        @Override
        public void onJoinedGame(GameInfoDTO gameInfo) {
        }

        @Override
        public void onPlayerInfosChanged(int gameId, List<PlayerInfoDTO> playerInfos) {
        }

        @Override
        public void onResolvingEvent(int gameId, EventCardDTO eventCard) {
        }

        @Override
        public void onRankingsReceived(List<LeadBoardDTO> rankings) {
        }

        @Override
        public void onGameCrashed(String reason) {
            this.gameCrashedReason = reason;
        }

        @Override
        public boolean isAlive() throws RemoteException {
            return true;
        }
    }
}