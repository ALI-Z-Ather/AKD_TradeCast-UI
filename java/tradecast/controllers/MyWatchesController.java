package tradecast.controllers;

import java.util.Locale;
import java.util.Optional;

import tradecast.data.MockMarketQuotes;
import tradecast.models.MarketQuoteRow;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class MyWatchesController {

    private static final Color COLOR_CHANGE_DOWN = Color.web("#cc0000");
    private static final Color COLOR_CHANGE_UP = Color.web("#006400");
    private static final Color COLOR_BID = Color.web("#006400");
    private static final Color COLOR_ASK = Color.web("#b00000");
    private static final Color COLOR_DEFAULT = Color.BLACK;

    @FXML
    private TextField scripSearchField;

    @FXML
    private Label selectedScripLabel;

    @FXML
    private TableView<MarketQuoteRow> marketTable;

    @FXML
    private void initialize() {
        marketTable.setItems(FXCollections.observableArrayList(MockMarketQuotes.sampleRows()));
        marketTable.getColumns().setAll(
                textColumn("Mkt", 36, r -> r.getMkt()),
                textColumn("Scrip", 52, r -> r.getScrip()),
                moneyColumn("LastPrice", 64, r -> r.getLastPrice(), ColumnStyle.NEUTRAL),
                changeColumn("Change", 58, r -> r.getChange()),
                moneyColumn("Buy", 56, r -> r.getBuy(), ColumnStyle.BUY),
                longColumn("BVol", 52, r -> r.getBVol(), ColumnStyle.BUY),
                moneyColumn("Sell", 56, r -> r.getSell(), ColumnStyle.SELL),
                longColumn("SVol", 52, r -> r.getSVol(), ColumnStyle.SELL),
                moneyColumn("High", 56, r -> r.getHigh(), ColumnStyle.NEUTRAL),
                moneyColumn("Low", 56, r -> r.getLow(), ColumnStyle.NEUTRAL),
                longColumn("TotalVolume", 72, r -> r.getTotalVolume(), ColumnStyle.NEUTRAL),
                moneyColumn("Average", 60, r -> r.getAverage(), ColumnStyle.NEUTRAL),
                moneyColumn("ClosePrice", 64, r -> r.getClosePrice(), ColumnStyle.NEUTRAL),
                tradeColumn());
        marketTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private enum ColumnStyle {
        NEUTRAL, BUY, SELL
    }

    private TableColumn<MarketQuoteRow, String> textColumn(String title, int width, java.util.function.Function<MarketQuoteRow, String> value) {
        TableColumn<MarketQuoteRow, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(data -> new SimpleStringProperty(value.apply(data.getValue())));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTextFill(COLOR_DEFAULT);
                } else {
                    setText(item);
                    setTextFill(COLOR_DEFAULT);
                }
            }
        });
        return col;
    }

    private TableColumn<MarketQuoteRow, String> moneyColumn(
            String title, int width, java.util.function.Function<MarketQuoteRow, Double> value, ColumnStyle style) {
        TableColumn<MarketQuoteRow, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(data -> new SimpleStringProperty(formatMoney(value.apply(data.getValue()))));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTextFill(COLOR_DEFAULT);
                    return;
                }
                setText(item);
                setTextFill(switch (style) {
                    case BUY -> COLOR_BID;
                    case SELL -> COLOR_ASK;
                    default -> COLOR_DEFAULT;
                });
            }
        });
        return col;
    }

    private TableColumn<MarketQuoteRow, String> longColumn(
            String title, int width, java.util.function.Function<MarketQuoteRow, Long> value, ColumnStyle style) {
        TableColumn<MarketQuoteRow, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(data -> new SimpleStringProperty(
                String.format(Locale.US, "%,d", value.apply(data.getValue()))));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTextFill(COLOR_DEFAULT);
                    return;
                }
                setText(item);
                setTextFill(switch (style) {
                    case BUY -> COLOR_BID;
                    case SELL -> COLOR_ASK;
                    default -> COLOR_DEFAULT;
                });
            }
        });
        return col;
    }

    private TableColumn<MarketQuoteRow, String> changeColumn(String title, int width, java.util.function.Function<MarketQuoteRow, Double> value) {
        TableColumn<MarketQuoteRow, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(data -> new SimpleStringProperty(formatSignedMoney(value.apply(data.getValue()))));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTextFill(COLOR_DEFAULT);
                    return;
                }
                setText(item);
                MarketQuoteRow row = getTableRow() == null ? null : getTableRow().getItem();
                if (row == null) {
                    setTextFill(COLOR_DEFAULT);
                    return;
                }
                double ch = row.getChange();
                if (ch < 0) {
                    setTextFill(COLOR_CHANGE_DOWN);
                } else if (ch > 0) {
                    setTextFill(COLOR_CHANGE_UP);
                } else {
                    setTextFill(COLOR_DEFAULT);
                }
            }
        });
        return col;
    }

    private TableColumn<MarketQuoteRow, Void> tradeColumn() {
        TableColumn<MarketQuoteRow, Void> col = new TableColumn<>("Trade");
        col.setPrefWidth(58);
        col.setSortable(false);
        col.setCellFactory(c -> new TableCell<>() {
            private final Button btn = new Button("Trade");

            {
                btn.getStyleClass().add("table-trade-button");
                btn.setFocusTraversable(false);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                btn.setOnAction(ev -> {
                    MarketQuoteRow row = getTableRow().getItem();
                    if (row == null) {
                        return;
                    }
                    Alert dlg = new Alert(AlertType.INFORMATION);
                    dlg.setTitle("Trade");
                    dlg.setHeaderText(null);
                    dlg.setContentText("Trade window placeholder for " + row.getScrip() + " (" + row.getMkt() + ").");
                    dlg.showAndWait();
                });
                setGraphic(btn);
            }
        });
        return col;
    }

    private static String formatMoney(double v) {
        return String.format("%.2f", v);
    }

    private static String formatSignedMoney(double v) {
        return String.format("%+.2f", v);
    }

    @FXML
    private void onScripBrowse() {
        ObservableList<String> choices = FXCollections.observableArrayList(MockMarketQuotes.knownScrips());
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Scrip Browse");
        dialog.setHeaderText("Select a symbol (mock list)");
        dialog.setContentText("Scrip:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(sym -> selectedScripLabel.setText(sym + " — REG (from browse)"));
    }

    @FXML
    private void onScripSearchCommitted() {
        String raw = scripSearchField.getText();
        if (raw == null || raw.trim().isEmpty()) {
            selectedScripLabel.setText("—");
            return;
        }
        String sym = raw.trim().toUpperCase();
        for (MarketQuoteRow row : marketTable.getItems()) {
            if (row.getScrip().equalsIgnoreCase(sym)) {
                selectedScripLabel.setText(row.getScrip() + " — " + row.getMkt() + " (from search)");
                return;
            }
        }
        selectedScripLabel.setText(sym + " — not in table (mock)");
    }

    @FXML
    private void onSaveProfile() {
        Alert ok = new Alert(AlertType.INFORMATION);
        ok.setTitle("Save Profile");
        ok.setHeaderText(null);
        ok.setContentText("1st Profile saved (mock). No data is written to disk.");
        ok.showAndWait();
    }
}
