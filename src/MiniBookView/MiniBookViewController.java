package MiniBookView;

import Database.DatabaseManager;
import MainUI.Tools;
import ViewBooks.ViewBookModel;
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

public class MiniBookViewController implements Initializable {
    public Region region;
    @FXML
    private JFXTextField searchNameText;

    @FXML
    private JFXButton searchName;

    @FXML
    private TableView<ViewBookModel> tableBook;

    @FXML
    private TableColumn<ViewBookModel, String> iD;

    @FXML
    private TableColumn<ViewBookModel, String> name;

    @FXML
    private TableColumn<ViewBookModel, String> writer;

    @FXML
    private TableColumn<ViewBookModel, Integer> edition;

    @FXML
    private TableColumn<ViewBookModel, Integer> quantity;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXButton minButton;

    private static int bookPrimaryID;
    private static String bookName;
    private int bookQuantity;
    private ObservableList<ViewBookModel> observableList = FXCollections.observableArrayList();
    private ObservableList<ViewBookModel> observableListAlter = FXCollections.observableArrayList();
    private ResultSet resultSet;
    DatabaseManager manager = DatabaseManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reload();

        JFXDepthManager.setDepth(region,2);
        JFXDepthManager.setDepth(tableBook,2);

        tableBook.setOnMousePressed(event -> {

            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                bookQuantity = tableBook.getSelectionModel().getSelectedItem().getBookQuantity();
                if (bookQuantity == 0) {
                    Tools.notification("/Graphics/!.png", "SORRY!!!", "This book isn't Available right now");
                } else {
                    bookPrimaryID = Tools.pkGetter(tableBook.getSelectionModel().getSelectedItem().getBookID());
                    bookName = tableBook.getSelectionModel().getSelectedItem().getBookName();
                    Tools.close(tableBook);
                }
            }

        });
    }

    private void reload() {
        observableList.removeAll(observableList);
        try {
            resultSet = manager.executeQuery("SELECT * FROM viewBooks ORDER BY ID");
            while (resultSet.next()) {
                observableList.add(new ViewBookModel(resultSet.getString("ID"), resultSet.getString("Name"), resultSet.getString("Language"), resultSet.getString("Writer"), resultSet.getInt("Quantity"),resultSet.getInt("Edition"), resultSet.getString("Genre")));
            }

        } catch (SQLException e) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
        }

        iD.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        name.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        writer.setCellValueFactory(new PropertyValueFactory<>("bookWriter"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("bookQuantity"));
        edition.setCellValueFactory(new PropertyValueFactory<>("bookEdition"));
        tableBook.setItems(observableList);
    }

    public void searchName(ActionEvent actionEvent) {
        observableListAlter.removeAll(observableListAlter);
        try {
            resultSet = manager.executeQuery("SELECT * FROM viewBooks WHERE Name LIKE '%" + searchNameText.getText() + "%'");
            while (resultSet.next()) {
                observableListAlter.add(new ViewBookModel(resultSet.getString("ID"), resultSet.getString("Name"), resultSet.getString("Language"), resultSet.getString("Writer"), resultSet.getInt("Quantity"),resultSet.getInt("Edition"), resultSet.getString("Genre")));
            }
            if (!resultSet.isBeforeFirst()) {
                Label label = new Label("No Results Found \nPress \u27F2 to refresh the table");
                label.setFont(new Font(16));
                tableBook.setPlaceholder(label);
            }

        } catch (SQLException e) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
        }

        iD.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        name.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        writer.setCellValueFactory(new PropertyValueFactory<>("bookWriter"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("bookQuantity"));
        tableBook.setItems(observableListAlter);
    }

    public void close(ActionEvent actionEvent) {
        Tools.close(minButton);
    }

    public void minimize(ActionEvent actionEvent) {
        Tools.minimize(minButton);
    }

    public static int getBookPrimaryID() {
        return bookPrimaryID;
    }

    public static String getBookName() {
        return bookName;
    }
}
