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

        String mediaUri;
        if (resource.toExternalForm().startsWith("jar:")) {
            try (java.io.InputStream is = MusicManager.class.getResourceAsStream("/assets/music/tribal.mp3")) {
                java.nio.file.Path tempAudio = java.nio.file.Files.createTempFile("mesos_music", ".mp3");
                tempAudio.toFile().deleteOnExit();
                java.nio.file.Files.copy(is, tempAudio, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                mediaUri = tempAudio.toUri().toString();
            } catch (Exception e) {
                System.err.println("Failed to extract music from JAR: " + e.getMessage());
                return;
            }
        } else {
            mediaUri = resource.toExternalForm();
        }

        Media media = new Media(mediaUri);
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