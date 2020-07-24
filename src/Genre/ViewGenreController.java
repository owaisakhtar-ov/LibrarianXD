package Genre;

import Database.DatabaseManager;
import MainUI.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class ViewGenreController implements Initializable {

    public ImageView frame;

    @FXML
    private JFXButton deleteGenre;

    @FXML
    private TableView<GenreModel> genreTable;

    @FXML
    private TableColumn<GenreModel, String> genreID;

    @FXML
    private TableColumn<GenreModel, String> genreName;

    @FXML
    private JFXTextField newGenreName;

    @FXML
    private JFXButton InsertGenre;

    @FXML
    private JFXButton minButton;

    @FXML
    private JFXButton closeButton;

    private ObservableList<GenreModel> observableList = FXCollections.observableArrayList();
    private ResultSet resultSet;

    private static int genrePrimaryID;
    private static String genreReferenceName;
    DatabaseManager manager = DatabaseManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            resultSet = manager.executeQuery("SELECT * FROM tbl_Genre ORDER BY Genre_ID");
            while (resultSet.next()) {
//                observableList.add(new GenreModel(resultSet.getString("ID"), resultSet.getString("Name")));
                observableList.add(new GenreModel(resultSet.getString("Genre_UniqueID"), resultSet.getString("Genre_Name"), resultSet.getInt("Genre_ID")));
            }

        } catch (SQLException e) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Connection Error");
        }
        //Initialize
        genreID.setCellValueFactory(new PropertyValueFactory<>("genre_UniqueID"));
        genreName.setCellValueFactory(new PropertyValueFactory<>("genreName"));
        genreTable.setItems(observableList);
        //Row Click Event
        genreTable.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                this.genrePrimaryID = genreTable.getSelectionModel().getSelectedItem().getGenre_ID();
                genreReferenceName = genreTable.getSelectionModel().getSelectedItem().getGenreName();
                Tools.close(this.genreTable);
            }
        });
    }

    public void insertNewGenre(ActionEvent actionEvent) {
        List nameList = new ArrayList<String>();
        for (int i = 0; i < observableList.size(); i++) {
            nameList.add(observableList.get(i).getGenreName().toUpperCase());
        }
        if (nameList.contains(newGenreName.getText().toUpperCase())) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "\"" + newGenreName.getText() + "\" is Already in the List");
            return;
        }
        try {
//            Statement statement = connection.createStatement();
            genreTable.setItems(observableList);
            if (!newGenreName.getText().equals("")) {
                observableList.removeAll(observableList);
                manager.execute("INSERT INTO tbl_Genre VALUES('" + newGenreName.getText() + "')");
                resultSet = manager.executeQuery("SELECT * FROM tbl_Genre ORDER BY Genre_ID");
                while (resultSet.next()) {
                    observableList.add(new GenreModel(resultSet.getString("Genre_UniqueID"), resultSet.getString("Genre_Name"), resultSet.getInt("Genre_ID")));
                }
                genreTable.setItems(observableList);
                Tools.notification("Graphics/edit_add.png", "SUCCESS!!!", "Genre Added Successfully");

            } else {
                Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Genre Name can't be Empty");
            }
        } catch (SQLException e) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
        }

    }

    public void close(javafx.event.ActionEvent actionEvent) {
        Tools.close(newGenreName);
    }

    public void minimize(ActionEvent actionEvent) {
        Tools.minimize(newGenreName);
    }

    public static int getGenrePrimaryID() {
        return genrePrimaryID;
    }

    public static String getGenreReferenceName() {
        return genreReferenceName;
    }

    public void deleteGenre(ActionEvent actionEvent) {
        if (!genreTable.getSelectionModel().isEmpty()) {
            Tools.confirmation(event -> {
                String uniqueID = genreTable.getSelectionModel().getSelectedItem().getGenre_UniqueID();
                genrePrimaryID = Tools.pkGetter(uniqueID);

                manager.execute("DELETE FROM tbl_Genre WHERE Genre_ID = " + genrePrimaryID);
                Tools.notification("Graphics/editdelete.png", "SUCCESS!!!", uniqueID + "    Deleted Successfully");

                //Reloading table
                observableList.removeIf(genreModel ->  genreModel.getGenre_UniqueID().equals(uniqueID));
                genreTable.setItems(observableList);

            }, "  CLICK HERE to confirm the Deletion\n\n   WARNING: Deleting this Genre will also Delete\n   the records associated with it");
        } else {
            Tools.notification("Graphics/SYSTEM ALERT STOP ICON.png", "WARNING!!!", "Please, Select an Item to delete");
        }
    }
}
