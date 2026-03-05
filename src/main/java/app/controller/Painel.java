package app.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Painel implements Initializable {

    @FXML
    private Label mensagem;

    @FXML
    private Button iniciar;

    @FXML
    private ImageView logo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // 🔥 Carregar logo
        logo.setImage(
                new Image(getClass().getResourceAsStream("/icons/logo.png"))
        );

        // Fade logo
        FadeTransition fadeLogo = new FadeTransition(Duration.seconds(1.5), logo);
        fadeLogo.setFromValue(0);
        fadeLogo.setToValue(1);
        fadeLogo.play();

        // Fade texto
        FadeTransition fadeTexto = new FadeTransition(Duration.seconds(1.5), mensagem);
        fadeTexto.setFromValue(0);
        fadeTexto.setToValue(1);
        fadeTexto.setDelay(Duration.seconds(0.5));
        fadeTexto.play();

        // Fade botão
        FadeTransition fadeBotao = new FadeTransition(Duration.seconds(1.5), iniciar);
        fadeBotao.setFromValue(0);
        fadeBotao.setToValue(1);
        fadeBotao.setDelay(Duration.seconds(1));
        fadeBotao.play();
    }

    @FXML
    private void abrirLogin() {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/login.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) iniciar.getScene().getWindow();
            stage.setScene(new Scene(root, 1150, 750));


            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}