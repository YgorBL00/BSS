package app.controller.vendedor;

import app.model.Usuario;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Platform;

import java.net.URL;
import java.util.ResourceBundle;

public class PainelUsuario implements Initializable {

    @FXML
    private Label usuarioLabel;

    @FXML
    private VBox conteudo;

    @FXML
    private TilePane cardsContainer;

    private Usuario usuario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        criarCard("Projeto");
        criarCard("Projeto Salvos");
        criarCard("Calculo De Paineis");

        // começa invisível
        conteudo.setOpacity(0);

        FadeTransition fade = new FadeTransition(Duration.seconds(0.7), conteudo);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }


    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        usuarioLabel.setText("VENDEDOR: " + usuario.getNome());
    }


    private void criarCard(String titulo) {

        VBox card = new VBox();
        card.setPrefSize(260, 140);
        card.setAlignment(Pos.CENTER);
        card.setCursor(Cursor.HAND);

        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-border-color: #d0d7e2;
            """);

        Label label = new Label(titulo);
        label.setStyle("-fx-font-size: 15; -fx-text-fill: #23336f;");

        card.getChildren().add(label);

        card.setOnMouseEntered(e ->
                card.setStyle("""
                    -fx-background-color: #f5f8ff;
                    -fx-background-radius: 12;
                    -fx-border-radius: 12;
                    -fx-border-color: #245edb;
                    """)
        );

        card.setOnMouseExited(e ->
                card.setStyle("""
                    -fx-background-color: white;
                    -fx-background-radius: 12;
                    -fx-border-radius: 12;
                    -fx-border-color: #d0d7e2;
                    """)
        );

        card.setOnMouseClicked(e -> {

            switch (titulo) {

                case "Projeto":
                    abrirTela("/app/usuario/caixote.fxml");
                    break;

                case "Projeto Salvos":
                    abrirTela("/app/usuario/projetos-salvos.fxml");
                    break;

                case "Calculo De Paineis":
                    abrirTela("/app/usuario/calculo-painel.fxml");
                    break;
            }

        });

        cardsContainer.getChildren().add(card);
    }

    private void abrirTela(String fxml) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Object controller = loader.getController();

            if (controller instanceof CaixoteUsuario) {
                ((CaixoteUsuario) controller).setUsuario(usuario);
            }

            if (controller instanceof CalculoPainelUsuario) {
                ((CalculoPainelUsuario) controller).setUsuario(usuario);
            }

            Stage stage = (Stage) conteudo.getScene().getWindow();
            stage.setScene(new Scene(root, 1150, 750));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void sairSistema(ActionEvent actionEvent) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/login.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) conteudo.getScene().getWindow();

            stage.setScene(new Scene(root, 1150, 750));
            stage.setTitle("Sistema BSS - Login");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}