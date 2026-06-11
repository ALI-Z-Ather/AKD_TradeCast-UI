package tradecast.utils;

import javafx.scene.Scene;

public final class SceneStyling {

    private static final String LEGACY_CSS = "/css/legacy.css";

    private SceneStyling() {
    }

    public static void applyLegacy(Scene scene) {
        var url = SceneStyling.class.getResource(LEGACY_CSS);
        if (url != null) {
            scene.getStylesheets().add(url.toExternalForm());
        }
    }
}
