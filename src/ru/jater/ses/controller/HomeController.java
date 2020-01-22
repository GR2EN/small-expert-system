package ru.jater.ses.controller;

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
import ru.jater.ses.util.FileReader;

import java.io.File;
import java.io.IOException;

public class HomeController {

    @FXML private MenuItem openFileButton;
    @FXML private ListView<String> hypotheses;
    @FXML private ListView<String> questions;
    @FXML private Label question;
    @FXML private Button noButton;
    @FXML private Button yesButton;
    @FXML private Button neutralButton;
    @FXML private Button ratherYesButton;
    @FXML private Button ratherNoButton;
    @FXML private Label aboutAuthor;
    @FXML private Button startButton;

    private void enableComponents() {
        hypotheses.setDisable(false);
        questions.setDisable(false);
        yesButton.setDisable(false);
        noButton.setDisable(false);
        neutralButton.setDisable(false);
        ratherYesButton.setDisable(false);
        ratherNoButton.setDisable(false);
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
    private void startTest() {
        startButton.setDisable(true);
        enableComponents();
    }

    @FXML
    void initialize() {
        openFileButton.setOnAction(actionEvent -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("База знаний экспертной системы", "*.mkb"));
            File file = fc.showOpenDialog(null);
            if (file != null) {
                startButton.setDisable(false);
                FileReader fileReader = new FileReader(file);
                aboutAuthor.setText(fileReader.getAuthor());
                fileReader.getQuestions().forEach(question -> questions.getItems().add(question));
                fileReader.getHypotheses().forEach(hypothese -> hypotheses.getItems().add(hypothese));
            }
        });
    }

}
