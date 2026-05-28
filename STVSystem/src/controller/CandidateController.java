package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import model.Candidate;

import service.ElectionService;

import javafx.stage.Stage;

import util.SceneSwitcher;

public class CandidateController {

    @FXML private TextField candidateNameField;

    @FXML private Button addButton;

    @FXML private ListView<String> candidateListView;

    @FXML
    public void initialize() {

        // LOAD DATABASE DATA
        ElectionService.loadCandidatesFromDatabase();

        refreshList();

        addButton.setOnAction(e -> addCandidate());
    }

    private void refreshList() {

        candidateListView.getItems().clear();

        for (Candidate c : ElectionService.getCandidates()) {

            candidateListView
                    .getItems()
                    .add(c.getName());
        }
    }

    private void addCandidate() {

        String name =
                candidateNameField
                .getText()
                .trim();

        if (name.isEmpty()) {

            new Alert(
                    Alert.AlertType.WARNING,
                    "Candidate name cannot be empty!"
            ).show();

            return;
        }

        ElectionService.addCandidate(name);

        refreshList();

        candidateNameField.clear();
    }

    @FXML
    private void goBack() {

        Stage stage =
                (Stage) candidateNameField
                .getScene()
                .getWindow();

        SceneSwitcher.switchScene(
                stage,
                "dashboard.fxml"
        );
    }
}