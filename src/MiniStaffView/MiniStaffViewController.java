package MiniStaffView;

import Database.DatabaseManager;
import MainUI.Tools;
import ViewStaff.ViewStaffModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MiniStaffViewController implements Initializable {
    public Region region;
    @FXML
    private JFXTextField searchNameText;

    @FXML
    private JFXButton searchName;

    @FXML
    private TableView<ViewStaffModel> tableStaff;

    @FXML
    private TableColumn<ViewStaffModel, String> iD;

    @FXML
    private TableColumn<ViewStaffModel, String> name;

    @FXML
    private TableColumn<ViewStaffModel, String> designation;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXButton minButton;
    private static int staffPrimaryID;
    private static String staffName;
    private ObservableList<ViewStaffModel> observableList = FXCollections.observableArrayList();
    private ObservableList<ViewStaffModel> observableListAlter = FXCollections.observableArrayList();
    private ResultSet resultSet;
    DatabaseManager manager = DatabaseManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reload();

        JFXDepthManager.setDepth(region,2);
        JFXDepthManager.setDepth(tableStaff,2);
        tableStaff.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                staffPrimaryID = Tools.pkGetter(tableStaff.getSelectionModel().getSelectedItem().getStaffID());
                staffName = tableStaff.getSelectionModel().getSelectedItem().getStaffName();
                Tools.close(tableStaff);
            }
        });
    }

    private void reload() {
        observableList.removeAll(observableList);
        try {
            resultSet = manager.executeQuery("SELECT * FROM viewStaff ORDER BY ID");
            while (resultSet.next()) {
                observableList.add(new ViewStaffModel(resultSet.getString("ID"), resultSet.getString("Name"), resultSet.getString("Contact"), resultSet.getString("Designation")));
            }
        } catch (SQLException ex) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
        }

        iD.setCellValueFactory(new PropertyValueFactory<>("staffID"));
        name.setCellValueFactory(new PropertyValueFactory<>("staffName"));
        designation.setCellValueFactory(new PropertyValueFactory<>("staffDesignation"));
        tableStaff.setItems(observableList);
    }

    public void searchName(ActionEvent actionEvent) {
        observableListAlter.removeAll(observableListAlter);
        try {
            resultSet = manager.executeQuery("SELECT * FROM viewStaff  WHERE Name LIKE '%"+searchNameText.getText()+"%' ORDER BY ID");
            while (resultSet.next()) {
                observableListAlter.add(new ViewStaffModel(resultSet.getString("ID"), resultSet.getString("Name"), resultSet.getString("Contact"), resultSet.getString("Designation")));
            }
            if (!resultSet.isBeforeFirst()) {
                Label label = new Label("No Results Found \nPress \u27F2 to refresh the table");
                label.setFont(new Font(16));
                tableStaff.setPlaceholder(label);
            }
        } catch (SQLException ex) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
        }

        iD.setCellValueFactory(new PropertyValueFactory<>("staffID"));
        name.setCellValueFactory(new PropertyValueFactory<>("staffName"));
        designation.setCellValueFactory(new PropertyValueFactory<>("staffDesignation"));
        tableStaff.setItems(observableListAlter);

    }

    public void close(ActionEvent actionEvent) {
        Tools.close(searchName);
    }

    public void minimize(ActionEvent actionEvent) {
        Tools.minimize(searchName);
    }

    public static int getStaffPrimaryID() {
        return staffPrimaryID;
    }

    public static String getStaffName() {
        return staffName;
    }
}
