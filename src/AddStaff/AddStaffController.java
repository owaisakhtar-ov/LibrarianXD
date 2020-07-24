package AddStaff;

import Database.DatabaseManager;
import MainUI.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AddStaffController implements Initializable {
    public ImageView frame;
    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField contact;

    @FXML
    private JFXTextField designation;

    @FXML
    private JFXButton insert;

    @FXML
    private JFXButton cancel;

    @FXML
    private JFXButton minButton;

    DatabaseManager manager = DatabaseManager.getInstance();

    public void insert(ActionEvent actionEvent) {
        if (!name.getText().isEmpty() && !contact.getText().isEmpty() && !designation.getText().isEmpty()) {
            //Insertion of DATA in DATABASE
            try {
                PreparedStatement insertQuery = manager.getConnection().prepareStatement("INSERT INTO tbl_Staff VALUES (?,?,?)");
                insertQuery.setString(1,name.getText());
                insertQuery.setString(2,contact.getText());
                insertQuery.setString(3,designation.getText());
                insertQuery.executeUpdate();

                Tools.notification("Graphics/edit_add.png", "SUCCESS!!!", "Staff Added Successfully");

            } catch (NumberFormatException e) {
                Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Wrong NUMERIC values");

            } catch (SQLException e) {
                Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
            }

        } else {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Please provide all the Information");
        }
    }

    public void cancel(ActionEvent actionEvent) {
        Tools.close(name);
    }

    public void minimize(ActionEvent actionEvent) {
        Tools.minimize(name);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
