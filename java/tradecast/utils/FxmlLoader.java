package tradecast.utils;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public final class FxmlLoader {

    private FxmlLoader() {
    }

    public static Parent loadRoot(String fxmlPath) throws IOException {
        return loadRoot(fxmlPath, null);
    }

    public static Parent loadRoot(String fxmlPath, Object root) throws IOException {
        URL url = Objects.requireNonNull(
                FxmlLoader.class.getResource(fxmlPath),
                "FXML not found on classpath: " + fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        if (root != null) {
            loader.setRoot(root);
        }
        loader.load();
        return loader.getRoot();
    }

    @SuppressWarnings("unchecked")
    public static <T> LoadResult<T> loadWithController(String fxmlPath) throws IOException {
        URL url = Objects.requireNonNull(
                FxmlLoader.class.getResource(fxmlPath),
                "FXML not found on classpath: " + fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        Parent parent = loader.load();
        return new LoadResult<>(parent, (T) loader.getController());
    }

    public record LoadResult<T>(Parent root, T controller) {
    }
}
