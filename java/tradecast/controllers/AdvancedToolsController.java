package tradecast.controllers;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AdvancedToolsController {

    private static final double DEFAULT_ADVANCED_DIVIDER = 0.44;
    private static final double DEFAULT_DEPTH_DIVIDER = 0.50;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    @FXML
    private SplitPane advancedSplitPane;

    @FXML
    private SplitPane depthSplitPane;

    @FXML
    private TableView<DepthRow> depthByOrderTable;

    @FXML
    private TableColumn<DepthRow, String> orderVolumeColumn;

    @FXML
    private TableColumn<DepthRow, String> orderPriceColumn;

    @FXML
    private TableColumn<DepthRow, String> orderFlagColumn;

    @FXML
    private TableView<DepthRow> depthByPriceTable;

    @FXML
    private TableColumn<DepthRow, String> priceVolumeColumn;

    @FXML
    private TableColumn<DepthRow, String> pricePriceColumn;

    @FXML
    private TableColumn<DepthRow, String> priceFlagColumn;

    @FXML
    private ListView<String> activityMessagesList;

    @FXML
    private ListView<String> orderMessagesList;

    @FXML
    private ListView<String> announcementMessagesList;

    @FXML
    private ListView<String> newsMessagesList;

    @FXML
    private void initialize() {
        bindDepthColumns(orderVolumeColumn, orderPriceColumn, orderFlagColumn);
        bindDepthColumns(priceVolumeColumn, pricePriceColumn, priceFlagColumn);

        depthByOrderTable.setItems(FXCollections.observableArrayList(mockDepthByOrder()));
        depthByPriceTable.setItems(FXCollections.observableArrayList(mockDepthByPrice()));
        depthByOrderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        depthByPriceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        activityMessagesList.setItems(FXCollections.observableArrayList(mockActivityMessages()));
        orderMessagesList.setItems(FXCollections.observableArrayList(mockOrderMessages()));
        announcementMessagesList.setItems(FXCollections.observableArrayList(mockAnnouncementMessages()));
        newsMessagesList.setItems(FXCollections.observableArrayList(mockNewsMessages()));

        applyDenseMessageCellFactory(activityMessagesList);
        applyDenseMessageCellFactory(orderMessagesList);
        applyDenseMessageCellFactory(announcementMessagesList);
        applyDenseMessageCellFactory(newsMessagesList);

        restoreDefaultLayout();
    }

    public void restoreDefaultLayout() {
        if (advancedSplitPane != null && !advancedSplitPane.getItems().isEmpty()) {
            advancedSplitPane.setDividerPositions(DEFAULT_ADVANCED_DIVIDER);
            advancedSplitPane.getItems().forEach(node -> {
                node.setVisible(true);
                node.setManaged(true);
            });
        }
        if (depthSplitPane != null && !depthSplitPane.getItems().isEmpty()) {
            depthSplitPane.setDividerPositions(DEFAULT_DEPTH_DIVIDER);
            depthSplitPane.getItems().forEach(node -> {
                node.setVisible(true);
                node.setManaged(true);
            });
        }
    }

    private static void bindDepthColumns(
            TableColumn<DepthRow, String> volumeColumn,
            TableColumn<DepthRow, String> priceColumn,
            TableColumn<DepthRow, String> flagColumn) {
        volumeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().volume()));
        priceColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().price()));
        flagColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().flag()));
    }

    private static void applyDenseMessageCellFactory(ListView<String> listView) {
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });
    }

    private static List<DepthRow> mockDepthByOrder() {
        return List.of(
                depth(9500, 274.10, "B"),
                depth(7400, 274.05, "B"),
                depth(6100, 274.00, "B"),
                depth(4800, 273.95, "B"),
                depth(3700, 273.90, "B"),
                depth(2900, 274.15, "S"),
                depth(4100, 274.20, "S"),
                depth(5300, 274.25, "S"),
                depth(6800, 274.30, "S"),
                depth(8200, 274.35, "S"));
    }

    private static List<DepthRow> mockDepthByPrice() {
        return List.of(
                depth(12000, 811.40, "B"),
                depth(10400, 811.35, "B"),
                depth(9300, 811.30, "B"),
                depth(7000, 811.25, "B"),
                depth(5600, 811.20, "B"),
                depth(5200, 811.45, "S"),
                depth(7400, 811.50, "S"),
                depth(8800, 811.55, "S"),
                depth(9900, 811.60, "S"),
                depth(11100, 811.65, "S"));
    }

    private static List<String> mockActivityMessages() {
        return List.of(
                log("- terminal initialized for market monitor context"),
                log("- watch profile 1 loaded with 14 symbols"),
                log("- symbols refreshed from static snapshot cache"),
                log("- no broker connectivity in this prototype"),
                log("- render cycle complete: depth + order tabs updated"),
                log("- heartbeat: local mock session still active"));
    }

    private static List<String> mockOrderMessages() {
        return List.of(
                log("ORDER NEW      id=MOCK-23091 side=BUY qty=1000 px=274.10"),
                log("ORDER ACK      id=MOCK-23091 venue=REG status=ACCEPTED"),
                log("ORDER NEW      id=MOCK-23092 side=SELL qty=500 px=274.30"),
                log("ORDER REPLACE  id=MOCK-23091 qty=1200 px=274.05"),
                log("ORDER CANCEL   id=MOCK-23092 reason=User request (mock)"),
                log("ORDER STATUS   id=MOCK-23091 leaves=1200 cum=0 state=WORKING"));
    }

    private static List<String> mockAnnouncementMessages() {
        return List.of(
                log("SYSTEM NOTICE  Daily maintenance window: 23:00-23:15"),
                log("SYSTEM NOTICE  Margin table review completed (mock update)"),
                log("EXCHANGE NOTE  Test bulletin feed (frontend demo only)"),
                log("BROKER NOTE    Settlement cycle reminder T+2"),
                log("CLIENT ALERT   Account profile update pending verification"),
                log("SYSTEM NOTICE  Session timers synchronized"));
    }

    private static List<String> mockNewsMessages() {
        return List.of(
                log("NEWS  Energy sector leads gains in late trading"),
                log("NEWS  Textile composite index closes slightly higher"),
                log("NEWS  Banking names trade mixed on moderate volume"),
                log("NEWS  Cement stocks show defensive rotation"),
                log("NEWS  Currency desk reports narrow intraday range"),
                log("NEWS  Macro calendar light for next session"));
    }

    private static String log(String text) {
        return TIME_FMT.format(LocalTime.now()) + "  " + text;
    }

    private static DepthRow depth(long volume, double price, String flag) {
        return new DepthRow(
                String.format(Locale.US, "%,d", volume),
                String.format(Locale.US, "%.2f", price),
                flag);
    }

    private record DepthRow(String volume, String price, String flag) {
    }
}
