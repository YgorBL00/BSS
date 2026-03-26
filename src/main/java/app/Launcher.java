package app;

import app.service.VersaoService;
//import app.update.Atualizador;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
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

        // Tela inicial simples
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/app/update.fxml")
        );

        Parent root = loader.load();
        Scene scene = new Scene(root, 400, 200);

        stage.setScene(scene);
        stage.show();

        // Task em background
        Task<Void> verificarSistema = new Task<>() {
            @Override
            protected Void call() {

                try {

                    // consulta Supabase
                    VersaoService service = new VersaoService();
                    String configUrl = service.buscarConfigUpdate();

                    if (configUrl != null) {

                        // roda Update4J
//                        Atualizador.verificar(configUrl);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        };

        verificarSistema.setOnSucceeded(e -> {

            Platform.runLater(() -> abrirSistema(stage));

        });

        new Thread(verificarSistema).start();
    }

    private void abrirSistema(Stage stage) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/painel.fxml")
            );

            Parent root = loader.load();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.setWidth(1150);
            stage.setHeight(750);
            stage.centerOnScreen(); // centraliza

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch();
    }
}