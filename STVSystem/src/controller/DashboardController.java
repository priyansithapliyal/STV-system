package controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.SceneSwitcher;

public class DashboardController {

    @FXML
    private VBox addCandidateCard;

    @FXML
    private VBox addBallotCard;

    @FXML
    private VBox viewResultCard;

    @FXML
    public void initialize() {

        addCandidateCard.setOnMouseClicked(
                e -> openScene("candidate.fxml", e));

        addBallotCard.setOnMouseClicked(
                e -> openScene("ballot.fxml", e));

        viewResultCard.setOnMouseClicked(
                e -> openScene("result.fxml", e));
    }

    private void openScene(String fxml, MouseEvent event) {

        Stage stage = (Stage)
                ((VBox) event.getSource())
                        .getScene()
                        .getWindow();

        SceneSwitcher.switchScene(stage, fxml);
    }
}