package com.example.higherlower;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.*;

public class higherLowerController implements Initializable {

    private itemService itemService;
    private scoreManager scoreManager;
    @FXML
    private Label item1Label;

    @FXML
    private Label item2Label;

    @FXML
    private Button item1Button;

    @FXML
    private Button item2Button;

    @FXML
    private Label highScoreLabel;

    @FXML
    private Label currScoreLabel;

    @FXML
    private Label answerLabel;

    //private List<item> items = new ArrayList<>();
    //private List<item> currentItemList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        itemService = new itemService();
        //itemService.chooseList("Rust");
        scoreManager = new scoreManager();
        try {
            scraper.scrapeRustClashForItems("data/RustItemsList.json");
            System.out.println("Scraped");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

            try {
                itemService.add2ItemsToList();
                itemService.add2ItemsToList();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        new Thread(() -> {
            // Wait until at least 2 items are in the list
            while (itemService.getItemsLength() < 2) {
                try {
                    Thread.sleep(100); // Check every 100ms
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // Update UI on the JavaFX Application Thread
            Platform.runLater(this::updateUI);
        }).start();
    }

    @FXML
    protected void handleItem1Action() throws FileNotFoundException {
        processButtonAction(true);
    }

    @FXML
    protected void handleItem2Action() throws FileNotFoundException {
        processButtonAction(false);
    }

    @FXML
    protected void nextButtonAction() throws IOException {
        System.out.println(itemService.getItemsLength());
        System.out.println(itemService.returnItems());
    }

    private void processButtonAction(boolean isButton1) throws FileNotFoundException {

        boolean correct = itemService.correctButtonPressed(isButton1);
        if (correct){
            answerLabel.setText("Correct!");
        }else {
            answerLabel.setText("Wrong!");
        }
        scoreManager.updateScore(correct);
        itemService.remove2FromList();
        updateUI();
        itemService.add2ItemsToList();
    }

    private void updateUI() {
        currScoreLabel.setText("Current Score: " + scoreManager.getCurrentScore());
        highScoreLabel.setText("High Score: " + scoreManager.getHighScore());
        updateItemLabels();
    }

    private void updateItemLabels() {
        List<item> items = itemService.returnItems();
        item1Label.setText(items.get(0).getName());
        item2Label.setText(items.get(1).getName());
        setImage(items.get(0).getImageUrl(), item1Button);
        setImage(items.get(1).getImageUrl(), item2Button);

    }

    private void setImage(String imageUrl, Button button) {
        Image image = new Image(imageUrl);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50); // Set your preferred width
        imageView.setFitHeight(50); // Set your preferred height
        button.setGraphic(imageView);
    }

}


