package MiniIssueView;

import Database.DatabaseManager;
import Issuance.ViewIssueModel;
import MainUI.Tools;
import ViewStaff.ViewStaffModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.effects.JFXDepthManager;
import javafx.beans.binding.Bindings;
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

import javax.naming.Binding;
import javax.tools.Tool;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class MiniIssueViewController implements Initializable {
    @FXML
    private Region region;

    @FXML
    private JFXDatePicker searchDateValue;

    @FXML
    private JFXButton searchDate;

    @FXML
    private TableView<ViewIssueModel> tableIssue;

    @FXML
    private TableColumn<ViewIssueModel, String> iD;

    @FXML
    private TableColumn<ViewIssueModel, Date> issueDate;

    @FXML
    private TableColumn<ViewIssueModel, Date> issueTill;

    @FXML
    private TableColumn<ViewIssueModel, String> stdID;

    @FXML
    private TableColumn<ViewIssueModel, String> bookName;

    @FXML
    private TableColumn<ViewIssueModel, String> staffID;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXButton minButton;

    private static int issuePrimaryID;
    private static int stdPrimaryID;
    private static int bookPrimaryID;
    private ObservableList<ViewIssueModel> observableList = FXCollections.observableArrayList();
    private ObservableList<ViewIssueModel> observableListAlter = FXCollections.observableArrayList();
    private ArrayList<String> returnValues = new ArrayList<>();
    private ResultSet resultSet;
    DatabaseManager manager = DatabaseManager.getInstance();

    public static int getIssuePrimaryID() {
        return issuePrimaryID;
    }

    public static int getStdPrimaryID() {
        return stdPrimaryID;
    }

    public static int getBookPrimaryID() {
        return bookPrimaryID;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getReturnValues();
        reload();
        searchDateValue.setValue(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        JFXDepthManager.setDepth(region,2);
        JFXDepthManager.setDepth(tableIssue,2);

        if (!observableList.isEmpty()) {
            tableIssue.setOnMousePressed(event -> {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    issuePrimaryID = Tools.pkGetter(tableIssue.getSelectionModel().getSelectedItem().getIssueID());
    //                stdPrimaryID = Tools.pkGetter(tableIssue.getSelectionModel().getSelectedItem().getStdID());
                    ViewIssueModel issueModel = observableList.filtered(viewIssueModel -> viewIssueModel.getIssueID().equals("IS" + issuePrimaryID)).get(0);
                    stdPrimaryID = Tools.pkGetter(issueModel.getStdID());
                    bookPrimaryID = Tools.pkGetter(issueModel.getBookID());
                    Tools.close(tableIssue);
                }
            });
        }


    }

    private void getReturnValues(){
        resultSet = manager.executeQuery("SELECT * FROM viewReturn ORDER BY ID");
        try {
            while (resultSet.next()) {
                returnValues.add(resultSet.getString("Issue ID"));
            }
        } catch (SQLException e) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
            e.printStackTrace();
        }


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
        observableList.removeIf(viewIssueModel -> returnValues.contains(viewIssueModel.getIssueID()));

        if (observableList.isEmpty()) {
            Label label = new Label("No Issue entry Available ");
            label.setFont(new Font(16));
            tableIssue.setPlaceholder(label);
        }

        tableIssue.setItems(observableList);
    }

    private void dataFeeder() {
        iD.setCellValueFactory(new PropertyValueFactory<>("issueID"));
        issueDate.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        issueTill.setCellValueFactory(new PropertyValueFactory<>("issueTill"));
        stdID.setCellValueFactory(new PropertyValueFactory<>("stdID"));
        bookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        staffID.setCellValueFactory(new PropertyValueFactory<>("staffID"));
    }

    public void searchDate(ActionEvent actionEvent) {
        observableListAlter.removeAll(observableListAlter);
        try {
            resultSet = manager.executeQuery("SELECT* FROM viewIssuance WHERE [Issue Date] = '" + searchDateValue.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "' ORDER By ID");
            while (resultSet.next()) {
                observableListAlter.add(new ViewIssueModel(resultSet.getString("ID"), resultSet.getDate("Issue Date"), resultSet.getDate("Issue Till"), resultSet.getString("Student ID"), resultSet.getString("Student Name"), resultSet.getString("Book ID"), resultSet.getString("Book Name"), resultSet.getString("Staff ID"), resultSet.getString("Staff Name")));
            }
            if (!resultSet.isBeforeFirst()) {
                Label label = new Label("No Results Found \nPress \u27F2 to refresh the table");
                label.setFont(new Font(16));
                tableIssue.setPlaceholder(label);
            }
        } catch (SQLException ex) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
        }

        dataFeeder();
        observableListAlter.removeIf(viewIssueModel -> returnValues.contains(viewIssueModel.getIssueID()));
        tableIssue.setItems(observableListAlter);

    }

    public void close(ActionEvent actionEvent) {
        Tools.close(region);
    }

    public void minimize(ActionEvent actionEvent) {
        Tools.minimize(region);
    }
}
