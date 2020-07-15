package com.komegunov.crawler.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.komegunov.crawler.spider.Spider;
import com.komegunov.crawler.spider.SpiderLeg;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CrawlerController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox headPane;

    @FXML
    private JFXTextField searchText;

    @FXML
    private JFXComboBox<Integer> maxPages;

    @FXML
    private JFXTextField startPage;

    @FXML
    private TextArea resultList;

    @FXML
    private JFXButton startSearch;

    @FXML
    private JFXButton stopSearch;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        Spider spider = new Spider();
        SpiderLeg spiderLeg = new SpiderLeg();

        maxPages.getItems().addAll(1,2,3,4,5,10,15,20,25);
        maxPages.setPromptText("Select pages");

        startSearch.setOnAction(action -> {
            final String textStartPage = startPage.getText();
            final String searchFieldText = searchText.getText();
            final int comboMaxPages = maxPages.getValue();
            if (startPage.getText().equals("") || searchText.getText().equals("")) {
                showAlertWithoutHeaderText();
            } else {
                try {
                    spider.search(textStartPage, searchFieldText, comboMaxPages);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        stopSearch.setOnAction(action -> {

        });
    }

    public void showAlertWithoutHeaderText() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning alert");
        alert.setHeaderText(null);
        alert.setContentText("Enter value.");
        alert.showAndWait();
    }

    public void printOnList(String str) {
        Platform.runLater(() -> resultList.appendText(str));
    }
}