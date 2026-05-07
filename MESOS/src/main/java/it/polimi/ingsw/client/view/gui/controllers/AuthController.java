package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.Credentials;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthController {

    @FXML
    private TextField nicknameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private ClientController clientController;

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    @FXML
    private void handleLogin() {
        String nickname = nicknameField.getText();
        String password = passwordField.getText();

        if (nickname.isBlank() || password.isBlank()) {
            messageLabel.setText("Please fill all the fields");
            return;
        }

        clientController.submitLogin(new Credentials(nickname, password));
    }

    @FXML
    private void handleRegistration() {
        String nickname = nicknameField.getText();
        String password = passwordField.getText();

        if (nickname.isBlank() || password.isBlank()) {
            messageLabel.setText("Please fill all the fields");
            return;
        }

        clientController.submitRegistration(new Credentials(nickname, password));
    }

    public void showErrorMessage(String message) {
        messageLabel.setText(message);
    }
}
