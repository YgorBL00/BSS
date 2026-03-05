package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/painel.fxml")
        );

        Scene scene = new Scene(loader.load(), 1150, 750);

        // Ícone
        stage.getIcons().add(
                new Image(getClass().getResourceAsStream("/icons/icone.png"))
        );

        stage.setTitle("BSS");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}