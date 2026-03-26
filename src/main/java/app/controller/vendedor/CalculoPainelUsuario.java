package app.controller.vendedor;

import app.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CalculoPainelUsuario {

    private Usuario usuario;

    @FXML
    private Button btnVoltar;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    private void voltarPainel() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/usuario/painel-usuario.fxml")
            );

            Parent tela = loader.load();

            PainelUsuario controller = loader.getController();
            controller.setUsuario(usuario);

            Stage stage = (Stage) btnVoltar.getScene().getWindow();
            stage.setScene(new Scene(tela, 1150, 750));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}