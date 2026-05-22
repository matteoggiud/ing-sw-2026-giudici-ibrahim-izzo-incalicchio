package it.polimi.ingsw.client.view.gui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class MusicManager {
    private static MediaPlayer backgroundPlayer;

    public static void startBackgroundMusic() {
        if (backgroundPlayer != null) {
            return;
        }

        URL resource = MusicManager.class.getResource("/assets/music/tribal.mp3");
        if (resource == null) {
            System.err.println("Music file not found");
            return;
        }

        Media media = new Media(resource.toExternalForm());
        backgroundPlayer = new MediaPlayer(media);
        backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundPlayer.setVolume(0.3);
        backgroundPlayer.play();
    }

    public static void stopBackgroundMusic() {
        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
            backgroundPlayer.dispose();
            backgroundPlayer = null;
        }
    }
}