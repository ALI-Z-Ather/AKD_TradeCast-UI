package tradecast.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.util.Duration;

public class DashboardShellController {

    private static final DateTimeFormatter CLOCK_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");
    private static final double DEFAULT_MAIN_DIVIDER = 0.76;

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Tab orderHistoryTab;

    @FXML
    private AdvancedToolsController advancedToolsViewController;

    @FXML
    private MenuItem orderHistoryMenuItem;

    @FXML
    private Label statusConnectionLabel;

    @FXML
    private Label statusClockLabel;

    private Timeline clockTimeline;

    @FXML
    private void initialize() {
        statusConnectionLabel.setText("Status: Mock session (not connected to any exchange or broker API).");
        startStatusClock();
        if (orderHistoryMenuItem != null) {
            orderHistoryMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.ALT_DOWN));
        }
        if (mainTabPane != null) {
            mainTabPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.getAccelerators().put(
                            new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN),
                            this::onRestoreDefaultLayout);
                }
            });
        }
    }

    @FXML
    private void onMenuOrderHistory() {
        if (mainTabPane != null && orderHistoryTab != null) {
            mainTabPane.getSelectionModel().select(orderHistoryTab);
        }
    }

    private void startStatusClock() {
        if (clockTimeline != null) {
            clockTimeline.stop();
        }
        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> refreshClock()));
        clockTimeline.setCycleCount(Timeline.INDEFINITE);
        clockTimeline.play();
        refreshClock();
    }

    private void refreshClock() {
        if (statusClockLabel != null) {
            statusClockLabel.setText(LocalDateTime.now().format(CLOCK_FORMAT));
        }
    }

    @FXML
    private void onFileExit() {
        Platform.exit();
    }

    @FXML
    private void onToolbarNotImplemented() {
    }

    @FXML
    private void onRestoreDefaultLayout() {
        if (mainSplitPane != null) {
            mainSplitPane.setDividerPositions(DEFAULT_MAIN_DIVIDER);
            mainSplitPane.getItems().forEach(node -> {
                node.setVisible(true);
                node.setManaged(true);
            });
        }
        if (advancedToolsViewController != null) {
            advancedToolsViewController.restoreDefaultLayout();
        }
    }

    public SplitPane getMainSplitPane() {
        return mainSplitPane;
    }

    public TabPane getMainTabPane() {
        return mainTabPane;
    }
}
