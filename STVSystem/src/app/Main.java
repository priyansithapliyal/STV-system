package app;

import database.DatabaseConnection;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // DATABASE CONNECTION TEST
        DatabaseConnection.getConnection();

        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));

        Scene scene = new Scene(root, 500, 400);

        scene.getStylesheets().add(
                getClass()
                .getResource("/style/style.css")
                .toExternalForm()
        );

        primaryStage.setTitle("STV Election System");

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}