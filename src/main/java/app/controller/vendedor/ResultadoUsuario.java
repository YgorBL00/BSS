package app.controller.vendedor;

import app.model.Usuario;
import app.service.FormatoCalculator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ResultadoUsuario {

    public Button btnVoltar;
    private Usuario usuario;
    private FormatoCalculator.ResultadoFormato resultados;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setResultados(FormatoCalculator.ResultadoFormato resultados) {
        this.resultados = resultados;
    }

    @FXML
    public void voltarRefrigeracao() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/usuario/refrigeracao.fxml")
            );

            Parent root = loader.load();

            CaixoteUsuario controller = loader.getController();
            controller.setUsuario(usuario);

            Stage stage = (Stage) btnVoltar.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}