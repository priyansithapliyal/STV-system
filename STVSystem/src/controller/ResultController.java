package controller;
 
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
 
import model.Candidate;
import service.ElectionService;
import session.UserSession;
import util.SceneSwitcher;
 
import java.util.List;
 
public class ResultController {
 
    @FXML
    private TableView<Candidate> resultTable;
 
    @FXML
    private TableColumn<Candidate, String> nameColumn;
 
    @FXML
    private TableColumn<Candidate, String> votesColumn;
 
    @FXML
    private Label totalCandidatesLabel;
 
    @FXML
    private Label totalVotesLabel;
 
    @FXML
    private Label winnerLabel;
 
    @FXML
    private BarChart<String, Number> resultChart;
 
    @FXML
    public void initialize() {
 
        // LOAD fresh data from DB every time results screen opens
        ElectionService.loadCandidatesFromDatabase();
        ElectionService.loadBallotsFromDatabase();
 
        // STV Winner Calculation
        List<Candidate> winners =
                ElectionService.calculateSTWWinners();
 
        // SUMMARY
        totalCandidatesLabel.setText(
                "Total Candidates: "
                        + ElectionService.getCandidates().size()
        );
 
        totalVotesLabel.setText(
                "Total Votes: "
                        + ElectionService.getBallots().size()
        );
 
        // WINNER DISPLAY
        if (winners.isEmpty()) {
 
            winnerLabel.setText("No winners yet");
 
        } else {
 
            StringBuilder names = new StringBuilder();
 
            for (Candidate c : winners) {
                names.append(c.getName()).append(", ");
            }
 
            winnerLabel.setText(
                    "Winner(s): "
                            + names.substring(0, names.length() - 2)
            );
        }
 
        // TABLE COLUMNS
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
 
        votesColumn.setCellValueFactory(param ->
                new SimpleStringProperty(
                        String.valueOf(
                                param.getValue().getVotes()
                        )
                )
        );
 
        // TABLE DATA
        resultTable.setItems(
                ElectionService.getCandidates()
        );
 
        // HIGHLIGHT WINNERS
        resultTable.setRowFactory(tv -> new TableRow<>() {
 
            @Override
            protected void updateItem(
                    Candidate item,
                    boolean empty
            ) {
 
                super.updateItem(item, empty);
 
                if (item == null || empty) {
 
                    setStyle("");
 
                } else if (winners.contains(item)) {
 
                    setStyle(
                            "-fx-background-color: lightgreen;"
                    );
 
                } else {
 
                    setStyle("");
                }
            }
        });
 
        // BAR CHART
        XYChart.Series<String, Number> series =
                new XYChart.Series<>();
 
        for (Candidate c :
                ElectionService.getCandidates()) {
 
            series.getData().add(
 
                    new XYChart.Data<>(
 
                            c.getName(),
                            c.getVotes()
                    )
            );
        }
 
        resultChart.getData().add(series);
    }
 
    @FXML
    private void resetElection() {
 
        // CONFIRM before wiping everything
        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "This will permanently delete all candidates, ballots, and reset all voter flags. Are you sure?",
                ButtonType.YES,
                ButtonType.NO
        );
 
        confirm.setTitle("Confirm Election Reset");
        confirm.showAndWait();
 
        if (confirm.getResult() != ButtonType.YES) {
            return;
        }
 
        // ✅ RESET — clears DB tables + in-memory state
        boolean success = ElectionService.resetElection();
 
        if (!success) {
 
            new Alert(
                    Alert.AlertType.ERROR,
                    "Failed to reset election. Check database connection."
            ).show();
 
            return;
        }
 
        // CLEAR UI
        resultTable.getItems().clear();
 
        resultChart.getData().clear();
 
        winnerLabel.setText(
                "Winner will appear here"
        );
 
        totalCandidatesLabel.setText(
                "Total Candidates: 0"
        );
 
        totalVotesLabel.setText(
                "Total Votes: 0"
        );
 
        new Alert(
                Alert.AlertType.INFORMATION,
                "Election Reset Successfully! All data has been cleared."
        ).show();
 
        // REDIRECT admin back to dashboard after reset
        Stage stage =
                (Stage) resultTable
                        .getScene()
                        .getWindow();
 
        SceneSwitcher.switchScene(
                stage,
                "dashboard.fxml"
        );
    }
 
    @FXML
    private void goBack() {
 
        Stage stage =
                (Stage) resultTable
                        .getScene()
                        .getWindow();
 
        // ✅ FIX: Check role — admin goes to admin dashboard, user goes to user dashboard
        if (UserSession.getRole().equals("admin")) {
 
            SceneSwitcher.switchScene(
                    stage,
                    "dashboard.fxml"
            );
 
        } else {
 
            SceneSwitcher.switchScene(
                    stage,
                    "userDashboard.fxml"
            );
        }
    }
}