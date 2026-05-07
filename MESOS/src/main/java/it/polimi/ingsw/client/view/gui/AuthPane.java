package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.Credentials;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AuthPane {
    private final ClientController controller;
    private final Label infoLabel = new Label();
    private final Label errorLabel = new Label();

    public AuthPane(ClientController controller) {
        this.controller = controller;
    }

    public Scene createScene() {
        Label title = new Label("MESOS - Authentication");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nicknameField = new TextField();
        nicknameField.setPromptText("Nickname");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        loginButton.setOnAction(event -> {
            String nickname = nicknameField.getText().trim();
            String password = passwordField.getText().trim();
            if (nickname.isEmpty() || password.isEmpty()) {
                setError("Nickname and password cannot be empty.");
                return;
            }
            cleanError();
            controller.submitRegistration(new Credentials(nickname, password));
        });

        registerButton.setOnAction(event -> {
            String nickname = nicknameField.getText().trim();
            String password = passwordField.getText().trim();
            if (nickname.isEmpty() || password.isEmpty()) {
                setError("Nickname and password cannot be empty.");
                return;
            }
            cleanError();
            controller.submitRegistration(new Credentials(nickname, password));
        });

        HBox buttons = new HBox(10, loginButton, registerButton);
        buttons.setAlignment(Pos.CENTER);

        infoLabel.setStyle("-fx-text-fill: green;");
        errorLabel.setStyle("-fx-text-fill: red;");

        VBox root = new VBox(12, title, nicknameField, passwordField, buttons, infoLabel, errorLabel);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        return new Scene(root);
    }

    private void setInfo(String info) {
        infoLabel.setText(info == null ? "" : info);
    }

    private void setError(String error) {
        errorLabel.setText(error == null ? "" : error);
    }

    private void cleanError() {
        errorLabel.setText("");
    }
}
