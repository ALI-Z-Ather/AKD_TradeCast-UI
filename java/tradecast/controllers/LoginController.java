package tradecast.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import tradecast.data.LoginMockCredentials;
import tradecast.utils.AppNavigation;
import tradecast.utils.FxmlLoader;
import tradecast.utils.OrdinalText;
import tradecast.utils.ValidationUtils;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController {

    private static final int PASSWORD_SLOTS = 10;
    private static final int CHALLENGE_POSITION_COUNT = 4;

    @FXML
    private TextField userIdField;

    @FXML
    private Label instructionLabel;

    @FXML
    private HBox passwordRow;

    @FXML
    private Label errorLabel;

    private final List<TextField> passwordBoxes = new ArrayList<>();
    private List<Integer> requiredOneBased = List.of();

    @FXML
    private void initialize() {
        buildPasswordBoxes();
        startNewChallenge();
        hideError();

        userIdField.textProperty().addListener((obs, oldV, newV) -> hideError());
    }

    private void buildPasswordBoxes() {
        for (int i = 0; i < PASSWORD_SLOTS; i++) {
            TextField box = new TextField();
            box.getStyleClass().add("password-char-box");
            box.setMaxWidth(32);
            box.setPrefWidth(28);
            box.setAlignment(Pos.CENTER);
            box.setPromptText("·");
            int slotIndex = i;
            box.setTextFormatter(new TextFormatter<>(change -> {
                String next = change.getControlNewText();
                if (next.length() <= 1) {
                    return change;
                }
                return null;
            }));
            box.textProperty().addListener((obs, oldV, newV) -> {
                if (!requiredOneBased.contains(slotIndex + 1)) {
                    return;
                }
                hideError();
            });
            passwordBoxes.add(box);
            passwordRow.getChildren().add(box);
        }
    }

    private void startNewChallenge() {
        requiredOneBased = ThreadLocalRandom.current()
                .ints(1, PASSWORD_SLOTS + 1)
                .distinct()
                .limit(CHALLENGE_POSITION_COUNT)
                .sorted()
                .boxed()
                .collect(Collectors.toList());

        String ordinals = OrdinalText.joinWithAmpersand(requiredOneBased);
        instructionLabel.setText(
                "Please enter " + ordinals + " character of your password.");

        for (int i = 0; i < PASSWORD_SLOTS; i++) {
            TextField box = passwordBoxes.get(i);
            boolean active = requiredOneBased.contains(i + 1);
            box.setDisable(!active);
            box.setEditable(active);
            box.clear();
            if (!active) {
                box.getStyleClass().remove("password-char-active");
                if (!box.getStyleClass().contains("password-char-disabled")) {
                    box.getStyleClass().add("password-char-disabled");
                }
            } else {
                box.getStyleClass().remove("password-char-disabled");
                if (!box.getStyleClass().contains("password-char-active")) {
                    box.getStyleClass().add("password-char-active");
                }
            }
        }
    }

    @FXML
    private void onLogin() {
        hideError();

        if (ValidationUtils.isNullOrBlank(userIdField.getText())) {
            showError("Please enter your User ID and each requested password character.");
            return;
        }
        for (int pos : requiredOneBased) {
            TextField box = passwordBoxes.get(pos - 1);
            if (ValidationUtils.isNullOrBlank(box.getText())) {
                showError("Please enter your User ID and each requested password character.");
                return;
            }
        }

        if (!isMockLoginValid()) {
            showError("Invalid login credentials!");
            clearActivePasswordInputs();
            return;
        }

        Stage stage = (Stage) userIdField.getScene().getWindow();
        try {
            AppNavigation.openDashboard(stage);
        } catch (IOException e) {
            Alert err = new Alert(AlertType.ERROR);
            err.setTitle("Login");
            err.setHeaderText("Could not open the main window.");
            err.setContentText(e.getMessage() != null ? e.getMessage() : e.toString());
            err.showAndWait();
        }
    }

    private boolean isMockLoginValid() {
        String userId = userIdField.getText().trim();
        if (!LoginMockCredentials.MOCK_USER_ID.equals(userId)) {
            return false;
        }
        String full = LoginMockCredentials.MOCK_PASSWORD;
        for (int pos : requiredOneBased) {
            String ch = passwordBoxes.get(pos - 1).getText().trim();
            if (ch.length() != 1) {
                return false;
            }
            if (full.charAt(pos - 1) != ch.charAt(0)) {
                return false;
            }
        }
        return true;
    }

    private void clearActivePasswordInputs() {
        for (int pos : requiredOneBased) {
            passwordBoxes.get(pos - 1).clear();
        }
    }

    @FXML
    private void onCancel() {
        Platform.exit();
    }

    @FXML
    private void onForgotPassword() {
        hideError();
        try {
            FxmlLoader.LoadResult<ForgotPasswordController> loaded =
                    FxmlLoader.loadWithController("/fxml/ForgotPasswordView.fxml");
            Scene scene = new Scene(loaded.root(), 420, 260);
            var css = getClass().getResource("/css/legacy.css");
            if (css != null) {
                scene.getStylesheets().add(css.toExternalForm());
            }
            Stage owner = (Stage) userIdField.getScene().getWindow();
            Stage dialog = new Stage();
            dialog.initOwner(owner);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle("Forgot Password");
            dialog.setResizable(false);
            dialog.setScene(scene);
            dialog.showAndWait();
        } catch (IOException e) {
            showError("Could not open Forgot Password window.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
}
