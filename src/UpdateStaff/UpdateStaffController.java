package UpdateStaff;

import Database.DatabaseManager;
import MainUI.Tools;
import ViewStaff.ViewStaffModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateStaffController {
    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField contact;

    @FXML
    private JFXTextField designation;

    @FXML
    private JFXButton update;

    @FXML
    private JFXButton cancel;

    @FXML
    private JFXButton minButton;

    private int staffID;
    DatabaseManager manager = DatabaseManager.getInstance();

    public void update(ActionEvent actionEvent) {
        if (!name.getText().isEmpty() && !contact.getText().isEmpty() && !designation.getText().isEmpty()) {
            //Insertion of DATA in DATABASE
            try {
                PreparedStatement insertQuery = manager.getConnection().prepareStatement("UPDATE tbl_Staff SET Staff_Name =?,Staff_Contact=?,Staff_Designation=? WHERE Staff_ID ="+staffID);
                insertQuery.setString(1,name.getText());
                insertQuery.setString(2,contact.getText());
                insertQuery.setString(3,designation.getText());
                insertQuery.executeUpdate();

                Tools.notification("/Graphics/Actions-view-refresh-icon.png", "SUCCESS!!!", "Staff Updated Successfully");

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

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public void initUpdateScene(ViewStaffModel viewStaffModel) {
        name.setText(viewStaffModel.getStaffName());
        contact.setText(viewStaffModel.getStaffContact());
        designation.setText(viewStaffModel.getStaffDesignation());

    }
}
