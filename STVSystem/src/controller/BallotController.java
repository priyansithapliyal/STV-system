package controller;
import database.DatabaseConnection;
import session.UserSession;
 
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
 
import javafx.fxml.FXML;
 
import javafx.scene.control.*;
 
import model.Ballot;
import model.Candidate;
 
import service.ElectionService;
 
import java.util.ArrayList;
import java.util.List;
 
import javafx.stage.Stage;
 
import util.SceneSwitcher;
 
public class BallotController {
 
    @FXML private ComboBox<String> firstPref;
 
    @FXML private ComboBox<String> secondPref;
 
    @FXML private ComboBox<String> thirdPref;
 
    @FXML private Button submitButton;
 
    @FXML private ListView<String> ballotListView;
 
    private final ObservableList<String> ballots =
            FXCollections.observableArrayList();
 
    @FXML
    public void initialize() {
 
        // LOAD DATABASE CANDIDATES
        ElectionService.loadCandidatesFromDatabase();
 
        refreshCandidates();
 
        ballotListView.setItems(ballots);
 
        submitButton.setOnAction(e -> addBallot());
    }
 
    private void refreshCandidates() {
 
        ObservableList<String> names =
                FXCollections.observableArrayList();
 
        for (Candidate c :
                ElectionService.getCandidates()) {
 
            names.add(c.getName());
        }
 
        firstPref.setItems(names);
 
        secondPref.setItems(names);
 
        thirdPref.setItems(names);
    }
 
    private void addBallot() {
 
        String first =
                firstPref.getValue();
 
        String second =
                secondPref.getValue();
 
        String third =
                thirdPref.getValue();
 
        // VALIDATION
        if (first == null
                && second == null
                && third == null) {
 
            new Alert(
                    Alert.AlertType.WARNING,
                    "Select at least one preference!"
            ).show();
 
            return;
        }
 
        // DUPLICATE CHECK
        if ((first != null && first.equals(second))
                || (first != null && first.equals(third))
                || (second != null && second.equals(third))) {
 
            new Alert(
                    Alert.AlertType.ERROR,
                    "Duplicate candidates not allowed!"
            ).show();
 
            return;
        }
 
        // CREATE BALLOT
        List<String> prefs =
                new ArrayList<>();
 
        if (first != null) prefs.add(first);
 
        if (second != null) prefs.add(second);
 
        if (third != null) prefs.add(third);
 
        Ballot ballot =
                new Ballot(prefs);
 
        // SAVE BALLOT
        ElectionService.addBallot(ballot);
        try {
 
            Connection conn =
                    DatabaseConnection.getConnection();
 
            String query =
                    "UPDATE users SET has_voted = true WHERE username = ?";
 
            PreparedStatement ps =
                    conn.prepareStatement(query);
 
            ps.setString(
                    1,
                    UserSession.getUsername()
            );
 
            ps.executeUpdate();
 
            System.out.println("User vote status updated!");
 
        } catch (Exception e) {
 
            e.printStackTrace();
        }
 
        ballots.add(prefs.toString());
 
        // RESET
        firstPref.setValue(null);
 
        secondPref.setValue(null);
 
        thirdPref.setValue(null);
 
        new Alert(
                Alert.AlertType.INFORMATION,
                "Vote Submitted Successfully! Results will be available once all users have voted."
        ).show();
 
        // DISABLE MULTIPLE VOTING
        submitButton.setDisable(true);
 
        // ✅ FIX: Redirect back to User Dashboard, NOT results
        // Results should only be shown when admin decides or all users have voted
        Stage stage =
                (Stage) firstPref
                .getScene()
                .getWindow();
 
        SceneSwitcher.switchScene(
                stage,
                "userDashboard.fxml"
        );
    }
 
    @FXML
    private void goBack() {
 
        Stage stage =
                (Stage) firstPref
                .getScene()
                .getWindow();
 
        // ADMIN
        if (UserSession.getRole().equals("admin")) {
 
            SceneSwitcher.switchScene(
                    stage,
                    "dashboard.fxml"
            );
        }
 
        // USER
        else {
 
            SceneSwitcher.switchScene(
                    stage,
                    "userDashboard.fxml"
            );
        }
    }
}
 