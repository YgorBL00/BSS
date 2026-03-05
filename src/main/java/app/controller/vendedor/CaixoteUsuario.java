package app.controller.vendedor;

import app.model.Usuario;
import app.service.FormatoCalculator;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaixoteUsuario {

    @FXML private TextField txtComprimento;
    @FXML private TextField txtLargura;
    @FXML private TextField txtAltura;
    @FXML private ComboBox<Integer> cbEspessura;
    @FXML private CheckBox chkPiso;
    @FXML private ComboBox<String> cbTipoPorta;
    @FXML private Spinner<Integer> spQtdPortas;
    @FXML private TextField txtTamanhoPorta;
    @FXML private Spinner<Integer> spCantoSemAcabamento;
    @FXML private Button btnAvancar;

    private Stage stage;
    private Usuario usuario;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    public void initialize() {

        cbEspessura.getItems().addAll(50, 70, 100, 120, 150);
        cbTipoPorta.getItems().addAll("Giratória", "Correr", "Pivotante");

        spQtdPortas.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));

        spCantoSemAcabamento.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, 1));

        spQtdPortas.setDisable(true);

        cbTipoPorta.valueProperty().addListener((obs, oldValue, newValue) ->
                spQtdPortas.setDisable(newValue == null)
        );
    }

    @FXML
    private void avancar() {

        try {

            if (txtComprimento.getText().isEmpty()
                    || txtLargura.getText().isEmpty()
                    || txtAltura.getText().isEmpty()
                    || cbEspessura.getValue() == null) {

                throw new Exception("Campos obrigatórios não preenchidos");
            }

            double C = Double.parseDouble(txtComprimento.getText().replace(",", "."));
            double L = Double.parseDouble(txtLargura.getText().replace(",", "."));
            double A = Double.parseDouble(txtAltura.getText().replace(",", "."));
            double E = cbEspessura.getValue() / 1000.0;

            boolean possuiPiso = chkPiso.isSelected();

            var resultados = FormatoCalculator.calcular(C, L, A, E, possuiPiso);
            int espessura = cbEspessura.getValue();

            System.out.println("\n===== LISTA DE MATERIAIS =====");

// PAREDE
            System.out.println(
                    resultados.paineisParede +
                            " paineis " + espessura + "mm 1,15x" +
                            String.format("%.2f", resultados.alturaParedeReal) +
                            " - parede"
            );

            imprimirRecortes(resultados.recortesParede, espessura, "parede");

// TETO
            System.out.println(
                    resultados.paineisTeto +
                            " paineis " + espessura + "mm 1,15x" +
                            String.format("%.2f", resultados.alturaTetoReal) +
                            " - teto"
            );

            imprimirRecortes(resultados.recortesTeto, espessura, "teto");

// PISO
            if (resultados.requerPiso) {

                System.out.println(
                        resultados.paineisPiso +
                                " paineis " + espessura + "mm 1,15x" +
                                String.format("%.2f", resultados.alturaPisoReal) +
                                " - piso"
                );

                imprimirRecortes(resultados.recortesPiso, espessura, "piso");
            }

            System.out.println("===============================");


        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Erro nos dados");
            alert.setContentText("Verifique os valores informados.");
            alert.showAndWait();
        }
    }

    private void imprimirRecortes(List<FormatoCalculator.Recorte> recortes, int espessura, String tipo) {

        Map<String, Integer> agrupado = new HashMap<>();

        for (FormatoCalculator.Recorte r : recortes) {

            String chave =
                    String.format("%.2f", r.largura) + "x" +
                            String.format("%.2f", r.altura);

            agrupado.put(chave, agrupado.getOrDefault(chave, 0) + 1);
        }

        for (Map.Entry<String, Integer> item : agrupado.entrySet()) {

            System.out.println(
                    item.getValue() +
                            " paineis " + espessura + "mm " +
                            item.getKey() +
                            " - recorte " + tipo
            );
        }
    }
}
