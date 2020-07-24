package MainUI;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SideDrawerController implements Initializable {

    @FXML
    public JFXButton students;
    @FXML
    private JFXButton drawerButton;

    @FXML
    private VBox sideContainer;

    @FXML
    private JFXButton checkButton;

    @FXML
    private ImageView logoImage;

    @FXML
    private FontAwesomeIconView arrowhead;
    private RotateTransition rotateTransition = new RotateTransition();
    private void rotateArrow() {
        rotateTransition.setNode(arrowhead);
        rotateTransition.setByAngle(180);
        rotateTransition.setDuration(Duration.millis(300));
        rotateTransition.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logoImage.setImage(new Image("/Graphics/Untitled-2.png"));

    }

    public void openDrawer(ActionEvent actionEvent) {
        rotateArrow();
    }

    public void bookTab(ActionEvent actionEvent) {
        rotateArrow();
    }

    public void studentsTab(ActionEvent actionEvent) {
        rotateArrow();
    }

    public void staffTab(ActionEvent actionEvent) {
        rotateArrow();
    }

    public void issueTab(ActionEvent actionEvent) {
        rotateArrow();
    }

    public void returnsTab(ActionEvent actionEvent) {
        rotateArrow();
    }

    public void aboutTab(ActionEvent actionEvent) {
        rotateArrow();
    }

    public void homeTab(ActionEvent actionEvent) {
        rotateArrow();
    }
}
