package app.controller.vendedor;

import app.model.Porta;
import app.model.Usuario;
import app.service.FormatoCalculator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.util.List;

public class RefrigeracaoUsuario {

    @FXML public Button btnOrcamento;
    @FXML private Label lblCliente;
    @FXML private Label lblTipo;
    @FXML private Label lblDimensoes;
    @FXML private Label lblEspessura;
    @FXML private Label lblAreaPorta;

    private Usuario usuario;
    private FormatoCalculator.ResultadoFormato resultados;
    private int espessura;
    private List<Porta> portas;

    public void setEspessura(int espessura) {this.espessura = espessura;if(lblEspessura != null){lblEspessura.setText(espessura + " mm");}}
    public void setUsuario(Usuario usuario) {this.usuario = usuario;}
    public void setResultados(FormatoCalculator.ResultadoFormato resultados) {this.resultados = resultados;}
    public void setCliente(String cliente) {lblCliente.setText(cliente);}
    public void setTipoCamara(String tipo) {lblTipo.setText(tipo);}
    public void setDimensoes(String dim) {lblDimensoes.setText(dim);}

    @FXML
    public void abrirResultado() {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/usuario/resultado.fxml")
            );


            Parent root = loader.load();

            ResultadoUsuario controller = loader.getController();
            controller.carregarDados(
                    usuario,
                    resultados,
                    espessura,
                    "", "", "", portas
            );

            Stage stage = (Stage) btnOrcamento.getScene().getWindow();
            stage.setScene(new Scene(root, 1150, 750));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void voltarCaixote() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/usuario/caixote.fxml")
            );

            Parent root = loader.load();

            CaixoteUsuario controller = loader.getController();
            controller.setUsuario(usuario);

            Stage stage = (Stage) btnOrcamento.getScene().getWindow();
            stage.setScene(new Scene(root, 1150, 750));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPortas(List<Porta> portas) {

        this.portas = portas;

        double area = 0;

        if(portas == null || portas.isEmpty()){
            area = 1.44;
        }else{

            for(Porta p : portas){

                String tamanho = p.getTamanho()
                        .replace(",", ".")
                        .toLowerCase();

                String[] partes = tamanho.split("x");

                double largura = Double.parseDouble(partes[0]);
                double altura = Double.parseDouble(partes[1]);

                area += largura * altura;
            }
        }

        if(lblAreaPorta != null){
            lblAreaPorta.setText(String.format("%.2f m²", area));
        }
    }
}