package tradecast.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import tradecast.data.MockOrderHistoryData;
import tradecast.models.OrderHistoryRow;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;

public class OrderHistoryController {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final Color COLOR_DEFAULT = Color.BLACK;

    private final List<OrderHistoryRow> masterRows = MockOrderHistoryData.allRows();

    @FXML private ComboBox<String> accountCombo;
    @FXML private ComboBox<String> nameCombo;
    @FXML private ComboBox<String> typeCombo;
    @FXML private ComboBox<String> scripCombo;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TableView<OrderHistoryRow> historyTable;
    @FXML private Label recordsFoundLabel;
    @FXML private Label currentDateLabel;

    @FXML
    private void initialize() {
        accountCombo.getItems().setAll(MockOrderHistoryData.accountNumbers());
        accountCombo.getSelectionModel().selectFirst();
        nameCombo.getItems().setAll(MockOrderHistoryData.clientNames());
        nameCombo.getSelectionModel().selectFirst();
        typeCombo.getItems().setAll(MockOrderHistoryData.orderTypes());
        typeCombo.getSelectionModel().selectFirst();
        scripCombo.getItems().setAll(MockOrderHistoryData.scrips());
        scripCombo.getSelectionModel().selectFirst();
        historyTable.getColumns().setAll(
                textColumn("Scrip", 64, OrderHistoryRow::getScrip),
                longColumn("Quantity", 68, OrderHistoryRow::getQuantity),
                moneyColumn("Gross Rate", 72, OrderHistoryRow::getGrossRate),
                moneyColumn("Net Amount", 84, OrderHistoryRow::getNetAmount),
                dateColumn("Clearing Date", 96, OrderHistoryRow::getClearingDate));
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        onGet();
    }

    @FXML
    private void onGet() {
        String acc = accountCombo.getSelectionModel().getSelectedItem();
        String nameRaw = nameCombo.getEditor().getText();
        String name = (nameRaw != null && !nameRaw.isBlank()) ? nameRaw : nameCombo.getSelectionModel().getSelectedItem();
        String type = typeCombo.getSelectionModel().getSelectedItem();
        String scrip = scripCombo.getSelectionModel().getSelectedItem();
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        List<OrderHistoryRow> filtered = masterRows.stream()
                .filter(r -> matchesAll(r, acc, name, type, scrip, start, end))
                .collect(Collectors.toList());
        historyTable.setItems(FXCollections.observableArrayList(filtered));
        recordsFoundLabel.setText("Total records found: " + filtered.size());
        currentDateLabel.setText("Current date: " + LocalDate.now().format(DATE_FMT));
    }

    private static boolean matchesAll(OrderHistoryRow r, String acc, String name, String type, String scrip, LocalDate start, LocalDate end) {
        if (!matchesToken(acc, r.getAccountNo())) return false;
        if (!matchesName(name, r.getClientName())) return false;
        if (!matchesToken(type, r.getOrderType())) return false;
        if (!matchesToken(scrip, r.getScrip())) return false;
        return matchesDateRange(r.getClearingDate(), start, end);
    }

    private static boolean matchesToken(String selected, String rowValue) {
        if (selected == null || selected.isBlank() || "(All)".equalsIgnoreCase(selected.trim())) return true;
        return selected.trim().equalsIgnoreCase(rowValue);
    }

    private static boolean matchesName(String selected, String rowName) {
        if (selected == null || selected.isBlank() || "(All)".equalsIgnoreCase(selected.trim())) return true;
        return rowName.toLowerCase(Locale.ROOT).contains(selected.trim().toLowerCase(Locale.ROOT));
    }

    private static boolean matchesDateRange(LocalDate clearing, LocalDate start, LocalDate end) {
        if (start != null && clearing.isBefore(start)) return false;
        if (end != null && clearing.isAfter(end)) return false;
        return true;
    }

    private TableColumn<OrderHistoryRow, String> textColumn(String title, int width, java.util.function.Function<OrderHistoryRow, String> value) {
        TableColumn<OrderHistoryRow, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(data -> new SimpleStringProperty(value.apply(data.getValue())));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                if (!empty && item != null) setTextFill(COLOR_DEFAULT);
            }
        });
        return col;
    }

    private TableColumn<OrderHistoryRow, String> longColumn(String title, int width, java.util.function.Function<OrderHistoryRow, Long> value) {
        TableColumn<OrderHistoryRow, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(data -> new SimpleStringProperty(String.format(Locale.US, "%,d", value.apply(data.getValue()))));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                if (!empty && item != null) setTextFill(COLOR_DEFAULT);
            }
        });
        return col;
    }

    private TableColumn<OrderHistoryRow, String> moneyColumn(String title, int width, java.util.function.Function<OrderHistoryRow, Double> value) {
        TableColumn<OrderHistoryRow, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(data -> new SimpleStringProperty(String.format(Locale.US, "%.2f", value.apply(data.getValue()))));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                if (!empty && item != null) setTextFill(COLOR_DEFAULT);
            }
        });
        return col;
    }

    private TableColumn<OrderHistoryRow, String> dateColumn(String title, int width, java.util.function.Function<OrderHistoryRow, LocalDate> value) {
        TableColumn<OrderHistoryRow, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(data -> new SimpleStringProperty(value.apply(data.getValue()).format(DATE_FMT)));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                if (!empty && item != null) setTextFill(COLOR_DEFAULT);
            }
        });
        return col;
    }

    @FXML
    private void onExportPlaceholder() {
        Alert a = new Alert(AlertType.INFORMATION);
        a.setTitle("Export");
        a.setHeaderText(null);
        a.setContentText("Export to PDF (mock) — no file is written.");
        a.showAndWait();
    }

    @FXML
    private void onPrintPlaceholder() {
        Alert a = new Alert(AlertType.INFORMATION);
        a.setTitle("Print");
        a.setHeaderText(null);
        a.setContentText("Print (mock) — no printer output.");
        a.showAndWait();
    }
}
