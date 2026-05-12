package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.view.Credentials;
import it.polimi.ingsw.client.view.gui.GuiContext;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AuthController {
    private static final double BACKGROUND_RATIO = 16.0 / 9.0;

    private static final double PANEL_CENTER_X = 0.68;
    private static final double PANEL_CENTER_Y = 0.58;

    @FXML
    private StackPane authRoot;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Pane authOverlay;

    @FXML
    private VBox authPanel;

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
        // Bindings for responsive design
        bindBackgroundCover();
        bindPanelToBackgroundPosition();
    }

    private void bindBackgroundCover() {
        // The background image should cover the entire authRoot while maintaining its aspect ratio.
        DoubleBinding coverWidth = Bindings.createDoubleBinding(
                () -> Math.max(authRoot.getWidth(), authRoot.getHeight() * BACKGROUND_RATIO),
                authRoot.widthProperty(),
                authRoot.heightProperty()
        );
        // Similarly, the cover height is determined by the width and the aspect ratio.
        DoubleBinding coverHeight = Bindings.createDoubleBinding(
                () -> Math.max(authRoot.getHeight(), authRoot.getWidth() / BACKGROUND_RATIO),
                authRoot.widthProperty(),
                authRoot.heightProperty()
        );

        // Bind the background image size to the calculated cover dimensions
        backgroundImage.fitWidthProperty().bind(coverWidth);
        backgroundImage.fitHeightProperty().bind(coverHeight);

        // The auth overlay should also cover the entire area of the background image to ensure consistent layout.
        authOverlay.prefWidthProperty().bind(coverWidth);
        authOverlay.prefHeightProperty().bind(coverHeight);
    }

    private void bindPanelToBackgroundPosition() {
        // The auth panel should have a width that is a percentage of the auth overlay, but clamped to reasonable limits for usability.
        authPanel.prefWidthProperty().bind(Bindings.createDoubleBinding(
                () -> clamp(authOverlay.getWidth() * 0.24, 360, 460),
                authOverlay.widthProperty()
        ));

        // The height of the auth panel can be determined by its content, but we can set a max height to prevent it from becoming too large
        authPanel.layoutXProperty().bind(Bindings.createDoubleBinding(
                () -> authOverlay.getWidth() * PANEL_CENTER_X - authPanel.getWidth() / 2.0,
                authOverlay.widthProperty(),
                authPanel.widthProperty()
        ));

        // The vertical position is similarly calculated to keep the panel centered around the specified point, but it will adjust based on the panel's height.
        authPanel.layoutYProperty().bind(Bindings.createDoubleBinding(
                () -> authOverlay.getHeight() * PANEL_CENTER_Y - authPanel.getHeight() / 2.0,
                authOverlay.heightProperty(),
                authPanel.heightProperty()
        ));
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    @FXML
    private void handleLogin() {
        String nickname = nicknameField.getText() == null ? "" : nicknameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        if (nickname.isBlank() || password.isBlank()) {
            showErrorMessage("Insert Nickname and Password.");
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
            showErrorMessage("Insert Nickname and Password.");
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
