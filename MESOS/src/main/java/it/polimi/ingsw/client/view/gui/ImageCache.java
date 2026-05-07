package it.polimi.ingsw.client.view.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageCache {
    private static final Map<String, Image> CACHE = new HashMap<>();

    public static Image getImage(String key) {
        return CACHE.computeIfAbsent(key, k -> {
            try (InputStream input = ImageCache.class.getResourceAsStream(k)) {
                if (input == null) throw new IllegalArgumentException("Image not found: " + k);
                return new Image(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static ImageView view(String imageKey, double width, double height) {
        ImageView imageView = new ImageView(getImage(imageKey));
        imageView.setPreserveRatio(true);
        if (width > 0) imageView.setFitWidth(width);
        if (height > 0) imageView.setFitHeight(height);
        imageView.setSmooth(true);
        imageView.setCache(true);
        return imageView;
    }
}
