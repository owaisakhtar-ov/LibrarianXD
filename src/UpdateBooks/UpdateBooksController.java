package UpdateBooks;

import Database.DatabaseManager;
import Genre.ViewGenreController;
import MainUI.Tools;
import ViewBooks.ViewBookModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateBooksController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private ImageView frame;

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
    private JFXButton update;

    @FXML
    private JFXButton cancel;

    @FXML
    private JFXButton minButton;
    public int genreID;
    public int bookID;

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

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

    public void update(ActionEvent actionEvent) {

        System.out.println(genreID = manager.getGenrePKByName(genre.getText()));
        if (!name.getText().isEmpty() && !writer.getText().isEmpty() && !edition.getText().isEmpty() && !quantity.getText().isEmpty() && !language.getText().isEmpty() && !genre.getText().equals("null")) {

            try {
                PreparedStatement preparedStatement = manager.getConnection().prepareStatement("UPDATE tbl_Book SET Book_Name =?,Book_Writer = ?,Book_Language = ?, Book_Edition=?,Book_Qualtity=?,GenreID =? WHERE Book_ID = " + bookID);
                preparedStatement.setString(1, name.getText());
                preparedStatement.setString(2, writer.getText());
                preparedStatement.setString(3, language.getText());
                preparedStatement.setInt(4, Integer.parseInt(edition.getText()));
                preparedStatement.setInt(5, Integer.parseInt(quantity.getText()));
                preparedStatement.setInt(6, genreID);
                preparedStatement.executeUpdate();
                Tools.notification("/Graphics/Actions-view-refresh-icon.png", "SUCCESS!!!", "Book Updated Successfully");


            } catch (NumberFormatException e1) {
                Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Wrong NUMERIC values");

            } catch (SQLException e) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
                e.printStackTrace();
            }
        } else {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Please provide all the Information");
        }
    }

    public void cancel(ActionEvent actionEvent) {
        Tools.close(name);
    }

    public void minimize(ActionEvent actionEvent) {
        Tools.minimize(name);
    }

    public void initUpdateScene(ViewBookModel viewBookModel) {
        name.setText(viewBookModel.getBookName());
        writer.setText(viewBookModel.getBookWriter());
        edition.setText(viewBookModel.getBookEdition() + "");
        quantity.setText(viewBookModel.getBookQuantity() + "");
        language.setText(viewBookModel.getBookLanguage());
        genre.setText(viewBookModel.getBookGenre());
    }

}
