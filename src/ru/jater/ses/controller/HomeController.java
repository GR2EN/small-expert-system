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
import java.util.ArrayList;

public class HomeController {

    private static final double YES = 1.00;
    private static final double RATHER_YES = 0.75;
    private static final double NOT_SURE = 0.50;
    private static final double RATHER_NO = 0.25;
    private static final double NO = 0.00;

    private ArrayList<String> questionsBuf;
    private ArrayList<String> hypothesesBuf;

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

    private void initData() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("База знаний экспертной системы", "*.mkb"));
        File file = fc.showOpenDialog(null);
        if (file != null) {
            startButton.setDisable(false);
            FileReader fileReader = new FileReader(file);
            aboutAuthor.setText(fileReader.getAuthor());
            questionsBuf.addAll(fileReader.getQuestions());
            hypothesesBuf.addAll(fileReader.getHypotheses());

            questionsBuf.forEach(question -> questions.getItems().add(question));
            hypothesesBuf.forEach(hypothese -> {
                String[] buf = hypothese.split(",");
                hypotheses.getItems().add(buf[0] + " [" + buf[1] + "]");
            });
        }
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
        questionsBuf = new ArrayList<>();
        hypothesesBuf = new ArrayList<>();

        openFileButton.setOnAction(actionEvent -> {
            initData();
        });
    }

}
