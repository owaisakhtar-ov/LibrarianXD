package Returns;

import Database.DatabaseManager;
import Issuance.ViewIssueModel;
import MainUI.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
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
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class ReturnsController implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Label dateLabel;

    @FXML
    private Region region;

    @FXML
    private JFXComboBox<String> searchOptions;

    @FXML
    private JFXTextField searchText;

    @FXML
    private JFXDatePicker searchDate;

    @FXML
    private JFXButton search;

    @FXML
    private TableView<ViewReturnsModel> returnsTable;

    @FXML
    private TableColumn<ViewReturnsModel, String> iD;

    @FXML
    private TableColumn<ViewReturnsModel, String> issueID;

    @FXML
    private TableColumn<ViewReturnsModel, Date> issueDate;

    @FXML
    private TableColumn<ViewReturnsModel, Date> issueTill;

    @FXML
    private TableColumn<ViewReturnsModel, Date> receivedDate;

    @FXML
    private TableColumn<ViewReturnsModel, String> staffName;

    @FXML
    private TableColumn<ViewReturnsModel, String> staffID;

    @FXML
    private JFXButton reloader1;

    @FXML
    private JFXButton issueNewBook;

    @FXML
    private JFXButton reloader;

    private ObservableList<ViewReturnsModel> observableList = FXCollections.observableArrayList();
    private ObservableList<ViewReturnsModel> observableListAlter = FXCollections.observableArrayList();
    private DatabaseManager manager = DatabaseManager.getInstance();
    private int returnsPrimaryID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reload();
        searchOptions.getItems().addAll("Search By ID", "Search By Issue Date", "Search By Received Date", "Search By Issue ID", "Search By Staffs' Name", "Search By Staffs' ID");
        searchDate.setDisable(true);
        JFXDepthManager.setDepth(returnsTable, 2);
        JFXDepthManager.setDepth(region,2);

    }

    private void reload() {
        observableList.removeAll(observableList);
        try {
            ResultSet resultSet = manager.executeQuery("SELECT* FROM viewReturn ORDER By ID");
            while (resultSet.next()) {
                observableList.add(new ViewReturnsModel(resultSet.getString("ID"), resultSet.getDate("Issue Date"), resultSet.getDate("Issue Till"), resultSet.getDate("Return Date"), resultSet.getString("Issue ID"), resultSet.getString("Recieved By"), resultSet.getString("Staff ID")));
            }
        } catch (SQLException e) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
            e.printStackTrace();
        }
        dataFeeder();
        returnsTable.setItems(observableList);
    }

    private void dataFeeder() {
        iD.setCellValueFactory(new PropertyValueFactory<>("retID"));
        issueID.setCellValueFactory(new PropertyValueFactory<>("issueID"));
        issueDate.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        issueTill.setCellValueFactory(new PropertyValueFactory<>("issueTill"));
        receivedDate.setCellValueFactory(new PropertyValueFactory<>("receivedDate"));
        staffName.setCellValueFactory(new PropertyValueFactory<>("staffName"));
        staffID.setCellValueFactory(new PropertyValueFactory<>("staffID"));
    }

    public void hitSearchOption(ActionEvent actionEvent) {

        if (searchOptions.getValue().contains("Date")) {
            dateLabel.setText(searchOptions.getValue().substring(10));
            searchDate.setDisable(false);
            searchText.setDisable(true);
            searchDate.setValue(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        } else {
            searchText.setPromptText(searchOptions.getValue().substring(9));
            searchDate.setDisable(true);
            searchText.setDisable(false);
        }
        searchText.setText(null);
    }

    public void search(ActionEvent actionEvent) {
        ResultSet resultSet;
        if (!searchOptions.getSelectionModel().isEmpty()) {
            observableListAlter.removeAll(observableListAlter);
            if (searchOptions.getValue().equalsIgnoreCase("Search By ID")) {
                resultSet = manager.executeQuery("SELECT* FROM viewReturn WHERE ID = '" + searchText.getText() + "' ORDER By ID");

            } else if (searchOptions.getValue().equalsIgnoreCase("Search By Issue Date")) {
                resultSet = manager.executeQuery("SELECT* FROM viewReturn WHERE [Issue Date] = '" + searchDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "' ORDER By ID");

            } else if (searchOptions.getValue().equalsIgnoreCase("Search By Received Date")) {
                resultSet = manager.executeQuery("SELECT* FROM viewReturn WHERE [Return Date] = '" + searchDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "' ORDER By ID");

            } else if (searchOptions.getValue().equalsIgnoreCase("Search By Issue ID")) {
                resultSet = manager.executeQuery("SELECT* FROM viewReturn WHERE [Issue ID] = '" + searchText.getText() + "'");

            } else if (searchOptions.getValue().equalsIgnoreCase("Search By Staffs' Name")) {
                resultSet = manager.executeQuery("SELECT* FROM viewReturn WHERE [Recieved By] LIKE '%" + searchText.getText() + "%' ORDER By ID");

            } else {
                resultSet = manager.executeQuery("SELECT* FROM viewReturn WHERE [Staff ID] = '" + searchText.getText() + "'");

            }

            try {
                while (resultSet.next()) {
                    observableListAlter.add(new ViewReturnsModel(resultSet.getString("ID"), resultSet.getDate("Issue Date"), resultSet.getDate("Return Date"), resultSet.getDate("Issue Till"), resultSet.getString("Issue ID"), resultSet.getString("Recieved By"), resultSet.getString("Staff ID")));
                }
                if (!resultSet.isBeforeFirst()) {
                    Label label = new Label("No Results Found \nPress \u27F2 to refresh the table");
                    label.setFont(new Font(16));
                    returnsTable.setPlaceholder(label);
                }
            } catch (SQLException e) {
                Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
            }
            dataFeeder();
            returnsTable.setItems(observableListAlter);

        } else {
            Tools.notification("Graphics/SYSTEM ALERT STOP ICON.png", "WARNING!!!", "Please, Select a Search Option");
        }
    }

    public void reloadTable(ActionEvent actionEvent) {
        reload();
    }

    public void issueNewBook(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/ReturnBook/ReturnBook.fxml"));
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
        stage.getIcons().add(new Image("/Graphics/main.png"));
        stage.showAndWait();
        reload();
    }

    public void deleteSelected(ActionEvent actionEvent) {
        if (!returnsTable.getSelectionModel().isEmpty()) {
            Tools.confirmation(event -> {
                String uniqueID = returnsTable.getSelectionModel().getSelectedItem().getRetID();
                returnsPrimaryID = Tools.pkGetter(uniqueID);

                manager.execute("DELETE FROM tbl_Return WHERE Return_ID = " + returnsPrimaryID);
                Tools.notification("Graphics/editdelete.png", "SUCCESS!!!", uniqueID + "    Deleted Successfully");

                //Reloading table
                observableList.removeIf(viewReturnsModel -> viewReturnsModel.getIssueID().equals(uniqueID));
                reloadTable(actionEvent);

            }, "  CLICK HERE to confirm the Deletion\n\n   WARNING: Deleting this Return Entry will also \n   Delete the records associated with it in Issuance");
        } else {
            Tools.notification("Graphics/SYSTEM ALERT STOP ICON.png", "WARNING!!!", "Please, Select an Item to delete ");
        }
    }
}
