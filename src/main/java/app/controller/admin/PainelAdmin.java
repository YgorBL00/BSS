package app.controller.admin;

import app.model.Usuario;
import javafx.animation.FadeTransition;
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

import java.net.URL;
import java.util.ResourceBundle;

public class PainelAdmin implements Initializable {

    @FXML
    private Label usuarioLabel;

    @FXML
    private VBox conteudo;

    @FXML
    private TilePane cardsContainer;

    private Usuario usuario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        criarCard("Lista de Materiais");

        // animação fade
        conteudo.setOpacity(0);

        FadeTransition fade = new FadeTransition(Duration.millis(700), conteudo);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        usuarioLabel.setText("ADMIN: " + usuario.getNome());
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

        // efeito hover
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

        // clique
        card.setOnMouseClicked(e -> {

            if (titulo.equals("Lista de Materiais")) {
                abrirTela("/app/admin/material.fxml");
            }

        });
        cardsContainer.getChildren().add(card);
    }

    private void abrirTela(String fxml) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = (Stage) conteudo.getScene().getWindow();
            stage.setScene(new Scene(root, 1150, 750));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sair() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) conteudo.getScene().getWindow();
            stage.setScene(new Scene(root, 1150, 750));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}