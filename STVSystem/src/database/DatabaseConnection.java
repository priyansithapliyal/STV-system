package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/stv_system";

    private static final String USER = "root";

    private static final String PASSWORD = "Ayush@12";

    public static Connection getConnection() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection =
                    DriverManager.getConnection(
                            URL,
                            USER,
                            PASSWORD
                    );

            System.out.println("Database Connected Successfully!");

            return connection;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
}