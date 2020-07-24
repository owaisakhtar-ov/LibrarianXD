package ReturnBook;

import Database.DatabaseManager;
import MainUI.Tools;
import MiniBookView.MiniBookViewController;
import MiniIssueView.MiniIssueViewController;
import MiniStaffView.MiniStaffViewController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReturnBookController implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private JFXDatePicker receivedDate;

    @FXML
    private JFXTextField receivedBy;

    @FXML
    private JFXTextField issueIDRef;

    @FXML
    private JFXButton minButton;

    @FXML
    private JFXButton receive;

    @FXML
    private JFXButton cancel;
    private int staffID;
    private int issueID;
    private int studentID;
    private int bookID;
    private DatabaseManager manager = DatabaseManager.getInstance();
    private Connection connection = manager.getConnection();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        receivedDate.setValue(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        //Staff Selection stage
        receivedBy.setOnMouseClicked(event -> {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("/MiniStaffView/MiniStaffView.fxml"));
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
                staffID = MiniStaffViewController.getStaffPrimaryID();
                receivedBy.setText("" + MiniStaffViewController.getStaffName());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //Issue Selection Stage
        issueIDRef.setOnMouseClicked(event -> {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("/MiniIssueView/MiniIssueView.fxml"));
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
                issueID = MiniIssueViewController.getIssuePrimaryID();
                studentID = MiniIssueViewController.getStdPrimaryID();
                bookID = MiniIssueViewController.getBookPrimaryID();
                if (issueID != 0) {
                    issueIDRef.setText("IS" + issueID);
                } else {
                    issueIDRef.setText("null");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void receiveBook(ActionEvent actionEvent) {
        //CHECK FOR ALL VALUES
        try {

            if (!issueIDRef.getText().equals("null") && !receivedBy.getText().equals("null")) {

                System.out.println("Accepted");
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO tbl_Return VALUES(?,?,?)");
                preparedStatement.setString(1,receivedDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                preparedStatement.setInt(2,staffID);
                preparedStatement.setInt(3,issueID);
                preparedStatement.executeUpdate();

                CallableStatement decreaseBookProc = connection.prepareCall("{call usp_Increase_NoOfBooks(?)}");
                decreaseBookProc.setInt(1,bookID);
                decreaseBookProc.execute();

                CallableStatement increaseBookIssue = connection.prepareCall("{call usp_Decrease_NoOfBooksIssued(?)}");
                increaseBookIssue.setInt(1,studentID);
                increaseBookIssue.execute();

                Tools.notification("Graphics/edit_add.png", "SUCCESS!!!", "Book Received Successfully");

            } else {
                Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Please provide all the Information");
            }
        } catch (NullPointerException ex) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Invalid Date Provided");
        } catch (SQLException e) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
        }

    }

    public void minimize(ActionEvent actionEvent) {
        Tools.minimize(receivedBy);
    }

    public void cancel(ActionEvent actionEvent) {
        Tools.close(receivedBy);
    }
}
