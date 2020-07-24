package Issuance;

import Database.DatabaseManager;
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

public class IssuanceController implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Region region;

    @FXML
    private Label dateLabel;

    @FXML
    public JFXDatePicker searchDate;

    @FXML
    private JFXComboBox<String> searchOptions;

    @FXML
    private JFXTextField searchText;

    @FXML
    private JFXButton search;

    @FXML
    private TableView<ViewIssueModel> issueTable;

    @FXML
    private TableColumn<ViewIssueModel, String> iD;

    @FXML
    private TableColumn<ViewIssueModel, Date> issueDate;

    @FXML
    private TableColumn<ViewIssueModel, Date> issueTill;

    @FXML
    private TableColumn<ViewIssueModel, String> stdID;

    @FXML
    private TableColumn<ViewIssueModel, String> stdName;

    @FXML
    private TableColumn<ViewIssueModel, String> bookID;

    @FXML
    private TableColumn<ViewIssueModel, String> bookName;

    @FXML
    private TableColumn<ViewIssueModel, String> staffID;

    @FXML
    private TableColumn<ViewIssueModel, String> staffName;

    @FXML
    private JFXButton issueNewBook;

    @FXML
    private JFXButton reloader;


    private ObservableList<ViewIssueModel> observableList = FXCollections.observableArrayList();
    private ObservableList<ViewIssueModel> observableListAlter = FXCollections.observableArrayList();
    private ResultSet resultSet;
    public DatabaseManager manager = DatabaseManager.getInstance();
//    private boolean isDate;
//    private int issueID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reload();
        searchOptions.getItems().addAll("Search By ID", "Search By Issue Date", "Search By Student's ID", "Search By Student's Name", "Search By Book's ID", "Search By Books's Name", "Search By Staffs' ID", "Search By Staffs' Name");
        searchDate.setDisable(true);

        JFXDepthManager.setDepth(issueTable, 2);
        JFXDepthManager.setDepth(region,2);
    }

    public void issueNewBook(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/IssueBook/IssueBook.fxml"));
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

    public void search(ActionEvent actionEvent) {
        if (!searchOptions.getSelectionModel().isEmpty()) {
            observableListAlter.removeAll(observableListAlter);
            if (searchOptions.getValue().equalsIgnoreCase("Search By ID")) {
                resultSet = manager.executeQuery("SELECT* FROM viewIssuance WHERE ID = '" + searchText.getText() + "' ORDER By ID");

            } else if (searchOptions.getValue().equalsIgnoreCase("Search By Issue Date")) {
                resultSet = manager.executeQuery("SELECT* FROM viewIssuance WHERE [Issue Date] = '" + searchDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "' ORDER By ID");

            } else if (searchOptions.getValue().equalsIgnoreCase("Search By Student's ID")) {
                resultSet = manager.executeQuery("SELECT* FROM viewIssuance WHERE [Student ID] = '" + searchText.getText() + "' ORDER By ID");

            } else if (searchOptions.getValue().equalsIgnoreCase("Search By Student's Name")) {
                resultSet = manager.executeQuery("SELECT * FROM viewIssuance WHERE [Student Name] like '%" + searchText.getText() + "%' ORDER By ID");

            } else if (searchOptions.getValue().equalsIgnoreCase("Search By Book's ID")) {
                resultSet = manager.executeQuery("SELECT * FROM viewIssuance WHERE [Book ID] = '" + searchText.getText() + "' ORDER By ID");

            } else if (searchOptions.getValue().equalsIgnoreCase("Search By Books's Name")) {
                resultSet = manager.executeQuery("SELECT * FROM viewIssuance WHERE [Book Name] LIKE '%" + searchText.getText() + "%' ORDER By ID");

            } else if (searchOptions.getValue().equalsIgnoreCase("Search By Staffs' ID")) {
                resultSet = manager.executeQuery("SELECT * FROM viewIssuance WHERE [Staff ID] = '" + searchText.getText() + "'");

            } else {
                resultSet = manager.executeQuery("SELECT * FROM viewIssuance WHERE [Staff Name] LIKE '%" + searchText.getText() + "%'");

            }

            try {
                while (resultSet.next()) {
                    observableListAlter.add(new ViewIssueModel(resultSet.getString("ID"), resultSet.getDate("Issue Date"), resultSet.getDate("Issue Till"), resultSet.getString("Student ID"), resultSet.getString("Student Name"), resultSet.getString("Book ID"), resultSet.getString("Book Name"), resultSet.getString("Staff ID"), resultSet.getString("Staff Name")));
                }
                if (!resultSet.isBeforeFirst()) {
                    Label label = new Label("No Results Found \nPress \u27F2 to refresh the table");
                    label.setFont(new Font(16));
                    issueTable.setPlaceholder(label);
                }
            } catch (SQLException e) {
                Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
            }
            dataFeeder();
            issueTable.setItems(observableListAlter);

        } else {
            Tools.notification("Graphics/SYSTEM ALERT STOP ICON.png", "WARNING!!!", "Please, Select a Search Option");
        }
    }

    public void reloadTable(ActionEvent actionEvent) {
        reload();
    }

    private void reload() {
        observableList.removeAll(observableList);
        try {
            resultSet = manager.executeQuery("SELECT* FROM viewIssuance ORDER By ID");
            while (resultSet.next()) {
                observableList.add(new ViewIssueModel(resultSet.getString("ID"), resultSet.getDate("Issue Date"), resultSet.getDate("Issue Till"), resultSet.getString("Student ID"), resultSet.getString("Student Name"), resultSet.getString("Book ID"), resultSet.getString("Book Name"), resultSet.getString("Staff ID"), resultSet.getString("Staff Name")));
            }
        } catch (SQLException e) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
            e.printStackTrace();
        }
        dataFeeder();
        issueTable.setItems(observableList);
    }

    private void dataFeeder() {
        iD.setCellValueFactory(new PropertyValueFactory<>("issueID"));
        issueDate.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        issueTill.setCellValueFactory(new PropertyValueFactory<>("issueTill"));
        stdID.setCellValueFactory(new PropertyValueFactory<>("stdID"));
        stdName.setCellValueFactory(new PropertyValueFactory<>("stdName"));
        bookID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        bookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        staffID.setCellValueFactory(new PropertyValueFactory<>("staffID"));
        staffName.setCellValueFactory(new PropertyValueFactory<>("staffName"));

    }

    public void hitSearchOption(ActionEvent actionEvent) {

        if (searchOptions.getValue().contains("Date")) {
            dateLabel.setText(searchOptions.getValue().substring(10));
            searchDate.setDisable(false);
            searchText.setDisable(true);
            searchDate.setValue(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
//            isDate = true;
        } else {
            searchText.setPromptText(searchOptions.getValue().substring(9));
            searchDate.setDisable(true);
            searchText.setDisable(false);
//            isDate = false;
        }
        searchText.setText(null);

    }

//    public void deleteSelected(ActionEvent actionEvent) {
//        if (!issueTable.getSelectionModel().isEmpty()) {
//            Tools.confirmation(event -> {
//                String uniqueID = issueTable.getSelectionModel().getSelectedItem().getIssueID();
//                issueID = Tools.pkGetter(uniqueID);
//
//                manager.execute("DELETE FROM tbl_Issuance WHERE Issue_ID = " + issueID);
//                Tools.notification("Graphics/editdelete.png", "SUCCESS!!!", uniqueID + "    Deleted Successfully");
//
//                //Reloading table
//                observableList.removeIf(viewIssueModel -> viewIssueModel.getIssueID().equals(uniqueID));
//                reloadTable(actionEvent);
//
//            }, "  CLICK HERE to confirm the Deletion\n\n   WARNING: Make sure to delete this Issue Entry\n   from \"Return\" Otherwise DataBase Error Occur");
//        } else {
//            Tools.notification("Graphics/SYSTEM ALERT STOP ICON.png", "WARNING!!!", "Please, Select an Item to delete");
//        }
//
//    }
}
