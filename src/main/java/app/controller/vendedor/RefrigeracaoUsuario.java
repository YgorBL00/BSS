package app.controller.vendedor;

import app.model.Usuario;
import app.service.FormatoCalculator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class RefrigeracaoUsuario {

    @FXML
    public Button btnOrcamento;

    private Usuario usuario;
    private FormatoCalculator.ResultadoFormato resultados;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setResultados(FormatoCalculator.ResultadoFormato resultados) {
        this.resultados = resultados;
    }

    @FXML
    public void abrirResultado() {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/usuario/resultado.fxml")
            );

            Parent root = loader.load();

            ResultadoUsuario controller = loader.getController();
            controller.setUsuario(usuario);
            controller.setResultados(resultados);

            Stage stage = (Stage) btnOrcamento.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void voltarCaixote() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/usuario/caixote.fxml")
            );

            Parent root = loader.load();

            ResultadoUsuario controller = loader.getController();
            controller.setUsuario(usuario);
            controller.setResultados(resultados);

            Stage stage = (Stage) btnOrcamento.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}