package tradecast.utils;

import java.io.IOException;

import javafx.scene.Scene;
import javafx.stage.Stage;

public final class AppNavigation {

    private static final String DASHBOARD_FXML = "/fxml/DashboardView.fxml";

    private AppNavigation() {
    }

    public static void openDashboard(Stage primaryStage) throws IOException {
        var loaded = FxmlLoader.loadWithController(DASHBOARD_FXML);
        Scene scene = new Scene(loaded.root(), 1024, 640);
        SceneStyling.applyLegacy(scene);
        primaryStage.setTitle("AKD TradeCast — Market Monitor");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(500);
    }
}
