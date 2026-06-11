package tradecast.controllers;

import java.util.Locale;
import java.util.Optional;

import tradecast.data.MockPortfolioData;
import tradecast.models.PortfolioHoldingRow;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class PortfolioWatchController {

    private static final Color COLOR_PROFIT = Color.web("#006400");
    private static final Color COLOR_LOSS = Color.web("#cc0000");
    private static final Color COLOR_NEUTRAL = Color.BLACK;

    @FXML private TextField portfolioWorthField;
    @FXML private TextField ledgerBalanceField;
    @FXML private TextField soldCollateralsField;
    @FXML private TextField netWorthField;
    @FXML private TableView<PortfolioHoldingRow> holdingsTable;

    @FXML
    private void initialize() {
        ledgerBalanceField.setText(formatMoney(MockPortfolioData.LEDGER_BALANCE));
        soldCollateralsField.setText(formatMoney(MockPortfolioData.SOLD_COLLATERALS));
        holdingsTable.setItems(FXCollections.observableArrayList(MockPortfolioData.sampleHoldings()));
        holdingsTable.getColumns().setAll(
                textColumn("Scrip", 56, PortfolioHoldingRow::getScrip),
                longColumn("Net Quantity", 72, PortfolioHoldingRow::getNetQuantity),
                moneyColumn("Avg. Price", 68, PortfolioHoldingRow::getAvgPrice),
                moneyColumn("Current Price", 72, PortfolioHoldingRow::getCurrentPrice),
                moneyColumn("Current Worth", 80, PortfolioHoldingRow::getCurrentWorth),
                profitLossColumn("Profit/Loss", 76),
                tradeColumn());
        holdingsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        holdingsTable.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends PortfolioHoldingRow> c) ->
                refreshSummaries());
        refreshSummaries();
    }

    private void refreshSummaries() {
        double portfolioWorth = holdingsTable.getItems().stream()
                .mapToDouble(PortfolioHoldingRow::getCurrentWorth).sum();
        portfolioWorthField.setText(formatMoney(portfolioWorth));
        double netWorth = portfolioWorth + MockPortfolioData.LEDGER_BALANCE + MockPortfolioData.SOLD_COLLATERALS;
        netWorthField.setText(formatMoney(netWorth));
    }

    private TableColumn<PortfolioHoldingRow, String> textColumn(String title, int width, java.util.function.Function<PortfolioHoldingRow, String> value) {
        TableColumn<PortfolioHoldingRow, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(data -> new SimpleStringProperty(value.apply(data.getValue())));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setTextFill(COLOR_NEUTRAL);
            }
        });
        return col;
    }

    private TableColumn<PortfolioHoldingRow, String> longColumn(String title, int width, java.util.function.Function<PortfolioHoldingRow, Long> value) {
        TableColumn<PortfolioHoldingRow, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(data -> new SimpleStringProperty(String.format(Locale.US, "%,d", value.apply(data.getValue()))));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setTextFill(COLOR_NEUTRAL);
            }
        });
        return col;
    }

    private TableColumn<PortfolioHoldingRow, String> moneyColumn(String title, int width, java.util.function.Function<PortfolioHoldingRow, Double> value) {
        TableColumn<PortfolioHoldingRow, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(data -> new SimpleStringProperty(formatMoney(value.apply(data.getValue()))));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setTextFill(COLOR_NEUTRAL);
            }
        });
        return col;
    }

    private TableColumn<PortfolioHoldingRow, String> profitLossColumn(String title, int width) {
        TableColumn<PortfolioHoldingRow, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(data -> new SimpleStringProperty(formatSignedMoney(data.getValue().getProfitLoss())));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setTextFill(COLOR_NEUTRAL); return; }
                setText(item);
                PortfolioHoldingRow row = getTableRow() == null ? null : getTableRow().getItem();
                if (row == null) { setTextFill(COLOR_NEUTRAL); return; }
                double pl = row.getProfitLoss();
                if (pl > 0) setTextFill(COLOR_PROFIT);
                else if (pl < 0) setTextFill(COLOR_LOSS);
                else setTextFill(COLOR_NEUTRAL);
            }
        });
        return col;
    }

    private TableColumn<PortfolioHoldingRow, Void> tradeColumn() {
        TableColumn<PortfolioHoldingRow, Void> col = new TableColumn<>("Trade");
        col.setPrefWidth(58);
        col.setSortable(false);
        col.setCellFactory(c -> new TableCell<>() {
            private final Button btn = new Button("Trade");
            { btn.getStyleClass().add("table-trade-button"); btn.setFocusTraversable(false); }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                btn.setOnAction(ev -> {
                    PortfolioHoldingRow row = getTableRow().getItem();
                    if (row == null) return;
                    Alert dlg = new Alert(AlertType.INFORMATION);
                    dlg.setTitle("Trade");
                    dlg.setHeaderText(null);
                    dlg.setContentText("Trade window placeholder for " + row.getScrip() + " (portfolio).");
                    dlg.showAndWait();
                });
                setGraphic(btn);
            }
        });
        return col;
    }

    private static String formatMoney(double v) { return String.format(Locale.US, "%.2f", v); }
    private static String formatSignedMoney(double v) { return String.format(Locale.US, "%+.2f", v); }

    @FXML
    private void onLiquidateAll() {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Liquidate All");
        confirm.setHeaderText(null);
        confirm.setContentText("Remove all security holdings from this mock portfolio view?");
        Optional<ButtonType> answer = confirm.showAndWait();
        if (answer.isEmpty() || answer.get() != ButtonType.OK) return;
        holdingsTable.getItems().clear();
        refreshSummaries();
        Alert done = new Alert(AlertType.INFORMATION);
        done.setTitle("Liquidate All");
        done.setHeaderText(null);
        done.setContentText("All holdings cleared (mock). Ledger balance and sold collaterals unchanged.");
        done.showAndWait();
    }
}
