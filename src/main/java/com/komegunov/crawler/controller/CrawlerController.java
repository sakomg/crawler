package com.komegunov.crawler.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.komegunov.crawler.spider.Spider;
import com.komegunov.crawler.spider.SpiderLeg;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CrawlerController {

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
    private JFXButton openCsvFile;

    @FXML
    private JFXProgressBar progressLoadFindLinks;

    private File file;

    private final Desktop desktop = Desktop.getDesktop();

    private static final String LINKS_CSV_FILE = "D:\\jdev\\crawler\\src\\main\\resources\\links\\data.csv";

    @FXML
    public void initialize() {
        Spider spider = new Spider();
        SpiderLeg spiderLeg = new SpiderLeg();
        FileChooser fileChooser = new FileChooser();

        maxPages.getItems().addAll(1, 2, 5, 10, 20);
        maxPages.setPromptText("Select pages");

        startSearch.setOnAction(action -> {
            final String textStartPage = startPage.getText();
            final String searchFieldText = searchText.getText();
            final int comboMaxPages = maxPages.getValue();
            if (textStartPage.equals("") || searchFieldText.equals("") || maxPages.getItems().isEmpty()) {
                showAlertWithoutHeaderText();
            } else {
                try {
                    clearResultList();
                    spiderLeg.crawl(textStartPage);
                    spiderLeg.countSearchForWord(searchFieldText);
                    spider.checkQuantitySearchWord(searchFieldText, textStartPage);
                    spider.search(textStartPage, comboMaxPages);
                    printOnList(spiderLeg.connectToWebPage(textStartPage));
                    printOnList(spiderLeg.crawl(textStartPage));
                    printOnList(spiderLeg.checkNullHtmlDocument(searchFieldText));
                    printOnList(spiderLeg.displayCountSearchWord(searchFieldText));
                    printOnList(spider.checkQuantitySearchWord(searchFieldText, textStartPage));
                    printOnList(spider.search(textStartPage, comboMaxPages));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.csv"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        openCsvFile.setOnAction(action -> {
            file = fileChooser.showOpenDialog(openCsvFile.getScene().getWindow());
            if (file != null) {
                try {
                    desktop.open(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        fileChooser.setInitialDirectory(new File(LINKS_CSV_FILE));
        fileChooser.setTitle("Open CSV file");

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

    public void clearResultList() {
        resultList.clear();
    }
}
