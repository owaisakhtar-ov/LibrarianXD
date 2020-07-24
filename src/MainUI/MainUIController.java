package MainUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainUIController implements Initializable {

    @FXML
    private JFXDrawer drawer;

    @FXML
    private AnchorPane sceneContainer;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXButton minButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Basic Initialization:
        Platform.runLater(() -> sceneContainer.requestFocus());

        //Drawer Setting
        AnchorPane sidePane = null;
        try {
            sidePane = FXMLLoader.load(getClass().getResource("/MainUI/sideDrawer.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        drawer.setSidePane(sidePane);

        sidePane.lookup("#drawerButton").setOnMouseClicked(event -> {
            if (drawer.isOpened()) {
                drawer.close();
            } else {
                drawer.toFront();
                drawer.open();
            }
        });

        drawer.setOnDrawerClosed(event -> drawer.toBack());

        //Check Point For Mouse Events
//        drawer.setOnMouseEntered(event -> System.out.println("Enter"));
//        drawer.setOnMouseExited(event -> System.out.println("Exit"));
        //Drawer Settings END

        //***********Default Window*********
        try {
            AnchorPane viewScene = FXMLLoader.load(getClass().getResource("/MainUI/StartupWindow.fxml"));
            sceneContainer.getChildren().setAll(viewScene);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Drawer buttons Events
        //************VIEW BOOKS************
        sidePane.lookup("#books").setOnMouseClicked(event -> {
            try {
                AnchorPane viewScene = FXMLLoader.load(getClass().getResource("/ViewBooks/ViewBooks.fxml"));
                sceneContainer.getChildren().setAll(viewScene);
                drawer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //***********VIEW STUDENTS**********
        sidePane.lookup("#students").setOnMouseClicked(event -> {

            try {
                AnchorPane viewScene = FXMLLoader.load(getClass().getResource("/viewStudents/ViewStudents.fxml"));
                sceneContainer.getChildren().setAll(viewScene);
                drawer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //**********VIEW STAFF*************
        sidePane.lookup("#staff").setOnMouseClicked(event -> {
            try {
                AnchorPane viewScene = FXMLLoader.load(getClass().getResource("/ViewStaff/ViewStaff.fxml"));
                sceneContainer.getChildren().setAll(viewScene);
                drawer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //************ISSUANCE***************
        sidePane.lookup("#issue").setOnMouseClicked(event -> {
            try {
                AnchorPane viewScene = FXMLLoader.load(getClass().getResource("/Issuance/Issuance.fxml"));
                sceneContainer.getChildren().setAll(viewScene);
                drawer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //************RETURN*****************
        sidePane.lookup("#returns").setOnMouseClicked(event -> {
            try {
                AnchorPane viewScene = FXMLLoader.load(getClass().getResource("/Returns/Returns.fxml"));
                sceneContainer.getChildren().setAll(viewScene);
                drawer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //************ABOUT******************
        sidePane.lookup("#about").setOnMouseClicked(event -> {
            try {
                AnchorPane viewScene = FXMLLoader.load(getClass().getResource("/MainUI/StartupWindow.fxml"));
                ImageView imageView = (ImageView) viewScene.lookup("#image");
                imageView.setImage(new Image("/Graphics/about.png"));

                viewScene.getChildren().remove(1, 6);
                sceneContainer.getChildren().setAll(viewScene);
                drawer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //***********BACK TO STARTUP***********
        sidePane.lookup("#home").setOnMouseClicked(event -> {
            try {
                AnchorPane viewScene = FXMLLoader.load(getClass().getResource("/MainUI/StartupWindow.fxml"));
                sceneContainer.getChildren().setAll(viewScene);
                drawer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void close(ActionEvent actionEvent) {
        Tools.close(drawer);
    }

    public void minimize(ActionEvent actionEvent) {
        Tools.minimize(drawer);

    }

}


