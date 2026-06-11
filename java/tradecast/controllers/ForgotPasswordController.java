package tradecast.controllers;

import tradecast.utils.ValidationUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ForgotPasswordController {

    @FXML
    private TextField userIdField;

    @FXML
    private TextField emailField;

    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {
        hideError();
    }

    @FXML
    private void onOk() {
        hideError();

        if (ValidationUtils.isNullOrBlank(userIdField.getText())) {
            showError("Please enter your User ID.");
            return;
        }
        if (ValidationUtils.isNullOrBlank(emailField.getText())) {
            showError("Please enter your Email Address.");
            return;
        }
        if (!ValidationUtils.isValidEmail(emailField.getText())) {
            showError("Please enter a valid email address (e.g. name@company.com).");
            return;
        }

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Forgot Password");
        info.setHeaderText(null);
        info.setContentText("Your request has been recorded (mock). No email is sent.");
        info.showAndWait();
        closeDialog();
    }

    @FXML
    private void onCancel() {
        closeDialog();
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

    private void closeDialog() {
        Stage stage = (Stage) userIdField.getScene().getWindow();
        stage.close();
    }
}
