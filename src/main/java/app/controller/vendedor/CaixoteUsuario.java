package app.controller.vendedor;

import app.model.Usuario;
import app.service.FormatoCalculator;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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
    @FXML private ComboBox<String> spCantoSemAcabamento;
    @FXML private Button btnAvancar;
    @FXML private VBox boxCantos;

    @FXML private CheckBox chkFrente;
    @FXML private CheckBox chkAtras;
    @FXML private CheckBox chkLadoEsquerdo;
    @FXML private CheckBox chkLadoDireito;

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
        cbTipoPorta.getItems().addAll("Giratória", "Correr");

        spQtdPortas.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));

        spCantoSemAcabamento.getItems().addAll("Não", "Sim");

        spCantoSemAcabamento.setValue("Não");

        spCantoSemAcabamento.valueProperty().addListener((obs, oldValue, newValue) -> {

            boolean mostrar = "Sim".equals(newValue);

            boxCantos.setVisible(mostrar);
            boxCantos.setManaged(mostrar);

        });

        spQtdPortas.setDisable(true);

        cbTipoPorta.valueProperty().addListener((obs, oldValue, newValue) ->
                spQtdPortas.setDisable(newValue == null)
        );
    }

    @FXML
    private void avancar() {

        String tipoPorta = cbTipoPorta.getValue();
        int qtdPortas = spQtdPortas.getValue();

        String tamanhoPorta = txtTamanhoPorta.getText();

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

            int cantoneiraFinal = resultados.cantoneiraExterna;

            // largura
            if (chkLadoEsquerdo.isSelected()) {
                cantoneiraFinal -= resultados.cantoneiraExterna / 4;
            }

            if (chkLadoDireito.isSelected()) {
                cantoneiraFinal -= resultados.cantoneiraExterna / 4;
            }

            // comprimento
            if (chkFrente.isSelected()) {
                cantoneiraFinal -= resultados.cantoneiraExterna / 4;
            }

            if (chkAtras.isSelected()) {
                cantoneiraFinal -= resultados.cantoneiraExterna / 4;
            }

            int espessura = cbEspessura.getValue();

            // =============================
            // PERFIL E CANTONEIRA POR ESPESSURA
            // =============================

            String perfilUDescricao = "40x" + espessura + "x40x3000";

            String cantoneiraExternaDescricao;

            switch (espessura) {

                case 50:
                    cantoneiraExternaDescricao = "40x90x3000";
                    break;

                case 70:
                    cantoneiraExternaDescricao = "40x120x3000";
                    break;

                case 100:
                    cantoneiraExternaDescricao = "40x140x3000";
                    break;

                case 120:
                    cantoneiraExternaDescricao = "40x160x3000";
                    break;

                case 150:
                    cantoneiraExternaDescricao = "40x190x3000";
                    break;

                default:
                    cantoneiraExternaDescricao = "modelo não definido";
            }

            System.out.println("\n===== LISTA DE MATERIAIS =====");

            // =============================
            // PAREDE
            // =============================

            System.out.println(
                    resultados.paineisParede +
                            " Paineis " + espessura + "mm 1,15x" +
                            String.format("%.2f", resultados.alturaParedeReal) +
                            " - PAREDE"
            );

            imprimirRecortes(resultados.recortesParede, espessura, "parede");

            // =============================
            // TETO
            // =============================

            System.out.println(
                    resultados.paineisTeto +
                            " Paineis " + espessura + "mm 1,15x" +
                            String.format("%.2f", resultados.alturaTetoReal) +
                            " - TETO"
            );

            imprimirRecortes(resultados.recortesTeto, espessura, "teto");

            // =============================
            // PISO
            // =============================

            if (resultados.requerPiso) {

                System.out.println(
                        resultados.paineisPiso +
                                " Paineis " + espessura + "mm 1,15x" +
                                String.format("%.2f", resultados.alturaPisoReal) +
                                " - PISO"
                );

                imprimirRecortes(resultados.recortesPiso, espessura, "piso");
            }
            // =============================
            // PORTA
            // =============================

            if (tipoPorta != null && qtdPortas > 0) {

                System.out.println(
                        qtdPortas +
                                " Porta frigorífica " +
                                tipoPorta +
                                " " +
                                tamanhoPorta
                );
            }

            // =============================
            // PU
            // =============================

            System.out.println(
                    resultados.sachePU +
                            " Sachês PU 40 (600ml) - vedação das juntas (" +
                            String.format("%.2f", resultados.metrosJuntaPU) +
                            " m de junta)"
            );

            // =============================
            // ACABAMENTOS
            // =============================

            System.out.println(
                    resultados.cantoneiraInterna +
                            " Cantoneiras internas 3m"
            );

            System.out.println(
                    cantoneiraFinal +
                            " Cantoneiras externas " +
                            cantoneiraExternaDescricao
            );

            System.out.println(
                    resultados.perfilU +
                            " Perfil U " +
                            perfilUDescricao
            );

            // =============================
            // FIXAÇÃO
            // =============================

            System.out.println(resultados.rebites + " Rebite 3.2x12");

            System.out.println(
                    resultados.parafusos +
                            " Parafusos n°8 c/ bucha e arruela 3/8"
            );

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
