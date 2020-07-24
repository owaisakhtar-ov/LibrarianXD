package viewStudents;

import Database.DatabaseManager;
import MainUI.Tools;
import UpdateStudent.UpdateStudentController;
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
import java.util.Date;
import java.util.ResourceBundle;

public class ViewStudentsController implements Initializable {
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
    private TableView<ViewStudentModel> tableStudents;

    @FXML
    private TableColumn<ViewStudentModel, String> iD;

    @FXML
    private TableColumn<ViewStudentModel, String> name;

    @FXML
    private TableColumn<ViewStudentModel, String> contact;

    @FXML
    private TableColumn<ViewStudentModel, String> email;

    @FXML
    private TableColumn<ViewStudentModel, String> address;

    @FXML
    private TableColumn<ViewStudentModel, Date> regDate;

    @FXML
    private TableColumn<ViewStudentModel, Integer> noOfBooks;

    @FXML
    private TableColumn<ViewStudentModel, String> blacklisted;

    @FXML
    private JFXButton delete;

    @FXML
    private JFXButton update;

    @FXML
    private JFXButton insert;

    @FXML
    private JFXButton reloader;


    private ObservableList<ViewStudentModel> observableList = FXCollections.observableArrayList();
    private ObservableList<ViewStudentModel> observableListAlter = FXCollections.observableArrayList();
    private ResultSet resultSet;
    public DatabaseManager manager = DatabaseManager.getInstance();
    private int studentPrimaryID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchOptions.getItems().addAll("Search By ID", "Search By Name", "Search By Address", "Search By Contact");
        reload();



        JFXDepthManager.setDepth(tableStudents,2);
        JFXDepthManager.setDepth(region,2);

        searchText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                search(new ActionEvent());
            }
        });

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

        tableFeeder();
        tableStudents.setItems(observableList);
    }

    public void deleteSelected(ActionEvent actionEvent) {
        if (!tableStudents.getSelectionModel().isEmpty()) {
            Tools.confirmation(event -> {
                String uniqueID = tableStudents.getSelectionModel().getSelectedItem().getStdID();
                studentPrimaryID = Tools.pkGetter(uniqueID);

                manager.execute("DELETE FROM tbl_Student WHERE Student_ID = " + studentPrimaryID);
                Tools.notification("Graphics/editdelete.png", "SUCCESS!!!", uniqueID + "    Deleted Successfully");

                //Reloading table
                observableList.removeIf(viewStudentModel -> viewStudentModel.getStdID().equals(uniqueID));
                reloadTable(actionEvent);

            }, "  CLICK HERE to confirm the Deletion\n\n   WARNING: Deleting this Student will also Delete\n   the records associated with it");
        } else {
            Tools.notification("Graphics/SYSTEM ALERT STOP ICON.png", "WARNING!!!", "Please, Select an Item to delete");
        }
    }

    public void updateSelected(ActionEvent actionEvent) {
        String uniqueID;
        ViewStudentModel studentModel;
        if (!tableStudents.getSelectionModel().isEmpty()) {
            uniqueID = tableStudents.getSelectionModel().getSelectedItem().getStdID();
            studentPrimaryID = Tools.pkGetter(uniqueID);
            studentModel = observableList.filtered(viewStudentModel -> viewStudentModel.getStdID().equals(uniqueID)).get(0);

            //UPDATE STAGE BEGINS
            Parent parent = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateStudent/UpdateStudent.fxml"));
                parent = loader.load();
                UpdateStudentController controller = loader.getController();
                controller.initUpdateScene(studentModel);
                controller.setStudentID(studentPrimaryID);
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

    public void insertNewStudent(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/AddStudents/AddStudents.fxml"));
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

    private void tableFeeder() {
        iD.setCellValueFactory(new PropertyValueFactory<>("stdID"));
        name.setCellValueFactory(new PropertyValueFactory<>("stdName"));
        contact.setCellValueFactory(new PropertyValueFactory<>("stdContact"));
        email.setCellValueFactory(new PropertyValueFactory<>("stdEmail"));
        address.setCellValueFactory(new PropertyValueFactory<>("stdAddress"));
        regDate.setCellValueFactory(new PropertyValueFactory<>("stdRegDate"));
        noOfBooks.setCellValueFactory(new PropertyValueFactory<>("stdNoOfBooks"));
        blacklisted.setCellValueFactory(new PropertyValueFactory<>("stdBlacklisted"));
    }

    public void hitSearchOption(ActionEvent actionEvent) {
        searchText.setPromptText(searchOptions.getValue().substring(9));
        searchText.setText(null);
    }

    public void search(ActionEvent actionEvent) {
        if (!searchOptions.getSelectionModel().isEmpty()) {
            observableListAlter.removeAll(observableListAlter);
            if (searchOptions.getValue().equalsIgnoreCase("Search By ID")) {
                resultSet = manager.executeQuery("SELECT* FROM viewStudent WHERE ID = '" + searchText.getText() + "' ORDER By ID");

            } else if (searchOptions.getValue().equalsIgnoreCase("Search By Name")) {
                resultSet = manager.executeQuery("SELECT* FROM viewStudent WHERE Name LIKE '%" + searchText.getText() + "%' ORDER By ID");

            } else if (searchOptions.getValue().equalsIgnoreCase("Search By Address")) {
                resultSet = manager.executeQuery("SELECT * FROM viewStudent WHERE Address LIKE '%" + searchText.getText() + "%'ORDER By ID");

            } else {
                resultSet = manager.executeQuery("SELECT* FROM viewStudent WHERE Contact = '" + searchText.getText() + "' ORDER By ID");

            }

            try {
                while (resultSet.next()) {
                    observableListAlter.add(new ViewStudentModel(resultSet.getString("ID"), resultSet.getString("Name"), resultSet.getString("Contact"), resultSet.getString("Email"), resultSet.getString("Address"), resultSet.getDate("Registration Date"), resultSet.getInt("No. Of Books Issued"), resultSet.getString("Blacklisted")));
                }
                if (!resultSet.isBeforeFirst()) {
                    Label label = new Label("No Results Found \nPress \u27F2 to refresh the table");
                    label.setFont(new Font(16));
                    tableStudents.setPlaceholder(label);
                }
            } catch (SQLException e) {
                Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
            }

            tableFeeder();
            tableStudents.setItems(observableListAlter);

        } else {
            Tools.notification("Graphics/SYSTEM ALERT STOP ICON.png", "WARNING!!!", "Please, Select a Search Option");
        }
    }
}
