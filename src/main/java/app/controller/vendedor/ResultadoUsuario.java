package app.controller.vendedor;

import app.model.Equipamento;
import app.model.Material;
import app.model.Usuario;
import app.service.FormatoCalculator;
import app.service.MaterialService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import app.model.Porta;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;
import java.io.File;


public class ResultadoUsuario {

    // =============================
    // FXML
    // =============================

    @FXML public Label lblCusto;
    @FXML public Label lblVenda;
    public Button btnExportar;
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
    private Equipamento equipamento;





    public void setDadosRefrigeracao(double carga, int amb, double evap, String gas) {
        this.cargaNecessaria = carga;
        this.tempAmbiente = amb;
        this.tempEvap = evap;
        this.gas = gas;
    }

    public void setEquipamento(Equipamento equipamentoSelecionado) {
        this.equipamento = equipamentoSelecionado;
        preencherTabela(); // ✅ força atualizar tabela
    }

    @FXML
    public void exportarPDF() {

        try {

            // =========================
            // ABRIR EXPLORADOR (SALVAR COMO)
            // =========================
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Salvar Orçamento PDF");

            fileChooser.getExtensionFilters().add(
                    new javafx.stage.FileChooser.ExtensionFilter("Arquivo PDF", "*.pdf")
            );

            fileChooser.setInitialFileName("orcamento_" + lblCliente.getText() + ".pdf");

            java.io.File file = fileChooser.showSaveDialog(btnVoltar.getScene().getWindow());

            // se cancelar, sai
            if (file == null) {
                return;
            }

            String caminho = file.getAbsolutePath();

            // =========================
            // CRIAR PDF
            // =========================
            com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new java.io.FileOutputStream(caminho));

            doc.open();

            // =========================
            // TÍTULO
            // =========================
            doc.add(new com.itextpdf.text.Paragraph("ORÇAMENTO DE CÂMARA FRIGORÍFICA\n\n"));

            // =========================
            // DADOS DO CLIENTE
            // =========================
            doc.add(new com.itextpdf.text.Paragraph("Cliente: " + lblCliente.getText()));
            doc.add(new com.itextpdf.text.Paragraph("Tipo: " + lblTipo.getText()));
            doc.add(new com.itextpdf.text.Paragraph("Dimensões: " + lblDimensoes.getText()));
            doc.add(new com.itextpdf.text.Paragraph("\n"));

            // =========================
            // TABELA (SEM VALORES)
            // =========================
            com.itextpdf.text.pdf.PdfPTable tabela = new com.itextpdf.text.pdf.PdfPTable(3);
            tabela.setWidthPercentage(100);

            tabela.addCell("Item");
            tabela.addCell("Descrição");
            tabela.addCell("Qtd");

            for (ItemTabela item : tableMateriais.getItems()) {
                tabela.addCell(item.getItem());
                tabela.addCell(item.getDescricao());
                tabela.addCell(String.valueOf(item.getQuantidade()));
            }

            doc.add(tabela);

            doc.close();

            // =========================
            // ABRIR PDF AUTOMATICAMENTE
            // =========================
            java.awt.Desktop.getDesktop().open(file);

            // =========================
            // ALERTA
            // =========================
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF gerado");
            alert.setHeaderText(null);
            alert.setContentText("PDF salvo em:\n" + caminho);
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        private Equipamento equipamentoSelecionado;
        private Equipamento equipamento;


        public ItemTabela(String item, String descricao, int quantidade, String unidade, double valor) {
            this.item = item;
            this.descricao = descricao;
            this.quantidade = quantidade;
            this.unidade = unidade;
            this.valor = valor;
        }

        public void setEquipamento(Equipamento equipamento) {this.equipamento = equipamento;}
        public String getItem() { return item; }
        public String getDescricao() { return descricao; }
        public int getQuantidade() { return quantidade; }
        public String getUnidade() { return unidade; }
        public double getValor() { return valor; }
        public double getTotal() {
            return valor * quantidade;
        }}



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

        MaterialService materialService = new MaterialService();
        List<Material> materiais = materialService.buscarTodos();

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
        } // ✅ FECHA AQUI

        // =========================
        // EQUIPAMENTO (FORA DO IF)
        // =========================
        if (equipamento != null) {

            // 🔹 UNIDADE CONDENSADORA
            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Unidade Condensadora " + equipamento.getModelo(),
                    1,
                    "un",
                    0
            ));

            // 🔹 GÁS (70% do tanque)
            double kgGas = equipamento.getTanqueLiquido() * 0.7;

            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Gás " + equipamento.getGas(),
                    (int) Math.ceil(kgGas),
                    "kg",
                    0
            ));

            // 🔹 LINHA DE LÍQUIDO (5m padrão)
            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Tubulução de Cobre Flexivel " + equipamento.getLinhaLiquido(),
                    5,
                    "m",
                    0
            ));

            // 🔹 LINHA DE SUCÇÃO (5m padrão)
            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Tubulução de Cobre Flexivel " + equipamento.getLinhaSucção(),
                    5,
                    "m",
                    0
            ));
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