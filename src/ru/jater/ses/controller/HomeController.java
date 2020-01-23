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
import java.util.Collections;

public class HomeController {

    private static final double YES = 1.00;
    private static final double RATHER_YES = 0.75;
    private static final double NOT_SURE = 0.50;
    private static final double RATHER_NO = 0.25;
    private static final double NO = 0.00;

    private ArrayList<String> questions;
    private ArrayList<String> hypotheses;
    private ArrayList<String> hypothesesTitles;
    private int index;

    @FXML private MenuItem openFileButton;
    @FXML private ListView<String> hypothesesList;
    @FXML private ListView<String> questionsList;
    @FXML private Label question;
    @FXML private Button noButton;
    @FXML private Button yesButton;
    @FXML private Button neutralButton;
    @FXML private Button ratherYesButton;
    @FXML private Button ratherNoButton;
    @FXML private Label aboutAuthor;
    @FXML private Button startButton;

    private void enableComponents() {
        hypothesesList.setDisable(false);
        questionsList.setDisable(false);
        yesButton.setDisable(false);
        noButton.setDisable(false);
        neutralButton.setDisable(false);
        ratherYesButton.setDisable(false);
        ratherNoButton.setDisable(false);
    }

    private void disableButtons() {
        yesButton.setDisable(true);
        noButton.setDisable(true);
        neutralButton.setDisable(true);
        ratherYesButton.setDisable(true);
        ratherNoButton.setDisable(true);
    }

    private void disableLists() {
        hypothesesList.setDisable(true);
        questionsList.setDisable(true);
    }

    private void clear() {
        aboutAuthor.setText("");
        questions.clear();
        hypotheses.clear();
        question.setText("");
        questionsList.getItems().clear();
        hypothesesList.getItems().clear();
        index = 1;
    }

    private void initData() {
        disableButtons();
        disableLists();
        clear();

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("База знаний экспертной системы", "*.mkb"));
        File file = fc.showOpenDialog(null);
        if (file != null) {
            startButton.setDisable(false);
            FileReader fileReader = new FileReader(file);
            aboutAuthor.setText(fileReader.getAuthor());
            questions.addAll(fileReader.getQuestions());
            hypotheses.addAll(fileReader.getHypotheses());

            questions.forEach(question -> questionsList.getItems().add(question));
            hypotheses.forEach(hypothese -> {
                String[] buf = hypothese.split(",");
                hypothesesList.getItems().add("[" + buf[1] + "] " + buf[0]);
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
        question.setText(questionsList.getItems().get(0));
    }

    @FXML
    void initialize() {
        questions = new ArrayList<>();
        hypotheses = new ArrayList<>();
        hypothesesTitles = new ArrayList<>();

        openFileButton.setOnAction(actionEvent -> initData());

        yesButton.setOnAction(actionEvent -> action(YES));
        ratherYesButton.setOnAction(actionEvent -> action(RATHER_YES));
        neutralButton.setOnAction(actionEvent -> action(NOT_SURE));
        ratherNoButton.setOnAction(actionEvent -> action(RATHER_NO));
        noButton.setOnAction(actionEvent -> action(NO));
    }

    private void action(double probability) {
        for (int i = 0; i < hypotheses.size(); i++) {
            String str = hypotheses.get(i);
            String[] lineName = str.split(",");

            for (int k = 1; k < lineName.length; k++) {
                if (index == Double.parseDouble(lineName[k])) {
                    double cons = 1000.0000;
                    if (probability > NOT_SURE) {
                        double pPositive = Double.parseDouble(lineName[k + 1]);
                        double pNegative = Double.parseDouble(lineName[k + 2]);
                        double pAp = Double.parseDouble(lineName[1]);
                        double posteriorProbability = (pPositive * pAp) / (pPositive * pAp + pNegative * (1 - pAp));
                        double pApCorrection = Math.round((pAp + ((probability - NOT_SURE) * (posteriorProbability - pAp)) / (NOT_SURE)) * cons) / cons;
                        lineName[1] = Double.toString(pApCorrection);
                    } else if (probability < NOT_SURE) {
                        double pPositive = Double.parseDouble(lineName[k + 1]);
                        double pNegative = Double.parseDouble(lineName[k + 2]);
                        double pAp = Double.parseDouble(lineName[1]);
                        double posteriorProb = ((1 - pPositive) * pAp) / ((1 - pPositive) * pAp + (1 - pNegative) * (1 - pAp));
                        double pApCorrection = Math.round((posteriorProb + (probability * (pAp - posteriorProb)) / (NOT_SURE)) * cons) / cons;
                        lineName[1] = Double.toString(pApCorrection);
                    }
                }
            }

            hypothesesTitles.add("[" + lineName[1] + "] " + lineName[0]);
            str = String.join(",", lineName);
            hypotheses.set(i, str);
        }

        hypothesesTitles.sort(Collections.reverseOrder());
        hypothesesList.getItems().clear();
        hypothesesTitles.forEach(hypothese -> hypothesesList.getItems().add(hypothese));
        hypothesesTitles.clear();
        index++;

        if (index <= questions.size()) {
            question.setText(questionsList.getItems().get(index - 1));
        } else {
            question.setText("Вероятно это " + hypothesesList.getItems().get(0).split("]")[1].toLowerCase());
            disableButtons();
        }
    }

}
