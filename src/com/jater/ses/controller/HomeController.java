package com.jater.ses.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HomeController {

    @FXML
    private MenuItem openFileMenuItem;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    private ListView<?> hypothesesList;

    @FXML
    private ListView<?> questionsList;

    @FXML
    private Label questionLabel;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    @FXML
    private Button notSureButton;

    @FXML
    void initialize() {

        aboutMenuItem.setOnAction(actionEvent -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../view/about.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setAlwaysOnTop(true);
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
