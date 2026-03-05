package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/painel.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root, 1150, 750);

        stage.getIcons().add(
                new Image(getClass().getResourceAsStream("/icons/icone.png"))
        );

        stage.setTitle("BSS");
        stage.setScene(scene);

        // 🔥 começa invisível
        stage.setOpacity(0);
        stage.show();

        // 🔥 depois mostra quando renderizar
        Platform.runLater(() -> stage.setOpacity(1));
    }

    public static void main(String[] args) {
        launch();
    }
}