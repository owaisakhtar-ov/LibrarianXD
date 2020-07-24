package UpdateStudent;

import Database.DatabaseManager;
import MainUI.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import viewStudents.ViewStudentModel;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class UpdateStudentController implements Initializable {
    @FXML
    private ImageView frame;

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
    private JFXButton updateStudent;

    @FXML
    private JFXButton close;

    @FXML
    private JFXButton minButton;

    private int studentID;
    private int isBlacklist;
    DatabaseManager manager = DatabaseManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void UpdateStudent(ActionEvent actionEvent) {
        isBlacklist = isBlackedList.isSelected() ? 1 : 0;
        if (!name.getText().isEmpty() && !email.getText().isEmpty() && !address.getText().isEmpty() && !contact.getText().isEmpty() && !noOfBooksIssue.getText().isEmpty() && !registrationDate.getValue().toString().isEmpty()) {
            try {
                PreparedStatement preparedStatement = manager.getConnection().prepareStatement("UPDATE tbl_Student SET Student_Name =?,Student_Email = ?,Student_Address=?,Student_Contact=?,Student_RegisterDate =?,Student_isBlackList=?,Student_No_Of_Books_issued =? WHERE Student_ID =" + studentID);
                preparedStatement.setString(1, name.getText());
                preparedStatement.setString(2, email.getText());
                preparedStatement.setString(3, address.getText());
                preparedStatement.setString(4, contact.getText());
                preparedStatement.setString(5, registrationDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                preparedStatement.setInt(6, isBlacklist);
                preparedStatement.setInt(7, Integer.parseInt(noOfBooksIssue.getText()));
                preparedStatement.executeUpdate();

                Platform.runLater(() -> Tools.notification("/Graphics/Actions-view-refresh-icon.png", "SUCCESS!!!", "Student Updated Successfully"));
                Tools.close(name);


            } catch (NumberFormatException ex) {
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

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public void initUpdateScene(ViewStudentModel viewStudentModel) {
        name.setText(viewStudentModel.getStdName());
        email.setText(viewStudentModel.getStdEmail());
        address.setText(viewStudentModel.getStdAddress());
        contact.setText(viewStudentModel.getStdContact());
        noOfBooksIssue.setText(viewStudentModel.getStdNoOfBooks()+"");

        boolean state = viewStudentModel.getStdBlacklisted().equals("Yes");
        isBlackedList.setSelected(state);

//        isBlackedList.setText(viewStudentModel.getStdBlacklisted());
        Date date = viewStudentModel.getStdRegDate();
        registrationDate.setValue(Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate());

    }
}
