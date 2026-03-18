package app.controller.vendedor;

import app.model.Material;
import app.model.ModeloResultado;
import app.model.Usuario;
import app.service.FormatoCalculator;
import app.service.MaterialService;
import app.service.ModeloService;
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
import app.service.CacheSistema;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import java.util.ArrayList;
import java.util.List;

public class ResultadoUsuario {

    // =============================
    // FXML
    // =============================

    @FXML public Label lblCusto;
    @FXML public Label lblVenda;
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
    private double cargaNecessaria;
    private int tempAmbiente;
    private double tempEvap;
    private String gas;

    public void setDadosRefrigeracao(double carga, int amb, double evap, String gas) {
        this.cargaNecessaria = carga;
        this.tempAmbiente = amb;
        this.tempEvap = evap;
        this.gas = gas;
    }

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

        // Formatter brasileiro
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

        colValor.setCellFactory(tc -> new TableCell<ItemTabela, Double>() {
            @Override
            protected void updateItem(Double valor, boolean empty) {
                super.updateItem(valor, empty);
                if (empty || valor == null) {
                    setText(null);
                } else {
                    setText("R$ " + df.format(valor));
                }
            }
        });

        // Coluna total
        colTotal.setCellFactory(tc -> new TableCell<ItemTabela, Double>() {
            @Override
            protected void updateItem(Double valor, boolean empty) {
                super.updateItem(valor, empty);
                if (empty || valor == null) {
                    setText(null);
                } else {
                    setText("R$ " + df.format(valor));
                }
            }
        });

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

        if (resultados == null) return;

        ObservableList<ItemTabela> lista = FXCollections.observableArrayList();

        List<Material> materiais = CacheSistema.getMateriais();

        // =========================
        // VALORES BASE PELO BANCO
        // =========================
        Material painelMaterial = materiais.stream()
                .filter(m -> m.getCodigo().startsWith("PAINEL_" + espessura))
                .findFirst()
                .orElse(null);

        Material perfilMaterial = materiais.stream()
                .filter(m -> m.getCodigo().startsWith("PERFIL_U_" + espessura))
                .findFirst()
                .orElse(null);

        Material cantExtMaterial = materiais.stream()
                .filter(m -> m.getCodigo().contains("CANT_EXT"))
                .findFirst()
                .orElse(null);

        Material cantIntMaterial = materiais.stream()
                .filter(m -> m.getCodigo().contains("CANT_INT"))
                .findFirst()
                .orElse(null);

        Material puMaterial = materiais.stream()
                .filter(m -> m.getCodigo().startsWith("PU_"))
                .findFirst()
                .orElse(null);

        Material rebiteMaterial = materiais.stream()
                .filter(m -> m.getCodigo().startsWith("REBITE"))
                .findFirst()
                .orElse(null);

        Material parafusoMaterial = materiais.stream()
                .filter(m -> m.getCodigo().startsWith("PARAFUSO"))
                .findFirst()
                .orElse(null);

        // =========================
        // PAINÉIS
        // =========================
        if (painelMaterial != null) {
            // Paredes
            double valorParede = painelMaterial.getValor() * resultados.areaParedesM2;
            lista.add(new ItemTabela(
                    "Painel",
                    "Painel PIR " + espessura + "mm - PAREDE",
                    (int) Math.ceil(resultados.areaParedesM2),
                    "m²",
                    painelMaterial.getValor()
            ));

            // Teto
            double valorTeto = painelMaterial.getValor() * resultados.areaTetoM2;
            lista.add(new ItemTabela(
                    "Painel",
                    "Painel PIR " + espessura + "mm - TETO",
                    (int) Math.ceil(resultados.areaTetoM2),
                    "m²",
                    painelMaterial.getValor()
            ));

            // Piso
            if (resultados.requerPiso) {
                double valorPiso = painelMaterial.getValor() * resultados.areaPisoM2;
                lista.add(new ItemTabela(
                        "Painel",
                        "Painel PIR " + espessura + "mm - PISO",
                        (int) Math.ceil(resultados.areaPisoM2),
                        "m²",
                        painelMaterial.getValor()
                ));
            }
        }

        // =========================
        // PERFIL
        // =========================
        if (perfilMaterial != null) {
            lista.add(new ItemTabela(
                    "Perfil",
                    "Perfil U 40x" + espessura + "x40x3000",
                    resultados.perfilU,
                    "barra",
                    perfilMaterial.getValor()
            ));
        }

        // =========================
        // CANTONEIRAS
        // =========================
        if (cantExtMaterial != null) {
            lista.add(new ItemTabela(
                    "Cantoneira",
                    "Cantoneira externa 40x" + espessura + "x3000",
                    resultados.cantoneiraExterna,
                    "barra",
                    cantExtMaterial.getValor()
            ));
        }

        if (cantIntMaterial != null) {
            lista.add(new ItemTabela(
                    "Cantoneira",
                    "Cantoneira interna 40x40x3000",
                    resultados.cantoneiraInterna,
                    "barra",
                    cantIntMaterial.getValor()
            ));
        }

        // =========================
        // PU
        // =========================
        if (puMaterial != null) {
            lista.add(new ItemTabela(
                    "PU",
                    "Sachê PU 40 (600ml)",
                    resultados.sachePU,
                    "un",
                    puMaterial.getValor()
            ));
        }

        // =========================
        // FIXAÇÃO
        // =========================
        if (rebiteMaterial != null) {
            lista.add(new ItemTabela(
                    "Fixação",
                    "Rebite 3.2x12",
                    resultados.rebites,
                    "un",
                    rebiteMaterial.getValor()
            ));
        }

        if (parafusoMaterial != null) {
            lista.add(new ItemTabela(
                    "Fixação",
                    "Parafuso Nº8 C/ Bucha e Arruela",
                    resultados.parafusos,
                    "un",
                    parafusoMaterial.getValor()
            ));
        }

        // =========================
        // PORTAS
        // =========================
        if (portas != null && !portas.isEmpty()) {
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

        // =============================
        // CALCULAR CUSTO E VENDA
        // =============================
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

        double custoTotal = lista.stream().mapToDouble(ItemTabela::getTotal).sum();
        lblCusto.setText("R$ " + df.format(custoTotal));

        double venda = custoTotal * 1.12 * 1.30;
        lblVenda.setText("R$ " + df.format(venda));

        ModeloService modeloService = new ModeloService();

        ModeloResultado modelo = modeloService.buscarModeloIdeal(
                cargaNecessaria,
                tempAmbiente,
                tempEvap,
                gas
        );

        // =========================
        // REFRIGERAÇÃO (MODELO)
        // =========================

        // 🔹 EXEMPLO FIXO (depois vira dinâmico)
        String sucao = modelo.getSucao();
        String liquido = modelo.getLiquido();
        double tanque = modelo.getTanque();
        String gasSelecionado = modelo.getGas();


        if (modelo != null) {
            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Unidade condensadora " + modelo,
                    1,
                    "un",
                    0 // depois você pode puxar valor do banco
            ));
        } else {
            lista.add(new ItemTabela(
                    "Refrigeração",
                    "⚠ Nenhum modelo encontrado",
                    1,
                    "un",
                    0
            ));
        }

// 🔹 BUSCAR MATERIAIS
        Material tuboSucMaterial = materiais.stream()
                .filter(m -> m.getCodigo().equals("TC-" + sucao))
                .findFirst()
                .orElse(null);

        Material tuboLiqMaterial = materiais.stream()
                .filter(m -> m.getCodigo().equals("TC-" + liquido))
                .findFirst()
                .orElse(null);

        Material gasMaterial = materiais.stream()
                .filter(m -> m.getCodigo().contains(gasSelecionado))
                .findFirst()
                .orElse(null);

// =========================
// TUBO SUCÇÃO
// =========================
        if (tuboSucMaterial != null) {
            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Linha de sucção " + sucao,
                    5,
                    tuboSucMaterial.getUnidade(),
                    tuboSucMaterial.getValor()
            ));
        }

// =========================
// TUBO LÍQUIDO
// =========================
        if (tuboLiqMaterial != null) {
            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Linha de líquido " + liquido,
                    5,
                    tuboLiqMaterial.getUnidade(),
                    tuboLiqMaterial.getValor()
            ));
        }

// =========================
// GÁS
// =========================
        double kgGas = tanque * 0.8;

        if (gasMaterial != null) {
            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Carga de gás " + gas,
                    (int) Math.ceil(kgGas),
                    gasMaterial.getUnidade(),
                    gasMaterial.getValor()
            ));
        }
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