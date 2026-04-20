package app.controller.vendedor;

import app.model.Porta;
import app.model.Usuario;
import app.service.FormatoCalculator;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller da tela de configuração da câmara fria
 * Responsável por coletar os dados geométricos e enviar
 * para a tela de cálculo de refrigeração.
 */
public class CaixoteUsuario {

    // =========================================
    // CAMPOS FXML (componentes da interface)
    // =========================================

    @FXML private TextField txtCliente;

    @FXML private RadioButton rbCongelado;
    @FXML private RadioButton rbResfriado;

    @FXML private TextField txtComprimento;
    @FXML private TextField txtLargura;
    @FXML private TextField txtAltura;

    @FXML private ComboBox<Integer> cbEspessura;
    @FXML private ComboBox<String> spCantoSemAcabamento;

    @FXML private CheckBox chkPiso;

    @FXML private VBox boxCantos;

    @FXML private RadioButton rb220Mono;
    @FXML private RadioButton rb220Tri;
    @FXML private RadioButton rb380Tri;


    @FXML private CheckBox chkFrente;
    @FXML private CheckBox chkAtras;
    @FXML private CheckBox chkLadoEsquerdo;
    @FXML private CheckBox chkLadoDireito;

    @FXML private Button btnAvancar;
    @FXML private Button btnVoltar;

    // =========================================
    // VARIÁVEIS DO SISTEMA
    // =========================================

    private Usuario usuario;
    private List<Porta> portas = new ArrayList<>();

    private FormatoCalculator.ResultadoFormato resultados;

    private String tensao;
    private String tipoCamara;

    private String memorial;

    // =========================================
    // SETTERS (recebidos de outras telas)
    // =========================================

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setResultados(FormatoCalculator.ResultadoFormato resultados) {
        this.resultados = resultados;
    }

    public void setEspessura(int espessura) {
        if (cbEspessura != null) {
            cbEspessura.setValue(espessura);
        }
    }

    public void setCliente(String cliente) {
        if (txtCliente != null) {
            txtCliente.setText(cliente);
        }
    }

    public void setDimensoes(String dimensoes) {

        if (dimensoes == null) return;

        String[] partes = dimensoes.split("x");

        if (partes.length == 3) {
            txtComprimento.setText(partes[0].trim());
            txtLargura.setText(partes[1].trim());
            txtAltura.setText(partes[2].trim());
        }
    }

    public void setTipoCamara(String tipoCamara) {

        this.tipoCamara = tipoCamara;

        if ("CONGELADOS".equals(tipoCamara)) {
            rbCongelado.setSelected(true);
        } else {
            rbResfriado.setSelected(true);
        }
    }

    public void setTensao(String tensao) {

        this.tensao = tensao;

        if ("220V_MONO".equals(tensao)) {
            rb220Mono.setSelected(true);
        }

        if ("220V_TRI".equals(tensao)) {
            rb220Tri.setSelected(true);
        }

        if ("380V_TRI".equals(tensao)) {
            rb380Tri.setSelected(true);
        }
    }

    public void setPortas(List<Porta> portas) {
        this.portas = portas;
    }

    // =========================================
    // INITIALIZE (executado ao abrir tela)
    // =========================================

    @FXML
    public void initialize() {

        // Grupo de seleção do tipo de câmara
        ToggleGroup grupoCamara = new ToggleGroup();
        rbCongelado.setToggleGroup(grupoCamara);
        rbResfriado.setToggleGroup(grupoCamara);


        // Espessuras de painel disponíveis
        cbEspessura.getItems().addAll(50, 70, 100, 120, 150);

        // Configuração de canto sem acabamento
        spCantoSemAcabamento.getItems().addAll("Não", "Sim");
        spCantoSemAcabamento.setValue("Não");

        // Tipo padrão
        rbResfriado.setSelected(true);

        // Grupo de tensão elétrica
        ToggleGroup grupoTensao = new ToggleGroup();
        rb220Mono.setToggleGroup(grupoTensao);
        rb220Tri.setToggleGroup(grupoTensao);
        rb380Tri.setToggleGroup(grupoTensao); // NOVO

        // Mostrar ou esconder cantos
        spCantoSemAcabamento.valueProperty().addListener((obs, oldVal, newVal) -> {

            boolean mostrar = "Sim".equals(newVal);

            boxCantos.setVisible(mostrar);
            boxCantos.setManaged(mostrar);
        });
    }

    // =========================================
    // CONFIGURAÇÃO DE PORTAS
    // =========================================

    @FXML
    private void abrirConfigPortas() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/usuario/ConfigPortas.fxml")
            );

            VBox root = loader.load();

            ConfigPortasController controller = loader.getController();

            controller.setLista(FXCollections.observableArrayList(portas));

            Stage popup = new Stage();
            popup.setTitle("Configuração de Portas");
            popup.setScene(new Scene(root));
            popup.showAndWait();

            portas = new ArrayList<>(controller.getLista());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================
    // BOTÃO AVANÇAR
    // =========================================

    @FXML
    private void avancar() {

        try {

            validarCampos();

            if (portas.isEmpty()) {

                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("Aviso");
                alerta.setHeaderText("Nenhuma porta configurada");
                alerta.setContentText("A câmara não possui portas configuradas.\nDeseja continuar mesmo assim?");

                ButtonType continuar = new ButtonType("Continuar");
                ButtonType cancelar = new ButtonType("Cancelar");

                alerta.getButtonTypes().setAll(continuar, cancelar);

                if (alerta.showAndWait().orElse(cancelar) == cancelar) {
                    return;
                }
            }

            if (rbCongelado.isSelected() && cbEspessura.getValue() < 100) {

                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("Aviso Técnico");
                alerta.setHeaderText("Espessura de painel baixa para congelados");
                alerta.setContentText(
                        "Para câmaras de CONGELADOS o recomendado é PIR mínimo de 100mm.\n\n" +
                                "Espessura selecionada: " + cbEspessura.getValue() + " mm\n\n" +
                                "Deseja continuar mesmo assim?"
                );

                ButtonType continuar = new ButtonType("Continuar");
                ButtonType voltar = new ButtonType("Voltar");

                alerta.getButtonTypes().setAll(continuar, voltar);

                if (alerta.showAndWait().orElse(voltar) == voltar) {
                    return;
                }
            }

            double C = Double.parseDouble(txtComprimento.getText().replace(",", "."));
            double L = Double.parseDouble(txtLargura.getText().replace(",", "."));
            double A = Double.parseDouble(txtAltura.getText().replace(",", "."));

            double espessuraPainel = cbEspessura.getValue() / 1000.0;

            boolean possuiPiso = chkPiso.isSelected();
            boolean isCongelado = rbCongelado.isSelected();

            // cálculo geométrico da câmara
            resultados = FormatoCalculator.calcular(
                    C, L, A, espessuraPainel, possuiPiso, portas, isCongelado
            );

            memorial = FormatoCalculator.gerarMemorial(
                    C,
                    L,
                    A,
                    cbEspessura.getValue(),
                    resultados
            );

            // definir tensão
            if (rb220Mono.isSelected()) {
                tensao = "220V_MONO";
            } else if (rb220Tri.isSelected()) {
                tensao = "220V_TRI";
            } else if (rb380Tri.isSelected()) {
                tensao = "380V_TRI";
            } else {
                tensao = "INDEFINIDO";
            }

            abrirTelaRefrigeracao();

        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Erro nos dados");
            alert.setContentText("Verifique os valores informados.");
            alert.showAndWait();
        }
    }

    // =========================================
    // VALIDAÇÃO
    // =========================================

    private void validarCampos() throws Exception {

        if (!rbCongelado.isSelected() && !rbResfriado.isSelected()) {
            throw new Exception("Selecione o tipo da câmara.");
        }

        if (txtComprimento.getText().isEmpty()
                || txtLargura.getText().isEmpty()
                || txtAltura.getText().isEmpty()
                || cbEspessura.getValue() == null) {

            throw new Exception("Campos obrigatórios não preenchidos");
        }
    }

    // =========================================
    // ABRIR TELA DE REFRIGERAÇÃO
    // =========================================

    private void abrirTelaRefrigeracao() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/usuario/refrigeracao.fxml")
            );

            Parent root = loader.load();

            RefrigeracaoUsuario controller = loader.getController();

            // enviar dados para a próxima tela
            controller.setUsuario(usuario);
            controller.setResultados(resultados);
            controller.setEspessura(cbEspessura.getValue());
            controller.setPortas(portas);
            controller.setTensao(tensao);

            controller.setCliente(txtCliente.getText());
            controller.setMemorial(memorial);

            double c = Double.parseDouble(txtComprimento.getText().replace(",", "."));
            double l = Double.parseDouble(txtLargura.getText().replace(",", "."));
            double a = Double.parseDouble(txtAltura.getText().replace(",", "."));

            controller.setDimensoes(c, l, a);

            tipoCamara = rbCongelado.isSelected() ? "CONGELADOS" : "RESFRIADOS";
            controller.setTipoCamara(tipoCamara);

            Stage stage = (Stage) btnAvancar.getScene().getWindow();
            stage.setScene(new Scene(root, 1150, 750));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void voltarPainel() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/usuario/painel-usuario.fxml")
            );

            Parent root = loader.load();

            PainelUsuario controller = loader.getController();
            controller.setUsuario(usuario);

            Stage stage = (Stage) btnAvancar.getScene().getWindow();
            stage.setScene(new Scene(root, 1150, 750));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}