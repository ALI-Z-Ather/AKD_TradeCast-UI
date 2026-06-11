package tradecast.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import tradecast.data.MockCashWithdrawalData;
import tradecast.models.CashWithdrawalStatementRow;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class CashWithdrawalStatementController {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    private final List<CashWithdrawalStatementRow> masterRows = MockCashWithdrawalData.allStatementRows();

    @FXML
    private ComboBox<String> accountCombo;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField pinField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TableView<CashWithdrawalStatementRow> statementTable;

    @FXML
    private Label recordsFoundLabel;

    @FXML
    private Label asOfDateLabel;

    @FXML
    private void initialize() {
        accountCombo.getItems().setAll(MockCashWithdrawalData.accountNumbersForStatement());
        accountCombo.getSelectionModel().selectFirst();

        statementTable.getColumns().setAll(
                textColumn("Requested Date", 96, r -> r.getRequestedDate().format(DATE_FMT)),
                moneyColumn("Amount Requested", 100, CashWithdrawalStatementRow::getAmountRequested),
                moneyColumn("Amount Approved", 100, CashWithdrawalStatementRow::getAmountApproved),
                textColumn("Withdrawal Mode", 120, CashWithdrawalStatementRow::getWithdrawalMode),
                textColumn("Comments", 140, CashWithdrawalStatementRow::getComments),
                statusColumn("Status", 88));
        statementTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        onGet();
    }

    @FXML
    private void onGet() {
        String pin = pinField.getText();
        if (pin != null && !pin.isBlank()) {
            if (pin.length() != 4 || !pin.chars().allMatch(Character::isDigit)) {
                Alert a = new Alert(AlertType.WARNING);
                a.setTitle("Cash Withdrawal Statement");
                a.setHeaderText(null);
                a.setContentText("If you enter a PIN, it must be exactly 4 digits.");
                a.showAndWait();
                return;
            }
        }

        String acc = accountCombo.getSelectionModel().getSelectedItem();
        String name = nameField.getText();
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();

        List<CashWithdrawalStatementRow> filtered = masterRows.stream()
                .filter(r -> matchesFilters(r, acc, name, start, end))
                .collect(Collectors.toList());

        statementTable.setItems(FXCollections.observableArrayList(filtered));
        recordsFoundLabel.setText("Total records found: " + filtered.size());
        asOfDateLabel.setText("Current date: " + LocalDate.now().format(DATE_FMT));
    }

    private static boolean matchesFilters(
            CashWithdrawalStatementRow r,
            String account,
            String name,
            LocalDate start,
            LocalDate end) {
        if (account != null && !account.isBlank() && !"(All)".equalsIgnoreCase(account.trim())) {
            if (!account.trim().equalsIgnoreCase(r.getAccountNo())) {
                return false;
            }
        }
        if (name != null && !name.isBlank()) {
            if (!r.getClientName().toLowerCase(Locale.ROOT).contains(name.trim().toLowerCase(Locale.ROOT))) {
                return false;
            }
        }
        LocalDate d = r.getRequestedDate();
        if (start != null && d.isBefore(start)) {
            return false;
        }
        if (end != null && d.isAfter(end)) {
            return false;
        }
        return true;
    }

    private TableColumn<CashWithdrawalStatementRow, String> textColumn(
            String title, int width, java.util.function.Function<CashWithdrawalStatementRow, String> value) {
        TableColumn<CashWithdrawalStatementRow, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(data -> new SimpleStringProperty(value.apply(data.getValue())));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setTextFill(Color.BLACK);
            }
        });
        return col;
    }

    private TableColumn<CashWithdrawalStatementRow, String> moneyColumn(
            String title, int width, java.util.function.Function<CashWithdrawalStatementRow, Double> value) {
        TableColumn<CashWithdrawalStatementRow, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(data -> new SimpleStringProperty(
                String.format(Locale.US, "%.2f", value.apply(data.getValue()))));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setTextFill(Color.BLACK);
            }
        });
        return col;
    }

    private TableColumn<CashWithdrawalStatementRow, String> statusColumn(String title, int width) {
        TableColumn<CashWithdrawalStatementRow, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                setText(item);
                CashWithdrawalStatementRow row = getTableRow() != null ? getTableRow().getItem() : null;
                if (row == null) {
                    setTextFill(Color.BLACK);
                    return;
                }
                switch (row.getStatus()) {
                    case "Approved" -> setTextFill(Color.web("#006400"));
                    case "Declined" -> setTextFill(Color.web("#cc0000"));
                    case "In-Process" -> setTextFill(Color.web("#0a246a"));
                    default -> setTextFill(Color.BLACK);
                }
            }
        });
        return col;
    }
}
