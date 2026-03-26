package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        stage.getIcons().add(
                new Image(getClass().getResourceAsStream("/icons/icone.png"))
        );

        stage.setTitle("BSS");

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/painel.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root, 1150, 750);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}