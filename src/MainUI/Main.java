package MainUI;

import Database.DatabaseManager;
import com.jfoenix.controls.JFXRippler;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Thread(DatabaseManager::getInstance).start();


        primaryStage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("/MainUI/MainUI.fxml"));



//        Parent root = FXMLLoader.load(getClass().getResource("/Genre/ViewGenre.fxml"));
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
        primaryStage.setScene(new Scene(root));
        primaryStage.centerOnScreen();
        primaryStage.getIcons().add(new Image("/Graphics/main.png"));
        primaryStage.show();



    }

    public static void main(String[] args) {
        launch(args);
    }
}
