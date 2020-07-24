package Database;

import MainUI.Tools;

import java.sql.*;

public class DatabaseManager {
    private static DatabaseManager databaseManager;
    private static final String connectURL = "jdbc:sqlserver://localhost:1433;database=LibraryDB;IntegratedSecurity=true";
    private static Statement statement = null;
    private static Connection connection = null;

    private DatabaseManager() {
        makeConnection();
    }

    private void makeConnection() {

        try {
            connection = DriverManager.getConnection(connectURL);
        } catch (SQLException e) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Connection Error\nMake sure that SQL Services are running");
        }
    }

    public static DatabaseManager getInstance() {
        if (databaseManager == null) {
            return new DatabaseManager();
        } else {
            return databaseManager;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean execute(String query) {
        try {
            statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println("Exception on DatabaseManager.executeQuery :"+e.getLocalizedMessage());
            return false;
        }
        return true;

    }

    public ResultSet executeQuery(String query) {
        ResultSet resultSet;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Exception on DatabaseManager.executeQuery :"+e.getLocalizedMessage());
            return null;
        }
        return resultSet;
    }

    public int getGenrePKByName(String GenreName) {
        ResultSet resultSet;
        int GenrePK = 0;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT Genre_ID FROM tbl_Genre WHERE Genre_Name='"+GenreName+"'");
            if (resultSet.next()) {
                GenrePK = resultSet.getInt("Genre_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return GenrePK;

    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
