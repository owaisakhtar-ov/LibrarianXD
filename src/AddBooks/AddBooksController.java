package AddBooks;

import Database.DatabaseManager;
import Genre.ViewGenreController;
import MainUI.Tools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AddBooksController implements Initializable {

    public ImageView frame;
    public Label heading;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField writer;

    @FXML
    private JFXTextField edition;

    @FXML
    private JFXTextField quantity;

    @FXML
    private JFXTextField language;

    @FXML
    private JFXTextField genre;

    @FXML
    private JFXButton insert;

    @FXML
    private JFXButton cancel;

    @FXML
    private JFXButton minButton;

    private int genreID;
    //    private ObservableList<BooksModel> observableList = FXCollections.observableArrayList();
    DatabaseManager manager = DatabaseManager.getInstance();

    @Override

    public void initialize(URL location, ResourceBundle resources) {
        genre.setOnMouseClicked(event -> {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("/Genre/ViewGenre.fxml"));
                Stage stage = new Stage(StageStyle.UNDECORATED);
                parent.setOnMousePressed(event2 -> {
                    xOffset = event2.getSceneX();
                    yOffset = event2.getSceneY();
                });
                parent.setOnMouseDragged(event2 -> {
                    stage.setX(event2.getScreenX() - xOffset);
                    stage.setY(event2.getScreenY() - yOffset);
                });
                Scene scene = new Scene(parent);
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.getIcons().add(new Image("/Graphics/main.png"));
                stage.showAndWait();
                genreID = ViewGenreController.getGenrePrimaryID();
                genre.setText("" + ViewGenreController.getGenreReferenceName());


            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void cancel(ActionEvent actionEvent) {
        Tools.close(name);
    }

    public void insert(ActionEvent actionEvent) {

        if (!name.getText().isEmpty() && !writer.getText().isEmpty() && !edition.getText().isEmpty() && !quantity.getText().isEmpty() && !language.getText().isEmpty() && !genre.getText().equals("null") &&!genre.getText().isEmpty()) {
            //Insertion of DATA in DATABASE
            try {
                PreparedStatement insertQuery = manager.getConnection().prepareStatement("INSERT INTO tbl_Book VALUES (?,?,?,?,?,?)");
                insertQuery.setString(1, name.getText());
                insertQuery.setString(2, writer.getText());
                insertQuery.setInt(3, Integer.parseInt(edition.getText()));
                insertQuery.setInt(4, Integer.parseInt(quantity.getText()));
                insertQuery.setString(5, language.getText());
                insertQuery.setInt(6, this.genreID);
                insertQuery.executeUpdate();
                Tools.notification("/Graphics/edit_add.png", "SUCCESS!!!", "Book Added Successfully");


            } catch (NumberFormatException e) {
                Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Wrong NUMERIC values");

            } catch (SQLException e) {
                Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
            }

        } else {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Please provide all the Information");
        }

    }

    public void minimize(ActionEvent actionEvent) {
        Tools.minimize(name);
    }

}
