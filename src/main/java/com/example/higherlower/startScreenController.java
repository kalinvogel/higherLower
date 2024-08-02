package com.example.higherlower;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class startScreenController {

    @FXML
    private Button startButton;

    private itemService itemService;
    private scoreManager scoreManager;

    /*
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        itemService = new itemService();
        scoreManager = new scoreManager();

        // Load items in a background thread
        new Thread(() -> {
            try {
                scraper.scrapeRustClashForItems("data/RustItemsList.json");
                System.out.println("Scraped");

                itemService.addItemToList();
                itemService.addItemToList();
                itemService.addItemToList();
                itemService.addItemToList();

                // Optionally, update the UI on the JavaFX Application Thread if needed
                Platform.runLater(() -> {
                    // Code to update UI, if necessary
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
    */
    @FXML
    private void handleStartButtonAction() {
        try {
            // Load the higherLower FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("higherLower.fxml"));
            Parent root = loader.load();

            // Get the current stage (the one with the start screen)
            Stage stage = (Stage) startButton.getScene().getWindow();

            // Set the scene to the higherLower screen
            Scene higherLowerScene = new Scene(root);
            stage.setScene(higherLowerScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
