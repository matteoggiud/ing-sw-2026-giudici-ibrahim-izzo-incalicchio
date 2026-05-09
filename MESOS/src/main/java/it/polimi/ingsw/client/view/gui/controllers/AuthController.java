package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.Credentials;
import it.polimi.ingsw.client.view.gui.GuiContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AuthController {

    @FXML
    private TextField nicknameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private GuiContext guiContext;

    public void setGuiContext(GuiContext guiContext) {
        this.guiContext = guiContext;
    }

    @FXML
    private void handleLogin() {
        String nickname = nicknameField.getText();
        String password = passwordField.getText();

        if (nickname.isBlank() || password.isBlank()) {
            showErrorMessage("Please fill all the fields");
            return;
        }

        guiContext.clientController().submitLogin(new Credentials(nickname, password));
    }

    @FXML
    private void handleRegistration() {
        String nickname = nicknameField.getText();
        String password = passwordField.getText();

        if (nickname.isBlank() || password.isBlank()) {
            showErrorMessage("Please fill all the fields");
            return;
        }

        guiContext.clientController().submitRegistration(new Credentials(nickname, password));
    }

    public void showErrorMessage(String message) {
        messageLabel.setText(guiContext.clientState().getErrorMessage());
        //messageLabel.setStyle("-fx-text-fill: red;");
    }

    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }
}
