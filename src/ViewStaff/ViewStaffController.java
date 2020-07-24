package ViewStaff;

import Database.DatabaseManager;
import MainUI.Tools;
import UpdateStaff.UpdateStaffController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewStaffController implements Initializable {

    public Region region;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private JFXComboBox<String> searchOptions;

    @FXML
    private JFXTextField searchText;

    @FXML
    private JFXButton search;

    @FXML
    private TableView<ViewStaffModel> staffTable;

    @FXML
    private TableColumn<ViewStaffModel, String> iD;

    @FXML
    private TableColumn<ViewStaffModel, String> name;

    @FXML
    private TableColumn<ViewStaffModel, String> contact;

    @FXML
    private TableColumn<ViewStaffModel, String> designation;

    @FXML
    private JFXButton delete;

    @FXML
    private JFXButton update;

    @FXML
    private JFXButton insert;

    @FXML
    private JFXButton reloader;


    private ObservableList<ViewStaffModel> observableList = FXCollections.observableArrayList();
    private ObservableList<ViewStaffModel> observableListAlter = FXCollections.observableArrayList();
    private ResultSet resultSet;
    public DatabaseManager manager = DatabaseManager.getInstance();
    private int staffPrimaryID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchOptions.getItems().addAll("Search By ID", "Search By Name", "Search By Destination");

        JFXDepthManager.setDepth(staffTable,2);
        JFXDepthManager.setDepth(region,2);

        reload();

        searchText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                search(new ActionEvent());
            }
        });

    }

    public void deleteSelected(ActionEvent actionEvent) {
        if (!staffTable.getSelectionModel().isEmpty()) {
            Tools.confirmation(event -> {
                String uniqueID = staffTable.getSelectionModel().getSelectedItem().getStaffID();
                staffPrimaryID = Tools.pkGetter(uniqueID);

                manager.execute("DELETE FROM tbl_Staff WHERE Staff_ID = " + staffPrimaryID);
                Tools.notification("Graphics/editdelete.png", "SUCCESS!!!", uniqueID + "    Deleted Successfully");

                //Reloading table
                observableList.removeIf(viewBookModel -> viewBookModel.getStaffID().equals(uniqueID));
                reloadTable(actionEvent);

            },"  CLICK HERE to confirm the Deletion\n\n   WARNING: Deleting this Staff Member will also Delete\n   the records associated with it");
        } else {
            Tools.notification("Graphics/SYSTEM ALERT STOP ICON.png", "WARNING!!!", "Please, Select an Item to delete");
        }
    }

    public void updateSelected(ActionEvent actionEvent) {
        String uniqueID;
        ViewStaffModel staffModel;
        if (!staffTable.getSelectionModel().isEmpty()) {
            uniqueID = staffTable.getSelectionModel().getSelectedItem().getStaffID();
            staffPrimaryID = Tools.pkGetter(uniqueID);
            staffModel = observableList.filtered(viewStaffModel -> viewStaffModel.getStaffID().equals(uniqueID)).get(0);

            //UPDATE STAGE BEGINS
            Parent parent = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateStaff/UpdateStaff.fxml"));
                parent = loader.load();
                UpdateStaffController controller = loader.getController();
                controller.initUpdateScene(staffModel);
                controller.setStaffID(staffPrimaryID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage(StageStyle.UNDECORATED);
            parent.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            parent.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("Graphics/c6.png"));
            stage.showAndWait();
            //UPDATE STAGE ENDS
            reload();


        } else {
            Tools.notification("/Graphics/SYSTEM ALERT STOP ICON.png", "WARNING!!!", "Please, Select an Item to Update");

        }
    }

    public void insertNewStaff(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/AddStaff/AddStaff.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage(StageStyle.UNDECORATED);
        parent.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        parent.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image("Graphics/c6.png"));
        stage.showAndWait();
        reload();
    }

    public void reloadTable(ActionEvent actionEvent) {
        reload();
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
        contact.setCellValueFactory(new PropertyValueFactory<>("staffContact"));
        designation.setCellValueFactory(new PropertyValueFactory<>("staffDesignation"));
        staffTable.setItems(observableList);

    }

    public void hitSearchOption(ActionEvent actionEvent) {
        searchText.setPromptText(searchOptions.getValue().substring(9));
        searchText.setText(null);
    }

    public void search(ActionEvent actionEvent) {
        if (!searchOptions.getSelectionModel().isEmpty()) {
            observableListAlter.removeAll(observableListAlter);
            if (searchOptions.getValue().equalsIgnoreCase("Search By ID")) {
                resultSet = manager.executeQuery("SELECT* FROM viewStaff WHERE ID = '" + searchText.getText() + "' ORDER By ID");

            } else if (searchOptions.getValue().equalsIgnoreCase("Search By Name")) {
                resultSet = manager.executeQuery("SELECT* FROM viewStaff WHERE Name LIKE '%" + searchText.getText() + "%' ORDER By ID");

            } else {
                resultSet = manager.executeQuery("SELECT * FROM viewStaff WHERE Designation = '" + searchText.getText() + "'ORDER By ID");

            }

            try {
                while (resultSet.next()) {
                    observableListAlter.add(new ViewStaffModel(resultSet.getString("ID"), resultSet.getString("Name"), resultSet.getString("Contact"), resultSet.getString("Designation")));
                }
                if (!resultSet.isBeforeFirst()) {
                    Label label = new Label("No Results Found \nPress \u27F2 to refresh the table");
                    label.setFont(new Font(16));
                    staffTable.setPlaceholder(label);
                }
            } catch (SQLException e) {
                Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
            }

            iD.setCellValueFactory(new PropertyValueFactory<>("staffID"));
            name.setCellValueFactory(new PropertyValueFactory<>("staffName"));
            contact.setCellValueFactory(new PropertyValueFactory<>("staffContact"));
            designation.setCellValueFactory(new PropertyValueFactory<>("staffDesignation"));
            staffTable.setItems(observableListAlter);

        } else {
            Tools.notification("Graphics/SYSTEM ALERT STOP ICON.png", "WARNING!!!", "Please, Select a Search Option");
        }
    }
}
