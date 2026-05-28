package controller;

import database.DatabaseConnection;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import util.SceneSwitcher;

import session.UserSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML private TextField usernameField;

    @FXML private PasswordField passwordField;

    @FXML private TextField visiblePasswordField;

    @FXML private CheckBox showPasswordCheckBox;

    @FXML private Button loginButton;

    @FXML
    public void initialize() {

        // SHOW / HIDE PASSWORD
        visiblePasswordField.managedProperty()
                .bind(showPasswordCheckBox.selectedProperty());

        visiblePasswordField.visibleProperty()
                .bind(showPasswordCheckBox.selectedProperty());

        passwordField.managedProperty()
                .bind(showPasswordCheckBox.selectedProperty().not());

        passwordField.visibleProperty()
                .bind(showPasswordCheckBox.selectedProperty().not());

        visiblePasswordField.textProperty()
                .bindBidirectional(passwordField.textProperty());

        // LOGIN BUTTON
        loginButton.setOnAction(e -> login());
    }

    @FXML
    private void login() {

        String username =
                usernameField.getText().trim();

        String password =
                passwordField.getText().trim();

        // EMPTY FIELD CHECK
        if (username.isEmpty() || password.isEmpty()) {

            new Alert(
                    Alert.AlertType.WARNING,
                    "Fields cannot be empty!"
            ).show();

            return;
        }

        try {

            Connection conn =
                    DatabaseConnection.getConnection();

            String query =
                    "SELECT * FROM users WHERE username=? AND password=?";

            PreparedStatement ps =
                    conn.prepareStatement(query);

            ps.setString(1, username);

            ps.setString(2, password);

            ResultSet rs =
                    ps.executeQuery();

            // USER FOUND
            if (rs.next()) {

                String role =
                        rs.getString("role");

                boolean hasVoted =
                        rs.getBoolean("has_voted");

                // SAVE SESSION
                UserSession.setUser(
                        username,
                        role
                );

                Stage stage =
                        (Stage) usernameField
                        .getScene()
                        .getWindow();

                // ================= ADMIN =================
                if (role.equals("admin")) {

                    SceneSwitcher.switchScene(
                            stage,
                            "dashboard.fxml"
                    );
                }

                // ================= VOTER =================
                else {

                    // ALREADY VOTED
                    if (hasVoted) {

                        new Alert(
                                Alert.AlertType.INFORMATION,
                                "You have already voted!"
                        ).show();

                        // SEND TO RESULTS
                        SceneSwitcher.switchScene(
                                stage,
                                "result.fxml"
                        );

                        return;
                    }

                    // USER DASHBOARD
                    SceneSwitcher.switchScene(
                            stage,
                            "userDashboard.fxml"
                    );
                }

            }

            // INVALID LOGIN
            else {

                new Alert(
                        Alert.AlertType.ERROR,
                        "Invalid Credentials!"
                ).show();
            }

        } catch (Exception e) {

            e.printStackTrace();

            new Alert(
                    Alert.AlertType.ERROR,
                    "Database Error!"
            ).show();
        }
    }
}