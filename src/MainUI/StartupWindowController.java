package MainUI;

import Database.DatabaseManager;
import com.jfoenix.effects.JFXDepthManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StartupWindowController implements Initializable {

    private int totalBooks = 0;

    @FXML
    private Label noOfStudents;
    @FXML
    private Label noOfBooks;
    @FXML
    private Label noOfStaff;
    @FXML
    private Label totalNoOfBooks;
    @FXML
    private Label noOfBookIssued;
    @FXML
    private ImageView image;

    private ResultSet resultSet;
    private DatabaseManager manager = DatabaseManager.getInstance();
    private ArrayList<String> returnValues = new ArrayList<>();
    private ArrayList<String> issueEntries = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        image.setImage(new Image("/Graphics/Untitled-3.png"));
        image.setDisable(true);

        JFXDepthManager depthManager = new JFXDepthManager();
        depthManager.setDepth(image,4);

        try {
            resultSet = manager.executeQuery("SELECT COUNT (*) FROM viewStudent");
            while (resultSet.next()){
                noOfStudents.setText(noOfStudents.getText()+resultSet.getInt(1));
            }

            resultSet = manager.executeQuery("SELECT COUNT (*) FROM viewBooks");
            while (resultSet.next()) {
                noOfBooks.setText(noOfBooks.getText()+resultSet.getInt(1));
            }

            resultSet = manager.executeQuery("SELECT COUNT (*) FROM viewStaff");
            while (resultSet.next()) {
                noOfStaff.setText(noOfStaff.getText() + resultSet.getInt(1));
            }

            resultSet = manager.executeQuery("SELECT * FROM viewBooks");
            while (resultSet.next()) {
                totalBooks += resultSet.getInt("Quantity");
            }
            totalNoOfBooks.setText(totalNoOfBooks.getText() + totalBooks);


            //**************************************************
            resultSet = manager.executeQuery("SELECT * FROM viewReturn ORDER BY ID");
            while (resultSet.next()) {
                returnValues.add(resultSet.getString("Issue ID"));
            }

            resultSet = manager.executeQuery("SELECT * FROM viewIssuance ORDER BY ID");
            while (resultSet.next()) {
                issueEntries.add(resultSet.getString("ID"));
            }

            issueEntries.removeIf(s -> returnValues.contains(s));
            noOfBookIssued.setText(noOfBookIssued.getText() + issueEntries.size());


//            resultSet = manager.executeQuery("SELECT COUNT (*) FROM viewIssuance");
//            while (resultSet.next()) {
//                noOfBookIssued.setText(noOfBookIssued.getText() + resultSet.getInt(1));
//            }
        }
        catch (SQLException e) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
        }


    }
}
