package app.controller.vendedor;

import app.model.Material;
import app.model.Porta;
import app.model.Usuario;
import app.service.FormatoCalculator;
import app.service.MaterialService;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class CaixoteUsuario {

    private static class ItemResumo {

        int id;
        String nome;
        int quantidade;
        double valor;

        ItemResumo(Material m, int qtd) {
            this.id = m.getId();
            this.nome = m.getNome();
            this.quantidade = qtd;
            this.valor = m.getValor();
        }

        double getTotal() {
            return quantidade * valor;
        }
    }

    // =============================
    // CAMPOS FXML
    // =============================

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

    @FXML private CheckBox chkFrente;
    @FXML private CheckBox chkAtras;
    @FXML private CheckBox chkLadoEsquerdo;
    @FXML private CheckBox chkLadoDireito;

    @FXML private Button btnAvancar;

    private Map<String, Material> materiaisPorCodigo = new HashMap<>();


    // =============================
    // VARIÁVEIS
    // =============================

    private Stage stage;
    private Usuario usuario;

    private List<Porta> portas = new ArrayList<>();


    // =============================
    // SETTERS
    // =============================

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    // =============================
    // INITIALIZE
    // =============================

    private void carregarMateriais() {

        MaterialService service = new MaterialService();

        for (Material m : service.buscarTodos()) {
            materiaisPorCodigo.put(m.getCodigo(), m);
        }
    }

    @FXML
    public void initialize() {

        ToggleGroup grupoCamara = new ToggleGroup();
        rbCongelado.setToggleGroup(grupoCamara);
        rbResfriado.setToggleGroup(grupoCamara);

        cbEspessura.getItems().addAll(50, 70, 100, 120, 150);

        spCantoSemAcabamento.getItems().addAll("Não", "Sim");
        spCantoSemAcabamento.setValue("Não");

        carregarMateriais(); // <<< AQUI

        spCantoSemAcabamento.valueProperty().addListener((obs, oldVal, newVal) -> {

            boolean mostrar = "Sim".equals(newVal);

            boxCantos.setVisible(mostrar);
            boxCantos.setManaged(mostrar);

        });
    }

    private Material buscarMaterialPorCodigo(String codigo) {
        return materiaisPorCodigo.get(codigo);
    }

    // =============================
    // POPUP CONFIG PORTAS
    // =============================

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

    // =============================
    // CALCULAR
    // =============================

    @FXML
    private void avancar() {

        try {

            validarCampos();

            double C = Double.parseDouble(txtComprimento.getText().replace(",", "."));
            double L = Double.parseDouble(txtLargura.getText().replace(",", "."));
            double A = Double.parseDouble(txtAltura.getText().replace(",", "."));

            double E = cbEspessura.getValue() / 1000.0;

            boolean possuiPiso = chkPiso.isSelected();

            var resultados = FormatoCalculator.calcular(C, L, A, E, possuiPiso, portas);

            abrirTelaRefrigeracao(resultados);

            //imprimirResultados(resultados);

        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Erro nos dados");
            alert.setContentText("Verifique os valores informados.");
            alert.showAndWait();
        }
    }


    // =============================
    // VALIDAÇÃO
    // =============================

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


    // =============================
    // RESULTADOS
    // =============================

    private void imprimirResultados(FormatoCalculator.ResultadoFormato resultados) {


        List<ItemResumo> resumo = new ArrayList<>();

        String plural = resultados.paineisParede > 1 ? "Paineis" : "Painel";

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

        String tipoCamara = rbCongelado.isSelected() ? "CONGELADOS" : "RESFRIADOS";

        System.out.println("\nCLIENTE: " + txtCliente.getText());
        System.out.println("TIPO CÂMARA: " + tipoCamara);

        System.out.println("\n===== LISTA DE MATERIAIS =====");

        // =============================
        // PAREDES
        // =============================

        System.out.println(resultados.paineisParede +
                " Paineis " + espessura +
                "mm 1,15x" +
                String.format("%.2f", resultados.alturaParedeReal) +
                " - PAREDE");

        imprimirRecortes(resultados.recortesParede, espessura, "parede");

        // =============================
        // TETO
        // =============================

        // =============================
// TETO
// =============================

        int recortesTeto = resultados.recortesTeto.size();
        int paineisInteirosTeto = resultados.paineisTeto - recortesTeto;

        for (int i = 0; i < paineisInteirosTeto; i++) {

            System.out.println(
                    "1 Paineis " + espessura +
                            "mm 1,15x" +
                            String.format("%.2f", resultados.alturaTetoReal) +
                            " - TETO"
            );
        }

        for (FormatoCalculator.Recorte r : resultados.recortesTeto) {

            System.out.println(
                    "1 Paineis " + espessura +
                            "mm 1,15x" +
                            String.format("%.2f", resultados.alturaTetoReal) +
                            " - TETO"
            );

            System.out.println(
                    "1 Paineis " + espessura +
                            "mm " +
                            String.format("%.2f", r.largura) +
                            "x" +
                            String.format("%.2f", r.altura) +
                            " - recorte teto"
            );
        }

        // =============================
        // PISO
        // =============================

        // =============================
        // PISO
        // =============================

        if (resultados.requerPiso) {

            int recortesPiso = resultados.recortesPiso.size();
            int paineisInteirosPiso = resultados.paineisPiso - recortesPiso;

            for (int i = 0; i < paineisInteirosPiso; i++) {

                System.out.println(
                        "1 Paineis " + espessura +
                                "mm 1,15x" +
                                String.format("%.2f", resultados.alturaPisoReal) +
                                " - PISO"
                );
            }

            for (FormatoCalculator.Recorte r : resultados.recortesPiso) {

                System.out.println(
                        "1 Paineis " + espessura +
                                "mm 1,15x" +
                                String.format("%.2f", resultados.alturaPisoReal) +
                                " - PISO"
                );

                System.out.println(
                        "1 Paineis " + espessura +
                                "mm " +
                                String.format("%.2f", r.largura) +
                                "x" +
                                String.format("%.2f", r.altura) +
                                " - recorte piso"
                );
            }
        }

        // =============================
        // PORTAS
        // =============================

        Map<String, Integer> agrupado = new HashMap<>();

        for (Porta p : portas) {

            String codigo = gerarCodigoPorta(p);

            Material materialPorta = buscarMaterialPorCodigo(codigo);

            if (materialPorta != null) {

                resumo.add(new ItemResumo(materialPorta, 1));

                System.out.println("1 " + materialPorta.getNome());

            } else {

                System.out.println("⚠ Porta não encontrada no BD: " + codigo);
            }
        }

        for (Map.Entry<String, Integer> item : agrupado.entrySet()) {

            System.out.println(
                    item.getValue() +
                            " Porta frigorífica " +
                            item.getKey()
            );
        }

        // =============================
        // PU
        // =============================
        Material pu = buscarMaterialPorCodigo("PU_400");

        if (pu != null) {
            resumo.add(new ItemResumo(pu, resultados.sachePU));
        }

        System.out.println(
                resultados.sachePU +
                        " Sachês PU 40 (600ml) - vedação das juntas (" +
                        String.format("%.2f", resultados.metrosJuntaPU) +
                        " m de junta)"
        );

        // =============================
        // ACABAMENTOS
        // =============================

        Material cantInterna = buscarMaterialPorCodigo("CANT_INT");

        if (cantInterna != null) {
            resumo.add(new ItemResumo(cantInterna, resultados.cantoneiraInterna));
        }

        System.out.println(
                resultados.cantoneiraInterna +
                        " Cantoneira interna 40x40x3000"
        );

        Material cantExterna = null;

        switch (espessura) {

            case 50:
                cantExterna = buscarMaterialPorCodigo("CANT_EXT_90");
                break;

            case 70:
                cantExterna = buscarMaterialPorCodigo("CANT_EXT_120");
                break;

            case 100:
                cantExterna = buscarMaterialPorCodigo("CANT_EXT_140");
                break;

            case 120:
                cantExterna = buscarMaterialPorCodigo("CANT_EXT_160");
                break;

            case 150:
                cantExterna = buscarMaterialPorCodigo("CANT_EXT_190");
                break;
        }

        if (cantExterna != null) {
            resumo.add(new ItemResumo(cantExterna, resultados.cantoneiraExterna));
        }

        System.out.println(
                resultados.cantoneiraExterna +
                        " Cantoneira externa " +
                        cantoneiraExternaDescricao
        );

        Material perfil = null;

        switch (espessura) {

            case 50:
                perfil = buscarMaterialPorCodigo("PERFIL_U_50");
                break;

            case 70:
                perfil = buscarMaterialPorCodigo("PERFIL_U_70");
                break;

            case 100:
                perfil = buscarMaterialPorCodigo("PERFIL_U_100");
                break;

            case 120:
                perfil = buscarMaterialPorCodigo("PERFIL_U_120");
                break;

            case 150:
                perfil = buscarMaterialPorCodigo("PERFIL_U_150");
                break;
        }

        if (perfil != null) {
            resumo.add(new ItemResumo(perfil, resultados.perfilU));
        }

        if (resultados.perfilU > 0) {

            System.out.println(
                    resultados.perfilU +
                            " Perfil U " +
                            perfilUDescricao
            );
        }

        // =============================
        // FIXAÇÃO
        // =============================

        Material rebite = buscarMaterialPorCodigo("REBITE_312");

        if (rebite != null) {
            resumo.add(new ItemResumo(rebite, resultados.rebites));
        }

        System.out.println(resultados.rebites + " Rebite 3.2x12");

        Material parafuso = buscarMaterialPorCodigo("PARAFUSO_N8");

        if (parafuso != null) {
            resumo.add(new ItemResumo(parafuso, resultados.parafusos));
        }

        if (resultados.parafusos > 0) {

            System.out.println(
                    resultados.parafusos +
                            " Parafuso n°8 c/ bucha e arruela 3/8"
            );
        }

        for (Integer frameQtd : resultados.framesExpositor) {

            String codigoFrame = "FRAME_" + frameQtd;

            Material frame = buscarMaterialPorCodigo(codigoFrame);

            if (frame != null) {

                resumo.add(new ItemResumo(frame, 1));

            } else {

                System.out.println("⚠ Frame não encontrado no BD: " + codigoFrame);
            }

            System.out.println(
                    "1 Frame expositor para " +
                            frameQtd +
                            " porta(s)"
            );
        }

        System.out.println("\n===== RESUMO TÉCNICO (BD) =====");

        double totalGeral = 0;

        System.out.printf("%-5s %-35s %-6s %-10s %-10s\n",
                "ID", "MATERIAL", "QTD", "V.UNIT", "TOTAL");

        for (ItemResumo item : resumo) {

            double total = item.getTotal();
            totalGeral += total;

            System.out.printf("%-5d %-35s %-6d %-10.2f %-10.2f\n",
                    item.id,
                    item.nome,
                    item.quantidade,
                    item.valor,
                    total
            );
        }

        System.out.println("--------------------------------------------");
        System.out.printf("TOTAL GERAL: R$ %.2f\n", totalGeral);

        System.out.println("===============================");
    }


    // =============================
    // RECORTES
    // =============================

    private void imprimirRecortes(List<FormatoCalculator.Recorte> recortes, int espessura, String tipo) {

        Map<String, Integer> agrupado = new LinkedHashMap<>();

        for (FormatoCalculator.Recorte r : recortes) {

            String chave =
                    String.format("%.2f", r.largura) + "x" +
                            String.format("%.2f", r.altura);

            agrupado.put(chave, agrupado.getOrDefault(chave, 0) + 1);
        }

        for (Map.Entry<String, Integer> item : agrupado.entrySet()) {

            System.out.println(
                    item.getValue() +
                            " Paineis " + espessura +
                            "mm " + item.getKey() +
                            " - recorte " + tipo
            );
        }
    }
    private String gerarCodigoPorta(Porta p) {

        // ================================
        // PORTA EXPOSITOR
        // ================================

        if (p.getTipo().equalsIgnoreCase("Expositora")) {
            return "PORTA_EXP";
        }

        // ================================
        // TIPO
        // ================================

        String tipo;

        if (p.getTipo().contains("Giratória")) {
            tipo = "G";
        } else {
            tipo = "C";
        }

        // ================================
        // TAMANHO
        // ================================

        String tamanho = p.getTamanho()
                .replace(",", ".")   // 1,20 -> 1.20
                .replace(" ", "")    // remove espaços
                .replace(".", "")    // remove ponto
                .replace("x", "");   // remove o X
        // exemplo: 1.20x2.00 -> 120200

        // ================================
        // APLICAÇÃO
        // ================================

        String aplicacao;

        if (p.getTipo().contains("Resfriados")) {
            aplicacao = "R";
        } else {
            aplicacao = "F";
        }

        // ================================
        // BATENTE
        // ================================

        String batente = p.getBatente();

        // ================================
        // ABERTURA
        // ================================

        String abertura;

        switch (p.getAbertura()) {

            case "Direita":
                abertura = "D";
                break;

            case "Esquerda":
                abertura = "E";
                break;

            default:
                abertura = "REV";
        }

        return "PORTA_" +
                tipo + "_" +
                tamanho + "_" +
                aplicacao + "_" +
                batente + "_" +
                abertura;
    }

    private void abrirTelaRefrigeracao(FormatoCalculator.ResultadoFormato resultados) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/usuario/refrigeracao.fxml")
            );

            Parent root = loader.load();

            RefrigeracaoUsuario controller = loader.getController();

            controller.setUsuario(usuario);
            controller.setResultados(resultados);
            controller.setEspessura(cbEspessura.getValue());
            controller.setPortas(portas);

            controller.setCliente(txtCliente.getText());
            controller.setTipoCamara(rbCongelado.isSelected() ? "CONGELADOS" : "RESFRIADOS");
            controller.setDimensoes(
                    txtComprimento.getText() + " x " +
                            txtLargura.getText() + " x " +
                            txtAltura.getText()
            );

            Stage stage = (Stage) btnAvancar.getScene().getWindow();
            stage.setScene(new Scene(root, 1150, 750));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//    private void abrirTelaResultado(FormatoCalculator.ResultadoFormato resultados) {
//
//        try {
//
//            FXMLLoader loader = new FXMLLoader(
//                    getClass().getResource("/app/usuario/resultado.fxml")
//            );
//
//            Parent root = loader.load();
//
//            ResultadoUsuario controller = loader.getController();
//
//            controller.setUsuario(usuario);
//            controller.setResultados(resultados);
//            controller.setEspessura(cbEspessura.getValue());
//
//            controller.setCliente(txtCliente.getText());
//            controller.setTipoCamara(rbCongelado.isSelected() ? "CONGELADOS" : "RESFRIADOS");
//            controller.setDimensoes(
//                    txtComprimento.getText() + " x " +
//                            txtLargura.getText() + " x " +
//                            txtAltura.getText()
//            );
//
//            Stage stage = (Stage) btnAvancar.getScene().getWindow();
//            stage.setScene(new Scene(root));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
