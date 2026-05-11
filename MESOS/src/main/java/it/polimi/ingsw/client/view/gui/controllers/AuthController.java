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
    private void initialize() {
        messageLabel.managedProperty().bind(messageLabel.visibleProperty());
        clearMessage();
    }

    @FXML
    private void handleLogin() {
        String nickname = nicknameField.getText() == null ? "" : nicknameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        if (nickname.isBlank() || password.isBlank()) {
            showErrorMessage("Please fill all the fields.");
            return;
        }

        clearMessage();
        guiContext.clientController().submitLogin(new Credentials(nickname, password));
    }

    @FXML
    private void handleRegistration() {
        String nickname = nicknameField.getText() == null ? "" : nicknameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        if (nickname.isBlank() || password.isBlank()) {
            showErrorMessage("Please fill all the fields.");
            return;
        }

        clearMessage();
        guiContext.clientController().submitRegistration(new Credentials(nickname, password));
    }

    public void showErrorMessage(String message) {
        showMessage(message, "error");
    }

    public void showInfoMessage(String message) {
        showMessage(message, "info");
    }

    public void clearMessage() {
        messageLabel.setText("");
        messageLabel.setVisible(false);
    }

    private void showMessage(String message, String typeClass) {
        String text = message == null || message.isBlank()
                ? "Something went wrong. Please try again."
                : message;

        messageLabel.setText(text);
        messageLabel.getStyleClass().setAll("auth-message", typeClass);
        messageLabel.setVisible(true);
    }

    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }
}
