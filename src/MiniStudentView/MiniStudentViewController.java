package MiniStudentView;

import Database.DatabaseManager;
import MainUI.Tools;
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
import viewStudents.ViewStudentModel;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MiniStudentViewController implements Initializable {

    public Region region;
    @FXML
    private JFXTextField searchNameText;

    @FXML
    private JFXButton searchName;

    @FXML
    private TableView<ViewStudentModel> tableStudent;

    @FXML
    private TableColumn<ViewStudentModel, String> iD;

    @FXML
    private TableColumn<ViewStudentModel, String> name;

    @FXML
    private TableColumn<ViewStudentModel, Integer> noOfBooks;

    @FXML
    private TableColumn<ViewStudentModel, String> blacklisted;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXButton minButton;


    private static int stdPrimaryID;
    private static String stdName;
    private ObservableList<ViewStudentModel> observableList = FXCollections.observableArrayList();
    private ObservableList<ViewStudentModel> observableListAlter = FXCollections.observableArrayList();
    private ResultSet resultSet;
    DatabaseManager manager = DatabaseManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reload();
        JFXDepthManager.setDepth(region,2);
        JFXDepthManager.setDepth(tableStudent,2);

        tableStudent.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                stdPrimaryID = Tools.pkGetter(tableStudent.getSelectionModel().getSelectedItem().getStdID());
                stdName = tableStudent.getSelectionModel().getSelectedItem().getStdName();
                Tools.close(tableStudent);
            }
        });
    }

    public void searchName(ActionEvent actionEvent) {
        observableListAlter.removeAll(observableListAlter);
        try {
            resultSet = manager.executeQuery("SELECT * FROM viewStudent WHERE Name LIKE '%" + searchNameText.getText() + "%'");
            while (resultSet.next()) {
                observableListAlter.add(new ViewStudentModel(resultSet.getString("ID"), resultSet.getString("Name"), resultSet.getString("Contact"), resultSet.getString("Email"), resultSet.getString("Address"), resultSet.getDate("Registration Date"), resultSet.getInt("No. Of Books Issued"), resultSet.getString("Blacklisted")));
            }
            if (!resultSet.isBeforeFirst()) {
                Label label = new Label("No Results Found \nPress \u27F2 to refresh the table");
                label.setFont(new Font(16));
                tableStudent.setPlaceholder(label);
            }
        } catch (SQLException e) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
        }
        iD.setCellValueFactory(new PropertyValueFactory<>("stdID"));
        name.setCellValueFactory(new PropertyValueFactory<>("stdName"));
        noOfBooks.setCellValueFactory(new PropertyValueFactory<>("stdNoOfBooks"));
        blacklisted.setCellValueFactory(new PropertyValueFactory<>("stdBlacklisted"));
        tableStudent.setItems(observableListAlter);
    }

    public void close(ActionEvent actionEvent) {
        Tools.close(searchName);
    }

    public void minimize(ActionEvent actionEvent) {
        Tools.minimize(searchName);
    }

    private void reload() {
        observableList.removeAll(observableList);
        try {
            resultSet = manager.executeQuery("SELECT * FROM viewStudent ORDER BY ID");
            while (resultSet.next()) {
                observableList.add(new ViewStudentModel(resultSet.getString("ID"), resultSet.getString("Name"), resultSet.getString("Contact"), resultSet.getString("Email"), resultSet.getString("Address"), resultSet.getDate("Registration Date"), resultSet.getInt("No. Of Books Issued"), resultSet.getString("Blacklisted")));

            }
        } catch (SQLException ex) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
        }

        iD.setCellValueFactory(new PropertyValueFactory<>("stdID"));
        name.setCellValueFactory(new PropertyValueFactory<>("stdName"));
        noOfBooks.setCellValueFactory(new PropertyValueFactory<>("stdNoOfBooks"));
        blacklisted.setCellValueFactory(new PropertyValueFactory<>("stdBlacklisted"));
        tableStudent.setItems(observableList);
    }

    public static int getStdPrimaryID() {
        return stdPrimaryID;
    }

    public static String getStdName() {
        return stdName;
    }
}
