package app.controller.vendedor;

import app.model.*;
import app.service.EvaporadoraService;
import app.service.FormatoCalculator;
import app.service.MaterialService;
import app.service.ProjetoService;
import app.util.NumeroUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

import javafx.stage.FileChooser;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;


public class ResultadoUsuario {

    // =============================
    // FXML
    // =============================

    @FXML public Label lblCusto;
    @FXML public Label lblVenda;
    @FXML public Button btnExportar;
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
    private double distQuadroUC;
    private double distQuadroEU;
    private double distUEUC;
    private String tensao;
    private double cargaKcal;
    double cargaComSeguranca = cargaKcal * 1.10;
    private Evaporadora evaporadora;
    private String memorial;

    public void setDadosRefrigeracao(double carga, int amb, double evap, String gas) {
        this.cargaNecessaria = carga;
        this.tempAmbiente = amb;
        this.tempEvap = evap;
        this.gas = gas;
    }

    public void setTensao(String tensao) {
        this.tensao = tensao;
        System.out.println("Tensao recebida: " + tensao);
        preencherTabela(); // 🔥 atualizar tabela
    }

    public void setMemorial(String memorial) {
        this.memorial = memorial;
    }

    public void setEquipamento(Equipamento equipamentoSelecionado) {
        this.equipamento = equipamentoSelecionado;
        preencherTabela(); // ✅ força atualizar tabela
    }

    public void setEvaporadora(Evaporadora evap) {
        this.evaporadora = evap;
    }

    public void setDistancias(double quadroUC, double quadroEU, double ueuc) {
        this.distQuadroUC = quadroUC;
        this.distQuadroEU = quadroEU;
        this.distUEUC = ueuc;
    }

    @FXML
    public void exportarPDF() {

        try {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Orçamento PDF");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Arquivo PDF", "*.pdf")
            );

            String nomeCliente = lblCliente.getText();

            if (nomeCliente == null || nomeCliente.trim().isEmpty()) {
                nomeCliente = "orcamento";
            }

            fileChooser.setInitialFileName("orcamento_" + nomeCliente + ".pdf");

            File file = fileChooser.showSaveDialog(btnVoltar.getScene().getWindow());

            if (file == null) return;

            gerarPDF(file);

            Desktop.getDesktop().open(file);

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
        public double getTotal() {return valor * quantidade;}
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

    public void setDimensoes(double c, double l, double a) {

        this.dimensoes =
                NumeroUtil.formatar(c) + " x " +
                        NumeroUtil.formatar(l) + " x " +
                        NumeroUtil.formatar(a);

        if (lblDimensoes != null) {
            lblDimensoes.setText(this.dimensoes);
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

        Map<String, Material> mapaMateriais = materiais.stream()
                .collect(Collectors.toMap(Material::getCodigo, m -> m));

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
        // PAINÉIS (TOTAL GERAL)
        // =========================
        if (painelMaterial != null) {

            double totalM2 = resultados.areaParedesM2 + resultados.areaTetoM2;

            if (resultados.requerPiso) {
                totalM2 += resultados.areaPisoM2;
            }

            lista.add(new ItemTabela(
                    "Painel",
                    "Painel PIR" + espessura + "mm",
                    (int) Math.ceil(totalM2),
                    "m²",
                    painelMaterial.getValor()
            ));
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
                    "Cantoneira externa 40x" + (espessura + 40) + "x3000",
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

        if (parafusoMaterial != null && !resultados.requerPiso) {
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
                    equipamento.getValor()
            ));

            // 🔹 GÁS (70% do tanque)
            double kgGas = equipamento.getTanqueLiquido() * 0.7;

            Material gasMaterial = mapaMateriais.get("GAS-" + equipamento.getGas());

            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Gás " + equipamento.getGas(),
                    (int) Math.ceil(kgGas),
                    "kg",
                    gasMaterial != null ? gasMaterial.getValor() : 0
            ));

            Evaporadora evap = this.evaporadora;

            if (evap != null) {
                lista.add(new ItemTabela(
                        "Refrigeração",
                        "Evaporadora " + evap.getCodigo() + " " + evap.getVentiladores() + "V",
                        1,
                        "un",
                        evap.getValor()
                ));

                int ventiladores = evap.getVentiladores();
                int numeroOrificio = Math.max(ventiladores - 1, 1);
                String codOrificio = "ORIF-N" + numeroOrificio;

                Material orificio = mapaMateriais.get(codOrificio);
                if (orificio != null) {
                    lista.add(new ItemTabela(
                            "Refrigeração",
                            orificio.getNome(),
                            1,
                            orificio.getUnidade(),
                            orificio.getValor()
                    ));
                }
            }

            // 🔹 LINHA DE LÍQUIDO → quadro → UC
            String codLiquido = "TC-" + equipamento.getLinhaLiquido();
            Material tuboLiquido = mapaMateriais.get(codLiquido);

            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Tubulação de Cobre Flexível " + equipamento.getLinhaLiquido(),
                    (int) Math.ceil(distUEUC), // agora usa somente distQuadroUC
                    "m",
                    tuboLiquido != null ? tuboLiquido.getValor() : 0
            ));

            // 🔹 LINHA DE SUCÇÃO → quadro → EU
            String codSuccao = "TC-" + equipamento.getLinhaSucção();
            Material tuboSuccao = mapaMateriais.get(codSuccao);

            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Tubulação de Cobre Flexível " + equipamento.getLinhaSucção(),
                    (int) Math.ceil(distUEUC), // agora usa somente distQuadroEU
                    "m",
                    tuboSuccao != null ? tuboSuccao.getValor() : 0
            ));

            // 🔹 ISOLAMENTO DA LINHA DE LIQUIDO
            String codIsolamento = "TUBO-" + equipamento.getLinhaLiquido();
            Material isolamento = mapaMateriais.get(codIsolamento);

            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Isolamento Elastomérico " + equipamento.getLinhaLiquido(),
                    (int) Math.ceil(distUEUC),
                    "m",
                    isolamento != null ? isolamento.getValor() : 0
            ));

            // 🔹 SIFÃO (sempre 1 unidade)
            String codSifao = "SIF-" + equipamento.getLinhaLiquido();

            Material sifao = mapaMateriais.get(codSifao);

            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Sifão de Cobre " + equipamento.getLinhaLiquido(),
                    1,
                    "un",
                    sifao != null ? sifao.getValor() : 0
            ));

            // 🔹 VÁLVULA SOLENOIDE
            String codVS = "VS-" + equipamento.getLinhaSucção();

            Material valvulaSolenoide = mapaMateriais.get(codVS);

            int qtdVS = 1;

            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Válvula Solenoide " + equipamento.getLinhaSucção(),
                    qtdVS,
                    "un",
                    valvulaSolenoide != null ? valvulaSolenoide.getValor() : 0
            ));

            // 🔹 BOBINA SOLENOIDE
            Material bobina = mapaMateriais.get("BOB-220");

            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Bobina Solenoide 220V",
                    qtdVS,
                    "un",
                    bobina != null ? bobina.getValor() : 0
            ));

            // 🔹 VÁLVULA DE EXPANSÃO
            String codVEX = "VEX-" + equipamento.getGas();

            Material valvulaExpansao = mapaMateriais.get(codVEX);

            int qtdVEX = 1; // 1 por evaporador

            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Válvula de Expansão " + equipamento.getGas(),
                    qtdVEX,
                    "un",
                    valvulaExpansao != null ? valvulaExpansao.getValor() : 0
            ));

            // 🔹 PORCA 1/4
            Material porca14 = mapaMateriais.get("PORC-014");
            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Porca de Flange 1/4",
                    1,
                    "un",
                    porca14 != null ? porca14.getValor() : 0
            ));

// 🔹 PORCA 3/8
            Material porca38 = mapaMateriais.get("PORC-038");
            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Porca de Flange 3/8",
                    3,
                    "un",
                    porca38 != null ? porca38.getValor() : 0
            ));

// 🔹 PORCA 1/2
            Material porca12 = mapaMateriais.get("PORC-012");
            lista.add(new ItemTabela(
                    "Refrigeração",
                    "Porca de Flange 1/2",
                    1,
                    "un",
                    porca12 != null ? porca12.getValor() : 0
            ));

            System.out.println("Gas equipamento: " + equipamento.getGas());

            String gasEq = equipamento.getGas().trim().toUpperCase();

            if (gasEq.equals("R404") || gasEq.equals("R404A")) {

                String bitola = equipamento.getLinhaSucção().trim();

                if (bitola.equals("3/8")) {
                    bitola = "1/2";
                }
                System.out.println("Bitola sucção: " + bitola);

                String codACU = "ACU-" + bitola;
                String codSEP = "SEP-" + bitola;

                System.out.println("Buscando: " + codACU);
                System.out.println("Buscando: " + codSEP);

                // ACUMULADOR
                Material acumulador = mapaMateriais.get(codACU);

                if (acumulador != null) {
                    lista.add(new ItemTabela(
                            "Refrigeração",
                            acumulador.getNome(),
                            1,
                            "un",
                            acumulador.getValor()
                    ));
                }

                // SEPARADOR DE ÓLEO
                Material separador = mapaMateriais.get(codSEP);

                if (separador != null) {
                    lista.add(new ItemTabela(
                            "Refrigeração",
                            separador.getNome(),
                            1,
                            "un",
                            separador.getValor()
                    ));
                }
            }

            // =========================
            // CABOS ELÉTRICOS
            // =========================

            Map<String, Integer> cabos = new HashMap<>();

            double distQuadroUC = this.distQuadroUC;
            double distQuadroEU = this.distQuadroEU;
            double distUEUC = this.distUEUC;

            // cria lógica elétrica baseada no equipamento
            Eletrica eletrica = new Eletrica(
                    equipamento.getModelo(),
                    true // ou pegar da tensão depois
            );

            // Compressor
            adicionarCabo(
                    cabos,
                    eletrica.getPernasCompressor(),
                    eletrica.getBitolaCompressor(),
                    distQuadroUC
            );
            adicionarCabo(
                    cabos,
                    eletrica.getPernasPressostato(),
                    eletrica.getBitolaPressostato(),
                    distQuadroUC
            );
            adicionarCabo(
                    cabos,
                    eletrica.getPernasSensor(),
                    eletrica.getBitolaSensor(),
                    distUEUC
            );
            adicionarCabo(
                    cabos,
                    eletrica.getPernasMotoventilador(),
                    eletrica.getBitolaMotoventilador(),
                    distQuadroEU
            );
            adicionarCabo(
                    cabos,
                    eletrica.getPernasSolenoide(),
                    eletrica.getBitolaSolenoide(),
                    distUEUC
            );

            for (Map.Entry<String, Integer> entry : cabos.entrySet()) {

                String codigo = entry.getKey();
                int metros = entry.getValue();

                Material cabo = mapaMateriais.get(codigo);

                if (cabo != null) {

                    lista.add(new ItemTabela(
                            "Elétrica",
                            cabo.getNome(),
                            metros,
                            cabo.getUnidade(),
                            cabo.getValor()
                    ));
                }
            }

            double area = resultados.areaPisoM2;

            int qtd = (int) Math.ceil(area / 10.0);

            Material lum120 = mapaMateriais.get("LUN-120");
            Material lum060 = mapaMateriais.get("LUN-060");

            if (area <= 2) {

                // 1 luminária 60cm
                if (lum060 != null) {
                    lista.add(new ItemTabela(
                            "Elétrica",
                            lum060.getNome(),
                            1,
                            lum060.getUnidade(),
                            lum060.getValor()
                    ));
                }

            } else {

                // luminárias 120cm
                if (lum120 != null) {
                    lista.add(new ItemTabela(
                            "Elétrica",
                            lum120.getNome(),
                            qtd,
                            lum120.getUnidade(),
                            lum120.getValor()
                    ));
                }
            }

            // =========================



            Material eletroduto = mapaMateriais.get("ELETRO-PVC01");
            Material condulete = mapaMateriais.get("CONDU-TAMP01");
            Material conector = mapaMateriais.get("CONEC-CON01");

            // quantidade de barras (3 metros cada)
            int barras = (int) Math.ceil(distQuadroUC / 3.0);

            if (barras < 1) barras = 1;

            // ELETRODUTO
            if (eletroduto != null) {
                lista.add(new ItemTabela(
                        "Elétrica",
                        eletroduto.getNome(),
                        barras,
                        eletroduto.getUnidade(),
                        eletroduto.getValor()
                ));
            }

            // CONDULETE (1 por barra)
            if (condulete != null) {
                lista.add(new ItemTabela(
                        "Elétrica",
                        condulete.getNome(),
                        barras,
                        condulete.getUnidade(),
                        condulete.getValor()
                ));
            }

            // CONECTOR (2 por barra)
            if (conector != null) {
                lista.add(new ItemTabela(
                        "Elétrica",
                        conector.getNome(),
                        barras * 2,
                        conector.getUnidade(),
                        conector.getValor()
                ));
            }

            // 🔹 QUADRO DE COMANDO
            double hp = equipamento.getHp();

            String codigoQuadro;

            if (hp <= 2) {
                codigoQuadro = "QC-1-2HP";
            }
            else if (hp <= 3) {
                codigoQuadro = "QC-2-3HP";
            }
            else if (hp <= 4) {
                codigoQuadro = "QC-3-4HP";
            }
            else if (hp <= 5) {
                codigoQuadro = "QC-4-5HP";
            }
            else if (hp <= 6) {
                codigoQuadro = "QC-6HP";
            }
            else {
                codigoQuadro = "QC-SOB-ENCOMENDA";
            }

            Material quadro = mapaMateriais.get(codigoQuadro);

            if (quadro != null) {

                String descricaoQuadro =
                        quadro.getNome() +
                                " - Câmara " + tipoCamara +
                                " - " + tensao;

                lista.add(new ItemTabela(
                        "Elétrica",
                        descricaoQuadro,
                        1,
                        quadro.getUnidade(),
                        quadro.getValor()
                ));
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
            controller.setResultados(resultados);
            controller.setEspessura(espessura);
            controller.setPortas(portas);
            controller.setTensao(tensao);

            controller.setCliente(lblCliente.getText());
            String[] partes = lblDimensoes.getText().split(" x ");

            double c = Double.parseDouble(partes[0].replace(",", "."));
            double l = Double.parseDouble(partes[1].replace(",", "."));
            double a = Double.parseDouble(partes[2].replace(",", "."));

            controller.setDimensoes(c, l, a);
            controller.setTipoCamara(lblTipo.getText());

            Stage stage = (Stage) btnVoltar.getScene().getWindow();
            stage.setScene(new Scene(root, 1150, 750));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void salvarProjeto() {

        try {

            File pdfTemp = File.createTempFile("orcamento_", ".pdf");

            gerarPDF(pdfTemp);

            String cliente = lblCliente.getText();
            String tipo = lblTipo.getText();
            String dimensoes = lblDimensoes.getText();

            double custo = NumeroUtil.converter(lblCusto.getText());
            double venda = NumeroUtil.converter(lblVenda.getText());

            ProjetoService service = new ProjetoService();

            service.salvarProjeto(
                    cliente,
                    tipo,
                    dimensoes,
                    custo,
                    venda,
                    pdfTemp
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Projeto salvo com sucesso!");
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gerarPDF(File file) throws Exception {

        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(file));
        doc.open();

        doc.add(new Paragraph("ORÇAMENTO DE CÂMARA FRIGORÍFICA\n\n"));

        doc.add(new Paragraph("Cliente: " + lblCliente.getText()));
        doc.add(new Paragraph("Tipo: " + lblTipo.getText()));
        doc.add(new Paragraph("Dimensões: " + lblDimensoes.getText()));
        doc.add(new Paragraph("\n"));

        PdfPTable tabela = new PdfPTable(4);
        tabela.setWidthPercentage(100);
        tabela.setWidths(new float[]{2f,6f,1f,1f});

        tabela.addCell("Item");
        tabela.addCell("Descrição");
        tabela.addCell("Qtd");
        tabela.addCell("Un");

        // PAINÉIS
        if (memorial != null && !memorial.isEmpty()) {

            String[] linhas = memorial.split("\n");

            for (String linha : linhas) {

                linha = linha.trim();

                if (linha.isEmpty()) continue;

                if (linha.contains("PAREDE") || linha.contains("TETO") || linha.contains("PISO"))
                    continue;

                try {

                    String[] partes = linha.split(" ");

                    int qtd = Integer.parseInt(partes[0]);

                    String medida = linha.substring(linha.indexOf(" ") + 1)
                            .replace("paineis", "")
                            .replace("painéis", "")
                            .trim()
                            .replace(" x ", "x");

                    tabela.addCell("Painel");
                    tabela.addCell("Painel PIR " + espessura + "mm " + medida);
                    tabela.addCell(String.valueOf(qtd));
                    tabela.addCell("un");

                } catch (Exception ignored) {}
            }
        }

        // RESTO DOS MATERIAIS
        for (ItemTabela item : tableMateriais.getItems()) {

            if (item.getItem().equalsIgnoreCase("Painel")) continue;

            tabela.addCell(item.getItem());
            tabela.addCell(item.getDescricao());
            tabela.addCell(String.valueOf(item.getQuantidade()));
            tabela.addCell(item.getUnidade());
        }

        doc.add(tabela);

        doc.close();
    }

    // Alterar método adicionarCabo
    private void adicionarCabo(Map<String, Integer> cabos, int vias, double bitola, double distancia) {
        // Multiplica a distância pelo número de vias
        double total = vias * distancia;

        // Arredonda para cima e garante mínimo de 1 metro
        int metros = (int) Math.ceil(total);
        if (metros < 1) metros = 1;

        String chave = "CABO-" + String.format("%.2f", bitola).replace(",", ".");

        // Adiciona ao mapa, somando se já existir
        cabos.merge(chave, metros, Integer::sum);
    }
}