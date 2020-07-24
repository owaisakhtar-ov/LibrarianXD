package MainUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;



public abstract class Tools {

    public Tools() {
    }

    public static void close(Parent instance) {
        Stage stage = (Stage) instance.getScene().getWindow();
        stage.close();
    }

    public static void minimize(Parent instance) {
        Stage stage = (Stage) instance.getScene().getWindow();
        stage.setIconified(true);
    }

    public static void notification(String iconPath, String title, String text) {
        ImageView imageRed = new ImageView(new Image(iconPath));
        imageRed.setFitHeight(55);
        imageRed.setFitWidth(55);
        Notifications.create()
                .title("\t\t" + title)
                .text(text)
                .graphic(imageRed)
                .darkStyle()
                .hideAfter(Duration.seconds(3))
                .show();
    }

    public static void confirmation(EventHandler eventHandler,String message) {
        ImageView imageRed = new ImageView(new Image("Graphics/SYSTEM ALERT STOP ICON.png"));
        imageRed.setFitHeight(55);
        imageRed.setFitWidth(55);
        Notifications.create()
                .title("\t\t Confirmation")
                .text(message)
                .graphic(imageRed)
                .darkStyle()
                .onAction((EventHandler<ActionEvent>) eventHandler)
                .hideAfter(Duration.seconds(10))
                .show();
    }

    public static int pkGetter(String UniqueID) {
        return Integer.parseInt(UniqueID.substring(2));
    }

}
