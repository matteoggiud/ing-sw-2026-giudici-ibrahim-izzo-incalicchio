package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.socket.ClientState;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.client.view.gui.controllers.AuthController;
import it.polimi.ingsw.server.model.observer.dto.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.List;


public class GuiClientView implements ClientView {
    private final ClientState clientState;
    private final Stage stage;
    private ClientController clientController;

    public GuiClientView(ClientState clientState, Stage stage) {
        this.clientState = clientState;
        this.stage = stage;
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * @param message
     */
    @Override
    public void showMessage(String message) {
        Platform.runLater(() -> {
            // Tutto il codice qui dentro viene eseguito in sicurezza sul Thread di JavaFX
            // Esempio:
            // myAlertBox.setText(message);
            // myAlertBox.show();
        });
    }

    /**
     * @param message
     */
    @Override
    public void showError(String message) {

    }

    /**
     * @param games
     */
    @Override
    public void showAvailableGames(List<GameInfoDTO> games) {

    }

    /**
     * @param gameInfo
     */
    @Override
    public void showGameInfo(GameInfoDTO gameInfo) {

    }

    /**
     * @param gameId
     * @param playerNicknames
     */
    @Override
    public void showGameStarted(int gameId, List<String> playerNicknames) {

    }

    /**
     * @param board
     */
    @Override
    public void showBoardSummary(BoardDTO board) {

    }

    /**
     * @param currentPlayerNickname
     * @param isMyTurn
     */
    @Override
    public void showPlaceTotemTurn(String currentPlayerNickname, boolean isMyTurn) {

    }

    /**
     * @param currentPlayerNickname
     * @param isMyTurn
     */
    @Override
    public void showResolveActionTurn(String currentPlayerNickname, boolean isMyTurn) {

    }

    /**
     * @param currentPlayerNickname
     * @param isMyTurn
     */
    @Override
    public void showExtraActionTurn(String currentPlayerNickname, boolean isMyTurn) {

    }

    /**
     * @param playerNickname
     * @param food
     * @param prestigePoints
     * @param isMe
     */
    @Override
    public void showPlayerResources(String playerNickname, int food, int prestigePoints, boolean isMe) {

    }

    /**
     * @param playerNickname
     * @param playerDeck
     * @param isMe
     */
    @Override
    public void showPlayerDeck(String playerNickname, PlayerDeckDTO playerDeck, boolean isMe) {

    }

    /**
     *
     */
    @Override
    public void showGameEnded() {

    }


    /**
     * @param playersInfo
     * @param myNickname
     */
    @Override
    public void showPlayersInfo(Collection<PlayerInfoDTO> playersInfo, String myNickname) {

    }

    /**
     * @param rankings
     */
    @Override
    public void showRankings(List<LeadBoardDTO> rankings) {

    }

    @Override
    public void showAuthenticator(String message) {
        Platform.runLater(() -> {
           try{
               FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AuthPane.fxml"));
               Parent root = loader.load();

               AuthController authController = loader.getController();
               authController.setClientController(clientController);
               
               if (message != null && !message.isBlank()) {
                   authController.showErrorMessage(message);
               }
               
               stage.setScene(new Scene(root));
               stage.show();
           } catch (IOException e){
               e.printStackTrace();
           }
            
        });
    }
}
