package IssueBook;

import Database.DatabaseManager;
import MainUI.Tools;
import MiniBookView.MiniBookViewController;
import MiniStaffView.MiniStaffViewController;
import MiniStudentView.MiniStudentViewController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

public class IssueBookController implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private JFXButton issue;

    @FXML
    private JFXButton cancel;

    @FXML
    private JFXButton minButton;

    @FXML
    private Label heading;

    @FXML
    private JFXTextField book;

    @FXML
    private JFXDatePicker issueDate;

    @FXML
    private JFXDatePicker issueTill;

    @FXML
    private JFXTextField student;

    @FXML
    private JFXTextField staff;

    private int bookID;
    private int staffID;
    private int studentID;
    DatabaseManager manager = DatabaseManager.getInstance();
    Connection connection = manager.getConnection();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        issueDate.setValue(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        //Book Selection stage
        book.setOnMouseClicked(event -> {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("/MiniBookView/MiniBookView.fxml"));
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
                bookID = MiniBookViewController.getBookPrimaryID();
                book.setText("" + MiniBookViewController.getBookName());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //Student selection stage
        student.setOnMouseClicked(event -> {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("/MiniStudentView/MiniStudentView.fxml"));
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
                studentID = MiniStudentViewController.getStdPrimaryID();
                student.setText("" + MiniStudentViewController.getStdName());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //staff selection stage
        staff.setOnMouseClicked(event -> {
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
                staff.setText("" + MiniStaffViewController.getStaffName());

            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    public void issueBook(ActionEvent actionEvent) {

        //CHECK FOR ALL VALUES
            try {
                if (!issueDate.getValue().toString().isEmpty() && !issueTill.getValue().toString().isEmpty() && !book.getText().equals("null") && !book.getText().isEmpty() && !student.getText().equals("null") && !student.getText().isEmpty() && !staff.getText().equals("null") && !staff.getText().isEmpty()) {

                System.out.println("Accepted");
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO tbl_Issuance VALUES(?,?,?,?,?)");
                preparedStatement.setString(1,issueDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                preparedStatement.setString(2,issueTill.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                preparedStatement.setInt(3,bookID);
                preparedStatement.setInt(4,studentID);
                preparedStatement.setInt(5,staffID);
                preparedStatement.executeUpdate();

                CallableStatement decreaseBookProc = connection.prepareCall("{call usp_Decrease_NoOfBooks(?)}");
                decreaseBookProc.setInt(1,bookID);
                decreaseBookProc.execute();

                CallableStatement increaseBookIssue = connection.prepareCall("{call usp_Increase_NoOfBooksIssued(?)}");
                increaseBookIssue.setInt(1,studentID);
                increaseBookIssue.execute();

                Tools.notification("Graphics/edit_add.png", "SUCCESS!!!", "Book Issued Successfully");

            } else {
                Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Please provide all the Information");
            }
        } catch (NullPointerException ex) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Invalid Date Provided");
        } catch (SQLException e) {
            Tools.notification("/Graphics/button_cancel.png", "ERROR!!!", "Database Error");
        }

    }

    public void cancel(ActionEvent actionEvent) {
        Tools.close(book);
    }

    public void minimize(ActionEvent actionEvent) {
        Tools.minimize(book);
    }
}
