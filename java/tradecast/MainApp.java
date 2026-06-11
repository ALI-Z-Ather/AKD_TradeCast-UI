package tradecast;

import tradecast.utils.FxmlLoader;
import tradecast.utils.SceneStyling;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static final String LOGIN_FXML = "/fxml/LoginView.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Application.setUserAgentStylesheet(null);

        Parent root = FxmlLoader.loadRoot(LOGIN_FXML);
        Scene scene = new Scene(root, 560, 420);
        SceneStyling.applyLegacy(scene);

        primaryStage.setTitle("AKD TradeCast");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
