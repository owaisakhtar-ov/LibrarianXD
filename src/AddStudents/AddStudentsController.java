package AddStudents;

import Database.DatabaseManager;
import MainUI.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddStudentsController implements Initializable {
    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField address;

    @FXML
    private JFXTextField contact;

    @FXML
    private JFXDatePicker registrationDate;

    @FXML
    private JFXTextField noOfBooksIssue;

    @FXML
    private JFXToggleButton isBlackedList;

    @FXML
    private JFXButton InsertStudent;

    @FXML
    private JFXButton close;

    @FXML
    private JFXButton minButton;

    DatabaseManager manager = DatabaseManager.getInstance();
    private int isBlacklist;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registrationDate.setValue(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        isBlackedList.setSelected(false);
        noOfBooksIssue.setText("0");
    }

    public void insertNewStudent(ActionEvent actionEvent) {
        isBlacklist = isBlackedList.isSelected() ? 1 : 0;
        if (!name.getText().isEmpty() && !email.getText().isEmpty() && !address.getText().isEmpty() && !contact.getText().isEmpty() && !registrationDate.getValue().toString().isEmpty() && !noOfBooksIssue.getText().isEmpty()) {
            //Insertion of DATA in DATABASE
            try {
                PreparedStatement preparedStatement = manager.getConnection().prepareStatement("INSERT INTO tbl_Student VALUES (?,?,?,?,?,?,?)");
                preparedStatement.setString(1,name.getText());
                preparedStatement.setString(2,email.getText());
                preparedStatement.setString(3,address.getText());
                preparedStatement.setString(4,contact.getText());
                preparedStatement.setString(5,registrationDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                preparedStatement.setInt(6,isBlacklist);
                preparedStatement.setInt(7,Integer.parseInt(noOfBooksIssue.getText()));
                preparedStatement.executeUpdate();

                Tools.notification("Graphics/edit_add.png", "SUCCESS!!!", "Student Added Successfully");

            } catch (NumberFormatException e) {
                Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Wrong NUMERIC values");

            } catch (SQLException e) {
                Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
            }

        } else {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Please provide all the Information");
        }

    }

    public void close(ActionEvent actionEvent) {
        Tools.close(name);
    }

    public void minimize(ActionEvent actionEvent) {
        Tools.minimize(name);
    }

}
