package tradecast.controllers;

import java.util.Locale;

import tradecast.data.MockCashWithdrawalData;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;

public class CashWithdrawalRequestController {

    @FXML
    private ComboBox<String> accountCombo;

    @FXML
    private PasswordField pinField;

    @FXML
    private TextField availableAmountField;

    @FXML
    private TextField withdrawalAmountField;

    @FXML
    private TextField remainingAmountField;

    @FXML
    private RadioButton chequeDeliveryRadio;

    @FXML
    private RadioButton chequePickupRadio;

    @FXML
    private RadioButton onlineTransferRadio;

    @FXML
    private TextArea commentsArea;

    @FXML
    private ToggleGroup withdrawalModeGroup;

    private double currentAvailable;

    @FXML
    private void initialize() {
        accountCombo.getItems().setAll(MockCashWithdrawalData.accountNumbersForRequest());
        accountCombo.getSelectionModel().selectFirst();

        pinField.setTextFormatter(new TextFormatter<>(change -> {
            String t = change.getControlNewText();
            if (t.matches("\\d*") && t.length() <= 4) {
                return change;
            }
            return null;
        }));

        accountCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> onAccountChanged());
        withdrawalAmountField.textProperty().addListener((obs, oldV, newV) -> refreshRemaining());

        onAccountChanged();
    }

    private void onAccountChanged() {
        String acc = accountCombo.getSelectionModel().getSelectedItem();
        currentAvailable = acc == null ? 0.0 : MockCashWithdrawalData.availableForAccount(acc);
        availableAmountField.setText(formatMoney(currentAvailable));
        refreshRemaining();
    }

    private void refreshRemaining() {
        double w = parseWithdrawalAmount();
        if (Double.isNaN(w)) {
            remainingAmountField.setText("—");
            return;
        }
        remainingAmountField.setText(formatMoney(currentAvailable - w));
    }

    private double parseWithdrawalAmount() {
        String raw = withdrawalAmountField.getText();
        if (raw == null || raw.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(raw.trim().replace(",", ""));
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    private static String formatMoney(double v) {
        return String.format(Locale.US, "%.2f", v);
    }

    @FXML
    private void onSubmit() {
        String pin = pinField.getText();
        if (pin == null || pin.length() != 4 || !pin.chars().allMatch(Character::isDigit)) {
            showWarning("PIN is required and must be exactly 4 digits.");
            return;
        }

        double withdrawal = parseWithdrawalAmount();
        if (Double.isNaN(withdrawal)) {
            showWarning("Withdrawal amount must be a valid number.");
            return;
        }
        if (withdrawal <= 0) {
            showWarning("Withdrawal amount must be greater than 0.");
            return;
        }
        if (withdrawal > currentAvailable) {
            showWarning("Withdrawal amount cannot exceed the available amount.");
            return;
        }

        if (withdrawalModeGroup == null
                || withdrawalModeGroup.getSelectedToggle() == null) {
            showWarning("Please select a withdrawal mode.");
            return;
        }

        String mode = selectedModeLabel();
        Alert ok = new Alert(AlertType.INFORMATION);
        ok.setTitle("Cash Withdrawal Request");
        ok.setHeaderText(null);
        ok.setContentText("Request submitted (mock). Account: " + accountCombo.getSelectionModel().getSelectedItem()
                + ", Amount: " + formatMoney(withdrawal) + ", Mode: " + mode + ". No payment is processed.");
        ok.showAndWait();
    }

    private String selectedModeLabel() {
        if (chequeDeliveryRadio.isSelected()) {
            return "Cheque Delivery";
        }
        if (chequePickupRadio.isSelected()) {
            return "Cheque Pickup";
        }
        if (onlineTransferRadio.isSelected()) {
            return "Online Fund Transfer";
        }
        return "";
    }

    private static void showWarning(String message) {
        Alert a = new Alert(AlertType.WARNING);
        a.setTitle("Cash Withdrawal Request");
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
