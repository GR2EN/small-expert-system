package com.jater.ses.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class HomeController {

    @FXML private MenuItem openFileButton;
    @FXML private MenuItem aboutAuthorButton;
    @FXML private Label hypothesesLabel;
    @FXML private Label questionsLabel;
    @FXML private ListView<?> hypotheses;
    @FXML private ListView<?> questions;
    @FXML private Label question;
    @FXML private Button yesButton;
    @FXML private Button noButton;
    @FXML private Button notSureButton;

    private void showComponents() {
        hypothesesLabel.setVisible(true);
        hypotheses.setVisible(true);
        questionsLabel.setVisible(true);
        questions.setVisible(true);
        question.setVisible(true);
        yesButton.setVisible(true);
        noButton.setVisible(true);
        notSureButton.setVisible(true);
    }

    @FXML
    private void showAboutAuthor() {
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
    }

    @FXML
    void initialize() {
        openFileButton.setOnAction(actionEvent -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("База знаний экспертной системы", "*.mkb"));
            File file = fc.showOpenDialog(null);
            if (file != null) {
                showComponents();
            }
        });
    }

}
