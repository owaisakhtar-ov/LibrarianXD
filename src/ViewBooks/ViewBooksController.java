package ViewBooks;

import Database.DatabaseManager;
import MainUI.Tools;
import UpdateBooks.UpdateBooksController;
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

public class ViewBooksController implements Initializable {
    public JFXButton reloader;
    public Region region;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public JFXTextField searchText;

    @FXML
    public JFXButton search;

    @FXML
    private JFXComboBox<String> searchOptions;

    @FXML
    private TableView<ViewBookModel> booksTable;

    @FXML
    private TableColumn<ViewBookModel, String> iD;

    @FXML
    private TableColumn<ViewBookModel, String> name;

    @FXML
    public TableColumn<ViewBookModel, Integer> edition;

    @FXML
    private TableColumn<ViewBookModel, String> language;

    @FXML
    private TableColumn<ViewBookModel, String> writer;

    @FXML
    private TableColumn<ViewBookModel, Integer> quantity;

    @FXML
    private TableColumn<ViewBookModel, String> genre;

    @FXML
    public JFXButton insert;
    @FXML
    private JFXButton delete;
    @FXML
    private JFXButton update;

    private ObservableList<ViewBookModel> observableList = FXCollections.observableArrayList();
    private ObservableList<ViewBookModel> observableListAlter = FXCollections.observableArrayList();
    private ResultSet resultSet;
    public DatabaseManager manager = DatabaseManager.getInstance();
    private int booksPrimaryID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        reload();
        searchOptions.getItems().addAll("Search By ID", "Search By Name", "Search By Writer");

//        JFXDepthManager depthManager = new JFXDepthManager();
        JFXDepthManager.setDepth(booksTable, 2);
        JFXDepthManager.setDepth(region,2);

        //FOR FURTHER POLISHING LATER
        searchText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                search(new ActionEvent());
            }
        });

    }

    public void deleteSelected(ActionEvent actionEvent) {

        if (!booksTable.getSelectionModel().isEmpty()) {
            Tools.confirmation(event -> {
                String uniqueID = booksTable.getSelectionModel().getSelectedItem().getBookID();
                booksPrimaryID = Tools.pkGetter(uniqueID);

                manager.execute("DELETE FROM tbl_Book WHERE Book_ID = " + booksPrimaryID);
                Tools.notification("Graphics/editdelete.png", "SUCCESS!!!", uniqueID + "    Deleted Successfully");

                //Reloading table
                observableList.removeIf(viewBookModel -> viewBookModel.getBookID().equals(uniqueID));
                reloadTable(actionEvent);

            }, "  CLICK HERE to confirm the Deletion\n\n   WARNING: Deleting this book will also Delete\n   the records associated with it");
        } else {
            Tools.notification("Graphics/SYSTEM ALERT STOP ICON.png", "WARNING!!!", "Please, Select an Item to delete ");
        }
    }

    public void updateSelected(ActionEvent actionEvent) {
        String uniqueID;
        ViewBookModel bookModel;
        if (!booksTable.getSelectionModel().isEmpty()) {
            uniqueID = booksTable.getSelectionModel().getSelectedItem().getBookID();
            booksPrimaryID = Tools.pkGetter(uniqueID);
            bookModel = observableList.filtered(viewBookModel -> viewBookModel.getBookID().equals(uniqueID)).get(0);

            //UPDATE STAGE BEGIN
            Parent parent = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateBooks/UpdateBooks.fxml"));
                parent = loader.load();
                UpdateBooksController controller = loader.getController();
                controller.initUpdateScene(bookModel);
                controller.setBookID(booksPrimaryID);

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

    public void insertNewBook(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/AddBooks/AddBooks.fxml"));
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
            resultSet = manager.executeQuery("SELECT * FROM viewBooks ORDER BY ID");
            while (resultSet.next()) {
                observableList.add(new ViewBookModel(resultSet.getString("ID"), resultSet.getString("Name"), resultSet.getString("Language"), resultSet.getString("Writer"), resultSet.getInt("Quantity"), resultSet.getInt("Edition"), resultSet.getString("Genre")));
            }

        } catch (SQLException e) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
        }

        iD.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        name.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        language.setCellValueFactory(new PropertyValueFactory<>("bookLanguage"));
        writer.setCellValueFactory(new PropertyValueFactory<>("bookWriter"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("bookQuantity"));
        edition.setCellValueFactory(new PropertyValueFactory<>("bookEdition"));
        genre.setCellValueFactory(new PropertyValueFactory<>("bookGenre"));

        booksTable.setItems(observableList);
    }

    public void hitSearchOption(ActionEvent actionEvent) {
        searchText.setPromptText(searchOptions.getValue().substring(9));
        searchText.setText(null);
    }

    public void search(ActionEvent actionEvent) {
        if (!searchOptions.getSelectionModel().isEmpty()) {
            observableListAlter.removeAll(observableListAlter);
            if (searchOptions.getValue().equalsIgnoreCase("Search By ID")) {
                resultSet = manager.executeQuery("SELECT* FROM viewBooks WHERE ID = '" + searchText.getText() + "' ORDER By ID");

            } else if (searchOptions.getValue().equalsIgnoreCase("Search By Name")) {
                resultSet = manager.executeQuery("SELECT* FROM viewBooks WHERE Name LIKE '%" + searchText.getText() + "%' ORDER By ID");

            } else {
                resultSet = manager.executeQuery("SELECT * FROM viewBooks WHERE Writer LIKE '%" + searchText.getText() + "%'ORDER By ID");

            }

            try {
                while (resultSet.next()) {
                    observableListAlter.add(new ViewBookModel(resultSet.getString("ID"), resultSet.getString("Name"), resultSet.getString("Language"), resultSet.getString("Writer"), resultSet.getInt("Quantity"), resultSet.getInt("Edition"), resultSet.getString("Genre")));
                }
                if (!resultSet.isBeforeFirst()) {
                    Label label = new Label("No Results Found \nPress \u27F2 to refresh the table");
                    label.setFont(new Font(16));
                    booksTable.setPlaceholder(label);
                }
            } catch (SQLException e) {
                Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
            }

            iD.setCellValueFactory(new PropertyValueFactory<>("bookID"));
            name.setCellValueFactory(new PropertyValueFactory<>("bookName"));
            language.setCellValueFactory(new PropertyValueFactory<>("bookLanguage"));
            writer.setCellValueFactory(new PropertyValueFactory<>("bookWriter"));
            quantity.setCellValueFactory(new PropertyValueFactory<>("bookQuantity"));
            genre.setCellValueFactory(new PropertyValueFactory<>("bookGenre"));
            booksTable.setItems(observableListAlter);

        } else {
            Tools.notification("Graphics/SYSTEM ALERT STOP ICON.png", "WARNING!!!", "Please, Select a Search Option");
        }

    }
}
