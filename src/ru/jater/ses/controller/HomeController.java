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
import ru.jater.ses.entity.Hypothese;
import ru.jater.ses.util.FileReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeController {

    private static final double YES = 1.00;
    private static final double RATHER_YES = 0.75;
    private static final double NOT_SURE = 0.50;
    private static final double RATHER_NO = 0.25;
    private static final double NO = 0.00;

    private ArrayList<String> questions;
    private List<Hypothese> hypotheses;
    private int questionId;

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
        questionId = 1;
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
            hypotheses.forEach(hypothese -> hypothesesList.getItems().add(hypothese.toString()));
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

        openFileButton.setOnAction(actionEvent -> initData());

        yesButton.setOnAction(actionEvent -> action(YES));
        ratherYesButton.setOnAction(actionEvent -> action(RATHER_YES));
        neutralButton.setOnAction(actionEvent -> action(NOT_SURE));
        ratherNoButton.setOnAction(actionEvent -> action(RATHER_NO));
        noButton.setOnAction(actionEvent -> action(NO));
    }

    private void action(double probability) {
        for (Hypothese hypothese : hypotheses) {
            double pPositive = hypothese.getpPositive(questionId);
            double pNegative = hypothese.getpNegative(questionId);
            double pAp = hypotheses.get(questionId).getpAposterior();
            if (probability > NOT_SURE) {
                double posteriorProbability = (pPositive * pAp) / (pPositive * pAp + pNegative * (1 - pAp));
                double pApCorrection = pAp + ((probability - NOT_SURE) * (posteriorProbability - pAp)) / (NOT_SURE);
                hypothese.setpAposterior(pApCorrection);
            } else if (probability < NOT_SURE) {
                double posteriorProb = ((1 - pPositive) * pAp) / ((1 - pPositive) * pAp + (1 - pNegative) * (1 - pAp));
                double pApCorrection = posteriorProb + (probability * (pAp - posteriorProb)) / (NOT_SURE);
                hypothese.setpAposterior(pApCorrection);
            }
        }

        hypotheses.sort(Collections.reverseOrder());
        hypothesesList.getItems().clear();
        hypotheses.forEach(hypo -> hypothesesList.getItems().add(hypo.toString()));
        questionId++;

        if (questionId <= questions.size()) {
            question.setText(questionsList.getItems().get(questionId - 1));
        } else {
            question.setText("Вероятно это " + hypothesesList.getItems().get(0));
            disableButtons();
        }
    }

}
