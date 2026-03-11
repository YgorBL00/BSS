package app.controller.vendedor;

import app.model.Material;
import app.model.Usuario;
import app.service.FormatoCalculator;
import app.service.MaterialService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import app.model.Porta;

import java.util.ArrayList;
import java.util.List;

public class ResultadoUsuario {

    // =============================
    // FXML
    // =============================

    @FXML private Button btnVoltar;

    @FXML private Label lblCliente;
    @FXML private Label lblTipo;
    @FXML private Label lblDimensoes;

    @FXML private TableView<ItemTabela> tableMateriais;

    @FXML private TableColumn<ItemTabela, String> colItem;
    @FXML private TableColumn<ItemTabela, String> colDescricao;
    @FXML private TableColumn<ItemTabela, Integer> colQuantidade;
    @FXML private TableColumn<ItemTabela, String> colUnidade;
    @FXML private TableColumn<ItemTabela, Double> colValor;
    @FXML private TableColumn<ItemTabela, Double> colTotal;

    // =============================
    // VARIÁVEIS
    // =============================

    private Usuario usuario;
    private FormatoCalculator.ResultadoFormato resultados;

    private String cliente;
    private String tipoCamara;
    private String dimensoes;
    private int espessura;
    private MaterialService materialService = new MaterialService();

    // =============================
    // CLASSE DA TABELA
    // =============================

    public static class ItemTabela {

        private String item;
        private String descricao;
        private int quantidade;
        private String unidade;
        private double valor;

        public ItemTabela(String item, String descricao, int quantidade, String unidade, double valor) {
            this.item = item;
            this.descricao = descricao;
            this.quantidade = quantidade;
            this.unidade = unidade;
            this.valor = valor;
        }

        public String getItem() { return item; }
        public String getDescricao() { return descricao; }
        public int getQuantidade() { return quantidade; }
        public String getUnidade() { return unidade; }
        public double getValor() { return valor; }

        public double getTotal() {
            return valor * quantidade;
        }
    }

    public void carregarDados(
            Usuario usuario,
            FormatoCalculator.ResultadoFormato resultados,
            int espessura,
            String cliente,
            String tipo,
            String dimensoes,
            List<Porta> portas
    ) {

        this.usuario = usuario;
        this.resultados = resultados;
        this.espessura = espessura;
        this.portas = portas;

        lblCliente.setText(cliente);
        lblTipo.setText(tipo);
        lblDimensoes.setText(dimensoes);

        preencherTabela();
    }

    private List<Porta> portas = new ArrayList<>();

    public void setPortas(List<Porta> portas) {
        this.portas = portas;
    }

    // =============================
    // INITIALIZE
    // =============================

    @FXML
    public void initialize() {

        colItem.setCellValueFactory(new PropertyValueFactory<>("item"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colUnidade.setCellValueFactory(new PropertyValueFactory<>("unidade"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    // =============================
    // SETTERS
    // =============================

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;

        if (lblCliente != null) {
            lblCliente.setText(cliente);
        }
    }

    public void setEspessura(int espessura) {
        this.espessura = espessura;
    }

    public void setTipoCamara(String tipoCamara) {
        this.tipoCamara = tipoCamara;

        if (lblTipo != null) {
            lblTipo.setText(tipoCamara);
        }
    }

    public void setDimensoes(String dimensoes) {
        this.dimensoes = dimensoes;

        if (lblDimensoes != null) {
            lblDimensoes.setText(dimensoes);
        }
    }

    public void setResultados(FormatoCalculator.ResultadoFormato resultados) {
        this.resultados = resultados;

        preencherTabela();
    }

    private String gerarCodigoPorta(Porta p) {

        if (p.getTipo().equalsIgnoreCase("Expositora")) {
            return "PORTA_EXP";
        }

        String tipo = p.getTipo().contains("Giratória") ? "G" : "C";

        String tamanho = p.getTamanho()
                .replace(",", ".")
                .replace(" ", "")
                .replace(".", "")
                .replace("x", "");

        String aplicacao = p.getTipo().contains("Resfriados") ? "R" : "F";

        String batente = p.getBatente();

        String abertura;

        switch (p.getAbertura()) {
            case "Direita": abertura = "D"; break;
            case "Esquerda": abertura = "E"; break;
            default: abertura = "REV";
        }

        return "PORTA_" + tipo + "_" + tamanho + "_" + aplicacao + "_" + batente + "_" + abertura;
    }

    // =============================
    // PREENCHER TABELA
    // =============================

    private void preencherTabela() {

        if (resultados == null) {
            return;
        }

        ObservableList<ItemTabela> lista = FXCollections.observableArrayList();

        double alturaParede = resultados.alturaParedeReal;

        double valorPainel = 180;
        double valorPerfil = 90;
        double valorCantoneira = 75;
        double valorPU = 25;
        double valorRebite = 0.20;
        double valorParafuso = 0.30;

        // =========================
        // PAREDE
        // =========================

        lista.add(new ItemTabela(
                "Painel",
                "Painel " + espessura + "mm 1,15x" + String.format("%.2f", alturaParede) + " - PAREDE",
                resultados.paineisParede,
                "un",
                valorPainel
        ));

        // =========================
        // TETO
        // =========================

        lista.add(new ItemTabela(
                "Painel",
                "Painel " + espessura + "mm 1,15x" + String.format("%.2f", resultados.alturaTetoReal) + " - TETO",
                resultados.paineisTeto,
                "un",
                valorPainel
        ));

        // =========================
        // PISO
        // =========================

        if (resultados.requerPiso) {

            lista.add(new ItemTabela(
                    "Painel",
                    "Painel " + espessura + "mm 1,15x" + String.format("%.2f", resultados.alturaPisoReal) + " - PISO",
                    resultados.paineisPiso,
                    "un",
                    valorPainel
            ));
        }

        String perfilUDescricao = "Perfil U 40x" + espessura + "x40x3000";

        String cantoneiraExternaDescricao;

        switch (espessura) {

            case 50:
                cantoneiraExternaDescricao = "Cantoneira externa 40x90x3000";
                break;

            case 70:
                cantoneiraExternaDescricao = "Cantoneira externa 40x120x3000";
                break;

            case 100:
                cantoneiraExternaDescricao = "Cantoneira externa 40x140x3000";
                break;

            case 120:
                cantoneiraExternaDescricao = "Cantoneira externa 40x160x3000";
                break;

            case 150:
                cantoneiraExternaDescricao = "Cantoneira externa 40x190x3000";
                break;

            default:
                cantoneiraExternaDescricao = "Cantoneira externa";
        }

        // =========================
        // PERFIL
        // =========================

        lista.add(new ItemTabela(
                "Perfil",
                perfilUDescricao,
                resultados.perfilU,
                "barra",
                valorPerfil
        ));

        // =========================
        // CANTONEIRA EXTERNA
        // =========================

        lista.add(new ItemTabela(
                "Cantoneira",
                cantoneiraExternaDescricao,
                resultados.cantoneiraExterna,
                "barra",
                valorCantoneira
        ));

        // =========================
        // PU
        // =========================

        lista.add(new ItemTabela(
                "PU",
                "Sachê PU 40 (600ml)",
                resultados.sachePU,
                "un",
                valorPU
        ));

        // =========================
        // CANTONEIRA INTERNA
        // =========================

        lista.add(new ItemTabela(
                "Cantoneira",
                "Cantoneira interna 40x40x3000",
                resultados.cantoneiraInterna,
                "barra",
                valorCantoneira
        ));

        // =========================
        // FIXAÇÃO
        // =========================

        lista.add(new ItemTabela(
                "Fixação",
                "Rebite 3.2x12",
                resultados.rebites,
                "un",
                valorRebite
        ));

        lista.add(new ItemTabela(
                "Fixação",
                "Parafuso Nº8",
                resultados.parafusos,
                "un",
                valorParafuso
        ));

        // =========================
// PORTAS
// =========================

        if (portas != null && !portas.isEmpty()) {

            List<Material> materiais = materialService.buscarTodos();

            for (Porta p : portas) {

                String codigo = gerarCodigoPorta(p);

                Material materialPorta = materiais.stream()
                        .filter(m -> m.getCodigo().equals(codigo))
                        .findFirst()
                        .orElse(null);

                if (materialPorta != null) {

                    lista.add(new ItemTabela(
                            "Porta",
                            materialPorta.getNome(),
                            1,
                            materialPorta.getUnidade(),
                            materialPorta.getValor()
                    ));

                } else {

                    lista.add(new ItemTabela(
                            "Porta",
                            "⚠ Porta não encontrada: " + codigo,
                            1,
                            "un",
                            0
                    ));
                }
            }
        }

        tableMateriais.setItems(lista);
    }

    // =============================
    // VOLTAR
    // =============================

    @FXML
    public void voltarRefrigeracao() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/usuario/refrigeracao.fxml")
            );

            Parent root = loader.load();

            RefrigeracaoUsuario controller = loader.getController();
            controller.setUsuario(usuario);

            Stage stage = (Stage) btnVoltar.getScene().getWindow();
            stage.setScene(new Scene(root, 1150, 750));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}