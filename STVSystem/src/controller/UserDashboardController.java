package controller;

import javafx.fxml.FXML;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import util.SceneSwitcher;

public class UserDashboardController {

    @FXML private VBox voteCard;

    @FXML private VBox resultCard;

    @FXML
    public void initialize() {

        voteCard.setOnMouseClicked(
                e -> openScene("ballot.fxml", e)
        );

        resultCard.setOnMouseClicked(
                e -> openScene("result.fxml", e)
        );
    }

    private void openScene(
            String fxml,
            MouseEvent event
    ) {

        Stage stage =
                (Stage) ((VBox) event.getSource())
                .getScene()
                .getWindow();

        SceneSwitcher.switchScene(
                stage,
                fxml
        );
    }
}